package net.minecraft.src;

import org.spoutcraft.client.entity.CraftTNTPrimed;

import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntityTNTPrimed extends Entity {

	public int fuse;
	//Spout start
	public float yield = 4F;
	public boolean incendiary = true;
	//Spout end

	public EntityTNTPrimed(World par1World) {
		super(par1World);
		this.fuse = 0;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
		//Spout start
		this.spoutEntity = new CraftTNTPrimed(this);
		//Spout end
	}

	public EntityTNTPrimed(World par1World, double par2, double par4, double par6) {
		this(par1World);
		this.setPosition(par2, par4, par6);
		float var8 = (float)(Math.random() * 3.1415927410125732D * 2.0D);
		this.motionX = (double)(-((float)Math.sin((double)var8)) * 0.02F);
		this.motionY = 0.20000000298023224D;
		this.motionZ = (double)(-((float)Math.cos((double)var8)) * 0.02F);
		this.fuse = 80;
		this.prevPosX = par2;
		this.prevPosY = par4;
		this.prevPosZ = par6;
	}

	protected void entityInit() {}

	protected boolean canTriggerWalking() {
		return false;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= 0.03999999910593033D;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;
		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
			this.motionY *= -0.5D;
		}

		if (this.fuse-- <= 0) {
			if (!this.worldObj.isRemote) {
				this.setEntityDead();
				this.explode();
			} else {
				this.setEntityDead();
			}
		} else {
			this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
		}

	}

	private void explode() {
		//Spout start
		this.worldObj.newExplosion((Entity)null, this.posX, this.posY, this.posZ, yield, incendiary);
		//Spout end
	}

	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setByte("Fuse", (byte)this.fuse);
	}

	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		this.fuse = par1NBTTagCompound.getByte("Fuse");
	}

	public float getShadowSize() {
		return 0.0F;
	}
}
