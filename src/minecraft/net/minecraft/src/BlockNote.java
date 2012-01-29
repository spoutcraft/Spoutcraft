package net.minecraft.src;

public class BlockNote extends BlockContainer {
	public BlockNote(int i) {
		super(i, 74, Material.wood);
	}

	public int getBlockTextureFromSide(int i) {
		return blockIndexInTexture;
	}

	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (l > 0) {
			boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k);
			TileEntityNote tileentitynote = (TileEntityNote)world.getBlockTileEntity(i, j, k);
			if (tileentitynote != null && tileentitynote.previousRedstoneState != flag) {
				if (flag) {
					tileentitynote.triggerNote(world, i, j, k);
				}
				tileentitynote.previousRedstoneState = flag;
			}
		}
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if (world.multiplayerWorld) {
			return true;
		}
		TileEntityNote tileentitynote = (TileEntityNote)world.getBlockTileEntity(i, j, k);
		if (tileentitynote != null) {
			tileentitynote.changePitch();
			tileentitynote.triggerNote(world, i, j, k);
		}
		return true;
	}

	public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if (world.multiplayerWorld) {
			return;
		}
		TileEntityNote tileentitynote = (TileEntityNote)world.getBlockTileEntity(i, j, k);
		if (tileentitynote != null) {
			tileentitynote.triggerNote(world, i, j, k);
		}
	}

	public TileEntity getBlockEntity() {
		return new TileEntityNote();
	}

	public void powerBlock(World world, int i, int j, int k, int l, int i1) {
		float f = (float)Math.pow(2D, (double)(i1 - 12) / 12D);
		String s = "harp";
		if (l == 1) {
			s = "bd";
		}
		if (l == 2) {
			s = "snare";
		}
		if (l == 3) {
			s = "hat";
		}
		if (l == 4) {
			s = "bassattack";
		}
		world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, (new StringBuilder()).append("note.").append(s).toString(), 3F, f);
		world.spawnParticle("note", (double)i + 0.5D, (double)j + 1.2D, (double)k + 0.5D, (double)i1 / 24D, 0.0D, 0.0D);
	}
}
