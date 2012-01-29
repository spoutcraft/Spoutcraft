package net.minecraft.src;

public class ItemSpade extends ItemTool {
	private static Block blocksEffectiveAgainst[];

	public ItemSpade(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, 1, enumtoolmaterial, blocksEffectiveAgainst);
	}

	public boolean canHarvestBlock(Block block) {
		if (block == Block.snow) {
			return true;
		}
		return block == Block.blockSnow;
	}

	static {
		blocksEffectiveAgainst = (new Block[] {
		            Block.grass, Block.dirt, Block.sand, Block.gravel, Block.snow, Block.blockSnow, Block.blockClay, Block.tilledField, Block.slowSand, Block.mycelium
		        });
	}
}
