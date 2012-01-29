package net.minecraft.src;

public class ItemShears extends Item {
	public ItemShears(int i) {
		super(i);
		setMaxStackSize(1);
		setMaxDamage(238);
	}

	public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving) {
		if (i == Block.leaves.blockID || i == Block.web.blockID || i == Block.tallGrass.blockID || i == Block.vine.blockID) {
			itemstack.damageItem(1, entityliving);
			return true;
		}
		else {
			return super.onBlockDestroyed(itemstack, i, j, k, l, entityliving);
		}
	}

	public boolean canHarvestBlock(Block block) {
		return block.blockID == Block.web.blockID;
	}

	public float getStrVsBlock(ItemStack itemstack, Block block) {
		if (block.blockID == Block.web.blockID || block.blockID == Block.leaves.blockID) {
			return 15F;
		}
		if (block.blockID == Block.cloth.blockID) {
			return 5F;
		}
		else {
			return super.getStrVsBlock(itemstack, block);
		}
	}
}
