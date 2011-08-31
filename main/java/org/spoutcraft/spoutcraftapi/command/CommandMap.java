/*
 * Based off org.bukkit.command.CommandMap.java
 */
package org.spoutcraft.spoutcraftapi.command;

import java.util.List;


public abstract interface CommandMap {
	
	public abstract void registerAll(String paramString, List<Command> paramList);

	public abstract boolean register(String paramString1, String paramString2, Command paramCommand);

	public abstract boolean register(String paramString, Command paramCommand);

	public abstract boolean dispatch(CommandSender paramCommandSender, String paramString) throws CommandException;

	public abstract void clearCommands();

	public abstract Command getCommand(String paramString);
	
}