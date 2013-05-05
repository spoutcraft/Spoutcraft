package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TileLoader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import net.minecraft.src.Icon;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagByte;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagDouble;
import net.minecraft.src.NBTTagFloat;
import net.minecraft.src.NBTTagInt;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagLong;
import net.minecraft.src.NBTTagShort;
import net.minecraft.src.NBTTagString;

class ItemOverride {
	private static final int MAX_DAMAGE = 65535;
	private static final int MAX_STACK_SIZE = 65535;
	static final int ITEM = 0;
	static final int OVERLAY = 1;
	static final int ARMOR = 2;
	private final String propertiesName;
	final int type;
	Icon icon;
	final String textureName;
	final ItemOverlay overlay;
	final int armorLayer;
	final BitSet itemsIDs;
	private final BitSet damage;
	private final BitSet stackSize;
	private final BitSet enchantmentIDs;
	private final BitSet enchantmentLevels;
	private final List nbtRules = new ArrayList();
	private boolean error;
	int lastEnchantmentLevel;

	static ItemOverride create(String var0) {
		Properties var1 = TexturePackAPI.getProperties(var0);

		if (var1 == null) {
			return null;
		} else {
			ItemOverride var2 = new ItemOverride(var0, var1);
			return var2.error ? null : var2;
		}
	}

	ItemOverride(String var1, Properties var2) {
		this.propertiesName = var1;
		String var3 = var1.replaceFirst("/[^/]*$", "");
		String var4 = MCPatcherUtils.getStringProperty(var2, "type", "item").toLowerCase();

		if (var4.equals("item")) {
			this.type = 0;
		} else if (var4.equals("overlay")) {
			this.type = 1;
		} else if (var4.equals("armor")) {
			this.type = 2;
		} else {
			this.error("unknown type %s", new Object[] {var4});
			this.type = 0;
		}

		var4 = MCPatcherUtils.getStringProperty(var2, "source", "");

		if (var4.equals("")) {
			var4 = MCPatcherUtils.getStringProperty(var2, "texture", "");
		}

		if (var4.equals("")) {
			var4 = MCPatcherUtils.getStringProperty(var2, "tile", "");
		}

		if (var4.equals("")) {
			var4 = var1.replaceFirst("\\.properties$", ".png");

			if (!TexturePackAPI.hasResource(var4)) {
				this.error("no source texture name specified", new Object[0]);
			}
		}

		if (!this.error && !var4.endsWith(".png")) {
			var4 = var4 + ".png";
		}

		this.textureName = !var4.startsWith("/") && !var4.startsWith("%") ? var3 + "/" + var4 : var4;

		if (this.type == 1) {
			this.overlay = ItemOverlay.create(this, var2);

			if (this.overlay == null) {
				this.error = true;
			}
		} else {
			this.overlay = null;
		}

		if (this.type == 2) {
			this.armorLayer = MCPatcherUtils.getIntProperty(var2, "layer", 0);
		} else {
			this.armorLayer = 0;
		}

		var4 = MCPatcherUtils.getStringProperty(var2, "matchItems", "");

		if (var4.equals("")) {
			if (this.type != 1) {
				this.error("no matching items specified", new Object[0]);
			}

			this.itemsIDs = null;
		} else {
			BitSet var5 = parseBitSet(var2, "matchItems", CITUtils.LOWEST_ITEM_ID, CITUtils.HIGHEST_ITEM_ID);
			boolean var6 = true;

			for (int var7 = CITUtils.LOWEST_ITEM_ID; var7 <= CITUtils.HIGHEST_ITEM_ID; ++var7) {
				if (Item.itemsList[var7] != null && !var5.get(var7)) {
					var6 = false;
					break;
				}
			}

			this.itemsIDs = var6 ? null : var5;
		}

		this.damage = parseBitSet(var2, "damage", 0, 65535);
		this.stackSize = parseBitSet(var2, "stackSize", 0, 65535);
		this.enchantmentIDs = parseBitSet(var2, "enchantmentIDs", 0, 255);
		this.enchantmentLevels = parseBitSet(var2, "enchantmentLevels", 0, 255);
		Iterator var10 = var2.entrySet().iterator();

		while (var10.hasNext()) {
			Entry var11 = (Entry)var10.next();
			String var12 = (String)var11.getKey();
			var4 = (String)var11.getValue();

			if (var12.startsWith("nbt.")) {
				String[] var8 = var12.split("\\.");

				if (var8.length > 1) {
					var8[0] = var4;

					for (int var9 = 1; var9 < var8.length; ++var9) {
						if ("*".equals(var8[var9])) {
							var8[var9] = null;
						}
					}

					this.nbtRules.add(var8);
				}
			}
		}
	}

