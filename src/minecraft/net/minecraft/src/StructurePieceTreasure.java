package net.minecraft.src;

public class StructurePieceTreasure extends WeightedRandomChoice {
	public int itemID;
	public int itemMetadata;
	public int minItemStack;
	public int maxItemStack;

	public StructurePieceTreasure(int i, int j, int k, int l, int i1) {
		super(i1);
		itemID = i;
		itemMetadata = j;
		minItemStack = k;
		maxItemStack = l;
	}
}
