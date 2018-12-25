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
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PERM_MSG
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PLUGIN_ID
import org.bukkit.plugin.java.annotation.command.Command as CommandYml


/**
 * Allows full control over warps.
 *
 * @see CommandWarpCreate
 * @see CommandWarpList
 * @see CommandWarpUse
 * @see CommandWarpRemove
 */
@CommandYml(name = "warp", desc = "Warp base command", permission = "$PLUGIN_ID.warp", permissionMessage = PERM_MSG, usage = "/warp <create|list|use> [<...>]")
class CommandWarp(plugin: CrunchCommands) : BaseCommand(
    plugin,
    mapOf(
        "create" to CommandWarpCreate(plugin),
        "list" to CommandWarpList(plugin),
        "use" to CommandWarpUse(plugin),
        "remove" to CommandWarpRemove(plugin)
    )
) {
    override fun tabComplete(sender: CommandSender, args: Array<String>): List<String>? = when {
        args.isEmpty() -> {
            subCommands
                .map { it.key }
                .filter { subCommand -> sender.hasPermission("$PLUGIN_ID.warp.$subCommand") }
        }
        else -> {
            val subCommand = args[0]
            when {
                subCommand == "use" -> {
                    val warpName = args.getOrElse(1) { "" }
                    plugin.warpManager.warps
                        .map { it.name }
                        .filter { it.startsWith(warpName) }
                }
                args.size == 1 -> {
                    subCommands
                        .map { it.key }
                        .filter { sender.hasPermission("$PLUGIN_ID.warp.$it") }
                        .filter { it.startsWith(subCommand) }
                }
                else -> emptyList()
            }
        }
    }
}