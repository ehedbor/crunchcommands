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

package org.hedbor.evan.crunchcommands.annotation


/**
 * An annotation that allows automatic population of plugin.yml.
 *
 * If an option is already specified in a plugin.yml, this annotation will not
 * override it.
 *
 * @param name The name of the plugin. Must only consist of alphanumeric
 *     characters and underscore.
 * @param version The version of the plugin.
 * @param apiVersion The version of the Spigot API used.
 * @param description A human friendly description of your plugin's functionality.
 * @param loadOrder Explicitly states when a plugin should be loaded.
 * @param authors Uniquely identifies who developed this plugin. If only one
 *     author is provided, the "author" tag will be set in plugin.yml.
 * @param website The plugin's or author's website.
 * @param chatPrefix The name to used when logging to console (instead of the plugin's name).
 * @param dependencies A list of dependencies that the plugin requires to load.
 * @param softDependencies A list of plugins required for the plugin to have
 *     full functionality.
 * @param loadBefore A list of plugins that should be loaded after your plugin.
 * @param commands A list of commands the plugin wishes to register.
 * @param permissions Permissions that the plugin wishes to register.
 * @param permissionPrefix If set, will cause the `$` character to be replaced
 *     with this message in permission strings.
 */
@Target(AnnotationTarget.CLASS)
annotation class Plugin(
    val name: String,
    val version: String,
    val apiVersion: String,
    val description: String = "",
    val loadOrder: LoadOrder = LoadOrder.NONE_SPECIFIED,
    val authors: Array<String> = [],
    val website: String = "",
    val chatPrefix: String = "",
    val dependencies: Array<String> = [],
    val softDependencies: Array<String> = [],
    val loadBefore: Array<String> = [],
    val commands: Array<Command> = [],
    val permissions: Array<Permission> = [],
    val permissionPrefix: String = ""
)