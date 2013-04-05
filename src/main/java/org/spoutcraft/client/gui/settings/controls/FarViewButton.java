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

public class FarViewButton extends AutomatedCheckBox {
	public FarViewButton() {
		super("Far View");
		setChecked(Configuration.isFarView());
		setTooltip("Far View\nOFF - (default) standard view distance\nON - 3x view distance\nFar View is very resource demanding!\n3x view distance => 9x chunks to be loaded => FPS / 9\nStandard view distances: 32, 64, 128, 256\nFar view distances: 96, 192, 384, 512");
	}

	@Override
	public void onButtonClick() {
		Configuration.setFarView(!Configuration.isFarView());
		Configuration.write();

		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
		}
	}
}
