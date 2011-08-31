/*
 * Based off org.bukkit.command.CommandExecutor.java
 */
package org.spoutcraft.spoutcraftapi.command;

public abstract interface CommandExecutor {
	public abstract boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString);
}

