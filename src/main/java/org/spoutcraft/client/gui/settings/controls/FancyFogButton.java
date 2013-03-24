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

import java.util.UUID;

import org.spoutcraft.client.config.Configuration;

public class FancyFogButton extends AutomatedCheckBox {
	UUID fancyGraphics;
	public FancyFogButton(UUID fancyGraphics) {
		super("Fancy Fog");
		this.fancyGraphics = fancyGraphics;
		setChecked(Configuration.isFancyFog());
		setTooltip("Fog type\nFast - faster fog\nFancy - slower fog, looks better\nThe fancy fog is available only if it is supported by the\ngraphic card.");
	}

	@Override
	public void onButtonClick() {
		Configuration.setFancyFog(!Configuration.isFancyFog());
		Configuration.write();
		((FancyGraphicsButton)getScreen().getWidget(fancyGraphics)).custom = true;
	}
}
