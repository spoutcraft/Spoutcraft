/*
 * Based off org.bukkit.command.PluginCommand.java
 */
package org.spoutcraft.spoutcraftapi.command;

import org.spoutcraft.spoutcraftapi.addon.Addon;

public final class AddonCommand extends Command {
	private final Addon owningAddon;
	private CommandExecutor executor;

	protected AddonCommand(String name, Addon owner) {
		super(name);
		this.executor = owner;
		this.owningAddon = owner;
		this.usageMessage = "";
	}

	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		boolean success = false;

		if (!this.owningAddon.isEnabled()) {
			return false;
		}
		try {
			success = this.executor.onCommand(sender, this, commandLabel, args);
		} catch (Throwable ex) {
			throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + this.owningAddon.getDescription().getFullName(), ex);
		}

		if ((!success) && (this.usageMessage.length() > 0)) {
			for (String line : this.usageMessage.replace("<command>", commandLabel).split("\n")) {
				sender.sendMessage(line);
			}
		}

		return success;
	}

	public void setExecutor(CommandExecutor executor) {
		this.executor = executor;
	}

	public CommandExecutor getExecutor() {
		return this.executor;
	}

	public Addon getAddon() {
		return this.owningAddon;
	}
}
