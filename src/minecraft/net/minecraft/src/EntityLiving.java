package net.minecraft.src;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.StepSound;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
//Spout Start
import org.getspout.spout.io.CustomTextureManager;
//Spout End
public abstract class EntityLiving extends Entity {

	public int heartsHalvesLife = 20;
	public float field_9365_p;
	public float field_9363_r;
	public float renderYawOffset = 0.0F;
	public float prevRenderYawOffset = 0.0F;
	protected float field_9362_u;
	protected float field_9361_v;
	protected float field_9360_w;
	protected float field_9359_x;
	protected boolean field_9358_y = true;
	protected String texture = "/mob/char.png";
	protected boolean field_9355_A = true;
	protected float field_9353_B = 0.0F;
	protected String entityType = null;
	protected float field_9349_D = 1.0F;
	protected int scoreValue = 0;
	protected float field_9345_F = 0.0F;
	public boolean isMultiplayerEntity = false;
	public float field_35169_bv = 0.1F;
	public float field_35168_bw = 0.02F;
	public float prevSwingProgress;
	public float swingProgress;
	public int health = 10;
	public int prevHealth;
	private int livingSoundTime;
	public int hurtTime;
	public int maxHurtTime;
	public float attackedAtYaw = 0.0F;
	public int deathTime = 0;
	public int attackTime = 0;
	public float prevCameraPitch;
	public float cameraPitch;
	protected boolean unused_flag = false;
	protected int field_35171_bJ;
	public int field_9326_T = -1;
	public float field_9325_U = (float)(Math.random() * 0.8999999761581421D + 0.10000000149011612D);
	public float field_705_Q;
	public float field_704_R;
	public float field_703_S;
	private EntityPlayer field_34904_b = null;
	private int field_34905_c = 0;
	public int field_35172_bP = 0;
	public int field_35173_bQ = 0;
	protected HashMap field_35170_bR = new HashMap();
	protected int newPosRotationIncrements;
	protected double newPosX;
	protected double newPosY;
	protected double newPosZ;
	protected double newRotationYaw;
	protected double newRotationPitch;
	float field_9348_ae = 0.0F;
	protected int field_9346_af = 0;
	protected int entityAge = 0;
	protected float moveStrafing;
	protected float moveForward;
	protected float randomYawVelocity;
	protected boolean isJumping = false;
	protected float defaultPitch = 0.0F;
	protected float moveSpeed = 0.7F;
	private Entity currentTarget;
	protected int numTicksToChaseTarget = 0;

	//Spout Start
	private HashMap<Byte, String> customTextures = new HashMap<Byte, String>();
	private byte textureToRender = 0;
	public double gravityMod = 1D;
	public double walkingMod = 1D;
	public double swimmingMod = 1D;
	public double jumpingMod = 1D;
	public double airspeedMod = 1D;
	//Spout End

	public EntityLiving(World var1) {
		super(var1);
		this.preventEntitySpawning = true;
		this.field_9363_r = (float)(Math.random() + 1.0D) * 0.01F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.field_9365_p = (float)Math.random() * 12398.0F;
		this.rotationYaw = (float)(Math.random() * 3.1415927410125732D * 2.0D);
		this.stepHeight = 0.5F;
	}

	protected void entityInit() {}

	public boolean canEntityBeSeen(Entity var1) {
		return this.worldObj.rayTraceBlocks(Vec3D.createVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), Vec3D.createVector(var1.posX, var1.posY + (double)var1.getEyeHeight(), var1.posZ)) == null;
	}

	public String getEntityTexture() {
		//Spout Start
		String custom = getCustomTextureUrl(getTextureToRender());
		if(custom == null || CustomTextureManager.getTexturePathFromUrl(custom) == null){
			return texture;
		} else {
			return CustomTextureManager.getTexturePathFromUrl(custom);
		}
		//Spout End
	}

