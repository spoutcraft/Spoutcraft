package org.getspout.spout.item;

import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

import org.lwjgl.opengl.GL11;
import org.spoutcraft.spoutcraftapi.block.design.GenericBlockDesign;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.util.MutableIntegerVector;

public class SpoutItem extends Item {

	private final static TIntIntHashMap itemBlock = new TIntIntHashMap();
	private final static TIntIntHashMap itemMetaData = new TIntIntHashMap();
	
	private static MutableIntegerVector mutableIntVector = new MutableIntegerVector(0, 0, 0);
	private final static HashMap<MutableIntegerVector, Integer> blockIdOverride = new HashMap<MutableIntegerVector, Integer>();
	private final static HashMap<MutableIntegerVector, Integer> blockMetaDataOverride = new HashMap<MutableIntegerVector, Integer>();

	public SpoutItem(int blockId) {
		super(blockId);
		this.setHasSubtypes(true);
	}

	public static void addItemInfoMap(int damage, Integer blockId, Short metaData) {
		if (blockId == null || metaData == null) {
			itemBlock.remove(damage);
			itemMetaData.remove(damage);
		} else {
			itemBlock.put(damage, blockId);
			itemMetaData.put(damage, metaData);
		}
	}

	public static void wipeMap() {
		itemBlock.clear();
		itemMetaData.clear();
		blockIdOverride.clear();
		blockMetaDataOverride.clear();
	}
	
	
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int face) {
		if (stack.itemID == MaterialData.flint.getRawId()) {
			int damage = stack.getItemDamage();
			if (damage >= 1024) {
				int blockId = itemBlock.get(damage);
				if (blockId == 0){
					return true;
				}
				short metaData = (short) itemMetaData.get(damage);
				if (metaData == 0) {
					//return true;
				}
				boolean result = onBlockItemUse(blockId, metaData, stack, player, world, x, y, z, face);
				return result;
			}
		}
		return super.onItemUse(stack, player, world, x, y, z, face);
	}

	// From super class
	public boolean onBlockItemUse(int blockID, short metaData, ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7) {
		if (var3.getBlockId(var4, var5, var6) == Block.snow.blockID) {
			var7 = 0;
		} else {
			if (var7 == 0) {
				--var5;
			}

			if (var7 == 1) {
				++var5;
			}

			if (var7 == 2) {
				--var6;
			}

			if (var7 == 3) {
				++var6;
			}

			if (var7 == 4) {
				--var4;
			}

			if (var7 == 5) {
				++var4;
			}
		}

		if (var1.stackSize == 0) {
			return false;
		} else if (var5 == 127 && Block.blocksList[blockID].blockMaterial.isSolid()) {
			return false;
		} else if (var3.canBlockBePlacedAt(blockID, var4, var5, var6, false, var7)) {
			Block var8 = Block.blocksList[blockID];
			if (var3.setBlockAndMetadataWithNotify(var4, var5, var6, blockID, metaData)) {
				Block.blocksList[blockID].onBlockPlaced(var3, var4, var5, var6, var7);
				Block.blocksList[blockID].onBlockPlacedBy(var3, var4, var5, var6, var2);
				
				if (var1.itemID == MaterialData.flint.getRawId() && var1.getItemDamage() != 0) {
					CustomBlock block = MaterialData.getCustomBlock(var1.getItemDamage());
					overrideBlock(var4, var5, var6, block.getCustomId(),0);
				}
				
				var3.playSoundEffect((double) ((float) var4 + 0.5F), (double) ((float) var5 + 0.5F), (double) ((float) var6 + 0.5F), var8.stepSound.stepSoundDir2(),
						(var8.stepSound.getVolume() + 1.0F) / 2.0F, var8.stepSound.getPitch() * 0.8F);
				--var1.stackSize;
			}

			return true;
		} else {
			return false;
		}
	}

	public static void overrideBlock(int x, int y, int z, Integer blockId, Integer metaData) {
		mutableIntVector.setIntX(x);
		mutableIntVector.setIntY(y);
		mutableIntVector.setIntZ(z);
		if (blockId == null || metaData == null) {
			blockIdOverride.remove(mutableIntVector);
			blockMetaDataOverride.remove(mutableIntVector);
		} else {
			MutableIntegerVector old = mutableIntVector;
			mutableIntVector = new MutableIntegerVector(0, 0, 0);
			blockIdOverride.put(old, blockId);
			blockMetaDataOverride.put(old, metaData);
		}
		 Minecraft.theMinecraft.theWorld.markBlockNeedsUpdate(x, y, z);
	}
	
	public static boolean renderCustomBlock(RenderBlocks renderBlocks, GenericBlockDesign design, Block block, int x, int y, int z) {
		
		if (design == null) {
			return false;
		}
		
		Tessellator tessallator = Tessellator.instance;

		block.setBlockBounds(design.getLowXBound(), design.getLowYBound(), design.getLowZBound(), design.getHighXBound(), design.getHighYBound(), design.getHighZBound());
		
		return draw(design, tessallator, block, renderBlocks, x, y, z);
	}
	
	public static void renderBlockOnInventory(GenericBlockDesign design, RenderBlocks renders, float brightness) {
		Tessellator tessellator = Tessellator.instance;
		
		GL11.glColor4f(1, 1, 1, 1.0F);
		
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		
		draw(design, tessellator, null, renders, brightness, 0, 0, 0);
		
		tessellator.draw();
	}
	
	public static boolean draw(GenericBlockDesign design, Tessellator tessallator, Block block, RenderBlocks renders, int x, int y, int z) {
		return draw(design, tessallator, block, renders, 1, x, y, z);
	}
	
	public static boolean draw(GenericBlockDesign design, Tessellator tessallator, Block block, RenderBlocks renders, float inventoryBrightness, int x, int y, int z) {
		
		if (block != null) {
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
		
		design.setBrightness(inventoryBrightness);
		
		for (int i = 0; i < design.getX().length; i++) {
			
			MutableIntegerVector sourceBlock = design.getLightSource(i, x, y, z);
			
			float baseBrightness;
			
			if (block != null) {
				baseBrightness = block.getBlockBrightness(renders.blockAccess, sourceBlock.getBlockX(), sourceBlock.getBlockY(), sourceBlock.getBlockZ());
			} else {
				baseBrightness = design.getBrightness();
			}

			float brightness = baseBrightness * design.getMaxBrightness() + (1 - baseBrightness) * design.getMinBrightness();
			
			tessallator.setColorOpaque_F(1.0F * brightness, 1.0F * brightness, 1.0F * brightness);
			
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
	
	public static int getKey(int x, int y, int z) {
		mutableIntVector.setIntX(x);
		mutableIntVector.setIntY(y);
		mutableIntVector.setIntZ(z);
		
		Integer blockId = blockIdOverride.get(mutableIntVector);
		Integer metaData = blockMetaDataOverride.get(mutableIntVector);
		
		if (metaData == null) {
			metaData = 0;
		}
		
		return getKey(blockId, metaData);
	}
	
	private static int getKey(int blockId, int metaData) {
		return blockId + (metaData << 24);
	}
	
	public static boolean isBlockOverride(int x, int y, int z) {
		mutableIntVector.setIntX(x);
		mutableIntVector.setIntY(y);
		mutableIntVector.setIntZ(z);
		return blockIdOverride.containsKey(mutableIntVector);
	}
	
	public static org.spoutcraft.spoutcraftapi.material.Block getBlockOverride(int x, int y, int z) {
		mutableIntVector.setIntX(x);
		mutableIntVector.setIntY(y);
		mutableIntVector.setIntZ(z);
		Integer blockId = blockIdOverride.get(mutableIntVector);
		
		return MaterialData.getBlock(MaterialData.flint.getRawId(), blockId.shortValue());
	}
	
	public static void reset() {
		itemBlock.clear();
		itemMetaData.clear();
		blockIdOverride.clear();
	}
	
}
