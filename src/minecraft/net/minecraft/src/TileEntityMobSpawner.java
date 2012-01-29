package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class TileEntityMobSpawner extends TileEntity {
	public int delay;
	private String mobID;
	public double yaw;
	public double yaw2;

	public TileEntityMobSpawner() {
		delay = -1;
		yaw2 = 0.0D;
		mobID = "Pig";
		delay = 20;
	}

	public String getMobID() {
		return mobID;
	}

	public void setMobID(String s) {
		mobID = s;
	}

	public boolean anyPlayerInRange() {
		return worldObj.getClosestPlayer((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D, 16D) != null;
	}

	public void updateEntity() {
		yaw2 = yaw;
		if (!anyPlayerInRange()) {
			return;
		}
		double d = (float)xCoord + worldObj.rand.nextFloat();
		double d1 = (float)yCoord + worldObj.rand.nextFloat();
		double d2 = (float)zCoord + worldObj.rand.nextFloat();
		worldObj.spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
		worldObj.spawnParticle("flame", d, d1, d2, 0.0D, 0.0D, 0.0D);
		for (yaw += 1000F / ((float)delay + 200F); yaw > 360D;) {
			yaw -= 360D;
			yaw2 -= 360D;
		}

		if (!worldObj.multiplayerWorld) {
			if (delay == -1) {
				updateDelay();
			}
			if (delay > 0) {
				delay--;
				return;
			}
			byte byte0 = 4;
			for (int i = 0; i < byte0; i++) {
				EntityLiving entityliving = (EntityLiving)EntityList.createEntityInWorld(mobID, worldObj);
				if (entityliving == null) {
					return;
				}
				int j = worldObj.getEntitiesWithinAABB(entityliving.getClass(), AxisAlignedBB.getBoundingBoxFromPool(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(8D, 4D, 8D)).size();
				if (j >= 6) {
					updateDelay();
					return;
				}
				if (entityliving == null) {
					continue;
				}
				double d3 = (double)xCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * 4D;
				double d4 = (yCoord + worldObj.rand.nextInt(3)) - 1;
				double d5 = (double)zCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * 4D;
				entityliving.setLocationAndAngles(d3, d4, d5, worldObj.rand.nextFloat() * 360F, 0.0F);
				if (entityliving.getCanSpawnHere()) {
					worldObj.spawnEntityInWorld(entityliving);
					worldObj.playAuxSFX(2004, xCoord, yCoord, zCoord, 0);
					entityliving.spawnExplosionParticle();
					updateDelay();
				}
			}
		}
		super.updateEntity();
	}

	private void updateDelay() {
		delay = 200 + worldObj.rand.nextInt(600);
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		mobID = nbttagcompound.getString("EntityId");
		delay = nbttagcompound.getShort("Delay");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setString("EntityId", mobID);
		nbttagcompound.setShort("Delay", (short)delay);
	}
}
