package net.minecraft.src;

public class ItemReed extends Item {
	private int spawnID;

	public ItemReed(int i, Block block) {
		super(i);
		spawnID = block.blockID;
	}

	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
		int i1 = world.getBlockId(i, j, k);
		if (i1 == Block.snow.blockID) {
			l = 0;
		}
		else if (i1 != Block.vine.blockID) {
			if (l == 0) {
				j--;
			}
			if (l == 1) {
				j++;
			}
			if (l == 2) {
				k--;
			}
			if (l == 3) {
				k++;
			}
			if (l == 4) {
				i--;
			}
			if (l == 5) {
				i++;
			}
		}
		if (!entityplayer.canPlayerEdit(i, j, k)) {
			return false;
		}
		if (itemstack.stackSize == 0) {
			return false;
		}
		if (world.canBlockBePlacedAt(spawnID, i, j, k, false, l)) {
			Block block = Block.blocksList[spawnID];
			if (world.setBlockWithNotify(i, j, k, spawnID)) {
				if (world.getBlockId(i, j, k) == spawnID) {
					Block.blocksList[spawnID].onBlockPlaced(world, i, j, k, l);
					Block.blocksList[spawnID].onBlockPlacedBy(world, i, j, k, entityplayer);
				}
				world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, block.stepSound.stepSoundDir2(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
				itemstack.stackSize--;
			}
		}
		return true;
	}
}
