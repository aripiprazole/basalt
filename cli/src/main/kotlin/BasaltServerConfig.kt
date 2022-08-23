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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.apache.logging.log4j.kotlin.Logging

@Serializable
data class BasaltServerConfig(
  val host: String = "127.0.0.1",
  val port: Int = 25565,
  val motd: BasaltMotdConfig,
  val spawn: BasaltLocation,
  val worlds: Set<BasaltWorldConfig>,
)

@Serializable
data class BasaltWorldConfig(val name: String, val codec: BasaltWorldCodec) {
  companion object : Logging

  init {
    if (codec == BasaltWorldCodec.Slime) {
      logger.warn("Slime world codec is unimplemented")
    }
  }
}

@Serializable
data class BasaltMotdConfig(
  val text: String,
  val version: String,
  @SerialName("max-players") val maxPlayers: Int, // TODO: move to top-level
)

@Serializable
data class BasaltLocation(
  val x: Double,
  val y: Double,
  val z: Double,
  val yaw: Float = 0f,
  val pitch: Float = 0f,
  val world: String,
)

@Serializable
enum class BasaltWorldCodec {
  @SerialName("anvil")
  Anvil,

  @SerialName("slime")
  Slime;
}
