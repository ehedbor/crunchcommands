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

package org.hedbor.evan.crunchcommands.command

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.annotation.permission.Permission
import org.hedbor.evan.crunchcommands.CrunchCommands
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PERM_MSG
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PLUGIN_ID
import org.bukkit.plugin.java.annotation.command.Command as CommandYml


/**
 * Teleports the caller to the specified player.
 */
@CommandYml(name = "ctp", desc = "Teleports to another player.", permission = "$PLUGIN_ID.ctp", permissionMessage = PERM_MSG, usage = "/ctp <name>")
@Permission(name = "$PLUGIN_ID.ctp", desc = "Allows ctp command", defaultValue = PermissionDefault.TRUE)
class CommandCtp(plugin: CrunchCommands) : CrunchCommand(plugin, isPlayersOnly = true) {
    override fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        val result = super.execute(sender, args)
        if (result !is CommandResult.Success) {
            return result
        }
        sender as Player

        // Must have exactly one target
        if (args.size != 1) {
            return CommandResult.IncorrectUsage()
        }

        // Target must be a connected player
        val targetName = args[0]
        val targets = Bukkit.getOnlinePlayers().filter { it.name == targetName }
        if (targets.isEmpty()) {
            return CommandResult.PlayerNotFound
        } else if (targets.size > 1) {
            return CommandResult.MultiplePlayersFound
        }
        val target = targets[0]

        // Teleport the sender and notify both players.
        sender.teleport(target)
        sender.sendMessage("${ChatColor.YELLOW}You teleported to ${target.displayName}.")
        target.sendMessage("${ChatColor.YELLOW}${sender.displayName} teleported to you.")

        return CommandResult.Success
    }
}