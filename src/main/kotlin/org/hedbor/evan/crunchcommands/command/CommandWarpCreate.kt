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

import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.annotation.permission.Permission
import org.hedbor.evan.crunchcommands.CrunchCommands
import org.hedbor.evan.crunchcommands.warp.Warp


/**
 * Sub-command to create a warp.
 *
 * @see CommandWarp
 */
@Permission(name = "${CrunchCommands.PLUGIN_ID}.warp.create", desc = "Allows creation of warps", defaultValue = PermissionDefault.FALSE)
class CommandWarpCreate(plugin: CrunchCommands) : SubCommand(plugin, "${CrunchCommands.PLUGIN_ID}.warp.create", true) {
    override fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        val result = super.execute(sender, args)
        if (result !is CommandResult.Success) return result
        sender as Player

        if (args.size != 1) {
            return CommandResult.IncorrectUsage("Usage: /warp create <name>")
        }
        val warpName = args[0]

        val warpManager = plugin.warpManager
        if (warpManager.getWarp(warpName) != null) {
            return CommandResult.WarpAlreadyExists(warpName)
        }

        val loc = sender.location
        val creator = sender.uniqueId
        warpManager.addWarp(Warp(warpName, loc, creator))

        sender.sendMessage("Successfully created warp \"$warpName\" at ${loc.toReadableString()}.")
        return CommandResult.Success
    }

    private fun Location.toReadableString() = "($blockX, $blockY, $blockZ)"
}
