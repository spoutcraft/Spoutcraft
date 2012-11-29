package net.minecraft.src;

import java.util.Calendar;

import org.spoutcraft.client.entity.CraftSkeleton; // Spout

public class EntitySkeleton extends EntityMob implements IRangedAttackMob {
	private EntityAIArrowAttack field_85037_d = new EntityAIArrowAttack(this, 0.25F, 60, 10.0F);
	private EntityAIAttackOnCollide field_85038_e = new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.31F, false);

	public EntitySkeleton(World par1World) {
		super(par1World);
		this.texture = "/mob/skeleton.png";
		this.moveSpeed = 0.25F;
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(3, new EntityAIFleeSun(this, this.moveSpeed));
		this.tasks.addTask(5, new EntityAIWander(this, this.moveSpeed));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));

		if (par1World != null && !par1World.isRemote) {
			this.func_85036_m();
		}

		// Spout Start
		this.spoutEntity = new CraftSkeleton(this);
		// Spout End
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(13, new Byte((byte)0));
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	public int getMaxHealth() {
		return 20;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.skeleton.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.skeleton.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.skeleton.death";
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	protected void playStepSound(int par1, int par2, int par3, int par4) {
		this.func_85030_a("mob.skeleton.step", 0.15F, 1.0F);
	}

	public boolean attackEntityAsMob(Entity par1Entity) {
		if (super.attackEntityAsMob(par1Entity)) {
			if (this.getSkeletonType() == 1 && par1Entity instanceof EntityLiving) {
				((EntityLiving)par1Entity).addPotionEffect(new PotionEffect(Potion.wither.id, 200));
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the amount of damage a mob should deal.
	 */
	public int getAttackStrength(Entity par1Entity) {
		if (this.getSkeletonType() == 1) {
			ItemStack var2 = this.getHeldItem();
			int var3 = 4;

			if (var2 != null) {
				var3 += var2.getDamageVsEntity(this);
			}

			return var3;
		} else {
			return super.getAttackStrength(par1Entity);
		}
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use
	 * this to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		if (this.worldObj.isDaytime() && !this.worldObj.isRemote) {
			float var1 = this.getBrightness(1.0F);

			if (var1 > 0.5F && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))) {
				boolean var2 = true;
				ItemStack var3 = this.getCurrentItemOrArmor(4);

				if (var3 != null) {
					if (var3.isItemStackDamageable()) {
						var3.setItemDamage(var3.getItemDamageForDisplay() + this.rand.nextInt(2));

						if (var3.getItemDamageForDisplay() >= var3.getMaxDamage()) {
							this.renderBrokenItemStack(var3);
							this.setCurrentItemOrArmor(4, (ItemStack)null);
						}
					}

					var2 = false;
				}

				if (var2) {
					this.setFire(8);
				}
			}
		}

		super.onLivingUpdate();
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);

		if (par1DamageSource.getSourceOfDamage() instanceof EntityArrow && par1DamageSource.getEntity() instanceof EntityPlayer) {
			EntityPlayer var2 = (EntityPlayer)par1DamageSource.getEntity();
			double var3 = var2.posX - this.posX;
			double var5 = var2.posZ - this.posZ;

			if (var3 * var3 + var5 * var5 >= 2500.0D) {
				var2.triggerAchievement(AchievementList.snipeSkeleton);
			}
		}
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return Item.arrow.shiftedIndex;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean par1, int par2) {
		int var3;
		int var4;

		if (this.getSkeletonType() == 1) {
			var3 = this.rand.nextInt(3 + par2) - 1;

			for (var4 = 0; var4 < var3; ++var4) {
				this.dropItem(Item.coal.shiftedIndex, 1);
			}
		} else {
			var3 = this.rand.nextInt(3 + par2);

			for (var4 = 0; var4 < var3; ++var4) {
				this.dropItem(Item.arrow.shiftedIndex, 1);
			}
		}

		var3 = this.rand.nextInt(3 + par2);

		for (var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.bone.shiftedIndex, 1);
		}
	}

	protected void dropRareDrop(int par1) {
		if (this.getSkeletonType() == 1) {
			this.entityDropItem(new ItemStack(Item.skull.shiftedIndex, 1, 1), 0.0F);
		}
	}

	protected void func_82164_bB() {
		super.func_82164_bB();
		this.setCurrentItemOrArmor(0, new ItemStack(Item.bow));
	}

	/**
	 * Returns the texture's file path as a String.
	 */
	public String getTexture() {
		return this.getSkeletonType() == 1 ? "/mob/skeleton_wither.png" : super.getTexture();
	}

	/**
	 * Initialize this creature.
	 */
	public void initCreature() {
		if (this.worldObj.provider instanceof WorldProviderHell && this.getRNG().nextInt(5) > 0) {
			this.tasks.addTask(4, this.field_85038_e);
			this.setSkeletonType(1);
			this.setCurrentItemOrArmor(0, new ItemStack(Item.swordStone));
		} else {
			this.tasks.addTask(4, this.field_85037_d);
			this.func_82164_bB();
			this.func_82162_bC();
		}

		if (this.rand.nextFloat() >= field_82181_as[this.worldObj.difficultySetting]) {
			;
		}

		this.canPickUpLoot = true;

		if (this.getCurrentItemOrArmor(4) == null) {
			Calendar var1 = this.worldObj.getCurrentDate();

			if (var1.get(2) + 1 == 10 && var1.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
				this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Block.pumpkinLantern : Block.pumpkin));
				this.equipmentDropChances[4] = 0.0F;
			}
		}
	}

	public void func_85036_m() {
		this.tasks.func_85156_a(this.field_85038_e);
		this.tasks.func_85156_a(this.field_85037_d);
		ItemStack var1 = this.getHeldItem();

		if (var1 != null && var1.itemID == Item.bow.shiftedIndex) {
			this.tasks.addTask(4, this.field_85037_d);
		} else {
			this.tasks.addTask(4, this.field_85038_e);
		}
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	public void attackEntityWithRangedAttack(EntityLiving par1EntityLiving) {
		EntityArrow var2 = new EntityArrow(this.worldObj, this, par1EntityLiving, 1.6F, 12.0F);
		int var3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
		int var4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());

		if (var3 > 0) {
			var2.setDamage(var2.getDamage() + (double)var3 * 0.5D + 0.5D);
		}

		if (var4 > 0) {
			var2.setKnockbackStrength(var4);
		}

		if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 || this.getSkeletonType() == 1) {
			var2.setFire(100);
		}

		this.func_85030_a("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.worldObj.spawnEntityInWorld(var2);
	}

	/**
	 * Return this skeleton's type.
	 */
	public int getSkeletonType() {
		return this.dataWatcher.getWatchableObjectByte(13);
	}

	/**
	 * Set this skeleton's type.
	 */
	public void setSkeletonType(int par1) {
		this.dataWatcher.updateObject(13, Byte.valueOf((byte)par1));
		this.isImmuneToFire = par1 == 1;

		if (par1 == 1) {
			this.setSize(0.72F, 2.16F);
		} else {
			this.setSize(0.6F, 1.8F);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);

		if (par1NBTTagCompound.hasKey("SkeletonType")) {
			byte var2 = par1NBTTagCompound.getByte("SkeletonType");
			this.setSkeletonType(var2);
		}

		this.func_85036_m();
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setByte("SkeletonType", (byte)this.getSkeletonType());
	}

	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
	 */
	public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack) {
		super.setCurrentItemOrArmor(par1, par2ItemStack);

		if (!this.worldObj.isRemote && par1 == 0) {
			this.func_85036_m();
		}
	}
}
