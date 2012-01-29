package net.minecraft.src;

enum EnumOS1 {
	linux("linux", 0),
	solaris("solaris", 1),
	windows("windows", 2),
	macos("macos", 3),
	unknown("unknown", 4);

	private static final EnumOS1 field_6525_f[] = (new EnumOS1[] {
		linux, solaris, windows, macos, unknown
	});

	private EnumOS1(String s, int i) {
	}
}
