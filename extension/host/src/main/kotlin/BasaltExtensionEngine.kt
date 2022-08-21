package basalt.extension.host

import basalt.extension.Extension
import basalt.extension.ExtensionEnableEvent
import basalt.extension.ExtensionEngine
import basalt.extension.ExtensionLoadEvent
import basalt.extension.definition.BasaltScript
import basalt.server.BasaltException
import basalt.server.BasaltServer
import java.io.File
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

class BasaltExtensionEngine(private val server: BasaltServer) : ExtensionEngine {
  override val loadedExtensions: HashSet<Extension> = HashSet()

  override fun loadExtension(name: String): Extension {
    val entrypoint = server.extensionsDirectory.resolve(name).takeIf { it.isFile }
      ?: throw BasaltException("Extension $name not found")

    val extension = Extension(name, entrypoint, entrypoint).also { loadedExtensions += it }

    evalFile(entrypoint)

    server.publish(ExtensionLoadEvent(extension))
    server.publish(ExtensionEnableEvent(extension))

    return extension
  }

  private fun evalFile(scriptFile: File): ResultWithDiagnostics<EvaluationResult> {
    val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<BasaltScript>()

    return BasicJvmScriptingHost().eval(scriptFile.toScriptSource(), compilationConfiguration, null)
  }
}
