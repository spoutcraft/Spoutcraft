/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.gui.settings;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.*;

public class GameSettingsScreen extends GuiScreen{
	private Button doneButton = null;
	public final GuiScreen parent;

	public GameSettingsScreen(GuiScreen parent) {
		this.parent = parent;
	}

	public void initGui() {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		Control control;

		GenericScrollArea screen = new GenericScrollArea();
		screen.setHeight(height - 16 - 24 - 40).setWidth(width).setY(24).setX(0);
		getScreen().attachWidget(spoutcraft, screen);

		GenericLabel label = new GenericLabel("Game Settings");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(10);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget(spoutcraft, label);

		int left = (int)(width / 2  - 155);
		int right = (int)(width / 2 + 5);

		control = new ResetButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(height - 30);
		getScreen().attachWidget(spoutcraft, control);

		doneButton = new GenericButton("Done");
		doneButton.setAlign(WidgetAnchor.CENTER_CENTER);
		doneButton.setX(right).setY(height - 30);
		doneButton.setHeight(20).setWidth(150);
		getScreen().attachWidget(spoutcraft, doneButton);

		int top = 5;

		Color grey = new Color(0.80F, 0.80F, 0.80F, 0.65F);

		//Controls + Audio
		label = new GenericLabel("Controls and Audio Settings");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(top);
		label.setTextColor(grey);
		screen.attachWidget(spoutcraft, label);
		top += 11;

		Gradient linebreak = new GenericGradient();
		linebreak.setBottomColor(grey);
		linebreak.setTopColor(grey);
		linebreak.setX(width/2 - 318 / 2).setY(top).setHeight(3).setWidth(318);
		screen.attachWidget(spoutcraft, linebreak);
		top += 6;

		control = new MusicSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new SoundEffectsSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		top += 22;

		control = new FieldOfViewSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new SensitivitySlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		top += 22;

		control = new InvertMouseButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new DifficultyButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		top += 22;

		control = new ControlsButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new LanguagesButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		top += 22;
		
		control = new ChatButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		top += 22;

		//Graphics
		label = new GenericLabel("Graphical Settings");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(top);
		label.setTextColor(grey);
		screen.attachWidget(spoutcraft, label);
		top += 11;

		linebreak = new GenericGradient();
		linebreak.setBottomColor(grey);
		linebreak.setTopColor(grey);
		linebreak.setX(width/2 - 318 / 2).setY(top).setHeight(3).setWidth(318);
		screen.attachWidget(spoutcraft, linebreak);
		top += 6;

		Label message = new GenericLabel("Spoutworth here, your loyal butler and assistant. Can I be of \n" +
										"assistance? Yes? Excellent. I will be managing your graphical and\n" +
										"performance settings to maximize framerate and quality. You can\n" +
										"adjust my priorities, or dismiss me and manually select settings.");
		message.setWidth(150).setHeight(20).setX(left).setY(top);
		
		if (ConfigReader.automatePerformance) {
			screen.attachWidget(spoutcraft, message);
			
			top += 47;
		}

		RadioButton button;
		button = (RadioButton) new FavorPerformanceButton("Favor Performance", message).setGroup(1).setAlign(WidgetAnchor.TOP_CENTER);
		button.setWidth(150).setHeight(20).setX(left).setY(top);
		button.setTooltip("Spoutworth will attempt to provide smooth framerates, potentially at the cost of appearance.");
		screen.attachWidget(spoutcraft, button);
		button.setSelected(ConfigReader.automatePerformance && ConfigReader.automateMode == 0);

		button = (RadioButton) new OptimalGameplayButton("Balanced Gameplay", message).setGroup(1).setAlign(WidgetAnchor.TOP_CENTER);
		button.setWidth(150).setHeight(20).setX(right).setY(top);
		button.setTooltip("Spoutworth will attempt to provide reasonable framerates and appearance.");
		screen.attachWidget(spoutcraft, button);
		button.setSelected(ConfigReader.automatePerformance && ConfigReader.automateMode == 1);

		top += 22;

		button = (RadioButton) new FavorAppearanceButton("Favor Appearance", message).setGroup(1).setAlign(WidgetAnchor.TOP_CENTER);
		button.setWidth(150).setHeight(20).setX(left).setY(top);
		button.setTooltip("Spoutworth will attempt to provide the best appearance, but potentially at the cost of framerates.");
		screen.attachWidget(spoutcraft, button);
		button.setSelected(ConfigReader.automatePerformance && ConfigReader.automateMode == 2);

		button = (RadioButton) new ManualSelectionButton("Manual Selection", message).setGroup(1).setAlign(WidgetAnchor.TOP_CENTER);
		button.setWidth(150).setHeight(20).setX(right).setY(top);
		button.setTooltip("Dismiss Spoutworth and adjust the settings manually yourself.");
		screen.attachWidget(spoutcraft, button);
		button.setSelected(!ConfigReader.automatePerformance);

		top += 22;

		linebreak = new GenericGradient();
		linebreak.setBottomColor(grey);
		linebreak.setTopColor(grey);
		linebreak.setX(width/2 - 318 / 2).setY(top).setHeight(3).setWidth(318);
		screen.attachWidget(spoutcraft, linebreak);
		top += 6;

		ArrayList<CheckBox> graphicCheckboxes = new ArrayList<CheckBox>();
		control = new FancyGraphicsButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		UUID fancyGraphics = control.getId();

		control = new FancyCloudsButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		graphicCheckboxes.add((CheckBox) control);

		top += 22;

		control = new FancyGrassButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		graphicCheckboxes.add((CheckBox) control);

		control = new FancyFogButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		graphicCheckboxes.add((CheckBox) control);

		top += 22;

		control = new BiomeColorsButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		graphicCheckboxes.add((CheckBox) control);

		control = new FancyTreesButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		graphicCheckboxes.add((CheckBox) control);

		top += 22;

		control = new FancyWaterButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		graphicCheckboxes.add((CheckBox) control);

		control = new FancyWeatherButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		graphicCheckboxes.add((CheckBox) control);

		top += 22;

		control = new FancyLightingButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		graphicCheckboxes.add((CheckBox) control);

		control = new FancyParticlesButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		graphicCheckboxes.add((CheckBox) control);

		((FancyGraphicsButton)screen.getWidget(fancyGraphics)).setLinkedButtons(graphicCheckboxes);

		top += 22;

		control = new SmoothFPSButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		
		control = new ServerLightButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);

