package com.pclewis.mcpatcher.mod;

import java.io.File;
import java.io.FilenameFilter;

final class TextureUtils$1 implements FilenameFilter {
	public boolean accept(File var1, String var2) {
		return var2.endsWith(".properties") && !TextureUtils.isCustomTerrainItemResource("/anim/" + var2);
	}
}
