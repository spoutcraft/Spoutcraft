/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Gui;

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
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 9;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setMaxNumShields(input.readInt());
		setAlwaysVisible(input.readBoolean());
		setIconOffset(input.readInt());
	}
	
	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(getMaxNumShields());
		output.writeBoolean(isAlwaysVisible());
		output.writeInt(getIconOffset());
	}
	
	public WidgetType getType() {
		return WidgetType.ArmorBar;
	}
	
	@Override
	public double getScreenX() {
		double mid = getScreen() != null ? getScreen().getWidth() / 2 : 427 / 2D;
		double diff = super.getScreenX() - mid - 163;
		return getScreen() != null ? getScreen().getWidth() / 2D - diff : this.getX();
	}
	
	@Override
	public double getScreenY() {
		int diff = (int) (240 - this.getY());
		return getScreen() != null ? getScreen().getHeight() - diff : this.getY();
	}
	
	public UUID getId() {
		return new UUID(0, 0);
	}
	
	/**
	 * Gets the maximum number of shields displayed on the HUD.
	 * 
	 * Armor is scaled to fit the number of shields appropriately.
	 * @return shields displayed
	 */
	public int getMaxNumShields() {
		return icons;
	}
	
	/**
	 * Sets the maximum number of shields displayed on the HUD.
	 * 
	 * Armor is scaled to fit the number of shields appropriately.
	 * @param shields to display
	 * @return this
	 */
	public ArmorBar setMaxNumShields(int icons) {
		this.icons = icons;
		return this;
	}
	
	/**
	 * True if the armor bar will appear even when the player has no armor equipped.
	 * @return always visible
	 */
	public boolean isAlwaysVisible() {
		return alwaysVisible;
	}
	
	/**
	 * Forces the armor bar to appear, even when the player has no armor equipped.
	 * @param visible
	 * @return this
	 */
	public ArmorBar setAlwaysVisible(boolean visible) {
		alwaysVisible = visible;
		return this;
	}
	
	/**
	 * Gets the number of pixels each shield is offset when drawing the next shield.
	 * @return pixel offset
	 */
	public int getIconOffset() {
		return iconOffset;
	}
	
	/**
	 * Sets the number of pixels each shield is offset when drawing the next shield.
	 * @param offset when drawing shields
	 * @return this
	 */
	public ArmorBar setIconOffset(int offset) {
		iconOffset = offset;
		return this;
	}
	
	//My failed attempt to get custom textures for the shields - afforess 21/8/11
	/*
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
	*/

	@Override
	public void render() {
		float armorPercent = Minecraft.theMinecraft.thePlayer.getPlayerArmorValue() / 0.2f;
		if (isVisible() && getMaxNumShields() > 0) {
			int y = (int)getScreenY();
			float armorPercentPerIcon = 100f / getMaxNumShields();
			for (int icon = 0; icon < getMaxNumShields(); icon++) {
				if (armorPercent > 0 || isAlwaysVisible()) {
					int x = (int)getScreenX() - icon * getIconOffset();
					boolean full = (icon + 1) * armorPercentPerIcon <= armorPercent;
					boolean half = (icon + 1) * armorPercentPerIcon < armorPercent + armorPercentPerIcon;
					if (full) { //white armor (filled in)
						Gui.drawStaticTexturedModalRect(x, y, 34, 9, 9, 9, 0f);
					}
					else if (half) { //half filled in
						Gui.drawStaticTexturedModalRect(x, y, 25, 9, 9, 9, 0f);
					}
					else {
						Gui.drawStaticTexturedModalRect(x, y, 16, 9, 9, 9, 0f);
					}
				}
			}
		}
	}
	
	@Override
	public int getVersion() {
		return super.getVersion() + 1;
	}

}
