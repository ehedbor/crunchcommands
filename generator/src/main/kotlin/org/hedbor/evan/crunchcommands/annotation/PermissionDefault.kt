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

package org.hedbor.evan.crunchcommands.annotation


/**
 * The default value of a permission.
 */
enum class PermissionDefault(private val ymlName: String) {
    /** Always grant the player the permission. */
    ALWAYS("true"),
    /** Never grant the player the permission. */
    NEVER("false"),
    /** Grant the permission if the player is an operator. */
    OP("op"),
    /** Grant the permission if the player is *not* an operator. */
    NOT_OP("not op");

    override fun toString() = ymlName
}