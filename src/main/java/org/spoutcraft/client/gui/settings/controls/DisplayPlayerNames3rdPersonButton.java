/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org/>
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

import net.minecraft.src.Minecraft;

import org.spoutcraft.client.config.Configuration;

public class DisplayPlayerNames3rdPersonButton extends AutomatedCheckBox {
	public DisplayPlayerNames3rdPersonButton() {
		super("Show Player Name in 3rd Person");
		setChecked(Configuration.isDisplayPlayerNames3rdPerson());
		setTooltip("Displays the players name when in 3rd person view.");
	}

	@Override
	public void onButtonClick() {
		Configuration.setDisplayPlayerNames3rdPerson(!Configuration.isDisplayPlayerNames3rdPerson());
		Configuration.write();		
	}
}
