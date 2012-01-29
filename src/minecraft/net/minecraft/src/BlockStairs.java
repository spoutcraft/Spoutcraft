package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockStairs extends Block {
	private Block modelBlock;

	protected BlockStairs(int i, Block block) {
		super(i, block.blockIndexInTexture, block.blockMaterial);
		modelBlock = block;
		setHardness(block.blockHardness);
		setResistance(block.blockResistance / 3F);
		setStepSound(block.stepSound);
		setLightOpacity(255);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return super.getCollisionBoundingBoxFromPool(world, i, j, k);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 10;
	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return super.shouldSideBeRendered(iblockaccess, i, j, k, l);
	}

	public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist) {
		int l = world.getBlockMetadata(i, j, k);
		if (l == 0) {
			setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
			setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		}
		else if (l == 1) {
			setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
			setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		}
		else if (l == 2) {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
			setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		}
		else if (l == 3) {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
			setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
			super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
		}
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		modelBlock.randomDisplayTick(world, i, j, k, random);
	}

	public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer) {
		modelBlock.onBlockClicked(world, i, j, k, entityplayer);
	}

	public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l) {
		modelBlock.onBlockDestroyedByPlayer(world, i, j, k, l);
	}

	public int getMixedBrightnessForBlock(IBlockAccess iblockaccess, int i, int j, int k) {
		return modelBlock.getMixedBrightnessForBlock(iblockaccess, i, j, k);
	}

	public float getBlockBrightness(IBlockAccess iblockaccess, int i, int j, int k) {
		return modelBlock.getBlockBrightness(iblockaccess, i, j, k);
	}

	public float getExplosionResistance(Entity entity) {
		return modelBlock.getExplosionResistance(entity);
	}

	public int getRenderBlockPass() {
		return modelBlock.getRenderBlockPass();
	}

	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return modelBlock.getBlockTextureFromSideAndMetadata(i, 0);
	}

	public int getBlockTextureFromSide(int i) {
		return modelBlock.getBlockTextureFromSideAndMetadata(i, 0);
	}

	public int tickRate() {
		return modelBlock.tickRate();
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		return modelBlock.getSelectedBoundingBoxFromPool(world, i, j, k);
	}

	public void velocityToAddToEntity(World world, int i, int j, int k, Entity entity, Vec3D vec3d) {
		modelBlock.velocityToAddToEntity(world, i, j, k, entity, vec3d);
	}

	public boolean isCollidable() {
		return modelBlock.isCollidable();
	}

	public boolean canCollideCheck(int i, boolean flag) {
		return modelBlock.canCollideCheck(i, flag);
	}

	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return modelBlock.canPlaceBlockAt(world, i, j, k);
	}

	public void onBlockAdded(World world, int i, int j, int k) {
		onNeighborBlockChange(world, i, j, k, 0);
		modelBlock.onBlockAdded(world, i, j, k);
	}

	public void onBlockRemoval(World world, int i, int j, int k) {
		modelBlock.onBlockRemoval(world, i, j, k);
	}

	public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
		modelBlock.onEntityWalking(world, i, j, k, entity);
	}

	public void updateTick(World world, int i, int j, int k, Random random) {
		modelBlock.updateTick(world, i, j, k, random);
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		return modelBlock.blockActivated(world, i, j, k, entityplayer);
	}

	public void onBlockDestroyedByExplosion(World world, int i, int j, int k) {
		modelBlock.onBlockDestroyedByExplosion(world, i, j, k);
	}

	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		if (l == 0) {
			world.setBlockMetadataWithNotify(i, j, k, 2);
		}
		if (l == 1) {
			world.setBlockMetadataWithNotify(i, j, k, 1);
		}
		if (l == 2) {
			world.setBlockMetadataWithNotify(i, j, k, 3);
		}
		if (l == 3) {
			world.setBlockMetadataWithNotify(i, j, k, 0);
		}
	}
}
