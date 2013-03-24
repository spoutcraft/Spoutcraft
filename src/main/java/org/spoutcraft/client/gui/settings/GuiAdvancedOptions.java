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

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.*;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.gui.settings.controls.AdvancedOpenGLButton;
import org.spoutcraft.client.gui.settings.controls.AmbientOcclusionButton;
import org.spoutcraft.client.gui.settings.controls.Anaglyph3DButton;
import org.spoutcraft.client.gui.settings.controls.AutosaveButton;
import org.spoutcraft.client.gui.settings.controls.BetterGrassButton;
import org.spoutcraft.client.gui.settings.controls.BiomeColorsButton;
import org.spoutcraft.client.gui.settings.controls.BrightnessSlider;
import org.spoutcraft.client.gui.settings.controls.ChatButton;
import org.spoutcraft.client.gui.settings.controls.ChunkRenderSpeed;
import org.spoutcraft.client.gui.settings.controls.ClearWaterToggleButton;
import org.spoutcraft.client.gui.settings.controls.ConnectedTexturesButton;
import org.spoutcraft.client.gui.settings.controls.ControlsButton;
import org.spoutcraft.client.gui.settings.controls.DelayedTooltipCheckbox;
import org.spoutcraft.client.gui.settings.controls.DifficultyButton;
import org.spoutcraft.client.gui.settings.controls.FancyCloudsButton;
import org.spoutcraft.client.gui.settings.controls.FancyFogButton;
import org.spoutcraft.client.gui.settings.controls.FancyGraphicsButton;
import org.spoutcraft.client.gui.settings.controls.FancyGrassButton;
import org.spoutcraft.client.gui.settings.controls.FancyLightingButton;
import org.spoutcraft.client.gui.settings.controls.FancyParticlesButton;
import org.spoutcraft.client.gui.settings.controls.FancyTreesButton;
import org.spoutcraft.client.gui.settings.controls.FancyWaterButton;
import org.spoutcraft.client.gui.settings.controls.FancyWeatherButton;
import org.spoutcraft.client.gui.settings.controls.FarViewButton;
import org.spoutcraft.client.gui.settings.controls.FastDebugInfoButton;
import org.spoutcraft.client.gui.settings.controls.FavorAppearanceButton;
import org.spoutcraft.client.gui.settings.controls.FavorPerformanceButton;
import org.spoutcraft.client.gui.settings.controls.FieldOfViewSlider;
import org.spoutcraft.client.gui.settings.controls.FlightSpeedSlider;
import org.spoutcraft.client.gui.settings.controls.GuiScaleButton;
import org.spoutcraft.client.gui.settings.controls.HotbarQuickKeysButton;
import org.spoutcraft.client.gui.settings.controls.InvertMouseButton;
import org.spoutcraft.client.gui.settings.controls.LanguagesButton;
import org.spoutcraft.client.gui.settings.controls.ManualSelectionButton;
import org.spoutcraft.client.gui.settings.controls.MinimapButton;
import org.spoutcraft.client.gui.settings.controls.MipMapSlider;
import org.spoutcraft.client.gui.settings.controls.MusicSlider;
import org.spoutcraft.client.gui.settings.controls.OptimalGameplayButton;
import org.spoutcraft.client.gui.settings.controls.PerformanceButton;
import org.spoutcraft.client.gui.settings.controls.RandomMobTextureButton;
import org.spoutcraft.client.gui.settings.controls.RenderDistanceButton;
import org.spoutcraft.client.gui.settings.controls.ReplaceBlocksButton;
import org.spoutcraft.client.gui.settings.controls.ReplaceToolsButton;
import org.spoutcraft.client.gui.settings.controls.ResetButton;
import org.spoutcraft.client.gui.settings.controls.ResizeScreenshotButton;
import org.spoutcraft.client.gui.settings.controls.ResizeScreenshotHeightField;
import org.spoutcraft.client.gui.settings.controls.ResizeScreenshotWidthField;
import org.spoutcraft.client.gui.settings.controls.SensitivitySlider;
import org.spoutcraft.client.gui.settings.controls.ServerLightButton;
import org.spoutcraft.client.gui.settings.controls.ServerTexturesButton;
import org.spoutcraft.client.gui.settings.controls.SignDistanceButton;
import org.spoutcraft.client.gui.settings.controls.SkyToggleButton;
import org.spoutcraft.client.gui.settings.controls.SmoothFPSButton;
import org.spoutcraft.client.gui.settings.controls.SmoothLightingSlider;
import org.spoutcraft.client.gui.settings.controls.SnooperButton;
import org.spoutcraft.client.gui.settings.controls.SoundEffectsSlider;
import org.spoutcraft.client.gui.settings.controls.StarsToggleButton;
import org.spoutcraft.client.gui.settings.controls.TimeButton;
import org.spoutcraft.client.gui.settings.controls.ViewBobbingButton;
import org.spoutcraft.client.gui.settings.controls.VoidFogButton;
import org.spoutcraft.client.gui.settings.controls.WaterBiomeColorsButton;
import org.spoutcraft.client.gui.settings.controls.WeatherToggleButton;

public class GuiAdvancedOptions extends GuiScreen {
	private Button doneButton = null;
	private CheckBox switchToSimpleCheck;
	public final GuiScreen parent;
	private GenericScrollArea scrollArea;

