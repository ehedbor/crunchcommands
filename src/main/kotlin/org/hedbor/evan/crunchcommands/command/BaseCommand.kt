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

import org.bukkit.command.CommandSender
import org.hedbor.evan.crunchcommands.CrunchCommands
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PLUGIN_ID


/**
 * Provides a basic framework for creating base commands.
 */
abstract class BaseCommand(
    plugin: CrunchCommands,
    private val baseCommandName: String,
    protected val subCommands: Map<String, SubCommand> = emptyMap()
) : CrunchCommand(plugin) {

    override fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        val result = super.execute(sender, args)
        if (result is Failure) {
            return result
        }

        if (args.isEmpty()) {
            return Failure.IncorrectUsage()
        }

        val subCommandName = args[0]
        val subCommand = subCommands[subCommandName]

        return subCommand?.execute(sender, args.copyOfRange(1, args.size)) ?: Failure.IncorrectUsage()
    }

    override fun tabComplete(sender: CommandSender, args: Array<String>): List<String>? {
        return when {
            args.size == 1 -> {
                val subCommand = args[0].toLowerCase()
                this.subCommands
                    .map { it.key.toLowerCase() }
                    .filter { sender.hasPermission("$PLUGIN_ID.$baseCommandName.$it") }
                    .filter { it.startsWith(subCommand) }
                    .sorted()
            }
            args.size > 1 -> {
                val subCommand = subCommands[args[0]]
                when (subCommand) {
                    null -> emptyList()
                    else -> subCommand.tabComplete(sender, args.copyOfRange(1, args.size))
                }
            }
            else -> super.tabComplete(sender, args)
        }
    }
}