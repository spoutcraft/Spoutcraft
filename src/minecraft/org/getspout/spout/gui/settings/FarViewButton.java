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
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class FarViewButton extends GenericCheckBox{

	public FarViewButton() {
		super("Far View");
		setChecked(ConfigReader.farView);
		setTooltip("Far View\nOFF - (default) standard view distance\nON - 3x view distance\nFar View is very resource demanding!\n3x view distance => 9x chunks to be loaded => FPS / 9\nStandard view distances: 32, 64, 128, 256\nFar view distances: 96, 192, 384, 512");
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.farView = !ConfigReader.farView;
		ConfigReader.write();
		
		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}
	}
}