		top += 22;


		control = new RenderDistanceButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new BetterGrassButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		top += 22;

		top += 5;

		//Performance
		label = new GenericLabel("Performance Settings");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(top);
		label.setTextColor(grey);
		screen.attachWidget(spoutcraft, label);
		top += 11;

		linebreak = new GenericGradient();
		linebreak.setBottomColor(grey);
		linebreak.setTopColor(grey);
		linebreak.setX(width/2 - 318 / 2).setY(top).setHeight(3).setWidth(318);
		screen.attachWidget(spoutcraft, linebreak);
		top += 6;

		control = new PreloadedChunksButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new FarViewButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);

		top += 22;

		control = new PerformanceButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new AutosaveButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		top += 22;

		control = new AdvancedOpenGLButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new SignDistanceButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);

		top += 22;

		top += 5;

		label = new GenericLabel("Appearance Settings");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(top);
		label.setTextColor(grey);
		screen.attachWidget(spoutcraft, label);
		top += 11;

		linebreak = new GenericGradient();
		linebreak.setBottomColor(grey);
		linebreak.setTopColor(grey);
		linebreak.setX(width/2 - 318 / 2).setY(top).setHeight(3).setWidth(318);
		screen.attachWidget(spoutcraft, linebreak);
		top += 6;

		control = new TimeButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new FastDebugInfoButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		top += 22;

		control = new SkyToggleButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new WeatherToggleButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		top += 22;

		control = new StarsToggleButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new ClearWaterToggleButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		top += 22;

		control = new ViewBobbingButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new VoidFogButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		top += 22;

		control = new DelayedTooltipCheckbox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new WaterBiomeColorsButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);

		top += 22;

		control = new Anaglyph3DButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new GuiScaleButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);

		top += 22;

		control = new SmoothLightingSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new BrightnessSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);

		top += 22;

		control = new MipMapSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		
		control = new FlightSpeedSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
	}

	@Override
	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
	}

	@Override
	protected void buttonClicked(Button btn) {
		if (btn.equals(doneButton)) {
			SpoutClient.getHandle().displayGuiScreen(parent);
		}
	}
}
