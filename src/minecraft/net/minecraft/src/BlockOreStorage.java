package net.minecraft.src;

public class BlockOreStorage extends Block {
	public BlockOreStorage(int i, int j) {
		super(i, Material.iron);
		blockIndexInTexture = j;
	}

	public int getBlockTextureFromSide(int i) {
		return blockIndexInTexture;
	}
}
