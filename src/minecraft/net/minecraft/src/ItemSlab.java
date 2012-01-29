package net.minecraft.src;

public class ItemSlab extends ItemBlock {
	public ItemSlab(int i) {
		super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	public int getIconFromDamage(int i) {
		return Block.stairSingle.getBlockTextureFromSideAndMetadata(2, i);
	}

	public int getMetadata(int i) {
		return i;
	}

	public String getItemNameIS(ItemStack itemstack) {
		int i = itemstack.getItemDamage();
		if (i < 0 || i >= BlockStep.blockStepTypes.length) {
			i = 0;
		}
		return (new StringBuilder()).append(super.getItemName()).append(".").append(BlockStep.blockStepTypes[i]).toString();
	}

	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
		if (l == 1);
		if (itemstack.stackSize == 0) {
			return false;
		}
		if (!entityplayer.canPlayerEdit(i, j, k)) {
			return false;
		}
		int i1 = world.getBlockId(i, j, k);
		int j1 = world.getBlockMetadata(i, j, k);
		if (l == 1 && i1 == Block.stairSingle.blockID && j1 == itemstack.getItemDamage()) {
			if (world.setBlockAndMetadataWithNotify(i, j, k, Block.stairDouble.blockID, j1)) {
				world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, Block.stairDouble.stepSound.stepSoundDir2(), (Block.stairDouble.stepSound.getVolume() + 1.0F) / 2.0F, Block.stairDouble.stepSound.getPitch() * 0.8F);
				itemstack.stackSize--;
			}
			return true;
		}
		else {
			return super.onItemUse(itemstack, entityplayer, world, i, j, k, l);
		}
	}
}
