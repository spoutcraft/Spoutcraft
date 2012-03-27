package org.spoutcraft.client.entity;

import org.spoutcraft.spoutcraftapi.entity.CameraEntity;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;

public class CraftCameraEntity extends CraftEntity implements CameraEntity {
	
	public CraftCameraEntity(FixedLocation location) {
		super(location);
		handle = new EntityCamera(this);
		teleport(location);
	}
	
	@Override
	public boolean teleport(FixedLocation location) {
		handle.setPositionAndRotation(location.getX(), location.getY(), location.getZ(), (float)location.getYaw(), (float)location.getPitch());
		((EntityCamera)handle).setRotation((float)location.getYaw(), (float)location.getPitch());
		return true;
	}
	
	public EntityCamera getHandle() {
		return (EntityCamera) handle;
	}

}