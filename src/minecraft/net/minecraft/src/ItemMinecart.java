package net.minecraft.src;

public class ItemMinecart extends Item {
	public int minecartType;

	public ItemMinecart(int i, int j) {
		super(i);
		maxStackSize = 1;
		minecartType = j;
	}

	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
		int i1 = world.getBlockId(i, j, k);
		if (BlockRail.isRailBlock(i1)) {
			if (!world.multiplayerWorld) {
				world.spawnEntityInWorld(new EntityMinecart(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, minecartType));
			}
			itemstack.stackSize--;
			return true;
		}
		else {
			return false;
		}
	}
}
