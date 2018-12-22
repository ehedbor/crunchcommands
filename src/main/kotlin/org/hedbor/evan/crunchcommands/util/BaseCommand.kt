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

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender


/**
 * Provides a basic framework for creating base commands.
 */
abstract class BaseCommand(
    private val subCommands: Map<String, SubCommand> = emptyMap()
) : CommandExecutor {

    constructor(vararg subCommands: Pair<String, SubCommand>) : this(subCommands.toMap())

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            return false
        }

        val subCommandName = args[0]
        val subCommand = subCommands[subCommandName]

        return if (subCommand == null) {
            false
        } else {
            subCommand.onCommand(sender, command, label, args.copyOfRange(1, args.size))
            true
        }
    }
}