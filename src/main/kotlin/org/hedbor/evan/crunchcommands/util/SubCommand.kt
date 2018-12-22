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

import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


/**
 * Provides a basic framework for creating sub-commands.
 */
abstract class SubCommand(
    private val permission: String,
    private val isPlayersOnly: Boolean = false
) : CommandExecutor {
    final override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        return if (isPlayersOnly && sender !is Player) {
            sender.sendMessage("${ChatColor.RED}Only players can use this command.")
            true
        } else if (!sender.hasPermission(permission)) {
            sender.sendMessage("${ChatColor.RED}You do not have permission to use this command.")
            true
        } else {
            execute(sender, args)
        }
    }

    abstract fun execute(sender: CommandSender, args: Array<String>): Boolean
}