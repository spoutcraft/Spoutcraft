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
package org.spoutcraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiParticle;
import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.gui.GenericGradient;

public abstract class GuiSpoutScreen extends GuiScreen {
	protected boolean instancesCreated = false;

	/**
	 * Init the widgets in this method.
	 * You should create new Instances of the widgets here,
	 * and also set unique properties that will not change when you resize the screen.
	 * Also attach the widgets to their screen.
	 */
	protected abstract void createInstances();

	/**
	 * Set the width, height and the position of the widgets here.
	 */
	protected abstract void layoutWidgets();

	@Override
	public void initGui() {
		if (!instancesCreated) {
			createInstances();
		}
		layoutWidgets();
		instancesCreated = true;
	}

	@Override
	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
	}

	@Override
	public void setWorldAndResolution(Minecraft var1, int var2, int var3) {
		this.guiParticles = new GuiParticle(var1);
		this.mc = var1;
		this.fontRenderer = var1.fontRenderer;
		this.width = var2;
		this.height = var3;

		if (!instancesCreated) {
			createInstances();
		}
		layoutWidgets();
		instancesCreated = true;

		bg = (GenericGradient) new GenericGradient().setHeight(this.height).setWidth(this.width);
	}
}
