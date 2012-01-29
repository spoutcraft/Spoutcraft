package net.minecraft.src;

import java.util.*;

public class ItemPotion extends Item {
	private HashMap idEffectNameMap;

	public ItemPotion(int i) {
		super(i);
		idEffectNameMap = new HashMap();
		setMaxStackSize(1);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	public List getEffectNames(ItemStack itemstack) {
		return getEffectNamesFromDamage(itemstack.getItemDamage());
	}

	public List getEffectNamesFromDamage(int i) {
		List list = (List)idEffectNameMap.get(Integer.valueOf(i));
		if (list == null) {
			list = PotionHelper.getPotionEffects(i, false);
			idEffectNameMap.put(Integer.valueOf(i), list);
		}
		return list;
	}

	public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		itemstack.stackSize--;
		if (!world.multiplayerWorld) {
			List list = getEffectNames(itemstack);
			if (list != null) {
				PotionEffect potioneffect;
				for (Iterator iterator = list.iterator(); iterator.hasNext(); entityplayer.addPotionEffect(new PotionEffect(potioneffect))) {
					potioneffect = (PotionEffect)iterator.next();
				}
			}
		}
		if (itemstack.stackSize <= 0) {
			return new ItemStack(Item.glassBottle);
		}
		else {
			entityplayer.inventory.addItemStackToInventory(new ItemStack(Item.glassBottle));
			return itemstack;
		}
	}

	public int getMaxItemUseDuration(ItemStack itemstack) {
		return 32;
	}

	public EnumAction getItemUseAction(ItemStack itemstack) {
		return EnumAction.drink;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (isSplash(itemstack.getItemDamage())) {
			itemstack.stackSize--;
			world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if (!world.multiplayerWorld) {
				world.spawnEntityInWorld(new EntityPotion(world, entityplayer, itemstack.getItemDamage()));
			}
			return itemstack;
		}
		else {
			entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
			return itemstack;
		}
	}

	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
		return false;
	}

	public int getIconFromDamage(int i) {
		return !isSplash(i) ? 140 : 154;
	}

	public int func_46057_a(int i, int j) {
		if (j == 0) {
			return 141;
		}
		else {
			return super.func_46057_a(i, j);
		}
	}

	public static boolean isSplash(int i) {
		return (i & 0x4000) != 0;
	}

	public int getColorFromDamage(int i, int j) {
		if (j > 0) {
			return 0xffffff;
		}
		else {
			return PotionHelper.func_40358_a(i, false);
		}
	}

	public boolean func_46058_c() {
		return true;
	}

	public boolean isEffectInstant(int i) {
		List list = getEffectNamesFromDamage(i);
		if (list == null || list.isEmpty()) {
			return false;
		}
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			PotionEffect potioneffect = (PotionEffect)iterator.next();
			if (Potion.potionTypes[potioneffect.getPotionID()].isInstant()) {
				return true;
			}
		}

		return false;
	}

	public String getItemDisplayName(ItemStack itemstack) {
		if (itemstack.getItemDamage() == 0) {
			return StatCollector.translateToLocal("item.emptyPotion.name").trim();
		}
		String s = "";
		if (isSplash(itemstack.getItemDamage())) {
			s = (new StringBuilder()).append(StatCollector.translateToLocal("potion.prefix.grenade").trim()).append(" ").toString();
		}
		List list = Item.potion.getEffectNames(itemstack);
		if (list != null && !list.isEmpty()) {
			String s1 = ((PotionEffect)list.get(0)).getEffectName();
			s1 = (new StringBuilder()).append(s1).append(".postfix").toString();
			return (new StringBuilder()).append(s).append(StatCollector.translateToLocal(s1).trim()).toString();
		}
		else {
			String s2 = PotionHelper.func_40359_b(itemstack.getItemDamage());
			return (new StringBuilder()).append(StatCollector.translateToLocal(s2).trim()).append(" ").append(super.getItemDisplayName(itemstack)).toString();
		}
	}

	public void addInformation(ItemStack itemstack, List list) {
		if (itemstack.getItemDamage() == 0) {
			return;
		}
		List list1 = Item.potion.getEffectNames(itemstack);
		if (list1 != null && !list1.isEmpty()) {
			for (Iterator iterator = list1.iterator(); iterator.hasNext();) {
				PotionEffect potioneffect = (PotionEffect)iterator.next();
				String s1 = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
				if (potioneffect.getAmplifier() > 0) {
					s1 = (new StringBuilder()).append(s1).append(" ").append(StatCollector.translateToLocal((new StringBuilder()).append("potion.potency.").append(potioneffect.getAmplifier()).toString()).trim()).toString();
				}
				if (potioneffect.getDuration() > 20) {
					s1 = (new StringBuilder()).append(s1).append(" (").append(Potion.func_40620_a(potioneffect)).append(")").toString();
				}
				if (Potion.potionTypes[potioneffect.getPotionID()].getIsBadEffect()) {
					list.add((new StringBuilder()).append("\247c").append(s1).toString());
				}
				else {
					list.add((new StringBuilder()).append("\2477").append(s1).toString());
				}
			}
		}
		else {
			String s = StatCollector.translateToLocal("potion.empty").trim();
			list.add((new StringBuilder()).append("\2477").append(s).toString());
		}
	}

	public boolean hasEffect(ItemStack itemstack) {
		List list = getEffectNames(itemstack);
		return list != null && !list.isEmpty();
	}
}
