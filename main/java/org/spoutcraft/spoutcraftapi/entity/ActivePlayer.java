/*
 * This file is part of SpoutcraftAPI (http://wiki.getspout.org/).
 * 
 * SpoutcraftAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutcraftAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.entity;

import java.net.InetSocketAddress;

import org.spoutcraft.spoutcraftapi.Achievement;
import org.spoutcraft.spoutcraftapi.Statistic;
import org.spoutcraft.spoutcraftapi.command.CommandSender;
import org.spoutcraft.spoutcraftapi.gui.InGameHUD;
import org.spoutcraft.spoutcraftapi.gui.Screen;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.player.RenderDistance;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;
import org.spoutcraft.spoutcraftapi.util.Location;

public interface ActivePlayer extends Player, CommandSender  {

	public RenderDistance getMaximumView();

	public void setMaximumView(RenderDistance distance);

	public RenderDistance getMinimumView();

	public void setMinimumView(RenderDistance distance);

	public RenderDistance getNextRenderDistance();

	public RenderDistance getCurrentView();
	
	public void setCurrentView(RenderDistance distance);

	public InGameHUD getMainScreen();

	public Screen getCurrentScreen();

	public void showAchievement(String title, String message, int id);

	public void showAchievement(String title, String message, int id, int data, int time);

	public String getEntityTitle(int id);

	public void setEntityTitle(int id, String title);

	public void resetEntityTitle(int id);

	public FixedLocation getLastClickedLocation();

	public void setCurrentScreen(Screen screen);
	
	public void setCompassTarget(Location loc);

	public Location getCompassTarget();

	public InetSocketAddress getAddress();

	public void sendRawMessage(String message);

	public void disconnect(String message);

	public void chat(String msg);

	public boolean performCommand(String command);
	
	public void saveData();

	public void loadData();

	public void awardAchievement(Achievement achievement);

	public void incrementStatistic(Statistic statistic);

	public void incrementStatistic(Statistic statistic, int amount);

	public void incrementStatistic(Statistic statistic, MaterialData material);

	public void incrementStatistic(Statistic statistic, MaterialData material, int amount);
}
