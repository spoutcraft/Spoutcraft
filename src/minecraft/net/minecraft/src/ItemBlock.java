package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;  //Spout HD

public class ItemBlock extends Item {
	private int blockID;

	public ItemBlock(int par1) {
		super(par1);
		this.blockID = par1 + 256;
		this.setIconIndex(Block.blocksList[par1 + 256].getBlockTextureFromSide(2));
	}

	public int getBlockID() {
		return this.blockID;
	}

	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7) {
		int var8 = par3World.getBlockId(par4, par5, par6);
		if (var8 == Block.snow.blockID) {
			par7 = 1;
		} else if (var8 != Block.vine.blockID && var8 != Block.tallGrass.blockID && var8 != Block.deadBush.blockID) {
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
		} else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6)) {
			return false;
		} else if (par5 == 255 && Block.blocksList[this.blockID].blockMaterial.isSolid()) {
			return false;
		} else if (par3World.canBlockBePlacedAt(this.blockID, par4, par5, par6, false, par7)) {
			Block var9 = Block.blocksList[this.blockID];
			if (par3World.setBlockAndMetadataWithNotify(par4, par5, par6, this.blockID, this.getMetadata(par1ItemStack.getItemDamage()))) {
				if (par3World.getBlockId(par4, par5, par6) == this.blockID) {
					Block.blocksList[this.blockID].onBlockPlaced(par3World, par4, par5, par6, par7);
					Block.blocksList[this.blockID].onBlockPlacedBy(par3World, par4, par5, par6, par2EntityPlayer);
				}

				par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), var9.stepSound.getStepSound(), (var9.stepSound.getVolume() + 1.0F) / 2.0F, var9.stepSound.getPitch() * 0.8F);
				--par1ItemStack.stackSize;
			}

			return true;
		} else {
			return false;
		}
	}

	public String getItemNameIS(ItemStack par1ItemStack) {
		return Block.blocksList[this.blockID].getBlockName();
	}

	public String getItemName() {
		return Block.blocksList[this.blockID].getBlockName();
	}

 //Spout HD start
	public int getColorFromDamage(int var1, int var2) {
		return Colorizer.getItemColorFromDamage(super.getColorFromDamage(var1, var2), this.blockID, var1);
	}
 //Spout HD end
}
