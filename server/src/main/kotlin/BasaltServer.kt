package basalt.server

import andesite.server.MinecraftServer
import basalt.extension.ExtensionEngine
import java.io.File

interface BasaltServer : MinecraftServer {
  val extensionEngine: ExtensionEngine
  val workingDirectory: File
}

fun BasaltServer(
  extensionEngine: ExtensionEngine,
  workingDirectory: File,
  minecraftServer: MinecraftServer,
): BasaltServer {
  return BasaltServerImpl(extensionEngine, workingDirectory, minecraftServer)
}

private class BasaltServerImpl(
  override val extensionEngine: ExtensionEngine,
  override val workingDirectory: File,
  private val minecraftServer: MinecraftServer,
) : BasaltServer, MinecraftServer by minecraftServer {
  override fun toString(): String {
    return "BasaltServer(minecraftServer=$minecraftServer)"
  }
}
