package org.hedbor.evan.crunchcommands.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


/**
 * Teleports the caller to the specified player.
 */
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