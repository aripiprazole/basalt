package basalt.script.definition

import kotlin.script.experimental.annotations.KotlinScript

@KotlinScript(
  fileExtension = "basalt.kts",
  compilationConfiguration = BasaltScriptConfiguration::class,
)
abstract class BasaltScript
