package net.minecraft.src;

public class ItemAxe extends ItemTool {
	private static Block blocksEffectiveAgainst[];

	protected ItemAxe(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, 3, enumtoolmaterial, blocksEffectiveAgainst);
	}

	public float getStrVsBlock(ItemStack itemstack, Block block) {
		if (block != null && block.blockMaterial == Material.wood) {
			return efficiencyOnProperMaterial;
		}
		else {
			return super.getStrVsBlock(itemstack, block);
		}
	}

	static {
		blocksEffectiveAgainst = (new Block[] {
		            Block.planks, Block.bookShelf, Block.wood, Block.chest, Block.stairDouble, Block.stairSingle, Block.pumpkin, Block.pumpkinLantern
		        });
	}
}
