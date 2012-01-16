/*
 * This file is part of Spoutcraft (http://spout.org).
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
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class Anaglyph3DButton extends GenericCheckBox{

	public Anaglyph3DButton() {
		super("3D Anaglyph");
		setChecked(ConfigReader.anaglyph3D);
		setTooltip("3D mode used with red-cyan 3D glasses.");
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.anaglyph3D = !ConfigReader.anaglyph3D;
		Minecraft.theMinecraft.gameSettings.anaglyph = ConfigReader.anaglyph3D;
		Minecraft.theMinecraft.renderEngine.refreshTextures();
	}
}
