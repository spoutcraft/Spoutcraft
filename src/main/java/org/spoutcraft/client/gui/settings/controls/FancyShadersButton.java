/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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

import com.pclewis.mcpatcher.mod.Shaders;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

import org.spoutcraft.api.event.screen.ButtonClickEvent;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.client.config.Configuration;

public class FancyShadersButton extends GenericButton {
	UUID fancyGraphics;
	public FancyShadersButton(UUID fancyGraphics) {
		super("Fancy Shaders");
		this.fancyGraphics = fancyGraphics;
		setEnabled(Configuration.isShadersSupported());
		setTooltip("Shaders\nShaders are post-processing effects for the graphics\nThey can have a serious impact on performance.");
	}

	@Override
	public String getTooltip() {
		if (!isEnabled()) {
			return "Your graphics card does not support shaders.\nShaders require OpenGL 2.0 or greater support.";
		}
		return super.getTooltip();
	}

	@Override
	public String getText() {
		switch(Configuration.getShaderType()) {
			case 0: return "Shaders: OFF";
			case 1: return "Shaders: Low";
			case 2: return "Shaders: Medium";
			case 3: return "Shaders: High";
			case 4: return "Shaders: Custom";
		}
		return "Shaders: Unknown";
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			Configuration.setShaderType(0);
		} else {
			Configuration.setShaderType(Configuration.getShaderType() + 1);
		}
		if (Configuration.getShaderType() > 3) {
			File shadersDir = new File(Minecraft.getMinecraftDir(), "shaders");
			if (!hasShaders(shadersDir) || shadersDir.listFiles().length != 18) { //18 shader files
				Configuration.setShaderType(0);
			}
		}
		if (Configuration.getShaderType() > 4) {
			Configuration.setShaderType(0);
		}
		Configuration.write();
		Shaders.setMode(Configuration.getShaderType());
	}

	private boolean hasShaders(File dir) {
		for (File f : dir.listFiles()) {
			String ext = FilenameUtils.getExtension(f.getName());
			if (ext.equalsIgnoreCase("fsh") || ext.equalsIgnoreCase("vsh")) {
				return true;
			}
		}
		return false;
	}
}
