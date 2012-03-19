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
package org.spoutcraft.client.gui.settings;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;

import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;

public class ClearWaterToggleButton extends GenericCheckBox {
	public ClearWaterToggleButton() {
		super("Clear Water");
		setChecked(ConfigReader.clearWater);
		setEnabled(SpoutClient.getInstance().isClearWaterCheat());
		setTooltip("Clear Water\nOFF - (default) standard water view\nON - can see deeper through water, no longer obscures vision\nClear water is very resource demanding!");
	}

	@Override
	public String getTooltip() {
		if (!isEnabled()) {
			return "This option is not allowed by your server, it is considered cheating.";
		}
		return super.getTooltip();
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.clearWater = !ConfigReader.clearWater;
		ConfigReader.write();

		Block.waterStill.setLightOpacity(isChecked() ? 1 : 3);
		Block.waterMoving.setLightOpacity(isChecked() ? 1 : 3);
		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}
	}
}
