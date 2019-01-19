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

package org.hedbor.evan.crunchcommands.annotation

import org.hedbor.evan.crunchcommands.generator.PluginGenerator
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class TestPluginGenerator {
    @Plugin(
        name = "CrunchCommands",
        version = "@VERSION@",
        apiVersion = "1.13",
        description = "My plugin.",
        loadOrder = LoadOrder.STARTUP,
        authors = ["Evan", "StackOverflow"],
        website = "www.spigotmc.org",
        prefix = "cc",
        dependencies = ["WorldEdit", "Towny"],
        softDependencies = ["Essentials, AnotherPlugin"],
        loadBefore = ["OnePlugin, TwoPlugin, RedPlugin", "Bob"],
        commands = [
            Command(
                // For the record, this is a meme, not a call for help.
                name = "commitdie",
                usage = "/commitdie",
                aliases = ["git-commit--die", "seppuku", "stuck"],
                description = "Do you crave death? This command is the one for you!",
                permission = "crunchcommands.sweet-release",
                permissionMessage = "You do not have permission to die, maggot!")],
        permissions = [
            Permission(
                "crunchcommands.sweet-release",
                "Allows suicide.",
                PermissionDefault.ALWAYS),
            Permission(
                "crunchcommands.*",
                "Wildcard permission",
                PermissionDefault.NEVER,
                [ChildPermission("crunchcommands.sweet-release")]
            )
        ]
    )
    companion object {
        private val YML_LIST = linkedMapOf<String, Any>(
            "main" to "org.hedbor.evan.crunchcommands.CrunchCommands",
            "name" to "CrunchCommands",
            "version" to "@VERSION@",
            "api-version" to "1.13",
            "description" to "My plugin.",
            "load" to "STARTUP",
            "authors" to "[Evan, StackOverflow]",
            "website" to "www.spigotmc.org",
            "prefix" to "cc",
            "depend" to "[WorldEdit, Towny]",
            "softdepend" to "[Essentials, AnotherPlugin]",
            "loadbefore" to "[OnePlugin, TwoPlugin, RedPlugin, Bob]",
            "commands" to linkedMapOf<String, Any>(
                "commitdie" to linkedMapOf<String, Any>(
                    "usage" to "/commitdie",
                    "aliases" to arrayOf("git-commit--die", "seppuku", "stuck"),
                    "description" to "Do you crave death? This command is the one for you!",
                    "permission" to "crunchcommands.sweet-release",
                    "permission-message" to "You do not have permission to die, maggot!"
                )
            ),
            "permissions" to linkedMapOf<String, Any>(
                "crunchcommands.sweet-release" to linkedMapOf<String, Any>(
                    "description" to "Allows suicide.",
                    "default" to "true"
                ),
                "crunchcommands.*" to linkedMapOf<String, Any>(
                    "description" to "Wildcard permission",
                    "default" to "false",
                    "children" to linkedMapOf<String, Any>(
                        "crunchcommands.sweet-release" to "true"
                    )
                )
            )
        )

        private val YML_FILE = """
            main: org.hedbor.evan.crunchcommands.CrunchCommands
            name: CrunchCommands
            version: @VERSION@
            api-version: 1.13
            description: My plugin.
            load: STARTUP
            authors: [Evan, StackOverflow]
            website: www.spigotmc.org
            prefix: cc
            depend: [WorldEdit, Towny]
            softdepend: [Essentials, AnotherPlugin]
            loadbefore: [OnePlugin, TwoPlugin, RedPlugin, Bob]
            commands:
              commitdie:
                usage: /commitdie
                aliases:
                  - git-commit--die
                  - seppuku
                  - stuck
                description: Do you crave death? This command is the one for you!
                permission: crunchcommands.sweet-release
                permission-message: You do not have permission to die, maggot!
            permissions:
              crunchcommands.sweet-release:
                description: Allows suicide.
                default: true
              crunchcommands.*:
                description: Wildcard permission
                default: false
                children:
                  crunchcommands.sweet-release: true

        """.trimIndent()
    }

    @Test
    fun `Correctly turn @Plugin into a plugin yml map`() {
        val annotation: Plugin? = TestPluginGenerator.Companion::class.java.getDeclaredAnnotation(Plugin::class.java)
        assertNotNull(annotation, "Annotation not found")

        val generator = PluginGenerator()
        val result = generator.createYmlInfo(
            "org.hedbor.evan.crunchcommands.CrunchCommands", annotation)

        compareYml(YML_LIST, result)
    }

    @Test
    fun `Correctly build a plugin yml file`() {
        val generator = PluginGenerator()
        val result = generator.buildYmlFile(YML_LIST)
        assertNotNull(result)

        compareLineByLine(YML_FILE, result)
    }

    private fun compareYml(expected: Map<*, *>, actual: Map<*, *>, path: List<*> = emptyList<Any?>()) {
        for ((key, expectedValue) in expected) {
            val actualValue = actual[key]
            val newPath = path + listOf(key)

            if (expectedValue is Map<*, *> && actualValue is Map<*, *>) {
                compareYml(expectedValue, actualValue, newPath)
            } else if (expectedValue is Array<*> && actualValue is Array<*>) {
                assert(expectedValue.contentDeepEquals(actualValue)) { "Arrays at \"${newPath.joinToString(".")}\" are not equal." }
            } else {
                assertEquals(expectedValue, actualValue, "Elements at \"${newPath.joinToString(".")}\" are not equal.")
            }
        }
    }

    private fun compareLineByLine(expected: String, actual: String) {
        val expectedLines = expected.lines()
        val actualLines = actual.lines()
        assertEquals(expectedLines.size, actualLines.size, "Expected line count differs from actual line count")

        for ((expectedLine, actualLine) in expectedLines.zip(actualLines)) {
            assertEquals(expectedLine, actualLine, "Unequal lines")
        }
    }
}