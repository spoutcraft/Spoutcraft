package org.spoutcraft.spoutcraftapi.entity;

public interface TextEntity extends Entity {
	
	public String getText();
	
	public void setText(String text);
	
	public boolean isRotatingWithPlayer();
	
	public void setRotatingWithPlayer(boolean flag);
	
	public double getScale();
	
	public void setScale(double s);
	
}
