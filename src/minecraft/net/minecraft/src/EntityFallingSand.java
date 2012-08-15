package net.minecraft.src;

import org.spoutcraft.client.entity.CraftFallingSand;

public class EntityFallingSand extends Entity {
	public int blockID;
	public int field_70285_b;
	public int fallTime = 0;
	public boolean field_70284_d;

	public EntityFallingSand(World par1World) {
		super(par1World);
		this.fallTime = 0;
		this.field_70284_d = true;
	}

	public EntityFallingSand(World par1World, double par2, double par4, double par6, int par8) {
		this(par1World, par2, par4, par6, par8, 0);
	}

	public EntityFallingSand(World par1World, double par2, double par4, double par6, int par8, int par9) {
		super(par1World);
		this.fallTime = 0;
		this.field_70284_d = true;
		this.blockID = par8;
		this.field_70285_b = par9;
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(par2, par4, par6);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = par2;
		this.prevPosY = par4;
		this.prevPosZ = par6;
		//Spout start
		this.spoutEntity = new CraftFallingSand(this);
		//Spout end
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public void onUpdate() {
		if (this.blockID == 0) {
			this.setDead();
		} else {
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			++this.fallTime;
			this.motionY -= 0.03999999910593033D;
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.9800000190734863D;
			this.motionY *= 0.9800000190734863D;
			this.motionZ *= 0.9800000190734863D;
			int var1 = MathHelper.floor_double(this.posX);
			int var2 = MathHelper.floor_double(this.posY);
			int var3 = MathHelper.floor_double(this.posZ);
			if (this.fallTime == 1 && this.worldObj.getBlockId(var1, var2, var3) == this.blockID) {
				this.worldObj.setBlockWithNotify(var1, var2, var3, 0);
			} else if (!this.worldObj.isRemote && this.fallTime == 1) {
				this.setDead();
			}

			if (this.onGround) {
				this.motionX *= 0.699999988079071D;
				this.motionZ *= 0.699999988079071D;
				this.motionY *= -0.5D;
				if (this.worldObj.getBlockId(var1, var2, var3) != Block.pistonMoving.blockID) {
					this.setDead();
					if ((!this.worldObj.func_72931_a(this.blockID, var1, var2, var3, true, 1, (Entity)null) || BlockSand.canFallBelow(this.worldObj, var1, var2 - 1, var3) || !this.worldObj.setBlockAndMetadataWithNotify(var1, var2, var3, this.blockID, this.field_70285_b)) && !this.worldObj.isRemote && this.field_70284_d) {
						this.dropItem(this.blockID, 1);
					}
				}
			} else if (this.fallTime > 100 && !this.worldObj.isRemote && (var2 < 1 || var2 > 256) || this.fallTime > 600) {
				this.dropItem(this.blockID, 1);
				this.setDead();
			}
		}
	}

	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setByte("Tile", (byte)this.blockID);
		par1NBTTagCompound.setByte("Data", (byte)this.field_70285_b);
		par1NBTTagCompound.setByte("Time", (byte)this.fallTime);
		par1NBTTagCompound.setBoolean("DropItem", this.field_70284_d);
	}

	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		this.blockID = par1NBTTagCompound.getByte("Tile") & 255;
		this.field_70285_b = par1NBTTagCompound.getByte("Data") & 255;
		this.fallTime = par1NBTTagCompound.getByte("Time") & 255;

		if (par1NBTTagCompound.hasKey("DropItem")) {
			this.field_70284_d = par1NBTTagCompound.getBoolean("DropItem");
		}

		if (this.blockID == 0) {
			this.blockID = Block.sand.blockID;
		}
	}

	public float getShadowSize() {
		return 0.0F;
	}

	public World getWorld() {
		return this.worldObj;
	}
}
