package org.hedbor.evan.crunchcommands

import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.annotation.command.Command
import org.bukkit.plugin.java.annotation.command.Commands
import org.bukkit.plugin.java.annotation.permission.ChildPermission
import org.bukkit.plugin.java.annotation.permission.Permission
import org.bukkit.plugin.java.annotation.permission.Permissions
import org.bukkit.plugin.java.annotation.plugin.Description
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.author.Author
import org.hedbor.evan.crunchcommands.command.CommandCtp
import org.hedbor.evan.crunchcommands.command.CommandDirt
import org.hedbor.evan.crunchcommands.util.commands


// used to shorten annotation declarations
private const val PREFIX = "crunchcommands"
private const val PERM_MSG = "You do not have permission to use this command."
private const val CMD_USAGE = "Usage: /<command>"

/**
 * The main plugin class.
 */
@Plugin(name = "CrunchCommands", version = "2.1.0")
@Author("Evan Hedbor")
@Description("A Spigot version of CrunchCommands!")
@Commands(
    Command(name = "dirt", desc = "Dispenses emergency dirt.", permission = "$PREFIX.dirt", permissionMessage = PERM_MSG, usage = CMD_USAGE),
    Command(name = "ctp", desc = "Teleports to another player.", permission = "$PREFIX.ctp", permissionMessage = PERM_MSG, usage = "$CMD_USAGE <name>")
)
@Permissions(
    Permission(name = "$PREFIX.dirt", desc = "Allows dirt command", defaultValue = PermissionDefault.OP),
    Permission(name = "$PREFIX.ctp", desc = "Allows ctp command", defaultValue = PermissionDefault.OP),
    Permission(name = "$PREFIX.*", desc = "Wildcard permission", defaultValue = PermissionDefault.OP, children = arrayOf(
        ChildPermission(name = "$PREFIX.dirt"),
        ChildPermission(name = "$PREFIX.ctp")
    ))
)
class CrunchCommands : JavaPlugin() {
    override fun onEnable() {
        commands {
            command("dirt", CommandDirt)
            command("ctp", CommandCtp)
        }
    }
}