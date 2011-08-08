package org.getspout.spout.player;

import org.getspout.spout.gui.InGameHUD;

public interface ActivePlayer {
	
	public RenderDistance getMaximumView();
	
	public void setMaximumView(RenderDistance distance);
	
	public RenderDistance getMinimumView();
	
	public void setMinimumView(RenderDistance distance);
	
	public RenderDistance getNextRenderDistance();
	
	public RenderDistance getCurrentView();
	
	public InGameHUD getMainScreen();
	
	public void showAchievement(String title, String message, int id);
	
	public String getEntityTitle(int id);
	
	public void setEntityTitle(int id, String title);
	
	public void resetEntityTitle(int id);

}
