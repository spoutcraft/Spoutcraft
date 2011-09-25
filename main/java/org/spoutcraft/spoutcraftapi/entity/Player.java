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

import java.net.InetSocketAddress;

import org.spoutcraft.spoutcraftapi.Achievement;
import org.spoutcraft.spoutcraftapi.Statistic;
import org.spoutcraft.spoutcraftapi.command.CommandSender;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.util.Location;

public interface Player extends HumanEntity, CommandSender {

	public boolean isOnline();

	public String getDisplayName();

	public void setDisplayName(String name);

	public void setCompassTarget(Location loc);

	public Location getCompassTarget();

	public InetSocketAddress getAddress();

	public void sendRawMessage(String message);

	public void kickPlayer(String message);

	public void chat(String msg);

	public boolean performCommand(String command);

	public boolean isSneaking();

	public void setSneaking(boolean sneak);

	public void saveData();

	public void loadData();

	public void setSleepingIgnored(boolean isSleeping);

	public boolean isSleepingIgnored();

	public void awardAchievement(Achievement achievement);

	public void incrementStatistic(Statistic statistic);

	public void incrementStatistic(Statistic statistic, int amount);

	public void incrementStatistic(Statistic statistic, MaterialData material);

	public void incrementStatistic(Statistic statistic, MaterialData material, int amount);

}
