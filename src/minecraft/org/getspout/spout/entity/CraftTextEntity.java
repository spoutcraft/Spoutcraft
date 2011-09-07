package org.getspout.spout.entity;

import org.spoutcraft.spoutcraftapi.entity.TextEntity;

public class CraftTextEntity extends CraftEntity implements TextEntity {

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
