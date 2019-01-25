/*
 * Copyright (C) 2018-2019 Evan Hedbor.
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

package org.hedbor.evan.crunchcommands.command.warp

import org.bukkit.command.CommandSender
import org.hedbor.evan.crunchcommands.CrunchCommands
import org.hedbor.evan.crunchcommands.command.CommandResult
import org.hedbor.evan.crunchcommands.command.Failure
import org.hedbor.evan.crunchcommands.command.SubCommand
import org.hedbor.evan.crunchcommands.command.Success
import org.hedbor.evan.crunchcommands.util.Paginator


/**
 * Sub-command to list all warps. Also aliased as the base command `/warps`.
 *
 * @see WarpCommand
 */
class ListWarpCommand(plugin: CrunchCommands) : SubCommand(plugin, "${CrunchCommands.PLUGIN_ID}.warp.list") {
    override fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        val result = super.execute(sender, args)
        if (result is Failure) return result

        val usageMessage = "Usage: /warp list [<page>]"

        val pageNumber = when (args.size) {
            // defaults to page 1
            0 -> 1
            // use the provided page number if explicitly mentioned
            1 -> args[0].toIntOrNull() ?: return Failure.IncorrectUsage(
                usageMessage
            )
            else -> return Failure.IncorrectUsage(usageMessage)
        }

        val allWarps = plugin.warpManager.warps.map { it.name }.sorted()
        val paginator = Paginator(title = "All Warps", contents = allWarps)
        paginator.sendTo(sender, pageNumber - 1)

        return Success.WarpsListed()
    }
}