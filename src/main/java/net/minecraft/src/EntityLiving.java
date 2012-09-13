package net.minecraft.src;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

// Spout Start
import org.spoutcraft.client.entity.CraftLivingEntity;
import org.spoutcraft.client.entity.EntityData;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.spoutcraftapi.entity.EntitySkinType;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
// Spout End

public abstract class EntityLiving extends Entity {
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
	public int health = this.getMaxHealth(); // Spout - protected to public!
	public int prevHealth;

	/**
	 * in each step in the damage calculations, this is set to the 'carryover' that would result if someone was damaged .25
	 * hearts (for example), and added to the damage in the next step
	 */
	protected int carryoverDamage;

	/** Number of ticks since this EntityLiving last produced its sound */
	private int livingSoundTime;

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
	public float prevLegYaw;
	public float legYaw;
	public float field_70754_ba;

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

	/**
	 * Set to 60 when hit by the player or the player's wolf, then decrements. Used to determine whether the entity should
	 * drop items on death.
	 */
	public int arrowHitTempCounter = 0;
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
	public int lastDamage = 0; // Spout - protected to public!

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
		this.navigator = new PathNavigate(this, par1World, 16.0F);
		this.senses = new EntitySenses(this);
		this.field_70770_ap = (float)(Math.random() + 1.0D) * 0.01F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.field_70769_ao = (float)Math.random() * 12398.0F;
		this.rotationYaw = (float)(Math.random() * Math.PI * 2.0D);
		this.rotationYawHead = this.rotationYaw;
		this.stepHeight = 0.5F;
		
		// Spout Start
		this.spoutEntity = new CraftLivingEntity(this);
		// Spout End
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

	public float func_70079_am() {
		return this.rotationYawHead;
	}

	/**
	 * Sets the head's yaw rotation of the entity.
	 */
	public void setHeadRotationYaw(float par1) {
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

	public boolean isExplosiveMob(Class par1Class) {
		return EntityCreeper.class != par1Class && EntityGhast.class != par1Class;
	}

	/**
	 * This function applies the benefits of growing back wool and faster growing up to the acting entity. (This function
	 * is used in the AIEatGrass)
	 */
	public void eatGrassBonus() {}

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
		this.revengeTimer = this.entityLivingToAttack != null ? 60 : 0;
	}

	protected void entityInit() {
		this.dataWatcher.addObject(8, Integer.valueOf(this.field_70748_f));
	}

	/**
	 * returns true if the entity provided in the argument can be seen. (Raytrace)
	 */
	public boolean canEntityBeSeen(Entity par1Entity) {
		return this.worldObj.rayTraceBlocks(Vec3.getVec3Pool().getVecFromPool(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), Vec3.getVec3Pool().getVecFromPool(par1Entity.posX, par1Entity.posY + (double)par1Entity.getEyeHeight(), par1Entity.posZ)) == null;
	}

