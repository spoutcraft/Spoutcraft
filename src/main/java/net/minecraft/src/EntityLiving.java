package net.minecraft.src;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
// MCPatcher Start
import org.spoutcraft.api.Spoutcraft;
// MCPatcher End
// Spout Start
import org.spoutcraft.api.entity.EntitySkinType;
import org.spoutcraft.api.material.CustomBlock;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.client.entity.EntityData;
import org.spoutcraft.client.io.CustomTextureManager;
// Spout End

public abstract class EntityLiving extends Entity {

	/**
	 * An array of probabilities that determines whether a random enchantment should be added to the held item. Indexed by
	 * difficulty.
	 */
	private static final float[] enchantmentProbability = new float[] {0.0F, 0.0F, 0.1F, 0.2F};

	/** Probability to get enchanted armor */
	private static final float[] armorEnchantmentProbability = new float[] {0.0F, 0.0F, 0.25F, 0.5F};

	/** Probability to get armor */
	private static final float[] armorProbability = new float[] {0.0F, 0.0F, 0.05F, 0.02F};

	/** Probability to pick up loot */
	public static final float[] pickUpLootProability = new float[] {0.0F, 0.1F, 0.15F, 0.45F};
	public int maxHurtResistantTime = 20;
	public float field_70769_ao;
	public float field_70770_ap;
	public float renderYawOffset = 0.0F;
	public float prevRenderYawOffset = 0.0F;

	/** Entity head rotation yaw */
	public float rotationYawHead = 0.0F;

	/** Entity head rotation yaw at previous tick */
	public float prevRotationYawHead = 0.0F;
	protected float field_70768_au;
	protected float field_70766_av;
	protected float field_70764_aw;
	protected float field_70763_ax;
	protected boolean field_70753_ay = true;

	/** the path for the texture of this entityLiving */
	protected String texture = "/mob/char.png";
	protected boolean field_70740_aA = true;
	protected float field_70741_aB = 0.0F;

	/**
	 * a string holding the type of entity it is currently only implemented in entityPlayer(as 'humanoid')
	 */
	protected String entityType = null;
	protected float field_70743_aD = 1.0F;

	/** The score value of the Mob, the amount of points the mob is worth. */
	protected int scoreValue = 0;
	protected float field_70745_aF = 0.0F;

	/**
	 * A factor used to determine how far this entity will move each tick if it is walking on land. Adjusted by speed, and
	 * slipperiness of the current block.
	 */
	public float landMovementFactor = 0.1F;

	/**
	 * A factor used to determine how far this entity will move each tick if it is jumping or falling.
	 */
	public float jumpMovementFactor = 0.02F;
	public float prevSwingProgress;
	public float swingProgress;
	// Spout Start - protected to public
	public int health = this.getMaxHealth();
	// Spout End
	public int prevHealth;

	/**
	 * in each step in the damage calculations, this is set to the 'carryover' that would result if someone was damaged .25
	 * hearts (for example), and added to the damage in the next step
	 */
	protected int carryoverDamage;

	/** Number of ticks since this EntityLiving last produced its sound */
	public int livingSoundTime;

	/**
	 * The amount of time remaining this entity should act 'hurt'. (Visual appearance of red tint)
	 */
	public int hurtTime;

	/** What the hurt time was max set to last. */
	public int maxHurtTime;

	/** The yaw at which this entity was last attacked from. */
	public float attackedAtYaw = 0.0F;

	/**
	 * The amount of time remaining this entity should act 'dead', i.e. have a corpse in the world.
	 */
	public int deathTime = 0;
	public int attackTime = 0;
	public float prevCameraPitch;
	public float cameraPitch;

	/**
	 * This gets set on entity death, but never used. Looks like a duplicate of isDead
	 */
	protected boolean dead = false;

	/** The experience points the Entity gives. */
	protected int experienceValue;
	public int field_70731_aW = -1;
	public float field_70730_aX = (float)(Math.random() * 0.8999999761581421D + 0.10000000149011612D);
	public float prevLimbYaw;
	public float limbYaw;

	/**
	 * Only relevant when legYaw is not 0(the entity is moving). Influences where in its swing legs and arms currently are.
	 */
	public float limbSwing;

	/** The most recent player that has attacked this entity */
	protected EntityPlayer attackingPlayer = null;

	/**
	 * Set to 60 when hit by the player or the player's wolf, then decrements. Used to determine whether the entity should
	 * drop items on death.
	 */
	protected int recentlyHit = 0;

	/** is only being set, has no uses as of MC 1.1 */
	private EntityLiving entityLivingToAttack = null;
	private int revengeTimer = 0;
	private EntityLiving lastAttackingEntity = null;
	public int arrowHitTimer = 0;
	protected HashMap activePotionsMap = new HashMap();

	/** Whether the DataWatcher needs to be updated with the active potions */
	private boolean potionsNeedUpdate = true;
	private int field_70748_f;
	private EntityLookHelper lookHelper;
	private EntityMoveHelper moveHelper;

	/** Entity jumping helper */
	private EntityJumpHelper jumpHelper;
	private EntityBodyHelper bodyHelper;
	private PathNavigate navigator;
	protected final EntityAITasks tasks;
	protected final EntityAITasks targetTasks;

	/** The active target the Task system uses for tracking */
	private EntityLiving attackTarget;
	private EntitySenses senses;
	private float AIMoveSpeed;
	private ChunkCoordinates homePosition = new ChunkCoordinates(0, 0, 0);

	/** If -1 there is no maximum distance */
	private float maximumHomeDistance = -1.0F;

	/** Equipment (armor and held item) for this entity. */
	private ItemStack[] equipment = new ItemStack[5];

	/** Chances for each equipment piece from dropping when this entity dies. */
	protected float[] equipmentDropChances = new float[5];
	private ItemStack[] field_82180_bT = new ItemStack[5];

	/** Whether an arm swing is currently in progress. */
	public boolean isSwingInProgress = false;
	public int swingProgressInt = 0;

	/** Whether this entity can pick up items from the ground. */
	private boolean canPickUpLoot = false;

	/** Whether this entity should NOT despawn. */
	private boolean persistenceRequired = false;
	protected final CombatTracker field_94063_bt = new CombatTracker(this);

	/**
	 * The number of updates over which the new position and rotation are to be applied to the entity.
	 */
	protected int newPosRotationIncrements;

	/** The new X position to be applied to the entity. */
	protected double newPosX;

	/** The new Y position to be applied to the entity. */
	protected double newPosY;

	/** The new Z position to be applied to the entity. */
	protected double newPosZ;

	/** The new yaw rotation to be applied to the entity. */
	protected double newRotationYaw;

	/** The new yaw rotation to be applied to the entity. */
	protected double newRotationPitch;
	float field_70706_bo = 0.0F;

	/** Amount of damage taken in last hit, in half-hearts */
	// Spout Start - protected to public
	public int lastDamage = 0;
	// Spout End

	/** Holds the living entity age, used to control the despawn. */
	protected int entityAge = 0;
	protected float moveStrafing;
	protected float moveForward;
	protected float randomYawVelocity;

	/** used to check whether entity is jumping. */
	protected boolean isJumping = false;
	protected float defaultPitch = 0.0F;
	protected float moveSpeed = 0.7F;

	/** Number of ticks since last jump */
	private int jumpTicks = 0;

	/** This entity's current target. */
	private Entity currentTarget;

	/** How long to keep a specific target entity */
	protected int numTicksToChaseTarget = 0;

	// Spout Start
	private EntityData entityData = new EntityData();
	public String displayName = null;
	public int maxAir = 300;
	// Spout End

	public EntityLiving(World par1World) {
		super(par1World);
		this.preventEntitySpawning = true;
		this.tasks = new EntityAITasks(par1World != null && par1World.theProfiler != null ? par1World.theProfiler : null);
		this.targetTasks = new EntityAITasks(par1World != null && par1World.theProfiler != null ? par1World.theProfiler : null);
		this.lookHelper = new EntityLookHelper(this);
		this.moveHelper = new EntityMoveHelper(this);
		this.jumpHelper = new EntityJumpHelper(this);
		this.bodyHelper = new EntityBodyHelper(this);
		this.navigator = new PathNavigate(this, par1World, (float)this.func_96121_ay());
		this.senses = new EntitySenses(this);
		this.field_70770_ap = (float)(Math.random() + 1.0D) * 0.01F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.field_70769_ao = (float)Math.random() * 12398.0F;
		this.rotationYaw = (float)(Math.random() * Math.PI * 2.0D);
		this.rotationYawHead = this.rotationYaw;

		for (int var2 = 0; var2 < this.equipmentDropChances.length; ++var2) {
			this.equipmentDropChances[var2] = 0.085F;
		}

		this.stepHeight = 0.5F;
	}

