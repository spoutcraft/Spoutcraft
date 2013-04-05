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

import java.util.List;

import net.minecraft.client.Minecraft;

import org.spoutcraft.api.gui.CheckBox;
import org.spoutcraft.client.config.Configuration;

public class FancyGraphicsButton extends AutomatedButton {
	public boolean custom = false;
	private List<CheckBox> linkedButtons = null;
	public FancyGraphicsButton() {
		setTooltip("Visual quality\nFast  - lower quality, faster\nFancy - higher quality, slower\nChanges the appearance of clouds, leaves, water,\nshadows and grass sides.");
	}

	@Override
	public String getText() {
		return "Graphics: " + (custom ? "Custom" : (Configuration.isFancyGraphics() ? "Fancy" :"Fast"));
	}

	@Override
	public void onButtonClick() {
		Configuration.setFancyGraphics(!Configuration.isFancyGraphics());
		for (CheckBox check : linkedButtons) {
			if (check.isChecked() != Configuration.isFancyGraphics()) {
				check.setChecked(Configuration.isFancyGraphics());
				check.onButtonClick();
			}
		}
		Minecraft.theMinecraft.gameSettings.fancyGraphics = Configuration.isFancyGraphics();
		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}
		custom = false;
	}

	public void setLinkedButtons(List<CheckBox> linked) {
		linkedButtons = linked;
		for (CheckBox check : linkedButtons) {
			if (check.isChecked() != Configuration.isFancyGraphics()) {
				custom = true;
				break;
			}
		}
	}
}
