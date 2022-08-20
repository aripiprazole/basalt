import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.7.10"
  kotlin("plugin.serialization") version "1.7.10"
  id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
  id("io.gitlab.arturbosch.detekt") version "1.19.0"
  application
}

application {
  mainClass.set("MainKt")
}

allprojects {
  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
  apply(plugin = "org.jlleitschuh.gradle.ktlint")
  apply(plugin = "io.gitlab.arturbosch.detekt")

  ktlint {
    version.set("0.45.2")
    android.set(false)
    additionalEditorconfigFile.set(rootProject.file(".editorconfig"))
  }

  detekt {
    buildUponDefaultConfig = true
    allRules = false

    config = files("${rootProject.projectDir}/config/detekt.yml")
    baseline = file("${rootProject.projectDir}/config/baseline.xml")
  }

  repositories {
    mavenCentral()
  }

  dependencies {
    testImplementation(kotlin("test"))
  }

  tasks.test {
    useJUnitPlatform()
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
  }
}
