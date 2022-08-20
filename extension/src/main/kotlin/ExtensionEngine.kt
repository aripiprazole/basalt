package basalt.extension

interface ExtensionEngine {
  fun loadExtension(name: String): Extension
}
