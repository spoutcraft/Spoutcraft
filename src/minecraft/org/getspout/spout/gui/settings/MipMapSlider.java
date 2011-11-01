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
package org.getspout.spout.gui.settings;

import net.minecraft.client.Minecraft;

import org.getspout.spout.config.ConfigReader;
import org.getspout.spout.config.MipMapUtils;
import org.lwjgl.opengl.GL11;
import org.spoutcraft.spoutcraftapi.event.screen.SliderDragEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericSlider;

public class MipMapSlider extends GenericSlider{
	
	public MipMapSlider() {
		super("Terrain Mipmaps");
		this.setSliderPosition(ConfigReader.mipmapsPercent);
		setTooltip("Terrain Mipmaps\nON - reduces the pixelation in far off terrain. However, not all \ngraphic cards support it, and some texture packs handle it poorly.\nOFF - Normal Minecraft terrian.");
	}
	
	@Override
	public String getText() {
		if (this.getSliderPosition() == 0F) {
			return "Terrain Mipmaps: OFF";
		}
		return "Terrain Mipmaps: " + (int)(this.getSliderPosition() * 100) + "%";
	}
	
	@Override
	public void onSliderDrag(SliderDragEvent event) {
		ConfigReader.mipmapsPercent = event.getNewPosition();
		ConfigReader.write();
		MipMapUtils.targetFade = ConfigReader.mipmapsPercent;
		GL11.glPushMatrix();
		int terrain = Minecraft.theMinecraft.renderEngine.getTexture("/terrain.png");
		if (MipMapUtils.mode == 3) {
			MipMapUtils.updateTerrain = ConfigReader.mipmapsPercent > 0F;
			GL11.glBindTexture(3553, terrain);
			if (ConfigReader.mipmapsPercent > 0F) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
			}
			else {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			}
			GL11.glPopMatrix();
			return;
		}

		if (ConfigReader.mipmapsPercent > 0F) {
			MipMapUtils.updateTerrain = true;

			GL11.glBindTexture(3553, terrain);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
		}
		GL11.glPopMatrix();
	}
	
	

}
