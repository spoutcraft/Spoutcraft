/*
 * Based off org.bukkit.command.CommandException.java
 */
package org.spoutcraft.spoutcraftapi.command;

public class CommandException extends RuntimeException {

	private static final long serialVersionUID = 7936404856385100186L;

	public CommandException() {
	}

	public CommandException(String msg) {
		super(msg);
	}

	public CommandException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
