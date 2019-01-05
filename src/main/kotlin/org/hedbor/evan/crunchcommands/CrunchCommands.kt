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
import org.hedbor.evan.crunchcommands.command.*
import org.hedbor.evan.crunchcommands.util.getObjectList
import org.hedbor.evan.crunchcommands.util.registerCommands
import org.hedbor.evan.crunchcommands.warp.Warp
import org.hedbor.evan.crunchcommands.warp.WarpManager


/**
 * The main plugin class.
 */
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