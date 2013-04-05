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

import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.GenericRadioButton;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.gui.settings.GuiSimpleOptions;

public class ManualSelectionButton extends GenericRadioButton {
	Label label;
	GuiScreen parent;
	public ManualSelectionButton(String title, Label label, GuiScreen parent) {
		super(title);
		this.label = label;
		this.parent = parent;
	}

	@Override
	public void onButtonClick() {
		Configuration.setAutomatePerformance(false);
		if (!Configuration.isAdvancedOptions()) {
			Configuration.setAdvancedOptions(true);
			SpoutClient.getHandle().displayGuiScreen(GuiSimpleOptions.constructOptionsScreen(parent));
		}
		Configuration.write();
		label.setTextColor(new Color(0.45F, 0.45F, 0.45F, 0.45F));
	}
}
