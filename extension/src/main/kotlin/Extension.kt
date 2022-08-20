package basalt.extension

import java.io.File

data class Extension(
  val name: String,
  val entrypoint: File,
  val folder: File,
)
