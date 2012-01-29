package net.minecraft.src;

public enum EnumSkyBlock {
	Sky("Sky", 0, 15),
	Block("Block", 1, 0);

	public final int defaultLightValue;
	private static final EnumSkyBlock allSkyBlocks[] = (new EnumSkyBlock[] {
		Sky, Block
	});

	private EnumSkyBlock(String s, int i, int j) {
		defaultLightValue = j;
	}
}
