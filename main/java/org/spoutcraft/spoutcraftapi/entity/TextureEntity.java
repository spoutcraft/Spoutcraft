package org.spoutcraft.spoutcraftapi.entity;

public interface TextureEntity extends Entity {
	
	public String getUrl();
	
	public void setUrl(String url);
	
	public boolean isRotatingWithPlayer();
	
	public void setRotatingWithPlayer(boolean flag);
	
	public float getHeight();
	
	public void setHeight(float height);
	
	public float getWidth();
	
	public void setWidth(float width);
	
}