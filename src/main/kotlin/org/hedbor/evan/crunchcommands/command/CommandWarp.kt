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
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.annotation.permission.Permission
import org.hedbor.evan.crunchcommands.CrunchCommands
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PERM_MSG
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PLUGIN_ID
import org.hedbor.evan.crunchcommands.util.Paginator
import org.hedbor.evan.crunchcommands.warp.Warp
import org.bukkit.plugin.java.annotation.command.Command as CommandYml


/**
 * Allows the creation, listation, and usation of warps.
 */
@CommandYml(name = "warp", desc = "Warp base command", permission = "$PLUGIN_ID.warp", permissionMessage = PERM_MSG, usage = "/warp <create|list|use> [<...>]")
class CommandWarp(plugin: CrunchCommands) : BaseCommand(
    plugin,
    "create" to CommandWarpCreate(plugin),
    "list" to CommandWarpList(plugin),
    "use" to CommandWarpUse(plugin)
), TabCompleter {
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


/**
 * Sub-command to create a warp.
 */
@Permission(name = "$PLUGIN_ID.warp.create", desc = "Allows creation of warps", defaultValue = PermissionDefault.FALSE)
class CommandWarpCreate(plugin: CrunchCommands) : SubCommand(plugin, "$PLUGIN_ID.warp.create", true) {
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


/**
 * Sub-command to list all warps. Also aliased as the base command `/warps`.
 */
@CommandYml(name = "warps", desc = "List all warps.", permission = "$PLUGIN_ID.warp.list", permissionMessage = PERM_MSG, usage = "/warps [<page>]")
@Permission(name = "$PLUGIN_ID.warp.list", desc = "Allows listing of warps", defaultValue = PermissionDefault.TRUE)
class CommandWarpList(plugin: CrunchCommands) : SubCommand(plugin, "$PLUGIN_ID.warp.list") {
    override fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        val result = super.execute(sender, args)
        if (result !is CommandResult.Success) return result

        val usageMessage = "Usage: /warp list [<page>]"

        val pageNumber = when (args.size) {
            // defaults to page 1
            0 -> 1
            // use the provided page number if explicitly mentioned
            1 -> args[0].toIntOrNull() ?: return CommandResult.IncorrectUsage(usageMessage)
            else -> return CommandResult.IncorrectUsage(usageMessage)
        }

        val allWarps = plugin.warpManager.warps.map { it.name }.sorted()
        val paginator = Paginator(title = "All Warps", contents = allWarps)
        paginator.sendTo(sender, pageNumber - 1)

        return CommandResult.Success
    }
}


/**
 * Sub-command to use a warp.
 */
@Permission(name = "$PLUGIN_ID.warp.use", desc = "Allows using of warps", defaultValue = PermissionDefault.TRUE)
class CommandWarpUse(plugin: CrunchCommands) : SubCommand(plugin, "$PLUGIN_ID.warp.use",  true) {
    override fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        // validate preconditions
        val result = super.execute(sender, args)
        if (result !is CommandResult.Success) return result
        sender as Player

        // must provide the warp name
        if (args.size != 1) {
            return CommandResult.IncorrectUsage("Usage: /warp use <name>")
        }
        val warpName = args[0]

        val warp = plugin.warpManager.getWarp(warpName) ?: return CommandResult.WarpDoesNotExist(warpName)

        sender.teleport(warp.location)
        sender.sendMessage("Whoosh!")

        return CommandResult.Success
    }
}