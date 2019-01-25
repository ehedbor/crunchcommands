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
import org.hedbor.evan.crunchcommands.command.CrunchCommand
import org.hedbor.evan.crunchcommands.command.Failure
import org.hedbor.evan.crunchcommands.command.Success
import org.hedbor.evan.crunchcommands.warp.Warp


/**
 * Sets a player's home.
 */
class SetHomeCommand(plugin: CrunchCommands) : CrunchCommand(plugin, isPlayersOnly = true) {
    override fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        val result = super.execute(sender, args)
        if (result is Failure) return result
        sender as Player

        val home = Warp(Warp.getHomeNameFor(sender), sender.location, sender.uniqueId)
        plugin.warpManager.setWarp(home)

        return Success.HomeSet(home)
    }
}