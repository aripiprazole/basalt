dependencies {
  implementation("org.jetbrains.kotlin:kotlin-scripting-common")
  implementation("org.jetbrains.kotlin:kotlin-scripting-jvm")
  implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host")

  implementation(project(":extension"))
  implementation(project(":extension:script-definition"))

  implementation(project(":server"))
}
