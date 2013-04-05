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

public class AdvancedOpenGLButton extends AutomatedButton {
	public AdvancedOpenGLButton() {
		setTooltip("Detect and render only visible geometry\nOFF - all geometry is rendered (slower)\nFast - only visible geometry is rendered (fastest)\nFancy - conservative, avoids visual artifacts (faster)\nThe option is available only if it is supported by the\ngraphic card.");
	}

	@Override
	public String getText() {
		switch (Configuration.getAdvancedOpenGL()) {
			case 0: return "Advanced OpenGL: OFF";
			case 1: return "Advanced OpenGL: Fast";
			case 2: return "Advanced OpenGL: Fancy";
		}
		return "Unknown State: " + Configuration.getAdvancedOpenGL();
	}

	@Override
	public void onButtonClick() {
		Configuration.setAdvancedOpenGL(Configuration.getAdvancedOpenGL() + 1);
		if (Configuration.getAdvancedOpenGL() > 2) {
			Configuration.setAdvancedOpenGL(0);
		}
		Configuration.write();
		Minecraft.theMinecraft.gameSettings.advancedOpengl = Configuration.getAdvancedOpenGL() != 0;
		Minecraft.theMinecraft.renderGlobal.setAllRenderesVisible();
	}
}