	protected int func_96121_ay() {
		return 16;
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

	public Random getRNG() {
		return this.rand;
	}

	public EntityLiving getAITarget() {
		return this.entityLivingToAttack;
	}

	public EntityLiving getLastAttackingEntity() {
		return this.lastAttackingEntity;
	}

	public void setLastAttackingEntity(Entity par1Entity) {
		if (par1Entity instanceof EntityLiving) {
			this.lastAttackingEntity = (EntityLiving)par1Entity;
		}
	}

	public int getAge() {
		return this.entityAge;
	}

	public float getRotationYawHead() {
		return this.rotationYawHead;
	}

	/**
	 * Sets the head's yaw rotation of the entity.
	 */
	public void setRotationYawHead(float par1) {
		this.rotationYawHead = par1;
	}

	/**
	 * the movespeed used for the new AI system
	 */
	public float getAIMoveSpeed() {
		return this.AIMoveSpeed;
	}

	/**
	 * set the movespeed used for the new AI system
	 */
	public void setAIMoveSpeed(float par1) {
		this.AIMoveSpeed = par1;
		this.setMoveForward(par1);
	}

	public boolean attackEntityAsMob(Entity par1Entity) {
		this.setLastAttackingEntity(par1Entity);
		return false;
	}

	/**
	 * Gets the active target the Task system uses for tracking
	 */
	public EntityLiving getAttackTarget() {
		return this.attackTarget;
	}

	/**
	 * Sets the active target the Task system uses for tracking
	 */
	public void setAttackTarget(EntityLiving par1EntityLiving) {
		this.attackTarget = par1EntityLiving;
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

	/**
	 * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance and
	 * deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
	 */
	protected void updateFallState(double par1, boolean par3) {
		if (!this.isInWater()) {
			this.handleWaterMovement();
		}

		if (par3 && this.fallDistance > 0.0F) {
			int var4 = MathHelper.floor_double(this.posX);
			int var5 = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double)this.yOffset);
			int var6 = MathHelper.floor_double(this.posZ);
			int var7 = this.worldObj.getBlockId(var4, var5, var6);

			if (var7 == 0) {
				int var8 = this.worldObj.blockGetRenderType(var4, var5 - 1, var6);

				if (var8 == 11 || var8 == 32 || var8 == 21) {
					var7 = this.worldObj.getBlockId(var4, var5 - 1, var6);
				}
			}

			if (var7 > 0) {
				Block.blocksList[var7].onFallenUpon(this.worldObj, var4, var5, var6, this, this.fallDistance);
			}
		}

		super.updateFallState(par1, par3);
	}

	/**
	 * Returns true if entity is within home distance from current position
	 */
	public boolean isWithinHomeDistanceCurrentPosition() {
		return this.isWithinHomeDistance(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
	}

	public boolean isWithinHomeDistance(int par1, int par2, int par3) {
		return this.maximumHomeDistance == -1.0F ? true : this.homePosition.getDistanceSquared(par1, par2, par3) < this.maximumHomeDistance * this.maximumHomeDistance;
	}

	public void setHomeArea(int par1, int par2, int par3, int par4) {
		this.homePosition.set(par1, par2, par3);
		this.maximumHomeDistance = (float)par4;
	}

	public ChunkCoordinates getHomePosition() {
		return this.homePosition;
	}

	public float getMaximumHomeDistance() {
		return this.maximumHomeDistance;
	}

	public void detachHome() {
		this.maximumHomeDistance = -1.0F;
	}

	public boolean hasHome() {
		return this.maximumHomeDistance != -1.0F;
	}

	public void setRevengeTarget(EntityLiving par1EntityLiving) {
		this.entityLivingToAttack = par1EntityLiving;
		this.revengeTimer = this.entityLivingToAttack != null ? 100 : 0;
	}

	protected void entityInit() {
		this.dataWatcher.addObject(8, Integer.valueOf(this.field_70748_f));
		this.dataWatcher.addObject(9, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(10, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(6, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(5, "");
	}

	/**
	 * returns true if the entity provided in the argument can be seen. (Raytrace)
	 */
	public boolean canEntityBeSeen(Entity par1Entity) {
		return this.worldObj.rayTraceBlocks(this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), this.worldObj.getWorldVec3Pool().getVecFromPool(par1Entity.posX, par1Entity.posY + (double)par1Entity.getEyeHeight(), par1Entity.posZ)) == null;
	}

	/**
	 * Returns the texture's file path as a String.
	 */
	public String getTexture() {
		// Spout Start
		String custom = getCustomTextureUrl(getTextureToRender());
		if (custom == null || CustomTextureManager.getTexturePathFromUrl(custom) == null) {
			return this.texture;
		} else {
			return CustomTextureManager.getTexturePathFromUrl(custom);
		}
		// Spout End
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities when colliding.
	 */
	public boolean canBePushed() {
		return !this.isDead;
	}

	public float getEyeHeight() {
		return this.height * 0.85F;
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
		this.prevSwingProgress = this.swingProgress;
		super.onEntityUpdate();
		this.worldObj.theProfiler.startSection("mobBaseTick");

		if (this.isEntityAlive() && this.rand.nextInt(1000) < this.livingSoundTime++) {
			this.livingSoundTime = -this.getTalkInterval();
			this.playLivingSound();
		}

		if (this.isEntityAlive() && this.isEntityInsideOpaqueBlock()) {
			this.attackEntityFrom(DamageSource.inWall, 1);
		}

		if (this.isImmuneToFire() || this.worldObj.isRemote) {
			this.extinguish();
		}

		boolean var1 = this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.disableDamage;

		if (this.isEntityAlive() && this.isInsideOfMaterial(Material.water) && !this.canBreatheUnderwater() && !this.activePotionsMap.containsKey(Integer.valueOf(Potion.waterBreathing.id)) && !var1) {
			this.setAir(this.decreaseAirSupply(this.getAir()));

			if (this.getAir() == -20) {
				this.setAir(0);

				for (int var2 = 0; var2 < 8; ++var2) {
					float var3 = this.rand.nextFloat() - this.rand.nextFloat();
					float var4 = this.rand.nextFloat() - this.rand.nextFloat();
					float var5 = this.rand.nextFloat() - this.rand.nextFloat();
					this.worldObj.spawnParticle("bubble", this.posX + (double)var3, this.posY + (double)var4, this.posZ + (double)var5, this.motionX, this.motionY, this.motionZ);
				}

				this.attackEntityFrom(DamageSource.drown, 2);
			}

			this.extinguish();
		} else {
			// Spout Start - 300 to maxAir
			this.setAir(maxAir);
			// Spout End
		}

		this.prevCameraPitch = this.cameraPitch;

		if (this.attackTime > 0) {
			--this.attackTime;
		}

		if (this.hurtTime > 0) {
			--this.hurtTime;
		}

		if (this.hurtResistantTime > 0) {
			--this.hurtResistantTime;
		}

		if (this.health <= 0) {
			this.onDeathUpdate();
		}

		if (this.recentlyHit > 0) {
			--this.recentlyHit;
		} else {
			this.attackingPlayer = null;
		}

		if (this.lastAttackingEntity != null && !this.lastAttackingEntity.isEntityAlive()) {
			this.lastAttackingEntity = null;
		}

		if (this.entityLivingToAttack != null) {
			if (!this.entityLivingToAttack.isEntityAlive()) {
				this.setRevengeTarget((EntityLiving)null);
			} else if (this.revengeTimer > 0) {
				--this.revengeTimer;
			} else {
				this.setRevengeTarget((EntityLiving)null);
			}
		}

		this.updatePotionEffects();
		this.field_70763_ax = this.field_70764_aw;
		this.prevRenderYawOffset = this.renderYawOffset;
		this.prevRotationYawHead = this.rotationYawHead;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		this.worldObj.theProfiler.endSection();
	}

	/**
	 * handles entity death timer, experience orb and particle creation
	 */
	protected void onDeathUpdate() {
		++this.deathTime;

		if (this.deathTime == 20) {
			int var1;

			if (!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && !this.isChild() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
				var1 = this.getExperiencePoints(this.attackingPlayer);

				while (var1 > 0) {
					int var2 = EntityXPOrb.getXPSplit(var1);
					var1 -= var2;
					this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var2));
				}
			}

			this.setDead();

			for (var1 = 0; var1 < 20; ++var1) {
				double var8 = this.rand.nextGaussian() * 0.02D;
				double var4 = this.rand.nextGaussian() * 0.02D;
				double var6 = this.rand.nextGaussian() * 0.02D;
				this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var8, var4, var6);
			}
		}
	}

	/**
	 * Decrements the entity's air supply when underwater
	 */
	protected int decreaseAirSupply(int par1) {
		int var2 = EnchantmentHelper.getRespiration(this);
		return var2 > 0 && this.rand.nextInt(var2 + 1) > 0 ? par1 : par1 - 1;
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
	 * Only use is to identify if class is an instance of player for experience dropping
	 */
	protected boolean isPlayer() {
		return false;
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
	 * Handles updating while being ridden by an entity
	 */
	public void updateRidden() {
		super.updateRidden();
		this.field_70768_au = this.field_70766_av;
		this.field_70766_av = 0.0F;
		this.fallDistance = 0.0F;
	}

	/**
	 * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX, posY,
	 * posZ, yaw, pitch
	 */
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
		this.yOffset = 0.0F;
		this.newPosX = par1;
		this.newPosY = par3;
		this.newPosZ = par5;
		this.newRotationYaw = (double)par7;
		this.newRotationPitch = (double)par8;
		this.newPosRotationIncrements = par9;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();

		if (!this.worldObj.isRemote) {
			int var1;

			for (var1 = 0; var1 < 5; ++var1) {
				ItemStack var2 = this.getCurrentItemOrArmor(var1);

				if (!ItemStack.areItemStacksEqual(var2, this.field_82180_bT[var1])) {
					((WorldServer)this.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet5PlayerInventory(this.entityId, var1, var2));
					this.field_82180_bT[var1] = var2 == null ? null : var2.copy();
				}
			}

			var1 = this.getArrowCountInEntity();

			if (var1 > 0) {
				if (this.arrowHitTimer <= 0) {
					this.arrowHitTimer = 20 * (30 - var1);
				}

				--this.arrowHitTimer;

				if (this.arrowHitTimer <= 0) {
					this.setArrowCountInEntity(var1 - 1);
				}
			}
		}

		this.onLivingUpdate();
		double var12 = this.posX - this.prevPosX;
		double var3 = this.posZ - this.prevPosZ;
		float var5 = (float)(var12 * var12 + var3 * var3);
		float var6 = this.renderYawOffset;
		float var7 = 0.0F;
		this.field_70768_au = this.field_70766_av;
		float var8 = 0.0F;

		if (var5 > 0.0025000002F) {
			var8 = 1.0F;
			var7 = (float)Math.sqrt((double)var5) * 3.0F;
			var6 = (float)Math.atan2(var3, var12) * 180.0F / (float)Math.PI - 90.0F;
		}

		if (this.swingProgress > 0.0F) {
			var6 = this.rotationYaw;
		}

		if (!this.onGround) {
			var8 = 0.0F;
		}

		this.field_70766_av += (var8 - this.field_70766_av) * 0.3F;
		this.worldObj.theProfiler.startSection("headTurn");

		if (this.isAIEnabled()) {
			this.bodyHelper.func_75664_a();
		} else {
			float var9 = MathHelper.wrapAngleTo180_float(var6 - this.renderYawOffset);
			this.renderYawOffset += var9 * 0.3F;
			float var10 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset);
			boolean var11 = var10 < -90.0F || var10 >= 90.0F;

			if (var10 < -75.0F) {
				var10 = -75.0F;
			}

			if (var10 >= 75.0F) {
				var10 = 75.0F;
			}

			this.renderYawOffset = this.rotationYaw - var10;

			if (var10 * var10 > 2500.0F) {
				this.renderYawOffset += var10 * 0.2F;
			}

			if (var11) {
				var7 *= -1.0F;
			}
		}

		this.worldObj.theProfiler.endSection();
		this.worldObj.theProfiler.startSection("rangeChecks");

		while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
			this.prevRenderYawOffset -= 360.0F;
		}

		while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
			this.prevRenderYawOffset += 360.0F;
		}

