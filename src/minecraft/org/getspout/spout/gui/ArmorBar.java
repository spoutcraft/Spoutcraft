package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

import org.getspout.spout.io.CustomTextureManager;

public class ArmorBar extends GenericWidget {
	private int icons = 10;
	private boolean alwaysVisible = false;
	private int iconOffset = 8;
	String fullArmorUrl = null;
	org.newdawn.slick.opengl.Texture fullArmortexture = null;
	String halfArmorUrl = null;
	org.newdawn.slick.opengl.Texture halfArmortexture = null;
	String emptyArmorUrl = null;
	org.newdawn.slick.opengl.Texture emptyArmortexture = null;

	public ArmorBar() {
		super();
		setX(427 / 2 + 82); //295
		setY(208);
	}
	
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setX(427 / 2 + 82);
		setY(208);
	}
	
	public WidgetType getType() {
		return WidgetType.ArmorBar;
	}
	
	@Override
	public double getScreenX() {
		if (getX() == 295) {
			return getScreen().getWidth() / 2 + 82;
		}
		return super.getScreenX();
	}
	
	@Override
	public double getScreenY() {
		if (getY() == 208) {
			return getScreen().getHeight() - 32;
		}
		return super.getScreenY();
	}
	
	public UUID getId() {
		return new UUID(0, 0);
	}
	
	public int getMaxNumShields() {
		return icons;
	}
	
	public ArmorBar setMaxNumShields(int icons) {
		this.icons = icons;
		return this;
	}
	
	public boolean isAlwaysVisible() {
		return alwaysVisible;
	}
	
	public ArmorBar setAlwaysVisible(boolean visible) {
		alwaysVisible = visible;
		return this;
	}
	
	public int getIconOffset() {
		return iconOffset;
	}
	
	public ArmorBar setIconOffset(int offset) {
		iconOffset = offset;
		return this;
	}
	
	public String getFullArmorUrl() {
		return fullArmorUrl;
	}
	
	public ArmorBar setFullArmorUrl(String url) {
		if (getFullArmorUrl() != null) {
			fullArmortexture = null;
		}
		this.fullArmorUrl = url;
		if (getFullArmorUrl() != null) {
			CustomTextureManager.downloadTexture(url);
		}
		return this;
	}
	
	public int getFullArmorTextureId() {
		if (getFullArmorUrl() == null) {
			return -1;
		}
		String path = CustomTextureManager.getTextureFromUrl(getFullArmorUrl());
		if (path == null) {
			return -1;
		}
		if (fullArmortexture == null) {
			fullArmortexture = CustomTextureManager.getTextureFromPath(path);
			if (fullArmortexture == null) {
				return -1;
			}
		}
		return fullArmortexture.getTextureID();
	}
	
	public String getHalfArmorUrl() {
		return halfArmorUrl;
	}
	
	public ArmorBar setHalfArmorUrl(String url){
		if (getHalfArmorUrl() != null) {
			halfArmortexture = null;
		}
		this.halfArmorUrl = url;
		if (getHalfArmorUrl() != null) {
			CustomTextureManager.downloadTexture(url);
		}
		return this;
	}
	
	public int getHalfArmorTextureId() {
		if (getHalfArmorUrl() == null) {
			return -1;
		}
		String path = CustomTextureManager.getTextureFromUrl(getHalfArmorUrl());
		if (path == null) {
			return -1;
		}
		if (halfArmortexture == null) {
			halfArmortexture = CustomTextureManager.getTextureFromPath(path);
			if (halfArmortexture == null) {
				return -1;
			}
		}
		return halfArmortexture.getTextureID();
	}
	
	public String getEmptyArmorUrl() {
		return emptyArmorUrl;
	}
	
	public ArmorBar setEmptyArmorUrl(String url) {
		if (getEmptyArmorUrl() != null) {
			emptyArmortexture = null;
		}
		this.emptyArmorUrl = url;
		if (getEmptyArmorUrl() != null) {
			CustomTextureManager.downloadTexture(url);
		}
		return this;
	}
	
	public int getEmptyArmorTextureId() {
		if (getEmptyArmorUrl() == null) {
			return -1;
		}
		String path = CustomTextureManager.getTextureFromUrl(getEmptyArmorUrl());
		if (path == null) {
			return -1;
		}
		if (emptyArmortexture == null) {
			emptyArmortexture = CustomTextureManager.getTextureFromPath(path);
			if (emptyArmortexture == null) {
				return -1;
			}
		}
		return emptyArmortexture.getTextureID();
	}

	@Override
	public void render() {
		
	}

}
