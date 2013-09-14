package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public abstract class EntityLiving extends EntityLivingBase {

	/** Number of ticks since this EntityLiving last produced its sound */
	public int livingSoundTime;

	/** The experience points the Entity gives. */
	protected int experienceValue;
	private EntityLookHelper lookHelper;
	private EntityMoveHelper moveHelper;

	/** Entity jumping helper */
	private EntityJumpHelper jumpHelper;
	private EntityBodyHelper bodyHelper;
	private PathNavigate navigator;
	protected final EntityAITasks tasks;
	protected final EntityAITasks targetTasks;

	/** The active target the Task system uses for tracking */
	private EntityLivingBase attackTarget;
	private EntitySenses senses;

	/** Equipment (armor and held item) for this entity. */
	private ItemStack[] equipment = new ItemStack[5];

	/** Chances for each equipment piece from dropping when this entity dies. */
	protected float[] equipmentDropChances = new float[5];

	/** Whether this entity can pick up items from the ground. */
	private boolean canPickUpLoot;

	/** Whether this entity should NOT despawn. */
	private boolean persistenceRequired;
	protected float defaultPitch;

	/** This entity's current target. */
	private Entity currentTarget;

	/** How long to keep a specific target entity */
	protected int numTicksToChaseTarget;
	private boolean field_110169_bv;
	private Entity field_110168_bw;
	private NBTTagCompound field_110170_bx;

	public EntityLiving(World par1World) {
		super(par1World);
		this.tasks = new EntityAITasks(par1World != null && par1World.theProfiler != null ? par1World.theProfiler : null);
		this.targetTasks = new EntityAITasks(par1World != null && par1World.theProfiler != null ? par1World.theProfiler : null);
		this.lookHelper = new EntityLookHelper(this);
		this.moveHelper = new EntityMoveHelper(this);
		this.jumpHelper = new EntityJumpHelper(this);
		this.bodyHelper = new EntityBodyHelper(this);
		this.navigator = new PathNavigate(this, par1World);
		this.senses = new EntitySenses(this);

		for (int var2 = 0; var2 < this.equipmentDropChances.length; ++var2) {
			this.equipmentDropChances[var2] = 0.085F;
		}
	}

	protected void func_110147_ax() {
		super.func_110147_ax();
		this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_111265_b).func_111128_a(16.0D);
	}

	public EntityLookHelper getLookHelper() {
		return this.lookHelper;
	}

	public EntityMoveHelper getMoveHelper() {
		return this.moveHelper;
	}

	public EntityJumpHelper getJumpHelper() {
		return this.jumpHelper;
	}

	public PathNavigate getNavigator() {
		return this.navigator;
	}

	/**
	 * returns the EntitySenses Object for the EntityLiving
	 */
	public EntitySenses getEntitySenses() {
		return this.senses;
	}

	/**
	 * Gets the active target the Task system uses for tracking
	 */
	public EntityLivingBase getAttackTarget() {
		return this.attackTarget;
	}

	/**
	 * Sets the active target the Task system uses for tracking
	 */
	public void setAttackTarget(EntityLivingBase par1EntityLivingBase) {
		this.attackTarget = par1EntityLivingBase;
	}

	/**
	 * Returns true if this entity can attack entities of the specified class.
	 */
	public boolean canAttackClass(Class par1Class) {
		return EntityCreeper.class != par1Class && EntityGhast.class != par1Class;
	}

	/**
	 * This function applies the benefits of growing back wool and faster growing up to the acting entity. (This function
	 * is used in the AIEatGrass)
	 */
	public void eatGrassBonus() {}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(11, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(10, "");
	}

	/**
	 * Get number of ticks, at least during which the living entity will be silent.
	 */
	public int getTalkInterval() {
		return 80;
	}

	/**
	 * Plays living's sound at its position
	 */
	public void playLivingSound() {
		String var1 = this.getLivingSound();

		if (var1 != null) {
			this.playSound(var1, this.getSoundVolume(), this.getSoundPitch());
		}
	}

	/**
	 * Gets called every tick from main Entity class
	 */
	public void onEntityUpdate() {
		super.onEntityUpdate();
		this.worldObj.theProfiler.startSection("mobBaseTick");

		if (this.isEntityAlive() && this.rand.nextInt(1000) < this.livingSoundTime++) {
			this.livingSoundTime = -this.getTalkInterval();
			this.playLivingSound();
		}

		this.worldObj.theProfiler.endSection();
	}

	/**
	 * Get the experience points the entity currently has.
	 */
	protected int getExperiencePoints(EntityPlayer par1EntityPlayer) {
		if (this.experienceValue > 0) {
			int var2 = this.experienceValue;
			ItemStack[] var3 = this.getLastActiveItems();

			for (int var4 = 0; var4 < var3.length; ++var4) {
				if (var3[var4] != null && this.equipmentDropChances[var4] <= 1.0F) {
					var2 += 1 + this.rand.nextInt(3);
				}
			}

			return var2;
		} else {
			return this.experienceValue;
		}
	}

	/**
	 * Spawns an explosion particle around the Entity's location
	 */
	public void spawnExplosionParticle() {
		for (int var1 = 0; var1 < 20; ++var1) {
			double var2 = this.rand.nextGaussian() * 0.02D;
			double var4 = this.rand.nextGaussian() * 0.02D;
			double var6 = this.rand.nextGaussian() * 0.02D;
			double var8 = 10.0D;
			this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - var2 * var8, this.posY + (double)(this.rand.nextFloat() * this.height) - var4 * var8, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - var6 * var8, var2, var4, var6);
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();

		if (!this.worldObj.isRemote) {
			this.func_110159_bB();
		}
	}

	protected float func_110146_f(float par1, float par2) {
		if (this.isAIEnabled()) {
			this.bodyHelper.func_75664_a();
			return par2;
		} else {
			return super.func_110146_f(par1, par2);
		}
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return null;
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return 0;
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
	 * par2 - Level of Looting used to kill this mob.
	 */
	protected void dropFewItems(boolean par1, int par2) {
		int var3 = this.getDropItemId();

		if (var3 > 0) {
			int var4 = this.rand.nextInt(3);

			if (par2 > 0) {
				var4 += this.rand.nextInt(par2 + 1);
			}

			for (int var5 = 0; var5 < var4; ++var5) {
				this.dropItem(var3, 1);
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("CanPickUpLoot", this.canPickUpLoot());
		par1NBTTagCompound.setBoolean("PersistenceRequired", this.persistenceRequired);
		NBTTagList var2 = new NBTTagList();
		NBTTagCompound var4;

		for (int var3 = 0; var3 < this.equipment.length; ++var3) {
			var4 = new NBTTagCompound();

			if (this.equipment[var3] != null) {
				this.equipment[var3].writeToNBT(var4);
			}

			var2.appendTag(var4);
		}

		par1NBTTagCompound.setTag("Equipment", var2);
		NBTTagList var6 = new NBTTagList();

		for (int var7 = 0; var7 < this.equipmentDropChances.length; ++var7) {
			var6.appendTag(new NBTTagFloat(var7 + "", this.equipmentDropChances[var7]));
		}

		par1NBTTagCompound.setTag("DropChances", var6);
		par1NBTTagCompound.setString("CustomName", this.getCustomNameTag());
		par1NBTTagCompound.setBoolean("CustomNameVisible", this.func_94062_bN());
		par1NBTTagCompound.setBoolean("Leashed", this.field_110169_bv);

		if (this.field_110168_bw != null) {
			var4 = new NBTTagCompound("Leash");

			if (this.field_110168_bw instanceof EntityLivingBase) {
				var4.setLong("UUIDMost", this.field_110168_bw.func_110124_au().getMostSignificantBits());
				var4.setLong("UUIDLeast", this.field_110168_bw.func_110124_au().getLeastSignificantBits());
			} else if (this.field_110168_bw instanceof EntityHanging) {
				EntityHanging var5 = (EntityHanging)this.field_110168_bw;
				var4.setInteger("X", var5.xPosition);
				var4.setInteger("Y", var5.yPosition);
				var4.setInteger("Z", var5.zPosition);
			}

			par1NBTTagCompound.setTag("Leash", var4);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setCanPickUpLoot(par1NBTTagCompound.getBoolean("CanPickUpLoot"));
		this.persistenceRequired = par1NBTTagCompound.getBoolean("PersistenceRequired");

		if (par1NBTTagCompound.hasKey("CustomName") && par1NBTTagCompound.getString("CustomName").length() > 0) {
			this.func_94058_c(par1NBTTagCompound.getString("CustomName"));
		}

		this.func_94061_f(par1NBTTagCompound.getBoolean("CustomNameVisible"));
		NBTTagList var2;
		int var3;

		if (par1NBTTagCompound.hasKey("Equipment")) {
			var2 = par1NBTTagCompound.getTagList("Equipment");

			for (var3 = 0; var3 < this.equipment.length; ++var3) {
				this.equipment[var3] = ItemStack.loadItemStackFromNBT((NBTTagCompound)var2.tagAt(var3));
			}
		}

		if (par1NBTTagCompound.hasKey("DropChances")) {
			var2 = par1NBTTagCompound.getTagList("DropChances");

			for (var3 = 0; var3 < var2.tagCount(); ++var3) {
				this.equipmentDropChances[var3] = ((NBTTagFloat)var2.tagAt(var3)).data;
			}
		}

		this.field_110169_bv = par1NBTTagCompound.getBoolean("Leashed");

		if (this.field_110169_bv && par1NBTTagCompound.hasKey("Leash")) {
			this.field_110170_bx = par1NBTTagCompound.getCompoundTag("Leash");
		}
	}

	public void setMoveForward(float par1) {
		this.moveForward = par1;
	}

	/**
	 * set the movespeed used for the new AI system
	 */
	public void setAIMoveSpeed(float par1) {
		super.setAIMoveSpeed(par1);
		this.setMoveForward(par1);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use
	 * this to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.worldObj.theProfiler.startSection("looting");

		if (!this.worldObj.isRemote && this.canPickUpLoot() && !this.dead && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
			List var1 = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(1.0D, 0.0D, 1.0D));
			Iterator var2 = var1.iterator();

			while (var2.hasNext()) {
				EntityItem var3 = (EntityItem)var2.next();

				if (!var3.isDead && var3.getEntityItem() != null) {
					ItemStack var4 = var3.getEntityItem();
					int var5 = getArmorPosition(var4);

					if (var5 > -1) {
						boolean var6 = true;
						ItemStack var7 = this.getCurrentItemOrArmor(var5);

						if (var7 != null) {
							if (var5 == 0) {
								if (var4.getItem() instanceof ItemSword && !(var7.getItem() instanceof ItemSword)) {
									var6 = true;
								} else if (var4.getItem() instanceof ItemSword && var7.getItem() instanceof ItemSword) {
									ItemSword var8 = (ItemSword)var4.getItem();
									ItemSword var9 = (ItemSword)var7.getItem();

									if (var8.func_82803_g() == var9.func_82803_g()) {
										var6 = var4.getItemDamage() > var7.getItemDamage() || var4.hasTagCompound() && !var7.hasTagCompound();
									} else {
										var6 = var8.func_82803_g() > var9.func_82803_g();
									}
								} else {
									var6 = false;
								}
							} else if (var4.getItem() instanceof ItemArmor && !(var7.getItem() instanceof ItemArmor)) {
								var6 = true;
							} else if (var4.getItem() instanceof ItemArmor && var7.getItem() instanceof ItemArmor) {
								ItemArmor var10 = (ItemArmor)var4.getItem();
								ItemArmor var11 = (ItemArmor)var7.getItem();

								if (var10.damageReduceAmount == var11.damageReduceAmount) {
									var6 = var4.getItemDamage() > var7.getItemDamage() || var4.hasTagCompound() && !var7.hasTagCompound();
								} else {
									var6 = var10.damageReduceAmount > var11.damageReduceAmount;
								}
							} else {
								var6 = false;
							}
						}

						if (var6) {
							if (var7 != null && this.rand.nextFloat() - 0.1F < this.equipmentDropChances[var5]) {
								this.entityDropItem(var7, 0.0F);
							}

							this.setCurrentItemOrArmor(var5, var4);
							this.equipmentDropChances[var5] = 2.0F;
							this.persistenceRequired = true;
							this.onItemPickup(var3, 1);
							var3.setDead();
						}
					}
				}
			}
		}

		this.worldObj.theProfiler.endSection();
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	protected boolean isAIEnabled() {
		return false;
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	protected boolean canDespawn() {
		return true;
	}

	/**
	 * Makes the entity despawn if requirements are reached
	 */
	protected void despawnEntity() {
		if (this.persistenceRequired) {
			this.entityAge = 0;
		} else {
			EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, -1.0D);

			if (var1 != null) {
				double var2 = var1.posX - this.posX;
				double var4 = var1.posY - this.posY;
				double var6 = var1.posZ - this.posZ;
				double var8 = var2 * var2 + var4 * var4 + var6 * var6;

				if (this.canDespawn() && var8 > 16384.0D) {
					this.setDead();
				}

				if (this.entityAge > 600 && this.rand.nextInt(800) == 0 && var8 > 1024.0D && this.canDespawn()) {
					this.setDead();
				} else if (var8 < 1024.0D) {
					this.entityAge = 0;
				}
			}
		}
	}

	protected void updateAITasks() {
		++this.entityAge;
		this.worldObj.theProfiler.startSection("checkDespawn");
		this.despawnEntity();
		this.worldObj.theProfiler.endSection();
		this.worldObj.theProfiler.startSection("sensing");
		this.senses.clearSensingCache();
		this.worldObj.theProfiler.endSection();
		this.worldObj.theProfiler.startSection("targetSelector");
		this.targetTasks.onUpdateTasks();
		this.worldObj.theProfiler.endSection();
		this.worldObj.theProfiler.startSection("goalSelector");
		this.tasks.onUpdateTasks();
		this.worldObj.theProfiler.endSection();
		this.worldObj.theProfiler.startSection("navigation");
		this.navigator.onUpdateNavigation();
		this.worldObj.theProfiler.endSection();
		this.worldObj.theProfiler.startSection("mob tick");
		this.updateAITick();
		this.worldObj.theProfiler.endSection();
		this.worldObj.theProfiler.startSection("controls");
		this.worldObj.theProfiler.startSection("move");
		this.moveHelper.onUpdateMoveHelper();
		this.worldObj.theProfiler.endStartSection("look");
		this.lookHelper.onUpdateLook();
		this.worldObj.theProfiler.endStartSection("jump");
		this.jumpHelper.doJump();
		this.worldObj.theProfiler.endSection();
		this.worldObj.theProfiler.endSection();
	}

	protected void updateEntityActionState() {
		super.updateEntityActionState();
		this.moveStrafing = 0.0F;
		this.moveForward = 0.0F;
		this.despawnEntity();
		float var1 = 8.0F;

		if (this.rand.nextFloat() < 0.02F) {
			EntityPlayer var2 = this.worldObj.getClosestPlayerToEntity(this, (double)var1);

			if (var2 != null) {
				this.currentTarget = var2;
				this.numTicksToChaseTarget = 10 + this.rand.nextInt(20);
			} else {
				this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
			}
		}

		if (this.currentTarget != null) {
			this.faceEntity(this.currentTarget, 10.0F, (float)this.getVerticalFaceSpeed());

			if (this.numTicksToChaseTarget-- <= 0 || this.currentTarget.isDead || this.currentTarget.getDistanceSqToEntity(this) > (double)(var1 * var1)) {
				this.currentTarget = null;
			}
		} else {
			if (this.rand.nextFloat() < 0.05F) {
				this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
			}

			this.rotationYaw += this.randomYawVelocity;
			this.rotationPitch = this.defaultPitch;
		}

		boolean var4 = this.isInWater();
		boolean var3 = this.handleLavaMovement();

		if (var4 || var3) {
			this.isJumping = this.rand.nextFloat() < 0.8F;
		}
	}

	/**
	 * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
	 * use in wolves.
	 */
	public int getVerticalFaceSpeed() {
		return 40;
	}

	/**
	 * Changes pitch and yaw so that the entity calling the function is facing the entity provided as an argument.
	 */
	public void faceEntity(Entity par1Entity, float par2, float par3) {
		double var4 = par1Entity.posX - this.posX;
		double var8 = par1Entity.posZ - this.posZ;
		double var6;

		if (par1Entity instanceof EntityLivingBase) {
			EntityLivingBase var10 = (EntityLivingBase)par1Entity;
			var6 = var10.posY + (double)var10.getEyeHeight() - (this.posY + (double)this.getEyeHeight());
		} else {
			var6 = (par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / 2.0D - (this.posY + (double)this.getEyeHeight());
		}

		double var14 = (double)MathHelper.sqrt_double(var4 * var4 + var8 * var8);
		float var12 = (float)(Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
		float var13 = (float)(-(Math.atan2(var6, var14) * 180.0D / Math.PI));
		this.rotationPitch = this.updateRotation(this.rotationPitch, var13, par3);
		this.rotationYaw = this.updateRotation(this.rotationYaw, var12, par2);
	}

	/**
	 * Arguments: current rotation, intended rotation, max increment.
	 */
	private float updateRotation(float par1, float par2, float par3) {
		float var4 = MathHelper.wrapAngleTo180_float(par2 - par1);

		if (var4 > par3) {
			var4 = par3;
		}

		if (var4 < -par3) {
			var4 = -par3;
		}

		return par1 + var4;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	public boolean getCanSpawnHere() {
		return this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox);
	}

	/**
	 * Returns render size modifier
	 */
	public float getRenderSizeModifier() {
		return 1.0F;
	}

	/**
	 * Will return how many at most can spawn in a chunk at once.
	 */
	public int getMaxSpawnedInChunk() {
		return 4;
	}

	public int func_82143_as() {
		if (this.getAttackTarget() == null) {
			return 3;
		} else {
			int var1 = (int)(this.func_110143_aJ() - this.func_110138_aP() * 0.33F);
			var1 -= (3 - this.worldObj.difficultySetting) * 4;

			if (var1 < 0) {
				var1 = 0;
			}

			return var1 + 3;
		}
	}

	/**
	 * Returns the item that this EntityLiving is holding, if any.
	 */
	public ItemStack getHeldItem() {
		return this.equipment[0];
	}

	/**
	 * 0 = item, 1-n is armor
	 */
	public ItemStack getCurrentItemOrArmor(int par1) {
		return this.equipment[par1];
	}

	public ItemStack func_130225_q(int var1) {
		return this.equipment[var1 + 1];
	}

	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
	 */
	public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack) {
		this.equipment[par1] = par2ItemStack;
	}

	public ItemStack[] getLastActiveItems() {
		return this.equipment;
	}

	/**
	 * Drop the equipment for this entity.
	 */
	protected void dropEquipment(boolean par1, int par2) {
		for (int var3 = 0; var3 < this.getLastActiveItems().length; ++var3) {
			ItemStack var4 = this.getCurrentItemOrArmor(var3);
			boolean var5 = this.equipmentDropChances[var3] > 1.0F;

			if (var4 != null && (par1 || var5) && this.rand.nextFloat() - (float)par2 * 0.01F < this.equipmentDropChances[var3]) {
				if (!var5 && var4.isItemStackDamageable()) {
					int var6 = Math.max(var4.getMaxDamage() - 25, 1);
					int var7 = var4.getMaxDamage() - this.rand.nextInt(this.rand.nextInt(var6) + 1);

					if (var7 > var6) {
						var7 = var6;
					}

					if (var7 < 1) {
						var7 = 1;
					}

					var4.setItemDamage(var7);
				}

				this.entityDropItem(var4, 0.0F);
			}
		}
	}

	/**
	 * Makes entity wear random armor based on difficulty
	 */
	protected void addRandomArmor() {
		if (this.rand.nextFloat() < 0.15F * this.worldObj.func_110746_b(this.posX, this.posY, this.posZ)) {
			int var1 = this.rand.nextInt(2);
			float var2 = this.worldObj.difficultySetting == 3 ? 0.1F : 0.25F;

			if (this.rand.nextFloat() < 0.095F) {
				++var1;
			}

			if (this.rand.nextFloat() < 0.095F) {
				++var1;
			}

			if (this.rand.nextFloat() < 0.095F) {
				++var1;
			}

			for (int var3 = 3; var3 >= 0; --var3) {
				ItemStack var4 = this.func_130225_q(var3);

				if (var3 < 3 && this.rand.nextFloat() < var2) {
					break;
				}

				if (var4 == null) {
					Item var5 = getArmorItemForSlot(var3 + 1, var1);

					if (var5 != null) {
						this.setCurrentItemOrArmor(var3 + 1, new ItemStack(var5));
					}
				}
			}
		}
	}

	public static int getArmorPosition(ItemStack par0ItemStack) {
		if (par0ItemStack.itemID != Block.pumpkin.blockID && par0ItemStack.itemID != Item.skull.itemID) {
			if (par0ItemStack.getItem() instanceof ItemArmor) {
				switch (((ItemArmor)par0ItemStack.getItem()).armorType) {
					case 0:
						return 4;

					case 1:
						return 3;

					case 2:
						return 2;

					case 3:
						return 1;
				}
			}

			return 0;
		} else {
			return 4;
		}
	}

	/**
	 * Params: Armor slot, Item tier
	 */
	public static Item getArmorItemForSlot(int par0, int par1) {
		switch (par0) {
			case 4:
				if (par1 == 0) {
					return Item.helmetLeather;
				} else if (par1 == 1) {
					return Item.helmetGold;
				} else if (par1 == 2) {
					return Item.helmetChain;
				} else if (par1 == 3) {
					return Item.helmetIron;
				} else if (par1 == 4) {
					return Item.helmetDiamond;
				}

			case 3:
				if (par1 == 0) {
					return Item.plateLeather;
				} else if (par1 == 1) {
					return Item.plateGold;
				} else if (par1 == 2) {
					return Item.plateChain;
				} else if (par1 == 3) {
					return Item.plateIron;
				} else if (par1 == 4) {
					return Item.plateDiamond;
				}

			case 2:
				if (par1 == 0) {
					return Item.legsLeather;
				} else if (par1 == 1) {
					return Item.legsGold;
				} else if (par1 == 2) {
					return Item.legsChain;
				} else if (par1 == 3) {
					return Item.legsIron;
				} else if (par1 == 4) {
					return Item.legsDiamond;
				}

			case 1:
				if (par1 == 0) {
					return Item.bootsLeather;
				} else if (par1 == 1) {
					return Item.bootsGold;
				} else if (par1 == 2) {
					return Item.bootsChain;
				} else if (par1 == 3) {
					return Item.bootsIron;
				} else if (par1 == 4) {
					return Item.bootsDiamond;
				}

			default:
				return null;
		}
	}

	/**
	 * Enchants the entity's armor and held item based on difficulty
	 */
	protected void enchantEquipment() {
		float var1 = this.worldObj.func_110746_b(this.posX, this.posY, this.posZ);

		if (this.getHeldItem() != null && this.rand.nextFloat() < 0.25F * var1) {
			EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItem(), (int)(5.0F + var1 * (float)this.rand.nextInt(18)));
		}

		for (int var2 = 0; var2 < 4; ++var2) {
			ItemStack var3 = this.func_130225_q(var2);

			if (var3 != null && this.rand.nextFloat() < 0.5F * var1) {
				EnchantmentHelper.addRandomEnchantment(this.rand, var3, (int)(5.0F + var1 * (float)this.rand.nextInt(18)));
			}
		}
	}

	public EntityLivingData func_110161_a(EntityLivingData par1EntityLivingData) {
		this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111121_a(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
		return par1EntityLivingData;
	}

	/**
	 * returns true if all the conditions for steering the entity are met. For pigs, this is true if it is being ridden by
	 * a player and the player is holding a carrot-on-a-stick
	 */
	public boolean canBeSteered() {
		return false;
	}

	/**
	 * Gets the username of the entity.
	 */
	public String getEntityName() {
		return this.hasCustomNameTag() ? this.getCustomNameTag() : super.getEntityName();
	}

	public void func_110163_bv() {
		this.persistenceRequired = true;
	}

	public void func_94058_c(String par1Str) {
		this.dataWatcher.updateObject(10, par1Str);
	}

	public String getCustomNameTag() {
		return this.dataWatcher.getWatchableObjectString(10);
	}

	public boolean hasCustomNameTag() {
		return this.dataWatcher.getWatchableObjectString(10).length() > 0;
	}

	public void func_94061_f(boolean par1) {
		this.dataWatcher.updateObject(11, Byte.valueOf((byte)(par1 ? 1 : 0)));
	}

	public boolean func_94062_bN() {
		return this.dataWatcher.getWatchableObjectByte(11) == 1;
	}

	public boolean getAlwaysRenderNameTagForRender() {
		return this.func_94062_bN();
	}

	public void setEquipmentDropChance(int par1, float par2) {
		this.equipmentDropChances[par1] = par2;
	}

	public boolean canPickUpLoot() {
		return this.canPickUpLoot;
	}

	public void setCanPickUpLoot(boolean par1) {
		this.canPickUpLoot = par1;
	}

	public boolean func_104002_bU() {
		return this.persistenceRequired;
	}

	public final boolean func_130002_c(EntityPlayer par1EntityPlayer) {
		if (this.func_110167_bD() && this.func_110166_bE() == par1EntityPlayer) {
			this.func_110160_i(true, !par1EntityPlayer.capabilities.isCreativeMode);
			return true;
		} else {
			ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();

			if (var2 != null && var2.itemID == Item.field_111214_ch.itemID && this.func_110164_bC()) {
				if (!(this instanceof EntityTameable) || !((EntityTameable)this).isTamed()) {
					this.func_110162_b(par1EntityPlayer, true);
					--var2.stackSize;
					return true;
				}

				if (par1EntityPlayer.getCommandSenderName().equalsIgnoreCase(((EntityTameable)this).getOwnerName())) {
					this.func_110162_b(par1EntityPlayer, true);
					--var2.stackSize;
					return true;
				}
			}

			return this.interact(par1EntityPlayer) ? true : super.func_130002_c(par1EntityPlayer);
		}
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */
	protected boolean interact(EntityPlayer par1EntityPlayer) {
		return false;
	}

	protected void func_110159_bB() {
		if (this.field_110170_bx != null) {
			this.func_110165_bF();
		}

		if (this.field_110169_bv) {
			if (this.field_110168_bw == null || this.field_110168_bw.isDead) {
				this.func_110160_i(true, true);
			}
		}
	}

	public void func_110160_i(boolean par1, boolean par2) {
		if (this.field_110169_bv) {
			this.field_110169_bv = false;
			this.field_110168_bw = null;

			if (!this.worldObj.isRemote && par2) {
				this.dropItem(Item.field_111214_ch.itemID, 1);
			}

			if (!this.worldObj.isRemote && par1 && this.worldObj instanceof WorldServer) {
				((WorldServer)this.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet39AttachEntity(1, this, (Entity)null));
			}
		}
	}

	public boolean func_110164_bC() {
		return !this.func_110167_bD() && !(this instanceof IMob);
	}

	public boolean func_110167_bD() {
		return this.field_110169_bv;
	}

	public Entity func_110166_bE() {
		return this.field_110168_bw;
	}

	public void func_110162_b(Entity par1Entity, boolean par2) {
		this.field_110169_bv = true;
		this.field_110168_bw = par1Entity;

		if (!this.worldObj.isRemote && par2 && this.worldObj instanceof WorldServer) {
			((WorldServer)this.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet39AttachEntity(1, this, this.field_110168_bw));
		}
	}

	private void func_110165_bF() {
		if (this.field_110169_bv && this.field_110170_bx != null) {
			if (this.field_110170_bx.hasKey("UUIDMost") && this.field_110170_bx.hasKey("UUIDLeast")) {
				UUID var5 = new UUID(this.field_110170_bx.getLong("UUIDMost"), this.field_110170_bx.getLong("UUIDLeast"));
				List var6 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.boundingBox.expand(10.0D, 10.0D, 10.0D));
				Iterator var7 = var6.iterator();

				while (var7.hasNext()) {
					EntityLivingBase var8 = (EntityLivingBase)var7.next();

					if (var8.func_110124_au().equals(var5)) {
						this.field_110168_bw = var8;
						break;
					}
				}
			} else if (this.field_110170_bx.hasKey("X") && this.field_110170_bx.hasKey("Y") && this.field_110170_bx.hasKey("Z")) {
				int var1 = this.field_110170_bx.getInteger("X");
				int var2 = this.field_110170_bx.getInteger("Y");
				int var3 = this.field_110170_bx.getInteger("Z");
				EntityLeashKnot var4 = EntityLeashKnot.func_110130_b(this.worldObj, var1, var2, var3);

				if (var4 == null) {
					var4 = EntityLeashKnot.func_110129_a(this.worldObj, var1, var2, var3);
				}

				this.field_110168_bw = var4;
			} else {
				this.func_110160_i(false, true);
			}
		}

		this.field_110170_bx = null;
	}
}
