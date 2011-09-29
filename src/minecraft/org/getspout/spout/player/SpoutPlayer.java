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
import org.spoutcraft.spoutcraftapi.util.Location;
import org.spoutcraft.spoutcraftapi.util.Vector;

import net.minecraft.src.EntityPlayer;

public class SpoutPlayer implements Player{
	protected EntityPlayer player;
	public void setPlayer(EntityPlayer player) {
		this.player = player;
	}
	public String getName() {
		return player.username;
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
		boolean sleep = player.isPlayerSleeping();
		return sleep;
	}
	public int getSleepTicks() {
		// TODO Auto-generated method stub
		return 0;
	}
	public int getHealth() {
		return player.health;
	}
	public void setHealth(int health) {
		player.health = health;
	}
	public double getEyeHeight() {
		return player.height;
	}
	public double getEyeHeight(boolean ignoreSneaking) {
		// TODO Auto-generated method stub
		return 0;
	}
	public Location getEyeLocation() {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
		// TODO Auto-generated method stub
		return null;
	}
	public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
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
	public Arrow shootArrow() {
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
	public Vehicle getVehicle() {
		// TODO Auto-generated method stub
		return null;
	}
	public int getRemainingAir() {
		return player.air;
	}
	public void setRemainingAir(int ticks) {
		player.air = ticks;
	}
	public int getMaximumAir() {
		return player.maxAir;
	}
	public void setMaximumAir(int ticks) {
		player.maxAir = ticks;
	}
	public void damage(int amount) {
		player.health -= amount;
	}
	public void damage(int amount, Entity source) {
		// TODO Auto-generated method stub
		
	}
	public int getMaximumNoDamageTicks() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void setMaximumNoDamageTicks(int ticks) {
		// TODO Auto-generated method stub
		
	}
	public int getLastDamage() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void setLastDamage(int damage) {
		// TODO Auto-generated method stub
		
	}
	public int getNoDamageTicks() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void setNoDamageTicks(int ticks) {
		// TODO Auto-generated method stub
		
	}
	public Location getLocation() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setVelocity(Vector velocity) {
		// TODO Auto-generated method stub
		
	}
	public Vector getVelocity() {
		// TODO Auto-generated method stub
		return null;
	}
	public World getWorld() {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean teleport(Location location) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean teleport(Entity destination) {
		// TODO Auto-generated method stub
		return false;
	}
	public List<Entity> getNearbyEntities(double x, double y, double z) {
		// TODO Auto-generated method stub
		return null;
	}
	public int getEntityId() {
		// TODO Auto-generated method stub
		return 0;
	}
	public int getFireTicks() {
		// TODO Auto-generated method stub
		return 0;
	}
	public int getMaxFireTicks() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void setFireTicks(int ticks) {
		// TODO Auto-generated method stub
		
	}
	public void remove() {
		player.isDead = true;
	}
	public boolean isDead() {
		return player.isDead;
	}
	public Entity getPassenger() {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean setPassenger(Entity passenger) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean eject() {
		// TODO Auto-generated method stub
		return false;
	}
	public float getFallDistance() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void setFallDistance(float distance) {
		// TODO Auto-generated method stub
		
	}
	public UUID getUniqueId() {
		return player.uniqueId;
	}
	public Object getProperty(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	public void setProperty(String name, Object value) {
		// TODO Auto-generated method stub
		
	}
	public Property getPropertyDelegate(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	public void sendMessage(String paramString) {
		// TODO Auto-generated method stub
		
	}
	public boolean isOnline() {
		// TODO Auto-generated method stub
		return false;
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
	public void incrementStatistic(Statistic statistic, MaterialData material, int amount) {
		// TODO Auto-generated method stub
		
	}
	public EntityPlayer getHandle() {
		return player;
	}

}
