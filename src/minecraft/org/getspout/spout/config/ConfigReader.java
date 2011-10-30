package org.getspout.spout.config;

import java.io.File;
import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.FileUtil;

public class ConfigReader {
	public static boolean clipboardAccess = false;
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
	public static boolean mipmaps = false;
	
	public static void read() {
		System.out.println("Reading Configuration");
		File config = new File(FileUtil.getSpoutcraftDirectory(), "spoutcraft.properties");
		try {
			if (!config.exists()) {
				config.createNewFile();
			}
			SettingsHandler settings = new SettingsHandler(config);
			Field[] fields = ConfigReader.class.getDeclaredFields();
			for (Field f : fields) {
				Object value = f.get(null);
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
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Minecraft.theMinecraft.gameSettings.anaglyph = ConfigReader.anaglyph3D;
		Minecraft.theMinecraft.gameSettings.renderDistance = ConfigReader.renderDistance;
		Minecraft.theMinecraft.gameSettings.fancyGraphics = ConfigReader.fancyGraphics;
		Minecraft.theMinecraft.gameSettings.advancedOpengl = ConfigReader.advancedOpenGL !=0;
		Minecraft.theMinecraft.gameSettings.guiScale = ConfigReader.guiScale;
		Minecraft.theMinecraft.gameSettings.limitFramerate = ConfigReader.performance;
		Minecraft.theMinecraft.gameSettings.viewBobbing = ConfigReader.viewBobbing;
		Minecraft.theMinecraft.gameSettings.gammaSetting = ConfigReader.brightnessSlider;
		
		System.out.println("Finished Reading Configuration");
	}
	
	public static void write() {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		File config = new File(FileUtil.getSpoutcraftDirectory(), "spoutcraft.properties");
		try {
			if (!config.exists()) {
				config.createNewFile();
			}
			SettingsHandler settings = new SettingsHandler(config);

			Field[] fields = ConfigReader.class.getDeclaredFields();
			for (Field f : fields) {
				Object value = f.get(null);
				if (settings.checkProperty(f.getName())) {
					settings.changeProperty(f.getName(), value);
				}
				else {
					settings.put(f.getName(), value);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (wasSandboxed) {
			SpoutClient.enableSandbox();
		}
	}
	
	public static boolean isHasClipboardAccess() {
		return clipboardAccess;
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
	
	public static void restoreDefaults() {
		clipboardAccess = false;
		advancedOpenGL = 0;
		anaglyph3D = false;
		autosave = 0;
		betterGrass = 0;
		fancyBiomeColors = false;
		brightnessSlider = 1F;
		chunkUpdates = 1;
		clearWater = false;
		dynamicUpdates = true;
		fancyClouds = false;
		fancyFog = false;
		fancyGraphics = false;
		fancyGrass = false;
		fancyTrees = false;
		fancyWater = false;
		fancyWeather = false;
		farView = false;
		fastDebug = 0;
		guiScale = 0;
		performance = 0;
		preloadedChunks = 0;
		renderDistance = 0;
		signDistance = 16;
		sky = true;
		smoothFPS = false;
		smoothLighting = 1F;
		stars = true;
		time = 0;
		viewBobbing = false;
		voidFog = true;
		weather = true;
		delayedTooltips = false;
		write();
	}

}
