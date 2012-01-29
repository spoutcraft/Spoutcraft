package net.minecraft.src;

public class BlockCloth extends Block {
	public BlockCloth() {
		super(35, 64, Material.cloth);
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (j == 0) {
			return blockIndexInTexture;
		}
		else {
			j = ~(j & 0xf);
			return 113 + ((j & 8) >> 3) + (j & 7) * 16;
		}
	}

	protected int damageDropped(int i) {
		return i;
	}

	public static int getBlockFromDye(int i) {
		return ~i & 0xf;
	}

	public static int getDyeFromBlock(int i) {
		return ~i & 0xf;
	}
}
