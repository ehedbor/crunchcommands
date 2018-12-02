package org.hedbor.evan.crunchcommands.command

import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.hedbor.evan.crunchcommands.util.itemMeta
import org.hedbor.evan.crunchcommands.util.itemStack


/**
 * Gives the user some emergency dirt.
 */
object CommandDirt : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val dirt = itemStack(Material.DIRT) {
                amount = 3
                itemMeta {
                    displayName = "${ChatColor.RED}EMERGENCY DIRT"
                    lore = arrayListOf("${ChatColor.DARK_RED}For rectal use only.")
                }
            }

            sender.inventory.addItem(dirt)
        } else {
            sender.sendMessage("Only players can use this command.")
        }

        return true
    }
}