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
import org.bukkit.entity.Player
import org.hedbor.evan.crunchcommands.CrunchCommands
import org.hedbor.evan.crunchcommands.command.CommandResult
import org.hedbor.evan.crunchcommands.command.Failure
import org.hedbor.evan.crunchcommands.command.SubCommand
import org.hedbor.evan.crunchcommands.command.Success
import org.hedbor.evan.crunchcommands.warp.Warp


/**
 * Sub-command to create a warp.
 *
 * @see WarpCommand
 */
class CreateWarpCommand(plugin: CrunchCommands) : SubCommand(plugin, "${CrunchCommands.PLUGIN_ID}.warp.create", true) {
    override fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        val result = super.execute(sender, args)
        if (result is Failure) return result
        sender as Player

        if (args.size != 1) {
            return Failure.IncorrectUsage("Usage: /warp create <name>")
        }
        val warpName = args[0].toLowerCase()

        // matches alphanumeric characters, underscore, and dash
        if (!warpName.matches(Regex("^[\\w-]+$"))) {
            return Failure.BadWarpName(warpName);
        }

        if (plugin.warpManager.getWarp(warpName) != null) {
            return Failure.WarpAlreadyExists(warpName)
        }

        val warp = Warp(warpName, sender.location, sender.uniqueId)
        plugin.warpManager.addWarp(warp)

        return Success.WarpCreated(warp)
    }

}
