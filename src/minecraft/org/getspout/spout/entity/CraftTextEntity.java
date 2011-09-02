package org.getspout.spout.entity;

import net.minecraft.src.Entity;

import org.spoutcraft.spoutcraftapi.entity.TextEntity;

public class CraftTextEntity extends CraftEntity implements TextEntity {

	public CraftTextEntity(EntityText handle) {
		super(handle);
	}
	
	public EntityText getHandle(){
		return (EntityText)handle;
	}

	@Override
	public String getText() {
		return getHandle().getText();
	}

	@Override
	public void setText(String text) {
		getHandle().setText(text);
	}

	@Override
	public boolean isRotatingWithPlayer() {
		return getHandle().isRotateWithPlayer();
	}

	@Override
	public void setRotatingWithPlayer(boolean flag) {
		getHandle().setRotateWithPlayer(flag);
	}

	@Override
	public float getScale() {
		return getHandle().getScale();
	}

	@Override
	public void setScale(float s) {
		getHandle().setScale(s);
	}
	
}
