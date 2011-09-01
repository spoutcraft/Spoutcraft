package org.spoutcraft.spoutcraftapi.entity;

import java.net.InetSocketAddress;

import org.spoutcraft.spoutcraftapi.Achievement;
import org.spoutcraft.spoutcraftapi.Material;
import org.spoutcraft.spoutcraftapi.Statistic;
import org.spoutcraft.spoutcraftapi.command.CommandSender;
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

	public void incrementStatistic(Statistic statistic, Material material);

	public void incrementStatistic(Statistic statistic, Material material, int amount);

}
