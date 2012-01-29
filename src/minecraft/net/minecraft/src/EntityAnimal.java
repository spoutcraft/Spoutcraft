package net.minecraft.src;

import java.util.List;
import java.util.Random;

public abstract class EntityAnimal extends EntityCreature {
	private int inLove;
	private int breeding;

	public EntityAnimal(World world) {
		super(world);
		breeding = 0;
	}

	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(12, new Integer(0));
	}

	public int getDelay() {
		return dataWatcher.getWatchableObjectInt(12);
	}

	public void setDelay(int i) {
		dataWatcher.updateObject(12, Integer.valueOf(i));
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
		int i = getDelay();
		if (i < 0) {
			i++;
			setDelay(i);
		}
		else if (i > 0) {
			i--;
			setDelay(i);
		}
		if (inLove > 0) {
			inLove--;
			String s = "heart";
			if (inLove % 10 == 0) {
				double d = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				double d2 = rand.nextGaussian() * 0.02D;
				worldObj.spawnParticle(s, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
			}
		}
		else {
			breeding = 0;
		}
	}

	protected void attackEntity(Entity entity, float f) {
		if (entity instanceof EntityPlayer) {
			if (f < 3F) {
				double d = entity.posX - posX;
				double d1 = entity.posZ - posZ;
				rotationYaw = (float)((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
				hasAttacked = true;
			}
			EntityPlayer entityplayer = (EntityPlayer)entity;
			if (entityplayer.getCurrentEquippedItem() == null || !isWheat(entityplayer.getCurrentEquippedItem())) {
				entityToAttack = null;
			}
		}
		else if (entity instanceof EntityAnimal) {
			EntityAnimal entityanimal = (EntityAnimal)entity;
			if (getDelay() > 0 && entityanimal.getDelay() < 0) {
				if ((double)f < 2.5D) {
					hasAttacked = true;
				}
			}
			else if (inLove > 0 && entityanimal.inLove > 0) {
				if (entityanimal.entityToAttack == null) {
					entityanimal.entityToAttack = this;
				}
				if (entityanimal.entityToAttack == this && (double)f < 3.5D) {
					entityanimal.inLove++;
					inLove++;
					breeding++;
					if (breeding % 4 == 0) {
						worldObj.spawnParticle("heart", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, 0.0D, 0.0D, 0.0D);
					}
					if (breeding == 60) {
						procreate((EntityAnimal)entity);
					}
				}
				else {
					breeding = 0;
				}
			}
			else {
				breeding = 0;
				entityToAttack = null;
			}
		}
	}

	private void procreate(EntityAnimal entityanimal) {
		EntityAnimal entityanimal1 = spawnBabyAnimal(entityanimal);
		if (entityanimal1 != null) {
			setDelay(6000);
			entityanimal.setDelay(6000);
			inLove = 0;
			breeding = 0;
			entityToAttack = null;
			entityanimal.entityToAttack = null;
			entityanimal.breeding = 0;
			entityanimal.inLove = 0;
			entityanimal1.setDelay(-24000);
			entityanimal1.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
			for (int i = 0; i < 7; i++) {
				double d = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				double d2 = rand.nextGaussian() * 0.02D;
				worldObj.spawnParticle("heart", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
			}

			worldObj.spawnEntityInWorld(entityanimal1);
		}
	}

	protected abstract EntityAnimal spawnBabyAnimal(EntityAnimal entityanimal);

	protected void attackBlockedEntity(Entity entity, float f) {
	}

	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		fleeingTick = 60;
		entityToAttack = null;
		inLove = 0;
		return super.attackEntityFrom(damagesource, i);
	}

	public float getBlockPathWeight(int i, int j, int k) {
		if (worldObj.getBlockId(i, j - 1, k) == Block.grass.blockID) {
			return 10F;
		}
		else {
			return worldObj.getLightBrightness(i, j, k) - 0.5F;
		}
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("Age", getDelay());
		nbttagcompound.setInteger("InLove", inLove);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		setDelay(nbttagcompound.getInteger("Age"));
		inLove = nbttagcompound.getInteger("InLove");
	}

	protected Entity findPlayerToAttack() {
		if (fleeingTick > 0) {
			return null;
		}
		float f = 8F;
		if (inLove > 0) {
			List list = worldObj.getEntitiesWithinAABB(getClass(), boundingBox.expand(f, f, f));
			for (int i = 0; i < list.size(); i++) {
				EntityAnimal entityanimal = (EntityAnimal)list.get(i);
				if (entityanimal != this && entityanimal.inLove > 0) {
					return entityanimal;
				}
			}
		}
		else if (getDelay() == 0) {
			List list1 = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityPlayer.class, boundingBox.expand(f, f, f));
			for (int j = 0; j < list1.size(); j++) {
				EntityPlayer entityplayer = (EntityPlayer)list1.get(j);
				if (entityplayer.getCurrentEquippedItem() != null && isWheat(entityplayer.getCurrentEquippedItem())) {
					return entityplayer;
				}
			}
		}
		else if (getDelay() > 0) {
			List list2 = worldObj.getEntitiesWithinAABB(getClass(), boundingBox.expand(f, f, f));
			for (int k = 0; k < list2.size(); k++) {
				EntityAnimal entityanimal1 = (EntityAnimal)list2.get(k);
				if (entityanimal1 != this && entityanimal1.getDelay() < 0) {
					return entityanimal1;
				}
			}
		}
		return null;
	}

	public boolean getCanSpawnHere() {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(boundingBox.minY);
		int k = MathHelper.floor_double(posZ);
		return worldObj.getBlockId(i, j - 1, k) == Block.grass.blockID && worldObj.getFullBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere();
	}

	public int getTalkInterval() {
		return 120;
	}

	protected boolean canDespawn() {
		return false;
	}

	protected int getExperiencePoints(EntityPlayer entityplayer) {
		return 1 + worldObj.rand.nextInt(3);
	}

	protected boolean isWheat(ItemStack itemstack) {
		return itemstack.itemID == Item.wheat.shiftedIndex;
	}

	public boolean interact(EntityPlayer entityplayer) {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (itemstack != null && isWheat(itemstack) && getDelay() == 0) {
			itemstack.stackSize--;
			if (itemstack.stackSize <= 0) {
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
			}
			inLove = 600;
			entityToAttack = null;
			for (int i = 0; i < 7; i++) {
				double d = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				double d2 = rand.nextGaussian() * 0.02D;
				worldObj.spawnParticle("heart", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
			}

			return true;
		}
		else {
			return super.interact(entityplayer);
		}
	}

	public boolean isChild() {
		return getDelay() < 0;
	}
}
