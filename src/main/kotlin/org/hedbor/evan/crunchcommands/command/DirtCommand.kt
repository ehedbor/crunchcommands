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

import net.md_5.bungee.api.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.hedbor.evan.crunchcommands.CrunchCommands
import org.hedbor.evan.crunchcommands.util.itemMeta
import org.hedbor.evan.crunchcommands.util.itemStack
import kotlin.random.Random


/**
 * Gives the user some emergency dirt.
 */
class DirtCommand(plugin: CrunchCommands) : CrunchCommand(plugin, isPlayersOnly = true) {
    override fun execute(sender: CommandSender, args: Array<String>): CommandResult {
        val result = super.execute(sender, args)
        if (result is Failure) {
            return result
        }
        sender as Player

        if (args.isNotEmpty()) {
            return Failure.IncorrectUsage()
        }

        // 5% chance to send the player into the sky
        val sendToHeaven = Random.nextInt(0, 100) >= 95
        return if (sendToHeaven) {
            val oldLocation = sender.location
            val blockLocation = Location(oldLocation.world, oldLocation.x, 255.0, oldLocation.z)

            sender.world.getBlockAt(blockLocation).type = Material.DIRT
            sender.teleport(blockLocation.add(0.0, 1.0, 0.0))

            Success.SentToHeaven()
        } else {
            val dirt = itemStack(Material.DIRT) {
                amount = 3
                addUnsafeEnchantment(Enchantment.DURABILITY, 1)
                itemMeta {
                    displayName = "${ChatColor.RED}EMERGENCY DIRT"
                    lore = arrayListOf("${ChatColor.DARK_RED}For rectal use only.")
                }
            }

            sender.inventory.addItem(dirt)
            Success.DirtReceived()
        }
    }
}