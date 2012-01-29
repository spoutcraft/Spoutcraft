package net.minecraft.src;

public class MaterialPortal extends Material {
	public MaterialPortal(MapColor mapcolor) {
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
