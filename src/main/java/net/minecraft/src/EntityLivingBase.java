package net.minecraft.src;

import com.prupe.mcpatcher.cc.ColorizeEntity;
import com.prupe.mcpatcher.mob.MobRandomizer$ExtraInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;


//Spout Start
import org.spoutcraft.api.entity.EntitySkinType;
import org.spoutcraft.api.material.CustomBlock;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.client.entity.EntityData;
import org.spoutcraft.client.io.CustomTextureManager;
//Spout End

public abstract class EntityLivingBase extends Entity {
	private static final UUID sprintingSpeedBoostModifierUUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
	private static final AttributeModifier sprintingSpeedBoostModifier = (new AttributeModifier(sprintingSpeedBoostModifierUUID, "Sprinting speed boost", 0.30000001192092896D, 2)).setSaved(false);
	private BaseAttributeMap attributeMap;
	private final CombatTracker _combatTracker = new CombatTracker(this);
	private final HashMap activePotionsMap = new HashMap();

	/** The equipment this mob was previously wearing, used for syncing. */
	private final ItemStack[] previousEquipment = new ItemStack[5];

	/** Whether an arm swing is currently in progress. */
	public boolean isSwingInProgress;
	public int swingProgressInt;
	public int arrowHitTimer;
	public float prevHealth;
	
	/**
	 * The amount of time remaining this entity should act 'hurt'. (Visual appearance of red tint)
	 */
	public int hurtTime;

	/** What the hurt time was max set to last. */
	public int maxHurtTime;

	/** The yaw at which this entity was last attacked from. */
	public float attackedAtYaw;

	/**
	 * The amount of time remaining this entity should act 'dead', i.e. have a corpse in the world.
	 */
	public int deathTime;
	public int attackTime;
	public float prevSwingProgress;
	public float swingProgress;
	public float prevLimbSwingAmount;
	public float limbSwingAmount;

	/**
	 * Only relevant when limbYaw is not 0(the entity is moving). Influences where in its swing legs and arms currently
	 * are.
	 */
	public float limbSwing;
	public int maxHurtResistantTime = 20;
	public float prevCameraPitch;
	public float cameraPitch;
	public float field_70769_ao;
	public float field_70770_ap;
	public float renderYawOffset;
	public float prevRenderYawOffset;

	/** Entity head rotation yaw */
	public float rotationYawHead;

	/** Entity head rotation yaw at previous tick */
	public float prevRotationYawHead;

	/**
	 * A factor used to determine how far this entity will move each tick if it is jumping or falling.
	 */
	public float jumpMovementFactor = 0.02F;

	/** The most recent player that has attacked this entity */
	protected EntityPlayer attackingPlayer;

	/**
	 * Set to 60 when hit by the player or the player's wolf, then decrements. Used to determine whether the entity should
	 * drop items on death.
	 */
	protected int recentlyHit;

	/**
	 * This gets set on entity death, but never used. Looks like a duplicate of isDead
	 */
	protected boolean dead;

	/** Holds the living entity age, used to control the despawn. */
	protected int entityAge;
	protected float field_70768_au;
	protected float field_110154_aX;
	protected float field_70764_aw;
	protected float field_70763_ax;
	protected float field_70741_aB;

	/** The score value of the Mob, the amount of points the mob is worth. */
	protected int scoreValue;

	/**
	 * Damage taken in the last hit. Mobs are resistant to damage less than this for a short time after taking damage.
	 */
	
	//Spout Start
	protected String texture = "/mob/char.png";
	public float lastDamage;
	private EntityData entityData = new EntityData();
	public String username = null;
	public String displayName = null;	
	public int maxAir = 300;
	// Spout End

	/** used to check whether entity is jumping. */
	protected boolean isJumping;
	public float moveStrafing;
	public float moveForward;
	protected float randomYawVelocity;

	/**
	 * The number of updates over which the new position and rotation are to be applied to the entity.
	 */
	protected int newPosRotationIncrements;

	/** The new X position to be applied to the entity. */
	protected double newPosX;

	/** The new Y position to be applied to the entity. */
	protected double newPosY;
	protected double newPosZ;

	/** The new yaw rotation to be applied to the entity. */
	protected double newRotationYaw;

	/** The new yaw rotation to be applied to the entity. */
	protected double newRotationPitch;

	/** Whether the DataWatcher needs to be updated with the active potions */
	private boolean potionsNeedUpdate = true;

	/** is only being set, has no uses as of MC 1.1 */
	private EntityLivingBase entityLivingToAttack;
	private int revengeTimer;
	private EntityLivingBase lastAttacker;

