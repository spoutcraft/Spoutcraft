package net.minecraft.src;

import java.util.List;

public class ItemRecord extends Item {
	public final String recordName;

	protected ItemRecord(int i, String s) {
		super(i);
		recordName = s;
		maxStackSize = 1;
	}

	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
		if (world.getBlockId(i, j, k) == Block.jukebox.blockID && world.getBlockMetadata(i, j, k) == 0) {
			if (world.multiplayerWorld) {
				return true;
			}
			else {
				((BlockJukeBox)Block.jukebox).ejectRecord(world, i, j, k, shiftedIndex);
				world.playAuxSFXAtEntity(null, 1005, i, j, k, shiftedIndex);
				itemstack.stackSize--;
				return true;
			}
		}
		else {
			return false;
		}
	}

	public void addInformation(ItemStack itemstack, List list) {
		list.add((new StringBuilder()).append("C418 - ").append(recordName).toString());
	}

	public EnumRarity getRarity(ItemStack itemstack) {
		return EnumRarity.rare;
	}
}
