package org.spoutcraft.spoutcraftapi.gui;

public interface KeyManager {
	
	public boolean isKeyDown(Keyboard ch);
	
	public boolean isRepeatingEvents();
	
	public void setRepeatingEvents(boolean repeat);

}
