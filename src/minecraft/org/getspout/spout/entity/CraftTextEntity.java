package org.getspout.spout.entity;

import org.getspout.spout.SpoutcraftWorld;
import org.spoutcraft.spoutcraftapi.entity.TextEntity;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;

public class CraftTextEntity extends CraftEntity implements TextEntity {

	public CraftTextEntity(FixedLocation location) {
		super(location);
		handle = new EntityText(((SpoutcraftWorld)location.getWorld()).getHandle());
		teleport(location);
	}
	
	public CraftTextEntity(EntityText handle) {
		super(handle);
	}
	
	public EntityText getHandle(){
		return (EntityText)handle;
	}

	public String getText() {
		return getHandle().getText();
	}

	public void setText(String text) {
		getHandle().setText(text);
	}

	public boolean isRotatingWithPlayer() {
		return getHandle().isRotateWithPlayer();
	}

	public void setRotatingWithPlayer(boolean flag) {
		getHandle().setRotateWithPlayer(flag);
	}

	public float getScale() {
		return getHandle().getScale();
	}

	public void setScale(float s) {
		getHandle().setScale(s);
	}
}
