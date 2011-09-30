/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spout.player;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.getspout.spout.entity.CraftEntity;
import org.getspout.spout.entity.CraftLivingEntity;
import org.spoutcraft.spoutcraftapi.Achievement;
import org.spoutcraft.spoutcraftapi.Statistic;
import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.entity.Arrow;
import org.spoutcraft.spoutcraftapi.entity.Egg;
import org.spoutcraft.spoutcraftapi.entity.Entity;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.entity.Snowball;
import org.spoutcraft.spoutcraftapi.entity.Vehicle;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;
import org.spoutcraft.spoutcraftapi.inventory.PlayerInventory;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.property.Property;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;
import org.spoutcraft.spoutcraftapi.util.Location;
import org.spoutcraft.spoutcraftapi.util.MutableLocation;
import org.spoutcraft.spoutcraftapi.util.Vector;

import net.minecraft.src.EntityPlayer;

public class SpoutPlayer extends CraftLivingEntity implements Player{
	
	public SpoutPlayer() {
		
	}
	
	public SpoutPlayer(net.minecraft.src.EntityPlayer handle) {
		this.handle = handle;
	}
	
	public EntityPlayer getMCPlayer(){
		return (EntityPlayer)handle;
	}
	
	public void setPlayer(EntityPlayer player) {
		this.handle = player;
	}
	public String getName() {
		return getMCPlayer().username;
	}
	public PlayerInventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}
	public ItemStack getItemInHand() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setItemInHand(ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	public boolean isSleeping() {
		boolean sleep = getMCPlayer().isPlayerSleeping();
		return sleep;
	}
	public int getSleepTicks() {
		// TODO Auto-generated method stub
		return 0;
	}
	public int getHealth() {
		return getMCPlayer().health;
	}
	public void setHealth(int health) {
		getMCPlayer().health = health;
	}
	public double getEyeHeight() {
		return getMCPlayer().height;
	}

	public EntityPlayer getHandle() {
		return getMCPlayer();
	}

	public void sendMessage(String paramString) {
		// TODO Auto-generated method stub
		
	}

	public boolean isOnline() {
		//This is obvious, isn't it?
		return true;
	}

	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDisplayName(String name) {
		// TODO Auto-generated method stub
		
	}

	public void setCompassTarget(Location loc) {
		// TODO Auto-generated method stub
		
	}

	public Location getCompassTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public InetSocketAddress getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendRawMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	public void kickPlayer(String message) {
		// TODO Auto-generated method stub
		
	}

	public void chat(String msg) {
		// TODO Auto-generated method stub
		
	}

	public boolean performCommand(String command) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSneaking() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setSneaking(boolean sneak) {
		// TODO Auto-generated method stub
		
	}

	public void saveData() {
		// TODO Auto-generated method stub
		
	}

	public void loadData() {
		// TODO Auto-generated method stub
		
	}

	public void setSleepingIgnored(boolean isSleeping) {
		// TODO Auto-generated method stub
		
	}

	public boolean isSleepingIgnored() {
		// TODO Auto-generated method stub
		return false;
	}

	public void awardAchievement(Achievement achievement) {
		// TODO Auto-generated method stub
		
	}

	public void incrementStatistic(Statistic statistic) {
		// TODO Auto-generated method stub
		
	}

	public void incrementStatistic(Statistic statistic, int amount) {
		// TODO Auto-generated method stub
		
	}

	public void incrementStatistic(Statistic statistic, MaterialData material) {
		// TODO Auto-generated method stub
		
	}

	public void incrementStatistic(Statistic statistic, MaterialData material,
			int amount) {
		// TODO Auto-generated method stub
		
	}
}
