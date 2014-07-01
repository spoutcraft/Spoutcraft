package com.prupe.mcpatcher;

import java.util.Comparator;
import net.minecraft.src.ResourceLocation;

final class TexturePackAPI$1 implements Comparator<ResourceLocation> {
	final boolean val$sortByFilename;

	TexturePackAPI$1(boolean var1) {
		this.val$sortByFilename = var1;
	}

	public int compare(ResourceLocation o1, ResourceLocation o2) {
		String n1 = o1.getResourceDomain();
		String n2 = o2.getResourceDomain();
		int result = n1.compareTo(n2);

		if (result != 0) {
			return result;
		} else {
			String f1 = o1.getResourcePath();
			String f2 = o2.getResourcePath();

			if (this.val$sortByFilename) {
				f1 = f1.replaceAll(".*/", "").replaceFirst("\\.properties", "");
				f2 = f2.replaceAll(".*/", "").replaceFirst("\\.properties", "");
			}

			return f1.compareTo(f2);
		}
	}	
}
