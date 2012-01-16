package org.spoutcraft.client.config;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.client.Minecraft;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.FileUtil;

public class ConfigReader {
	public static boolean clipboardaccess = false;
	public static boolean fastLogin = false;
	public static int advancedOpenGL = 0;
	public static boolean anaglyph3D = false;
	public static int autosave = 0;
	public static int betterGrass = 0;
	public static boolean fancyBiomeColors = false;
	public static boolean waterBiomeColors = true;
	public static float brightnessSlider = 1F;
	public static int chunkUpdates = 1;
	public static boolean clearWater = false;
	public static boolean dynamicUpdates = true;
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
	public static int fastDebug = 0;
	public static int guiScale = 0;
	public static int performance = 0;
	public static int preloadedChunks = 0;
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

	public transient static Object[] settings = null;
	
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
					}
					else if (value instanceof Integer) {
						f.set(null, getOrSetIntegerProperty(settings, f.getName(), (Integer)value));
					}
					else if (value instanceof Float) {
						f.set(null, getOrSetFloatProperty(settings, f.getName(), (Float)value));
					}
				}
			}
		}
		catch (Exception e) {
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

					Field[] fields = ConfigReader.class.getDeclaredFields();
					for (Field f : fields) {
						if (Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers())) {
							Object value = f.get(null);
							if (settings.checkProperty(f.getName())) {
								settings.changeProperty(f.getName(), value);
							}
							else {
								settings.put(f.getName(), value);
							}
						}
					}
				}
				catch (Exception e) {
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
	
	private static void updateMCConfig() {
		Minecraft.theMinecraft.gameSettings.anaglyph = ConfigReader.anaglyph3D;
		Minecraft.theMinecraft.gameSettings.renderDistance = ConfigReader.renderDistance;
		Minecraft.theMinecraft.gameSettings.fancyGraphics = ConfigReader.fancyGraphics;
		Minecraft.theMinecraft.gameSettings.advancedOpengl = ConfigReader.advancedOpenGL !=0;
		Minecraft.theMinecraft.gameSettings.guiScale = ConfigReader.guiScale;
		Minecraft.theMinecraft.gameSettings.limitFramerate = ConfigReader.performance;
		Minecraft.theMinecraft.gameSettings.viewBobbing = ConfigReader.viewBobbing;
		Minecraft.theMinecraft.gameSettings.gammaSetting = ConfigReader.brightnessSlider;
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
