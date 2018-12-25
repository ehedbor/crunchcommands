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


/**
 * Indicates the success or failure of a [CrunchCommand].
 */
sealed class CommandResult(val message: String? = null) {
    object Success : CommandResult()

    class IncorrectUsage(usage: String? = null) : CommandResult(usage)
    object PlayersOnly : CommandResult("Only players can use this command.")
    object NoPermission : CommandResult("You do not have permission to use this command.")
    class WarpAlreadyExists(warpName: String) : CommandResult("Warp \"$warpName\" already exists.")
    class WarpDoesNotExist(warpName: String) : CommandResult("Warp \"$warpName\" does not exist.")
    object PlayerNotFound : CommandResult("No player found with that name.")
    object MultiplePlayersFound : CommandResult("Multiple players found with that name.")
}