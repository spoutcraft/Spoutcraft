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

import java.util.List;
import java.util.ArrayList;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.CheckBox;
import org.spoutcraft.api.gui.Control;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericGradient;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericScrollArea;
import org.spoutcraft.api.gui.Gradient;
import org.spoutcraft.api.gui.RenderPriority;
import org.spoutcraft.api.gui.WidgetAnchor;
import org.spoutcraft.client.SpoutClient;

public class GuiMinimapMenu extends GuiScreen {
	private List<Control> hideable;
	private Button doneButton = null, positionButton = null, toggleCheckBox = null, advancedMobsButton = null;
	GuiScreen parent;
	public GuiMinimapMenu(GuiScreen parent) {
		this.parent = parent;
	}

	private void setControlsEnabled(boolean enabled) {
		for (Control control: hideable) {
		    control.setEnabled(enabled);
		}
	}

	public void initGui() {
		hideable = new ArrayList<Control>();
		Control control;

		GenericScrollArea screen = new GenericScrollArea();
		screen.setHeight(height - 24 - 30).setWidth(width).setY(24).setX(0);
		getScreen().attachWidget("Spoutcraft", screen);

		GenericLabel label = new GenericLabel("Minimap Settings");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(10);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget("Spoutcraft", label);

		int left = (int)(width / 2  - 155);
		int right = (int)(width / 2 + 5);

		control = new ResetButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(height - 25);
		getScreen().attachWidget("Spoutcraft", control);

		doneButton = new GenericButton("Done");
		doneButton.setAlign(WidgetAnchor.CENTER_CENTER);
		doneButton.setX(right).setY(height - 25);
		doneButton.setHeight(20).setWidth(150);
		getScreen().attachWidget("Spoutcraft", doneButton);

		int top = 5;
		final Color grey = new Color(0.80F, 0.80F, 0.80F, 0.65F);

		label = new GenericLabel("Minimap Position");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(top);
		label.setTextColor(grey);
		screen.attachWidget("Spoutcraft", label);

		top += 11;

		Gradient linebreak = new GenericGradient();
		linebreak.setBottomColor(grey);
		linebreak.setTopColor(grey);
		linebreak.setX(width/2 - 318 / 2).setY(top).setHeight(3).setWidth(318);
		screen.attachWidget("Spoutcraft", linebreak);

		top += 6;

		positionButton = new GenericButton("Move Minimap");
		positionButton.setGeometry(width / 2 - 75, top, 150, 20);
		screen.attachWidget("Spoutcraft", positionButton);

		top += 27;

		label = new GenericLabel("Minimap Configuration");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(top);
		label.setTextColor(grey);
		screen.attachWidget("Spoutcraft", label);

		top += 11;

		linebreak = new GenericGradient();
		linebreak.setBottomColor(grey);
		linebreak.setTopColor(grey);
		linebreak.setX(width/2 - 318 / 2).setY(top).setHeight(3).setWidth(318);
		screen.attachWidget("Spoutcraft", linebreak);

		top += 6;

		toggleCheckBox = new MinimapToggleCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		toggleCheckBox.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", toggleCheckBox);

		control = new ColorToggleCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control.setEnabled(false);
		control.setTooltip("Feature broken.");

		top += 22;

		control = new CoordsToggleCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		hideable.add(control);

		control = new SquareToggleCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		hideable.add(control);

		control = new ScaleToggleCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		hideable.add(control);

		control = new DirectionsToggleCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		hideable.add(control);

		control = new MinimapModeButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control.setEnabled(false);
		control.setTooltip("Feature broken.");

		control = new ZoomModeButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		hideable.add(control);

		control = new DeathpointsCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		hideable.add(control);

		control = new BackgroundCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		hideable.add(control);

		control = new HeightMapCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		hideable.add(control);

		control = new ScanRadiusSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		hideable.add(control);

		control = new ShowEntitiesCheckbox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		hideable.add(control);

		advancedMobsButton = new GenericButton("Filter Mobs").setAlign(WidgetAnchor.TOP_CENTER);
		advancedMobsButton.setWidth(150).setHeight(20).setX(right).setY(top);
		advancedMobsButton.setTooltip("Select which mobs are shown on the minimap");
		screen.attachWidget("Spoutcraft", advancedMobsButton);

		hideable.add(advancedMobsButton);

		setControlsEnabled(MinimapConfig.getInstance().isEnabled());
	}

	@Override
	public void drawScreen(int x, int y, float z) {
		drawDefaultBackground();

		super.drawScreen(x, y, z);
	}

	@Override
	protected void buttonClicked(Button btn) {
		if (btn.equals(toggleCheckBox)) {
		    setControlsEnabled(((CheckBox) btn).isChecked());
		}
		if (btn.equals(doneButton)) {
			MinimapConfig.getInstance().save();
			SpoutClient.getHandle().displayGuiScreen(parent);
		}
		if (btn.equals(positionButton)) {
			mc.displayGuiScreen(new GuiMoveMinimap(this));
		}
		if (btn.equals(advancedMobsButton)) {
			mc.displayGuiScreen(new GuiAdvancedEntitySettings(this));
		}
	}
}
