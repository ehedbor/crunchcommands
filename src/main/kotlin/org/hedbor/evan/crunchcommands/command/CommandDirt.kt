package org.hedbor.evan.crunchcommands.command

import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.annotation.permission.Permission
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.CMD_USAGE
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PERM_MSG
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PLUGIN_ID
import org.hedbor.evan.crunchcommands.util.itemMeta
import org.hedbor.evan.crunchcommands.util.itemStack
import org.bukkit.plugin.java.annotation.command.Command as CommandYml


/**
 * Gives the user some emergency dirt.
 */
@CommandYml(name = "dirt", desc = "Dispenses emergency dirt.", permission = "$PLUGIN_ID.dirt", permissionMessage = PERM_MSG, usage = CMD_USAGE)
@Permission(name = "$PLUGIN_ID.dirt", desc = "Allows dirt command", defaultValue = PermissionDefault.TRUE)
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