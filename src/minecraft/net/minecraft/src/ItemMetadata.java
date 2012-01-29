package net.minecraft.src;

public class ItemMetadata extends ItemBlock {
	private Block field_35437_a;

	public ItemMetadata(int i, Block block) {
		super(i);
		field_35437_a = block;
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	public int getIconFromDamage(int i) {
		return field_35437_a.getBlockTextureFromSideAndMetadata(2, i);
	}

	public int getMetadata(int i) {
		return i;
	}
}
