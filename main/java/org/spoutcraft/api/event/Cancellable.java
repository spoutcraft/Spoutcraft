package org.spoutcraft.api.event;

public abstract interface Cancellable {
	
	public abstract boolean isCancelled();
	
	public abstract void setCancelled(boolean arg0);
	
}
