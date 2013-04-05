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
package org.spoutcraft.client.config;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;

import org.spoutcraft.client.io.FileUtil;

public class Configuration {
	// Client settings
	private static int advancedOpenGL = 0;
	private static boolean anaglyph3D = false;
	private static int autosave = 0;
	public static int betterGrass = 0;
	private static boolean fancyBiomeColors = false;
	private static boolean waterBiomeColors = true;
	private static float brightnessSlider = 1F;

	private static boolean fancyClouds = false;
	private static boolean fancyFog = false;
	private static boolean fancyGraphics = false;
	public static boolean fancyGrass = false;
	private static boolean fancyTrees = false;
	private static boolean fancyWater = false;
	private static boolean fancyWeather = false;
	private static boolean farView = false;
	public static boolean fancyLight = false;
	private static boolean fancyParticles = false;
	private static int fastDebug = 0;
	private static int guiScale = 0;
	private static int performance = 0;
	private static int chunkRenderPasses = 2;
	private static int renderDistance = 0;
	private static int signDistance = 16;
	public static boolean cheatsky = true;
	private static boolean forcesky = false;
	private static boolean showsky = true;
	public static boolean cheatclearWater = false;
	private static boolean forceclearWater = false;
	private static boolean showclearWater = false;
	public static boolean cheatvoidFog = false;
	private static boolean forcevoidFog = false;
	private static boolean showvoidFog = true;
	public static boolean cheatweather = true;
	private static boolean forceweather = false;
	private static boolean showweather = true;
	public static boolean cheatstars = true;
	private static boolean forcestars = false;
	private static boolean showstars = true;
	private static boolean smoothFPS = false;
	public static float smoothLighting = 1F;
	private static int time = 0;
	private static boolean viewBobbing = false;
	private static boolean delayedTooltips = true;
	private static float mipmapsPercent = 0F;
	private static boolean automatePerformance = true;
	private static int automateMode = 0;
	private static boolean clientLight = false;
	private static float flightSpeedFactor = 1.0F;
	private static boolean replaceTools = false;
	private static boolean replaceBlocks = false;
	private static boolean hotbarQuickKeysEnabled = true;
	private static boolean resizeScreenshots = false;
	private static int resizedScreenshotWidth = 6000;
	private static int resizedScreenshotHeight = 3200;
	private static float chatOpacity = 0.5f;
	private static int mainMenuState = defaultMenuState();
	private static boolean connectedTextures = false;
	private static boolean advancedOptions = false;
	private static boolean randomMobTextures = true;
	public static boolean ambientOcclusion = false;
	private static boolean serverTexturePromptsEnabled = true;

	// Config specific
	private static transient Map<String, Object> defaultSettings = new HashMap<String, Object>();
	private static transient boolean dirty = false;
	private static transient boolean vsync = false;

