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

package org.hedbor.evan.crunchcommands

import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.annotation.permission.ChildPermission
import org.bukkit.plugin.java.annotation.permission.Permission
import org.bukkit.plugin.java.annotation.permission.Permissions
import org.bukkit.plugin.java.annotation.plugin.ApiVersion
import org.bukkit.plugin.java.annotation.plugin.Description
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.author.Author
import org.hedbor.evan.crunchcommands.CrunchCommands.Companion.PLUGIN_ID
import org.hedbor.evan.crunchcommands.command.CommandCtp
import org.hedbor.evan.crunchcommands.command.CommandDirt
import org.hedbor.evan.crunchcommands.util.commands


/**
 * The main plugin class.
 */
@Plugin(name = "CrunchCommands", version = "@VERSION@")
@Author("Evan Hedbor")
@Description("A Spigot version of CrunchCommands!")
@ApiVersion(ApiVersion.Target.v1_13)
@Permissions(value = [
    Permission(name = "$PLUGIN_ID.*", desc = "Wildcard permission", defaultValue = PermissionDefault.OP, children = [
        ChildPermission(name = "$PLUGIN_ID.dirt"),
        ChildPermission(name = "$PLUGIN_ID.ctp")
    ])
])
class CrunchCommands : JavaPlugin() {
    companion object {
        internal const val PLUGIN_ID = "crunchcommands"
        internal const val PERM_MSG = "You do not have permission to use this command."
        internal const val CMD_USAGE = "Usage: /<command>"
    }

    override fun onEnable() {
        commands {
            command("dirt", CommandDirt)
            command("ctp", CommandCtp)
        }
    }
}