package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;  //Spout HD
import net.minecraft.src.Entity;
import net.minecraft.src.EntityEggInfo;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Facing;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StatCollector;
import net.minecraft.src.World;

public class ItemMonsterPlacer extends Item {

	public ItemMonsterPlacer(int var1) {
		super(var1);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}

	public String getItemDisplayName(ItemStack var1) {
		String var2 = ("" + StatCollector.translateToLocal(this.getItemName() + ".name")).trim();
		String var3 = EntityList.func_44040_a(var1.getItemDamage());
		if (var3 != null) {
			var2 = var2 + " " + StatCollector.translateToLocal("entity." + var3 + ".name");
		}

		return var2;
	}

	public int getColorFromDamage(int var1, int var2) {
		EntityEggInfo var3 = (EntityEggInfo)EntityList.field_44041_a.get(Integer.valueOf(var1));
		return var3 != null ? (var2 == 0 ? Colorizer.colorizeSpawnerEgg(var3.field_46061_b, var1, var2) : Colorizer.colorizeSpawnerEgg(var3.field_46062_c, var1, var2)) : Colorizer.colorizeSpawnerEgg(16777215, var1, var2); //Spout HD
	}

	public boolean func_46058_c() {
		return true;
	}

	public int func_46057_a(int var1, int var2) {
		return var2 > 0 ? super.func_46057_a(var1, var2) + 16 : super.func_46057_a(var1, var2);
	}

	public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7) {
		if (var3.multiplayerWorld) {
			return true;
		}
		else {
			var4 += Facing.offsetsXForSide[var7];
			var5 += Facing.offsetsYForSide[var7];
			var6 += Facing.offsetsZForSide[var7];
			Entity var8 = EntityList.createEntity(var1.getItemDamage(), var3);
			if (var8 != null) {
				if (!var2.capabilities.depleteBuckets) {
					--var1.stackSize;
				}

				var8.setLocationAndAngles((double)var4 + 0.5D, (double)var5, (double)var6 + 0.5D, 0.0F, 0.0F);
				var3.spawnEntityInWorld(var8);
			}

			return true;
		}
	}
}
