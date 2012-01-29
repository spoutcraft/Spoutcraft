package net.minecraft.src;

public enum EnumEnchantmentType {
	all("all", 0),
	armor("armor", 1),
	armor_feet("armor_feet", 2),
	armor_legs("armor_legs", 3),
	armor_torso("armor_torso", 4),
	armor_head("armor_head", 5),
	weapon("weapon", 6),
	digger("digger", 7),
	bow("bow", 8);

	private static final EnumEnchantmentType allEnchantmentTypes[] = (new EnumEnchantmentType[] {
		all, armor, armor_feet, armor_legs, armor_torso, armor_head, weapon, digger, bow
	});

	private EnumEnchantmentType(String s, int i) {
	}

	public boolean canEnchantItem(Item item) {
		if (this == all) {
			return true;
		}
		if (item instanceof ItemArmor) {
			if (this == armor) {
				return true;
			}
			ItemArmor itemarmor = (ItemArmor)item;
			if (itemarmor.armorType == 0) {
				return this == armor_head;
			}
			if (itemarmor.armorType == 2) {
				return this == armor_legs;
			}
			if (itemarmor.armorType == 1) {
				return this == armor_torso;
			}
			if (itemarmor.armorType == 3) {
				return this == armor_feet;
			}
			else {
				return false;
			}
		}
		if (item instanceof ItemSword) {
			return this == weapon;
		}
		if (item instanceof ItemTool) {
			return this == digger;
		}
		if (item instanceof ItemBow) {
			return this == bow;
		}
		else {
			return false;
		}
	}
}
