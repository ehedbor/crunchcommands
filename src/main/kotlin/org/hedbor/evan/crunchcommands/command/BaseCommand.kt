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


/**
 * Provides a basic framework for creating base commands.
 */
abstract class BaseCommand(
    plugin: CrunchCommands,
    protected val subCommands: Map<String, SubCommand> = emptyMap()
) : CrunchCommand(plugin) {

    constructor(plugin: CrunchCommands, vararg subCommands: Pair<String, SubCommand>) : this(plugin, subCommands.toMap())

    override fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        val result = super.execute(sender, args)
        if (result !is CommandResult.Success) {
            return result
        }

        if (args.isEmpty()) {
            return CommandResult.IncorrectUsage()
        }

        val subCommandName = args[0]
        val subCommand = subCommands[subCommandName]

        return subCommand?.execute(sender, args.copyOfRange(1, args.size))
            ?: CommandResult.IncorrectUsage()
    }
}