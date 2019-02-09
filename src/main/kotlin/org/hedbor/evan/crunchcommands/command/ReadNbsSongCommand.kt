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

package org.hedbor.evan.crunchcommands.command

import org.bukkit.command.CommandSender
import org.hedbor.evan.crunchcommands.CrunchCommands


class ReadNbsSongCommand(plugin: CrunchCommands) : CrunchCommand(plugin) {
    override fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        val result = super.execute(sender, args)
        if (result is Failure) {
            return result
        }

        if (args.isEmpty()) {
            return Failure.IncorrectUsage()
        }

        val songName = args[0]
        val songFile = plugin.dataFolder.resolve("songs/$songName.nbs")
        val song = plugin.songManager.loadSong(songFile)

        song.header.apply {
            sender.sendMessage(arrayOf(
                "Length: $songLength ticks",
                "        ${songLength / (tempo / 100.0)} sec",
                "Height: $songHeight layers",
                "Name: $songName",
                "Author: $songAuthor,",
                "Original author: $originalSongAuthor",
                "Description: $songDescription",
                "Tempo: ${tempo / 100.0} tps",
                "Auto-saving: $autoSaving",
                "Auto-save duration: $autoSavingDuration min",
                "Time signature: $timeSignature/4",
                "Time spent: $minutesSpent min"
            ))
        }
        sender.sendMessage("Song contents:")
        for (noteBlock in song.noteBlocks) {
            sender.sendMessage("T${noteBlock.tick} / L${noteBlock.layer}   K${noteBlock.key} | ${noteBlock.instrument}")
        }

        return Success.Generic()
    }
}