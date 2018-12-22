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

package org.hedbor.evan.crunchcommands.util

import org.bukkit.command.CommandSender
import kotlin.math.min


/**
 * Allows the creation of paginated lists.
 */
class Paginator(
    private val title: String? = null,
    private val contents: List<String> = emptyList(),
    private val header: String? = null,
    private val footer: String? = null,
    private val padding: String = "-"
) {
    companion object {
        private const val ROW_WIDTH = 55
        private const val ITEMS_PER_PAGE = 8
    }

    init {
        require(padding.isNotEmpty()) { "Padding must have a length of at least 1" }
    }

    fun sendTo(sender: CommandSender, pageIndex: Int = 0) {
        sendTopRow(sender)
        sendHeader(sender)
        sendContents(sender, pageIndex)
        sendBottomRow(sender, pageIndex)
        sendFooter(sender)
    }

    private fun sendTopRow(sender: CommandSender) {
        if (title == null) {
            sender.sendMessage(repeatPadding(ROW_WIDTH))
        } else {
            val pad = repeatPadding((ROW_WIDTH - title.length - 2) / 2)
            sender.sendMessage("$pad $title $pad")
        }
    }

    private fun sendHeader(sender: CommandSender) {
        if (header != null) {
            sender.sendMessage(header)
        }
    }

    private fun sendContents(sender: CommandSender, pageIndex: Int) {
        val startIndex = pageIndex * ITEMS_PER_PAGE
        val endIndex = min(startIndex + ITEMS_PER_PAGE, contents.size)
        if (startIndex in 0..contents.size) {
            contents.subList(startIndex, endIndex).forEach(sender::sendMessage)
        }
    }

    private fun sendBottomRow(sender: CommandSender, pageIndex: Int) {
        val currentPage = pageIndex + 1
        val pageCount = contents.size / ITEMS_PER_PAGE + 1
        val pageInfo = "< $currentPage/$pageCount >"
        val pad = repeatPadding((ROW_WIDTH - pageInfo.length - 2) / 2)
        sender.sendMessage("$pad $pageInfo $pad")
    }

    private fun sendFooter(sender: CommandSender) {
        if (footer != null) {
            sender.sendMessage(footer)
        }
    }

    private fun repeatPadding(charCount: Int) = padding.repeat(charCount / padding.length)
}