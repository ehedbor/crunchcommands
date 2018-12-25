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
    protected val plugin: CrunchCommands,
    protected val permission: String? = null,
    protected val isPlayersOnly: Boolean = false
) : CommandExecutor, TabCompleter {

    /**
     * Executes the command.
     *
     * @return `false` if the command was used incorrectly and a usage message should be displayed.
     */
    final override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val result = execute(sender, args)

        return if (result == CommandResult.Success) {
            true
        } else {
            if (result.message != null) {
                sender.sendMessage("" + ChatColor.RED + result.message)
            }

            return !(result is CommandResult.IncorrectUsage && result.message != null)
        }
    }

    /**
     * Executes the command.
     *
     * @return [CommandResult.Success] if the command was used correctly.
     */
    open fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        return if (isPlayersOnly && sender !is Player) {
            CommandResult.PlayersOnly
        } else if (!sender.hasPermission(permission)) {
            CommandResult.NoPermission
        } else {
            CommandResult.Success
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