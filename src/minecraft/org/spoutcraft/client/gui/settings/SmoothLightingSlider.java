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
package org.spoutcraft.client.gui.settings;

import net.minecraft.client.Minecraft;

import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.SliderDragEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericSlider;

public class SmoothLightingSlider extends GenericSlider{
	
	public SmoothLightingSlider() {
		super("Smooth Lighting");
		setSliderPosition(ConfigReader.smoothLighting);
		setTooltip("Smooth lighting\nOFF - no smooth lighting (faster)\n1% - light smooth lighting (slower)\n100% - dark smooth lighting (slower)");
	}
	
	@Override
	public void onSliderDrag(SliderDragEvent event) {
		ConfigReader.smoothLighting = event.getNewPosition();
		Minecraft.theMinecraft.gameSettings.ambientOcclusion = ConfigReader.smoothLighting > 0F;
		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}
		ConfigReader.write();
	}
	
	public String getText() {
		return "Smooth Lighting: " + (int)(this.getSliderPosition() * 100) + "%";
	}
}
