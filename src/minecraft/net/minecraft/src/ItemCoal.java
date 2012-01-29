package net.minecraft.src;

public class ItemCoal extends Item {
	public ItemCoal(int i) {
		super(i);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	public String getItemNameIS(ItemStack itemstack) {
		if (itemstack.getItemDamage() == 1) {
			return "item.charcoal";
		}
		else {
			return "item.coal";
		}
	}
}
