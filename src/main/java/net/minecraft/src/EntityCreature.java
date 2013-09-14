package net.minecraft.src;

import java.util.UUID;

public abstract class EntityCreature extends EntityLiving {
	public static final UUID field_110179_h = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
	public static final AttributeModifier field_110181_i = (new AttributeModifier(field_110179_h, "Fleeing speed bonus", 2.0D, 2)).func_111168_a(false);
	// Spout Start - private to public
	public PathEntity pathToEntity;
	// Spout End

	/** The Entity this EntityCreature is set to attack. */
	// Spout Start - private to public
	public Entity entityToAttack;
	// Spout End

	/**
	 * returns true if a creature has attacked recently only used for creepers and skeletons
	 */
	protected boolean hasAttacked;

	/** Used to make a creature speed up and wander away when hit. */
	protected int fleeingTick;
	private ChunkCoordinates homePosition = new ChunkCoordinates(0, 0, 0);

	/** If -1 there is no maximum distance */
	private float maximumHomeDistance = -1.0F;
	private EntityAIBase field_110178_bs = new EntityAIMoveTowardsRestriction(this, 1.0D);
	private boolean field_110180_bt;

	public EntityCreature(World par1World) {
		super(par1World);
	}

	/**
	 * Disables a mob's ability to move on its own while true.
	 */
	protected boolean isMovementCeased() {
		return false;
	}

	protected void updateEntityActionState() {
		this.worldObj.theProfiler.startSection("ai");

		if (this.fleeingTick > 0 && --this.fleeingTick == 0) {
			AttributeInstance var1 = this.func_110148_a(SharedMonsterAttributes.field_111263_d);
			var1.func_111124_b(field_110181_i);
		}

		this.hasAttacked = this.isMovementCeased();
		float var21 = 16.0F;

		if (this.entityToAttack == null) {
			this.entityToAttack = this.findPlayerToAttack();

			if (this.entityToAttack != null) {
				this.pathToEntity = this.worldObj.getPathEntityToEntity(this, this.entityToAttack, var21, true, false, false, true);
			}
		} else if (this.entityToAttack.isEntityAlive()) {
			float var2 = this.entityToAttack.getDistanceToEntity(this);

			if (this.canEntityBeSeen(this.entityToAttack)) {
				this.attackEntity(this.entityToAttack, var2);
			}
		} else {
			this.entityToAttack = null;
		}

		this.worldObj.theProfiler.endSection();

		if (!this.hasAttacked && this.entityToAttack != null && (this.pathToEntity == null || this.rand.nextInt(20) == 0)) {
			this.pathToEntity = this.worldObj.getPathEntityToEntity(this, this.entityToAttack, var21, true, false, false, true);
		} else if (!this.hasAttacked && (this.pathToEntity == null && this.rand.nextInt(180) == 0 || this.rand.nextInt(120) == 0 || this.fleeingTick > 0) && this.entityAge < 100) {
			this.updateWanderPath();
		}

		int var22 = MathHelper.floor_double(this.boundingBox.minY + 0.5D);
		boolean var3 = this.isInWater();
		boolean var4 = this.handleLavaMovement();
		this.rotationPitch = 0.0F;

		if (this.pathToEntity != null && this.rand.nextInt(100) != 0) {
			this.worldObj.theProfiler.startSection("followpath");
			Vec3 var5 = this.pathToEntity.getPosition(this);
			double var6 = (double)(this.width * 2.0F);

			while (var5 != null && var5.squareDistanceTo(this.posX, var5.yCoord, this.posZ) < var6 * var6) {
				this.pathToEntity.incrementPathIndex();

				if (this.pathToEntity.isFinished()) {
					var5 = null;
					this.pathToEntity = null;
				} else {
					var5 = this.pathToEntity.getPosition(this);
				}
			}

			this.isJumping = false;

			if (var5 != null) {
				double var8 = var5.xCoord - this.posX;
				double var10 = var5.zCoord - this.posZ;
				double var12 = var5.yCoord - (double)var22;
				float var14 = (float)(Math.atan2(var10, var8) * 180.0D / Math.PI) - 90.0F;
				float var15 = MathHelper.wrapAngleTo180_float(var14 - this.rotationYaw);
				this.moveForward = (float)this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e();

				if (var15 > 30.0F) {
					var15 = 30.0F;
				}

				if (var15 < -30.0F) {
					var15 = -30.0F;
				}

				this.rotationYaw += var15;

				if (this.hasAttacked && this.entityToAttack != null) {
					double var16 = this.entityToAttack.posX - this.posX;
					double var18 = this.entityToAttack.posZ - this.posZ;
					float var20 = this.rotationYaw;
					this.rotationYaw = (float)(Math.atan2(var18, var16) * 180.0D / Math.PI) - 90.0F;
					var15 = (var20 - this.rotationYaw + 90.0F) * (float)Math.PI / 180.0F;
					this.moveStrafing = -MathHelper.sin(var15) * this.moveForward * 1.0F;
					this.moveForward = MathHelper.cos(var15) * this.moveForward * 1.0F;
				}

				if (var12 > 0.0D) {
					this.isJumping = true;
				}
			}

			if (this.entityToAttack != null) {
				this.faceEntity(this.entityToAttack, 30.0F, 30.0F);
			}

			if (this.isCollidedHorizontally && !this.hasPath()) {
				this.isJumping = true;
			}

			if (this.rand.nextFloat() < 0.8F && (var3 || var4)) {
				this.isJumping = true;
			}

			this.worldObj.theProfiler.endSection();
		} else {
			super.updateEntityActionState();
			this.pathToEntity = null;
		}
	}

