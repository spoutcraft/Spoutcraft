package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntitySnowman extends EntitySnowmanBase {
	public EntitySnowman(World world) {
		super(world);
		texture = "/mob/snowman.png";
		setSize(0.4F, 1.8F);
	}

	public int getMaxHealth() {
		return 4;
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (entityToAttack == null && !hasPath() && worldObj.rand.nextInt(100) == 0) {
			List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityMob.class, AxisAlignedBB.getBoundingBoxFromPool(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
			if (!list.isEmpty()) {
				setEntityToAttack((Entity)list.get(worldObj.rand.nextInt(list.size())));
			}
		}
		int i = MathHelper.floor_double(posX);
		int k = MathHelper.floor_double(posY);
		int i1 = MathHelper.floor_double(posZ);
		if (worldObj.getWorldChunkManager().getTemperature(i, k, i1) > 1.0F) {
			attackEntityFrom(DamageSource.onFire, 1);
		}
		for (int j = 0; j < 4; j++) {
			int l = MathHelper.floor_double(posX + (double)((float)((j % 2) * 2 - 1) * 0.25F));
			int j1 = MathHelper.floor_double(posY);
			int k1 = MathHelper.floor_double(posZ + (double)((float)(((j / 2) % 2) * 2 - 1) * 0.25F));
			if (worldObj.getBlockId(l, j1, k1) == 0 && worldObj.getWorldChunkManager().getTemperature(l, j1, k1) < 0.8F && Block.snow.canPlaceBlockAt(worldObj, l, j1, k1)) {
				worldObj.setBlockWithNotify(l, j1, k1, Block.snow.blockID);
			}
		}
	}

	protected void attackEntity(Entity entity, float f) {
		if (f < 10F) {
			double d = entity.posX - posX;
			double d1 = entity.posZ - posZ;
			if (attackTime == 0) {
				EntitySnowball entitysnowball = new EntitySnowball(worldObj, this);
				double d2 = (entity.posY + (double)entity.getEyeHeight()) - 1.1000000238418579D - entitysnowball.posY;
				float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
				worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
				worldObj.spawnEntityInWorld(entitysnowball);
				entitysnowball.setThrowableHeading(d, d2 + (double)f1, d1, 1.6F, 12F);
				attackTime = 10;
			}
			rotationYaw = (float)((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
			hasAttacked = true;
		}
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
	}

	protected int getDropItemId() {
		return Item.snowball.shiftedIndex;
	}

	protected void dropFewItems(boolean flag, int i) {
		int j = rand.nextInt(16);
		for (int k = 0; k < j; k++) {
			dropItem(Item.snowball.shiftedIndex, 1);
		}
	}
}
