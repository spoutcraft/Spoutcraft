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

import org.spoutcraft.spoutcraftapi.Achievement;
import org.spoutcraft.spoutcraftapi.Material;
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
import org.spoutcraft.spoutcraftapi.property.Property;
import org.spoutcraft.spoutcraftapi.util.Location;
import org.spoutcraft.spoutcraftapi.util.Vector;

import net.minecraft.src.EntityPlayer;

public class SpoutPlayer implements Player{
	private final EntityPlayer player;
	public SpoutPlayer(EntityPlayer player) {
		this.player = player;
	}
	
	public EntityPlayer getHandle() {
		return player;
	}

	public PlayerInventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	public ItemStack getItemInHand() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSleepTicks() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isSleeping() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setItemInHand(ItemStack arg0) {
		// TODO Auto-generated method stub
		
	}

	public void damage(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public void damage(int arg0, Entity arg1) {
		// TODO Auto-generated method stub
		
	}

	public double getEyeHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getEyeHeight(boolean arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Location getEyeLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getHealth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getLastDamage() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Block> getLineOfSight(HashSet<Byte> arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMaximumAir() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaximumNoDamageTicks() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNoDamageTicks() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getRemainingAir() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Block getTargetBlock(HashSet<Byte> arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public Vehicle getVehicle() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isInsideVehicle() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean leaveVehicle() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setHealth(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setLastDamage(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setMaximumAir(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setMaximumNoDamageTicks(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setNoDamageTicks(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setRemainingAir(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public Arrow shootArrow() {
		// TODO Auto-generated method stub
		return null;
	}

	public Egg throwEgg() {
		// TODO Auto-generated method stub
		return null;
	}

	public Snowball throwSnowball() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean eject() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getEntityId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getFallDistance() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getFireTicks() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Location getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMaxFireTicks() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Entity> getNearbyEntities(double arg0, double arg1, double arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public Entity getPassenger() {
		// TODO Auto-generated method stub
		return null;
	}

	public UUID getUniqueId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector getVelocity() {
		// TODO Auto-generated method stub
		return null;
	}

	public World getWorld() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDead() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public void remove() {
		// TODO Auto-generated method stub
		
	}

	public void setFallDistance(float arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setFireTicks(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public boolean setPassenger(Entity arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setVelocity(Vector arg0) {
		// TODO Auto-generated method stub
		
	}

	public boolean teleport(Location arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean teleport(Entity arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object getProperty(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Property getPropertyDelegate(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setProperty(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	public void sendMessage(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void awardAchievement(Achievement arg0) {
		// TODO Auto-generated method stub
		
	}

	public void chat(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public InetSocketAddress getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	public Location getCompassTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void incrementStatistic(Statistic arg0) {
		// TODO Auto-generated method stub
		
	}

	public void incrementStatistic(Statistic arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void incrementStatistic(Statistic arg0, Material arg1) {
		// TODO Auto-generated method stub
		
	}

	public void incrementStatistic(Statistic arg0, Material arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	public boolean isOnline() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSleepingIgnored() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSneaking() {
		// TODO Auto-generated method stub
		return false;
	}

	public void kickPlayer(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void loadData() {
		// TODO Auto-generated method stub
		
	}

	public boolean performCommand(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public void saveData() {
		// TODO Auto-generated method stub
		
	}

	public void sendRawMessage(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setCompassTarget(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setDisplayName(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setSleepingIgnored(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setSneaking(boolean arg0) {
		// TODO Auto-generated method stub
		
	}
}
