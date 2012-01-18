/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.gui;

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

	public void drawString(String text, int x, int y, int color) {
		Minecraft.theMinecraft.fontRenderer.drawString(text, x, y, color);
	}

	public void drawCenteredString(String text, int x, int y, int color) {
		Minecraft.theMinecraft.fontRenderer.drawString(text, x, y - getTextWidth(text) / 2, color);
	}
}
