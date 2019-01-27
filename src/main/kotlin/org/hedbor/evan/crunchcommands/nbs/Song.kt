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

import org.bukkit.Sound


/**
 * A note block song, in .nbs format.
 */
data class Song(
    val header: Header,
    val noteBlocks: List<Note>
) {
    /**
     * Contains information about the song.
     *
     * @param songLength The length of the song in ticks. Divide by tempo to get
     *     length of song in seconds.
     * @param songHeight The last layer with at least one note block, or the last
     *     layer to have its name or volume changed.
     * @param songName The name of the song.
     * @param songAuthor The author of the song.
     * @param originalSongAuthor The original author of the song.
     * @param songDescription The description of the song.
     * @param tempo The tempo of the song multiplied by 100. Measured in ticks per
     *     second.
     * @param autoSaving Whether auto-saving has been enabled (0 or 1).
     * @param autoSavingDuration The amount of minutes between each auto-save (if
     *     enabled) (1-60).
     * @param timeSignature The time signature of the song. If this is 3, then the
     *     signature is 3/4. Ranges from 2-8.
     * @param minutesSpent The amount of minutes spent on the project.
     * @param leftClicks The amount of times the user left clicked.
     * @param rightClicks The amount of times the user right clicked.
     * @param blocksAdded The amount of times the user added a block.
     * @param blocksRemoved The amount of times the user removed a block.
     * @param midiOrSchematicFileName If the song was imported from a .mid or
     *     .schematic file, then that file name is stored here.
     */
    data class Header(
        val songLength: Int,
        val songHeight: Int,
        val songName: String,
        val songAuthor: String,
        val originalSongAuthor: String,
        val songDescription: String,
        val tempo: Int,
        val autoSaving: Boolean,
        val autoSavingDuration: Int,
        val timeSignature: Int,
        val minutesSpent: Int,
        val leftClicks: Int,
        val rightClicks: Int,
        val blocksAdded: Int,
        val blocksRemoved: Int,
        val midiOrSchematicFileName: String
    )

    /**
     * Contains information about how the note blocks are placed, what instruments
     * they have and what note.
     *
     * @param tick The current tick.
     * @param layer The current layer.
     * @param instrument The instrument of the note block. This is 0-9, or higher
     *     if the song has custom instruments.
     * @param key The key of the note block, from 0-87, where 0 is A0 and 87 is C8.
     */
    data class Note(
        val tick: Int,
        val layer: Int,
        val instrument: Int,
        val key: Int
    )

    object Instrument {
        const val PIANO: Byte = 0
        const val DOUBLE_BASS: Byte = 1
        const val BASS_DRUM: Byte = 2
        const val SNARE_DRUM: Byte = 3
        const val CLICK: Byte = 4
        const val GUITAR: Byte = 5
        const val FLUTE: Byte = 6
        const val BELL: Byte = 7
        const val CHIME: Byte = 8
        const val XYLOPHONE: Byte = 9
    }

    enum class Instrument2(val value: Int, val sound: Sound) {
        PIANO(0, Sound.BLOCK_NOTE_BLOCK_HARP),
        DOUBLE_BASS(1, Sound.BLOCK_NOTE_BLOCK_BASS),
        BASS_DRUM(2, Sound.BLOCK_NOTE_BLOCK_BASEDRUM),
        SNARE_DRUM(3, Sound.BLOCK_NOTE_BLOCK_SNARE),
        CLICK(4, Sound.BLOCK_NOTE_BLOCK_HAT),
        GUITAR(5, Sound.BLOCK_NOTE_BLOCK_GUITAR),
        FLUTE(6, Sound.BLOCK_NOTE_BLOCK_FLUTE),
        BELL(7, Sound.BLOCK_NOTE_BLOCK_BELL),
        CHIME(8, Sound.BLOCK_NOTE_BLOCK_CHIME),
        XYLOPHONE(9, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE);

    }
}