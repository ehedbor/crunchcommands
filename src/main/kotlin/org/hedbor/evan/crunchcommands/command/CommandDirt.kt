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
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.annotation.permission.Permission
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.CMD_USAGE
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PERM_MSG
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PLUGIN_ID
import org.hedbor.evan.crunchcommands.util.itemMeta
import org.hedbor.evan.crunchcommands.util.itemStack
import kotlin.random.Random
import org.bukkit.plugin.java.annotation.command.Command as CommandYml


/**
 * Gives the user some emergency dirt.
 */
@CommandYml(name = "dirt", desc = "Dispenses emergency dirt.", permission = "$PLUGIN_ID.dirt", permissionMessage = PERM_MSG, usage = CMD_USAGE)
@Permission(name = "$PLUGIN_ID.dirt", desc = "Allows dirt command", defaultValue = PermissionDefault.TRUE)
object CommandDirt : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isNotEmpty()) {
            return false
        }

        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}Only players can use this command.")
            return true
        }

        // 5% chance to send the player into the sky
        val sendToHeaven = Random.nextInt(0, 100) >= 95
        if (sendToHeaven) {
            val oldLocation = sender.location
            val blockLocation = Location(oldLocation.world, oldLocation.x, 255.0, oldLocation.z)
            sender.world.getBlockAt(blockLocation).type = Material.DIRT
            sender.teleport(blockLocation.add(0.0, 1.0, 0.0))
            sender.sendMessage("${ChatColor.LIGHT_PURPLE}haha lol")
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
        }

        return true
    }
}