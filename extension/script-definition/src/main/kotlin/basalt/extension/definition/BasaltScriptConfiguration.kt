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

package basalt.extension.definition

import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptCollectedData
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptConfigurationRefinementContext
import kotlin.script.experimental.api.asSuccess
import kotlin.script.experimental.api.collectedAnnotations
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.dependencies
import kotlin.script.experimental.api.onSuccess
import kotlin.script.experimental.api.refineConfiguration
import kotlin.script.experimental.api.with
import kotlin.script.experimental.dependencies.CompoundDependenciesResolver
import kotlin.script.experimental.dependencies.DependsOn
import kotlin.script.experimental.dependencies.FileSystemDependenciesResolver
import kotlin.script.experimental.dependencies.Repository
import kotlin.script.experimental.dependencies.maven.MavenDependenciesResolver
import kotlin.script.experimental.dependencies.resolveFromScriptSourceAnnotations
import kotlin.script.experimental.jvm.JvmDependency
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlinx.coroutines.runBlocking

object BasaltScriptConfiguration : ScriptCompilationConfiguration(
  {
    defaultImports(DependsOn::class, Repository::class)

    jvm {
      // Extract the whole classpath from context classloader and use it as dependencies
      dependenciesFromCurrentContext(wholeClasspath = true)
    }

    // Callbacks
    refineConfiguration {
      // Process specified annotations with the provided handler
      onAnnotations(
        DependsOn::class,
        Repository::class,
        handler = { runBlocking { configureMavenDepsOnAnnotations(it) } },
      )
    }
  },
)

private val resolver = CompoundDependenciesResolver(
  FileSystemDependenciesResolver(),
  MavenDependenciesResolver(),
)

suspend fun configureMavenDepsOnAnnotations(
  context: ScriptConfigurationRefinementContext,
): ResultWithDiagnostics<ScriptCompilationConfiguration> {
  val annotations = context.collectedData
    ?.get(ScriptCollectedData.collectedAnnotations)
    ?.takeIf { it.isNotEmpty() }
    ?: return context.compilationConfiguration.asSuccess()

  return resolver
    .resolveFromScriptSourceAnnotations(annotations)
    .onSuccess { jvmDependencies ->
      context.compilationConfiguration
        .with { dependencies.append(JvmDependency(jvmDependencies)) }
        .asSuccess()
    }
}
