package net.minecraft.src;

public class ItemColored extends ItemBlock {
	private final Block blockRef;
	private String blockNames[];

	public ItemColored(int i, boolean flag) {
		super(i);
		blockRef = Block.blocksList[getBlockID()];
		if (flag) {
			setMaxDamage(0);
			setHasSubtypes(true);
		}
	}

	public int getColorFromDamage(int i, int j) {
		return blockRef.getRenderColor(i);
	}

	public int getIconFromDamage(int i) {
		return blockRef.getBlockTextureFromSideAndMetadata(0, i);
	}

	public int getMetadata(int i) {
		return i;
	}

	public ItemColored setBlockNames(String as[]) {
		blockNames = as;
		return this;
	}

	public String getItemNameIS(ItemStack itemstack) {
		if (blockNames == null) {
			return super.getItemNameIS(itemstack);
		}
		int i = itemstack.getItemDamage();
		if (i >= 0 && i < blockNames.length) {
			return (new StringBuilder()).append(super.getItemNameIS(itemstack)).append(".").append(blockNames[i]).toString();
		}
		else {
			return super.getItemNameIS(itemstack);
		}
	}
}
