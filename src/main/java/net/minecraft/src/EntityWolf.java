package net.minecraft.src;

// Spout Start
import org.spoutcraft.api.entity.EntitySkinType;
// Spout End

public class EntityWolf extends EntityTameable {
	private float field_70926_e;
	private float field_70924_f;

	/** true is the wolf is wet else false */
	private boolean isShaking;
	private boolean field_70928_h;

	/**
	 * This time increases while wolf is shaking and emitting water particles.
	 */
	private float timeWolfIsShaking;
	private float prevTimeWolfIsShaking;

	public EntityWolf(World par1World) {
		super(par1World);
		this.texture = "/mob/wolf.png";
		this.setSize(0.6F, 0.8F);
		this.moveSpeed = 0.3F;
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, this.moveSpeed, true));
		this.tasks.addTask(5, new EntityAIFollowOwner(this, this.moveSpeed, 10.0F, 2.0F));
		this.tasks.addTask(6, new EntityAIMate(this, this.moveSpeed));
		this.tasks.addTask(7, new EntityAIWander(this, this.moveSpeed));
		this.tasks.addTask(8, new EntityAIBeg(this, 8.0F));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(9, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntitySheep.class, 16.0F, 200, false));
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	/**
	 * Sets the active target the Task system uses for tracking
	 */
	public void setAttackTarget(EntityLiving par1EntityLiving) {
		super.setAttackTarget(par1EntityLiving);

		if (par1EntityLiving instanceof EntityPlayer) {
			this.setAngry(true);
		}
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	protected void updateAITick() {
		this.dataWatcher.updateObject(18, Integer.valueOf(this.getHealth()));
	}

	public int getMaxHealth() {
		return this.isTamed() ? 20 : 8;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(18, new Integer(this.getHealth()));
		this.dataWatcher.addObject(19, new Byte((byte)0));
		this.dataWatcher.addObject(20, new Byte((byte)BlockCloth.getBlockFromDye(1)));
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	protected void playStepSound(int par1, int par2, int par3, int par4) {
		this.playSound("mob.wolf.step", 0.15F, 1.0F);
	}

	/**
	 * Returns the texture's file path as a String.
	 */
	public String getTexture() {
		// Spout Start
		if (isTamed()) {
			return this.getCustomTexture(EntitySkinType.WOLF_TAMED, "/mob/wolf_tame.png");
		}
		if (isAngry()) {
			return this.getCustomTexture(EntitySkinType.WOLF_ANGRY, "/mob/wolf_angry.png");
		} else {
			return super.getTexture();
		}
		// Spout End
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("Angry", this.isAngry());
		par1NBTTagCompound.setByte("CollarColor", (byte)this.getCollarColor());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setAngry(par1NBTTagCompound.getBoolean("Angry"));

		if (par1NBTTagCompound.hasKey("CollarColor")) {
			this.setCollarColor(par1NBTTagCompound.getByte("CollarColor"));
		}
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	protected boolean canDespawn() {
		return this.isAngry();
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return this.isAngry() ? "mob.wolf.growl" : (this.rand.nextInt(3) == 0 ? (this.isTamed() && this.dataWatcher.getWatchableObjectInt(18) < 10 ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.wolf.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.wolf.death";
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume() {
		return 0.4F;
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return -1;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use
	 * this to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (!this.worldObj.isRemote && this.isShaking && !this.field_70928_h && !this.hasPath() && this.onGround) {
			this.field_70928_h = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
			this.worldObj.setEntityState(this, (byte)8);
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
		this.field_70924_f = this.field_70926_e;

		if (this.func_70922_bv()) {
			this.field_70926_e += (1.0F - this.field_70926_e) * 0.4F;
		} else {
			this.field_70926_e += (0.0F - this.field_70926_e) * 0.4F;
		}

		if (this.func_70922_bv()) {
			this.numTicksToChaseTarget = 10;
		}

		if (this.isWet()) {
			this.isShaking = true;
			this.field_70928_h = false;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		} else if ((this.isShaking || this.field_70928_h) && this.field_70928_h) {
			if (this.timeWolfIsShaking == 0.0F) {
				this.playSound("mob.wolf.shake", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}

			this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
			this.timeWolfIsShaking += 0.05F;

			if (this.prevTimeWolfIsShaking >= 2.0F) {
				this.isShaking = false;
				this.field_70928_h = false;
				this.prevTimeWolfIsShaking = 0.0F;
				this.timeWolfIsShaking = 0.0F;
			}

			if (this.timeWolfIsShaking > 0.4F) {
				float var1 = (float)this.boundingBox.minY;
				int var2 = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);

				for (int var3 = 0; var3 < var2; ++var3) {
					float var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					float var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					this.worldObj.spawnParticle("splash", this.posX + (double)var4, (double)(var1 + 0.8F), this.posZ + (double)var5, this.motionX, this.motionY, this.motionZ);
				}
			}
		}
	}

	public boolean getWolfShaking() {
		return this.isShaking;
	}

	/**
	 * Used when calculating the amount of shading to apply while the wolf is shaking.
	 */
	public float getShadingWhileShaking(float par1) {
		return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * par1) / 2.0F * 0.25F;
	}

	public float getShakeAngle(float par1, float par2) {
		float var3 = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * par1 + par2) / 1.8F;

		if (var3 < 0.0F) {
			var3 = 0.0F;
		} else if (var3 > 1.0F) {
			var3 = 1.0F;
		}

		return MathHelper.sin(var3 * (float)Math.PI) * MathHelper.sin(var3 * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
	}

	public float getInterestedAngle(float par1) {
		return (this.field_70924_f + (this.field_70926_e - this.field_70924_f) * par1) * 0.15F * (float)Math.PI;
	}

	public float getEyeHeight() {
		return this.height * 0.8F;
	}

	/**
	 * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
	 * use in wolves.
	 */
	public int getVerticalFaceSpeed() {
		return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		if (this.isEntityInvulnerable()) {
			return false;
		} else {
			Entity var3 = par1DamageSource.getEntity();
			this.aiSit.setSitting(false);

			if (var3 != null && !(var3 instanceof EntityPlayer) && !(var3 instanceof EntityArrow)) {
				par2 = (par2 + 1) / 2;
			}

			return super.attackEntityFrom(par1DamageSource, par2);
		}
	}

	public boolean attackEntityAsMob(Entity par1Entity) {
		int var2 = this.isTamed() ? 4 : 2;
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), var2);
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer par1EntityPlayer) {
		ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();

		if (this.isTamed()) {
			if (var2 != null) {
				if (Item.itemsList[var2.itemID] instanceof ItemFood) {
					ItemFood var3 = (ItemFood)Item.itemsList[var2.itemID];

					if (var3.isWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectInt(18) < 20) {
						if (!par1EntityPlayer.capabilities.isCreativeMode) {
							--var2.stackSize;
						}

						this.heal(var3.getHealAmount());

						if (var2.stackSize <= 0) {
							par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
						}

						return true;
					}
				} else if (var2.itemID == Item.dyePowder.itemID) {
					int var4 = BlockCloth.getBlockFromDye(var2.getItemDamage());

					if (var4 != this.getCollarColor()) {
						this.setCollarColor(var4);

						if (!par1EntityPlayer.capabilities.isCreativeMode && --var2.stackSize <= 0) {
							par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
						}

						return true;
					}
				}
			}

			if (par1EntityPlayer.username.equalsIgnoreCase(this.getOwnerName()) && !this.worldObj.isRemote && !this.isBreedingItem(var2)) {
				this.aiSit.setSitting(!this.isSitting());
				this.isJumping = false;
				this.setPathToEntity((PathEntity)null);
			}
		} else if (var2 != null && var2.itemID == Item.bone.itemID && !this.isAngry()) {
			if (!par1EntityPlayer.capabilities.isCreativeMode) {
				--var2.stackSize;
			}

			if (var2.stackSize <= 0) {
				par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
			}

			if (!this.worldObj.isRemote) {
				if (this.rand.nextInt(3) == 0) {
					this.setTamed(true);
					this.setPathToEntity((PathEntity)null);
					this.setAttackTarget((EntityLiving)null);
					this.aiSit.setSitting(true);
					this.setEntityHealth(20);
					this.setOwner(par1EntityPlayer.username);
					this.playTameEffect(true);
					this.worldObj.setEntityState(this, (byte)7);
				} else {
					this.playTameEffect(false);
					this.worldObj.setEntityState(this, (byte)6);
				}
			}

			return true;
		}

		return super.interact(par1EntityPlayer);
	}

	public void handleHealthUpdate(byte par1) {
		if (par1 == 8) {
			this.field_70928_h = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		} else {
			super.handleHealthUpdate(par1);
		}
	}

	public float getTailRotation() {
		return this.isAngry() ? 1.5393804F : (this.isTamed() ? (0.55F - (float)(20 - this.dataWatcher.getWatchableObjectInt(18)) * 0.02F) * (float)Math.PI : ((float)Math.PI / 5F));
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
	 * the animal type)
	 */
	public boolean isBreedingItem(ItemStack par1ItemStack) {
		return par1ItemStack == null ? false : (!(Item.itemsList[par1ItemStack.itemID] instanceof ItemFood) ? false : ((ItemFood)Item.itemsList[par1ItemStack.itemID]).isWolfsFavoriteMeat());
	}

	/**
	 * Will return how many at most can spawn in a chunk at once.
	 */
	public int getMaxSpawnedInChunk() {
		return 8;
	}

	/**
	 * Determines whether this wolf is angry or not.
	 */
	public boolean isAngry() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
	}

	/**
	 * Sets whether this wolf is angry or not.
	 */
	public void setAngry(boolean par1) {
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);

		if (par1) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 2)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -3)));
		}
	}

	/**
	 * Return this wolf's collar color.
	 */
	public int getCollarColor() {
		return this.dataWatcher.getWatchableObjectByte(20) & 15;
	}

	/**
	 * Set this wolf's collar color.
	 */
	public void setCollarColor(int par1) {
		this.dataWatcher.updateObject(20, Byte.valueOf((byte)(par1 & 15)));
	}

	/**
	 * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
	 */
	public EntityWolf spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
		EntityWolf var2 = new EntityWolf(this.worldObj);
		String var3 = this.getOwnerName();

		if (var3 != null && var3.trim().length() > 0) {
			var2.setOwner(var3);
			var2.setTamed(true);
		}

		return var2;
	}

	public void func_70918_i(boolean par1) {
		byte var2 = this.dataWatcher.getWatchableObjectByte(19);

		if (par1) {
			this.dataWatcher.updateObject(19, Byte.valueOf((byte)1));
		} else {
			this.dataWatcher.updateObject(19, Byte.valueOf((byte)0));
		}
	}

	/**
	 * Returns true if the mob is currently able to mate with the specified mob.
	 */
	public boolean canMateWith(EntityAnimal par1EntityAnimal) {
		if (par1EntityAnimal == this) {
			return false;
		} else if (!this.isTamed()) {
			return false;
		} else if (!(par1EntityAnimal instanceof EntityWolf)) {
			return false;
		} else {
			EntityWolf var2 = (EntityWolf)par1EntityAnimal;
			return !var2.isTamed() ? false : (var2.isSitting() ? false : this.isInLove() && var2.isInLove());
		}
	}

	public boolean func_70922_bv() {
		return this.dataWatcher.getWatchableObjectByte(19) == 1;
	}

	public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
		return this.spawnBabyAnimal(par1EntityAgeable);
	}
}
