package org.spoutcraft.spoutcraftapi.gui;

public interface MinecraftFont {
	
	public int getTextWidth(String text);
	
	public boolean isAllowedChar(char ch);
	
	public boolean isAllowedText(String text);
}
