package org.getspout.spout.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ChatAllowedCharacters;

import org.spoutcraft.spoutcraftapi.gui.MinecraftFont;

public class MinecraftFontWrapper implements MinecraftFont{

	public int getTextWidth(String text) {
		return Minecraft.theMinecraft.fontRenderer.getStringWidth(text);
	}

	public boolean isAllowedChar(char ch) {
		return ChatAllowedCharacters.allowedCharacters.indexOf(ch) > -1;
	}

	public boolean isAllowedText(String text) {
		for (int i = 0; i < text.length(); i++) {
			if (!isAllowedChar(text.charAt(i))) {
				return false;
			}
		}
		return true;
	}

}
