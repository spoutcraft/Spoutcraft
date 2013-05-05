package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.TileLoader;
import com.prupe.mcpatcher.mod.CITUtils$1;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Icon;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Tessellator;

public class CITUtils {
	static final int MAX_ITEMS = Item.itemsList.length;
	static final int MAX_ENCHANTMENTS = 256;
	static int LOWEST_ITEM_ID;
	static int HIGHEST_ITEM_ID;
	static final Map itemNameMap = new HashMap();
	private static final int ITEM_ID_POTION = 373;
	private static final boolean enableItems = Config.getBoolean("Custom Item Textures", "items", true);
	private static final boolean enableOverlays = Config.getBoolean("Custom Item Textures", "overlays", true);
	private static final boolean enableArmor = Config.getBoolean("Custom Item Textures", "armor", true);
	static TileLoader tileLoader;
	private static final ItemOverride[][] items = new ItemOverride[MAX_ITEMS][];
	private static final ItemOverride[][] overlays = new ItemOverride[MAX_ITEMS][];
	private static final ItemOverride[][] armors = new ItemOverride[MAX_ITEMS][];
	private static ItemOverlayList armorMatches;
	private static int armorMatchIndex;
	private static Icon lastIcon;

	public static void init() {}

	public static Icon getIcon(Icon var0, Item var1, ItemStack var2) {
		lastIcon = var0;

		if (enableItems) {
			ItemOverride var3 = findMatch(items, var2);

			if (var3 != null) {
				lastIcon = var3.icon;
			}
		}

		return lastIcon;
	}

	public static String getArmorTexture(String var0, EntityLiving var1, ItemStack var2) {
		if (enableArmor) {
			int var3 = var0.endsWith("_b.png") ? 1 : 0;
			ItemOverride var4 = findMatch(armors, var2, var3);

			if (var4 != null) {
				return var4.textureName;
			}
		}

		return var0;
	}

	private static ItemOverride findMatch(ItemOverride[][] var0, ItemStack var1) {
		int var2 = var1.itemID;

		if (var2 >= 0 && var2 < var0.length && var0[var2] != null) {
			int[] var3 = getEnchantmentLevels(var1.stackTagCompound);
			ItemOverride[] var4 = var0[var2];
			int var5 = var4.length;

			for (int var6 = 0; var6 < var5; ++var6) {
				ItemOverride var7 = var4[var6];

				if (var7.match(var2, var1, var3)) {
					return var7;
				}
			}
		}

		return null;
	}

	private static ItemOverride findMatch(ItemOverride[][] var0, ItemStack var1, int var2) {
		int var3 = var1.itemID;

		if (var3 >= 0 && var3 < var0.length && var0[var3] != null) {
			int[] var4 = getEnchantmentLevels(var1.stackTagCompound);
			ItemOverride[] var5 = var0[var3];
			int var6 = var5.length;

			for (int var7 = 0; var7 < var6; ++var7) {
				ItemOverride var8 = var5[var7];

				if (var8.armorLayer == var2 && var8.match(var3, var1, var4)) {
					return var8;
				}
			}
		}

		return null;
	}

	public static boolean renderOverlayHeld(ItemStack var0) {
		if (enableOverlays && var0 != null) {
			ItemOverlayList var1 = new ItemOverlayList(overlays, var0);

			if (var1.isEmpty()) {
				return false;
			} else {
				int var2;
				int var3;

				if (lastIcon == null) {
					var3 = 256;
					var2 = 256;
				} else {
					var2 = Math.round((float)lastIcon.getSheetWidth() * (lastIcon.getMaxU() - lastIcon.getMinU()));
					var3 = Math.round((float)lastIcon.getSheetHeight() * (lastIcon.getMaxV() - lastIcon.getMinV()));
				}

				ItemOverlay.beginOuter3D();

				for (int var4 = 0; var4 < var1.size(); ++var4) {
					var1.getOverlay(var4).render3D(Tessellator.instance, var1.getIntensity(var4), var2, var3);
				}

				ItemOverlay.endOuter3D();
				return true;
			}
		} else {
			return false;
		}
	}

