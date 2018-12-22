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

import net.md_5.bungee.api.ChatColor
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.annotation.permission.ChildPermission
import org.bukkit.plugin.java.annotation.permission.Permission
import org.hedbor.evan.crunchcommands.CrunchCommands
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.CMD_USAGE
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PERM_MSG
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PLUGIN_ID
import org.hedbor.evan.crunchcommands.util.BaseCommand
import org.hedbor.evan.crunchcommands.util.Paginator
import org.hedbor.evan.crunchcommands.util.SubCommand
import org.hedbor.evan.crunchcommands.warp.Warp
import org.bukkit.plugin.java.annotation.command.Command as CommandYml


/**
 * Allows the creation, listation, and usation of warps.
 */
@CommandYml(name = "warp", desc = "Warp base command", permission = "$PLUGIN_ID.warp", permissionMessage = PERM_MSG, usage = "$CMD_USAGE <create|list|use> [<...>]")
@Permission(name = "$PLUGIN_ID.warp.*", desc = "Allows creating, listing, and using warps.", defaultValue = PermissionDefault.OP, children = [
    ChildPermission(name = "$PLUGIN_ID.warp.create"),
    ChildPermission(name = "$PLUGIN_ID.warp.list"),
    ChildPermission(name = "$PLUGIN_ID.warp.use")
])
object CommandWarp : BaseCommand(
    "create" to CommandWarpCreate,
    "list" to CommandWarpList,
    "use" to CommandWarpUse
) {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        return super.onCommand(sender, command, label, args)
    }
}

@Permission(name = "$PLUGIN_ID.warp.create", desc = "Allows creation of warps", defaultValue = PermissionDefault.FALSE)
object CommandWarpCreate : SubCommand("$PLUGIN_ID.warp.create", isPlayersOnly = true) {
    override fun execute(sender: CommandSender, args: Array<String>): Boolean {

        // must provide the warp name
        if (args.size != 1) {
            sender.sendMessage("${ChatColor.RED}Usage: /warp create <name>")
            return true
        }
        val warpName = args[0]

        val warpManager = CrunchCommands.instance.warpManager
        if (warpManager.getWarp(warpName) != null) {
            sender.sendMessage("${ChatColor.RED}Warp \"$warpName\" already exists.")
            return true
        }

        val loc = (sender as Player).location
        warpManager.addWarp(Warp(warpName, loc))
        sender.sendMessage("Successfully created warp \"$warpName\" at ${loc.toReadableString()}.")

        return true
    }

    private fun Location.toReadableString() = "($blockX, $blockY, $blockZ)"
}

@Permission(name = "$PLUGIN_ID.warp.list", desc = "Allows listing of warps", defaultValue = PermissionDefault.TRUE)
object CommandWarpList : SubCommand("$PLUGIN_ID.warp.list") {
    override fun execute(sender: CommandSender, args: Array<String>): Boolean {
        val usageMessage = "${ChatColor.RED}Usage: /warp list [<page>]"
        val pageNumber = when (args.size) {
            // defaults to page 1
            0 -> 1
            // use the provided page number if explicitly mentioned
            1 -> {
                val pageOrNull = args[0].toIntOrNull()
                if (pageOrNull == null) {
                    sender.sendMessage(usageMessage)
                    return true
                }
                pageOrNull
            }
            else -> {
                sender.sendMessage(usageMessage)
                return true
            }
        }

        val warpManager = CrunchCommands.instance.warpManager
        val allWarps = warpManager.warps.map { it.name }.sorted()
        val paginator = Paginator(title = "All Warps", contents = allWarps)
        paginator.sendTo(sender, pageNumber - 1)

        return true
    }
}

@Permission(name = "$PLUGIN_ID.warp.use", desc = "Allows using of warps", defaultValue = PermissionDefault.TRUE)
object CommandWarpUse : SubCommand("$PLUGIN_ID.warp.use", isPlayersOnly = true) {
    override fun execute(sender: CommandSender, args: Array<String>): Boolean {
        // must provide the warp name
        if (args.size != 1) {
            sender.sendMessage("${ChatColor.RED}Usage: /warp use <name>")
            return true
        }
        val warpName = args[0]

        val warp = CrunchCommands.instance.warpManager.getWarp(warpName)
        if (warp == null) {
            sender.sendMessage("${ChatColor.RED}Warp \"$warpName\" does not exist.")
            return true
        }

        sender as Player
        sender.teleport(warp.location)
        sender.sendMessage("Whoosh!")

        return true
    }
}