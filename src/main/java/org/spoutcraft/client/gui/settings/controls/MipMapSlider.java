/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
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
package org.spoutcraft.client.gui.settings.controls;

import org.spoutcraft.api.gui.GenericSlider;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.config.MipMapUtils;

public class MipMapSlider extends GenericSlider {
	public MipMapSlider() {
		super("Terrain Mipmaps");
		this.setSliderPosition(Configuration.getMipmapsPercent());
		setTooltip("Terrain Mipmaps\nON - reduces the pixelation in far off terrain. However, not all \ngraphic cards support it, and some texture packs handle it poorly.\nOFF - Normal Minecraft terrain.");
	}

	@Override
	public String getText() {
		if (this.getSliderPosition() == 0F) {
			return "Terrain Mipmaps: OFF";
		}
		return "Terrain Mipmaps: " + (int)(this.getSliderPosition() * 100) + "%";
	}

	@Override
	public void onSliderDrag(float old, float newPos) {
		Configuration.setMipmapsPercent(newPos);
		Configuration.write();
		MipMapUtils.update();
	}
}
