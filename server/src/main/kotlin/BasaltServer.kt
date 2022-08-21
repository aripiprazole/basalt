package basalt.server

import andesite.server.MinecraftServer
import basalt.extension.ExtensionEngine
import java.io.File

interface BasaltServer : MinecraftServer {
  val extensionEngine: ExtensionEngine
  val workingDirectory: File

  val extensionsDirectory: File
}

class BasaltServerImpl(
  override val workingDirectory: File,
  private val minecraftServer: MinecraftServer,
) : BasaltServer, MinecraftServer by minecraftServer {
  override lateinit var extensionEngine: ExtensionEngine

  override val extensionsDirectory: File =
    workingDirectory.resolve("scripts").takeIf { !it.isFile }
      ?.apply { mkdirs() }
      ?: throw BasaltException("Scripts directory is a file")

  override fun toString(): String {
    return "BasaltServer(minecraftServer=$minecraftServer)"
  }
}
