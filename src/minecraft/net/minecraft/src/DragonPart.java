package net.minecraft.src;

public class DragonPart extends Entity {
	public final EntityDragonBase entityDragonObj;
	public final String name;

	public DragonPart(EntityDragonBase entitydragonbase, String s, float f, float f1) {
		super(entitydragonbase.worldObj);
		setSize(f, f1);
		entityDragonObj = entitydragonbase;
		name = s;
	}

	protected void entityInit() {
	}

	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}

	public boolean canBeCollidedWith() {
		return true;
	}

	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		return entityDragonObj.attackEntityFromPart(this, damagesource, i);
	}

	public boolean isEntityEqual(Entity entity) {
		return this == entity || entityDragonObj == entity;
	}
}
