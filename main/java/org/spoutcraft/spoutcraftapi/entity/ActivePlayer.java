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
