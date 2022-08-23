/*
 *    Copyright 2021 Gabrielle Guimarães de Oliveira
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    mavenCentral()
  }

  dependencies {
    implementation("me.gabrielleeg1:andesite-server-java:1.0.6-SNAPSHOT")

    testImplementation(kotlin("test"))
  }

  tasks.test {
    useJUnitPlatform()
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
  }
}