	public static boolean renderOverlayDropped(ItemStack var0) {
		return renderOverlayHeld(var0);
	}

	public static boolean renderOverlayGUI(ItemStack var0, int var1, int var2, float var3) {
		if (enableOverlays && var0 != null) {
			ItemOverlayList var4 = new ItemOverlayList(overlays, var0);

			if (var4.isEmpty()) {
				return false;
			} else {
				ItemOverlay.beginOuter2D();

				for (int var5 = 0; var5 < var4.size(); ++var5) {
					var4.getOverlay(var5).render2D(Tessellator.instance, var4.getIntensity(var5), (float)(var1 - 2), (float)(var2 - 2), (float)(var1 + 18), (float)(var2 + 18), var3 - 50.0F);
				}

				ItemOverlay.endOuter2D();
				return true;
			}
		} else {
			return false;
		}
	}

	public static boolean setupArmorOverlays(EntityLiving var0, int var1) {
		if (enableOverlays && var0 != null) {
			ItemStack var2 = null;

			if (var0 instanceof EntityLiving) {
				var2 = ((EntityLiving)var0).getCurrentArmor(3 - var1);
			} else if (var0 instanceof EntityPlayer) {
				var2 = ((EntityPlayer)var0).getCurrentArmor(3 - var1);
			}

			if (var2 == null) {
				return false;
			} else {
				armorMatches = new ItemOverlayList(overlays, var2);
				armorMatchIndex = 0;
				return !armorMatches.isEmpty();
			}
		} else {
			return false;
		}
	}

	public static boolean preRenderArmorOverlay() {
		if (armorMatchIndex < armorMatches.size()) {
			ItemOverlay var0 = armorMatches.getOverlay(armorMatchIndex);
			var0.beginArmor(armorMatches.getIntensity(armorMatchIndex));
			return true;
		} else {
			armorMatches = null;
			armorMatchIndex = 0;
			return false;
		}
	}

	public static void postRenderArmorOverlay() {
		armorMatches.getOverlay(armorMatchIndex).endArmor();
		++armorMatchIndex;
	}

	static String getItemName(int var0) {
		if (var0 >= 0 && var0 < Item.itemsList.length) {
			Item var1 = Item.itemsList[var0];

			if (var1 != null) {
				String var2 = var1.getUnlocalizedName();

				if (var2 != null) {
					return var2;
				}
			}
		}

		return "unknown item " + var0;
	}

	static int[] getEnchantmentLevels(NBTTagCompound var0) {
		int[] var1 = null;

		if (var0 != null) {
			NBTBase var2 = var0.getTag("ench");

			if (var2 == null) {
				var2 = var0.getTag("StoredEnchantments");
			}

			if (var2 instanceof NBTTagList) {
				NBTTagList var3 = (NBTTagList)var2;

				for (int var4 = 0; var4 < var3.tagCount(); ++var4) {
					var2 = var3.tagAt(var4);

					if (var2 instanceof NBTTagCompound) {
						short var5 = ((NBTTagCompound)var2).getShort("id");
						short var6 = ((NBTTagCompound)var2).getShort("lvl");

						if (var5 >= 0 && var5 < 256 && var6 > 0) {
							if (var1 == null) {
								var1 = new int[256];
							}

							var1[var5] += var6;
						}
					}
				}
			}
		}

		return var1;
	}

	static ItemOverride[][] access$100() {
		return items;
	}

	static ItemOverride[][] access$200() {
		return overlays;
	}

	static ItemOverride[][] access$300() {
		return armors;
	}

	static Icon access$402(Icon var0) {
		lastIcon = var0;
		return var0;
	}

	static boolean access$500() {
		return enableItems;
	}

	static boolean access$600() {
		return enableOverlays;
	}

	static boolean access$700() {
		return enableArmor;
	}

	static {
		TexturePackChangeHandler.register(new CITUtils$1("Custom Item Textures", 3));
	}
}
