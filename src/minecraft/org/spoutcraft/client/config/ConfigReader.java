/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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
import java.util.List;

import net.minecraft.client.Minecraft;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.FileUtil;

import com.pclewis.mcpatcher.mod.Shaders;

public class ConfigReader {
	//Client settings
	public static int advancedOpenGL = 0;
	public static boolean anaglyph3D = false;
	public static int autosave = 0;
	public static int betterGrass = 0;
	public static boolean fancyBiomeColors = false;
	public static boolean waterBiomeColors = true;
	public static float brightnessSlider = 1F;
	public static boolean clearWater = false;
	public static boolean fancyClouds = false;
	public static boolean fancyFog = false;
	public static boolean fancyGraphics = false;
	public static boolean fancyGrass = false;
	public static boolean fancyTrees = false;
	public static boolean fancyWater = false;
	public static boolean fancyWeather = false;
	public static boolean farView = false;
	public static boolean fancyLight = false;
	public static boolean fancyParticles = false;
	public static int shaderType = 0;
	public static int fastDebug = 0;
	public static int guiScale = 0;
	public static int performance = 0;
	public static int chunkRenderPasses = 2;
	public static int renderDistance = 0;
	public static int signDistance = 16;
	public static boolean sky = true;
	public static boolean smoothFPS = false;
	public static float smoothLighting = 1F;
	public static boolean stars = true;
	public static int time = 0;
	public static boolean viewBobbing = false;
	public static boolean voidFog = true;
	public static boolean weather = true;
	public static boolean delayedTooltips = false;
	public static float mipmapsPercent = 0F;
	public static boolean automatePerformance = true;
	public static int automateMode = 0;
	public static boolean showChatColors = true;
	public static boolean showJoinMessages = true;
	public static boolean showDamageAlerts = true;
	public static boolean highlightMentions = true;
	public static boolean chatGrabsMouse = true;
	public static boolean ignorePeople = false;
	public static boolean chatUsesRegex;
	public static boolean sendColorsAsUnicode = true;
	public static boolean clientLight = false;
	public static float flightSpeedMultiplier = 2.0F;
	public static boolean askBeforeOpeningUrl = true;
	public static boolean replaceTools = false;
	public static boolean replaceBlocks = false;
	public static boolean hotbarQuickKeysEnabled = true;
	
	//Launcher settings
	public static boolean fastLogin = false;
	public static boolean clipboardaccess = false;
	public static int memory = 1;
	public static int custombuild = -1;
	public static boolean recupdate = true;
	public static boolean devupdate = false;
	public static boolean worldbackup = false;
	public static boolean retryLogins = true;
	public static boolean latestLWJGL = false;
	public static boolean acceptUpdates = true;

	public transient static Object[] settings = null;
	public transient static String[] unknown = null;

	public static void read() {
		File config = new File(FileUtil.getSpoutcraftDirectory(), "spoutcraft.properties");
		try {
			if (!config.exists()) {
				config.createNewFile();
			}
			SettingsHandler settings = new SettingsHandler(config);
			Field[] fields = ConfigReader.class.getDeclaredFields();

			ConfigReader.settings = new Object[fields.length];

			for (int i = 0; i < fields.length; i++) {
				Field f = fields[i];
				if (Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers())) {

					Object value = f.get(null);

					ConfigReader.settings[i] = value;
					if (value instanceof Boolean) {
						f.set(null, getOrSetBooleanProperty(settings, f.getName(), (Boolean)value));
					} else if (value instanceof Integer) {
						f.set(null, getOrSetIntegerProperty(settings, f.getName(), (Integer)value));
					} else if (value instanceof Float) {
						f.set(null, getOrSetFloatProperty(settings, f.getName(), (Float)value));
					} else if (value instanceof String) {
						f.set(null, getOrSetStringProperty(settings, f.getName(), (String) value));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		updateMCConfig();
	}

	public static void write() {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		Runnable write = new Runnable() {
			public void run() {
				File config = new File(FileUtil.getSpoutcraftDirectory(), "spoutcraft.properties");
				try {
					if (!config.exists()) {
						config.createNewFile();
					}
					SettingsHandler settings = new SettingsHandler(config);
					settings.load();
					Field[] fields = ConfigReader.class.getDeclaredFields();
					for (Field f : fields) {
						if (Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers())) {
							Object value = f.get(null);
							if (settings.checkProperty(f.getName())) {
								settings.changeProperty(f.getName(), value);
							} else {
								settings.put(f.getName(), value);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		(new Thread(write)).start();
		if (wasSandboxed) {
			SpoutClient.enableSandbox();
		}
	}

	public static boolean isHasClipboardAccess() {
		return clipboardaccess;
	}

	private static boolean getOrSetBooleanProperty(SettingsHandler settings, String property, boolean defaultValue) {
		if (settings.checkProperty(property)) {
			return settings.getPropertyBoolean(property);
		}
		settings.put(property, defaultValue);
		return defaultValue;
	}

	private static int getOrSetIntegerProperty(SettingsHandler settings, String property, int defaultValue) {
		if (settings.checkProperty(property)) {
			return settings.getPropertyInteger(property);
		}
		settings.put(property, defaultValue);
		return defaultValue;
	}

	private static float getOrSetFloatProperty(SettingsHandler settings, String property, float defaultValue) {
		if (settings.checkProperty(property)) {
			return settings.getPropertyDouble(property).floatValue();
		}
		settings.put(property, defaultValue);
		return defaultValue;
	}
	
	private static String getOrSetStringProperty(SettingsHandler settings, String property, String defaultValue) {
		if (settings.checkProperty(property)) {
			return settings.getPropertyString(property);
		}
		settings.put(property, defaultValue);
		return defaultValue;
	}

	private static void updateMCConfig() {
		Minecraft.theMinecraft.gameSettings.anaglyph = ConfigReader.anaglyph3D;
		Minecraft.theMinecraft.gameSettings.renderDistance = ConfigReader.renderDistance;
		Minecraft.theMinecraft.gameSettings.fancyGraphics = ConfigReader.fancyGraphics;
		Minecraft.theMinecraft.gameSettings.advancedOpengl = ConfigReader.advancedOpenGL !=0;
		Minecraft.theMinecraft.gameSettings.guiScale = ConfigReader.guiScale;
		Minecraft.theMinecraft.gameSettings.viewBobbing = ConfigReader.viewBobbing;
		Minecraft.theMinecraft.gameSettings.gammaSetting = ConfigReader.brightnessSlider;
		
		Minecraft.theMinecraft.gameSettings.limitFramerate = ConfigReader.performance;
		org.lwjgl.opengl.Display.setVSyncEnabled(ConfigReader.performance == 3);
		
		if (!Shaders.isOpenGL2()) {
			shaderType = 0;
		}
		Shaders.setMode(shaderType);
		
		if (ConfigReader.signDistance < 8) {
			ConfigReader.signDistance = 8;
		}
		else if (ConfigReader.signDistance >= 128 && ConfigReader.signDistance != Integer.MAX_VALUE) {
			ConfigReader.signDistance = Integer.MAX_VALUE;
		}
	}

	public static void restoreDefaults() {
		if (ConfigReader.settings != null) {
			Field[] fields = ConfigReader.class.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field f = fields[i];
				Object value = ConfigReader.settings[i];
				if (value != null && f != null) {
					try {
						f.set(null, value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			write();
		}
		updateMCConfig();
	}
}
