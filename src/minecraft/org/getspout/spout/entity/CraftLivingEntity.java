package org.getspout.spout.entity;

import java.util.HashSet;
import java.util.List;

import net.minecraft.src.EntityLiving;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.entity.Arrow;
import org.spoutcraft.spoutcraftapi.entity.Egg;
import org.spoutcraft.spoutcraftapi.entity.Entity;
import org.spoutcraft.spoutcraftapi.entity.LivingEntity;
import org.spoutcraft.spoutcraftapi.entity.Snowball;
import org.spoutcraft.spoutcraftapi.entity.Vehicle;
import org.spoutcraft.spoutcraftapi.util.Location;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {

	public EntityLiving getEntityLiving(){
		return (EntityLiving) handle;
	}
	
	public int getHealth() {
		return getEntityLiving().health;
	}

	public void setHealth(int health) {
		getEntityLiving().health = health;
	}

	public double getEyeHeight() {
		return getEyeHeight(false);
	}

	public double getEyeHeight(boolean ignoreSneaking) {
		//TODO: with sneaking ignored!
		return getEntityLiving().getEyeHeight();
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

	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent,
			int maxDistance) {
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
		// TODO Auto-generated method stub
		return 0;
	}

	public void setRemainingAir(int ticks) {
		// TODO Auto-generated method stub

	}

	public int getMaximumAir() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setMaximumAir(int ticks) {
		// TODO Auto-generated method stub

	}

	public void damage(int amount) {
		// TODO Auto-generated method stub

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

}
