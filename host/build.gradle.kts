dependencies {
  implementation("org.jetbrains.kotlin:kotlin-scripting-common")
  implementation("org.jetbrains.kotlin:kotlin-scripting-jvm")
  implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host")
  implementation(project(":script-definition"))

  implementation("com.charleskorn.kaml:kaml:0.47.0")
}
