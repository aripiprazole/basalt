package basalt.extension.host

import basalt.extension.Extension
import basalt.extension.ExtensionEngine
import basalt.server.BasaltServer

class BasaltExtensionEngine(val server: BasaltServer) : ExtensionEngine {
  override fun loadExtension(name: String): Extension {
  }
}
