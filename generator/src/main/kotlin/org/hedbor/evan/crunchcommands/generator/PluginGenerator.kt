/*
 * Copyright (C) 2019 Evan Hedbor.
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
import org.hedbor.evan.crunchcommands.annotation.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic


/**
 * Generates a plugin.yml file.
 */
@AutoService(Processor::class)
class PluginGenerator : AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
        private const val INDENT = "  "
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

        val yml = createYmlInfo(pluginClassName, pluginAnnotation)
        val ymlInfo = buildYmlFile(yml) ?: return false

        // TODO: save ymlInfo to a file

        return true
    }

    internal fun buildYmlFile(yml: Map<String, Any>): String? {
        var content = ""

        fun addMap(element: Map<*, *>, curIndent: String): Boolean {
            for ((key, value) in element) {
                when (value) {
                    is Map<*, *> -> { // should be LinkedHashMap<String, Any>
                        content += "$curIndent$key:\n"
                        val success = addMap(value, "$curIndent$INDENT")
                        if (!success) { return false }
                    }
                    is Array<*> -> { // should be Array<String>
                        content += "$curIndent$key:\n"
                        for (e in value) {
                            content += "$curIndent$INDENT- $e\n"
                        }
                    }
                    is String -> {
                        content += "$curIndent$key: $value\n"
                    }
                    else -> {
                        err("Couldn't build plugin.yml: unexpected type \"${value?.javaClass?.name}\".")
                        return false
                    }
                }
            }
            return true
        }

        val success = addMap(yml, "")
        return when (success) {
            true -> content
            false -> null
        }

    }

    internal fun createYmlInfo(mainClass: String, pluginAnnotation: Plugin): LinkedHashMap<String, Any> {
        // can store a string, another LinkedHashMap, or an array.
        // use a linked hash map to preserve the order
        val yml = LinkedHashMap<String, Any>()

        pluginAnnotation.apply {
            // add required attributes
            yml["main"] = mainClass
            yml["name"] = name
            yml["version"] = version
            yml["api-version"] = apiVersion

            yml.putIfNotBlank("description", description)
            yml.putIf("load", loadOrder) { it != LoadOrder.NONE_SPECIFIED }

            // add "author" element if only author is present, or "authors" if multiple are present
            yml.putIf("author", authors.getOrNull(0)) { authors.size == 1 }
            yml.putContentIf("authors", authors) { it.size > 1 }

            yml.putIfNotBlank("website", website)
            yml.putIfNotBlank("prefix", prefix)

            yml.putContentIfNotEmpty("depend", dependencies)
            yml.putContentIfNotEmpty("softdepend", softDependencies)
            yml.putContentIfNotEmpty("loadbefore", loadBefore)
        }

        // add the "commands" and "permissions" elements
        val commands = LinkedHashMap<String, Any>()
        for (cmd in pluginAnnotation.commands) {
            val cmdInfo = LinkedHashMap<String, Any>()
            cmdInfo.putIfNotBlank("usage", cmd.usage)
            cmdInfo.putIfNotEmpty("aliases", cmd.aliases)
            cmdInfo.putIfNotBlank("description", cmd.description)
            cmdInfo.putIfNotBlank("permission", cmd.permission)
            cmdInfo.putIfNotBlank("permission-message", cmd.permissionMessage)

            commands[cmd.name] = cmdInfo
        }
        yml.putIfNotEmpty("commands", commands)

        val permissions = LinkedHashMap<String, Any>()
        for (perm in pluginAnnotation.permissions) {
            val permInfo = LinkedHashMap<String, Any>()
            permInfo.putIfNotBlank("description", perm.description)
            permInfo["default"] = perm.default.toString()

            val children = LinkedHashMap<String, Any>()
            for (child in perm.children) {
                children[child.name] = child.inherit.toString()
            }
            permInfo.putIfNotEmpty("children", children)

            permissions[perm.name] = permInfo
        }
        yml.putIfNotEmpty("permissions", permissions)

        return yml
    }


    private fun <T> MutableMap<String, Any>.putIf(key: String, value: T, predicate: (T) -> Boolean) {
        if (predicate(value)) {
            this[key] = value.toString()
        }
    }

    private fun MutableMap<String, Any>.putIfNotBlank(key: String, value: String) {
        if (value.isNotBlank()) {
            this[key] = value
        }
    }

    private fun <T> MutableMap<String, Any>.putContentIf(key: String, value: Array<T>, predicate: (Array<T>) -> Boolean) {
        if (value.isNotEmpty()) {
            this[key] = value.contentToString()
        }
    }

    private fun <T> MutableMap<String, Any>.putContentIfNotEmpty(key: String, value: Array<T>) {
        if (value.isNotEmpty()) {
            this[key] = value.contentToString()
        }
    }

    private fun MutableMap<String, Any>.putIfNotEmpty(key: String, value: Array<String>) {
        if (value.isNotEmpty()) {
            this[key] = value
        }
    }

    private fun MutableMap<String, Any>.putIfNotEmpty(key: String, value: Map<String, Any>) {
        if (value.isNotEmpty()) {
            this[key] = value
        }
    }


    private fun err(msg: String, element: Element? = null) {
        when (element) {
            null -> processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, msg)
            else -> processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, msg, element)
        }
    }

//    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
//        for (element in roundEnv.getElementsAnnotatedWith(Plugin::class.java)) {
//            val className = element.simpleName.toString()
//            val packageName = processingEnv.elementUtils.getPackageOf(element).toString()
//            generateClass(className, packageName)
//        }
//        return true
//    }
//
//    private fun generateClass(className: String, packageName: String) {
//        val fileName = "Generated_$className"
//        val file = FileSpec.builder(packageName, fileName)
//            .addType(TypeSpec.classBuilder(fileName)
//                .addFunction(FunSpec.builder("getName")
//                    .addStatement("return \"World\"")
//                    .build())
//                .build())
//            .build()
//
//        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
//        file.writeTo(File(kaptKotlinGeneratedDir, "$fileName.kt"))
//    }
}