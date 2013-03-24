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
package org.spoutcraft.client.gui.minimap;

import org.lwjgl.input.Mouse;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericGradient;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericSlider;
import org.spoutcraft.api.gui.Gradient;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.api.gui.Slider;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.GuiSpoutScreen;

public class GuiMoveMinimap extends GuiSpoutScreen {
	private Label title;
	private Gradient minimapDrag;
	private Button buttonDone, buttonReset;
	private Slider sliderScale;

	private final static Color dragColor = new Color(1f, 1f, 1f, 0.5f);

	private GuiScreen parent;

	private boolean dragging = false;
	private int dragStartX = 0, dragStartY = 0;

	public GuiMoveMinimap(GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	protected void createInstances() {
		title = new GenericLabel("Drag the minimap around");
		minimapDrag = new GenericGradient();
		minimapDrag.setTopColor(dragColor);
		minimapDrag.setBottomColor(dragColor);
		buttonDone = new GenericButton("Done");
		buttonReset = new GenericButton("Reset to Default");
		sliderScale = new GenericSlider();
		float scale = MinimapConfig.getInstance().getSizeAdjust();
		sliderScale.setSliderPosition(scale / 4);

		getScreen().attachWidgets("Spoutcraft", title, minimapDrag, buttonDone, buttonReset, sliderScale);
	}

	@Override
	protected void layoutWidgets() {
		title.setX(width / 2 - SpoutClient.getHandle().fontRenderer.getStringWidth(title.getText()) / 2);
		title.setY(10);

		sliderScale.setGeometry(width / 2 - 100, height - 55, 200, 20);

		buttonDone.setGeometry(width / 2 + 5, height - 25, 150, 20);
		buttonReset.setGeometry(width / 2 - 155, height - 25, 150, 20);
	}

	@Override
	public void updateScreen() {
		int x = (int) MinimapConfig.getInstance().getAdjustX();
		int y = (int) MinimapConfig.getInstance().getAdjustY();
		float scale = sliderScale.getSliderPosition() * 4;
		int width = (int) (65 * scale); int height = (int) (65 * scale);
		if (MinimapConfig.getInstance().isCoords()) {
			height += 18 * scale;
		}

		sliderScale.setText("Size adjust: " + Math.round(scale * 100f) / 100f);

		MinimapConfig.getInstance().setSizeAdjust(scale);

		minimapDrag.setGeometry(this.width + x - width, y, width, height);
		super.updateScreen();
	}

	@Override
	protected void mouseClicked(int x, int y, int button) {
		if (button == 0 && isInBoundingRect(minimapDrag, x, y)) {
			dragging = true;
			dragStartX = x;
			dragStartY = y;
		}
		super.mouseClicked(x, y, button);
	}

	@Override
	protected void mouseMovedOrUp(int x, int y, int button) {
		if (button == 0 && !Mouse.isButtonDown(button)) {
			dragging = false;
		}
		if (dragging) {
			int mX = (int) MinimapConfig.getInstance().getAdjustX();
			int mY = (int) MinimapConfig.getInstance().getAdjustY();
			mX += x - dragStartX;
			mY += y - dragStartY;
			MinimapConfig.getInstance().setAdjustX(mX);
			MinimapConfig.getInstance().setAdjustY(mY);

			dragStartX = x;
			dragStartY = y;
		}
		super.mouseMovedOrUp(x, y, button);
	}

	@Override
	protected void buttonClicked(Button btn) {
		if (btn == buttonDone) {
			mc.displayGuiScreen(parent);
		}
		if (btn == buttonReset) {
			MinimapConfig.getInstance().setAdjustX(0);
			MinimapConfig.getInstance().setAdjustY(0);
			MinimapConfig.getInstance().setSizeAdjust(1);
			sliderScale.setSliderPosition(1f / 4f);
		}
	}
}
