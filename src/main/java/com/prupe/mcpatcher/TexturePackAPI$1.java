package com.prupe.mcpatcher;

import java.util.Comparator;
import net.minecraft.src.ResourceLocation;

final class TexturePackAPI$1 implements Comparator<ResourceLocation> {
	public int compare(ResourceLocation o1, ResourceLocation o2) {
		String f1 = o1.func_110623_a().replaceAll(".*/", "").replaceFirst("\\.properties", "");
		String f2 = o2.func_110623_a().replaceAll(".*/", "").replaceFirst("\\.properties", "");
		int result = f1.compareTo(f2);
		return result != 0 ? result : o1.func_110623_a().compareTo(o2.func_110623_a());
	}
}
