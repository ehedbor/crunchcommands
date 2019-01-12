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
 * Represents a command and its information.
 *
 * @param name The command's name.
 * @param description A short description of what the command does.
 * @param usage A short description of how to use this command.
 * @param permission The most basic permission node required to use the command.
 * @param permissionMessage A no-permission message which is displayed to a
 *     user if they do not have the required permission to use this command.
 * @param aliases Alternate command names a user may use instead.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Command(
    val name: String,
    val description: String = "",
    val usage: String = "",
    val permission: String = "",
    val permissionMessage: String = "§cYou do not have permission to use this command.",
    val aliases: Array<String> = []
)