package net.minecraft.src;

import org.spoutcraft.client.entity.CraftSlime; // Spout

public class EntitySlime extends EntityLiving implements IMob {
	public float field_70813_a;
	public float field_70811_b;
	public float field_70812_c;

	/** the time between each jump of the slime */
	private int slimeJumpDelay = 0;

	public EntitySlime(World par1World) {
		super(par1World);
		this.texture = "/mob/slime.png";
		int var2 = 1 << this.rand.nextInt(3);
		this.yOffset = 0.0F;
		this.slimeJumpDelay = this.rand.nextInt(20) + 10;
		this.setSlimeSize(var2);
		// Spout Start
		this.spoutEntity = new CraftSlime(this);
		// Spout End
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte)1));
	}

	public void setSlimeSize(int par1) { // Spout protected -> public
		this.dataWatcher.updateObject(16, new Byte((byte)par1));
		this.setSize(0.6F * (float)par1, 0.6F * (float)par1);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.setEntityHealth(this.getMaxHealth());
		this.experienceValue = par1;
	}

	public int getMaxHealth() {
		int var1 = this.getSlimeSize();
		return var1 * var1;
	}

	/**
	 * Returns the size of the slime.
	 */
	public int getSlimeSize() {
		return this.dataWatcher.getWatchableObjectByte(16);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("Size", this.getSlimeSize() - 1);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setSlimeSize(par1NBTTagCompound.getInteger("Size") + 1);
	}

	/**
	 * Returns the name of a particle effect that may be randomly created by EntitySlime.onUpdate()
	 */
	protected String getSlimeParticle() {
		return "slime";
	}

	/**
	 * Returns the name of the sound played when the slime jumps.
	 */
	protected String getJumpSound() {
		return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		if (!this.worldObj.isRemote && this.worldObj.difficultySetting == 0 && this.getSlimeSize() > 0) {
			this.isDead = true;
		}

		this.field_70811_b += (this.field_70813_a - this.field_70811_b) * 0.5F;
		this.field_70812_c = this.field_70811_b;
		boolean var1 = this.onGround;
		super.onUpdate();
		int var2;

		if (this.onGround && !var1) {
			var2 = this.getSlimeSize();

			for (int var3 = 0; var3 < var2 * 8; ++var3) {
				float var4 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
				float var5 = this.rand.nextFloat() * 0.5F + 0.5F;
				float var6 = MathHelper.sin(var4) * (float)var2 * 0.5F * var5;
				float var7 = MathHelper.cos(var4) * (float)var2 * 0.5F * var5;
				this.worldObj.spawnParticle(this.getSlimeParticle(), this.posX + (double)var6, this.boundingBox.minY, this.posZ + (double)var7, 0.0D, 0.0D, 0.0D);
			}

			if (this.makesSoundOnLand()) {
				this.func_85030_a(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			}

			this.field_70813_a = -0.5F;
		} else if (!this.onGround && var1) {
			this.field_70813_a = 1.0F;
		}

		this.func_70808_l();

		if (this.worldObj.isRemote) {
			var2 = this.getSlimeSize();
			this.setSize(0.6F * (float)var2, 0.6F * (float)var2);
		}

	}

	protected void updateEntityActionState() {
		this.despawnEntity();
		EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);

		if (var1 != null) {
			this.faceEntity(var1, 10.0F, 20.0F);
		}

		if (this.onGround && this.slimeJumpDelay-- <= 0) {
			this.slimeJumpDelay = this.getJumpDelay();

			if (var1 != null) {
				this.slimeJumpDelay /= 3;
			}

			this.isJumping = true;

			if (this.makesSoundOnJump()) {
				this.func_85030_a(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
			}

			this.moveStrafing = 1.0F - this.rand.nextFloat() * 2.0F;
			this.moveForward = (float)(1 * this.getSlimeSize());
		} else {
			this.isJumping = false;

			if (this.onGround) {
				this.moveStrafing = this.moveForward = 0.0F;
			}
		}
	}

	protected void func_70808_l() {
		this.field_70813_a *= 0.6F;
	}

	/**
	 * Gets the amount of time the slime needs to wait between jumps.
	 */
	protected int getJumpDelay() {
		return this.rand.nextInt(20) + 10;
	}

	protected EntitySlime createInstance() {
		return new EntitySlime(this.worldObj);
	}

	/**
	 * Will get destroyed next tick.
	 */
	public void setDead() {
		int var1 = this.getSlimeSize();

		if (!this.worldObj.isRemote && var1 > 1 && this.getHealth() <= 0) {
			int var2 = 2 + this.rand.nextInt(3);

			for (int var3 = 0; var3 < var2; ++var3) {
				float var4 = ((float)(var3 % 2) - 0.5F) * (float)var1 / 4.0F;
				float var5 = ((float)(var3 / 2) - 0.5F) * (float)var1 / 4.0F;
				EntitySlime var6 = this.createInstance();
				var6.setSlimeSize(var1 / 2);
				var6.setLocationAndAngles(this.posX + (double)var4, this.posY + 0.5D, this.posZ + (double)var5, this.rand.nextFloat() * 360.0F, 0.0F);
				this.worldObj.spawnEntityInWorld(var6);
			}
		}

		super.setDead();
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
		if (this.canDamagePlayer()) {
			int var2 = this.getSlimeSize();

			if (this.canEntityBeSeen(par1EntityPlayer) && this.getDistanceSqToEntity(par1EntityPlayer) < 0.6D * (double)var2 * 0.6D * (double)var2 && par1EntityPlayer.attackEntityFrom(DamageSource.causeMobDamage(this), this.getAttackStrength())) {
				this.func_85030_a("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}
		}
	}

	/**
	 * Indicates weather the slime is able to damage the player (based upon the slime's size)
	 */
	protected boolean canDamagePlayer() {
		return this.getSlimeSize() > 1;
	}

	/**
	 * Gets the amount of damage dealt to the player when "attacked" by the slime.
	 */
	protected int getAttackStrength() {
		return this.getSlimeSize();
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return this.getSlimeSize() == 1 ? Item.slimeBall.shiftedIndex : 0;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	public boolean getCanSpawnHere() {
		Chunk var1 = this.worldObj.getChunkFromBlockCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));

		if (this.worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && this.rand.nextInt(4) != 1) {
			return false;
		} else {
			if (this.getSlimeSize() == 1 || this.worldObj.difficultySetting > 0) {
				if (this.worldObj.getBiomeGenForCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ)) == BiomeGenBase.swampland && this.posY > 50.0D && this.posY < 70.0D && this.worldObj.getBlockLightValue(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) <= this.rand.nextInt(8)) {
					return super.getCanSpawnHere();
				}

				if (this.rand.nextInt(10) == 0 && var1.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0D) {
					return super.getCanSpawnHere();
				}
			}

			return false;
		}
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume() {
		return 0.4F * (float)this.getSlimeSize();
	}

	/**
	 * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
	 * use in wolves.
	 */
	public int getVerticalFaceSpeed() {
		return 0;
	}

	/**
	 * Returns true if the slime makes a sound when it jumps (based upon the slime's size)
	 */
	protected boolean makesSoundOnJump() {
		return this.getSlimeSize() > 0;
	}

	/**
	 * Returns true if the slime makes a sound when it lands after a jump (based upon the slime's size)
	 */
	protected boolean makesSoundOnLand() {
		return this.getSlimeSize() > 2;
	}
}