	/**
	 * Returns the texture's file path as a String.
	 */
	public String getTexture() {
		// Spout Start
		String custom = getCustomTextureUrl(getTextureToRender());
		if(custom == null || CustomTextureManager.getTexturePathFromUrl(custom) == null){
			return texture;
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
			this.worldObj.playSoundAtEntity(this, var1, this.getSoundVolume(), this.getSoundPitch());
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

		if (this.isEntityAlive() && this.isInsideOfMaterial(Material.water) && !this.canBreatheUnderwater() && !this.activePotionsMap.containsKey(Integer.valueOf(Potion.waterBreathing.id))) {
			this.setAir(this.decreaseAirSupply(this.getAir()));

			if (this.getAir() == -20) {
				this.setAir(0);

				for (int var1 = 0; var1 < 8; ++var1) {
					float var2 = this.rand.nextFloat() - this.rand.nextFloat();
					float var3 = this.rand.nextFloat() - this.rand.nextFloat();
					float var4 = this.rand.nextFloat() - this.rand.nextFloat();
					this.worldObj.spawnParticle("bubble", this.posX + (double)var2, this.posY + (double)var3, this.posZ + (double)var4, this.motionX, this.motionY, this.motionZ);
				}

				this.attackEntityFrom(DamageSource.drown, 2);
			}

			this.extinguish();
		} else {
			this.setAir(maxAir); // Spout - 300 to maxAir
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

			if (!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && !this.isChild()) {
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
		return par1 - 1;
	}

	/**
	 * Get the experience points the entity currently has.
	 */
	protected int getExperiencePoints(EntityPlayer par1EntityPlayer) {
		return this.experienceValue;
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

		if (this.arrowHitTempCounter > 0) {
			if (this.arrowHitTimer <= 0) {
				this.arrowHitTimer = 60;
			}

			--this.arrowHitTimer;

			if (this.arrowHitTimer <= 0) {
				--this.arrowHitTempCounter;
			}
		}

		this.onLivingUpdate();
		double var1 = this.posX - this.prevPosX;
		double var3 = this.posZ - this.prevPosZ;
		float var5 = (float)(var1 * var1 + var3 * var3);
		float var6 = this.renderYawOffset;
		float var7 = 0.0F;
		this.field_70768_au = this.field_70766_av;
		float var8 = 0.0F;

		if (var5 > 0.0025000002F) {
			var8 = 1.0F;
			var7 = (float)Math.sqrt((double)var5) * 3.0F;
			var6 = (float)Math.atan2(var3, var1) * 180.0F / (float)Math.PI - 90.0F;
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
			this.health += par1;

			if (this.health > this.getMaxHealth()) {
				this.health = this.getMaxHealth();
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
		if (this.worldObj.isRemote) {
			return false;
		} else {
			this.entityAge = 0;

			if (this.health <= 0) {
				return false;
			} else if (par1DamageSource.fireDamage() && this.isPotionActive(Potion.fireResistance)) {
				return false;
			} else {
				this.legYaw = 1.5F;
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
						this.recentlyHit = 60;
						this.attackingPlayer = (EntityPlayer)var4;
					} else if (var4 instanceof EntityWolf) {
						EntityWolf var5 = (EntityWolf)var4;

						if (var5.isTamed()) {
							this.recentlyHit = 60;
							this.attackingPlayer = null;
						}
					}
				}

				if (var3) {
					this.worldObj.setEntityState(this, (byte)2);

					if (par1DamageSource != DamageSource.drown && par1DamageSource != DamageSource.field_76375_l) {
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
						this.worldObj.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), this.getSoundPitch());
					}

					this.onDeath(par1DamageSource);
				} else if (var3) {
					this.worldObj.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), this.getSoundPitch());
				}

				return true;
			}
		}
	}

	/**
	 * Gets the pitch of living sounds in living entities.
	 */
	private float getSoundPitch() {
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
		return 0;
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
		if (this.isPotionActive(Potion.resistance)) {
			int var3 = (this.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
			int var4 = 25 - var3;
			int var5 = par2 * var4 + this.carryoverDamage;
			par2 = var5 / 25;
			this.carryoverDamage = var5 % 25;
		}

		return par2;
	}

	/**
	 * Deals damage to the entity. If its a EntityPlayer then will take damage from the armor first and then health second
	 * with the reduced value. Args: damageAmount
	 */
	public void damageEntity(DamageSource par1DamageSource, int par2) { // Spout - protected to public!
		par2 = this.applyArmorCalculations(par1DamageSource, par2);
		par2 = this.applyPotionDamageCalculations(par1DamageSource, par2);
		this.health -= par2;
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
		return "damage.hurtflesh";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "damage.hurtflesh";
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

		if (this.scoreValue >= 0 && var2 != null) {
			var2.addToPlayerScore(this, this.scoreValue);
		}

		if (var2 != null) {
			var2.onKillEntity(this);
		}

		this.dead = true;

		if (!this.worldObj.isRemote) {
			int var3 = 0;

			if (var2 instanceof EntityPlayer) {
				var3 = EnchantmentHelper.getLootingModifier(((EntityPlayer)var2).inventory);
			}

			if (!this.isChild()) {
				this.dropFewItems(this.recentlyHit > 0, var3);

				if (this.recentlyHit > 0) {
					int var4 = this.rand.nextInt(200) - var3;

					if (var4 < 5) {
						this.dropRareDrop(var4 <= 0 ? 1 : 0);
					}
				}
			}
		}

		this.worldObj.setEntityState(this, (byte)3);
	}

	protected void dropRareDrop(int par1) {}

	/**
	 * Drop 0-2 items of this living's type
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
		
		par1 *= getData().getGravityMod(); // Spout - added gravity mod.
		
		int var2 = MathHelper.ceiling_float_int(par1 - 3.0F);

		if (var2 > 0) {
			if (var2 > 4) {
				this.worldObj.playSoundAtEntity(this, "damage.fallbig", 1.0F, 1.0F);
			} else {
				this.worldObj.playSoundAtEntity(this, "damage.fallsmall", 1.0F, 1.0F);
			}

			this.attackEntityFrom(DamageSource.fall, var2);
			int var3 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 0.20000000298023224D - (double)this.yOffset), MathHelper.floor_double(this.posZ));

			if (var3 > 0) {
				StepSound var4 = Block.blocksList[var3].stepSound;
				this.worldObj.playSoundAtEntity(this, var4.getStepSound(), var4.getVolume() * 0.5F, var4.getPitch() * 0.75F);
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
			this.moveFlying(par1, par2, ((float) ((this.isAIEnabled() ? 0.04F : 0.02F) * getData().getSwimmingMod()))); // Spout - added swimming mod
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.800000011920929D;
			this.motionY *= 0.800000011920929D;
			this.motionZ *= 0.800000011920929D;
			this.motionY -= 0.02D * getData().getGravityMod(); // Spout - added gravity modifier!

			if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var9, this.motionZ)) {
				this.motionY = 0.30000001192092896D;
			}
		} else if (this.handleLavaMovement() && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).capabilities.isFlying)) {
			var9 = this.posY;
			this.moveFlying(par1, par2, (float)(0.02F * getData().getSwimmingMod())); // Spout - added swimming modifier!
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
					org.spoutcraft.spoutcraftapi.World world = this.worldObj.world;
					org.spoutcraft.spoutcraftapi.block.Chunk chunk = world.getChunkAt(x, y, z);
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
					var5 = (float) (this.getAIMoveSpeed() * getData().getWalkingMod()); // Spout
				} else {
					var5 = (float) (this.landMovementFactor * getData().getWalkingMod()); // Spout
				}

				var5 *= var8;
			} else {
				var5 = (float) (this.jumpMovementFactor * getData().getAirspeedMod()); // Spout - added AirSpeed modifier!
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
					short customId = worldObj.world.getChunkAt(x, y, z).getCustomBlockId(x, y, z);
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

			this.motionY -= 0.08D * getData().getGravityMod(); // Spout - added gravity multipler!
			this.motionY *= 0.9800000190734863D;
			this.motionX *= (double)var3;
			this.motionZ *= (double)var3;
		}

		this.prevLegYaw = this.legYaw;
		var9 = this.posX - this.prevPosX;
		double var12 = this.posZ - this.prevPosZ;
		float var11 = MathHelper.sqrt_double(var9 * var9 + var12 * var12) * 4.0F;

		if (var11 > 1.0F) {
			var11 = 1.0F;
		}

		this.legYaw += (var11 - this.legYaw) * 0.4F;
		this.field_70754_ba += this.legYaw;
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
		par1NBTTagCompound.setShort("Health", (short)this.health);
		par1NBTTagCompound.setShort("HurtTime", (short)this.hurtTime);
		par1NBTTagCompound.setShort("DeathTime", (short)this.deathTime);
		par1NBTTagCompound.setShort("AttackTime", (short)this.attackTime);

		if (!this.activePotionsMap.isEmpty()) {
			NBTTagList var2 = new NBTTagList();
			Iterator var3 = this.activePotionsMap.values().iterator();

			while (var3.hasNext()) {
				PotionEffect var4 = (PotionEffect)var3.next();
				NBTTagCompound var5 = new NBTTagCompound();
				var5.setByte("Id", (byte)var4.getPotionID());
				var5.setByte("Amplifier", (byte)var4.getAmplifier());
				var5.setInteger("Duration", var4.getDuration());
				var2.appendTag(var5);
			}

			par1NBTTagCompound.setTag("ActiveEffects", var2);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		if (this.health < -32768) {
			this.health = -32768;
		}

		this.health = par1NBTTagCompound.getShort("Health");

		if (!par1NBTTagCompound.hasKey("Health")) {
			this.health = this.getMaxHealth();
		}

		this.hurtTime = par1NBTTagCompound.getShort("HurtTime");
		this.deathTime = par1NBTTagCompound.getShort("DeathTime");
		this.attackTime = par1NBTTagCompound.getShort("AttackTime");

		if (par1NBTTagCompound.hasKey("ActiveEffects")) {
			NBTTagList var2 = par1NBTTagCompound.getTagList("ActiveEffects");

			for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
				NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
				byte var5 = var4.getByte("Id");
				byte var6 = var4.getByte("Amplifier");
				int var7 = var4.getInteger("Duration");
				this.activePotionsMap.put(Integer.valueOf(var5), new PotionEffect(var5, var7, var6));
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
		float var9 = this.landMovementFactor;
		this.landMovementFactor *= this.getSpeedModifier();
		this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
		this.landMovementFactor = var9;
		this.worldObj.theProfiler.endSection();
		this.worldObj.theProfiler.startSection("push");

		if (!this.worldObj.isRemote) {
			List var2 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

			if (var2 != null && !var2.isEmpty()) {
				Iterator var10 = var2.iterator();

				while (var10.hasNext()) {
					Entity var4 = (Entity)var10.next();

					if (var4.canBePushed()) {
						var4.applyEntityCollision(this);
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
		this.motionY = 0.41999998688697815D * getData().getJumpingMod(); // Spout - added jumping modifier!

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
			var6 = this.posY + (double)this.getEyeHeight() - (var10.posY + (double)var10.getEyeHeight());
		} else {
			var6 = (par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / 2.0D - (this.posY + (double)this.getEyeHeight());
		}

		double var14 = (double)MathHelper.sqrt_double(var4 * var4 + var8 * var8);
		float var12 = (float)(Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
		float var13 = (float)(-(Math.atan2(var6, var14) * 180.0D / Math.PI));
		this.rotationPitch = -this.updateRotation(this.rotationPitch, var13, par3);
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
			return Vec3.getVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
		} else {
			double var2 = this.prevPosX + (this.posX - this.prevPosX) * (double)par1;
			double var4 = this.prevPosY + (this.posY - this.prevPosY) * (double)par1;
			double var6 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double)par1;
			return Vec3.getVec3Pool().getVecFromPool(var2, var4, var6);
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
			return Vec3.getVec3Pool().getVecFromPool((double)(var3 * var4), (double)var5, (double)(var2 * var4));
		} else {
			var2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * par1;
			var3 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * par1;
			var4 = MathHelper.cos(-var3 * 0.017453292F - (float)Math.PI);
			var5 = MathHelper.sin(-var3 * 0.017453292F - (float)Math.PI);
			float var6 = -MathHelper.cos(-var2 * 0.017453292F);
			float var7 = MathHelper.sin(-var2 * 0.017453292F);
			return Vec3.getVec3Pool().getVecFromPool((double)(var5 * var6), (double)var7, (double)(var4 * var6));
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

	/**
	 * Returns the item that this EntityLiving is holding, if any.
	 */
	public ItemStack getHeldItem() {
		return null;
	}

	public void handleHealthUpdate(byte par1) {
		if (par1 == 2) {
			this.legYaw = 1.5F;
			this.hurtResistantTime = this.maxHurtResistantTime;
			this.hurtTime = this.maxHurtTime = 10;
			this.attackedAtYaw = 0.0F;
			this.worldObj.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.attackEntityFrom(DamageSource.generic, 0);
		} else if (par1 == 3) {
			this.worldObj.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
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
	public int getItemIcon(ItemStack par1ItemStack, int par2) {
		return par1ItemStack.getIconIndex();
	}

	protected void updatePotionEffects() {
		Iterator var1 = this.activePotionsMap.keySet().iterator();

		while (var1.hasNext()) {
			Integer var2 = (Integer)var1.next();
			PotionEffect var3 = (PotionEffect)this.activePotionsMap.get(var2);

			if (!var3.onUpdate(this) /*&& !this.worldObj.isRemote*/) { // Spout - better way to solve this? <- depends, what are we solving?
				var1.remove();
				this.onFinishedPotionEffect(var3);
			}
		}

		int var9;

		if (this.potionsNeedUpdate) {
			if (!this.worldObj.isRemote) {
				if (this.activePotionsMap.isEmpty()) {
					this.dataWatcher.updateObject(8, Integer.valueOf(0));
				} else {
					var9 = PotionHelper.calcPotionLiquidColor(this.activePotionsMap.values());
					this.dataWatcher.updateObject(8, Integer.valueOf(var9));
				}
			}

			this.potionsNeedUpdate = false;
		}

		if (this.rand.nextBoolean()) {
			var9 = this.dataWatcher.getWatchableObjectInt(8);

			if (var9 > 0) {
				double var10 = (double)(var9 >> 16 & 255) / 255.0D;
				double var5 = (double)(var9 >> 8 & 255) / 255.0D;
				double var7 = (double)(var9 >> 0 & 255) / 255.0D;
				this.worldObj.spawnParticle("mobSpell", this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - (double)this.yOffset, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, var10, var5, var7);
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
	 * input is the potion id to remove from the current active potion effects
	 */
	public void removePotionEffect(int par1) {
		this.activePotionsMap.remove(Integer.valueOf(par1));
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
	protected float getSpeedModifier() {
		float var1 = 1.0F;

		if (this.isPotionActive(Potion.moveSpeed)) {
			var1 *= 1.0F + 0.2F * (float)(this.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
		}

		if (this.isPotionActive(Potion.moveSlowdown)) {
			var1 *= 1.0F - 0.15F * (float)(this.getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
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
		this.worldObj.playSoundAtEntity(this, "random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);

		for (int var2 = 0; var2 < 5; ++var2) {
			Vec3 var3 = Vec3.getVec3Pool().getVecFromPool(((double)this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
			var3.rotateAroundX(-this.rotationPitch * (float)Math.PI / 180.0F);
			var3.rotateAroundY(-this.rotationYaw * (float)Math.PI / 180.0F);
			Vec3 var4 = Vec3.getVec3Pool().getVecFromPool(((double)this.rand.nextFloat() - 0.5D) * 0.3D, (double)(-this.rand.nextFloat()) * 0.6D - 0.3D, 0.6D);
			var4.rotateAroundX(-this.rotationPitch * (float)Math.PI / 180.0F);
			var4.rotateAroundY(-this.rotationYaw * (float)Math.PI / 180.0F);
			var4 = var4.addVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
			this.worldObj.spawnParticle("iconcrack_" + par1ItemStack.getItem().shiftedIndex, var4.xCoord, var4.yCoord, var4.zCoord, var3.xCoord, var3.yCoord + 0.05D, var3.zCoord);
		}
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
	
}