package net.minecraft.src;


public enum EnumOptions {
	//Spout Start
	MUSIC("MUSIC", 0, "MUSIC", 0, "options.music", true, false),
	SOUND("SOUND", 1, "SOUND", 1, "options.sound", true, false),
	INVERT_MOUSE("INVERT_MOUSE", 2, "INVERT_MOUSE", 2, "options.invertMouse", false, true),
	SENSITIVITY("SENSITIVITY", 3, "SENSITIVITY", 3, "options.sensitivity", true, false),
	FOV("FOV", 4, "FOV", 4, "options.fov", true, false),
	GAMMA("GAMMA", 5, "GAMMA", 5, "options.gamma", true, false),
	RENDER_DISTANCE("RENDER_DISTANCE", 6, "RENDER_DISTANCE", 6, "options.renderDistance", false, false),
	VIEW_BOBBING("VIEW_BOBBING", 7, "VIEW_BOBBING", 7, "options.viewBobbing", false, true),
	ANAGLYPH("ANAGLYPH", 8, "ANAGLYPH", 8, "options.anaglyph", false, true),
	ADVANCED_OPENGL("ADVANCED_OPENGL", 9, "ADVANCED_OPENGL", 9, "options.advancedOpengl", false, true),
	FRAMERATE_LIMIT("FRAMERATE_LIMIT", 10, "FRAMERATE_LIMIT", 10, "options.framerateLimit", false, false),
	DIFFICULTY("DIFFICULTY", 11, "DIFFICULTY", 11, "options.difficulty", false, false),
	GRAPHICS("GRAPHICS", 12, "GRAPHICS", 12, "options.graphics", false, false),
	AMBIENT_OCCLUSION("AMBIENT_OCCLUSION", 13, "AMBIENT_OCCLUSION", 13, "options.ao", false, true),
	GUI_SCALE("GUI_SCALE", 14, "GUI_SCALE", 14, "options.guiScale", false, false),
	FOG_FANCY("FOG_FANCY", 15, "FOG_FANCY", 15, "Fog", false, false),
	FOG_START("FOG_START", 16, "FOG_START", 16, "Fog Start", false, false, true),
	//MIPMAP_LEVEL("MIPMAP_LEVEL", 17, "MIPMAP_LEVEL", 17, "Mipmap Level", false, false),
	//MIPMAP_TYPE("MIPMAP_TYPE", 16, "MIPMAP_TYPE", 16, "Mipmap Type", false, false),
	LOAD_FAR("LOAD_FAR", 17, "LOAD_FAR", 18, "Load Far", false, false),
	PRELOADED_CHUNKS("PRELOADED_CHUNKS", 18, "PRELOADED_CHUNKS", 19, "Preloaded Chunks", false, false),
	SMOOTH_FPS("SMOOTH_FPS", 19, "SMOOTH_FPS", 20, "Smooth FPS", false, false),
	BRIGHTNESS("BRIGHTNESS", 20, "BRIGHTNESS", 21, "Brightness", true, false, true),
	CLOUDS("CLOUDS", 21, "CLOUDS", 22, "Clouds", false, false),
	CLOUD_HEIGHT("CLOUD_HEIGHT", 22, "CLOUD_HEIGHT", 23, "Cloud Height", true, false, true),
	TREES("TREES", 23, "TREES", 24, "Trees", false, false),
	GRASS("GRASS", 24, "GRASS", 25, "Grass", false, false),
	RAIN("RAIN", 25, "RAIN", 27, "Rain & Snow", false, false),
	WATER("WATER", 26, "RAIN", 28, "Water", false, false),
	ANIMATED_WATER("ANIMATED_WATER", 27, "ANIMATED_WATER", 29, "Water Animated", false, false),
	ANIMATED_LAVA("ANIMATED_LAVA", 28, "ANIMATED_LAVA", 30, "Lava Animated", false, false),
	ANIMATED_FIRE("ANIMATED_FIRE", 29, "ANIMATED_FLAMES", 31, "Fire Animated", false, false),
	ANIMATED_PORTAL("ANIMATED_PORTAL", 30, "ANIMATED_PORTAL", 32, "Portal Animated", false, false),
	AO_LEVEL("AO_LEVEL", 31, "AO_LEVEL", 33, "Smooth Lighting", true, false),
	FAST_DEBUG_INFO("FAST_DEBUG_INFO", 32, "FAST_DEBUG_INFO", 34, "Fast Debug Info", false, false),
	AUTOSAVE_TICKS("AUTOSAVE_TICKS", 33, "AUTOSAVE_TICKS", 35, "Autosave", false, false),
	BETTER_GRASS("BETTER_GRASS", 34, "BETTER_GRASS", 36, "Better Grass", false, false),
	ANIMATED_REDSTONE("ANIMATED_REDSTONE", 35, "ANIMATED_REDSTONE", 37, "Redstone Animated", false, false),
	ANIMATED_EXPLOSION("ANIMATED_EXPLOSION", 36, "ANIMATED_EXPLOSION", 38, "Explosion Animated", false, false),
	ANIMATED_FLAME("ANIMATED_FLAME", 37, "ANIMATED_FLAME", 39, "Flame Animated", false, false),
	ANIMATED_SMOKE("ANIMATED_SMOKE", 38, "ANIMATED_SMOKE", 40, "Smoke Animated", false, false),
	WEATHER("WEATHER", 39, "WEATHER", 41, "Weather", false, false, true),
	SKY("SKY", 40, "SKY", 42, "Sky", false, false, true),
	STARS("STARS", 41, "STARS", 43, "Stars", false, false, true),
	FAR_VIEW("FAR_VIEW", 42, "FAR_VIEW", 44, "Far View", false, false),
	CHUNK_UPDATES("CHUNK_UPDATES", 43, "CHUNK_UPDATES", 45, "Chunk Updates", false, false),
	CHUNK_UPDATES_DYNAMIC("CHUNK_UPDATES_DYNAMIC", 44, "CHUNK_UPDATES_DYNAMIC", 46, "Dynamic Updates", false, false),
	TIME("TIME", 45, "TIME", 47, "Time", false, false, true),
	CLEAR_WATER("CLEAR_WATER", 46, "CLEAR_WATER", 48, "Clear Water", false, false, true);
	//Spout End
	private final boolean enumFloat;
	private final boolean enumBoolean;
	private final String enumString;
	//Spout Start
	private final boolean cheating;
	//Spout End

	public static EnumOptions getEnumOptions(int var0) {
		EnumOptions[] var1 = values();
		int var2 = var1.length;

		for(int var3 = 0; var3 < var2; ++var3) {
			EnumOptions var4 = var1[var3];
			if(var4.returnEnumOrdinal() == var0) {
				return var4;
			}
		}

		return null;
	}

	//Spout Start
	private EnumOptions(String var1, int var2, String var3, int var4, String var5, boolean var6, boolean var7) {
		this.enumString = var5;
		this.enumFloat = var6;
		this.enumBoolean = var7;
		this.cheating = false;
	}
	private EnumOptions(String var1, int var2, String var3, int var4, String var5, boolean var6, boolean var7, boolean cheating) {
		this.enumString = var5;
		this.enumFloat = var6;
		this.enumBoolean = var7;
		this.cheating = cheating;
	}
	
	public boolean isVisualCheating() {
		return cheating;
	}
	//Spout End

	public boolean getEnumFloat() {
		return this.enumFloat;
	}

	public boolean getEnumBoolean() {
		return this.enumBoolean;
	}

	public int returnEnumOrdinal() {
		return this.ordinal();
	}

	public String getEnumString() {
		return this.enumString;
	}

}
