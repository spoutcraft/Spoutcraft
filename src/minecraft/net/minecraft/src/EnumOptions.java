package net.minecraft.src;

public enum EnumOptions {
	MUSIC("MUSIC", 0, "options.music", true, false),
	SOUND("SOUND", 1, "options.sound", true, false),
	INVERT_MOUSE("INVERT_MOUSE", 2, "options.invertMouse", false, true),
	SENSITIVITY("SENSITIVITY", 3, "options.sensitivity", true, false),
	FOV("FOV", 4, "options.fov", true, false),
	GAMMA("GAMMA", 5, "options.gamma", true, false),
	RENDER_DISTANCE("RENDER_DISTANCE", 6, "options.renderDistance", false, false),
	VIEW_BOBBING("VIEW_BOBBING", 7, "options.viewBobbing", false, true),
	ANAGLYPH("ANAGLYPH", 8, "options.anaglyph", false, true),
	ADVANCED_OPENGL("ADVANCED_OPENGL", 9, "options.advancedOpengl", false, true),
	FRAMERATE_LIMIT("FRAMERATE_LIMIT", 10, "options.framerateLimit", false, false),
	DIFFICULTY("DIFFICULTY", 11, "options.difficulty", false, false),
	GRAPHICS("GRAPHICS", 12, "options.graphics", false, false),
	AMBIENT_OCCLUSION("AMBIENT_OCCLUSION", 13, "options.ao", false, true),
	GUI_SCALE("GUI_SCALE", 14, "options.guiScale", false, false),
	RENDER_CLOUDS("RENDER_CLOUDS", 15, "options.renderClouds", false, true),
	PARTICLES("PARTICLES", 16, "options.particles", false, false);

	private final boolean enumFloat;
	private final boolean enumBoolean;
	private final String enumString;
	private static final EnumOptions allOptions[] = (new EnumOptions[] {
		MUSIC, SOUND, INVERT_MOUSE, SENSITIVITY, FOV, GAMMA, RENDER_DISTANCE, VIEW_BOBBING, ANAGLYPH, ADVANCED_OPENGL,
		FRAMERATE_LIMIT, DIFFICULTY, GRAPHICS, AMBIENT_OCCLUSION, GUI_SCALE, RENDER_CLOUDS, PARTICLES
	});

	public static EnumOptions getEnumOptions(int i) {
		EnumOptions aenumoptions[] = values();
		int j = aenumoptions.length;
		for (int k = 0; k < j; k++) {
			EnumOptions enumoptions = aenumoptions[k];
			if (enumoptions.returnEnumOrdinal() == i) {
				return enumoptions;
			}
		}

		return null;
	}

	private EnumOptions(String s, int i, String s1, boolean flag, boolean flag1) {
		enumString = s1;
		enumFloat = flag;
		enumBoolean = flag1;
	}

	public boolean getEnumFloat() {
		return enumFloat;
	}

	public boolean getEnumBoolean() {
		return enumBoolean;
	}

	public int returnEnumOrdinal() {
		return ordinal();
	}

	public String getEnumString() {
		return enumString;
	}
}
