package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import net.minecraft.src.Stitcher;
import net.minecraft.src.TextureMap;

class CITUtils$ItemOverride {
	private static final int NBITS = 65535;
	private final String propertiesName;
	Icon icon;
	private final List textureNames = new ArrayList();
	final List itemsIDs = new ArrayList();
	private final BitSet damage;
	private final BitSet stackSize;
	private final List nbtRules = new ArrayList();
	private final String textureName;
	private boolean error;

	static CITUtils$ItemOverride create(String var0) {
		Properties var1 = TexturePackAPI.getProperties(var0);

		if (var1 == null) {
			return null;
		} else {
			CITUtils$ItemOverride var2 = new CITUtils$ItemOverride(var0, var1);
			return var2.error ? null : var2;
		}
	}

	private CITUtils$ItemOverride(String var1, Properties var2) {
		this.propertiesName = var1;
		String var3 = var1.replaceFirst("/[^/]*$", "");
		String var4 = MCPatcherUtils.getStringProperty(var2, "source", "");

		if (var4.equals("")) {
			var4 = MCPatcherUtils.getStringProperty(var2, "texture", "");
		}

		if (var4.equals("")) {
			var4 = MCPatcherUtils.getStringProperty(var2, "tile", "");
		}

		if (var4.equals("")) {
			this.error("no source texture name specified", new Object[0]);
		}

		this.textureName = var4.startsWith("/") ? var4 : var3 + "/" + var4;
		var4 = MCPatcherUtils.getStringProperty(var2, "matchItems", "");
		String[] var5 = var4.split("\\s+");
		int var6 = var5.length;
		int var7;
		int var17;

		for (var7 = 0; var7 < var6; ++var7) {
			String var8 = var5[var7];

			if (!var8.equals("")) {
				if (!var8.matches("\\d+") && !var8.matches("\\d+-\\d+")) {
					for (var17 = 0; var17 < Item.itemsList.length; ++var17) {
						if (var8.equals(CITUtils.access$000(var17))) {
							this.itemsIDs.add(Integer.valueOf(var17));
							break;
						}
					}
				} else {
					int[] var9 = MCPatcherUtils.parseIntegerList(var8, 0, Item.itemsList.length - 1);
					int var10 = var9.length;

					for (int var11 = 0; var11 < var10; ++var11) {
						int var12 = var9[var11];
						this.itemsIDs.add(Integer.valueOf(var12));
					}
				}
			}
		}

		if (this.itemsIDs.isEmpty()) {
			this.error("no matching items specified", new Object[0]);
		}

		var4 = MCPatcherUtils.getStringProperty(var2, "damage", "");
		int[] var13;
		int var16;

		if (var4.equals("")) {
			this.damage = null;
		} else {
			this.damage = new BitSet();
			var13 = MCPatcherUtils.parseIntegerList(var4, 0, 65535);
			var6 = var13.length;

			for (var7 = 0; var7 < var6; ++var7) {
				var16 = var13[var7];
				this.damage.set(var16);
			}
		}

		var4 = MCPatcherUtils.getStringProperty(var2, "stackSize", "");

		if (var4.equals("")) {
			this.stackSize = null;
		} else {
			this.stackSize = new BitSet();
			var13 = MCPatcherUtils.parseIntegerList(var4, 0, 65535);
			var6 = var13.length;

			for (var7 = 0; var7 < var6; ++var7) {
				var16 = var13[var7];
				this.stackSize.set(var16);
			}
		}

		Iterator var14 = var2.entrySet().iterator();

		while (var14.hasNext()) {
			Entry var15 = (Entry)var14.next();
			String var18 = (String)var15.getKey();
			var4 = (String)var15.getValue();

			if (var18.startsWith("nbt.")) {
				String[] var19 = var18.split("\\.");

				if (var19.length > 1) {
					var19[0] = var4;

					for (var17 = 1; var17 < var19.length; ++var17) {
						if ("*".equals(var19[var17])) {
							var19[var17] = null;
						}
					}

					this.nbtRules.add(var19);
				}
			}
		}

		if (!this.error) {
			CITUtils.access$100().preload(this.textureName, this.textureNames, false);
		}
	}

	void registerIcon(TextureMap var1, Stitcher var2, Map var3) {
		Icon[] var4 = CITUtils.access$100().registerIcons(var1, var2, var3, this.textureNames);
		this.icon = var4[0];
	}

	boolean match(Icon var1, int var2, ItemStack var3) {
		if (this.damage != null && !this.damage.get(var3.getItemDamageForDisplay())) {
			return false;
		} else if (this.stackSize != null && !this.stackSize.get(var3.stackSize)) {
			return false;
		} else {
			Iterator var4 = this.nbtRules.iterator();
			String[] var5;

			do {
				if (!var4.hasNext()) {
					return true;
				}

				var5 = (String[])var4.next();
			} while (matchNBTTagCompound(var5, 1, var5[0], var3.stackTagCompound));

			return false;
		}
	}

	public String toString() {
		return String.format("ItemOverride{%s, %s}", new Object[] {this.propertiesName, this.textureName});
	}

	private void error(String var1, Object ... var2) {
		this.error = true;
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
