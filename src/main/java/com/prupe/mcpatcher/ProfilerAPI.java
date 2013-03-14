package com.prupe.mcpatcher;

public class ProfilerAPI {
	private static final boolean enable = Config.getBoolean("extraProfiling", false);

	public static void startSection(String var0) {
		if (enable) {
			MCPatcherUtils.getMinecraft().mcProfiler.startSection(var0);
		}
	}

	public static void endStartSection(String var0) {
		if (enable) {
			MCPatcherUtils.getMinecraft().mcProfiler.endStartSection(var0);
		}
	}

	public static void endSection() {
		if (enable) {
			MCPatcherUtils.getMinecraft().mcProfiler.endSection();
		}
	}
}
