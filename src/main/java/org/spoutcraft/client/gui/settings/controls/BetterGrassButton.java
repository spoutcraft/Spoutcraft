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

import org.spoutcraft.client.config.Configuration;

public class BetterGrassButton extends AutomatedButton {
	public BetterGrassButton() {
		setTooltip("Better Grass / Snow\nOFF - default side grass texture, fastest\nFast - full side grass texture, slower\nFancy - dynamic side grass texture, slowest");
	}

	@Override
	public String getText() {
		switch(Configuration.getBetterGrass()) {
			case 0: return "Better Grass / Snow: OFF";
			case 1: return "Better Grass / Snow: Fast";
			case 2: return "Better Grass / Snow: Fancy";
		}
		return "Unknown State: " + Configuration.getBetterGrass();
	}

	@Override
	public void onButtonClick() {
		Configuration.setBetterGrass(Configuration.getBetterGrass() + 1);
		if (Configuration.getBetterGrass() > 2) {
			Configuration.setBetterGrass(0);
		}
		Configuration.write();

		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}
	}
}
