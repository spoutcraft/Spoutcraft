package net.minecraft.src;

import java.util.*;

public class EnchantmentHelper {
	private static final Random enchantmentRand = new Random();
	private static final EnchantmentModifierDamage enchantmentModifierDamage = new EnchantmentModifierDamage(null);
	private static final EnchantmentModifierLiving enchantmentModifierLiving = new EnchantmentModifierLiving(null);

	public EnchantmentHelper() {
	}

	public static int getEnchantmentLevel(int i, ItemStack itemstack) {
		if (itemstack == null) {
			return 0;
		}
		NBTTagList nbttaglist = itemstack.getEnchantmentTagList();
		if (nbttaglist == null) {
			return 0;
		}
		for (int j = 0; j < nbttaglist.tagCount(); j++) {
			short word0 = ((NBTTagCompound)nbttaglist.tagAt(j)).getShort("id");
			short word1 = ((NBTTagCompound)nbttaglist.tagAt(j)).getShort("lvl");
			if (word0 == i) {
				return word1;
			}
		}

		return 0;
	}

	private static int getMaxEnchantmentLevel(int i, ItemStack aitemstack[]) {
		int j = 0;
		ItemStack aitemstack1[] = aitemstack;
		int k = aitemstack1.length;
		for (int l = 0; l < k; l++) {
			ItemStack itemstack = aitemstack1[l];
			int i1 = getEnchantmentLevel(i, itemstack);
			if (i1 > j) {
				j = i1;
			}
		}

		return j;
	}

