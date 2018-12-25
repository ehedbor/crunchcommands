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

package org.hedbor.evan.crunchcommands.warp


/**
 * Responsible for managing the list of warps and their locations.
 */
class WarpManager(warps: List<Warp> = emptyList()) {
    var warps = warps
        private set

    fun getWarp(name: String): Warp? {
        return warps.firstOrNull { it.name == name }
    }

    /**
     * Adds a warp if one with the given name does not already exist.
     */
    fun addWarp(warp: Warp): Boolean {
        return if (getWarp(warp.name) != null) {
            false
        } else {
            warps += warp
            true
        }
    }
}