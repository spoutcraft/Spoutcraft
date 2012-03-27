package org.spoutcraft.client.entity;

import org.spoutcraft.client.SpoutClient;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayerSP;

public class EntityCamera extends EntityPlayerSP {

	public EntityCamera(CraftCameraEntity spoutEntity) {
		super(SpoutClient.getHandle(), SpoutClient.getHandle().theWorld, SpoutClient.getHandle().session, SpoutClient.getHandle().thePlayer.dimension);
		yOffset = 1.62F;
		this.spoutEntity = spoutEntity;
	}
	
	@Override
	public boolean canBePushed() {
		return false;
	}
	
	@Override
	public void onEntityUpdate() { }
	
	@Override
	public void onDeath(DamageSource source) { }
	
	@Override
	public boolean isEntityAlive() {
		return true;
	}
	
	public void setRotation(float yaw, float pitch) {
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;
		
		prevRotationYaw = rotationYaw;
		prevRotationPitch = rotationPitch;
		rotationYaw = yaw;
		rotationPitch = pitch;
	}

}