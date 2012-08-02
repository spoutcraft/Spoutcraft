package net.minecraft.src;

import java.util.List;

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

	public boolean func_77648_a(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
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
		} else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6)) {
			return false;
		} else if (par5 == 255 && Block.blocksList[this.blockID].blockMaterial.isSolid()) {
			return false;
		} else if (par3World.func_72931_a(this.blockID, par4, par5, par6, false, par7, par2EntityPlayer)) {
			Block var12 = Block.blocksList[this.blockID];
			if (par3World.setBlockAndMetadataWithNotify(par4, par5, par6, this.blockID, this.getMetadata(par1ItemStack.getItemDamage()))) {
				if (par3World.getBlockId(par4, par5, par6) == this.blockID) {
					Block.blocksList[this.blockID].func_71909_a(par3World, par4, par5, par6, par7, par8, par9, par10);
					Block.blocksList[this.blockID].onBlockPlacedBy(par3World, par4, par5, par6, par2EntityPlayer);
				}

				par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), var12.stepSound.getStepSound(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
				--par1ItemStack.stackSize;
			}

			return true;
		} else {
			return false;
		}
	}

	public boolean func_77884_a(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer, ItemStack par7ItemStack) {
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

		return par1World.func_72931_a(this.getBlockID(), par2, par3, par4, false, par5, (Entity)null);
	}

	public String getItemNameIS(ItemStack par1ItemStack) {
		return Block.blocksList[this.blockID].getBlockName();
	}

	public String getItemName() {
		return Block.blocksList[this.blockID].getBlockName();
	}

	public CreativeTabs func_77640_w() {
		return Block.blocksList[this.blockID].func_71882_w();
	}

	public void func_77633_a(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		Block.blocksList[this.blockID].func_71879_a(par1, par2CreativeTabs, par3List);
	}
	
//Spout start
	//fix metadata for double slabs
	public int getMetadata(int damage) {
		if (blockID == Block.stairDouble.blockID){
			return damage;
		}
		return super.getMetadata(damage);
	}
//Spout end
 //Spout HD start
	public int getColorFromDamage(int var1, int var2) {
		return Colorizer.getItemColorFromDamage(super.getColorFromDamage(var1, var2), this.blockID, var1);
	}
 //Spout HD end
}
