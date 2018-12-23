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

import org.bukkit.Location
import org.bukkit.configuration.serialization.ConfigurationSerializable


/**
 * Represents a warp with a name and location.
 */
data class Warp(val name: String, val location: Location) : ConfigurationSerializable {
    override fun serialize(): MutableMap<String, Any> {
        return mutableMapOf(
            "name" to name,
            "location" to location.serialize()
        )
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        fun deserialize(values: Map<String, Any>): Warp {
            val name = values["name"] as? String
            val location = values["location"] as? Map<*, *>
            requireNotNull(name) { "Name not present" }
            requireNotNull(location) { "Location not preset" }

            @Suppress("UNCHECKED_CAST")
            return Warp(name, Location.deserialize(location as MutableMap<String, Any>))
        }
    }
}