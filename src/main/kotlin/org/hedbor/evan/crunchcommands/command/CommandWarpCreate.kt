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
import org.hedbor.evan.crunchcommands.warp.Warp


/**
 * Sub-command to create a warp.
 *
 * @see CommandWarp
 */
class CommandWarpCreate(plugin: CrunchCommands) : SubCommand(plugin, "${CrunchCommands.PLUGIN_ID}.warp.create", true) {
    override fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        val result = super.execute(sender, args)
        if (result is Failure) return result
        sender as Player

        if (args.size != 1) {
            return Failure.IncorrectUsage("Usage: /warp create <name>")
        }
        val warpName = args[0].toLowerCase()

        val warpManager = plugin.warpManager
        if (warpManager.getWarp(warpName) != null) {
            return Failure.WarpAlreadyExists(warpName)
        }

        val warp = Warp(warpName, sender.location, sender.uniqueId)
        warpManager.addWarp(warp)

        return Success.WarpCreated(warp)
    }

}
