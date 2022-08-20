dependencies {
  implementation("org.jetbrains.kotlin:kotlin-scripting-common")
  implementation("org.jetbrains.kotlin:kotlin-scripting-jvm")
  implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host")

  implementation(project(":server"))

  implementation(project(":extension"))
  implementation(project(":extension:script-definition"))
  implementation(project(":extension:host"))

  implementation("com.github.ajalt.clikt:clikt:3.5.0")
  implementation("com.charleskorn.kaml:kaml:0.47.0")
}
