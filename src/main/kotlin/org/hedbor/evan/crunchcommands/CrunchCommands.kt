/*
 * Copyright (C) 2019 Evan Hedbor.
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

import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PLUGIN_ID
import org.hedbor.evan.crunchcommands.annotation.*
import org.hedbor.evan.crunchcommands.command.*
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
    commands = [
        Command("dirt", "Provides emergency dirt.", "/dirt", "$PLUGIN_ID.dirt"),
        Command("ctp", "Teleports you to another player.", "/ctp <name>", "$PLUGIN_ID.ctp"),
        Command("suicide", "Ends your life.", "/suicide", "$PLUGIN_ID.suicide"),
        Command("warp", "Base warp command", "/warp <create|list|remove|use> [<...>]", "$PLUGIN_ID.warp"),
        Command("warps", "Alias for /warp list", "/warps [<page>]", "$PLUGIN_ID.warp.list")
    ],
    permissions = [
        Permission("$PLUGIN_ID.dirt", "Allows dirt command", PermissionDefault.ALWAYS),
        Permission("$PLUGIN_ID.ctp", "Allows ctp command", PermissionDefault.ALWAYS),
        Permission("$PLUGIN_ID.suicide", "Allows suicide command", PermissionDefault.ALWAYS),
        Permission("$PLUGIN_ID.warp", "Allows warp base command", PermissionDefault.ALWAYS),
        Permission("$PLUGIN_ID.warp.create", "Allows warp creation", PermissionDefault.OP),
        Permission("$PLUGIN_ID.warp.remove", "Allows warp deletion", PermissionDefault.OP),
        Permission("$PLUGIN_ID.warp.list", "Allows warp listing", PermissionDefault.ALWAYS),
        Permission("$PLUGIN_ID,warp.use", "Allows warp use", PermissionDefault.ALWAYS),
        Permission("$PLUGIN_ID.warp.*", "Allows full control over warps", PermissionDefault.NEVER, [
            ChildPermission("$PLUGIN_ID.warp"),
            ChildPermission("$PLUGIN_ID.warp.create"),
            ChildPermission("$PLUGIN_ID.warp.remove"),
            ChildPermission("$PLUGIN_ID.warp.list"),
            ChildPermission("$PLUGIN_ID.warp.use")
        ]),
        Permission("$PLUGIN_ID.*", "Wildcard permission", PermissionDefault.NEVER, [
            ChildPermission("$PLUGIN_ID.dirt"),
            ChildPermission("$PLUGIN_ID.ctp"),
            ChildPermission("$PLUGIN_ID.suicide"),
            ChildPermission("$PLUGIN_ID.warp.*")
        ])
    ]
)
class CrunchCommands : JavaPlugin() {
    lateinit var warpManager: WarpManager

    companion object {
        internal const val PLUGIN_ID = "crunchcommands"
        internal const val PERM_MSG = "§cYou do not have permission to use this command."
    }

    override fun onEnable() {
        registerCommands(
            "dirt" to CommandDirt(this),
            "ctp" to CommandCtp(this),
            "suicide" to CommandSuicide(this),
            "warp" to CommandWarp(this),
            "warps" to CommandWarpList(this)
        )

        setupConfig()
    }

    override fun onDisable() {
        saveConfig()
    }

    private fun setupConfig() {
        ConfigurationSerialization.registerClass(Warp::class.java, "org.hedbor.evan.crunchcommands.warp.Warp")
        config.addDefault("warps", emptyList<Warp>())

        val warps = config.getObjectList<Warp>("warps")
        warpManager = WarpManager(this, warps)
    }

    override fun saveConfig() {
        config["warps"] = warpManager.warps
        super.saveConfig()
    }
}