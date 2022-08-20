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
