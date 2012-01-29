package net.minecraft.src;

public class MaterialLogic extends Material {
	public MaterialLogic(MapColor mapcolor) {
		super(mapcolor);
	}

	public boolean isSolid() {
		return false;
	}

	public boolean getCanBlockGrass() {
		return false;
	}

	public boolean getIsSolid() {
		return false;
	}
}