	/**
	 * Time remaining during which the Animal is sped up and flees.
	 */
	protected void updateWanderPath() {
		this.worldObj.theProfiler.startSection("stroll");
		boolean var1 = false;
		int var2 = -1;
		int var3 = -1;
		int var4 = -1;
		float var5 = -99999.0F;

		for (int var6 = 0; var6 < 10; ++var6) {
			int var7 = MathHelper.floor_double(this.posX + (double)this.rand.nextInt(13) - 6.0D);
			int var8 = MathHelper.floor_double(this.posY + (double)this.rand.nextInt(7) - 3.0D);
			int var9 = MathHelper.floor_double(this.posZ + (double)this.rand.nextInt(13) - 6.0D);
			float var10 = this.getBlockPathWeight(var7, var8, var9);

			if (var10 > var5) {
				var5 = var10;
				var2 = var7;
				var3 = var8;
				var4 = var9;
				var1 = true;
			}
		}

		if (var1) {
			this.pathToEntity = this.worldObj.getEntityPathToXYZ(this, var2, var3, var4, 10.0F, true, false, false, true);
		}

		this.worldObj.theProfiler.endSection();
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
	 */
	protected void attackEntity(Entity par1Entity, float par2) {}

	/**
	 * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
	 * Args: x, y, z
	 */
	public float getBlockPathWeight(int par1, int par2, int par3) {
		return 0.0F;
	}

	/**
	 * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking (Animals,
	 * Spiders at day, peaceful PigZombies).
	 */
	protected Entity findPlayerToAttack() {
		return null;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	public boolean getCanSpawnHere() {
		int var1 = MathHelper.floor_double(this.posX);
		int var2 = MathHelper.floor_double(this.boundingBox.minY);
		int var3 = MathHelper.floor_double(this.posZ);
		return super.getCanSpawnHere() && this.getBlockPathWeight(var1, var2, var3) >= 0.0F;
	}

	/**
	 * Returns true if entity has a path to follow
	 */
	public boolean hasPath() {
		return this.pathToEntity != null;
	}

	/**
	 * sets the Entities walk path in EntityCreature
	 */
	public void setPathToEntity(PathEntity par1PathEntity) {
		this.pathToEntity = par1PathEntity;
	}

	/**
	 * Returns current entities target
	 */
	public Entity getEntityToAttack() {
		return this.entityToAttack;
	}

	/**
	 * Sets the entity which is to be attacked.
	 */
	public void setTarget(Entity par1Entity) {
		this.entityToAttack = par1Entity;
	}

	public boolean func_110173_bK() {
		return this.func_110176_b(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
	}

	public boolean func_110176_b(int par1, int par2, int par3) {
		return this.maximumHomeDistance == -1.0F ? true : this.homePosition.getDistanceSquared(par1, par2, par3) < this.maximumHomeDistance * this.maximumHomeDistance;
	}

	public void func_110171_b(int par1, int par2, int par3, int par4) {
		this.homePosition.set(par1, par2, par3);
		this.maximumHomeDistance = (float)par4;
	}

	public ChunkCoordinates func_110172_bL() {
		return this.homePosition;
	}

	public float func_110174_bM() {
		return this.maximumHomeDistance;
	}

	public void func_110177_bN() {
		this.maximumHomeDistance = -1.0F;
	}

	public boolean func_110175_bO() {
		return this.maximumHomeDistance != -1.0F;
	}

	protected void func_110159_bB() {
		super.func_110159_bB();

		if (this.func_110167_bD() && this.func_110166_bE() != null && this.func_110166_bE().worldObj == this.worldObj) {
			Entity var1 = this.func_110166_bE();
			this.func_110171_b((int)var1.posX, (int)var1.posY, (int)var1.posZ, 5);
			float var2 = this.getDistanceToEntity(var1);

			if (this instanceof EntityTameable && ((EntityTameable)this).isSitting()) {
				if (var2 > 10.0F) {
					this.func_110160_i(true, true);
				}

				return;
			}

			if (!this.field_110180_bt) {
				this.tasks.addTask(2, this.field_110178_bs);
				this.getNavigator().setAvoidsWater(false);
				this.field_110180_bt = true;
			}

			this.func_142017_o(var2);

			if (var2 > 4.0F) {
				this.getNavigator().tryMoveToEntityLiving(var1, 1.0D);
			}

			if (var2 > 6.0F) {
				double var3 = (var1.posX - this.posX) / (double)var2;
				double var5 = (var1.posY - this.posY) / (double)var2;
				double var7 = (var1.posZ - this.posZ) / (double)var2;
				this.motionX += var3 * Math.abs(var3) * 0.4D;
				this.motionY += var5 * Math.abs(var5) * 0.4D;
				this.motionZ += var7 * Math.abs(var7) * 0.4D;
			}

			if (var2 > 10.0F) {
				this.func_110160_i(true, true);
			}
		} else if (!this.func_110167_bD() && this.field_110180_bt) {
			this.field_110180_bt = false;
			this.tasks.removeTask(this.field_110178_bs);
			this.getNavigator().setAvoidsWater(true);
			this.func_110177_bN();
		}
	}

	protected void func_142017_o(float par1) {}
}
