package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.TexturePackChangeHandler;

final class Colorizer$1 extends TexturePackChangeHandler {
	Colorizer$1(String var1, int var2) {
		super(var1, var2);
	}

	public void beforeChange() {
		Colorizer.access$000();
	}

	public void afterChange() {
		Colorizer.access$100();
		ColorizeBlock.reloadColorMaps(Colorizer.access$200());

		if (Colorizer.useFogColors) {
			ColorizeWorld.reloadFogColors(Colorizer.access$200());
		}

		if (Colorizer.usePotionColors) {
			ColorizeItem.reloadPotionColors(Colorizer.access$200());
		}

		if (Colorizer.useSwampColors) {
			ColorizeBlock.reloadSwampColors(Colorizer.access$200());
		}

		if (Colorizer.useBlockColors) {
			ColorizeBlock.reloadBlockColors(Colorizer.access$200());
		}

		if (Colorizer.useParticleColors) {
			ColorizeEntity.reloadParticleColors(Colorizer.access$200());
		}

		if (Colorizer.useRedstoneColors) {
			ColorizeBlock.reloadRedstoneColors(Colorizer.access$200());
		}

		if (Colorizer.useStemColors) {
			ColorizeBlock.reloadStemColors(Colorizer.access$200());
		}

		if (Colorizer.useCloudType) {
			ColorizeWorld.reloadCloudType(Colorizer.access$200());
		}

		if (Colorizer.useMapColors) {
			ColorizeItem.reloadMapColors(Colorizer.access$200());
		}

		if (Colorizer.useDyeColors) {
			ColorizeEntity.reloadDyeColors(Colorizer.access$200());
		}

		if (Colorizer.useTextColors) {
			ColorizeWorld.reloadTextColors(Colorizer.access$200());
		}

		if (Colorizer.useXPOrbColors) {
			ColorizeEntity.reloadXPOrbColors(Colorizer.access$200());
		}
	}
}
