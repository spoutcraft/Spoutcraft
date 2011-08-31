/*
 * Based off org.bukkit.command.Command.java
 */

package org.spoutcraft.spoutcraftapi.command;

import java.util.ArrayList;
import java.util.List;


public abstract class Command {
	private final String name;
	private String nextLabel;
	private String label;
	private List<String> aliases;
	private List<String> activeAliases;
	private CommandMap commandMap = null;
	protected String description = "";
	protected String usageMessage;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Command(String name) {
		this(name, "", "/" + name, new ArrayList());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Command(String name, String description, String usageMessage, List<String> aliases) {
		this.name = name;
		this.nextLabel = name;
		this.label = name;
		this.description = description;
		this.usageMessage = usageMessage;
		this.aliases = aliases;
		this.activeAliases = new ArrayList(aliases);
	}

	public abstract boolean execute(CommandSender paramCommandSender, String paramString, String[] paramArrayOfString);

	public String getName() {
		return this.name;
	}

	public String getLabel() {
		return this.label;
	}

	public boolean setLabel(String name) {
		this.nextLabel = name;
		if (!isRegistered()) {
			this.label = name;
			return true;
		}
		return false;
	}

	public boolean register(CommandMap commandMap) {
		if (allowChangesFrom(commandMap)) {
			this.commandMap = commandMap;
			return true;
		}

		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean unregister(CommandMap commandMap) {
		if (allowChangesFrom(commandMap)) {
			this.commandMap = null;
			this.activeAliases = new ArrayList(this.aliases);
			this.label = this.nextLabel;
			return true;
		}

		return false;
	}

	private boolean allowChangesFrom(CommandMap commandMap) {
		return (null == this.commandMap) || (this.commandMap == commandMap);
	}

	public boolean isRegistered() {
		return null != this.commandMap;
	}

	public List<String> getAliases() {
		return this.activeAliases;
	}

	public String getDescription() {
		return this.description;
	}

	public String getUsage() {
		return this.usageMessage;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Command setAliases(List<String> aliases) {
		this.aliases = aliases;
		if (!isRegistered()) {
			this.activeAliases = new ArrayList(aliases);
		}
		return this;
	}

	public Command setDescription(String description) {
		this.description = description;
		return this;
	}

	public Command setUsage(String usage) {
		this.usageMessage = usage;
		return this;
	}
}
