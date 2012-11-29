package net.minecraft.src;

import java.util.List;

import com.pclewis.mcpatcher.mod.Colorizer;  // Spout HD

public class ItemBlock extends Item {

	/** The block ID of the Block associated with this ItemBlock */
	private int blockID;

	public ItemBlock(int par1) {
		super(par1);
		this.blockID = par1 + 256;
		this.setIconIndex(Block.blocksList[par1 + 256].getBlockTextureFromSide(2));
	}

	/**
	 * Returns the blockID for this Item
	 */
	public int getBlockID() {
		return this.blockID;
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		int var11 = par3World.getBlockId(par4, par5, par6);

		if (var11 == Block.snow.blockID) {
			par7 = 1;
		} else if (var11 != Block.vine.blockID && var11 != Block.tallGrass.blockID && var11 != Block.deadBush.blockID) {
			if (par7 == 0) {
				--par5;
			}

			if (par7 == 1) {
				++par5;
			}

			if (par7 == 2) {
				--par6;
			}

			if (par7 == 3) {
				++par6;
			}

			if (par7 == 4) {
				--par4;
			}

			if (par7 == 5) {
				++par4;
			}
		}

		if (par1ItemStack.stackSize == 0) {
			return false;
		} else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
			return false;
		} else if (par5 == 255 && Block.blocksList[this.blockID].blockMaterial.isSolid()) {
			return false;
		} else if (par3World.canPlaceEntityOnSide(this.blockID, par4, par5, par6, false, par7, par2EntityPlayer)) {
			Block var12 = Block.blocksList[this.blockID];
			int var13 = this.getMetadata(par1ItemStack.getItemDamage());
			int var14 = Block.blocksList[this.blockID].func_85104_a(par3World, par4, par5, par6, par7, par8, par9, par10, var13);

			if (par3World.setBlockAndMetadataWithNotify(par4, par5, par6, this.blockID, var14)) {
				if (par3World.getBlockId(par4, par5, par6) == this.blockID) {
					Block.blocksList[this.blockID].onBlockPlacedBy(par3World, par4, par5, par6, par2EntityPlayer);
					Block.blocksList[this.blockID].func_85105_g(par3World, par4, par5, par6, var14);
				}

				par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), var12.stepSound.getPlaceSound(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
				--par1ItemStack.stackSize;
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if the given ItemBlock can be placed on the given side of the given block position.
	 */
	public boolean canPlaceItemBlockOnSide(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer, ItemStack par7ItemStack) {
		int var8 = par1World.getBlockId(par2, par3, par4);

		if (var8 == Block.snow.blockID) {
			par5 = 1;
		} else if (var8 != Block.vine.blockID && var8 != Block.tallGrass.blockID && var8 != Block.deadBush.blockID) {
			if (par5 == 0) {
				--par3;
			}

			if (par5 == 1) {
				++par3;
			}

			if (par5 == 2) {
				--par4;
			}

			if (par5 == 3) {
				++par4;
			}

			if (par5 == 4) {
				--par2;
			}

			if (par5 == 5) {
				++par2;
			}
		}

		return par1World.canPlaceEntityOnSide(this.getBlockID(), par2, par3, par4, false, par5, (Entity)null);
	}

	public String getItemNameIS(ItemStack par1ItemStack) {
		return Block.blocksList[this.blockID].getBlockName();
	}

	public String getItemName() {
		return Block.blocksList[this.blockID].getBlockName();
	}

	/**
	 * gets the CreativeTab this item is displayed on
	 */
	public CreativeTabs getCreativeTab() {
		return Block.blocksList[this.blockID].getCreativeTabToDisplayOn();
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		Block.blocksList[this.blockID].getSubBlocks(par1, par2CreativeTabs, par3List);
	}

// Spout Start
	//fix metadata for double slabs
	public int getMetadata(int damage) {
		if (blockID == Block.stoneDoubleSlab.blockID){
			return damage;
		}
		return super.getMetadata(damage);
	}
// Spout End
}