		while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
			this.prevRotationPitch -= 360.0F;
		}

		while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYawHead - this.prevRotationYawHead < -180.0F) {
			this.prevRotationYawHead -= 360.0F;
		}

		while (this.rotationYawHead - this.prevRotationYawHead >= 180.0F) {
			this.prevRotationYawHead += 360.0F;
		}

		this.worldObj.theProfiler.endSection();
		this.field_70764_aw += var7;
	}

	/**
	 * Heal living entity (param: amount of half-hearts)
	 */
	public void heal(int par1) {
		if (this.health > 0) {
			this.setEntityHealth(this.getHealth() + par1);

			if (this.health > this.getMaxHealth()) {
				this.setEntityHealth(this.getMaxHealth());
			}

			this.hurtResistantTime = this.maxHurtResistantTime / 2;
		}
	}

	public abstract int getMaxHealth();

	public int getHealth() {
		return this.health;
	}

	public void setEntityHealth(int par1) {
		this.health = par1;

		if (par1 > this.getMaxHealth()) {
			par1 = this.getMaxHealth();
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		if (this.isEntityInvulnerable()) {
			return false;
		} else if (this.worldObj.isRemote) {
			return false;
		} else {
			this.entityAge = 0;

			if (this.health <= 0) {
				return false;
			} else if (par1DamageSource.isFireDamage() && this.isPotionActive(Potion.fireResistance)) {
				return false;
			} else {
				if ((par1DamageSource == DamageSource.anvil || par1DamageSource == DamageSource.fallingBlock) && this.getCurrentItemOrArmor(4) != null) {
					this.getCurrentItemOrArmor(4).damageItem(par2 * 4 + this.rand.nextInt(par2 * 2), this);
					par2 = (int)((float)par2 * 0.75F);
				}

				this.limbYaw = 1.5F;
				boolean var3 = true;

				if ((float)this.hurtResistantTime > (float)this.maxHurtResistantTime / 2.0F) {
					if (par2 <= this.lastDamage) {
						return false;
					}

					this.damageEntity(par1DamageSource, par2 - this.lastDamage);
					this.lastDamage = par2;
					var3 = false;
				} else {
					this.lastDamage = par2;
					this.prevHealth = this.health;
					this.hurtResistantTime = this.maxHurtResistantTime;
					this.damageEntity(par1DamageSource, par2);
					this.hurtTime = this.maxHurtTime = 10;
				}

				this.attackedAtYaw = 0.0F;
				Entity var4 = par1DamageSource.getEntity();

				if (var4 != null) {
					if (var4 instanceof EntityLiving) {
						this.setRevengeTarget((EntityLiving)var4);
					}

					if (var4 instanceof EntityPlayer) {
						this.recentlyHit = 100;
						this.attackingPlayer = (EntityPlayer)var4;
					} else if (var4 instanceof EntityWolf) {
						EntityWolf var5 = (EntityWolf)var4;

						if (var5.isTamed()) {
							this.recentlyHit = 100;
							this.attackingPlayer = null;
						}
					}
				}

				if (var3) {
					this.worldObj.setEntityState(this, (byte)2);

					if (par1DamageSource != DamageSource.drown) {
						this.setBeenAttacked();
					}

					if (var4 != null) {
						double var9 = var4.posX - this.posX;
						double var7;

						for (var7 = var4.posZ - this.posZ; var9 * var9 + var7 * var7 < 1.0E-4D; var7 = (Math.random() - Math.random()) * 0.01D) {
							var9 = (Math.random() - Math.random()) * 0.01D;
						}

						this.attackedAtYaw = (float)(Math.atan2(var7, var9) * 180.0D / Math.PI) - this.rotationYaw;
						this.knockBack(var4, par2, var9, var7);
					} else {
						this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
					}
				}

				if (this.health <= 0) {
					if (var3) {
						this.playSound(this.getDeathSound(), this.getSoundVolume(), this.getSoundPitch());
					}

					this.onDeath(par1DamageSource);
				} else if (var3) {
					this.playSound(this.getHurtSound(), this.getSoundVolume(), this.getSoundPitch());
				}

				return true;
			}
		}
	}

	/**
	 * Gets the pitch of living sounds in living entities.
	 */
	protected float getSoundPitch() {
		return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
	}

	/**
	 * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
	 */
	public void performHurtAnimation() {
		this.hurtTime = this.maxHurtTime = 10;
		this.attackedAtYaw = 0.0F;
	}

	/**
	 * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
	 */
	public int getTotalArmorValue() {
		int var1 = 0;
		ItemStack[] var2 = this.getLastActiveItems();
		int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			ItemStack var5 = var2[var4];

			if (var5 != null && var5.getItem() instanceof ItemArmor) {
				int var6 = ((ItemArmor)var5.getItem()).damageReduceAmount;
				var1 += var6;
			}
		}

		return var1;
	}

	protected void damageArmor(int par1) {}

	/**
	 * Reduces damage, depending on armor
	 */
	protected int applyArmorCalculations(DamageSource par1DamageSource, int par2) {
		if (!par1DamageSource.isUnblockable()) {
			int var3 = 25 - this.getTotalArmorValue();
			int var4 = par2 * var3 + this.carryoverDamage;
			this.damageArmor(par2);
			par2 = var4 / 25;
			this.carryoverDamage = var4 % 25;
		}

		return par2;
	}

	/**
	 * Reduces damage, depending on potions
	 */
	protected int applyPotionDamageCalculations(DamageSource par1DamageSource, int par2) {
		int var3;
		int var4;
		int var5;

		if (this.isPotionActive(Potion.resistance)) {
			var3 = (this.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
			var4 = 25 - var3;
			var5 = par2 * var4 + this.carryoverDamage;
			par2 = var5 / 25;
			this.carryoverDamage = var5 % 25;
		}

		if (par2 <= 0) {
			return 0;
		} else {
			var3 = EnchantmentHelper.getEnchantmentModifierDamage(this.getLastActiveItems(), par1DamageSource);

			if (var3 > 20) {
				var3 = 20;
			}

			if (var3 > 0 && var3 <= 20) {
				var4 = 25 - var3;
				var5 = par2 * var4 + this.carryoverDamage;
				par2 = var5 / 25;
				this.carryoverDamage = var5 % 25;
			}

			return par2;
		}
	}

	/**
	 * Deals damage to the entity. If its a EntityPlayer then will take damage from the armor first and then health second
	 * with the reduced value. Args: damageAmount
	 */
	// Spout Start - protected to public
	public void damageEntity(DamageSource par1DamageSource, int par2) { // Spout protected -> public
	// Spout End
		if (!this.isEntityInvulnerable()) {
			par2 = this.applyArmorCalculations(par1DamageSource, par2);
			par2 = this.applyPotionDamageCalculations(par1DamageSource, par2);
			int var3 = this.getHealth();
			this.health -= par2;
			this.field_94063_bt.func_94547_a(par1DamageSource, var3, par2);
		}
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume() {
		return 1.0F;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return null;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "damage.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "damage.hit";
	}

	/**
	 * knocks back this entity
	 */
	public void knockBack(Entity par1Entity, int par2, double par3, double par5) {
		this.isAirBorne = true;
		float var7 = MathHelper.sqrt_double(par3 * par3 + par5 * par5);
		float var8 = 0.4F;
		this.motionX /= 2.0D;
		this.motionY /= 2.0D;
		this.motionZ /= 2.0D;
		this.motionX -= par3 / (double)var7 * (double)var8;
		this.motionY += (double)var8;
		this.motionZ -= par5 / (double)var7 * (double)var8;

		if (this.motionY > 0.4000000059604645D) {
			this.motionY = 0.4000000059604645D;
		}
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource par1DamageSource) {
		Entity var2 = par1DamageSource.getEntity();
		EntityLiving var3 = this.func_94060_bK();

		if (this.scoreValue >= 0 && var3 != null) {
			var3.addToPlayerScore(this, this.scoreValue);
		}

		if (var2 != null) {
			var2.onKillEntity(this);
		}

		this.dead = true;

		if (!this.worldObj.isRemote) {
			int var4 = 0;

			if (var2 instanceof EntityPlayer) {
				var4 = EnchantmentHelper.getLootingModifier((EntityLiving)var2);
			}

			if (!this.isChild() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
				this.dropFewItems(this.recentlyHit > 0, var4);
				this.dropEquipment(this.recentlyHit > 0, var4);

				if (this.recentlyHit > 0) {
					int var5 = this.rand.nextInt(200) - var4;

					if (var5 < 5) {
						this.dropRareDrop(var5 <= 0 ? 1 : 0);
					}
				}
			}
		}

		this.worldObj.setEntityState(this, (byte)3);
	}

	protected void dropRareDrop(int par1) {}

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
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId() {
		return 0;
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float par1) {
		super.fall(par1);
		// Spout Start - Gravity mod
		par1 *= getData().getGravityMod();
		// Spout End
		int var2 = MathHelper.ceiling_float_int(par1 - 3.0F);

		if (var2 > 0) {
			if (var2 > 4) {
				this.playSound("damage.fallbig", 1.0F, 1.0F);
			} else {
				this.playSound("damage.fallsmall", 1.0F, 1.0F);
			}

			this.attackEntityFrom(DamageSource.fall, var2);
			int var3 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 0.20000000298023224D - (double)this.yOffset), MathHelper.floor_double(this.posZ));

			if (var3 > 0) {
				StepSound var4 = Block.blocksList[var3].stepSound;
				this.playSound(var4.getStepSound(), var4.getVolume() * 0.5F, var4.getPitch() * 0.75F);
			}
		}
	}

	/**
	 * Moves the entity based on the specified heading.  Args: strafe, forward
	 */
	public void moveEntityWithHeading(float par1, float par2) {
		double var9;

		if (this.isInWater() && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).capabilities.isFlying)) {
			var9 = this.posY;
			// Spout Start - Swimming mod
			this.moveFlying(par1, par2, ((float) ((this.isAIEnabled() ? 0.04F : 0.02F) * getData().getSwimmingMod())));
			// Spout End
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.800000011920929D;
			this.motionY *= 0.800000011920929D;
			this.motionZ *= 0.800000011920929D;
			// Spout Start - Added gravity modifier
			this.motionY -= 0.02D * getData().getGravityMod();
			// Spout End

			if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var9, this.motionZ)) {
				this.motionY = 0.30000001192092896D;
			}
		} else if (this.handleLavaMovement() && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).capabilities.isFlying)) {
			var9 = this.posY;
			// Spout Start - Added swimming modifier
			this.moveFlying(par1, par2, (float)(0.02F * getData().getSwimmingMod()));
			// Spout End
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
			this.motionY -= 0.02D;

			if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var9, this.motionZ)) {
				this.motionY = 0.30000001192092896D;
			}
		} else {
			float var3 = 0.91F;

			if (this.onGround) {
				var3 = 0.54600006F;
				int var4 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

				if (var4 > 0) {
					var3 = Block.blocksList[var4].slipperiness * 0.91F;
					// Spout Start
					int x = MathHelper.floor_double(this.posX);
					int y = MathHelper.floor_double(this.boundingBox.minY) - 1;
					int z = MathHelper.floor_double(this.posZ);
					org.spoutcraft.client.block.SpoutcraftChunk chunk = Spoutcraft.getChunkAt(worldObj, x, y, z);
					short customId = chunk.getCustomBlockId(x, y, z);
					if (customId > 0) {
						CustomBlock block = MaterialData.getCustomBlock(customId);
						if (block != null) {
							var3 = block.getFriction() * 0.98F;
						}
					}
					// Spout End
				}
			}

			float var8 = 0.16277136F / (var3 * var3 * var3);
			float var5;

			if (this.onGround) {
				if (this.isAIEnabled()) {
					// Spout Start
					var5 = (float) (this.getAIMoveSpeed() * getData().getWalkingMod());
					// Spout End
				} else {
					// Spout Start
					var5 = (float) (this.landMovementFactor * getData().getWalkingMod());
					// Spout End
				}

				var5 *= var8;
			} else {
				// Spout Start - Added AirSpeed modifier
				var5 = (float) (this.jumpMovementFactor * getData().getAirspeedMod());
				// Spout End
			}

			this.moveFlying(par1, par2, var5);
			var3 = 0.91F;

			if (this.onGround) {
				var3 = 0.54600006F;
				int var6 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

				if (var6 > 0) {
					var3 = Block.blocksList[var6].slipperiness * 0.91F;
					// Spout Start
					int x = MathHelper.floor_double(this.posX);
					int y = MathHelper.floor_double(this.boundingBox.minY) - 1;
					int z = MathHelper.floor_double(this.posZ);
					short customId = Spoutcraft.getChunkAt(worldObj, x, y, z).getCustomBlockId(x, y, z);
					if (customId > 0) {
						CustomBlock block = MaterialData.getCustomBlock(customId);
						if (block != null) {
							var3 = block.getFriction() * 0.98F;
						}
					}
					// Spout End
				}
			}

			if (this.isOnLadder()) {
				float var10 = 0.15F;

				if (this.motionX < (double)(-var10)) {
					this.motionX = (double)(-var10);
				}

				if (this.motionX > (double)var10) {
					this.motionX = (double)var10;
				}

				if (this.motionZ < (double)(-var10)) {
					this.motionZ = (double)(-var10);
				}

				if (this.motionZ > (double)var10) {
					this.motionZ = (double)var10;
				}

				this.fallDistance = 0.0F;

				if (this.motionY < -0.15D) {
					this.motionY = -0.15D;
				}

				boolean var7 = this.isSneaking() && this instanceof EntityPlayer;

				if (var7 && this.motionY < 0.0D) {
					this.motionY = 0.0D;
				}
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);

			if (this.isCollidedHorizontally && this.isOnLadder()) {
				this.motionY = 0.2D;
			}

			if (this.worldObj.isRemote && (!this.worldObj.blockExists((int)this.posX, 0, (int)this.posZ) || !this.worldObj.getChunkFromBlockCoords((int)this.posX, (int)this.posZ).isChunkLoaded)) {
				if (this.posY > 0.0D) {
					this.motionY = -0.1D;
				} else {
					this.motionY = 0.0D;
				}
			} else {
				// Spout Start - Added gravity modifier
				this.motionY -= 0.08D * getData().getGravityMod();
				// Spout End
			}

			this.motionY *= 0.9800000190734863D;
			this.motionX *= (double)var3;
			this.motionZ *= (double)var3;
		}

		this.prevLimbYaw = this.limbYaw;
		var9 = this.posX - this.prevPosX;
		double var12 = this.posZ - this.prevPosZ;
		float var11 = MathHelper.sqrt_double(var9 * var9 + var12 * var12) * 4.0F;

		if (var11 > 1.0F) {
			var11 = 1.0F;
		}

		this.limbYaw += (var11 - this.limbYaw) * 0.4F;
		this.limbSwing += this.limbYaw;
	}

	/**
	 * returns true if this entity is by a ladder, false otherwise
	 */
	public boolean isOnLadder() {
		int var1 = MathHelper.floor_double(this.posX);
		int var2 = MathHelper.floor_double(this.boundingBox.minY);
		int var3 = MathHelper.floor_double(this.posZ);
		int var4 = this.worldObj.getBlockId(var1, var2, var3);
		return var4 == Block.ladder.blockID || var4 == Block.vine.blockID;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		// MCPatcher Start
		com.prupe.mcpatcher.mod.MobRandomizer$ExtraInfo.writeToNBT(this, par1NBTTagCompound);
		// MCPatcher End
		if (this.health < -32768) {
			this.health = -32768;
		}

		par1NBTTagCompound.setShort("Health", (short)this.health);
		par1NBTTagCompound.setShort("HurtTime", (short)this.hurtTime);
		par1NBTTagCompound.setShort("DeathTime", (short)this.deathTime);
		par1NBTTagCompound.setShort("AttackTime", (short)this.attackTime);
		par1NBTTagCompound.setBoolean("CanPickUpLoot", this.canPickUpLoot());
		par1NBTTagCompound.setBoolean("PersistenceRequired", this.persistenceRequired);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.equipment.length; ++var3) {
			NBTTagCompound var4 = new NBTTagCompound();

			if (this.equipment[var3] != null) {
				this.equipment[var3].writeToNBT(var4);
			}

			var2.appendTag(var4);
		}

		par1NBTTagCompound.setTag("Equipment", var2);
		NBTTagList var6;

		if (!this.activePotionsMap.isEmpty()) {
			var6 = new NBTTagList();
			Iterator var7 = this.activePotionsMap.values().iterator();

			while (var7.hasNext()) {
				PotionEffect var5 = (PotionEffect)var7.next();
				var6.appendTag(var5.writeCustomPotionEffectToNBT(new NBTTagCompound()));
			}

			par1NBTTagCompound.setTag("ActiveEffects", var6);
		}

		var6 = new NBTTagList();

		for (int var8 = 0; var8 < this.equipmentDropChances.length; ++var8) {
			var6.appendTag(new NBTTagFloat(var8 + "", this.equipmentDropChances[var8]));
		}

		par1NBTTagCompound.setTag("DropChances", var6);
		par1NBTTagCompound.setString("CustomName", this.func_94057_bL());
		par1NBTTagCompound.setBoolean("CustomNameVisible", this.func_94062_bN());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		// MCPatcher Start
		com.prupe.mcpatcher.mod.MobRandomizer$ExtraInfo.readFromNBT(this, par1NBTTagCompound);
		// MCPatcher End
		this.health = par1NBTTagCompound.getShort("Health");

		if (!par1NBTTagCompound.hasKey("Health")) {
			this.health = this.getMaxHealth();
		}

		this.hurtTime = par1NBTTagCompound.getShort("HurtTime");
		this.deathTime = par1NBTTagCompound.getShort("DeathTime");
		this.attackTime = par1NBTTagCompound.getShort("AttackTime");
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

		if (par1NBTTagCompound.hasKey("ActiveEffects")) {
			var2 = par1NBTTagCompound.getTagList("ActiveEffects");

			for (var3 = 0; var3 < var2.tagCount(); ++var3) {
				NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
				PotionEffect var5 = PotionEffect.readCustomPotionEffectFromNBT(var4);
				this.activePotionsMap.put(Integer.valueOf(var5.getPotionID()), var5);
			}
		}

		if (par1NBTTagCompound.hasKey("DropChances")) {
			var2 = par1NBTTagCompound.getTagList("DropChances");

			for (var3 = 0; var3 < var2.tagCount(); ++var3) {
				this.equipmentDropChances[var3] = ((NBTTagFloat)var2.tagAt(var3)).data;
			}
		}
	}

	/**
	 * Checks whether target entity is alive.
	 */
	public boolean isEntityAlive() {
		return !this.isDead && this.health > 0;
	}

	public boolean canBreatheUnderwater() {
		return false;
	}

	public void setMoveForward(float par1) {
		this.moveForward = par1;
	}

	public void setJumping(boolean par1) {
		this.isJumping = par1;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use
	 * this to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		if (this.jumpTicks > 0) {
			--this.jumpTicks;
		}

		if (this.newPosRotationIncrements > 0) {
			double var1 = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
			double var3 = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
			double var5 = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;
			double var7 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - (double)this.rotationYaw);
			this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.newPosRotationIncrements);
			this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
			--this.newPosRotationIncrements;
			this.setPosition(var1, var3, var5);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		} else if (!this.isClientWorld()) {
			this.motionX *= 0.98D;
			this.motionY *= 0.98D;
			this.motionZ *= 0.98D;
		}

		if (Math.abs(this.motionX) < 0.005D) {
			this.motionX = 0.0D;
		}

		if (Math.abs(this.motionY) < 0.005D) {
			this.motionY = 0.0D;
		}

		if (Math.abs(this.motionZ) < 0.005D) {
			this.motionZ = 0.0D;
		}

		this.worldObj.theProfiler.startSection("ai");

		if (this.isMovementBlocked()) {
			this.isJumping = false;
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
			this.randomYawVelocity = 0.0F;
		} else if (this.isClientWorld()) {
			if (this.isAIEnabled()) {
				this.worldObj.theProfiler.startSection("newAi");
				this.updateAITasks();
				this.worldObj.theProfiler.endSection();
			} else {
				this.worldObj.theProfiler.startSection("oldAi");
				this.updateEntityActionState();
				this.worldObj.theProfiler.endSection();
				this.rotationYawHead = this.rotationYaw;
			}
		}

		this.worldObj.theProfiler.endSection();
		this.worldObj.theProfiler.startSection("jump");

		if (this.isJumping) {
			if (!this.isInWater() && !this.handleLavaMovement()) {
				if (this.onGround && this.jumpTicks == 0) {
					this.jump();
					this.jumpTicks = 10;
				}
			} else {
				this.motionY += 0.03999999910593033D;
			}
		} else {
			this.jumpTicks = 0;
		}

		this.worldObj.theProfiler.endSection();
		this.worldObj.theProfiler.startSection("travel");
		this.moveStrafing *= 0.98F;
		this.moveForward *= 0.98F;
		this.randomYawVelocity *= 0.9F;
		float var11 = this.landMovementFactor;
		this.landMovementFactor *= this.getSpeedModifier();
		this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
		this.landMovementFactor = var11;
		this.worldObj.theProfiler.endSection();
		this.worldObj.theProfiler.startSection("push");

		if (!this.worldObj.isRemote) {
			this.func_85033_bc();
		}

		this.worldObj.theProfiler.endSection();
		this.worldObj.theProfiler.startSection("looting");

		if (!this.worldObj.isRemote && this.canPickUpLoot() && !this.dead && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
			List var2 = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(1.0D, 0.0D, 1.0D));
			Iterator var12 = var2.iterator();

			while (var12.hasNext()) {
				EntityItem var4 = (EntityItem)var12.next();

				// Spout Start - item instead of func_92059_d
				if (!var4.isDead && var4.getEntityItem()!= null) {
					ItemStack var13 = var4.getEntityItem();
				// Spout End
					int var6 = getArmorPosition(var13);

					if (var6 > -1) {
						boolean var14 = true;
						ItemStack var8 = this.getCurrentItemOrArmor(var6);

						if (var8 != null) {
							if (var6 == 0) {
								if (var13.getItem() instanceof ItemSword && !(var8.getItem() instanceof ItemSword)) {
									var14 = true;
								} else if (var13.getItem() instanceof ItemSword && var8.getItem() instanceof ItemSword) {
									ItemSword var9 = (ItemSword)var13.getItem();
									ItemSword var10 = (ItemSword)var8.getItem();

									if (var9.func_82803_g() == var10.func_82803_g()) {
										var14 = var13.getItemDamage() > var8.getItemDamage() || var13.hasTagCompound() && !var8.hasTagCompound();
									} else {
										var14 = var9.func_82803_g() > var10.func_82803_g();
									}
								} else {
									var14 = false;
								}
							} else if (var13.getItem() instanceof ItemArmor && !(var8.getItem() instanceof ItemArmor)) {
								var14 = true;
							} else if (var13.getItem() instanceof ItemArmor && var8.getItem() instanceof ItemArmor) {
								ItemArmor var15 = (ItemArmor)var13.getItem();
								ItemArmor var16 = (ItemArmor)var8.getItem();

								if (var15.damageReduceAmount == var16.damageReduceAmount) {
									var14 = var13.getItemDamage() > var8.getItemDamage() || var13.hasTagCompound() && !var8.hasTagCompound();
								} else {
									var14 = var15.damageReduceAmount > var16.damageReduceAmount;
								}
							} else {
								var14 = false;
							}
						}

						if (var14) {
							if (var8 != null && this.rand.nextFloat() - 0.1F < this.equipmentDropChances[var6]) {
								this.entityDropItem(var8, 0.0F);
							}

							this.setCurrentItemOrArmor(var6, var13);
							this.equipmentDropChances[var6] = 2.0F;
							this.persistenceRequired = true;
							this.onItemPickup(var4, 1);
							var4.setDead();
						}
					}
				}
			}
		}

		this.worldObj.theProfiler.endSection();
	}

	protected void func_85033_bc() {
		List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

		if (var1 != null && !var1.isEmpty()) {
			for (int var2 = 0; var2 < var1.size(); ++var2) {
				Entity var3 = (Entity)var1.get(var2);

				if (var3.canBePushed()) {
					this.collideWithEntity(var3);
				}
			}
		}
	}

	protected void collideWithEntity(Entity par1Entity) {
		par1Entity.applyEntityCollision(this);
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	protected boolean isAIEnabled() {
		return false;
	}

	/**
	 * Returns whether the entity is in a local (client) world
	 */
	protected boolean isClientWorld() {
		return !this.worldObj.isRemote;
	}

	/**
	 * Dead and sleeping entities cannot move
	 */
	protected boolean isMovementBlocked() {
		return this.health <= 0;
	}

	public boolean isBlocking() {
		return false;
	}

	/**
	 * Causes this entity to do an upwards motion (jumping).
	 */
	protected void jump() {
		// Spout Start - Added jumping modifier
		this.motionY = 0.41999998688697815D * getData().getJumpingMod();
		// Spout End

		if (this.isPotionActive(Potion.jump)) {
			this.motionY += (double)((float)(this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
		}

		if (this.isSprinting()) {
			float var1 = this.rotationYaw * 0.017453292F;
			this.motionX -= (double)(MathHelper.sin(var1) * 0.2F);
			this.motionZ += (double)(MathHelper.cos(var1) * 0.2F);
		}

		this.isAirBorne = true;
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
		if (!this.persistenceRequired) {
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

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	protected void updateAITick() {}

	protected void updateEntityActionState() {
		++this.entityAge;
		this.despawnEntity();
		this.moveStrafing = 0.0F;
		this.moveForward = 0.0F;
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
	 * Updates the arm swing progress counters and animation progress
	 */
	protected void updateArmSwingProgress() {
		int var1 = this.getArmSwingAnimationEnd();

		if (this.isSwingInProgress) {
			++this.swingProgressInt;

			if (this.swingProgressInt >= var1) {
				this.swingProgressInt = 0;
				this.isSwingInProgress = false;
			}
		} else {
			this.swingProgressInt = 0;
		}

		this.swingProgress = (float)this.swingProgressInt / (float)var1;
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

		if (par1Entity instanceof EntityLiving) {
			EntityLiving var10 = (EntityLiving)par1Entity;
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
		return this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox);
	}

	/**
	 * sets the dead flag. Used when you fall off the bottom of the world.
	 */
	protected void kill() {
		this.attackEntityFrom(DamageSource.outOfWorld, 4);
	}

	/**
	 * Returns where in the swing animation the living entity is (from 0 to 1).  Args: partialTickTime
	 */
	public float getSwingProgress(float par1) {
		float var2 = this.swingProgress - this.prevSwingProgress;

		if (var2 < 0.0F) {
			++var2;
		}

		return this.prevSwingProgress + var2 * par1;
	}

	/**
	 * interpolated position vector
	 */
	public Vec3 getPosition(float par1) {
		if (par1 == 1.0F) {
			return this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
		} else {
			double var2 = this.prevPosX + (this.posX - this.prevPosX) * (double)par1;
			double var4 = this.prevPosY + (this.posY - this.prevPosY) * (double)par1;
			double var6 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double)par1;
			return this.worldObj.getWorldVec3Pool().getVecFromPool(var2, var4, var6);
		}
	}

	/**
	 * returns a (normalized) vector of where this entity is looking
	 */
	public Vec3 getLookVec() {
		return this.getLook(1.0F);
	}

	/**
	 * interpolated look vector
	 */
	public Vec3 getLook(float par1) {
		float var2;
		float var3;
		float var4;
		float var5;

		if (par1 == 1.0F) {
			var2 = MathHelper.cos(-this.rotationYaw * 0.017453292F - (float)Math.PI);
			var3 = MathHelper.sin(-this.rotationYaw * 0.017453292F - (float)Math.PI);
			var4 = -MathHelper.cos(-this.rotationPitch * 0.017453292F);
			var5 = MathHelper.sin(-this.rotationPitch * 0.017453292F);
			return this.worldObj.getWorldVec3Pool().getVecFromPool((double)(var3 * var4), (double)var5, (double)(var2 * var4));
		} else {
			var2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * par1;
			var3 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * par1;
			var4 = MathHelper.cos(-var3 * 0.017453292F - (float)Math.PI);
			var5 = MathHelper.sin(-var3 * 0.017453292F - (float)Math.PI);
			float var6 = -MathHelper.cos(-var2 * 0.017453292F);
			float var7 = MathHelper.sin(-var2 * 0.017453292F);
			return this.worldObj.getWorldVec3Pool().getVecFromPool((double)(var5 * var6), (double)var7, (double)(var4 * var6));
		}
	}

	/**
	 * Returns render size modifier
	 */
	public float getRenderSizeModifier() {
		return 1.0F;
	}

	/**
	 * Performs a ray trace for the distance specified and using the partial tick time. Args: distance, partialTickTime
	 */
	public MovingObjectPosition rayTrace(double par1, float par3) {
		Vec3 var4 = this.getPosition(par3);
		Vec3 var5 = this.getLook(par3);
		Vec3 var6 = var4.addVector(var5.xCoord * par1, var5.yCoord * par1, var5.zCoord * par1);
		return this.worldObj.rayTraceBlocks(var4, var6);
	}

	/**
	 * Will return how many at most can spawn in a chunk at once.
	 */
	public int getMaxSpawnedInChunk() {
		return 4;
	}

	public void handleHealthUpdate(byte par1) {
		if (par1 == 2) {
			this.limbYaw = 1.5F;
			this.hurtResistantTime = this.maxHurtResistantTime;
			this.hurtTime = this.maxHurtTime = 10;
			this.attackedAtYaw = 0.0F;
			this.playSound(this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.attackEntityFrom(DamageSource.generic, 0);
		} else if (par1 == 3) {
			this.playSound(this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.health = 0;
			this.onDeath(DamageSource.generic);
		} else {
			super.handleHealthUpdate(par1);
		}
	}

	/**
	 * Returns whether player is sleeping or not
	 */
	public boolean isPlayerSleeping() {
		return false;
	}

	/**
	 * Gets the Icon Index of the item currently held
	 */
	public Icon getItemIcon(ItemStack par1ItemStack, int par2) {
		return par1ItemStack.getIconIndex();
	}

	protected void updatePotionEffects() {
		Iterator var1 = this.activePotionsMap.keySet().iterator();

		while (var1.hasNext()) {
			Integer var2 = (Integer)var1.next();
			PotionEffect var3 = (PotionEffect)this.activePotionsMap.get(var2);

			try {
				if (!var3.onUpdate(this)) {
					if (!this.worldObj.isRemote) {
						var1.remove();
						this.onFinishedPotionEffect(var3);
					}
				} else if (var3.getDuration() % 600 == 0) {
					this.onChangedPotionEffect(var3);
				}
			} catch (Throwable var11) {
				CrashReport var5 = CrashReport.makeCrashReport(var11, "Ticking mob effect instance");
				CrashReportCategory var6 = var5.makeCategory("Mob effect being ticked");
				var6.addCrashSectionCallable("Effect Name", new CallableEffectName(this, var3));
				var6.addCrashSectionCallable("Effect ID", new CallableEffectID(this, var3));
				var6.addCrashSectionCallable("Effect Duration", new CallableEffectDuration(this, var3));
				var6.addCrashSectionCallable("Effect Amplifier", new CallableEffectAmplifier(this, var3));
				var6.addCrashSectionCallable("Effect is Splash", new CallableEffectIsSplash(this, var3));
				var6.addCrashSectionCallable("Effect is Ambient", new CallableEffectIsAmbient(this, var3));
				throw new ReportedException(var5);
			}
		}

		int var12;

		if (this.potionsNeedUpdate) {
			if (!this.worldObj.isRemote) {
				if (this.activePotionsMap.isEmpty()) {
					this.dataWatcher.updateObject(9, Byte.valueOf((byte)0));
					this.dataWatcher.updateObject(8, Integer.valueOf(0));
					this.setHasActivePotion(false);
				} else {
					var12 = PotionHelper.calcPotionLiquidColor(this.activePotionsMap.values());
					this.dataWatcher.updateObject(9, Byte.valueOf((byte)(PotionHelper.func_82817_b(this.activePotionsMap.values()) ? 1 : 0)));
					this.dataWatcher.updateObject(8, Integer.valueOf(var12));
					this.setHasActivePotion(this.isPotionActive(Potion.invisibility.id));
				}
			}

			this.potionsNeedUpdate = false;
		}

		var12 = this.dataWatcher.getWatchableObjectInt(8);
		boolean var13 = this.dataWatcher.getWatchableObjectByte(9) > 0;

		if (var12 > 0) {
			boolean var4 = false;

			if (!this.getHasActivePotion()) {
				var4 = this.rand.nextBoolean();
			} else {
				var4 = this.rand.nextInt(15) == 0;
			}

			if (var13) {
				var4 &= this.rand.nextInt(5) == 0;
			}

			if (var4 && var12 > 0) {
				double var14 = (double)(var12 >> 16 & 255) / 255.0D;
				double var7 = (double)(var12 >> 8 & 255) / 255.0D;
				double var9 = (double)(var12 >> 0 & 255) / 255.0D;
				this.worldObj.spawnParticle(var13 ? "mobSpellAmbient" : "mobSpell", this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - (double)this.yOffset, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, var14, var7, var9);
			}
		}
	}

	public void clearActivePotions() {
		Iterator var1 = this.activePotionsMap.keySet().iterator();

		while (var1.hasNext()) {
			Integer var2 = (Integer)var1.next();
			PotionEffect var3 = (PotionEffect)this.activePotionsMap.get(var2);

			if (!this.worldObj.isRemote) {
				var1.remove();
				this.onFinishedPotionEffect(var3);
			}
		}
	}

	public Collection getActivePotionEffects() {
		return this.activePotionsMap.values();
	}

	public boolean isPotionActive(int par1) {
		return this.activePotionsMap.containsKey(Integer.valueOf(par1));
	}

	public boolean isPotionActive(Potion par1Potion) {
		return this.activePotionsMap.containsKey(Integer.valueOf(par1Potion.id));
	}

	/**
	 * returns the PotionEffect for the supplied Potion if it is active, null otherwise.
	 */
	public PotionEffect getActivePotionEffect(Potion par1Potion) {
		return (PotionEffect)this.activePotionsMap.get(Integer.valueOf(par1Potion.id));
	}

	/**
	 * adds a PotionEffect to the entity
	 */
	public void addPotionEffect(PotionEffect par1PotionEffect) {
		if (this.isPotionApplicable(par1PotionEffect)) {
			if (this.activePotionsMap.containsKey(Integer.valueOf(par1PotionEffect.getPotionID()))) {
				((PotionEffect)this.activePotionsMap.get(Integer.valueOf(par1PotionEffect.getPotionID()))).combine(par1PotionEffect);
				this.onChangedPotionEffect((PotionEffect)this.activePotionsMap.get(Integer.valueOf(par1PotionEffect.getPotionID())));
			} else {
				this.activePotionsMap.put(Integer.valueOf(par1PotionEffect.getPotionID()), par1PotionEffect);
				this.onNewPotionEffect(par1PotionEffect);
			}
		}
	}

	public boolean isPotionApplicable(PotionEffect par1PotionEffect) {
		if (this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
			int var2 = par1PotionEffect.getPotionID();

			if (var2 == Potion.regeneration.id || var2 == Potion.poison.id) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns true if this entity is undead.
	 */
	public boolean isEntityUndead() {
		return this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
	}

	/**
	 * Remove the speified potion effect from this entity.
	 */
	public void removePotionEffectClient(int par1) {
		this.activePotionsMap.remove(Integer.valueOf(par1));
	}

	/**
	 * Remove the specified potion effect from this entity.
	 */
	public void removePotionEffect(int par1) {
		PotionEffect var2 = (PotionEffect)this.activePotionsMap.remove(Integer.valueOf(par1));

		if (var2 != null) {
			this.onFinishedPotionEffect(var2);
		}
	}

	protected void onNewPotionEffect(PotionEffect par1PotionEffect) {
		this.potionsNeedUpdate = true;
	}

	protected void onChangedPotionEffect(PotionEffect par1PotionEffect) {
		this.potionsNeedUpdate = true;
	}

	protected void onFinishedPotionEffect(PotionEffect par1PotionEffect) {
		this.potionsNeedUpdate = true;
	}

	/**
	 * This method returns a value to be applied directly to entity speed, this factor is less than 1 when a slowdown
	 * potion effect is applied, more than 1 when a haste potion effect is applied and 2 for fleeing entities.
	 */
	public float getSpeedModifier() {
		float var1 = 1.0F;

		if (this.isPotionActive(Potion.moveSpeed)) {
			var1 *= 1.0F + 0.2F * (float)(this.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
		}

		if (this.isPotionActive(Potion.moveSlowdown)) {
			var1 *= 1.0F - 0.15F * (float)(this.getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
		}

		if (var1 < 0.0F) {
			var1 = 0.0F;
		}

		return var1;
	}

	/**
	 * Move the entity to the coordinates informed, but keep yaw/pitch values.
	 */
	public void setPositionAndUpdate(double par1, double par3, double par5) {
		this.setLocationAndAngles(par1, par3, par5, this.rotationYaw, this.rotationPitch);
	}

	/**
	 * If Animal, checks if the age timer is negative
	 */
	public boolean isChild() {
		return false;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}

	/**
	 * Renders broken item particles using the given ItemStack
	 */
	public void renderBrokenItemStack(ItemStack par1ItemStack) {
		this.playSound("random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);

		for (int var2 = 0; var2 < 5; ++var2) {
			Vec3 var3 = this.worldObj.getWorldVec3Pool().getVecFromPool(((double)this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
			var3.rotateAroundX(-this.rotationPitch * (float)Math.PI / 180.0F);
			var3.rotateAroundY(-this.rotationYaw * (float)Math.PI / 180.0F);
			Vec3 var4 = this.worldObj.getWorldVec3Pool().getVecFromPool(((double)this.rand.nextFloat() - 0.5D) * 0.3D, (double)(-this.rand.nextFloat()) * 0.6D - 0.3D, 0.6D);
			var4.rotateAroundX(-this.rotationPitch * (float)Math.PI / 180.0F);
			var4.rotateAroundY(-this.rotationYaw * (float)Math.PI / 180.0F);
			var4 = var4.addVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
			this.worldObj.spawnParticle("iconcrack_" + par1ItemStack.getItem().itemID, var4.xCoord, var4.yCoord, var4.zCoord, var3.xCoord, var3.yCoord + 0.05D, var3.zCoord);
		}
	}

	public int func_82143_as() {
		if (this.getAttackTarget() == null) {
			return 3;
		} else {
			int var1 = (int)((float)this.health - (float)this.getMaxHealth() * 0.33F);
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

	public ItemStack getCurrentArmor(int par1) {
		return this.equipment[par1 + 1];
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
		if (this.rand.nextFloat() < armorProbability[this.worldObj.difficultySetting]) {
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
				ItemStack var4 = this.getCurrentArmor(var3);

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

	/**
	 * Called whenever an item is picked up from walking over it. Args: pickedUpEntity, stackSize
	 */
	public void onItemPickup(Entity par1Entity, int par2) {
		if (!par1Entity.isDead && !this.worldObj.isRemote) {
			EntityTracker var3 = ((WorldServer)this.worldObj).getEntityTracker();

			if (par1Entity instanceof EntityItem) {
				var3.sendPacketToAllPlayersTrackingEntity(par1Entity, new Packet22Collect(par1Entity.entityId, this.entityId));
			}

			if (par1Entity instanceof EntityArrow) {
				var3.sendPacketToAllPlayersTrackingEntity(par1Entity, new Packet22Collect(par1Entity.entityId, this.entityId));
			}

			if (par1Entity instanceof EntityXPOrb) {
				var3.sendPacketToAllPlayersTrackingEntity(par1Entity, new Packet22Collect(par1Entity.entityId, this.entityId));
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
				return Item.helmetSteel;
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
				return Item.plateSteel;
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
				return Item.legsSteel;
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
				return Item.bootsSteel;
			} else if (par1 == 4) {
				return Item.bootsDiamond;
			}

		default:
			return null;
		}
	}

	protected void func_82162_bC() {
		if (this.getHeldItem() != null && this.rand.nextFloat() < enchantmentProbability[this.worldObj.difficultySetting]) {
			EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItem(), 5 + this.worldObj.difficultySetting * this.rand.nextInt(6));
		}

		for (int var1 = 0; var1 < 4; ++var1) {
			ItemStack var2 = this.getCurrentArmor(var1);

			if (var2 != null && this.rand.nextFloat() < armorEnchantmentProbability[this.worldObj.difficultySetting]) {
				EnchantmentHelper.addRandomEnchantment(this.rand, var2, 5 + this.worldObj.difficultySetting * this.rand.nextInt(6));
			}
		}
	}

	/**
	 * Initialize this creature.
	 */
	public void initCreature() {}

	/**
	 * Returns an integer indicating the end point of the swing animation, used by {@link #swingProgress} to provide a
	 * progress indicator. Takes dig speed enchantments into account.
	 */
	private int getArmSwingAnimationEnd() {
		return this.isPotionActive(Potion.digSpeed) ? 6 - (1 + this.getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1 : (this.isPotionActive(Potion.digSlowdown) ? 6 + (1 + this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6);
	}

	/**
	 * Swings the item the player is holding.
	 */
	public void swingItem() {
		if (!this.isSwingInProgress || this.swingProgressInt >= this.getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0) {
			this.swingProgressInt = -1;
			this.isSwingInProgress = true;

			if (this.worldObj instanceof WorldServer) {
				((WorldServer)this.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet18Animation(this, 1));
			}
		}
	}

	/**
	 * returns true if all the conditions for steering the entity are met. For pigs, this is true if it is being ridden by
	 * a player and the player is holding a carrot-on-a-stick
	 */
	public boolean canBeSteered() {
		return false;
	}

	/**
	 * counts the amount of arrows stuck in the entity. getting hit by arrows increases this, used in rendering
	 */
	public final int getArrowCountInEntity() {
		return this.dataWatcher.getWatchableObjectByte(10);
	}

	/**
	 * sets the amount of arrows stuck in the entity. used for rendering those
	 */
	public final void setArrowCountInEntity(int par1) {
		this.dataWatcher.updateObject(10, Byte.valueOf((byte)par1));
	}

	// Spout Start
	public EntityData getData() {
		return entityData;
	}

	public void setData(EntityData e) {
		this.entityData = e;
	}

	public String getCustomTextureUrl(byte id){
		if (getData().getCustomTextures() == null) {
			return null;
		}
		return getData().getCustomTextures().get(id);
	}

	public String getCustomTexture(byte id){
		if(getCustomTextureUrl(id) != null ) {
			return CustomTextureManager.getTexturePathFromUrl(getCustomTextureUrl(id));
		}
		return null;
	}

	public String getCustomTexture(EntitySkinType type, String def) {
		String tex = getCustomTexture(type.getId());
		if (tex == null) {
			tex = def;
		}
		return tex;
	}

	public void setCustomTexture(String url, byte id){
		if (url != null) {
			CustomTextureManager.downloadTexture(url);
		}
		if (getData().getCustomTextures() != null) {
			getData().getCustomTextures().put(id, url);
		}
	}

	public void setTextureToRender(byte textureToRender) {
		getData().setTextureToRender(textureToRender);
	}

	public byte getTextureToRender() {
		return getData().getTextureToRender();
	}
	// Spout End

	public EntityLiving func_94060_bK() {
		return (EntityLiving)(this.field_94063_bt.func_94550_c() != null ? this.field_94063_bt.func_94550_c() : (this.attackingPlayer != null ? this.attackingPlayer : (this.entityLivingToAttack != null ? this.entityLivingToAttack : null)));
	}

	/**
	 * Gets the username of the entity.
	 */
	public String getEntityName() {
		return this.func_94056_bM() ? this.func_94057_bL() : super.getEntityName();
	}

	public void func_94058_c(String par1Str) {
		this.dataWatcher.updateObject(5, par1Str);
	}

	public String func_94057_bL() {
		return this.dataWatcher.getWatchableObjectString(5);
	}

	public boolean func_94056_bM() {
		return this.dataWatcher.getWatchableObjectString(5).length() > 0;
	}

	public void func_94061_f(boolean par1) {
		this.dataWatcher.updateObject(6, Byte.valueOf((byte)(par1 ? 1 : 0)));
	}

	public boolean func_94062_bN() {
		return this.dataWatcher.getWatchableObjectByte(6) == 1;
	}

	public boolean func_94059_bO() {
		return this.func_94062_bN();
	}

	public void func_96120_a(int par1, float par2) {
		this.equipmentDropChances[par1] = par2;
	}

	public boolean canPickUpLoot() {
		return this.canPickUpLoot;
	}

	public void setCanPickUpLoot(boolean par1) {
		this.canPickUpLoot = par1;
	}

}
