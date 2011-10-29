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

import java.util.UUID;

import net.minecraft.client.Minecraft;

import org.getspout.spout.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class FancyGrassButton extends GenericCheckBox{
	UUID fancyGraphics;
	public FancyGrassButton(UUID fancyGraphics) {
		super("Fancy Grass");
		this.fancyGraphics = fancyGraphics;
		setChecked(ConfigReader.fancyGrass);
		setTooltip("Fancy Grass\nFast - lower quality, faster\nFancy - higher quality, slower\nFast grass uses default side texture.\nFancy grass uses biome side texture.");
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.fancyGrass = !ConfigReader.fancyGrass;
		ConfigReader.write();
		((FancyGraphicsButton)getScreen().getWidget(fancyGraphics)).custom = true;
		
		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}
	}
}
