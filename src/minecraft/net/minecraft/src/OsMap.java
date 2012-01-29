package net.minecraft.src;

class OsMap {
	static final int field_1193_a[];

	static {
		field_1193_a = new int[EnumOS1.values().length];
		try {
			field_1193_a[EnumOS1.linux.ordinal()] = 1;
		}
		catch (NoSuchFieldError nosuchfielderror) { }
		try {
			field_1193_a[EnumOS1.solaris.ordinal()] = 2;
		}
		catch (NoSuchFieldError nosuchfielderror1) { }
		try {
			field_1193_a[EnumOS1.windows.ordinal()] = 3;
		}
		catch (NoSuchFieldError nosuchfielderror2) { }
		try {
			field_1193_a[EnumOS1.macos.ordinal()] = 4;
		}
		catch (NoSuchFieldError nosuchfielderror3) { }
	}
}
