package org.spoutcraft.spoutcraftapi.addon;

import org.spoutcraft.spoutcraftapi.command.CommandExecutor;

public abstract interface Addon extends CommandExecutor {
	
	public abstract AddonDescriptionFile getDescription();
	
	public abstract void onEnable();
	
	public abstract void onDisable();
	
	public abstract AddonLoader getAddonLoader();
	
	public abstract boolean isEnabled();
	
	public abstract void setEnabled(boolean arg);

	public abstract boolean isNaggable();

	public abstract void setNaggable(boolean b);
	
	public enum Mode {
		SINGLE_PLAYER, 
		MULTIPLAYER, 
		BOTH;
	}
	
}
