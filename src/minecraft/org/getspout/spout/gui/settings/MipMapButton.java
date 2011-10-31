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
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class MipMapButton extends GenericCheckBox{
	
	public MipMapButton() {
		super("Terrain Mipmaps");
		this.setChecked(ConfigReader.mipmaps);
		setTooltip("Terrain Mipmaps\nON - reduces the pixelation in far off terrain. However, not all \ngraphic cards support it, and some texture packs handle it poorly.\nOFF - Normal Minecraft terrian.");
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.mipmaps = !ConfigReader.mipmaps;
		ConfigReader.write();
		MipMapUtils.update();
	}
	
	

}
