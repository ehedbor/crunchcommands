package org.hedbor.evan.crunchcommands.util

import org.bukkit.Material
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin


@Suppress("unused")
fun JavaPlugin.commands(block: CommandsBlock.() -> Unit) {
    CommandsBlock().block()
}

class CommandsBlock {
    fun JavaPlugin.command(name: String, block: PluginCommand.() -> Unit = {}) {
        getCommand(name).block()
    }

    fun JavaPlugin.command(name: String, executor: CommandExecutor, block: PluginCommand.() -> Unit = {}) {
        val c = getCommand(name)
        c.executor = executor
        c.block()
    }
}

fun itemStack(material: Material, block: ItemStack.() -> Unit = {}): ItemStack {
    val stack = ItemStack(material)
    stack.block()
    return stack
}

fun ItemStack.itemMeta(block: ItemMeta.() -> Unit): ItemMeta {
    this.itemMeta.block()
    return this.itemMeta
}