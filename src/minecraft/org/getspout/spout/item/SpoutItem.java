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

	public static boolean renderCustomBlock(RenderBlocks renderBlocks, GenericBlockDesign design, Block block, int x, int y, int z) {

		if (design == null) {
			return false;
		}

		Tessellator tessallator = Tessellator.instance;

		block.setBlockBounds(design.getLowXBound(), design.getLowYBound(), design.getLowZBound(), design.getHighXBound(), design.getHighYBound(), design.getHighZBound());

		return draw(design, tessallator, block, renderBlocks, x, y, z);
	}

	public static void renderBlockOnInventory(BlockDesign design, RenderBlocks renders, float brightness) {
		Tessellator tessellator = Tessellator.instance;

		GL11.glColor4f(1, 1, 1, 1.0F);

		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);

		draw((GenericBlockDesign) design, tessellator, null, renders, brightness, 0, 0, 0);

		tessellator.draw();
	}

	public static boolean draw(BlockDesign design, Tessellator tessallator, Block block, RenderBlocks renders, int x, int y, int z) {
		return draw((GenericBlockDesign) design, tessallator, block, renders, 1, x, y, z);
	}

	public static boolean draw(GenericBlockDesign design, Tessellator tessallator, Block block, RenderBlocks renders, float inventoryBrightness, int x, int y, int z) {
		//int color = 16777215; 
		if (block != null) {
			//color = block.colorMultiplier(renders.blockAccess, x, y, z);
			IBlockAccess access = renders.blockAccess;
			boolean enclosed = true;
			enclosed &= access.isBlockOpaqueCube(x, y + 1, z);
			enclosed &= access.isBlockOpaqueCube(x, y - 1, z);
			enclosed &= access.isBlockOpaqueCube(x, y, z + 1);
			enclosed &= access.isBlockOpaqueCube(x, y, z - 1);
			enclosed &= access.isBlockOpaqueCube(x + 1, y, z);
			enclosed &= access.isBlockOpaqueCube(x - 1, y, z);
			if (enclosed) {
				return false;
			}
		}
		
		if (design.getX() == null) {
			return false;
		}

		design.setBrightness(inventoryBrightness);
		
		int internalLightLevel = 0;
		if (block == null) {
			internalLightLevel = 0x00F000F0;
		} else {
			internalLightLevel = block.getMixedBrightnessForBlock(renders.blockAccess, x, y, z);
		}

		for (int i = 0; i < design.getX().length; i++) {

			MutableIntegerVector sourceBlock = design.getLightSource(i, x, y, z);

			int sideBrightness;

			if (block != null && sourceBlock != null) {
				sideBrightness = block.getMixedBrightnessForBlock(renders.blockAccess, sourceBlock.getBlockX(), sourceBlock.getBlockY(), sourceBlock.getBlockZ());
			} else {
				sideBrightness = internalLightLevel;
			}

			tessallator.setBrightness(sideBrightness);
			
			tessallator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

			float[] xx = design.getX()[i];
			float[] yy = design.getY()[i];
			float[] zz = design.getZ()[i];
			float[] tx = design.getTextureXPos()[i];
			float[] ty = design.getTextureYPos()[i];

			for (int j = 0; j < 4; j++) {
				tessallator.addVertexWithUV(x + xx[j], y + yy[j], z + zz[j], tx[j], ty[j]);
			}
		}
		return true;

	}
}