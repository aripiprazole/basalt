package basalt.extension.host

import basalt.extension.Extension
import basalt.extension.ExtensionEngine
import basalt.server.BasaltServer

class BasaltExtensionEngine : ExtensionEngine {
  lateinit var server: BasaltServer
  
  override fun loadExtension(name: String): Extension {
  }
}
