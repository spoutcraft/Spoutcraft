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
import java.util.HashMap;
import java.util.UUID;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.Tessellator;

import org.getspout.spout.client.SpoutClient;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.gui.*;

public class VideoSettings extends GuiScreen{
	private Button doneButton = null;
	private Label title = null;
	public final GuiScreen parent;
	
	public VideoSettings(GuiScreen parent) {
		this.parent = parent;
	}
	
	public void initGui() {
		GenericScrollArea screen = new GenericScrollArea();
		screen.setHeight(height - 16 - 24 - 40).setWidth(width).setY(16+24).setX(0);
		getScreen().attachWidget("Spoutcraft", screen);
		
		GenericLabel label = new GenericLabel("Video Settings");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(16);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget("Spoutcraft", label);
		title = label;
		
		doneButton = new GenericButton("Done");
		doneButton.setAlign(WidgetAnchor.CENTER_CENTER);
		doneButton.setX((int) (width / 2 - 200 / 2)).setY(height - 30);
		doneButton.setHeight(20).setWidth(200);
		getScreen().attachWidget("Spoutcraft", doneButton);
		
		int left = (int)(width / 2  - 155);
		int right = (int)(width / 2 + 5);
		int top = 5;
		
		Color grey = new Color(0.80F, 0.80F, 0.80F, 0.65F);
		
		label = new GenericLabel("Graphical Settings");
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
		
		
		ArrayList<CheckBox> graphicCheckboxes = new ArrayList<CheckBox>();
		Control control;
		control = new FancyGraphicsButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);
		UUID fancyGraphics = control.getId();
		
		control = new FancyCloudsButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);
		
		top += 22;
		
		control = new FancyGrassButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);
		
		control = new FancyFogButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);
		
		top += 22;
		
		control = new BiomeColorsButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);
		
		control = new FancyTreesButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);
		
		top += 22;
		
		control = new FancyWaterButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);
		
		control = new FancyWeatherButton(fancyGraphics).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		graphicCheckboxes.add((CheckBox) control);
		
		((FancyGraphicsButton)screen.getWidget(fancyGraphics)).setLinkedButtons(graphicCheckboxes);
		
		top += 22;
		
		control = new Anaglyph3DButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);
		
		control = new SmoothFPSButton().setAlign(WidgetAnchor.TOP_CENTER);
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
		
		top += 5;
		
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
		
		
		
		
		
		control = new DynamicUpdatesButton().setAlign(WidgetAnchor.TOP_CENTER).setAuto(false);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);
		
		control = new FarViewButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		
		top += 22;
		
		control = new PreloadedChunksButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);
		
		control = new ChunkUpdatesButton().setAlign(WidgetAnchor.TOP_CENTER);
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
		
		control = new GuiScaleButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;
		
		control = new SignDistanceButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);
		
		control = new OptimizeButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;
		
		
		top += 5;
		
		label = new GenericLabel("Gameplay Settings");
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
		
		
		control = new RenderDistanceButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget("Spoutcraft", control);
		
		control = new BetterGrassButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget("Spoutcraft", control);
		top += 22;
		
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
		//RIGHT CONTROL HERE
		
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
	
	/*
	FOG_FANCY("FOG_FANCY", 15, "FOG_FANCY", 15, "Fog", false, false),
	VOID_FOG("VOID_FOG", 16, "VOID_FOG", 16, "Void Fog", false, false, false),
	//MIPMAP_LEVEL("MIPMAP_LEVEL", 17, "MIPMAP_LEVEL", 17, "Mipmap Level", false, false),
	//MIPMAP_TYPE("MIPMAP_TYPE", 16, "MIPMAP_TYPE", 16, "Mipmap Type", false, false),
	LOAD_FAR("LOAD_FAR", 17, "LOAD_FAR", 18, "Load Far", false, false),
	PRELOADED_CHUNKS("PRELOADED_CHUNKS", 18, "PRELOADED_CHUNKS", 19, "Preloaded Chunks", false, false),
	SMOOTH_FPS("SMOOTH_FPS", 19, "SMOOTH_FPS", 20, "Smooth FPS", false, false),
	//BRIGHTNESS("BRIGHTNESS", 20, "BRIGHTNESS", 21, "Brightness", true, false, true),
	GAMMA("GAMMA", 20, "GAMMA", 21, "options.gamma", true, false, false),
	CLOUDS("CLOUDS", 21, "CLOUDS", 22, "Clouds", false, false),
	CLOUD_HEIGHT("CLOUD_HEIGHT", 22, "CLOUD_HEIGHT", 23, "Cloud Height", true, false, true),
	TREES("TREES", 23, "TREES", 24, "Trees", false, false),
	GRASS("GRASS", 24, "GRASS", 25, "Grass", false, false),
	RAIN("RAIN", 25, "RAIN", 27, "Rain & Snow", false, false),
	WATER("WATER", 26, "RAIN", 28, "Water", false, false),
	AO_LEVEL("AO_LEVEL", 31, "AO_LEVEL", 33, "Smooth Lighting", true, false),
	FAST_DEBUG_INFO("FAST_DEBUG_INFO", 32, "FAST_DEBUG_INFO", 34, "Fast Debug Info", false, false),
	AUTOSAVE_TICKS("AUTOSAVE_TICKS", 33, "AUTOSAVE_TICKS", 35, "Autosave", false, false),
	BETTER_GRASS("BETTER_GRASS", 34, "BETTER_GRASS", 36, "Better Grass", false, false),
	WEATHER("WEATHER", 39, "WEATHER", 41, "Weather", false, false, true),
	SKY("SKY", 40, "SKY", 42, "Sky", false, false, true),
	STARS("STARS", 41, "STARS", 43, "Stars", false, false, true),
	FAR_VIEW("FAR_VIEW", 42, "FAR_VIEW", 44, "Far View", false, false),
	CHUNK_UPDATES("CHUNK_UPDATES", 43, "CHUNK_UPDATES", 45, "Chunk Updates", false, false),
	CHUNK_UPDATES_DYNAMIC("CHUNK_UPDATES_DYNAMIC", 44, "CHUNK_UPDATES_DYNAMIC", 46, "Dynamic Updates", false, false),
	TIME("TIME", 45, "TIME", 47, "Time", false, false, true),
	CLEAR_WATER("CLEAR_WATER", 46, "CLEAR_WATER", 48, "Clear Water", false, false, true),
	BIOME_COLORS("BIOME_COLORS", 49, "BIOME_COLORS", 50, "Biome Colors", false, false),
	SIGN_DISTANCE("SIGN_DISTANCE", 51, "SIGN_DISTANCE", 52, "Sign Distance", false, false);
	*/
}

