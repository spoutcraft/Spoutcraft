package com.prupe.mcpatcher;

public class ProfilerAPI {
	private static final boolean enable = Config.getBoolean("extraProfiling", false);

	public static void startSection(String name) {
		if (enable) {
			MCPatcherUtils.getMinecraft().mcProfiler.startSection(name);
		}
	}

	public static void endStartSection(String name) {
		if (enable) {
			MCPatcherUtils.getMinecraft().mcProfiler.endStartSection(name);
		}
	}

	public static void endSection() {
		if (enable) {
			MCPatcherUtils.getMinecraft().mcProfiler.endSection();
		}
	}
}
