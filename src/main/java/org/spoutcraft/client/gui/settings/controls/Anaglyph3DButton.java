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

import net.minecraft.client.Minecraft;

import org.spoutcraft.api.gui.GenericCheckBox;
import org.spoutcraft.client.config.Configuration;

public class Anaglyph3DButton extends GenericCheckBox {
	public Anaglyph3DButton() {
		super("3D Anaglyph");
		setChecked(Configuration.isAnaglyph3D());
		setTooltip("3D mode used with red-cyan 3D glasses.");
	}

	@Override
	public void onButtonClick() {
		Configuration.setAnaglyph3D(!Configuration.isAnaglyph3D());
		Minecraft.theMinecraft.gameSettings.anaglyph = Configuration.isAnaglyph3D();
		Minecraft.theMinecraft.renderEngine.refreshTextures();
	}
}
