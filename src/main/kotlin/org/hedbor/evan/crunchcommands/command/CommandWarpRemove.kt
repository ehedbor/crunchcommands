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
 * Allows warps to be removed.
 */
class CommandWarpRemove(plugin: CrunchCommands) : SubCommand(plugin, "$PLUGIN_ID.warp.remove") {
    override fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        val result = super.execute(sender, args)
        if (result is Failure) {
            return result
        }

        if (args.size != 1) {
            return Failure.IncorrectUsage("Usage: /warp remove <name>")
        }

        val warpName = args[0]
        val warp = plugin.warpManager.removeWarp(warpName)

        return if (warp != null) {
            Success.WarpRemoved(warp)
        } else {
            Failure.WarpDoesNotExist(warpName)
        }
    }
}