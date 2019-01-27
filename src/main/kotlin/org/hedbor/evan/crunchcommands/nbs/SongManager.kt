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

package org.hedbor.evan.crunchcommands.nbs

import org.hedbor.evan.crunchcommands.CrunchCommands
import java.io.DataInputStream
import java.io.File


/**
 * Allows reading of songs created with Note Block Studio (.nbs files).
 */
class SongManager(val plugin: CrunchCommands) {
    fun loadSong(file: File): Song {
        val input = DataInputStream(file.inputStream().buffered())
        lateinit var header: Song.Header
        lateinit var noteBlocks: List<Song.Note>

        input.use {
            header = readHeader(input)
            noteBlocks = readNoteBlocks(input)
        }

        return Song(header, noteBlocks)
    }

    private fun readHeader(input: DataInputStream): Song.Header {
        val songLength = input.readShort()
        val songHeight = input.readShort()
        val songName = input.readNbsString()
        val songAuthor = input.readNbsString()
        val originalSongAuthor = input.readNbsString()
        val songDescription = input.readNbsString()
        val tempo = input.readShort()
        val autoSaving = input.readBoolean()
        val autoSaveDuration = input.readByte()
        val timeSignature = input.readByte()
        val minutesSpent = input.readInt()
        val leftClicks = input.readInt()
        val rightClicks = input.readInt()
        val blocksAdded = input.readInt()
        val blocksRemoved = input.readInt()
        val midiOrSchematicFileName = input.readNbsString()

        return Song.Header(
            songLength.toInt(),
            songHeight.toInt(),
            songName,
            songAuthor,
            originalSongAuthor,
            songDescription,
            tempo.toInt(),
            autoSaving,
            autoSaveDuration.toInt(),
            timeSignature.toInt(),
            minutesSpent,
            leftClicks,
            rightClicks,
            blocksAdded,
            blocksRemoved,
            midiOrSchematicFileName
        )
    }

    private fun readNoteBlocks(input: DataInputStream): List<Song.Note> {
        val noteBlocks = mutableListOf<Song.Note>()
        var tick = -1
        var jumps = 0

        while (true) {
            jumps = input.readShort().toInt()
            if (jumps == 0) {
                break
            }
            tick += jumps

            var layer = -1
            while (true) {
                jumps = input.readShort().toInt()
                if (jumps == 0) {
                    break
                }
                layer += jumps
                val instrument = input.readByte().toInt()
                val key = input.readByte().toInt()
                noteBlocks += Song.Note(tick, layer, instrument, key)
            }
        }

        return noteBlocks.toList()
    }

    private fun DataInputStream.readNbsString(): String {
        val length = this.readInt()
        val bytes = ByteArray(length)
        this.read(bytes)

        return String(bytes)
    }
}

