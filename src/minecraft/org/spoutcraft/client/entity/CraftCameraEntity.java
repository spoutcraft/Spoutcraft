package org.spoutcraft.client.entity;

import org.spoutcraft.spoutcraftapi.entity.CameraEntity;
import org.spoutcraft.spoutcraftapi.property.Property;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;
import org.spoutcraft.spoutcraftapi.util.Location;
import org.spoutcraft.spoutcraftapi.util.Vector;

public class CraftCameraEntity extends CraftEntity implements CameraEntity {
	public CraftCameraEntity(FixedLocation location) {
		super(location);
		handle = new EntityCamera(this);
		teleport(location);
		addProperty("location", new Property() {
			public void set(Object value) {
				teleport((Location)value);
			}
			public Object get() {
				return getLocation();
			}
		});
		addProperty("velocity", new Property() {
			public void set(Object value) {
				setVelocity((Vector)value);
			}
			public Object get() {
				return getVelocity();
			}
		});
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
