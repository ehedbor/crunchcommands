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
import org.bukkit.entity.Player
import org.hedbor.evan.crunchcommands.CrunchCommands


/**
 * Sub-command to use a warp.
 *
 * @see CommandWarp
 */
class CommandWarpUse(plugin: CrunchCommands) : SubCommand(plugin, "${CrunchCommands.PLUGIN_ID}.warp.use",  true) {
    override fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        // validate preconditions
        val result = super.execute(sender, args)
        if (result is Failure) return result
        sender as Player

        // must provide the warp name
        if (args.size != 1) {
            return Failure.IncorrectUsage("Usage: /warp use <name>")
        }
        val warpName = args[0]

        val warp = plugin.warpManager.getWarp(warpName) ?: return Failure.WarpDoesNotExist(warpName)

        sender.teleport(warp.location)
        return Success.WarpUsed()
    }

    override fun tabComplete(sender: CommandSender, args: Array<String>): List<String>? {
        return when {
            args.size == 1 -> {
                val warpName = args[0].toLowerCase()
                plugin.warpManager.warps
                    .map { it.name }
                    .filter { it.startsWith(warpName) }
                    .sorted()
            }
            else -> emptyList()
        }
    }
}