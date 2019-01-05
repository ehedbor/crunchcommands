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

package org.hedbor.evan.crunchcommands.command

import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.hedbor.evan.crunchcommands.CrunchCommands


/**
 * The base class for all CrunchCommands.
 *
 * @param plugin The plugin instance.
 * @param permission The permission to check before using this command. Set to `null` to disable this check.
 * @param isPlayersOnly Set to `true` to enforce players-only.
 */
abstract class CrunchCommand(
    internal val plugin: CrunchCommands,
    internal val permission: String? = null,
    internal val isPlayersOnly: Boolean = false
) : CommandExecutor, TabCompleter {

    companion object {
        internal val SUCCESS_COLOR = ChatColor.YELLOW
        internal val FAILURE_COLOR = ChatColor.RED
    }

    /**
     * Executes the command.
     *
     * @return `false` if the command was used incorrectly and a usage message should be displayed.
     */
    final override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val result = execute(sender, args)

        val chatColor = when (result) {
            is Success -> SUCCESS_COLOR
            is Failure -> FAILURE_COLOR
        }

        if (result.message != null) {
            sender.sendMessage("$chatColor${result.message}")
        }

        val shouldShowUsage = (result is Failure.IncorrectUsage && result.message == null)
        return !shouldShowUsage
    }

    /**
     * Executes the command.
     *
     * @return [Success] if the command was used correctly.
     */
    open fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        return if (isPlayersOnly && sender !is Player) {
            Failure.PlayersOnly()
        } else if (permission != null && !sender.hasPermission(permission)) {
            Failure.NoPermission()
        } else {
            Success.Generic()
        }
    }

    final override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String>? {
        return tabComplete(sender, args)
    }

    /**
     * Provides tab completion options.
     *
     * @return The list of tab completions, or `null` to use the default tab completer.
     */
    open fun tabComplete(sender: CommandSender, args: Array<String>): List<String>? {
        return emptyList()
    }
}