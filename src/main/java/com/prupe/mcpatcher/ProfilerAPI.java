package com.prupe.mcpatcher;

import net.minecraft.src.Minecraft;

public class ProfilerAPI {
	private static final boolean enable = Config.getInstance().extraProfiling;

	public static void startSection(String name) {
		if (enable) {
			Minecraft.getMinecraft().mcProfiler.startSection(name);
		}
	}

	public static void endStartSection(String name) {
		if (enable) {
			Minecraft.getMinecraft().mcProfiler.endStartSection(name);
		}
	}

	public static void endSection() {
		if (enable) {
			Minecraft.getMinecraft().mcProfiler.endSection();
		}
	}
}