/*

private String[] getTooltipLines(String option) {
	final String[] cheating = {"This option has been disabled by the server because", " it is considered cheating.", " ", "Contact your admin if you would like it enabled"};
	if (option.equals("Clouds")) {
		return new String[]{"Clouds", "  Default - as set by setting Graphics", "  Fast - lower quality, faster", "  Fancy - higher quality, slower", "  OFF - no clouds, fastest", "Fast clouds are rendered 2D.", "Fancy clouds are rendered 3D."};
	}
	else if (option.equals("Cloud Height")) {
		if (!SpoutClient.getInstance().isCloudHeightCheat()) {
			return cheating;
		}
		return new String[]{"Cloud Height", "  OFF - default height", "  100% - above world height limit"};
	}
	else if (option.equals("Trees")) {
		return new String[]{"Trees", "  Default - as set by setting Graphics", "  Fast - lower quality, faster", "  Fancy - higher quality, slower", "Fast trees have opaque leaves.", "Fancy trees have transparent leaves."};
	}
	else if (option.equals("Grass")) {
		return new String[]{"Grass", "  Default - as set by setting Graphics", "  Fast - lower quality, faster", "  Fancy - higher quality, slower", "Fast grass uses default side texture.", "Fancy grass uses biome side texture."};
	}
	else if (option.equals("Water")) {
		return new String[]{"Water", "  Default - as set by setting Graphics", "  Fast  - lower quality, faster", "  Fancy - higher quality, slower", "Fast water (1 pass) has some visual artifacts", "Fancy water (2 pass) has no visual artifacts"};
	}
	else if (option.equals("Rain & Snow")) {
		return new String[]{"Rain & Snow", "  Default - as set by setting Graphics", "  Fast  - light rain/snow, faster", "  Fancy - heavy rain/snow, slower", "  OFF - no rain/snow, fastest", "When rain is OFF the splashes and rain sounds", "are still active."};
	}
	else if (option.equals("Sky")) {
		if (!SpoutClient.getInstance().isSkyCheat()) {
			return cheating;
		}
		return new String[]{"Sky", "  ON - sky is visible, slower", "  OFF  - sky is not visible, faster", "When sky is OFF the moon and sun are still visible."};
	}
	else if (option.equals("Stars")) {
		if (!SpoutClient.getInstance().isStarsCheat()) {
			return cheating;
		}
		return new String[]{"Stars", "  ON - stars are visible, slower", "  OFF  - stars are not visible, faster"};
	}
	else if (option.equals("Better Grass")) {
		return new String[]{"Better Grass", "  OFF - default side grass texture, fastest", "  Fast - full side grass texture, slower", "  Fancy - dynamic side grass texture, slowest"};
	}
	else if (option.equals("Weather")) {
		if (!SpoutClient.getInstance().isWeatherCheat()) {
			return cheating;
		}
		return new String[]{"Weather", "  ON - weather is active, slower", "  OFF  - weather is not active, faster", "The weather controls rain, snow and thunderstorms."};
	}
	else if (option.equals("Autosave")) {
		return new String[]{"Autosave interval", "Default autosave interval (2s) is NOT RECOMMENDED.", "Autosave causes the famous Lag Spike of Death."};
	}
	else if (option.equals("Fast Debug Info")) {
		return new String[]{"Fast Debug Info", " OFF - default debug info screen, slower", " ON - debug info screen without lagometer, faster", "Removes the lagometer from the debug screen (F3)."};
	}
	else if (option.equals("Chunk Updates")) {
		return new String[]{"Chunk updates per frame", " 1 - (default) slower world loading, higher FPS", " 3 - faster world loading, lower FPS", " 5 - fastest world loading, lowest FPS"};
	}
	else if (option.equals("Dynamic Updates")) {
		return new String[]{"Chunk updates per frame", " OFF - (default) standard chunk updates per frame", " ON - more updates while the player is standing still", "Dynamic updates force more chunk updates while", "the player is standing still to load the world faster."};
	}
	else if (option.equals("Far View")) {
		return new String[]{"Far View", " OFF - (default) standard view distance", " ON - 3x view distance", "Far View is very resource demanding!", "3x view distance => 9x chunks to be loaded => FPS / 9", "Standard view distances: 32, 64, 128, 256", "Far view distances: 96, 192, 384, 512"};
	}
	else if (option.equals("Clear Water")) {
		if (!SpoutClient.getInstance().isClearWaterCheat()) {
			return cheating;
		}
		return new String[]{"Clear Water", " OFF - (default) standard water view", " ON - can see deeper through water, no longer obscures vision", "Clear water is very resource demanding!",};
	}
	else if (option.equals("Time")) {
		if (!SpoutClient.getInstance().isTimeCheat()) {
			return cheating;
		}
		return new String[]{"Time", " Default - normal day/night cycles", " Day Only - day only", " Night Only - night only"};
	}
	else if (option.equals("Biome Colors")) {
		return new String[]{"Biome Colors", " Fast - caches colors for grass and water per chunk.", " May cause sharp changes in color near chunk edges.", "", " Fancy - normal coloring for grass and water.", " Calculates color for water and grass for each block."};
	}
	return null;
}
private String[] getTooltipLines(String option) {
final String[] cheating = {"This option has been disabled by the server because", " it is considered cheating.", " ", "Contact your admin if you would like it enabled"};
final String[] mipmap = {"This option has been disabled because your texture pack", " does not support mipmaps.", " ", "Full mipmap support for all texture packs is planned"};
if (option.equals("Graphics")) {
	return new String[]{"Visual quality", "  Fast  - lower quality, faster", "  Fancy - higher quality, slower", "Changes the appearance of clouds, leaves, water,", "shadows and grass sides."};
}
else if (option.equals("Render Distance")) {
	return new String[]{"Visible distance", "  Far - 256m (slower)", "  Normal - 128m", "  Short - 64m (faster)", "  Tiny - 32m (fastest)"};
}
else if (option.equals("Smooth Lighting")) {
	return new String[]{"Smooth lighting", "  OFF - no smooth lighting (faster)", "  1% - light smooth lighting (slower)", "  100% - dark smooth lighting (slower)"};
}
else if (option.equals("Performance")) {
	return new String[]{"FPS Limit", "  Max FPS - no limit (fastest)", "  Balanced - limit 120 FPS (slower)", "  Power saver - limit 40 FPS (slowest)", "  VSync - limit to monitor framerate (60, 30, 20)", "Balanced and Power saver decrease the FPS even if", "the limit value is not reached."};
}
else if (option.equals("3D Anaglyph")) {
	return new String[]{"3D mode used with red-cyan 3D glasses."};
}
else if (option.equals("View Bobbing")) {
	return new String[]{"More realistic movement.", "When using mipmaps set it to OFF for best results."};
}
else if (option.equals("GUI Scale")) {
	return new String[]{"GUI Scale", "Smaller GUI might be faster"};
}
else if (option.equals("Advanced OpenGL")) {
	return new String[]{"Detect and render only visible geometry", "  OFF - all geometry is rendered (slower)", "  Fast - only visible geometry is rendered (fastest)", "  Fancy - conservative, avoids visual artifacts (faster)", "The option is available only if it is supported by the ", "graphic card."};
}
else if (option.equals("Fog")) {
	return new String[]{"Fog type", "  Fast - faster fog", "  Fancy - slower fog, looks better", "The fancy fog is available only if it is supported by the ", "graphic card."};
}
else if (option.equals("Void Fog")) {
	return new String[]{"Void Fog", "  ON - A dark fog that obscures vision appears at low" , " levels of the map.", "  OFF - normal view distance at all height levels."};
}
else if (option.equals("Mipmap Level")) {
	if (!Config.canUseMipmaps()) {
		return mipmap;
	}
	return new String[]{"Visual effect which makes distant objects look better", "by smoothing the texture details", "  OFF - no smoothing", "  1 - minimum smoothing", "  4 - maximum smoothing", "This option usually does not affect the performance."};
}
else if (option.equals("Mipmap Type")) {
	if (!Config.canUseMipmaps()) {
		return mipmap;
	}
	return new String[]{"Visual effect which makes distant objects look better", "by smoothing the texture details", "  Nearest - rough smoothing", "  Linear - fine smoothing", "This option usually does not affect the performance."};
}
else if (option.equals("Load Far")) {
	return new String[]{"Loads the world chunks at distance Far.", "Switching the render distance does not cause all chunks ", "to be loaded again.", "  OFF - world chunks loaded up to render distance", "  ON - world chunks loaded at distance Far, allows", "		 fast render distance switching"};
}
else if (option.equals("Preloaded Chunks")) {
	return new String[]{"Defines an area in which no chunks will be loaded", "  OFF - after 5m new chunks will be loaded", "  2 - after 32m  new chunks will be loaded", "  8 - after 128m new chunks will be loaded", "Higher values need more time to load all the chunks"};
}
else if (option.equals("Smooth FPS")) {
	return new String[]{"Stabilizes FPS by flushing the graphic driver buffers", "  OFF - no stabilization, FPS may fluctuate", "  ON - FPS stabilization", "This option is graphic driver dependant and its effect", "is not always visible"};
}
else if (option.equals("Sign Distance")) {
	return new String[]{"The distance from which you can see the text on a sign", "Farther distances can decreases FPS."};
}
else if (option.equals("Optimize Video Settings")) {
	if (this.mc.theWorld == null) {
		return new String[]{"This can only be used in game."};
	}
	return new String[]{"Attempts to configure your video settings to achieve ~60 fps.", "May be more or less, depending on hardware"};
}
else if (option.equals("Brightness")) {
	if (!SpoutClient.getInstance().isBrightnessCheat()) {
		return cheating;
	}
	return new String[]{"Increases the brightness of darker objects", "  OFF - standard brightness", "  100% - maximum brightness for darker objects", "This options does not change the brightness of ", "fully black objects"};
}
return null;
}
*/