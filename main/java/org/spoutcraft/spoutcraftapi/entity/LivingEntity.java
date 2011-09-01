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
