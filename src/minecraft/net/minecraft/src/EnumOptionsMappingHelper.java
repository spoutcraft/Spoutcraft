package net.minecraft.src;

class EnumOptionsMappingHelper {
	static final int enumOptionsMappingHelperArray[];

	static {
		enumOptionsMappingHelperArray = new int[EnumOptions.values().length];
		try {
			enumOptionsMappingHelperArray[EnumOptions.INVERT_MOUSE.ordinal()] = 1;
		}
		catch (NoSuchFieldError nosuchfielderror) { }
		try {
			enumOptionsMappingHelperArray[EnumOptions.VIEW_BOBBING.ordinal()] = 2;
		}
		catch (NoSuchFieldError nosuchfielderror1) { }
		try {
			enumOptionsMappingHelperArray[EnumOptions.ANAGLYPH.ordinal()] = 3;
		}
		catch (NoSuchFieldError nosuchfielderror2) { }
		try {
			enumOptionsMappingHelperArray[EnumOptions.ADVANCED_OPENGL.ordinal()] = 4;
		}
		catch (NoSuchFieldError nosuchfielderror3) { }
		try {
			enumOptionsMappingHelperArray[EnumOptions.AMBIENT_OCCLUSION.ordinal()] = 5;
		}
		catch (NoSuchFieldError nosuchfielderror4) { }
		try {
			enumOptionsMappingHelperArray[EnumOptions.RENDER_CLOUDS.ordinal()] = 6;
		}
		catch (NoSuchFieldError nosuchfielderror5) { }
	}
}