	/** Holds the value of ticksExisted when setLastAttacker was last called. */
	private int lastAttackerTime;

	/**
	 * A factor used to determine how far this entity will move each tick if it is walking on land. Adjusted by speed, and
	 * slipperiness of the current block.
	 */
	private float landMovementFactor;

	/** Number of ticks since last jump */
	private int jumpTicks;
	private float field_110151_bq;
	public int overridePotionColor;

	public EntityLivingBase(World par1World) {
		super(par1World);
		this.applyEntityAttributes();
		this.setHealth(this.getMaxHealth());
		this.preventEntitySpawning = true;
		this.field_70770_ap = (float)(Math.random() + 1.0D) * 0.01F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.field_70769_ao = (float)Math.random() * 12398.0F;
		this.rotationYaw = (float)(Math.random() * Math.PI * 2.0D);
		this.rotationYawHead = this.rotationYaw;
		this.stepHeight = 0.5F;
	}

	protected void entityInit() {
		this.dataWatcher.addObject(7, Integer.valueOf(0));
		this.dataWatcher.addObject(8, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(9, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(6, Float.valueOf(1.0F));
	}

	protected void applyEntityAttributes() {
		this.getAttributeMap().func_111150_b(SharedMonsterAttributes.maxHealth);
		this.getAttributeMap().func_111150_b(SharedMonsterAttributes.knockbackResistance);
		this.getAttributeMap().func_111150_b(SharedMonsterAttributes.movementSpeed);

		if (!this.isAIEnabled()) {
			this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.10000000149011612D);
		}
	}

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

	public boolean canBreatheUnderwater() {
		return false;
	}

	/**
	 * Gets called every tick from main Entity class
	 */
	public void onEntityUpdate() {
		this.prevSwingProgress = this.swingProgress;
		super.onEntityUpdate();
		this.worldObj.theProfiler.startSection("livingEntityBaseTick");

		if (this.isEntityAlive() && this.isEntityInsideOpaqueBlock()) {
			this.attackEntityFrom(DamageSource.inWall, 1.0F);
		}

		if (this.isImmuneToFire() || this.worldObj.isRemote) {
			this.extinguish();
		}

		boolean var1 = this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.disableDamage;

		if (this.isEntityAlive() && this.isInsideOfMaterial(Material.water)) {
			if (!this.canBreatheUnderwater() && !this.isPotionActive(Potion.waterBreathing.id) && !var1) {
				this.setAir(this.decreaseAirSupply(this.getAir()));

				if (this.getAir() == -20) {
					this.setAir(0);

					for (int var2 = 0; var2 < 8; ++var2) {
						float var3 = this.rand.nextFloat() - this.rand.nextFloat();
						float var4 = this.rand.nextFloat() - this.rand.nextFloat();
						float var5 = this.rand.nextFloat() - this.rand.nextFloat();
						this.worldObj.spawnParticle("bubble", this.posX + (double)var3, this.posY + (double)var4, this.posZ + (double)var5, this.motionX, this.motionY, this.motionZ);
					}

					this.attackEntityFrom(DamageSource.drown, 2.0F);
				}
			}

			this.extinguish();

			if (!this.worldObj.isRemote && this.isRiding() && this.ridingEntity instanceof EntityLivingBase) {
				this.mountEntity((Entity)null);
			}
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

		if (this.getHealth() <= 0.0F) {
			this.onDeathUpdate();
		}

		if (this.recentlyHit > 0) {
			--this.recentlyHit;
		} else {
			this.attackingPlayer = null;
		}

		if (this.lastAttacker != null && !this.lastAttacker.isEntityAlive()) {
			this.lastAttacker = null;
		}

		if (this.entityLivingToAttack != null && !this.entityLivingToAttack.isEntityAlive()) {
			this.setRevengeTarget((EntityLivingBase)null);
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
	 * If Animal, checks if the age timer is negative
	 */
	public boolean isChild() {
		return false;
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
		return 0;
	}

	/**
	 * Only use is to identify if class is an instance of player for experience dropping
	 */
	protected boolean isPlayer() {
		return false;
	}

	public Random getRNG() {
		return this.rand;
	}

	public EntityLivingBase getAITarget() {
		return this.entityLivingToAttack;
	}

	public int func_142015_aE() {
		return this.revengeTimer;
	}

	public void setRevengeTarget(EntityLivingBase par1EntityLivingBase) {
		this.entityLivingToAttack = par1EntityLivingBase;
		this.revengeTimer = this.ticksExisted;
	}

	public EntityLivingBase getLastAttacker() {
		return this.lastAttacker;
	}

	public int getLastAttackerTime() {
		return this.lastAttackerTime;
	}

	public void setLastAttacker(Entity par1Entity) {
		if (par1Entity instanceof EntityLivingBase) {
			this.lastAttacker = (EntityLivingBase)par1Entity;
		} else {
			this.lastAttacker = null;
		}

		this.lastAttackerTime = this.ticksExisted;
	}

	public int getAge() {
		return this.entityAge;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		MobRandomizer$ExtraInfo.writeToNBT(this, par1NBTTagCompound);
		par1NBTTagCompound.setFloat("HealF", this.getHealth());
		par1NBTTagCompound.setShort("Health", (short)((int)Math.ceil((double)this.getHealth())));
		par1NBTTagCompound.setShort("HurtTime", (short)this.hurtTime);
		par1NBTTagCompound.setShort("DeathTime", (short)this.deathTime);
		par1NBTTagCompound.setShort("AttackTime", (short)this.attackTime);
		par1NBTTagCompound.setFloat("AbsorptionAmount", this.getAbsorptionAmount());
		ItemStack[] var2 = this.getLastActiveItems();
		int var3 = var2.length;
		int var4;
		ItemStack var5;

		for (var4 = 0; var4 < var3; ++var4) {
			var5 = var2[var4];

			if (var5 != null) {
				this.attributeMap.removeAttributeModifiers(var5.getAttributeModifiers());
			}
		}

		par1NBTTagCompound.setTag("Attributes", SharedMonsterAttributes.func_111257_a(this.getAttributeMap()));
		var2 = this.getLastActiveItems();
		var3 = var2.length;

		for (var4 = 0; var4 < var3; ++var4) {
			var5 = var2[var4];

			if (var5 != null) {
				this.attributeMap.applyAttributeModifiers(var5.getAttributeModifiers());
			}
		}

		if (!this.activePotionsMap.isEmpty()) {
			NBTTagList var6 = new NBTTagList();
			Iterator var7 = this.activePotionsMap.values().iterator();

			while (var7.hasNext()) {
				PotionEffect var8 = (PotionEffect)var7.next();
				var6.appendTag(var8.writeCustomPotionEffectToNBT(new NBTTagCompound()));
			}

			par1NBTTagCompound.setTag("ActiveEffects", var6);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		MobRandomizer$ExtraInfo.readFromNBT(this, par1NBTTagCompound);
		this.setAbsorptionAmount(par1NBTTagCompound.getFloat("AbsorptionAmount"));

		if (par1NBTTagCompound.hasKey("Attributes") && this.worldObj != null && !this.worldObj.isRemote) {
			SharedMonsterAttributes.func_111260_a(this.getAttributeMap(), par1NBTTagCompound.getTagList("Attributes"), this.worldObj == null ? null : this.worldObj.getWorldLogAgent());
		}

		if (par1NBTTagCompound.hasKey("ActiveEffects")) {
			NBTTagList var2 = par1NBTTagCompound.getTagList("ActiveEffects");

			for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
				NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
				PotionEffect var5 = PotionEffect.readCustomPotionEffectFromNBT(var4);
				this.activePotionsMap.put(Integer.valueOf(var5.getPotionID()), var5);
			}
		}

		if (par1NBTTagCompound.hasKey("HealF")) {
			this.setHealth(par1NBTTagCompound.getFloat("HealF"));
		} else {
			NBTBase var6 = par1NBTTagCompound.getTag("Health");

			if (var6 == null) {
				this.setHealth(this.getMaxHealth());
			} else if (var6.getId() == 5) {
				this.setHealth(((NBTTagFloat)var6).data);
			} else if (var6.getId() == 2) {
				this.setHealth((float)((NBTTagShort)var6).data);
			}
		}

		this.hurtTime = par1NBTTagCompound.getShort("HurtTime");
		this.deathTime = par1NBTTagCompound.getShort("DeathTime");
		this.attackTime = par1NBTTagCompound.getShort("AttackTime");
	}

	protected void updatePotionEffects() {
		Iterator var1 = this.activePotionsMap.keySet().iterator();

		while (var1.hasNext()) {
			Integer var2 = (Integer)var1.next();
			PotionEffect var3 = (PotionEffect)this.activePotionsMap.get(var2);

			if (!var3.onUpdate(this)) {
				if (!this.worldObj.isRemote) {
					var1.remove();
					this.onFinishedPotionEffect(var3);
				}
			} else if (var3.getDuration() % 600 == 0) {
				this.onChangedPotionEffect(var3, false);
			}
		}

		int var11;

		if (this.potionsNeedUpdate) {
			if (this.activePotionsMap.isEmpty()) {
				this.dataWatcher.updateObject(8, Byte.valueOf((byte)0));
				this.dataWatcher.updateObject(7, Integer.valueOf(0));
				this.overridePotionColor = 0;
				this.setInvisible(false);
			} else {
				var11 = PotionHelper.calcPotionLiquidColor(this.activePotionsMap.values());
				this.dataWatcher.updateObject(8, Byte.valueOf((byte)(PotionHelper.func_82817_b(this.activePotionsMap.values()) ? 1 : 0)));
				this.dataWatcher.updateObject(7, Integer.valueOf(var11));
				this.overridePotionColor = var11;
				this.setInvisible(this.isPotionActive(Potion.invisibility.id));
			}

			this.potionsNeedUpdate = false;
		}

		var11 = ColorizeEntity.getPotionEffectColor(this.dataWatcher.getWatchableObjectInt(7), this);
		boolean var12 = this.dataWatcher.getWatchableObjectByte(8) > 0;

		if (var11 > 0) {
			boolean var4 = false;

			if (!this.isInvisible()) {
				var4 = this.rand.nextBoolean();
			} else {
				var4 = this.rand.nextInt(15) == 0;
			}

			if (var12) {
				var4 &= this.rand.nextInt(5) == 0;
			}

			if (var4 && var11 > 0) {
				double var5 = (double)(var11 >> 16 & 255) / 255.0D;
				double var7 = (double)(var11 >> 8 & 255) / 255.0D;
				double var9 = (double)(var11 >> 0 & 255) / 255.0D;
				this.worldObj.spawnParticle(var12 ? "mobSpellAmbient" : "mobSpell", this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - (double)this.yOffset, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, var5, var7, var9);
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
				this.onChangedPotionEffect((PotionEffect)this.activePotionsMap.get(Integer.valueOf(par1PotionEffect.getPotionID())), true);
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

		if (!this.worldObj.isRemote) {
			Potion.potionTypes[par1PotionEffect.getPotionID()].applyAttributesModifiersToEntity(this, this.getAttributeMap(), par1PotionEffect.getAmplifier());
		}
	}

	protected void onChangedPotionEffect(PotionEffect par1PotionEffect, boolean par2) {
		this.potionsNeedUpdate = true;

		if (par2 && !this.worldObj.isRemote) {
			Potion.potionTypes[par1PotionEffect.getPotionID()].removeAttributesModifiersFromEntity(this, this.getAttributeMap(), par1PotionEffect.getAmplifier());
			Potion.potionTypes[par1PotionEffect.getPotionID()].applyAttributesModifiersToEntity(this, this.getAttributeMap(), par1PotionEffect.getAmplifier());
		}
	}

	protected void onFinishedPotionEffect(PotionEffect par1PotionEffect) {
		this.potionsNeedUpdate = true;

		if (!this.worldObj.isRemote) {
			Potion.potionTypes[par1PotionEffect.getPotionID()].removeAttributesModifiersFromEntity(this, this.getAttributeMap(), par1PotionEffect.getAmplifier());
		}
	}

	/**
	 * Heal living entity (param: amount of half-hearts)
	 */
	public void heal(float par1) {
		float var2 = this.getHealth();

		if (var2 > 0.0F) {
			this.setHealth(var2 + par1);
		}
	}

	public final float getHealth() {
		return this.dataWatcher.getWatchableObjectFloat(6);
	}

	public void setHealth(float par1) {
		this.dataWatcher.updateObject(6, Float.valueOf(MathHelper.clamp_float(par1, 0.0F, this.getMaxHealth())));
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (this.isEntityInvulnerable()) {
			return false;
		} else if (this.worldObj.isRemote) {
			return false;
		} else {
			this.entityAge = 0;

			if (this.getHealth() <= 0.0F) {
				return false;
			} else if (par1DamageSource.isFireDamage() && this.isPotionActive(Potion.fireResistance)) {
				return false;
			} else {
				if ((par1DamageSource == DamageSource.anvil || par1DamageSource == DamageSource.fallingBlock) && this.getCurrentItemOrArmor(4) != null) {
					this.getCurrentItemOrArmor(4).damageItem((int)(par2 * 4.0F + this.rand.nextFloat() * par2 * 2.0F), this);
					par2 *= 0.75F;
				}

				this.limbSwingAmount = 1.5F;
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
					this.prevHealth = this.getHealth();
					this.hurtResistantTime = this.maxHurtResistantTime;
					this.damageEntity(par1DamageSource, par2);
					this.hurtTime = this.maxHurtTime = 10;
				}

				this.attackedAtYaw = 0.0F;
				Entity var4 = par1DamageSource.getEntity();

				if (var4 != null) {
					if (var4 instanceof EntityLivingBase) {
						this.setRevengeTarget((EntityLivingBase)var4);
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

				if (this.getHealth() <= 0.0F) {
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

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource par1DamageSource) {
		Entity var2 = par1DamageSource.getEntity();
		EntityLivingBase var3 = this.func_94060_bK();

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
				var4 = EnchantmentHelper.getLootingModifier((EntityLivingBase)var2);
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

	/**
	 * Drop the equipment for this entity.
	 */
	protected void dropEquipment(boolean par1, int par2) {}

	/**
	 * knocks back this entity
	 */
	public void knockBack(Entity par1Entity, float par2, double par3, double par5) {
		if (this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue()) {
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

	protected void dropRareDrop(int par1) {}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
	 * par2 - Level of Looting used to kill this mob.
	 */
	protected void dropFewItems(boolean par1, int par2) {}

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
	 * Checks whether target entity is alive.
	 */
	public boolean isEntityAlive() {
		return !this.isDead && this.getHealth() > 0.0F;
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float par1) {
		super.fall(par1);
		// Spout Start - Gravity mod
		par1 *= getData().getGravityMod();
		// Spout End
		PotionEffect var2 = this.getActivePotionEffect(Potion.jump);
		float var3 = var2 != null ? (float)(var2.getAmplifier() + 1) : 0.0F;
		int var4 = MathHelper.ceiling_float_int(par1 - 3.0F - var3);

		if (var4 > 0) {
			if (var4 > 4) {
				this.playSound("damage.fallbig", 1.0F, 1.0F);
			} else {
				this.playSound("damage.fallsmall", 1.0F, 1.0F);
			}

			this.attackEntityFrom(DamageSource.fall, (float)var4);
			int var5 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 0.20000000298023224D - (double)this.yOffset), MathHelper.floor_double(this.posZ));

			if (var5 > 0) {
				StepSound var6 = Block.blocksList[var5].stepSound;
				this.playSound(var6.getStepSound(), var6.getVolume() * 0.5F, var6.getPitch() * 0.75F);
			}
		}
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

	protected void damageArmor(float par1) {}

	/**
	 * Reduces damage, depending on armor
	 */
	protected float applyArmorCalculations(DamageSource par1DamageSource, float par2) {
		if (!par1DamageSource.isUnblockable()) {
			int var3 = 25 - this.getTotalArmorValue();
			float var4 = par2 * (float)var3;
			this.damageArmor(par2);
			par2 = var4 / 25.0F;
		}

		return par2;
	}

	/**
	 * Reduces damage, depending on potions
	 */
	protected float applyPotionDamageCalculations(DamageSource par1DamageSource, float par2) {
		if (this instanceof EntityZombie) {
			par2 = par2;
		}

		int var3;
		int var4;
		float var5;

		if (this.isPotionActive(Potion.resistance) && par1DamageSource != DamageSource.outOfWorld) {
			var3 = (this.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
			var4 = 25 - var3;
			var5 = par2 * (float)var4;
			par2 = var5 / 25.0F;
		}

		if (par2 <= 0.0F) {
			return 0.0F;
		} else {
			var3 = EnchantmentHelper.getEnchantmentModifierDamage(this.getLastActiveItems(), par1DamageSource);

			if (var3 > 20) {
				var3 = 20;
			}

			if (var3 > 0 && var3 <= 20) {
				var4 = 25 - var3;
				var5 = par2 * (float)var4;
				par2 = var5 / 25.0F;
			}

			return par2;
		}
	}

	/**
	 * Deals damage to the entity. If its a EntityPlayer then will take damage from the armor first and then health second
	 * with the reduced value. Args: damageAmount
	 */
	// Spout Start Protected > Public
	public void damageEntity(DamageSource par1DamageSource, float par2) {
		if (!this.isEntityInvulnerable()) {
			par2 = this.applyArmorCalculations(par1DamageSource, par2);
			par2 = this.applyPotionDamageCalculations(par1DamageSource, par2);
			float var3 = par2;
			par2 = Math.max(par2 - this.getAbsorptionAmount(), 0.0F);
			this.setAbsorptionAmount(this.getAbsorptionAmount() - (var3 - par2));

			if (par2 != 0.0F) {
				float var4 = this.getHealth();
				this.setHealth(var4 - par2);
				this.func_110142_aN().func_94547_a(par1DamageSource, var4, par2);
				this.setAbsorptionAmount(this.getAbsorptionAmount() - par2);
			}
		}
	}

	public CombatTracker func_110142_aN() {
		return this._combatTracker;
	}

	public EntityLivingBase func_94060_bK() {
		return (EntityLivingBase)(this._combatTracker.func_94550_c() != null ? this._combatTracker.func_94550_c() : (this.attackingPlayer != null ? this.attackingPlayer : (this.entityLivingToAttack != null ? this.entityLivingToAttack : null)));
	}

	public final float getMaxHealth() {
		return (float)this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
	}

	/**
	 * counts the amount of arrows stuck in the entity. getting hit by arrows increases this, used in rendering
	 */
	public final int getArrowCountInEntity() {
		return this.dataWatcher.getWatchableObjectByte(9);
	}

	/**
	 * sets the amount of arrows stuck in the entity. used for rendering those
	 */
	public final void setArrowCountInEntity(int par1) {
		this.dataWatcher.updateObject(9, Byte.valueOf((byte)par1));
	}

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

	public void handleHealthUpdate(byte par1) {
		if (par1 == 2) {
			this.limbSwingAmount = 1.5F;
			this.hurtResistantTime = this.maxHurtResistantTime;
			this.hurtTime = this.maxHurtTime = 10;
			this.attackedAtYaw = 0.0F;
			this.playSound(this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.attackEntityFrom(DamageSource.generic, 0.0F);
		} else if (par1 == 3) {
			this.playSound(this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.setHealth(0.0F);
			this.onDeath(DamageSource.generic);
		} else {
			super.handleHealthUpdate(par1);
		}
	}

	/**
	 * sets the dead flag. Used when you fall off the bottom of the world.
	 */
	protected void kill() {
		this.attackEntityFrom(DamageSource.outOfWorld, 4.0F);
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

	public AttributeInstance getEntityAttribute(Attribute par1Attribute) {
		return this.getAttributeMap().getAttributeInstance(par1Attribute);
	}

	public BaseAttributeMap getAttributeMap() {
		if (this.attributeMap == null) {
			this.attributeMap = new ServersideAttributeMap();
		}

		return this.attributeMap;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}

	/**
	 * Returns the item that this EntityLiving is holding, if any.
	 */
	public abstract ItemStack getHeldItem();

	/**
	 * 0 = item, 1-n is armor
	 */
	public abstract ItemStack getCurrentItemOrArmor(int var1);

	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
	 */
	public abstract void setCurrentItemOrArmor(int var1, ItemStack var2);

	/**
	 * Set sprinting switch for Entity.
	 */
	public void setSprinting(boolean par1) {
		super.setSprinting(par1);
		AttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

		if (var2.getModifier(sprintingSpeedBoostModifierUUID) != null) {
			var2.removeModifier(sprintingSpeedBoostModifier);
		}

		if (par1) {
			var2.applyModifier(sprintingSpeedBoostModifier);
		}
	}

	public abstract ItemStack[] getLastActiveItems();

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume() {
		return 1.0F;
	}

	/**
	 * Gets the pitch of living sounds in living entities.
	 */
	protected float getSoundPitch() {
		return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
	}

	/**
	 * Dead and sleeping entities cannot move
	 */
	protected boolean isMovementBlocked() {
		return this.getHealth() <= 0.0F;
	}

	/**
	 * Move the entity to the coordinates informed, but keep yaw/pitch values.
	 */
	public void setPositionAndUpdate(double par1, double par3, double par5) {
		this.setLocationAndAngles(par1, par3, par5, this.rotationYaw, this.rotationPitch);
	}

	/**
	 * Moves the entity to a position out of the way of its mount.
	 */
	public void dismountEntity(Entity par1Entity) {
		double var3 = par1Entity.posX;
		double var5 = par1Entity.boundingBox.minY + (double)par1Entity.height;
		double var7 = par1Entity.posZ;

		for (double var9 = -1.5D; var9 < 2.0D; ++var9) {
			for (double var11 = -1.5D; var11 < 2.0D; ++var11) {
				if (var9 != 0.0D || var11 != 0.0D) {
					int var13 = (int)(this.posX + var9);
					int var14 = (int)(this.posZ + var11);
					AxisAlignedBB var2 = this.boundingBox.getOffsetBoundingBox(var9, 1.0D, var11);

					if (this.worldObj.getCollidingBlockBounds(var2).isEmpty()) {
						if (this.worldObj.doesBlockHaveSolidTopSurface(var13, (int)this.posY, var14)) {
							this.setPositionAndUpdate(this.posX + var9, this.posY + 1.0D, this.posZ + var11);
							return;
						}

						if (this.worldObj.doesBlockHaveSolidTopSurface(var13, (int)this.posY - 1, var14) || this.worldObj.getBlockMaterial(var13, (int)this.posY - 1, var14) == Material.water) {
							var3 = this.posX + var9;
							var5 = this.posY + 1.0D;
							var7 = this.posZ + var11;
						}
					}
				}
			}
		}

		this.setPositionAndUpdate(var3, var5, var7);
	}

	public boolean getAlwaysRenderNameTagForRender() {
		return false;
	}

	/**
	 * Gets the Icon Index of the item currently held
	 */
	public Icon getItemIcon(ItemStack par1ItemStack, int par2) {
		return par1ItemStack.getIconIndex();
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
	 * Moves the entity based on the specified heading.  Args: strafe, forward
	 */
	public void moveEntityWithHeading(float par1, float par2) {
		double var10;

		if (this.isInWater() && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).capabilities.isFlying)) {
			var10 = this.posY;
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

			if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var10, this.motionZ)) {
				this.motionY = 0.30000001192092896D;
			}
		} else if (this.handleLavaMovement() && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).capabilities.isFlying)) {
			var10 = this.posY;
			// Spout Start - Added swimming modifier
			this.moveFlying(par1, par2, (float)(0.02F * getData().getSwimmingMod()));
			// Spout End
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
			this.motionY -= 0.02D;

			if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var10, this.motionZ)) {
				this.motionY = 0.30000001192092896D;
			}
		} else {
			float var3 = 0.91F;

			if (this.onGround) {
				var3 = 0.54600006F;
				int var4 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

				if (var4 > 0) {
					var3 = Block.blocksList[var4].slipperiness * 0.91F;
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
					org.spoutcraft.client.block.SpoutcraftChunk chunk = org.spoutcraft.api.Spoutcraft.getChunkAt(worldObj, x, y, z);
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

			if (this.isOnLadder()) {
				float var11 = 0.15F;

				if (this.motionX < (double)(-var11)) {
					this.motionX = (double)(-var11);
				}

				if (this.motionX > (double)var11) {
					this.motionX = (double)var11;
				}

				if (this.motionZ < (double)(-var11)) {
					this.motionZ = (double)(-var11);
				}

				if (this.motionZ > (double)var11) {
					this.motionZ = (double)var11;
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

		this.prevLimbSwingAmount = this.limbSwingAmount;
		var10 = this.posX - this.prevPosX;
		double var9 = this.posZ - this.prevPosZ;
		float var12 = MathHelper.sqrt_double(var10 * var10 + var9 * var9) * 4.0F;

		if (var12 > 1.0F) {
			var12 = 1.0F;
		}

		this.limbSwingAmount += (var12 - this.limbSwingAmount) * 0.4F;
		this.limbSwing += this.limbSwingAmount;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	protected boolean isAIEnabled() {
		return false;
	}

	/**
	 * the movespeed used for the new AI system
	 */
	public float getAIMoveSpeed() {
		return this.isAIEnabled() ? this.landMovementFactor : 0.1F;
	}

	/**
	 * set the movespeed used for the new AI system
	 */
	public void setAIMoveSpeed(float par1) {
		this.landMovementFactor = par1;
	}

	public boolean attackEntityAsMob(Entity par1Entity) {
		this.setLastAttacker(par1Entity);
		return false;
	}

	/**
	 * Returns whether player is sleeping or not
	 */
	public boolean isPlayerSleeping() {
		return false;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();

		if (!this.worldObj.isRemote) {
			int var1 = this.getArrowCountInEntity();

			if (var1 > 0) {
				if (this.arrowHitTimer <= 0) {
					this.arrowHitTimer = 20 * (30 - var1);
				}

				--this.arrowHitTimer;

				if (this.arrowHitTimer <= 0) {
					this.setArrowCountInEntity(var1 - 1);
				}
			}

			for (int var2 = 0; var2 < 5; ++var2) {
				ItemStack var3 = this.previousEquipment[var2];
				ItemStack var4 = this.getCurrentItemOrArmor(var2);

				if (!ItemStack.areItemStacksEqual(var4, var3)) {
					((WorldServer)this.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet5PlayerInventory(this.entityId, var2, var4));

					if (var3 != null) {
						this.attributeMap.removeAttributeModifiers(var3.getAttributeModifiers());
					}

					if (var4 != null) {
						this.attributeMap.applyAttributeModifiers(var4.getAttributeModifiers());
					}

					this.previousEquipment[var2] = var4 == null ? null : var4.copy();
				}
			}
		}

		this.onLivingUpdate();
		double var9 = this.posX - this.prevPosX;
		double var10 = this.posZ - this.prevPosZ;
		float var5 = (float)(var9 * var9 + var10 * var10);
		float var6 = this.renderYawOffset;
		float var7 = 0.0F;
		this.field_70768_au = this.field_110154_aX;
		float var8 = 0.0F;

		if (var5 > 0.0025000002F) {
			var8 = 1.0F;
			var7 = (float)Math.sqrt((double)var5) * 3.0F;
			var6 = (float)Math.atan2(var10, var9) * 180.0F / (float)Math.PI - 90.0F;
		}

		if (this.swingProgress > 0.0F) {
			var6 = this.rotationYaw;
		}

		if (!this.onGround) {
			var8 = 0.0F;
		}

		this.field_110154_aX += (var8 - this.field_110154_aX) * 0.3F;
		this.worldObj.theProfiler.startSection("headTurn");
		var7 = this.func_110146_f(var6, var7);
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

	protected float func_110146_f(float par1, float par2) {
		float var3 = MathHelper.wrapAngleTo180_float(par1 - this.renderYawOffset);
		this.renderYawOffset += var3 * 0.3F;
		float var4 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset);
		boolean var5 = var4 < -90.0F || var4 >= 90.0F;

		if (var4 < -75.0F) {
			var4 = -75.0F;
		}

		if (var4 >= 75.0F) {
			var4 = 75.0F;
		}

		this.renderYawOffset = this.rotationYaw - var4;

		if (var4 * var4 > 2500.0F) {
			this.renderYawOffset += var4 * 0.2F;
		}

		if (var5) {
			par2 *= -1.0F;
		}

		return par2;
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
		this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
		this.worldObj.theProfiler.endSection();
		this.worldObj.theProfiler.startSection("push");

		if (!this.worldObj.isRemote) {
			this.collideWithNearbyEntities();
		}

		this.worldObj.theProfiler.endSection();
	}

	protected void updateAITasks() {}

	protected void collideWithNearbyEntities() {
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
	 * Handles updating while being ridden by an entity
	 */
	public void updateRidden() {
		super.updateRidden();
		this.field_70768_au = this.field_110154_aX;
		this.field_110154_aX = 0.0F;
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
	 * main AI tick function, replaces updateEntityActionState
	 */
	protected void updateAITick() {}

	protected void updateEntityActionState() {
		++this.entityAge;
	}

	public void setJumping(boolean par1) {
		this.isJumping = par1;
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

	/**
	 * returns true if the entity provided in the argument can be seen. (Raytrace)
	 */
	public boolean canEntityBeSeen(Entity par1Entity) {
		return this.worldObj.clip(this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), this.worldObj.getWorldVec3Pool().getVecFromPool(par1Entity.posX, par1Entity.posY + (double)par1Entity.getEyeHeight(), par1Entity.posZ)) == null;
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
	 * Performs a ray trace for the distance specified and using the partial tick time. Args: distance, partialTickTime
	 */
	public MovingObjectPosition rayTrace(double par1, float par3) {
		Vec3 var4 = this.getPosition(par3);
		Vec3 var5 = this.getLook(par3);
		Vec3 var6 = var4.addVector(var5.xCoord * par1, var5.yCoord * par1, var5.zCoord * par1);
		return this.worldObj.clip(var4, var6);
	}

	/**
	 * Returns whether the entity is in a local (client) world
	 */
	public boolean isClientWorld() {
		return !this.worldObj.isRemote;
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
	 * Sets that this entity has been attacked.
	 */
	protected void setBeenAttacked() {
		this.velocityChanged = this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue();
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

	public float getAbsorptionAmount() {
		return this.field_110151_bq;
	}

	public void setAbsorptionAmount(float par1) {
		if (par1 < 0.0F) {
			par1 = 0.0F;
		}

		this.field_110151_bq = par1;
	}

	public Team getTeam() {
		return null;
	}

	public boolean isOnSameTeam(EntityLivingBase par1EntityLivingBase) {
		return this.isOnTeam(par1EntityLivingBase.getTeam());
	}

	/**
	 * Returns true if the entity is on a specific team.
	 */
	public boolean isOnTeam(Team par1Team) {
		return this.getTeam() != null ? this.getTeam().isSameTeam(par1Team) : false;
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
	
	public void setDisplayName(String var1) {
		this.displayName = var1;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	// Spout End
}
