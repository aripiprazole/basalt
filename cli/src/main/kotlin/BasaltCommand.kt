/*
 *    Copyright 2021 Gabrielle Guimarães de Oliveira
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package basalt.cli.host

import andesite.java.server.createJavaServer
import andesite.protocol.java.v756.v756
import andesite.protocol.misc.Chat
import andesite.protocol.misc.UuidSerializer
import andesite.protocol.serialization.MinecraftCodec
import andesite.world.Location
import andesite.world.World
import andesite.world.anvil.readAnvilWorld
import andesite.world.block.BlockRegistry
import andesite.world.block.readBlockRegistry
import basalt.extension.host.BasaltExtensionEngine
import basalt.server.BasaltException
import basalt.server.BasaltServer
import basalt.server.BasaltServerImpl
import com.charleskorn.kaml.Yaml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.defaultLazy
import com.github.ajalt.clikt.parameters.types.file
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import net.benwoodworth.knbt.Nbt
import net.benwoodworth.knbt.NbtCompression
import net.benwoodworth.knbt.NbtVariant
import org.apache.logging.log4j.kotlin.Logging

class BasaltCommand : CliktCommand() {
  companion object : Logging

  private val yaml: Yaml = Yaml()

  private val coroutineContext: CoroutineContext = CoroutineName("basalt") + SupervisorJob()

  private val target: File by argument("target", help = "the target basalt server")
    .file(mustExist = true, canBeFile = false, mustBeReadable = true)
    .defaultLazy { File(".") }

  override fun run(): Unit = runBlocking(CoroutineName("basalt/cli")) {
    logger.info("Basalt is starting up at folder: $target")

    val config = decodeBasaltConfig()
    val server = createServerByConfig(config)

    server.extensionEngine.loadInitialExtensions()

    server.listen()
  }

  private fun createServerByConfig(config: BasaltServerConfig): BasaltServer {
    val minecraftServer = createJavaServer(coroutineContext) {
      hostname = config.host
      port = config.port

      blockRegistry = createBlockRegistry()
      codec = createMinecraftCodec()

      motd {
        maxPlayers = config.motd.maxPlayers
        version = config.motd.version
        text = Chat.of(config.motd.text)
      }

      val worldRegistry = createWorldRegistry(blockRegistry, config.worlds)

      spawn = config.spawn.run {
        val world = worldRegistry[world]
          ?: throw BasaltException("World $world is not registered")

        Location(x, y, z, yaw, pitch, world)
      }
    }

    val basaltServer = BasaltServerImpl(target, minecraftServer).apply {
      extensionEngine = BasaltExtensionEngine(this)
    }

    return basaltServer
  }

  private fun decodeBasaltConfig(): BasaltServerConfig {
    val file = target.resolve("basalt.yaml").takeIf { it.exists() }
      ?: target.resolve("basalt.yml").takeIf { it.exists() }
      ?: throw BasaltException("No basalt.yaml or basalt.yml found in ${target.absolutePath}")

    logger.debug("Discovered basalt config at $file")

    return yaml.decodeFromString(file.readText())
  }

  private fun createMinecraftCodec(): MinecraftCodec {
    return MinecraftCodec.v756 {
      nbt = Nbt {
        variant = NbtVariant.Java
        compression = NbtCompression.None
        ignoreUnknownKeys = true
      }

      json = Json {
        prettyPrint = true
      }

      serializersModule = SerializersModule {
        contextual(UuidSerializer)
      }
    }
  }

  private fun createBlockRegistry(): BlockRegistry {
    return ClassLoader
      .getSystemResource("v756/blocks.json")
      ?.readText()
      ?.let(::readBlockRegistry)
      ?: throw BasaltException("Failed to load block registry")
  }

  private fun createWorldRegistry(
    blockRegistry: BlockRegistry,
    worlds: Set<BasaltWorldConfig>,
  ): Map<String, World> {
    return worlds.associate { (name) ->
      val folder = target.resolve(name).takeIf { it.exists() }
        ?: target.resolve("worlds/$name").takeIf { it.exists() }
        ?: throw BasaltException("World $name does not exist")

      name to readAnvilWorld(blockRegistry, folder)
    }
  }
}