	void preload(TileLoader var1) {
		if (this.type == 0) {
			var1.preloadTile(this.textureName, false);
		}
	}

	void registerIcon(TileLoader var1) {
		this.icon = var1.getIcon(this.textureName);
	}

	boolean match(int var1, ItemStack var2, int[] var3) {
		if (this.damage != null && !this.damage.get(var2.getItemDamageForDisplay())) {
			return false;
		} else if (this.stackSize != null && !this.stackSize.get(var2.stackSize)) {
			return false;
		} else {
			if (this.enchantmentIDs != null || this.enchantmentLevels != null) {
				if (var3 == null) {
					return false;
				}

				this.lastEnchantmentLevel = 0;
				int var4;

				if (this.enchantmentIDs == null) {
					var4 = 0;
					int[] var5 = var3;
					int var6 = var3.length;

					for (int var7 = 0; var7 < var6; ++var7) {
						int var8 = var5[var7];
						var4 += var8;
					}

					if (!this.enchantmentLevels.get(var4)) {
						return false;
					}

					this.lastEnchantmentLevel = var4;
				} else {
					for (var4 = this.enchantmentIDs.nextSetBit(0); var4 >= 0; var4 = this.enchantmentIDs.nextSetBit(var4 + 1)) {
						if (this.enchantmentLevels == null) {
							if (var3[var4] > 0) {
								this.lastEnchantmentLevel = Math.max(this.lastEnchantmentLevel, var3[var4]);
							}
						} else if (this.enchantmentLevels.get(var3[var4])) {
							this.lastEnchantmentLevel = Math.max(this.lastEnchantmentLevel, var3[var4]);
						}
					}
				}

				if (this.lastEnchantmentLevel <= 0) {
					return false;
				}
			}

			Iterator var10 = this.nbtRules.iterator();
			String[] var9;

			do {
				if (!var10.hasNext()) {
					return true;
				}

				var9 = (String[])var10.next();
			} while (matchNBTTagCompound(var9, 1, var9[0], var2.stackTagCompound));

			return false;
		}
	}

	public String toString() {
		String var1 = this.type == 0 ? "item" : (this.type == 1 ? "overlay" : (this.type == 2 ? "armor" : "unknown type " + this.type));
		return String.format("ItemOverride{%s, %s, %s}", new Object[] {var1, this.propertiesName, this.textureName});
	}

	void error(String var1, Object ... var2) {
		this.error = true;
	}

	private static BitSet parseBitSet(Properties var0, String var1, int var2, int var3) {
		String var4 = MCPatcherUtils.getStringProperty(var0, var1, "");

		if (var4.equals("")) {
			return null;
		} else {
			BitSet var5 = new BitSet();
			int[] var6 = MCPatcherUtils.parseIntegerList(var4, var2, var3);
			int var7 = var6.length;

			for (int var8 = 0; var8 < var7; ++var8) {
				int var9 = var6[var8];
				var5.set(var9);
			}

			return var5;
		}
	}

