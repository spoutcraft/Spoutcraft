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

import org.getspout.spout.entity.CraftLivingEntity;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;
import org.spoutcraft.spoutcraftapi.inventory.PlayerInventory;
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
	
	public boolean isOnline() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getDisplayName() {
		return getMCPlayer().displayName;
	}

	public void setDisplayName(String name) {
		getMCPlayer().displayName = name;
	}

	public boolean isSneaking() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setSneaking(boolean sneak) {
		// TODO Auto-generated method stub
		
	}
}
