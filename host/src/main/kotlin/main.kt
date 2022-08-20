package basalt.script.host

import kotlin.system.exitProcess

fun main(vararg args: String) {
  try {
    BasaltCommand().main(args)
  } catch (exception: BasaltException) {
    System.err.println(exception.message)
    exitProcess(1)
  }
}
