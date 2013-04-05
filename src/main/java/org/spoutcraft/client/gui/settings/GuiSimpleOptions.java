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
package org.spoutcraft.client.gui.settings;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.CheckBox;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.Control;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericCheckBox;
import org.spoutcraft.api.gui.GenericGradient;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericScrollArea;
import org.spoutcraft.api.gui.Gradient;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.api.gui.RadioButton;
import org.spoutcraft.api.gui.RenderPriority;
import org.spoutcraft.api.gui.WidgetAnchor;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.gui.settings.controls.ChatButton;
import org.spoutcraft.client.gui.settings.controls.ControlsButton;
import org.spoutcraft.client.gui.settings.controls.DifficultyButton;
import org.spoutcraft.client.gui.settings.controls.FavorAppearanceButton;
import org.spoutcraft.client.gui.settings.controls.FavorPerformanceButton;
import org.spoutcraft.client.gui.settings.controls.FieldOfViewSlider;
import org.spoutcraft.client.gui.settings.controls.LanguagesButton;
import org.spoutcraft.client.gui.settings.controls.ManualSelectionButton;
import org.spoutcraft.client.gui.settings.controls.MinimapButton;
import org.spoutcraft.client.gui.settings.controls.MusicSlider;
import org.spoutcraft.client.gui.settings.controls.OptimalGameplayButton;
import org.spoutcraft.client.gui.settings.controls.ResetButton;
import org.spoutcraft.client.gui.settings.controls.SoundEffectsSlider;

public class GuiSimpleOptions extends GuiScreen {
	GuiScreen parent = null;
	Button doneButton, chatButton;
	GenericScrollArea scroll;
	Label title;
	CheckBox switchToAdvancedCheck;

	public static GuiScreen constructOptionsScreen(GuiScreen parent) {
		return Configuration.isAdvancedOptions() ? new GuiAdvancedOptions(parent) : new GuiSimpleOptions(parent);
	}

	public GuiSimpleOptions(GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	public void initGui() {
		Control control;

		GenericScrollArea screen = new GenericScrollArea();
		scroll = screen;
		screen.setHeight(height - 24 - 30).setWidth(width).setY(24).setX(0);
		getScreen().attachWidget("Spoutcraft", screen);

		GenericLabel label = new GenericLabel("Game Settings");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(10);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget("Spoutcraft", label);

		int left = (int)(width / 2  - 155);
		int right = (int)(width / 2 + 5);
		int center = (int)(width / 2 - 80);

		control = new ResetButton(parent).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(height - 25);
		getScreen().attachWidget("Spoutcraft", control);

		switchToAdvancedCheck = new GenericCheckBox("Advanced");
		switchToAdvancedCheck.setChecked(false);
		switchToAdvancedCheck.setX(5).setY(3).setWidth(100).setHeight(20);
		switchToAdvancedCheck.setPriority(RenderPriority.Low);
		getScreen().attachWidget("Spoutcraft", switchToAdvancedCheck);

		doneButton = new GenericButton("Done");
		doneButton.setAlign(WidgetAnchor.CENTER_CENTER);
		doneButton.setX(right).setY(height - 25);
		doneButton.setHeight(20).setWidth(150);
		getScreen().attachWidget("Spoutcraft", doneButton);

		int top = 5;

		Color grey = new Color(0.80F, 0.80F, 0.80F, 0.65F);

		label = new GenericLabel("Controls and Audio Settings");
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

		control = new MusicSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new SoundEffectsSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;

		control = new FieldOfViewSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new DifficultyButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;

		control = new ControlsButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new LanguagesButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;

		control = new ChatButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new MinimapButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		// Graphics
		label = new GenericLabel("Graphical Settings");
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

		// TODO Clean up references to Spoutworth and remove message
		Label message = new GenericLabel("");
		message.setWidth(150).setHeight(20).setX(left).setY(top);

		if (Configuration.isAutomatePerformance()) {
			screen.attachWidget("Spoutcraft", message);

			//top += 47;
		}

		RadioButton button;
		button = (RadioButton) new FavorPerformanceButton("Favor Performance", message).setGroup(1).setAlign(WidgetAnchor.TOP_CENTER);
		button.setWidth(150).setHeight(20).setX(left).setY(top);
		button.setTooltip("Spoutcraft will attempt to provide smooth framerates, potentially at the cost of appearance.");
		screen.attachWidget("Spoutcraft", button);
		button.setSelected(Configuration.isAutomatePerformance() && Configuration.getAutomateMode() == 0);

		button = (RadioButton) new OptimalGameplayButton("Balanced Gameplay", message).setGroup(1).setAlign(WidgetAnchor.TOP_CENTER);
		button.setWidth(150).setHeight(20).setX(right).setY(top);
		button.setTooltip("Spoutcraft will attempt to provide reasonable framerates and appearance.");
		screen.attachWidget("Spoutcraft", button);
		button.setSelected(Configuration.isAutomatePerformance() && Configuration.getAutomateMode() == 1);

		top += 22;

		button = (RadioButton) new FavorAppearanceButton("Favor Appearance", message).setGroup(1).setAlign(WidgetAnchor.TOP_CENTER);
		button.setWidth(150).setHeight(20).setX(left).setY(top);
		button.setTooltip("Spoutcraft will attempt to provide the best appearance, but potentially at the cost of framerates.");
		screen.attachWidget("Spoutcraft", button);
		button.setSelected(Configuration.isAutomatePerformance() && Configuration.getAutomateMode() == 2);

		button = (RadioButton) new ManualSelectionButton("Manual Selection", message, parent).setGroup(1).setAlign(WidgetAnchor.TOP_CENTER);
		button.setWidth(150).setHeight(20).setX(right).setY(top);
		button.setTooltip("Disable automatic performance settings and adjust the settings manually.");
		screen.attachWidget("Spoutcraft", button);
		button.setSelected(!Configuration.isAutomatePerformance());

		top += 22;
		// TODO add option controls to the scroll area
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected void buttonClicked(Button btn) {
		if (btn == doneButton) {
			SpoutClient.getHandle().displayGuiScreen(parent);
		}
		if (btn == switchToAdvancedCheck) {
			Configuration.setAdvancedOptions(true);
			Configuration.write();
			SpoutClient.getHandle().displayGuiScreen(new GuiAdvancedOptions(parent));
		}
	}

}
