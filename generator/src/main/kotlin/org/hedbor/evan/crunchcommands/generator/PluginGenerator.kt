/*
 * Copyright (C) 2018-2019 Evan Hedbor.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.hedbor.evan.crunchcommands.generator

import com.google.auto.service.AutoService
import org.bukkit.configuration.file.YamlConstructor
import org.bukkit.configuration.file.YamlRepresenter
import org.hedbor.evan.crunchcommands.annotation.*
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation


/**
 * Generates a plugin.yml file.
 */
@Suppress("unused")
@AutoService(Processor::class)
class PluginGenerator: AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    private var foundPluginClass: Boolean = false

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return listOf(Plugin::class, Command::class, Permission::class, ChildPermission::class)
            .map { it.java.canonicalName }
            .toMutableSet()
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val elements = roundEnv.getElementsAnnotatedWith(Plugin::class.java)
        if (elements.size > 1 && foundPluginClass) {
            err("Found more than one plugin class")
            return false
        } else if (elements.isEmpty()) {
            return false
        }

        val pluginElement = elements.iterator().next()
        foundPluginClass = true

        if (pluginElement !is TypeElement) {
            err("Plugin element is not a class")
            return false
        }

        val pluginClassName = pluginElement.qualifiedName.toString()
        val pluginAnnotation = pluginElement.getAnnotation(Plugin::class.java)!!

        val ymlMap = createYmlMap(pluginClassName, pluginAnnotation)
        generateYmlFile(ymlMap)

        return true
    }

    private fun createYmlMap(mainClass: String, plugin: Plugin): LinkedHashMap<String, Any> {
        // can store a string, another LinkedHashMap, or an array.
        // use a linked hash map to preserve the order
        val yml = LinkedHashMap<String, Any>()

        yml.apply {
            // add required attributes
            this["main"] = mainClass
            this["name"] = plugin.name
            this["version"] = plugin.version
            this["api-version"] = plugin.apiVersion

            putIfNotEmpty("description", plugin.description)
            putIf("load", plugin.loadOrder) { it != LoadOrder.NONE_SPECIFIED }

            // add "author" element if only author is present, or "authors" if multiple are present
            putIf("author", plugin.authors.getOrNull(0)) { plugin.authors.size == 1 }
            putIf("authors", plugin.authors) { it.size > 1 }

            putIfNotEmpty("website", plugin.website)
            putIfNotEmpty("prefix", plugin.chatPrefix)

            putIfNotEmpty("depend", plugin.dependencies)
            putIfNotEmpty("softdepend", plugin.softDependencies)
            putIfNotEmpty("loadbefore", plugin.loadBefore)

            // check if the permission prefix exists
            val permPrefix = if (plugin.permissionPrefix.isNotBlank()) plugin.permissionPrefix else null

            // add the "commands" and "permissions" elements
            val commands = LinkedHashMap<String, LinkedHashMap<String, Any>>()
            for (cmd in plugin.commands) {
                val cmdInfo = LinkedHashMap<String, Any>().apply {
                    putIfNotEmpty("usage", cmd.usage)
                    putIfNotEmpty("aliases", cmd.aliases)
                    putIfNotEmpty("description", cmd.description)
                    if (permPrefix == null) {
                        putIfNotEmpty("permission", cmd.permission)
                    } else {
                        putIfNotEmpty("permission", cmd.permission.replace("$", permPrefix))
                    }
                    putIfNotEmpty("permission-message", cmd.permissionMessage)
                }

                commands[cmd.name] = cmdInfo
            }
            putIfNotEmpty("commands", commands)


            val permissions = LinkedHashMap<String, LinkedHashMap<String, Any>>()
            for (perm in plugin.permissions) {
                val permInfo = LinkedHashMap<String, Any>().apply {
                    putIfNotEmpty("description", perm.description)
                    this["default"] = perm.default.toString()

                    val children = LinkedHashMap<String, Boolean>()
                    for (child in perm.children) {
                        val childName = if (permPrefix == null) child.name else child.name.replace("$", permPrefix)
                        children[childName] = child.inherit
                    }
                    putIfNotEmpty("children", children)
                }

                val permName = if (permPrefix == null) perm.name else perm.name.replace("$", permPrefix)
                permissions[permName] = permInfo
            }
            yml.putIfNotEmpty("permissions", permissions)

        }

        return yml
    }

    @Throws(IOException::class)
    private fun generateYmlFile(pluginYml: Map<String, Any>) {
        val yamlOptions = DumperOptions().apply {
            indent = 2
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
            defaultScalarStyle = DumperOptions.ScalarStyle.PLAIN
        }
        val yamlRepresenter = YamlRepresenter().apply {
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
            defaultScalarStyle = DumperOptions.ScalarStyle.PLAIN
        }
        val yaml = Yaml(YamlConstructor(), yamlRepresenter, yamlOptions)

        val file = processingEnv.filer.createResource(StandardLocation.CLASS_OUTPUT, "", "plugin.yml")
        file.openWriter().use { writer ->
            yaml.dump(pluginYml, writer)
            writer.flush()
        }
    }

    private fun <T> MutableMap<String, Any>.putIf(key: String, value: T, predicate: (T) -> Boolean) {
        if (predicate(value)) {
            this[key] = value ?: "null"
        }
    }

    private fun MutableMap<String, Any>.putIfNotEmpty(key: String, value: String) {
        if (value.isNotBlank()) {
            this[key] = value
        }
    }

    private fun MutableMap<String, Any>.putIfNotEmpty(key: String, values: Array<String>) {
        if (values.isNotEmpty()) {
            this[key] = values
        }
    }

    private fun MutableMap<String, Any>.putIfNotEmpty(key: String, values: Map<String, Any>) {
        if (values.isNotEmpty()) {
            this[key] = values
        }
    }

    private fun err(msg: String, element: Element? = null) {
        when (element) {
            null -> processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, msg)
            else -> processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, msg, element)
        }
    }
}