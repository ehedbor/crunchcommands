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
import org.hedbor.evan.crunchcommands.util.toReadableString
import org.hedbor.evan.crunchcommands.warp.Warp


// Note: a null message means that nothing should be displayed to the user.

/**
 * Indicates the success or failure of a [CrunchCommand].
 */
sealed class CommandResult(val message: String? = null)


/**
 * Indicates that a [CrunchCommand] succeeded.
 */
sealed class Success(message: String? = null) : CommandResult(message) {
    class Generic : Success("Command succeeded successfully.")
    class DirtReceived : Success("You have received: ${ChatColor.RED}EMERGENCY DIRT${CrunchCommand.SUCCESS_COLOR}!")
    class SentToHeaven : Success("${ChatColor.LIGHT_PURPLE}haha lol welcome to the sky")
    class TeleportedToPlayer(playerName: String) : Success("You teleported to $playerName.")
    class WarpCreated(warp: Warp) : Success("Successfully created warp \"${warp.name}\" at ${warp.location.toReadableString()}.")
    class WarpRemoved(warp: Warp) : Success("Successfully removed warp \"${warp.name}\" at ${warp.location.toReadableString()}.")
    class WarpsListed : Success()
    class WarpUsed : Success("Whoosh!")
}


/**
 * Indicates that a [CrunchCommand] failed.
 */
sealed class Failure(message: String? = null) : CommandResult(message) {
    class Generic : Failure("Command failed successfully.")
    class IncorrectUsage(usage: String? = null) : Failure(usage)
    class MultiplePlayersFound : Failure("Multiple players found with that name.")
    class NoPermission : Failure("You do not have permission to use this command.")
    class PlayerNotFound : Failure("No player found with that name.")
    class PlayersOnly : Failure("Only players can use this command.")
    class WarpAlreadyExists(warpName: String) : Failure("Warp \"$warpName\" already exists.")
    class WarpDoesNotExist(warpName: String) : Failure("Warp \"$warpName\" does not exist.")
}