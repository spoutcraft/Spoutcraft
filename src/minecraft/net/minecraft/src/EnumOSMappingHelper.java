package net.minecraft.src;

public class EnumOSMappingHelper {
	public static final int enumOSMappingArray[];

	static {
		enumOSMappingArray = new int[EnumOS2.values().length];
		try {
			enumOSMappingArray[EnumOS2.linux.ordinal()] = 1;
		}
		catch (NoSuchFieldError nosuchfielderror) { }
		try {
			enumOSMappingArray[EnumOS2.solaris.ordinal()] = 2;
		}
		catch (NoSuchFieldError nosuchfielderror1) { }
		try {
			enumOSMappingArray[EnumOS2.windows.ordinal()] = 3;
		}
		catch (NoSuchFieldError nosuchfielderror2) { }
		try {
			enumOSMappingArray[EnumOS2.macos.ordinal()] = 4;
		}
		catch (NoSuchFieldError nosuchfielderror3) { }
	}
}
