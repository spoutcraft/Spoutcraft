/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.getspout.spout.gui.settings;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.src.GuiScreen;
import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.*;

public class VideoSettings extends GuiScreen{
	private Button doneButton = null;
	public final GuiScreen parent;
	
	public VideoSettings(GuiScreen parent) {
		this.doFramerateLimit = true;
		this.parent = parent;
	}
	
	public void initGui() {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		
		GenericScrollArea screen = new GenericScrollArea();
		screen.setHeight(height - 16 - 24 - 40).setWidth(width).setY(16+24).setX(0);
		getScreen().attachWidget(spoutcraft, screen);
		
		GenericLabel label = new GenericLabel("Video Settings");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(16);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget(spoutcraft, label);
		
		doneButton = new GenericButton("Done");
		doneButton.setAlign(WidgetAnchor.CENTER_CENTER);
		doneButton.setX((int) (width / 2 - 200 / 2)).setY(height - 30);
		doneButton.setHeight(20).setWidth(200);
		getScreen().attachWidget(spoutcraft, doneButton);
		
		int left = (int)(width / 2  - 155);
		int right = (int)(width / 2 + 5);
		int top = 5;
		
		Color grey = new Color(0.80F, 0.80F, 0.80F, 0.65F);
		
		label = new GenericLabel("Graphical Settings");
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
		
		
		ArrayList<CheckBox> graphicCheckboxes = new ArrayList<CheckBox>();
		Control control;
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
		
		((FancyGraphicsButton)screen.getWidget(fancyGraphics)).setLinkedButtons(graphicCheckboxes);
		
		top += 22;
		
		control = new Anaglyph3DButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		
		control = new SmoothFPSButton().setAlign(WidgetAnchor.TOP_CENTER);
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
		
		top += 5;
		
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
		
		
		
		
		
		control = new DynamicUpdatesButton().setAlign(WidgetAnchor.TOP_CENTER).setAuto(false);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		
		control = new FarViewButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		
		top += 22;
		
		control = new PreloadedChunksButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		
		control = new ChunkUpdatesButton().setAlign(WidgetAnchor.TOP_CENTER);
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
		
		control = new GuiScaleButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		top += 22;
		
		control = new SignDistanceButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		
		control = new OptimizeButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		top += 22;
		
		
		top += 5;
		
		label = new GenericLabel("Gameplay Settings");
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
		
		
		control = new RenderDistanceButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		
		control = new BetterGrassButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		top += 22;
		
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
	}

	@Override
	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
	}

	@Override
	protected void buttonClicked(Button btn) {
		if(btn.equals(doneButton)) {
			SpoutClient.getHandle().displayGuiScreen(parent);
		}
	}
}