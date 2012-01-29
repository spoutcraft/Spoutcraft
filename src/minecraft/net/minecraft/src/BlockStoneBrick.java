package net.minecraft.src;

public class BlockStoneBrick extends Block {
	public BlockStoneBrick(int i) {
		super(i, 54, Material.rock);
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		switch (j) {
			default:
				return 54;

			case 1:
				return 100;

			case 2:
				return 101;
		}
	}

	protected int damageDropped(int i) {
		return i;
	}
}
