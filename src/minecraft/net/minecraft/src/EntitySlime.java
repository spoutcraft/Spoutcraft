package net.minecraft.src;

import org.getspout.spout.entity.CraftSlime;

import net.minecraft.src.Chunk;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IMob;
import net.minecraft.src.Item;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntitySlime extends EntityLiving implements IMob {

	public float field_768_a;
	public float field_767_b;
	private int slimeJumpDelay = 0;


	public EntitySlime(World var1) {
		super(var1);
		this.texture = "/mob/slime.png";
		int var2 = 1 << this.rand.nextInt(3);
		this.yOffset = 0.0F;
		this.slimeJumpDelay = this.rand.nextInt(20) + 10;
		this.setSlimeSize(var2);
		this.field_35171_bJ = var2;
		//Spout start
		this.spoutEntity = new CraftSlime(this);
		//Spout end
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte)1));
	}

	public void setSlimeSize(int var1) {
		this.dataWatcher.updateObject(16, new Byte((byte)var1));
		this.setSize(0.6F * (float)var1, 0.6F * (float)var1);
		this.health = var1 * var1;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public int getSlimeSize() {
		return this.dataWatcher.getWatchableObjectByte(16);
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setInteger("Size", this.getSlimeSize() - 1);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.setSlimeSize(var1.getInteger("Size") + 1);
	}

	public void onUpdate() {
		this.field_767_b = this.field_768_a;
		boolean var1 = this.onGround;
		super.onUpdate();
		if(this.onGround && !var1) {
			int var2 = this.getSlimeSize();

			for(int var3 = 0; var3 < var2 * 8; ++var3) {
				float var4 = this.rand.nextFloat() * 3.1415927F * 2.0F;
				float var5 = this.rand.nextFloat() * 0.5F + 0.5F;
				float var6 = MathHelper.sin(var4) * (float)var2 * 0.5F * var5;
				float var7 = MathHelper.cos(var4) * (float)var2 * 0.5F * var5;
				this.worldObj.spawnParticle("slime", this.posX + (double)var6, this.boundingBox.minY, this.posZ + (double)var7, 0.0D, 0.0D, 0.0D);
			}

			if(var2 > 2) {
				this.worldObj.playSoundAtEntity(this, "mob.slime", this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			}

			this.field_768_a = -0.5F;
		}

		this.field_768_a *= 0.6F;
	}

	protected void updateEntityActionState() {
		this.despawnEntity();
		EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		if(var1 != null) {
			this.faceEntity(var1, 10.0F, 20.0F);
		}

		if(this.onGround && this.slimeJumpDelay-- <= 0) {
			this.slimeJumpDelay = this.rand.nextInt(20) + 10;
			if(var1 != null) {
				this.slimeJumpDelay /= 3;
			}

			this.isJumping = true;
			if(this.getSlimeSize() > 1) {
				this.worldObj.playSoundAtEntity(this, "mob.slime", this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
			}

			this.field_768_a = 1.0F;
			this.moveStrafing = 1.0F - this.rand.nextFloat() * 2.0F;
			this.moveForward = (float)(1 * this.getSlimeSize());
		} else {
			this.isJumping = false;
			if(this.onGround) {
				this.moveStrafing = this.moveForward = 0.0F;
			}
		}

	}

	public void setEntityDead() {
		int var1 = this.getSlimeSize();
		if(!this.worldObj.multiplayerWorld && var1 > 1 && this.health == 0) {
			for(int var2 = 0; var2 < 4; ++var2) {
				float var3 = ((float)(var2 % 2) - 0.5F) * (float)var1 / 4.0F;
				float var4 = ((float)(var2 / 2) - 0.5F) * (float)var1 / 4.0F;
				EntitySlime var5 = new EntitySlime(this.worldObj);
				var5.setSlimeSize(var1 / 2);
				var5.setLocationAndAngles(this.posX + (double)var3, this.posY + 0.5D, this.posZ + (double)var4, this.rand.nextFloat() * 360.0F, 0.0F);
				this.worldObj.entityJoinedWorld(var5);
			}
		}

		super.setEntityDead();
	}

	public void onCollideWithPlayer(EntityPlayer var1) {
		int var2 = this.getSlimeSize();
		if(var2 > 1 && this.canEntityBeSeen(var1) && (double)this.getDistanceToEntity(var1) < 0.6D * (double)var2 && var1.attackEntityFrom(DamageSource.causeMobDamage(this), var2)) {
			this.worldObj.playSoundAtEntity(this, "mob.slimeattack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
		}

	}

	protected String getHurtSound() {
		return "mob.slime";
	}

	protected String getDeathSound() {
		return "mob.slime";
	}

	protected int getDropItemId() {
		return this.getSlimeSize() == 1?Item.slimeBall.shiftedIndex:0;
	}

	public boolean getCanSpawnHere() {
		Chunk var1 = this.worldObj.getChunkFromBlockCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
		return (this.getSlimeSize() == 1 || this.worldObj.difficultySetting > 0) && this.rand.nextInt(10) == 0 && var1.func_997_a(987234911L).nextInt(10) == 0 && this.posY < 16.0D;
	}

	protected float getSoundVolume() {
		return 0.6F;
	}
}
