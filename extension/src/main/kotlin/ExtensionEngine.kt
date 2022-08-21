package basalt.extension

interface ExtensionEngine {
  val loadedExtensions: Set<Extension>

  fun loadExtension(name: String): Extension
}
