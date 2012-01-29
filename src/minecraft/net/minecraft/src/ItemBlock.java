package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;  //Spout HD
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemBlock extends Item {

	private int blockID;

	public ItemBlock(int var1) {
		super(var1);
		this.blockID = var1 + 256;
		this.setIconIndex(Block.blocksList[var1 + 256].getBlockTextureFromSide(2));
	}

	public int getBlockID() {
		return this.blockID;
	}

	public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7) {
		int var8 = var3.getBlockId(var4, var5, var6);
		if (var8 == Block.snow.blockID) {
			var7 = 0;
		}
		else if (var8 != Block.vine.blockID) {
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
		}
		else if (!var2.canPlayerEdit(var4, var5, var6)) {
			return false;
		}
		else if (var5 == var3.worldHeight - 1 && Block.blocksList[this.blockID].blockMaterial.isSolid()) {
			return false;
		}
		else if (var3.canBlockBePlacedAt(this.blockID, var4, var5, var6, false, var7)) {
			Block var9 = Block.blocksList[this.blockID];
			if (var3.setBlockAndMetadataWithNotify(var4, var5, var6, this.blockID, this.getMetadata(var1.getItemDamage()))) {
				if (var3.getBlockId(var4, var5, var6) == this.blockID) {
					Block.blocksList[this.blockID].onBlockPlaced(var3, var4, var5, var6, var7);
					Block.blocksList[this.blockID].onBlockPlacedBy(var3, var4, var5, var6, var2);
				}

				var3.playSoundEffect((double)((float)var4 + 0.5F), (double)((float)var5 + 0.5F), (double)((float)var6 + 0.5F), var9.stepSound.stepSoundDir2(), (var9.stepSound.getVolume() + 1.0F) / 2.0F, var9.stepSound.getPitch() * 0.8F);
				--var1.stackSize;
			}

			return true;
		}
		else {
			return false;
		}
	}

	public String getItemNameIS(ItemStack var1) {
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
