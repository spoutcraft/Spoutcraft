package com.prupe.mcpatcher;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.prupe.mcpatcher.Config$ModEntry;
import com.prupe.mcpatcher.Config$ProfileEntry;
import com.prupe.mcpatcher.Config$VersionEntry;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

public class Config {
	private static Config instance = new Config();
	private static File jsonFile;
	private static boolean readOnly;
	public static final String MCPATCHER_PROPERTIES = "mcpatcher.properties";
	public static final String MCPATCHER_JSON = "mcpatcher.json";
	public static final String LAUNCHER_JSON = "launcher_profiles.json";
	public static final String VERSIONS_JSON = "versions.json";
	static final String TAG_MINECRAFT_VERSION = "minecraftVersion";
	static final String TAG_PATCHER_VERSION = "patcherVersion";
	static final String TAG_PRE_PATCH_STATE = "prePatchState";
	static final String TAG_MODIFIED_CLASSES = "modifiedClasses";
	static final String TAG_ADDED_CLASSES = "addedClasses";
	static final String VAL_BUILTIN = "builtIn";
	static final String VAL_EXTERNAL_ZIP = "externalZip";
	static final String VAL_EXTERNAL_JAR = "externalJar";
	private static final String TAG_SELECTED_PROFILE = "selectedProfile";
	public static final String MCPATCHER_PROFILE_NAME = "MCPatcher";
	private static final int VAL_FORMAT_CURRENT = 1;
	private static final int VAL_FORMAT_MIN = 1;
	private static final int VAL_FORMAT_MAX = 1;
	transient String selectedProfile = "MCPatcher";
	int format = 1;
	String patcherVersion;
	boolean betaWarningShown;
	boolean selectPatchedProfile = true;
	boolean fetchRemoteVersionList = true;
	boolean extraProfiling;
	String lastModDirectory;
	LinkedHashMap<String, String> logging = new LinkedHashMap();
	LinkedHashMap<String, Config$ProfileEntry> profiles = new LinkedHashMap();

	static boolean load(File minecraftDir, boolean isGame) {
		jsonFile = new File(minecraftDir, "mcpatcher.json");
		instance = (Config)JsonUtils.parseJson(jsonFile, Config.class);

		if (instance != null && instance.format > 0) {
			if (instance.format < 1) {
				instance.format = 1;
				save();
			} else if (instance.format > 1) {
				setReadOnly(true);
			}
		} else {
			instance = new Config();

			if (isGame) {
				// Spout Removed
				//System.out.printf("WARNING: configuration file %s not found, using defaults\n", new Object[] {jsonFile});
			}

			save();
		}
		// Spout Nullified because we don't use profiles.
		//String profile = getSelectedLauncherProfile(minecraftDir);
		String profile = null;

		if (MCPatcherUtils.isNullOrEmpty(profile)) {
			if (isGame) {
				// Spout Removed
				//System.out.printf("WARNING: could not determine selected profile, defaulting to %s\n", new Object[] {"MCPatcher"});
			}

			profile = "MCPatcher";
		} else if (!instance.profiles.containsKey(profile) && isGame) {
			// Spout Removed
			//System.out.printf("WARNING: selected profile \'%s\' not found, using defaults\n", new Object[] {profile});
		}

		instance.selectedProfile = profile;
		return true;
	}

	static boolean save() {
		boolean success = false;

		if (jsonFile != null && !readOnly) {
			JsonUtils.writeJson((Object)instance, jsonFile);
		}

		return success;
	}

	private static String getSelectedLauncherProfile(File minecraftDir) {
		File path = new File(minecraftDir, "launcher_profiles.json");
		JsonObject json = JsonUtils.parseJson(path);

		if (json != null) {
			JsonElement element = json.get("selectedProfile");

			if (element != null && element.isJsonPrimitive()) {
				return element.getAsString();
			}
		}

		return null;
	}

	public static Config getInstance() {
		return instance;
	}

	public static void setReadOnly(boolean readOnly) {
		readOnly = readOnly;
	}