	public static synchronized void read() {
		// Cleanup old
		File old = new File(FileUtil.getConfigDir(), "spoutcraft.properties");
		old.delete();

		File configFile = new File(FileUtil.getConfigDir(), "client.yml");
		try {
			if (!configFile.exists()) {
				configFile.createNewFile();
			}
			org.bukkit.util.config.Configuration config = new org.bukkit.util.config.Configuration(configFile);
			config.load();

			Field[] fields = Configuration.class.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field f = fields[i];
				if (Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers())) {
					f.setAccessible(true);
					Object value = f.get(null);
					defaultSettings.put(f.getName(), value);

					if (value instanceof Boolean) {
						f.set(null, config.getBoolean(f.getName(), (Boolean)value));
					} else if (value instanceof Integer) {
						f.set(null, config.getInt(f.getName(), (Integer)value));
					} else if (value instanceof Float) {
						f.set(null, (float)config.getDouble(f.getName(), (Float)value));
					} else if (value instanceof Double) {
						f.set(null, config.getDouble(f.getName(), (Double)value));
					}  else if (value instanceof String) {
						f.set(null, config.getString(f.getName(), (String)value));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		updateMCConfig();
	}

	public static synchronized void write() {
		dirty = false;
		File configFile = new File(FileUtil.getConfigDir(), "client.yml");
		try {
			if (!configFile.exists()) {
				configFile.createNewFile();
			}
			org.bukkit.util.config.Configuration config = new org.bukkit.util.config.Configuration(configFile);
			config.load();
			Field[] fields = Configuration.class.getDeclaredFields();
			for (Field f : fields) {
				if (Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers())) {
					Object value = f.get(null);
					config.setProperty(f.getName(), value);
				}
			}
			config.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void updateMCConfig() {
		Minecraft.theMinecraft.gameSettings.ambientOcclusion = Configuration.isAmbientOcclusion() ? 2 : 0;
		Minecraft.theMinecraft.gameSettings.anaglyph = Configuration.isAnaglyph3D();
		Minecraft.theMinecraft.gameSettings.renderDistance = Configuration.getRenderDistance();
		Minecraft.theMinecraft.gameSettings.fancyGraphics = Configuration.isFancyGraphics();
		Minecraft.theMinecraft.gameSettings.advancedOpengl = Configuration.getAdvancedOpenGL() !=0;
		Minecraft.theMinecraft.gameSettings.guiScale = Configuration.getGuiScale();
		Minecraft.theMinecraft.gameSettings.viewBobbing = Configuration.isViewBobbing();
		Minecraft.theMinecraft.gameSettings.gammaSetting = Configuration.getBrightnessSlider();

		Minecraft.theMinecraft.gameSettings.limitFramerate = Configuration.getPerformance();
		if (vsync != (Configuration.getPerformance() == 3)) {
			vsync = Configuration.getPerformance() == 3;
			org.lwjgl.opengl.Display.setVSyncEnabled(vsync);
		}

		if (Configuration.getSignDistance() < 8) {
			signDistance = 8;
		} else if (Configuration.getSignDistance() >= 128 && Configuration.getSignDistance() != Integer.MAX_VALUE) {
			signDistance = Integer.MAX_VALUE;
		}
	}

	public static int defaultMenuState() {
		if (isOpenGL(3)) {
			return 1;
		}
		if (isOpenGL(2)) {
			return 2;
		}
		return 3;
	}

	public static boolean isOpenGL(int v) {
		try {
			String version = GL11.glGetString(GL11.GL_VERSION);
			return Integer.parseInt(String.valueOf(version.charAt(0))) >= v;
		}
		catch (Exception e) {
			return false;
		}
	}

	public static synchronized void restoreDefaults() {
		Field[] fields = Configuration.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			Object value = defaultSettings.get(f.getName());
			if (value != null && f != null) {
				try {
					f.setAccessible(true);
					f.set(null, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		write();
		updateMCConfig();
	}

	public static synchronized int getAdvancedOpenGL() {
		return advancedOpenGL;
	}

	public static synchronized void setAdvancedOpenGL(int advancedOpenGL) {
		Configuration.advancedOpenGL = advancedOpenGL;
		onPropertyChange();
	}

	public static synchronized boolean isAnaglyph3D() {
		return anaglyph3D;
	}

	public static synchronized void setAnaglyph3D(boolean anaglyph3D) {
		Configuration.anaglyph3D = anaglyph3D;
		onPropertyChange();
	}

	public static synchronized int getAutosave() {
		return autosave;
	}

	public static synchronized void setAutosave(int autosave) {
		Configuration.autosave = autosave;
		onPropertyChange();
	}

	public static synchronized int getBetterGrass() {
		return betterGrass;
	}

	public static synchronized void setBetterGrass(int betterGrass) {
		Configuration.betterGrass = betterGrass;
		onPropertyChange();
	}

	public static synchronized boolean isFancyBiomeColors() {
		return fancyBiomeColors;
	}

	public static synchronized void setFancyBiomeColors(boolean fancyBiomeColors) {
		Configuration.fancyBiomeColors = fancyBiomeColors;
		onPropertyChange();
	}

	public static synchronized boolean isWaterBiomeColors() {
		return waterBiomeColors;
	}

	public static synchronized void setWaterBiomeColors(boolean waterBiomeColors) {
		Configuration.waterBiomeColors = waterBiomeColors;
		onPropertyChange();
	}

	public static synchronized float getBrightnessSlider() {
		return brightnessSlider;
	}

	public static synchronized void setBrightnessSlider(float brightnessSlider) {
		Configuration.brightnessSlider = brightnessSlider;
		onPropertyChange();
	}

	public static synchronized boolean isClearWater() {
		return showclearWater;
	}

	public static synchronized void setClearWater(boolean showclearWater) {
		Configuration.showclearWater = showclearWater;
		onPropertyChange();
	}

	public static synchronized boolean isFancyClouds() {
		return fancyClouds;
	}

	public static synchronized void setFancyClouds(boolean fancyClouds) {
		Configuration.fancyClouds = fancyClouds;
		onPropertyChange();
	}

	public static synchronized boolean isFancyFog() {
		return fancyFog;
	}

	public static synchronized void setFancyFog(boolean fancyFog) {
		Configuration.fancyFog = fancyFog;
		onPropertyChange();
	}

	public static synchronized boolean isFancyGraphics() {
		return fancyGraphics;
	}

	public static synchronized void setFancyGraphics(boolean fancyGraphics) {
		Configuration.fancyGraphics = fancyGraphics;
		onPropertyChange();
	}

	public static synchronized boolean isFancyGrass() {
		return fancyGrass;
	}

	public static synchronized void setFancyGrass(boolean fancyGrass) {
		Configuration.fancyGrass = fancyGrass;
		onPropertyChange();
	}

	public static synchronized boolean isFancyTrees() {
		return fancyTrees;
	}

	public static synchronized void setFancyTrees(boolean fancyTrees) {
		Configuration.fancyTrees = fancyTrees;
		onPropertyChange();
	}

	public static synchronized boolean isFancyWater() {
		return fancyWater;
	}

	public static synchronized void setFancyWater(boolean fancyWater) {
		Configuration.fancyWater = fancyWater;
		onPropertyChange();
	}

	public static synchronized boolean isFancyWeather() {
		return fancyWeather;
	}

	public static synchronized void setFancyWeather(boolean fancyWeather) {
		Configuration.fancyWeather = fancyWeather;
		onPropertyChange();
	}

	public static synchronized boolean isFarView() {
		return farView;
	}

	public static synchronized void setFarView(boolean farView) {
		Configuration.farView = farView;
		onPropertyChange();
	}

	public static synchronized boolean isFancyParticles() {
		return fancyParticles;
	}

	public static synchronized void setFancyParticles(boolean fancyParticles) {
		Configuration.fancyParticles = fancyParticles;
		onPropertyChange();
	}

	public static synchronized boolean isFancyLight() {
		return fancyLight;
	}

	public static synchronized void setFancyLight(boolean fancyLight) {
		Configuration.fancyLight = fancyLight;
		onPropertyChange();
	}

	public static synchronized int getFastDebug() {
		return fastDebug;
	}

	public static synchronized void setFastDebug(int fastDebug) {
		Configuration.fastDebug = fastDebug;
		onPropertyChange();
	}

	public static synchronized int getGuiScale() {
		return guiScale;
	}

	public static synchronized void setGuiScale(int guiScale) {
		Configuration.guiScale = guiScale;
		onPropertyChange();
	}

	public static synchronized int getPerformance() {
		return performance;
	}

	public static synchronized void setPerformance(int performance) {
		Configuration.performance = performance;
		onPropertyChange();
	}

	public static synchronized int getChunkRenderPasses() {
		return chunkRenderPasses;
	}

	public static synchronized void setChunkRenderPasses(int chunkRenderPasses) {
		Configuration.chunkRenderPasses = chunkRenderPasses;
		onPropertyChange();
	}

	public static synchronized int getRenderDistance() {
		return renderDistance;
	}

	public static synchronized void setRenderDistance(int renderDistance) {
		Configuration.renderDistance = renderDistance;
		onPropertyChange();
	}

	public static synchronized int getSignDistance() {
		return signDistance;
	}

	public static synchronized void setSignDistance(int signDistance) {
		Configuration.signDistance = signDistance;
		onPropertyChange();
	}

	public static synchronized boolean isCheatSky() {
		return cheatsky;
	}

	public static synchronized boolean isForceSky() {
		return forcesky;
	}

	public static synchronized boolean isSky() {
		return showsky;
	}

	public static synchronized void setSky(boolean showsky) {
		Configuration.showsky = showsky;
		onPropertyChange();
	}

	public static synchronized boolean isSmoothFPS() {
		return smoothFPS;
	}

	public static synchronized void setSmoothFPS(boolean smoothFPS) {
		Configuration.smoothFPS = smoothFPS;
		onPropertyChange();
	}

	public static synchronized float getSmoothLighting() {
		return smoothLighting;
	}

	public static synchronized void setSmoothLighting(float smoothLighting) {
		Configuration.smoothLighting = smoothLighting;
		onPropertyChange();
	}

	public static synchronized boolean isCheatStars() {
		return cheatstars;
	}

	public static synchronized boolean isForceStars() {
		return forcestars;
	}

	public static synchronized boolean isStars() {
		return showstars;
	}

	public static synchronized void setStars(boolean showstars) {
		Configuration.showstars = showstars;
		onPropertyChange();
	}

	public static synchronized int getTime() {
		return time;
	}

	public static synchronized void setTime(int time) {
		Configuration.time = time;
		onPropertyChange();
	}

	public static synchronized boolean isViewBobbing() {
		return viewBobbing;
	}

	public static synchronized void setViewBobbing(boolean viewBobbing) {
		Configuration.viewBobbing = viewBobbing;
		onPropertyChange();
	}

	public static synchronized boolean isCheatVoidFog() {
		return cheatvoidFog;
	}

	public static synchronized boolean isForceVoidFog() {
		return forcevoidFog;
	}

	public static synchronized boolean isVoidFog() {
		return showvoidFog;
	}

	public static synchronized void setVoidFog(boolean showvoidFog) {
		Configuration.showvoidFog = showvoidFog;
		onPropertyChange();
	}

	public static synchronized boolean isCheatWeather() {
		return cheatweather;
	}

	public static synchronized boolean isForceWeather() {
		return forceweather;
	}

	public static synchronized boolean isWeather() {
		return showweather;
	}

	public static synchronized void setWeather(boolean showweather) {
		Configuration.showweather = showweather;
		onPropertyChange();
	}

	public static synchronized boolean isDelayedTooltips() {
		return delayedTooltips;
	}

	public static synchronized void setDelayedTooltips(boolean delayedTooltips) {
		Configuration.delayedTooltips = delayedTooltips;
		onPropertyChange();
	}

	public static synchronized float getMipmapsPercent() {
		return mipmapsPercent;
	}

	public static synchronized void setMipmapsPercent(float mipmapsPercent) {
		Configuration.mipmapsPercent = mipmapsPercent;
		onPropertyChange();
	}

	public static synchronized boolean isAutomatePerformance() {
		return automatePerformance;
	}

	public static synchronized void setAutomatePerformance(boolean automatePerformance) {
		Configuration.automatePerformance = automatePerformance;
		onPropertyChange();
	}

	public static synchronized int getAutomateMode() {
		return automateMode;
	}

	public static synchronized void setAutomateMode(int automateMode) {
		Configuration.automateMode = automateMode;
		onPropertyChange();
	}

	public static synchronized boolean isClientLight() {
		return clientLight;
	}

	public static synchronized void setClientLight(boolean clientLight) {
		Configuration.clientLight = clientLight;
		onPropertyChange();
	}

	public static synchronized float getFlightSpeedFactor() {
		return flightSpeedFactor;
	}

	public static synchronized void setFlightSpeedFactor(float flightSpeedFactor) {
		Configuration.flightSpeedFactor = flightSpeedFactor;
		onPropertyChange();
	}

	public static synchronized boolean isReplaceTools() {
		return replaceTools;
	}

	public static synchronized void setReplaceTools(boolean replaceTools) {
		Configuration.replaceTools = replaceTools;
		onPropertyChange();
	}

	public static synchronized boolean isReplaceBlocks() {
		return replaceBlocks;
	}

	public static synchronized void setReplaceBlocks(boolean replaceBlocks) {
		Configuration.replaceBlocks = replaceBlocks;
		onPropertyChange();
	}

	public static synchronized boolean isHotbarQuickKeysEnabled() {
		return hotbarQuickKeysEnabled;
	}

	public static synchronized void setHotbarQuickKeysEnabled(boolean hotbarQuickKeysEnabled) {
		Configuration.hotbarQuickKeysEnabled = hotbarQuickKeysEnabled;
		onPropertyChange();
	}

	public static synchronized boolean isResizeScreenshots() {
		return resizeScreenshots;
	}

	public static synchronized void setResizeScreenshots(boolean resizeScreenshots) {
		Configuration.resizeScreenshots = resizeScreenshots;
		onPropertyChange();
	}

	public static synchronized int getResizedScreenshotWidth() {
		return resizedScreenshotWidth;
	}

	public static synchronized void setResizedScreenshotWidth(int resizedScreenshotWidth) {
		Configuration.resizedScreenshotWidth = resizedScreenshotWidth;
		onPropertyChange();
	}

	public static synchronized int getResizedScreenshotHeight() {
		return resizedScreenshotHeight;
	}

	public static synchronized void setResizedScreenshotHeight(int resizedScreenshotHeight) {
		Configuration.resizedScreenshotHeight = resizedScreenshotHeight;
		onPropertyChange();
	}

	public static synchronized float getChatOpacity() {
		return chatOpacity;
	}

	public static synchronized void setChatOpacity(float chatOpacity) {
		Configuration.chatOpacity = chatOpacity;
		onPropertyChange();
	}

	public static synchronized int getMainMenuState() {
		return mainMenuState;
	}

	public static synchronized void setMainMenuState(int mainMenuState) {
		Configuration.mainMenuState = mainMenuState;
		onPropertyChange();
	}

	public static synchronized boolean isConnectedTextures() {
		return connectedTextures;
	}

	public static synchronized void setConnectedTextures(boolean connectedTextures) {
		Configuration.connectedTextures = connectedTextures;
		onPropertyChange();
	}

	public static synchronized boolean isRandomMobTextures() {
		return randomMobTextures;
	}

	public static synchronized void setRandomMobTextures(boolean randomMobTextures) {
		Configuration.randomMobTextures = randomMobTextures;
		onPropertyChange();
	}

	public static synchronized boolean isAdvancedOptions() {
		return advancedOptions;
	}

	public static synchronized void setAdvancedOptions(boolean advancedOptions) {
		Configuration.advancedOptions = advancedOptions;
		onPropertyChange();
	}

	public static synchronized void setAmbientOcclusion(boolean ambientOcclusion) {
		Configuration.ambientOcclusion = ambientOcclusion;
		onPropertyChange();
	}

	public static synchronized boolean isAmbientOcclusion() {
		return ambientOcclusion;
	}

	public static synchronized boolean isServerTexturePromptsEnabled() {
		return serverTexturePromptsEnabled;
	}

	public static synchronized void setServerTexturePromptsEnabled(boolean serverTexturePromptsEnabled) {
		Configuration.serverTexturePromptsEnabled = serverTexturePromptsEnabled;
		onPropertyChange();
	}

	private static synchronized void onPropertyChange() {
		updateMCConfig();
		dirty = true;
	}

	public static void onTick() {
		if (dirty) {
			write();
		}
	}

}
