package net.minecraft.src;

public class ItemLeaves extends ItemBlock {
	public ItemLeaves(int i) {
		super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	public int getMetadata(int i) {
		return i | 4;
	}

	public int getIconFromDamage(int i) {
		return Block.leaves.getBlockTextureFromSideAndMetadata(0, i);
	}

	public int getColorFromDamage(int i, int j) {
		if ((i & 1) == 1) {
			return ColorizerFoliage.getFoliageColorPine();
		}
		if ((i & 2) == 2) {
			return ColorizerFoliage.getFoliageColorBirch();
		}
		else {
			return ColorizerFoliage.getFoliageColorBasic();
		}
	}
}
