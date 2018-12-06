package org.hedbor.evan.crunchcommands.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.annotation.permission.Permission
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.CMD_USAGE
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PERM_MSG
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PLUGIN_ID
import org.bukkit.plugin.java.annotation.command.Command as CommandYml


/**
 * Teleports the caller to the specified player.
 */
@CommandYml(name = "ctp", desc = "Teleports to another player.", permission = "$PLUGIN_ID.ctp", permissionMessage = PERM_MSG, usage = "$CMD_USAGE <name>")
@Permission(name = "$PLUGIN_ID.ctp", desc = "Allows ctp command", defaultValue = PermissionDefault.TRUE)
object CommandCtp : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command.")
            return true
        }

        // Must have exactly one target
        if (args.size != 1) {
            return false
        }

        // Target must be a connected player
        val targetName = args[0]
        val target: Player? = Bukkit.getPlayer(targetName)
        if (target == null) {
            sender.sendMessage("Unknown player \"$targetName\".")
            return true
        }

        // Teleport the sender and notify both players.
        sender.teleport(target)
        sender.sendMessage("You teleported to ${target.displayName}.")
        target.sendMessage("${sender.displayName} teleported to you.")

        return true
    }
}