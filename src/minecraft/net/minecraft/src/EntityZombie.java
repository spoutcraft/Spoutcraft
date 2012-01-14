package net.minecraft.src;

import org.spoutcraft.client.entity.CraftZombie;

import net.minecraft.src.EntityAIAttackOnCollide;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EnumCreatureAttribute;
import net.minecraft.src.Item;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class EntityZombie extends EntityMob {

	public EntityZombie(World var1) {
		super(var1);
		this.texture = "/mob/zombie.png";
		this.moveSpeed = 0.5F;
		this.attackStrength = 4;
		this.field_46019_bU.func_46118_a(1, new EntityAISwimming(this));
		this.field_46019_bU.func_46118_a(2, new EntityAIAttackOnCollide(this, var1, 16.0F));
		this.field_46019_bU.func_46118_a(3, new EntityAIWander(this));
		this.field_46019_bU.func_46118_a(4, new EntityAIWatchClosest(this, var1, 8.0F));
		this.field_46019_bU.func_46118_a(4, new EntityAILookIdle(this));
		//Spout start
		this.spoutEntity = new CraftZombie(this);
		//Spout end
	}

	public int getMaxHealth() {
		return 20;
	}

	public int getTotalArmorValue() {
		return 2;
	}
	
	protected boolean func_46006_aR() {
		return false;
	}

	public void onLivingUpdate() {
		if(this.worldObj.isDaytime() && !this.worldObj.multiplayerWorld) {
			float var1 = this.getEntityBrightness(1.0F);
			if(var1 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F) {
				this.setFire(8);
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

	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}
}
