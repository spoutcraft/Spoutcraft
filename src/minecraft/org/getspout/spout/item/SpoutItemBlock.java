package org.getspout.spout.item;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class SpoutItemBlock extends ItemBlock {

	public SpoutItemBlock(int blockId) {
		super(blockId);
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int face) {
		return super.onItemUse(stack, player, world, x, y, z, face);
	}

}
