/*
 * This file is part of Spoutcraft API (http://wiki.getspout.org/).
 * 
 * Spoutcraft API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.entity;

import org.spoutcraft.spoutcraftapi.gui.InGameHUD;
import org.spoutcraft.spoutcraftapi.gui.Screen;
import org.spoutcraft.spoutcraftapi.player.RenderDistance;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;

public interface ActivePlayer extends Player {

	public RenderDistance getMaximumView();

	public void setMaximumView(RenderDistance distance);

	public RenderDistance getMinimumView();

	public void setMinimumView(RenderDistance distance);

	public RenderDistance getNextRenderDistance();

	public RenderDistance getCurrentView();

	public InGameHUD getMainScreen();

	public Screen getCurrentScreen();

	public void showAchievement(String title, String message, int id);

	public void showAchievement(String title, String message, int id, int data, int time);

	public String getEntityTitle(int id);

	public void setEntityTitle(int id, String title);

	public void resetEntityTitle(int id);

	public FixedLocation getLastClickedLocation();

	void setCurrentScreen(Screen screen);
	
	/**
	 * Sets if the player can or can't toggle flying through normal means.
	 * 
	 * @param fly to set
	 */
	public void setCanFly(boolean fly);
	
	/**
	 * Checks if the player can or can't toggle flying through normal means.
	 * 
	 * @return ability to fly
	 */
	public boolean canFly();
	
	/**
	 * Sets if the player is currently flying or not.
	 * 
	 * @param flying to set
	 */
	public void setFlying(boolean flying);
	
	/**
	 * Gets if the player is currently flying or not.
	 * 
	 * @return currently flying
	 */
	public boolean isFlying();
	
	/**
	 * Sets if the player is invulnerable to damage or not.
	 * 
	 * @param invulnerable to set
	 */
	public void setInvulnerable(boolean invulnerable);
	
	/**
	 * Gets if the player is invulnerable to damage or not.
	 * 
	 * @return invulnerable
	 */
	public boolean isInvulnerable();
	
	/**
	 * Sets if the player can instantly break blocks or not.
	 * 
	 * @param instantBreak value to set
	 */
	public void setInstantBreak(boolean instantBreak);
	
	/**
	 * Checks if the player can instantly break blocks or not.
	 * 
	 * @return instantBreak
	 */
	public boolean canInstantBreak();
}