	private static void applyEnchantmentModifier(IEnchantmentModifier ienchantmentmodifier, ItemStack itemstack) {
		if (itemstack == null) {
			return;
		}
		NBTTagList nbttaglist = itemstack.getEnchantmentTagList();
		if (nbttaglist == null) {
			return;
		}
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			short word0 = ((NBTTagCompound)nbttaglist.tagAt(i)).getShort("id");
			short word1 = ((NBTTagCompound)nbttaglist.tagAt(i)).getShort("lvl");
			if (Enchantment.enchantmentsList[word0] != null) {
				ienchantmentmodifier.calculateModifier(Enchantment.enchantmentsList[word0], word1);
			}
		}
	}

	private static void applyEnchantmentModifierArray(IEnchantmentModifier ienchantmentmodifier, ItemStack aitemstack[]) {
		ItemStack aitemstack1[] = aitemstack;
		int i = aitemstack1.length;
		for (int j = 0; j < i; j++) {
			ItemStack itemstack = aitemstack1[j];
			applyEnchantmentModifier(ienchantmentmodifier, itemstack);
		}
	}

	public static int getEnchantmentModifierDamage(InventoryPlayer inventoryplayer, DamageSource damagesource) {
		enchantmentModifierDamage.damageModifier = 0;
		enchantmentModifierDamage.damageSource = damagesource;
		applyEnchantmentModifierArray(enchantmentModifierDamage, inventoryplayer.armorInventory);
		if (enchantmentModifierDamage.damageModifier > 25) {
			enchantmentModifierDamage.damageModifier = 25;
		}
		return (enchantmentModifierDamage.damageModifier + 1 >> 1) + enchantmentRand.nextInt((enchantmentModifierDamage.damageModifier >> 1) + 1);
	}

	public static int getEnchantmentModifierLiving(InventoryPlayer inventoryplayer, EntityLiving entityliving) {
		enchantmentModifierLiving.livingModifier = 0;
		enchantmentModifierLiving.entityLiving = entityliving;
		applyEnchantmentModifier(enchantmentModifierLiving, inventoryplayer.getCurrentItem());
		if (enchantmentModifierLiving.livingModifier > 0) {
			return 1 + enchantmentRand.nextInt(enchantmentModifierLiving.livingModifier);
		}
		else {
			return 0;
		}
	}

	public static int getKnockbackModifier(InventoryPlayer inventoryplayer, EntityLiving entityliving) {
		return getEnchantmentLevel(Enchantment.knockback.effectId, inventoryplayer.getCurrentItem());
	}

	public static int getFireAspectModifier(InventoryPlayer inventoryplayer, EntityLiving entityliving) {
		return getEnchantmentLevel(Enchantment.fireAspect.effectId, inventoryplayer.getCurrentItem());
	}

	public static int getRespiration(InventoryPlayer inventoryplayer) {
		return getMaxEnchantmentLevel(Enchantment.respiration.effectId, inventoryplayer.armorInventory);
	}

	public static int getEfficiencyModifier(InventoryPlayer inventoryplayer) {
		return getEnchantmentLevel(Enchantment.efficiency.effectId, inventoryplayer.getCurrentItem());
	}

	public static int getUnbreakingModifier(InventoryPlayer inventoryplayer) {
		return getEnchantmentLevel(Enchantment.unbreaking.effectId, inventoryplayer.getCurrentItem());
	}

	public static boolean getSilkTouchModifier(InventoryPlayer inventoryplayer) {
		return getEnchantmentLevel(Enchantment.silkTouch.effectId, inventoryplayer.getCurrentItem()) > 0;
	}

	public static int getFortuneModifier(InventoryPlayer inventoryplayer) {
		return getEnchantmentLevel(Enchantment.fortune.effectId, inventoryplayer.getCurrentItem());
	}

	public static int getLootingModifier(InventoryPlayer inventoryplayer) {
		return getEnchantmentLevel(Enchantment.looting.effectId, inventoryplayer.getCurrentItem());
	}

	public static boolean getAquaAffinityModifier(InventoryPlayer inventoryplayer) {
		return getMaxEnchantmentLevel(Enchantment.aquaAffinity.effectId, inventoryplayer.armorInventory) > 0;
	}

	public static int calcItemStackEnchantability(Random random, int i, int j, ItemStack itemstack) {
		Item item = itemstack.getItem();
		int k = item.getItemEnchantability();
		if (k <= 0) {
			return 0;
		}
		if (j > 30) {
			j = 30;
		}
		j = 1 + (j >> 1) + random.nextInt(j + 1);
		int l = random.nextInt(5) + j;
		if (i == 0) {
			return (l >> 1) + 1;
		}
		if (i == 1) {
			return (l * 2) / 3 + 1;
		}
		else {
			return l;
		}
	}

	public static List buildEnchantmentList(Random random, ItemStack itemstack, int i) {
		Item item = itemstack.getItem();
		int j = item.getItemEnchantability();
		if (j <= 0) {
			return null;
		}
		j = 1 + random.nextInt((j >> 1) + 1) + random.nextInt((j >> 1) + 1);
		int k = j + i;
		float f = ((random.nextFloat() + random.nextFloat()) - 1.0F) * 0.25F;
		int l = (int)((float)k * (1.0F + f) + 0.5F);
		ArrayList arraylist = null;
		Map map = mapEnchantmentData(l, itemstack);
		if (map != null && !map.isEmpty()) {
			EnchantmentData enchantmentdata = (EnchantmentData)WeightedRandom.func_35733_a(random, map.values());
			if (enchantmentdata != null) {
				arraylist = new ArrayList();
				arraylist.add(enchantmentdata);
				for (int i1 = l >> 1; random.nextInt(50) <= i1; i1 >>= 1) {
					Iterator iterator = map.keySet().iterator();
					do {
						if (!iterator.hasNext()) {
							break;
						}
						Integer integer = (Integer)iterator.next();
						boolean flag = true;
						Iterator iterator1 = arraylist.iterator();
						do {
							if (!iterator1.hasNext()) {
								break;
							}
							EnchantmentData enchantmentdata2 = (EnchantmentData)iterator1.next();
							if (enchantmentdata2.enchantmentobj.canApplyTogether(Enchantment.enchantmentsList[integer.intValue()])) {
								continue;
							}
							flag = false;
							break;
						}
						while (true);
						if (!flag) {
							iterator.remove();
						}
					}
					while (true);
					if (!map.isEmpty()) {
						EnchantmentData enchantmentdata1 = (EnchantmentData)WeightedRandom.func_35733_a(random, map.values());
						arraylist.add(enchantmentdata1);
					}
				}
			}
		}
		return arraylist;
	}

	public static Map mapEnchantmentData(int i, ItemStack itemstack) {
		Item item = itemstack.getItem();
		HashMap hashmap = null;
		Enchantment aenchantment[] = Enchantment.enchantmentsList;
		int j = aenchantment.length;
		for (int k = 0; k < j; k++) {
			Enchantment enchantment = aenchantment[k];
			if (enchantment == null || !enchantment.type.canEnchantItem(item)) {
				continue;
			}
			for (int l = enchantment.getMinLevel(); l <= enchantment.getMaxLevel(); l++) {
				if (i < enchantment.getMinEnchantability(l) || i > enchantment.getMaxEnchantability(l)) {
					continue;
				}
				if (hashmap == null) {
					hashmap = new HashMap();
				}
				hashmap.put(Integer.valueOf(enchantment.effectId), new EnchantmentData(enchantment, l));
			}
		}

		return hashmap;
	}
}
