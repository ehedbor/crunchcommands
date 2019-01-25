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

package org.hedbor.evan.crunchcommands.warp

import org.bukkit.entity.Player
import org.hedbor.evan.crunchcommands.CrunchCommands


/**
 * Responsible for managing the list of [Warp]s.
 */
class WarpManager(val plugin: CrunchCommands, warps: List<Warp> = emptyList()) {
    /**
     * The warp list, ordered alphabetically.
     */
    var warps = warps
        private set

    /**
     * Returns the [Warp] with the given [name], or `null` if it doesn't exist.
     */
    fun getWarp(name: String): Warp? {
        return warps.firstOrNull { it.name == name }
    }

    /**
     * Returns the home for the given [player], or `null` if it doesn't exist.
     */
    fun getHome(player: Player): Warp? {
        return getWarp(Warp.getHomeNameFor(player))
    }

    /**
     * Adds a [Warp] if one with the same name does not already exist.
     *
     * @return `true` if the [Warp] was removed, `false` if the [Warp] never existed.
     */
    fun addWarp(warp: Warp): Boolean {
        return if (getWarp(warp.name) != null) {
            false
        } else {
            warps += warp
            plugin.saveConfig()
            true
        }
    }

    /**
     * Removes the [Warp] with the given [name].
     *
     * @return The removed [Warp], or `null` if it never existed.
     */
    fun removeWarp(name: String): Warp? {
        val warp = getWarp(name)
        if (warp != null) {
            warps -= warp
        }
        return warp
    }

    /**
     * Forcibly adds a [Warp], removing existing warps.
     */
    fun setWarp(warp: Warp) {
        removeWarp(warp.name)
        addWarp(warp)
    }
}