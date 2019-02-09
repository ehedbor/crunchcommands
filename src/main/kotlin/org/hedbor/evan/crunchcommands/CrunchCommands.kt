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

package org.hedbor.evan.crunchcommands

import org.bukkit.plugin.java.JavaPlugin
import org.hedbor.evan.crunchcommands.annotation.*
import org.hedbor.evan.crunchcommands.command.CtpCommand
import org.hedbor.evan.crunchcommands.command.DirtCommand
import org.hedbor.evan.crunchcommands.command.ReadNbsSongCommand
import org.hedbor.evan.crunchcommands.command.SuicideCommand
import org.hedbor.evan.crunchcommands.command.warp.ListWarpCommand
import org.hedbor.evan.crunchcommands.command.warp.WarpCommand
import org.hedbor.evan.crunchcommands.nbs.SongManager
import org.hedbor.evan.crunchcommands.util.ConfigurationSerialization
import org.hedbor.evan.crunchcommands.util.getObjectList
import org.hedbor.evan.crunchcommands.util.registerCommands
import org.hedbor.evan.crunchcommands.warp.Warp
import org.hedbor.evan.crunchcommands.warp.WarpManager


/**
 * The main plugin class.
 */
@Plugin(
    name = "CrunchCommands",
    version = "@VERSION@",
    apiVersion = "1.13",
    description = "A Spigot version of CrunchCommands!",
    authors = ["Evan Hedbor"],
    permissionPrefix = "crunchcommands",
    commands = [
        Command("dirt", "Provides emergency dirt.", "/dirt", "$.dirt"),
        Command("ctp", "Teleports you to another player.", "/ctp <name>", "$.ctp"),
        Command("home", "Teleports you home.", "/home", "$.home.use"),
        Command("readsong", "Reads a song.", "/readsong <song>", "$.song.read"),
        Command("sethome", "Sets your home.", "/sethome", "$.home.set"),
        Command("suicide", "Ends your life.", "/<command>", "$.suicide", aliases = ["seppuku", "stuck"]),
        Command("warp", "Base warp command", "/warp <create|list|remove|use> [<...>]", "$.warp"),
        Command("warps", "Alias for /warp list", "/warps [<page>]", "$.warp.list")
    ],
    permissions = [
        Permission("$.dirt", "Allows dirt command", PermissionDefault.ALWAYS),
        Permission("$.ctp", "Allows ctp command", PermissionDefault.ALWAYS),
        Permission("$.home.set", "Allows sethome command", PermissionDefault.ALWAYS),
        Permission("$.home.use", "Allows home command", PermissionDefault.ALWAYS),
        Permission("$.home.*", "Allows full control over homes", PermissionDefault.NEVER, [
            ChildPermission("$.home.set"),
            ChildPermission("$.home.use")
        ]),
        Permission("$.song.read", "Allows readsong command", PermissionDefault.ALWAYS),
        Permission("$.suicide", "Allows suicide command", PermissionDefault.ALWAYS),
        Permission("$.warp", "Allows warp base command", PermissionDefault.ALWAYS),
        Permission("$.warp.create", "Allows warp creation", PermissionDefault.OP),
        Permission("$.warp.remove", "Allows warp deletion", PermissionDefault.OP),
        Permission("$.warp.list", "Allows warp listing", PermissionDefault.ALWAYS),
        Permission("$,warp.use", "Allows warp use", PermissionDefault.ALWAYS),
        Permission("$.warp.*", "Allows full control over warps", PermissionDefault.NEVER, [
            ChildPermission("$.warp"),
            ChildPermission("$.warp.create"),
            ChildPermission("$.warp.remove"),
            ChildPermission("$.warp.list"),
            ChildPermission("$.warp.use")
        ]),
        Permission("$.*", "Wildcard permission", PermissionDefault.NEVER, [
            ChildPermission("$.dirt"),
            ChildPermission("$.ctp"),
            ChildPermission("$.suicide"),
            ChildPermission("$.warp.*")
        ])
    ]
)
class CrunchCommands : JavaPlugin() {
    companion object {
        internal const val PLUGIN_ID = "crunchcommands"
    }

    lateinit var warpManager: WarpManager
    lateinit var songManager: SongManager

    override fun onEnable() {
        registerCommands(
            "dirt" to DirtCommand(this),
            "ctp" to CtpCommand(this),
            "readsong" to ReadNbsSongCommand(this),
            "suicide" to SuicideCommand(this),
            "warp" to WarpCommand(this),
            "warps" to ListWarpCommand(this)
        )

        songManager = SongManager(this)
        setupConfig()
    }

    override fun onDisable() {
        saveConfig()
    }

    private fun setupConfig() {
        ConfigurationSerialization.registerClass<Warp>()
        config.addDefault("warps", emptyList<Warp>())

        val warps = config.getObjectList<Warp>("warps")
        warpManager = WarpManager(this, warps)
    }

    override fun saveConfig() {
        config["warps"] = warpManager.warps
        super.saveConfig()
    }
}