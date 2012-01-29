package net.minecraft.src;

public class BlockBreakable extends Block {
	private boolean localFlag;

	protected BlockBreakable(int i, int j, Material material, boolean flag) {
		super(i, j, material);
		localFlag = flag;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		int i1 = iblockaccess.getBlockId(i, j, k);
		if (!localFlag && i1 == blockID) {
			return false;
		}
		else {
			return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
		}
	}
}