	static Level getLogLevel(String category) {
		Level level = Level.INFO;
		String value = (String)instance.logging.get(category);

		if (value != null) {
			try {
				level = Level.parse(value.trim().toUpperCase());
			} catch (Throwable var4) {
				;
			}
		}

		setLogLevel(category, level);
		return level;
	}

	static void setLogLevel(String category, Level level) {
		instance.logging.put(category, level.toString().toUpperCase());
	}

	public static String getString(String mod, String tag, Object defaultValue) {
		LinkedHashMap modConfig = instance.getModConfig(mod);
		String value = (String)modConfig.get(tag);

		if (value == null) {
			modConfig.put(tag, defaultValue.toString());
			return defaultValue.toString();
		} else {
			return value;
		}
	}

	public static int getInt(String mod, String tag, int defaultValue) {
		int value;

		try {
			value = Integer.parseInt(getString(mod, tag, Integer.valueOf(defaultValue)));
		} catch (NumberFormatException var5) {
			value = defaultValue;
		}

		return value;
	}

	public static boolean getBoolean(String mod, String tag, boolean defaultValue) {
		String value = getString(mod, tag, Boolean.valueOf(defaultValue)).toLowerCase();
		return value.equals("false") ? false : (value.equals("true") ? true : defaultValue);
	}

	public static void set(String mod, String tag, Object value) {
		if (value == null) {
			remove(mod, tag);
		} else {
			instance.getModConfig(mod).put(tag, value.toString());
		}
	}

	public static void remove(String mod, String tag) {
		instance.getModConfig(mod).remove(tag);
	}

	String getSelectedProfileName() {
		if (MCPatcherUtils.isNullOrEmpty(this.selectedProfile)) {
			this.selectedProfile = "MCPatcher";
		}

		return this.selectedProfile;
	}

	Config$ProfileEntry getSelectedProfile() {
		Config$ProfileEntry profile = (Config$ProfileEntry)this.profiles.get(this.getSelectedProfileName());

		if (profile == null) {
			profile = new Config$ProfileEntry();
			this.profiles.put(this.selectedProfile, profile);
		}

		return profile;
	}

	Config$VersionEntry getSelectedVersion() {
		Config$ProfileEntry profile = this.getSelectedProfile();
		Config$VersionEntry version = (Config$VersionEntry)profile.versions.get(profile.version);

		if (version == null) {
			version = new Config$VersionEntry();
			profile.versions.put(profile.version, version);
		}

		return version;
	}

	Config$ModEntry getModEntry(String mod) {
		return (Config$ModEntry)this.getSelectedVersion().mods.get(mod);
	}

	Collection<Config$ModEntry> getModEntries() {
		return this.getSelectedVersion().mods.values();
	}

	private LinkedHashMap<String, String> getModConfig(String mod) {
		return Config$ProfileEntry.access$000(this.getSelectedProfile(), mod);
	}

	void removeMod(String mod) {
		this.getSelectedProfile().config.remove(mod);
		this.getSelectedVersion().mods.remove(mod);
	}

	void removeProfile(String name) {
		if (!name.equals(this.selectedProfile)) {
			this.profiles.remove(name);
		}
	}

	void removeVersion(String name) {
		if (!name.equals(this.getSelectedProfile().version)) {
			this.getSelectedProfile().versions.remove(name);
		}
	}

	Map<String, String> getPatchedVersionMap() {
		HashMap map = new HashMap();
		Iterator i$ = this.profiles.values().iterator();

		while (i$.hasNext()) {
			Config$ProfileEntry profile = (Config$ProfileEntry)i$.next();
			profile.versions.remove((Object)null);
			profile.versions.remove("");
			Iterator i$1 = profile.versions.entrySet().iterator();

			while (i$1.hasNext()) {
				Entry entry = (Entry)i$1.next();
				String patchedVersion = (String)entry.getKey();
				String unpatchedVersion = ((Config$VersionEntry)entry.getValue()).original;
				map.put(patchedVersion, unpatchedVersion);
			}
		}

		return map;
	}
}
