package basalt.script.host

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasaltServerConfig(
  val host: String = "127.0.0.1",
  val port: Int = 25565,
  val codec: BasaltCodecConfig,
  val motd: BasaltMotdConfig,
  val worlds: Set<BasaltWorldCodec>,
)

@Serializable
data class BasaltWorldConfig(val name: String, val codec: BasaltWorldCodec)

@Serializable
data class BasaltCodecConfig(val version: Int)

@Serializable
data class BasaltMotdConfig(
  val text: String,
  val version: String,
  @SerialName("max-players") val maxPlayers: Int, // TODO: move to top-level
)

@Serializable
enum class BasaltWorldCodec {
  @SerialName("anvil")
  Anvil,

  @SerialName("slime")
  Slime;
}
