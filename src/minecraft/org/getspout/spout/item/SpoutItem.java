package org.getspout.spout.item;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

import org.lwjgl.opengl.GL11;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.block.design.BlockDesign;
import org.spoutcraft.spoutcraftapi.block.design.GenericBlockDesign;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.util.MutableIntegerVector;

public class SpoutItem extends Item {

	public SpoutItem(int blockId) {
		super(blockId);
		this.setHasSubtypes(true);
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int face) {
		if (stack.itemID == MaterialData.flint.getRawId()) {
			int damage = stack.getItemDamage();
			if (damage >= 1024) {
				CustomBlock block = MaterialData.getCustomBlock(damage);
				//This is an item with no block component, return success
				if (block == null){
					return true;
				}
				if (onBlockItemUse(block, player, world, x, y, z, face)) {
					return true;
				}
				return false;
			}
		}
		return super.onItemUse(stack, player, world, x, y, z, face);
	}

	// From super class
	public boolean onBlockItemUse(CustomBlock block, EntityPlayer player, World world, int x, int y, int z, int side) {
		if (world.getBlockId(x, y, z) == Block.snow.blockID) {
			side = 0;
		} else {
			if (side == 0) {
				--y;
			}

			if (side == 1) {
				++y;
			}

			if (side == 2) {
				--z;
			}

			if (side == 3) {
				++z;
			}

			if (side == 4) {
				--x;
			}

			if (side == 5) {
				++x;
			}
		}
		
		int id = block.getBlockId();
		if (world.canBlockBePlacedAt(id, x, y, z, false, side)) {
			Block var8 = Block.blocksList[id];
			if (world.setBlockAndMetadataWithNotify(x, y, z, id, 0)) {
				Block.blocksList[id].onBlockPlaced(world, x, y, z, side);
				Block.blocksList[id].onBlockPlacedBy(world, x, y, z, player);
				
				Spoutcraft.getWorld().getChunkAt(x, y, z).setCustomBlockId(x, y, z, (short) block.getCustomId());
				
				world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), var8.stepSound.stepSoundDir2(),
						(var8.stepSound.getVolume() + 1.0F) / 2.0F, var8.stepSound.getPitch() * 0.8F);
			}

			return true;
		}
		return false;
	}
}