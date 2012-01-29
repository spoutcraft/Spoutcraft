package net.minecraft.src;

import java.util.Random;

public class BlockGlass extends BlockBreakable {
	public BlockGlass(int i, int j, Material material, boolean flag) {
		super(i, j, material, flag);
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public int getRenderBlockPass() {
		return 0;
	}
}
