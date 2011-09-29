package org.getspout.spout.entity;

import net.minecraft.src.EntityArrow;

import org.spoutcraft.spoutcraftapi.entity.Arrow;
import org.spoutcraft.spoutcraftapi.entity.LivingEntity;

public class CraftArrow extends CraftEntity implements Arrow {

	public EntityArrow getArrow(){
		return (EntityArrow)handle;
	}
	
	public LivingEntity getShooter() {
		return (LivingEntity) getArrow().shootingEntity.spoutEntity;
	}

	public void setShooter(LivingEntity shooter) {
		getArrow().shootingEntity = ((CraftLivingEntity)shooter).getEntityLiving();
	}

	public boolean doesBounce() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setBounce(boolean doesBounce) {
		// TODO Auto-generated method stub

	}

}