	public GuiAdvancedOptions(GuiScreen parent) {
		this.parent = parent;
	}

	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		int scroll = scrollArea == null ? 0 : scrollArea.getScrollPosition(Orientation.VERTICAL);
		super.setWorldAndResolution(mc, width, height);
		scrollArea.setScrollPosition(Orientation.VERTICAL, scroll);
	}

	public void initGui() {
		Control control;

		GenericScrollArea screen = new GenericScrollArea();
		scrollArea = screen;
		screen.setHeight(height - 24 - 30).setWidth(width).setY(24).setX(0);
		getScreen().attachWidget("Spoutcraft", screen);

		GenericLabel label = new GenericLabel("Game Settings");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(10);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget("Spoutcraft", label);

		int left = (int)(width / 2  - 155);
		int right = (int)(width / 2 + 5);
		int center = (int)(width / 2 - 75);

		control = new ResetButton(parent).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(height - 25);
		getScreen().attachWidget("Spoutcraft", control);

		switchToSimpleCheck = new GenericCheckBox("Advanced");
		switchToSimpleCheck.setChecked(true);
		switchToSimpleCheck.setAlign(WidgetAnchor.CENTER_CENTER);
		switchToSimpleCheck.setX(5).setY(3).setWidth(100).setHeight(20);
		switchToSimpleCheck.setPriority(RenderPriority.Low);
		getScreen().attachWidget("Spoutcraft", switchToSimpleCheck);

		doneButton = new GenericButton("Done");
		doneButton.setAlign(WidgetAnchor.CENTER_CENTER);
		doneButton.setX(right).setY(height - 25);
		doneButton.setHeight(20).setWidth(150);
		getScreen().attachWidget("Spoutcraft", doneButton);

		int top = 5;

		Color grey = new Color(0.80F, 0.80F, 0.80F, 0.65F);

		// Controls and audio
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

		control = new SensitivitySlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;

		control = new InvertMouseButton().setAlign(WidgetAnchor.TOP_CENTER);
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

		linebreak = new GenericGradient();
		linebreak.setBottomColor(grey);
		linebreak.setTopColor(grey);
		linebreak.setX(width/2 - 318 / 2).setY(top).setHeight(3).setWidth(318);
		screen.attachWidget("Spoutcraft", linebreak);
		top += 6;

		ArrayList<CheckBox> graphicCheckboxes = new ArrayList<CheckBox>();

		control = new FancyGraphicsButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX((int) (width / 2 - size / 2) - 75/2).setY(top);
		screen.attachWidget("Spoutcraft", control);
		UUID fancyGraphics = control.getId();
		top+=20;

		control = new FancyGrassButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);

		control = new FancyCloudsButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);

		top += 22;

		control = new BiomeColorsButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);

		control = new FancyFogButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);

		top += 22;

		control = new FancyWaterButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);

		control = new FancyTreesButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);

		top += 22;

		control = new FancyLightingButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);

		control = new FancyWeatherButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);

		top += 22;

		control = new SmoothFPSButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new FancyParticlesButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);

		((FancyGraphicsButton)screen.getWidget(fancyGraphics)).setLinkedButtons(graphicCheckboxes);

		top += 22;

		control = new ConnectedTexturesButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new ServerLightButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		control = new RandomMobTextureButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new AmbientOcclusionButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		control = new RenderDistanceButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new BetterGrassButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;

		top += 5;

		// Performance
		label = new GenericLabel("Performance Settings");
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

		control = new ChunkRenderSpeed().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new FarViewButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		control = new PerformanceButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new AutosaveButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;

		control = new AdvancedOpenGLButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new SignDistanceButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		top += 5;

		// Appearance
		label = new GenericLabel("Appearance Settings");
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

		control = new TimeButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new FastDebugInfoButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;

		control = new SkyToggleButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new WeatherToggleButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;

		control = new StarsToggleButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new ClearWaterToggleButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;

		control = new ViewBobbingButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new VoidFogButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;

		control = new DelayedTooltipCheckbox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new WaterBiomeColorsButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		control = new Anaglyph3DButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new ServerTexturesButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		control = new SmoothLightingSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new BrightnessSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		control = new MipMapSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new FlightSpeedSlider().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		control = new HotbarQuickKeysButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new GuiScaleButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);

		top += 22;

		top += 5;

		// Inventory
		label = new GenericLabel("Inventory Settings");
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

		control = new ReplaceToolsButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		control = new ReplaceBlocksButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;

		top += 5;

		// Miscellaneous
		label = new GenericLabel("Miscellaneous Settings");
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

		control = new ResizeScreenshotButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);

		label = new GenericLabel("Size:");
		label.setWidth(150).setHeight(20).setX(right-7).setY(top + 6);
		screen.attachWidget("Spoutcraft", label);

		control = new ResizeScreenshotWidthField();
		control.setWidth(35).setHeight(15).setX(right+20).setY(top + 2);
		screen.attachWidget("Spoutcraft", control);

		label = new GenericLabel("X");
		label.setWidth(150).setHeight(20).setX(right+63).setY(top + 6);
		screen.attachWidget("Spoutcraft", label);

		control = new ResizeScreenshotHeightField();
		control.setWidth(35).setHeight(15).setX(right+75).setY(top + 2);
		screen.attachWidget("Spoutcraft", control);
		top += 22;

		control = new SnooperButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(center).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;
		top += 5;
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

		if (btn.equals(switchToSimpleCheck)) {
			Configuration.setAdvancedOptions(false);
			SpoutClient.getHandle().displayGuiScreen(new GuiSimpleOptions(parent));
		}
	}
}
