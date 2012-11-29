package net.minecraft.src;

import org.spoutcraft.client.entity.CraftEgg; // Spout

public class EntityEgg extends EntityThrowable {
	public EntityEgg(World par1World) {
		super(par1World);
		// Spout Start
		this.spoutEntity = new CraftEgg(this);
		// Spout End
	}

	public EntityEgg(World par1World, EntityLiving par2EntityLiving) {
		super(par1World, par2EntityLiving);
		// Spout Start
		this.spoutEntity = new CraftEgg(this);
		// Spout End
	}

	public EntityEgg(World par1World, double par2, double par4, double par6) {
		super(par1World, par2, par4, par6);
		// Spout Start
		this.spoutEntity = new CraftEgg(this);
		// Spout End
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
		if (par1MovingObjectPosition.entityHit != null) {
			par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_85052_h()), 0);
		}

		if (!this.worldObj.isRemote && this.rand.nextInt(8) == 0) {
			byte var2 = 1;

			if (this.rand.nextInt(32) == 0) {
				var2 = 4;
			}

			for (int var3 = 0; var3 < var2; ++var3) {
				EntityChicken var4 = new EntityChicken(this.worldObj);
				var4.setGrowingAge(-24000);
				var4.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
				this.worldObj.spawnEntityInWorld(var4);
			}
		}

		for (int var5 = 0; var5 < 8; ++var5) {
			this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
		}

		if (!this.worldObj.isRemote) {
			this.setDead();
		}
	}
}
