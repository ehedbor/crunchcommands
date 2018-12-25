/*
 * Copyright (C) 2018 Evan Hedbor.
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

package org.hedbor.evan.crunchcommands.util

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter
import org.bukkit.configuration.Configuration
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KClass


internal fun JavaPlugin.registerCommand(name: String, executor: CommandExecutor? = null, block: PluginCommand.() -> Unit = {}) {
    val c = getCommand(name)
    c.executor = executor
    if (executor is TabCompleter) {
        c.tabCompleter = executor
    }

    c.block()
}

internal fun JavaPlugin.registerCommands(vararg commands: Pair<String, CommandExecutor>) {
    for (cmd in commands) {
        registerCommand(cmd.first, cmd.second)
    }
}

internal fun itemStack(material: Material, block: ItemStack.() -> Unit = {}): ItemStack {
    val stack = ItemStack(material)
    stack.block()
    return stack
}

internal fun ItemStack.itemMeta(block: ItemMeta.() -> Unit): ItemMeta {
    val itemMetaCopy = itemMeta
    itemMetaCopy.block()
    itemMeta = itemMetaCopy
    return itemMeta
}


internal fun <T : Any> Configuration.getObjectList(entryClass: KClass<T>, path: String, def: List<T> = emptyList()): List<T> {
    val result = ArrayList<T>()
    for (entry in getList(path, def)) {
        if (entry != null && entry::class == entryClass) {
            result.add(entryClass.java.cast(entry))
        }
    }
    return result
}

internal inline fun <reified T : Any> Configuration.getObjectList(path: String, def: List<T> = emptyList()): List<T> {
    return getObjectList(T::class, path, def)
}

internal fun Location.toReadableString() = "($blockX, $blockY, $blockZ)"
