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

import java.util.HashSet;
import java.util.List;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.util.Location;

public interface LivingEntity extends Entity {
	public int getHealth();

	public void setHealth(int health);

	public double getEyeHeight();

	public double getEyeHeight(boolean ignoreSneaking);

	public Location getEyeLocation();

	public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance);

	public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance);

	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance);

	public Egg throwEgg();

	public Snowball throwSnowball();

	public Arrow shootArrow();

	public boolean isInsideVehicle();

	public boolean leaveVehicle();

	public Vehicle getVehicle();

	public int getRemainingAir();

	public void setRemainingAir(int ticks);

	public int getMaximumAir();

	public void setMaximumAir(int ticks);

	public void damage(int amount);

	public void damage(int amount, Entity source);

	public int getMaximumNoDamageTicks();

	public void setMaximumNoDamageTicks(int ticks);

	public int getLastDamage();

	public void setLastDamage(int damage);

	public int getNoDamageTicks();

	public void setNoDamageTicks(int ticks);
}