	private static boolean matchNBT(String[] var0, int var1, String var2, NBTBase var3) {
		return var3 instanceof NBTTagByte ? matchNBTTagByte(var0, var1, var2, (NBTTagByte)var3) : (var3 instanceof NBTTagCompound ? matchNBTTagCompound(var0, var1, var2, (NBTTagCompound)var3) : (var3 instanceof NBTTagList ? matchNBTTagList(var0, var1, var2, (NBTTagList)var3) : (var3 instanceof NBTTagDouble ? matchNBTTagDouble(var0, var1, var2, (NBTTagDouble)var3) : (var3 instanceof NBTTagFloat ? matchNBTTagFloat(var0, var1, var2, (NBTTagFloat)var3) : (var3 instanceof NBTTagInt ? matchNBTTagInteger(var0, var1, var2, (NBTTagInt)var3) : (var3 instanceof NBTTagLong ? matchNBTTagLong(var0, var1, var2, (NBTTagLong)var3) : (var3 instanceof NBTTagShort ? matchNBTTagShort(var0, var1, var2, (NBTTagShort)var3) : (var3 instanceof NBTTagString ? matchNBTTagString(var0, var1, var2, (NBTTagString)var3) : false))))))));
	}

	private static boolean matchNBTTagCompound(String[] var0, int var1, String var2, NBTTagCompound var3) {
		if (var3 != null && var1 < var0.length) {
			if (var0[var1] == null) {
				Iterator var4 = var3.getTags().iterator();
				NBTBase var5;

				do {
					if (!var4.hasNext()) {
						return false;
					}

					var5 = (NBTBase)var4.next();
				} while (!matchNBT(var0, var1 + 1, var2, var5));

				return true;
			} else {
				return matchNBT(var0, var1 + 1, var2, var3.getTag(var0[var1]));
			}
		} else {
			return false;
		}
	}

	private static boolean matchNBTTagList(String[] var0, int var1, String var2, NBTTagList var3) {
		if (var1 >= var0.length) {
			return false;
		} else {
			int var4;

			if (var0[var1] == null) {
				for (var4 = 0; var4 < var3.tagCount(); ++var4) {
					if (matchNBT(var0, var1 + 1, var2, var3.tagAt(var4))) {
						return true;
					}
				}
			} else {
				try {
					var4 = Integer.parseInt(var0[var1]);
					return var4 >= 0 && var4 < var3.tagCount() && matchNBT(var0, var1 + 1, var2, var3.tagAt(var4));
				} catch (NumberFormatException var5) {
					;
				}
			}

			return false;
		}
	}

	private static boolean matchNBTTagByte(String[] var0, int var1, String var2, NBTTagByte var3) {
		try {
			return var3.data == Byte.parseByte(var2);
		} catch (NumberFormatException var5) {
			return false;
		}
	}

	private static boolean matchNBTTagDouble(String[] var0, int var1, String var2, NBTTagDouble var3) {
		try {
			return var3.data == Double.parseDouble(var2);
		} catch (NumberFormatException var5) {
			return false;
		}
	}

	private static boolean matchNBTTagFloat(String[] var0, int var1, String var2, NBTTagFloat var3) {
		try {
			return var3.data == Float.parseFloat(var2);
		} catch (NumberFormatException var5) {
			return false;
		}
	}

	private static boolean matchNBTTagInteger(String[] var0, int var1, String var2, NBTTagInt var3) {
		try {
			return var3.data == Integer.parseInt(var2);
		} catch (NumberFormatException var5) {
			return false;
		}
	}

	private static boolean matchNBTTagLong(String[] var0, int var1, String var2, NBTTagLong var3) {
		try {
			return var3.data == Long.parseLong(var2);
		} catch (NumberFormatException var5) {
			return false;
		}
	}

	private static boolean matchNBTTagShort(String[] var0, int var1, String var2, NBTTagShort var3) {
		try {
			return var3.data == Short.parseShort(var2);
		} catch (NumberFormatException var5) {
			return false;
		}
	}

	private static boolean matchNBTTagString(String[] var0, int var1, String var2, NBTTagString var3) {
		return var2.equals(var3.data);
	}
}
