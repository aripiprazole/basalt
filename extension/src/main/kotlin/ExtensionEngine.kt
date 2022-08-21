package basalt.extension

interface ExtensionEngine {
  val loadedExtensions: Set<Extension>

  suspend fun loadInitialExtensions()

  suspend fun loadExtension(name: String): Extension
}
