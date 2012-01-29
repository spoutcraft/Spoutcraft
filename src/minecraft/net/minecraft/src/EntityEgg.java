package net.minecraft.src;

import org.spoutcraft.client.entity.CraftEgg;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityChicken;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityThrowable;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class EntityEgg extends EntityThrowable {

	public EntityEgg(World var1) {
		super(var1);
		//Spout start
		this.spoutEntity = new CraftEgg(this);
		//Spout end
	}

	public EntityEgg(World var1, EntityLiving var2) {
		super(var1, var2);
		//Spout start
		this.spoutEntity = new CraftEgg(this);
		//Spout end
	}

	public EntityEgg(World var1, double var2, double var4, double var6) {
		super(var1, var2, var4, var6);
		//Spout start
		this.spoutEntity = new CraftEgg(this);
		//Spout end
	}

	protected void onThrowableCollision(MovingObjectPosition var1) {
		if(var1.entityHit != null && var1.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.throwingEntity), 0)) {
			;
		}

		if(!this.worldObj.multiplayerWorld && this.rand.nextInt(8) == 0) {
			byte var2 = 1;
			if(this.rand.nextInt(32) == 0) {
				var2 = 4;
			}

			for(int var3 = 0; var3 < var2; ++var3) {
				EntityChicken var4 = new EntityChicken(this.worldObj);
				var4.setDelay(-24000);
				var4.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
				this.worldObj.spawnEntityInWorld(var4);
			}
		}

		for(int var5 = 0; var5 < 8; ++var5) {
			this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
		}

		if(!this.worldObj.multiplayerWorld) {
			this.setEntityDead();
		}

	}
}
