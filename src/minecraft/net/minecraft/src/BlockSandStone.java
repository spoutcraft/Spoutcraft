package net.minecraft.src;

public class BlockSandStone extends Block {
	public BlockSandStone(int i) {
		super(i, 192, Material.rock);
	}

	public int getBlockTextureFromSide(int i) {
		if (i == 1) {
			return blockIndexInTexture - 16;
		}
		if (i == 0) {
			return blockIndexInTexture + 16;
		}
		else {
			return blockIndexInTexture;
		}
	}
}
