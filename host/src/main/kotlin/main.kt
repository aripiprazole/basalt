package basalt.script.host

import basalt.script.definition.BasaltScript
import java.io.File
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

fun main(vararg args: String) {
  if (args.size != 1) {
    println("usage: basalt <script file>")
  } else {
    val scriptFile = File(args[0])
    println("executing script $scriptFile")
    val result = evalFile(scriptFile)
    result.reports.forEach {
      if (it.severity > ScriptDiagnostic.Severity.DEBUG) {
        it.exception?.printStackTrace()
        println(" : ${it.message}" + if (it.exception == null) "" else ": ${it.exception}")
      }
    }
  }
}

fun evalFile(scriptFile: File): ResultWithDiagnostics<EvaluationResult> {
  val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<BasaltScript>()
  return BasicJvmScriptingHost().eval(scriptFile.toScriptSource(), compilationConfiguration, null)
}
