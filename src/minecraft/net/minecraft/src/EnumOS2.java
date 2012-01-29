package net.minecraft.src;

public enum EnumOS2 {
	linux("linux", 0),
	solaris("solaris", 1),
	windows("windows", 2),
	macos("macos", 3),
	unknown("unknown", 4);

	private static final EnumOS2 allOSes[] = (new EnumOS2[] {
		linux, solaris, windows, macos, unknown
	});

	private EnumOS2(String s, int i) {
	}
}
