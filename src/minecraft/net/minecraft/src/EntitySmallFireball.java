package net.minecraft.src;

public class EntitySmallFireball extends EntityFireball {
	public EntitySmallFireball(World world) {
		super(world);
		setSize(0.3125F, 0.3125F);
	}

	public EntitySmallFireball(World world, EntityLiving entityliving, double d, double d1, double d2) {
		super(world, entityliving, d, d1, d2);
		setSize(0.3125F, 0.3125F);
	}

	public EntitySmallFireball(World world, double d, double d1, double d2,
	        double d3, double d4, double d5) {
		super(world, d, d1, d2, d3, d4, d5);
		setSize(0.3125F, 0.3125F);
	}

	protected void func_40071_a(MovingObjectPosition movingobjectposition) {
		if (!worldObj.multiplayerWorld) {
			if (movingobjectposition.entityHit != null) {
				if (!movingobjectposition.entityHit.isImmuneToFire() && movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, shootingEntity), 5)) {
					movingobjectposition.entityHit.setFire(5);
				}
			}
			else {
				int i = movingobjectposition.blockX;
				int j = movingobjectposition.blockY;
				int k = movingobjectposition.blockZ;
				switch (movingobjectposition.sideHit) {
					case 1:
						j++;
						break;

					case 0:
						j--;
						break;

					case 2:
						k--;
						break;

					case 3:
						k++;
						break;

					case 5:
						i++;
						break;

					case 4:
						i--;
						break;
				}
				if (worldObj.isAirBlock(i, j, k)) {
					worldObj.setBlockWithNotify(i, j, k, Block.fire.blockID);
				}
			}
			setEntityDead();
		}
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		return false;
	}
}
