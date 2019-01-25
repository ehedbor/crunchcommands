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
import org.hedbor.evan.crunchcommands.command.BaseCommand
import org.hedbor.evan.crunchcommands.command.CrunchCommand


/**
 * Allows full control over warps.
 *
 * @see CreateWarpCommand
 * @see ListWarpCommand
 * @see UseWarpCommand
 * @see RemoveWarpCommand
 */
class WarpCommand(plugin: CrunchCommands) : BaseCommand(
    plugin,
    subCommands = mapOf(
        "create" to CreateWarpCommand(plugin),
        "list" to ListWarpCommand(plugin),
        "use" to UseWarpCommand(plugin),
        "remove" to RemoveWarpCommand(plugin)
    )
)


/**
 * Returns proper tab completion for warp names.
 */
@Suppress("UNUSED_PARAMETER")
internal fun CrunchCommand.tabCompleteWarp(sender: CommandSender, args: Array<String>): List<String>? {
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