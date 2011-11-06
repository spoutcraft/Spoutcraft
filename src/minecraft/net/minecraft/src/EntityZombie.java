package net.minecraft.src;

import org.getspout.spout.entity.CraftZombie;

import net.minecraft.src.EntityMob;
import net.minecraft.src.Item;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class EntityZombie extends EntityMob {

	public EntityZombie(World var1) {
		super(var1);
		this.texture = "/mob/zombie.png";
		this.moveSpeed = 0.5F;
		this.attackStrength = 5;
		
		//Spout start
		this.spoutEntity = new CraftZombie(this);
		//Spout end
	}

	public void onLivingUpdate() {
		if(this.worldObj.isDaytime() && !this.worldObj.multiplayerWorld) {
			float var1 = this.getEntityBrightness(1.0F);
			if(var1 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F) {
				this.fire = 300;
			}
		}

		super.onLivingUpdate();
	}

	protected String getLivingSound() {
		return "mob.zombie";
	}

	protected String getHurtSound() {
		return "mob.zombiehurt";
	}

	protected String getDeathSound() {
		return "mob.zombiedeath";
	}

	protected int getDropItemId() {
		return Item.rottenFlesh.shiftedIndex;
	}
}