//Spout Start
	public String getCustomTextureUrl(byte id){
		return customTextures.get(id);
	}
	
	public String getCustomTexture(byte id){
		if(getCustomTextureUrl(id) != null)
		{
			return CustomTextureManager.getTexturePathFromUrl(getCustomTextureUrl(id));
		}
		return null;
	}

	public void setCustomTexture(String url, byte id){
		if (url != null) {
			CustomTextureManager.downloadTexture(url);
		}
		customTextures.put(id, url);
	}
	
	public void setTextureToRender(byte textureToRender) {
		this.textureToRender = textureToRender;
	}

	public byte getTextureToRender() {
		return textureToRender;
	}
	//Spout End

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public boolean canBePushed() {
		return !this.isDead;
	}

	public float getEyeHeight() {
		return this.height * 0.85F;
	}

	public int getTalkInterval() {
		return 80;
	}

	public void playLivingSound() {
		String var1 = this.getLivingSound();
		if(var1 != null) {
			this.worldObj.playSoundAtEntity(this, var1, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
		}

	}

	public void onEntityUpdate() {
		this.prevSwingProgress = this.swingProgress;
		super.onEntityUpdate();
		if(this.rand.nextInt(1000) < this.livingSoundTime++) {
			this.livingSoundTime = -this.getTalkInterval();
			this.playLivingSound();
		}

		if(this.isEntityAlive() && this.isEntityInsideOpaqueBlock()) {
			this.attackEntityFrom(DamageSource.inWall, 1);
		}

		if(this.isImmuneToFire || this.worldObj.multiplayerWorld) {
			this.fire = 0;
		}

		int var1;
		if(this.isEntityAlive() && this.isInsideOfMaterial(Material.water) && !this.canBreatheUnderwater() && !this.field_35170_bR.containsKey(Integer.valueOf(Potion.potionWaterBreathing.id))) {
			--this.air;
			if(this.air == -20) {
				this.air = 0;

				for(var1 = 0; var1 < 8; ++var1) {
					float var2 = this.rand.nextFloat() - this.rand.nextFloat();
					float var3 = this.rand.nextFloat() - this.rand.nextFloat();
					float var4 = this.rand.nextFloat() - this.rand.nextFloat();
					this.worldObj.spawnParticle("bubble", this.posX + (double)var2, this.posY + (double)var3, this.posZ + (double)var4, this.motionX, this.motionY, this.motionZ);
				}

				this.attackEntityFrom(DamageSource.drown, 2);
			}

			this.fire = 0;
		} else {
			this.air = this.maxAir;
		}

		this.prevCameraPitch = this.cameraPitch;
		if(this.attackTime > 0) {
			--this.attackTime;
		}

		if(this.hurtTime > 0) {
			--this.hurtTime;
		}

		if(this.heartsLife > 0) {
			--this.heartsLife;
		}

		if(this.health <= 0) {
			++this.deathTime;
			if(this.deathTime > 20) {
				if(this.field_34905_c > 0 || this.func_35163_av()) {
					var1 = this.func_36001_a(field_34904_b);

					while(var1 > 0) {
						int var8 = EntityXPOrb.func_35121_b(var1);
						var1 -= var8;
						this.worldObj.entityJoinedWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var8));
					}
				}
				this.onEntityDeath();
				this.setEntityDead();

				for(var1 = 0; var1 < 20; ++var1) {
					double var9 = this.rand.nextGaussian() * 0.02D;
					double var10 = this.rand.nextGaussian() * 0.02D;
					double var6 = this.rand.nextGaussian() * 0.02D;
					this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var9, var10, var6);
				}
			}
		}

		if(this.field_34905_c > 0) {
			--this.field_34905_c;
		} else {
			this.field_34904_b = null;
		}

		this.func_36000_g();
		this.field_9359_x = this.field_9360_w;
		this.prevRenderYawOffset = this.renderYawOffset;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}

	protected int func_36001_a(EntityPlayer var1) {
		return this.field_35171_bJ;
	}

	protected boolean func_35163_av() {
		return false;
	}

	public void spawnExplosionParticle() {
		for(int var1 = 0; var1 < 20; ++var1) {
			double var2 = this.rand.nextGaussian() * 0.02D;
			double var4 = this.rand.nextGaussian() * 0.02D;
			double var6 = this.rand.nextGaussian() * 0.02D;
			double var8 = 10.0D;
			this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - var2 * var8, this.posY + (double)(this.rand.nextFloat() * this.height) - var4 * var8, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - var6 * var8, var2, var4, var6);
		}

	}

	public void updateRidden() {
		super.updateRidden();
		this.field_9362_u = this.field_9361_v;
		this.field_9361_v = 0.0F;
	}

	public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
		this.yOffset = 0.0F;
		this.newPosX = var1;
		this.newPosY = var3;
		this.newPosZ = var5;
		this.newRotationYaw = (double)var7;
		this.newRotationPitch = (double)var8;
		this.newPosRotationIncrements = var9;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.field_35172_bP > 0) {
			if(this.field_35173_bQ <= 0) {
				this.field_35173_bQ = 60;
			}

			--this.field_35173_bQ;
			if(this.field_35173_bQ <= 0) {
				--this.field_35172_bP;
			}
		}

		this.onLivingUpdate();
		double var1 = this.posX - this.prevPosX;
		double var3 = this.posZ - this.prevPosZ;
		float var5 = MathHelper.sqrt_double(var1 * var1 + var3 * var3);
		float var6 = this.renderYawOffset;
		float var7 = 0.0F;
		this.field_9362_u = this.field_9361_v;
		float var8 = 0.0F;
		if(var5 > 0.05F) {
			var8 = 1.0F;
			var7 = var5 * 3.0F;
			var6 = (float)Math.atan2(var3, var1) * 180.0F / 3.1415927F - 90.0F;
		}

		if(this.swingProgress > 0.0F) {
			var6 = this.rotationYaw;
		}

		if(!this.onGround) {
			var8 = 0.0F;
		}

		this.field_9361_v += (var8 - this.field_9361_v) * 0.3F;

		float var9;
		for(var9 = var6 - this.renderYawOffset; var9 < -180.0F; var9 += 360.0F) {
			;
		}

		while(var9 >= 180.0F) {
			var9 -= 360.0F;
		}

		this.renderYawOffset += var9 * 0.3F;

		float var10;
		for(var10 = this.rotationYaw - this.renderYawOffset; var10 < -180.0F; var10 += 360.0F) {
			;
		}

		while(var10 >= 180.0F) {
			var10 -= 360.0F;
		}

		boolean var11 = var10 < -90.0F || var10 >= 90.0F;
		if(var10 < -75.0F) {
			var10 = -75.0F;
		}

		if(var10 >= 75.0F) {
			var10 = 75.0F;
		}

		this.renderYawOffset = this.rotationYaw - var10;
		if(var10 * var10 > 2500.0F) {
			this.renderYawOffset += var10 * 0.2F;
		}

		if(var11) {
			var7 *= -1.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
			this.prevRenderYawOffset -= 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
			this.prevRenderYawOffset += 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch < -180.0F) {
			this.prevRotationPitch -= 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		this.field_9360_w += var7;
	}

	protected void setSize(float var1, float var2) {
		super.setSize(var1, var2);
	}

	public void heal(int var1) {
		if(this.health > 0) {
			this.health += var1;
			if(this.health > 20) {
				this.health = 20;
			}

			this.heartsLife = this.heartsHalvesLife / 2;
		}
	}

	public boolean attackEntityFrom(DamageSource var1, int var2) {
		if(this.worldObj.multiplayerWorld) {
			return false;
		} else {
			this.entityAge = 0;
			if(this.health <= 0) {
				return false;
			} else {
				this.field_704_R = 1.5F;
				boolean var3 = true;
				if((float)this.heartsLife > (float)this.heartsHalvesLife / 2.0F) {
					if(var2 <= this.field_9346_af) {
						return false;
					}

					this.damageEntity(var1, var2 - this.field_9346_af);
					this.field_9346_af = var2;
					var3 = false;
				} else {
					this.field_9346_af = var2;
					this.prevHealth = this.health;
					this.heartsLife = this.heartsHalvesLife;
					this.damageEntity(var1, var2);
					this.hurtTime = this.maxHurtTime = 10;
				}

				this.attackedAtYaw = 0.0F;
				Entity var4 = var1.getEntity();
				if(var4 != null) {
					if(var4 instanceof EntityPlayer) {
						this.field_34905_c = 60;
						this.field_34904_b = (EntityPlayer)var4;
					} else if(var4 instanceof EntityWolf) {
						EntityWolf var5 = (EntityWolf)var4;
						if(var5.isWolfTamed()) {
							this.field_34905_c = 60;
							this.field_34904_b = null;
						}
					}
				}

				if(var3) {
					this.worldObj.setEntityState(this, (byte)2);
					this.setBeenAttacked();
					if(var4 != null) {
						double var9 = var4.posX - this.posX;

						double var7;
						for(var7 = var4.posZ - this.posZ; var9 * var9 + var7 * var7 < 1.0E-4D; var7 = (Math.random() - Math.random()) * 0.01D) {
							var9 = (Math.random() - Math.random()) * 0.01D;
						}

						this.attackedAtYaw = (float)(Math.atan2(var7, var9) * 180.0D / 3.1415927410125732D) - this.rotationYaw;
						this.knockBack(var4, var2, var9, var7);
					} else {
						this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
					}
				}

				if(this.health <= 0) {
					if(var3) {
						this.worldObj.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
					}

					this.onDeath(var1);
				} else if(var3) {
					this.worldObj.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				}

				return true;
			}
		}
	}

	public void performHurtAnimation() {
		this.hurtTime = this.maxHurtTime = 10;
		this.attackedAtYaw = 0.0F;
	}

	protected void damageEntity(DamageSource var1, int var2) {
		this.health -= var2;
	}

	protected float getSoundVolume() {
		return 1.0F;
	}

	protected String getLivingSound() {
		return null;
	}

	protected String getHurtSound() {
		return "random.hurt";
	}

	protected String getDeathSound() {
		return "random.hurt";
	}

	public void knockBack(Entity var1, int var2, double var3, double var5) {
		this.field_35118_ao = true;
		float var7 = MathHelper.sqrt_double(var3 * var3 + var5 * var5);
		float var8 = 0.4F;
		this.motionX /= 2.0D;
		this.motionY /= 2.0D;
		this.motionZ /= 2.0D;
		this.motionX -= var3 / (double)var7 * (double)var8;
		this.motionY += 0.4000000059604645D;
		this.motionZ -= var5 / (double)var7 * (double)var8;
		if(this.motionY > 0.4000000059604645D) {
			this.motionY = 0.4000000059604645D;
		}

	}

	public void onDeath(DamageSource var1) {
		Entity var2 = var1.getEntity();
		if(this.scoreValue >= 0 && var2 != null) {
			var2.addToPlayerScore(this, this.scoreValue);
		}

		if(var2 != null) {
			var2.onKillEntity(this);
		}

		this.unused_flag = true;
		if(!this.worldObj.multiplayerWorld) {
			this.dropFewItems(this.field_34905_c > 0);
		}

		this.worldObj.setEntityState(this, (byte)3);
	}

	protected void dropFewItems(boolean var1) {
		int var2 = this.getDropItemId();
		if(var2 > 0) {
			int var3 = this.rand.nextInt(3);

			for(int var4 = 0; var4 < var3; ++var4) {
				this.dropItem(var2, 1);
			}
		}

	}

	protected int getDropItemId() {
		return 0;
	}

	protected void fall(float var1) {
		super.fall(var1);
		int var2 = (int)Math.ceil((double)(var1 - 3.0F));
		if(var2 > 0) {
			this.attackEntityFrom(DamageSource.fall, var2);
			int var3 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 0.20000000298023224D - (double)this.yOffset), MathHelper.floor_double(this.posZ));
			if(var3 > 0) {
				StepSound var4 = Block.blocksList[var3].stepSound;
				this.worldObj.playSoundAtEntity(this, var4.stepSoundDir2(), var4.getVolume() * 0.5F, var4.getPitch() * 0.75F);
			}
		}

	}

	public void moveEntityWithHeading(float var1, float var2) {
		double var3;
		if(this.isInWater()) {
			var3 = this.posY;
			moveFlying(var1, var2, (float) (0.02F * swimmingMod)); //Spout
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.800000011920929D;
			this.motionY *= 0.800000011920929D;
			this.motionZ *= 0.800000011920929D;
			//Spout start
			motionY -= 0.02D * gravityMod;
			//Spout end
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var3, this.motionZ)) {
				this.motionY = 0.30000001192092896D;
			}
		} else if(this.handleLavaMovement()) {
			var3 = this.posY;
			this.moveFlying(var1, var2, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
			this.motionY -= 0.02D;
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var3, this.motionZ)) {
				this.motionY = 0.30000001192092896D;
			}
		} else {
			float var8 = 0.91F;
			if(this.onGround) {
				var8 = 0.54600006F;
				int var4 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
				if(var4 > 0) {
					var8 = Block.blocksList[var4].slipperiness * 0.91F;
				}
			}

			float var9 = 0.16277136F / (var8 * var8 * var8);
			//Spout start
			float movement = this.field_35168_bw;
			if (onGround) {
				movement = (float) (this.field_35169_bv * var9 * walkingMod);
			}
			else if (!isInWater()) {
				movement *= airspeedMod;
			}
			moveFlying(var1, var2, movement);
			//Spout end
			var8 = 0.91F;
			if(this.onGround) {
				var8 = 0.54600006F;
				int var6 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
				if(var6 > 0) {
					var8 = Block.blocksList[var6].slipperiness * 0.91F;
				}
			}

			if(this.isOnLadder()) {
				float var11 = 0.15F;
				if(this.motionX < (double)(-var11)) {
					this.motionX = (double)(-var11);
				}

				if(this.motionX > (double)var11) {
					this.motionX = (double)var11;
				}

				if(this.motionZ < (double)(-var11)) {
					this.motionZ = (double)(-var11);
				}

				if(this.motionZ > (double)var11) {
					this.motionZ = (double)var11;
				}

				this.fallDistance = 0.0F;
				if(this.motionY < -0.15D) {
					this.motionY = -0.15D;
				}

				if(this.isSneaking() && this.motionY < 0.0D) {
					this.motionY = 0.0D;
				}
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			if(this.isCollidedHorizontally && this.isOnLadder()) {
				this.motionY = 0.2D;
			}

			this.motionY -= 0.08D * gravityMod; //Spout
			this.motionY *= 0.9800000190734863D;
			this.motionX *= (double)var8;
			this.motionZ *= (double)var8;
		}

		this.field_705_Q = this.field_704_R;
		var3 = this.posX - this.prevPosX;
		double var10 = this.posZ - this.prevPosZ;
		float var7 = MathHelper.sqrt_double(var3 * var3 + var10 * var10) * 4.0F;
		if(var7 > 1.0F) {
			var7 = 1.0F;
		}

		this.field_704_R += (var7 - this.field_704_R) * 0.4F;
		this.field_703_S += this.field_704_R;
	}

	public boolean isOnLadder() {
		int var1 = MathHelper.floor_double(this.posX);
		int var2 = MathHelper.floor_double(this.boundingBox.minY);
		int var3 = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBlockId(var1, var2, var3) == Block.ladder.blockID;
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		var1.setShort("Health", (short)this.health);
		var1.setShort("HurtTime", (short)this.hurtTime);
		var1.setShort("DeathTime", (short)this.deathTime);
		var1.setShort("AttackTime", (short)this.attackTime);
		if(!this.field_35170_bR.isEmpty()) {
			NBTTagList var2 = new NBTTagList();
			Iterator var3 = this.field_35170_bR.values().iterator();

			while(var3.hasNext()) {
				PotionEffect var4 = (PotionEffect)var3.next();
				NBTTagCompound var5 = new NBTTagCompound();
				var5.setByte("Id", (byte)var4.func_35799_a());
				var5.setByte("Amplifier", (byte)var4.func_35801_c());
				var5.setInteger("Duration", var4.func_35802_b());
				var2.setTag(var5);
			}

			var1.setTag("ActiveEffects", var2);
		}

	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		this.health = var1.getShort("Health");
		if(!var1.hasKey("Health")) {
			this.health = 10;
		}

		this.hurtTime = var1.getShort("HurtTime");
		this.deathTime = var1.getShort("DeathTime");
		this.attackTime = var1.getShort("AttackTime");
		if(var1.hasKey("ActiveEffects")) {
			NBTTagList var2 = var1.getTagList("ActiveEffects");

			for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
				NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
				byte var5 = var4.getByte("Id");
				byte var6 = var4.getByte("Amplifier");
				int var7 = var4.getInteger("Duration");
				this.field_35170_bR.put(Integer.valueOf(var5), new PotionEffect(var5, var7, var6));
			}
		}

	}

	public boolean isEntityAlive() {
		return !this.isDead && this.health > 0;
	}

	public boolean canBreatheUnderwater() {
		return false;
	}

	public void onLivingUpdate() {
		if(this.newPosRotationIncrements > 0) {
			double var1 = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
			double var3 = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
			double var5 = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;

			double var7;
			for(var7 = this.newRotationYaw - (double)this.rotationYaw; var7 < -180.0D; var7 += 360.0D) {
				;
			}

			while(var7 >= 180.0D) {
				var7 -= 360.0D;
			}

			this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.newPosRotationIncrements);
			this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
			--this.newPosRotationIncrements;
			this.setPosition(var1, var3, var5);
			this.setRotation(this.rotationYaw, this.rotationPitch);
			List var9 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.contract(0.03125D, 0.0D, 0.03125D));
			if(var9.size() > 0) {
				double var10 = 0.0D;

				for(int var12 = 0; var12 < var9.size(); ++var12) {
					AxisAlignedBB var13 = (AxisAlignedBB)var9.get(var12);
					if(var13.maxY > var10) {
						var10 = var13.maxY;
					}
				}

				var3 += var10 - this.boundingBox.minY;
				this.setPosition(var1, var3, var5);
			}
		}

		if(this.isMovementBlocked()) {
			this.isJumping = false;
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
			this.randomYawVelocity = 0.0F;
		} else if(!this.isMultiplayerEntity) {
			this.updateEntityActionState();
		}

		boolean var14 = this.isInWater();
		boolean var2 = this.handleLavaMovement();
		if(this.isJumping) {
			if(var14) {
				this.motionY += 0.03999999910593033D * jumpingMod; //Spout
			} else if(var2) {
				this.motionY += 0.03999999910593033D * jumpingMod; //Spout
			} else if(this.onGround) {
				this.jump();
			}
		}

		this.moveStrafing *= 0.98F;
		this.moveForward *= 0.98F;
		this.randomYawVelocity *= 0.9F;
		float var15 = this.field_35169_bv;
		this.field_35169_bv *= this.func_35166_t_();
		this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
		this.field_35169_bv = var15;
		List var4 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
		if(var4 != null && var4.size() > 0) {
			for(int var16 = 0; var16 < var4.size(); ++var16) {
				Entity var6 = (Entity)var4.get(var16);
				if(var6.canBePushed()) {
					var6.applyEntityCollision(this);
				}
			}
		}

	}

	protected boolean isMovementBlocked() {
		return this.health <= 0;
	}

	public boolean func_35162_ad() {
		return false;
	}

	protected void jump() {
		this.motionY = 0.41999998688697815D * jumpingMod; //Spout
		if(this.func_35117_Q()) {
			float var1 = this.rotationYaw * 0.017453292F;
			this.motionX -= (double)(MathHelper.sin(var1) * 0.2F);
			this.motionZ += (double)(MathHelper.cos(var1) * 0.2F);
		}

		this.field_35118_ao = true;
	}

	protected boolean canDespawn() {
		return true;
	}

	protected void despawnEntity() {
		EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
		if(this.canDespawn() && var1 != null) {
			double var2 = var1.posX - this.posX;
			double var4 = var1.posY - this.posY;
			double var6 = var1.posZ - this.posZ;
			double var8 = var2 * var2 + var4 * var4 + var6 * var6;
			if(var8 > 16384.0D) {
				this.setEntityDead();
			}

			if(this.entityAge > 600 && this.rand.nextInt(800) == 0) {
				if(var8 < 1024.0D) {
					this.entityAge = 0;
				} else {
					this.setEntityDead();
				}
			}
		}

	}

	protected void updateEntityActionState() {
		++this.entityAge;
		EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
		this.despawnEntity();
		this.moveStrafing = 0.0F;
		this.moveForward = 0.0F;
		float var2 = 8.0F;
		if(this.rand.nextFloat() < 0.02F) {
			var1 = this.worldObj.getClosestPlayerToEntity(this, (double)var2);
			if(var1 != null) {
				this.currentTarget = var1;
				this.numTicksToChaseTarget = 10 + this.rand.nextInt(20);
			} else {
				this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
			}
		}

		if(this.currentTarget != null) {
			this.faceEntity(this.currentTarget, 10.0F, (float)this.getVerticalFaceSpeed());
			if(this.numTicksToChaseTarget-- <= 0 || this.currentTarget.isDead || this.currentTarget.getDistanceSqToEntity(this) > (double)(var2 * var2)) {
				this.currentTarget = null;
			}
		} else {
			if(this.rand.nextFloat() < 0.05F) {
				this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
			}

			this.rotationYaw += this.randomYawVelocity;
			this.rotationPitch = this.defaultPitch;
		}

		boolean var3 = this.isInWater();
		boolean var4 = this.handleLavaMovement();
		if(var3 || var4) {
			this.isJumping = this.rand.nextFloat() < 0.8F;
		}

	}

	protected int getVerticalFaceSpeed() {
		return 40;
	}

	public void faceEntity(Entity var1, float var2, float var3) {
		double var4 = var1.posX - this.posX;
		double var8 = var1.posZ - this.posZ;
		double var6;
		if(var1 instanceof EntityLiving) {
			EntityLiving var10 = (EntityLiving)var1;
			var6 = this.posY + (double)this.getEyeHeight() - (var10.posY + (double)var10.getEyeHeight());
		} else {
			var6 = (var1.boundingBox.minY + var1.boundingBox.maxY) / 2.0D - (this.posY + (double)this.getEyeHeight());
		}

		double var14 = (double)MathHelper.sqrt_double(var4 * var4 + var8 * var8);
		float var12 = (float)(Math.atan2(var8, var4) * 180.0D / 3.1415927410125732D) - 90.0F;
		float var13 = (float)(-(Math.atan2(var6, var14) * 180.0D / 3.1415927410125732D));
		this.rotationPitch = -this.updateRotation(this.rotationPitch, var13, var3);
		this.rotationYaw = this.updateRotation(this.rotationYaw, var12, var2);
	}

	public boolean hasCurrentTarget() {
		return this.currentTarget != null;
	}

	public Entity getCurrentTarget() {
		return this.currentTarget;
	}

	private float updateRotation(float var1, float var2, float var3) {
		float var4;
		for(var4 = var2 - var1; var4 < -180.0F; var4 += 360.0F) {
			;
		}

		while(var4 >= 180.0F) {
			var4 -= 360.0F;
		}

		if(var4 > var3) {
			var4 = var3;
		}

		if(var4 < -var3) {
			var4 = -var3;
		}

		return var1 + var4;
	}

	public void onEntityDeath() {}

	public boolean getCanSpawnHere() {
		return this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox);
	}

	protected void kill() {
		this.attackEntityFrom(DamageSource.outOfWorld, 4);
	}

	public float getSwingProgress(float var1) {
		float var2 = this.swingProgress - this.prevSwingProgress;
		if(var2 < 0.0F) {
			++var2;
		}

		return this.prevSwingProgress + var2 * var1;
	}

	public Vec3D getPosition(float var1) {
		if(var1 == 1.0F) {
			return Vec3D.createVector(this.posX, this.posY, this.posZ);
		} else {
			double var2 = this.prevPosX + (this.posX - this.prevPosX) * (double)var1;
			double var4 = this.prevPosY + (this.posY - this.prevPosY) * (double)var1;
			double var6 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double)var1;
			return Vec3D.createVector(var2, var4, var6);
		}
	}

	public Vec3D getLookVec() {
		return this.getLook(1.0F);
	}

	public Vec3D getLook(float var1) {
		float var2;
		float var3;
		float var4;
		float var5;
		if(var1 == 1.0F) {
			var2 = MathHelper.cos(-this.rotationYaw * 0.017453292F - 3.1415927F);
			var3 = MathHelper.sin(-this.rotationYaw * 0.017453292F - 3.1415927F);
			var4 = -MathHelper.cos(-this.rotationPitch * 0.017453292F);
			var5 = MathHelper.sin(-this.rotationPitch * 0.017453292F);
			return Vec3D.createVector((double)(var3 * var4), (double)var5, (double)(var2 * var4));
		} else {
			var2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * var1;
			var3 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * var1;
			var4 = MathHelper.cos(-var3 * 0.017453292F - 3.1415927F);
			var5 = MathHelper.sin(-var3 * 0.017453292F - 3.1415927F);
			float var6 = -MathHelper.cos(-var2 * 0.017453292F);
			float var7 = MathHelper.sin(-var2 * 0.017453292F);
			return Vec3D.createVector((double)(var5 * var6), (double)var7, (double)(var4 * var6));
		}
	}

	public float func_35159_aC() {
		return 1.0F;
	}

	public MovingObjectPosition rayTrace(double var1, float var3) {
		Vec3D var4 = this.getPosition(var3);
		Vec3D var5 = this.getLook(var3);
		Vec3D var6 = var4.addVector(var5.xCoord * var1, var5.yCoord * var1, var5.zCoord * var1);
		return this.worldObj.rayTraceBlocks(var4, var6);
	}

	public int getMaxSpawnedInChunk() {
		return 4;
	}

	public ItemStack getHeldItem() {
		return null;
	}

	public void handleHealthUpdate(byte var1) {
		if(var1 == 2) {
			this.field_704_R = 1.5F;
			this.heartsLife = this.heartsHalvesLife;
			this.hurtTime = this.maxHurtTime = 10;
			this.attackedAtYaw = 0.0F;
			this.worldObj.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.attackEntityFrom(DamageSource.generic, 0);
		} else if(var1 == 3) {
			this.worldObj.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.health = 0;
			this.onDeath(DamageSource.generic);
		} else {
			super.handleHealthUpdate(var1);
		}

	}

	public boolean isPlayerSleeping() {
		return false;
	}

	public int getItemIcon(ItemStack var1) {
		return var1.getIconIndex();
	}

	protected void func_36000_g() {
		Iterator var1 = this.field_35170_bR.keySet().iterator();

		while(var1.hasNext()) {
			Integer var2 = (Integer)var1.next();
			PotionEffect var3 = (PotionEffect)this.field_35170_bR.get(var2);
			if(!var3.func_35798_a(this) && !this.worldObj.multiplayerWorld) {
				var1.remove();
				this.func_35158_d(var3);
			}
		}

	}

	public boolean func_35160_a(Potion var1) {
		return this.field_35170_bR.containsKey(Integer.valueOf(var1.id));
	}

	public PotionEffect func_35167_b(Potion var1) {
		return (PotionEffect)this.field_35170_bR.get(Integer.valueOf(var1.id));
	}

	public void func_35165_a(PotionEffect var1) {
		if(this.field_35170_bR.containsKey(Integer.valueOf(var1.func_35799_a()))) {
			((PotionEffect)this.field_35170_bR.get(Integer.valueOf(var1.func_35799_a()))).func_35796_a(var1);
			this.func_35161_c((PotionEffect)this.field_35170_bR.get(Integer.valueOf(var1.func_35799_a())));
		} else {
			this.field_35170_bR.put(Integer.valueOf(var1.func_35799_a()), var1);
			this.func_35164_b(var1);
		}

	}

	public void func_36002_f(int var1) {
		this.field_35170_bR.remove(Integer.valueOf(var1));
	}

	protected void func_35164_b(PotionEffect var1) {}

	protected void func_35161_c(PotionEffect var1) {}

	protected void func_35158_d(PotionEffect var1) {}

	protected float func_35166_t_() {
		float var1 = 1.0F;
		if(this.func_35160_a(Potion.potionSpeed)) {
			var1 *= 1.0F + 0.2F * (float)(this.func_35167_b(Potion.potionSpeed).func_35801_c() + 1);
		}

		if(this.func_35160_a(Potion.potionSlowdown)) {
			var1 *= 1.0F - 0.15F * (float)(this.func_35167_b(Potion.potionSlowdown).func_35801_c() + 1);
		}

		return var1;
	}
}
