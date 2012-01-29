package net.minecraft.src;

public class BiomeGenPlains extends BiomeGenBase {
	protected BiomeGenPlains(int i) {
		super(i);
		biomeDecorator.treesPerChunk = -999;
		biomeDecorator.flowersPerChunk = 4;
		biomeDecorator.grassPerChunk = 10;
	}
}
