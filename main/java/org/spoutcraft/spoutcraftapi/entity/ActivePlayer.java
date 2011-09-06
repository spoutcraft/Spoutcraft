package org.spoutcraft.spoutcraftapi.entity;

import org.spoutcraft.spoutcraftapi.gui.InGameHUD;
import org.spoutcraft.spoutcraftapi.player.RenderDistance;

public interface ActivePlayer {
	
	public RenderDistance getMaximumView();
	
	public void setMaximumView(RenderDistance distance);
	
	public RenderDistance getMinimumView();
	
	public void setMinimumView(RenderDistance distance);
	
	public RenderDistance getNextRenderDistance();
	
	public RenderDistance getCurrentView();
	
	public InGameHUD getMainScreen();
	
	public void showAchievement(String title, String message, int id);
	
	public void showAchievement(String title, String message, int id, int data, int time);
	
	public String getEntityTitle(int id);
	
	public void setEntityTitle(int id, String title);
	
	public void resetEntityTitle(int id);

}
