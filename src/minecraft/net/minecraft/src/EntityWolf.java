package net.minecraft.src;

import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAIAttackOnCollide;
import net.minecraft.src.EntityAIBeg;
import net.minecraft.src.EntityAIFollowOwner;
import net.minecraft.src.EntityAIHurtByTarget;
import net.minecraft.src.EntityAILeapAtTarget;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAIMate;
import net.minecraft.src.EntityAIOwnerHurtByTarget;
import net.minecraft.src.EntityAIOwnerHurtTarget;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAITargetNonTamed;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.EntityTameable;
import net.minecraft.src.Item;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PathEntity;
import net.minecraft.src.World;

// Spout start
import org.spoutcraft.client.entity.CraftPig;
import org.spoutcraft.client.entity.CraftWolf;
import org.spoutcraft.spoutcraftapi.entity.EntitySkinType;
// Spout end

public class EntityWolf extends EntityTameable {

	private boolean looksWithInterest = false;
	private float field_25048_b;
	private float field_25054_c;
	private boolean isShaking;
	private boolean field_25052_g;
	private float timeWolfIsShaking;
	private float prevTimeWolfIsShaking;

	public EntityWolf(World par1World) {
		super(par1World);
		this.texture = "/mob/wolf.png";
		this.setSize(0.6F, 0.8F);
		this.moveSpeed = 0.3F;
		this.func_48084_aL().func_48664_a(true);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.field_48146_a);
		this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, this.moveSpeed, true));
		this.tasks.addTask(5, new EntityAIFollowOwner(this, this.moveSpeed, 10.0F, 2.0F));
		this.tasks.addTask(6, new EntityAIMate(this, this.moveSpeed));
		this.tasks.addTask(7, new EntityAIWander(this, this.moveSpeed));
		this.tasks.addTask(8, new EntityAIBeg(this, 8.0F));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(9, new EntityAILookIdle(this));
		this.field_48105_bU.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.field_48105_bU.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.field_48105_bU.addTask(3, new EntityAIHurtByTarget(this, true));
		this.field_48105_bU.addTask(4, new EntityAITargetNonTamed(this, EntitySheep.class, 16.0F, 200, false));
		//Spout start
		this.spoutEntity = new CraftWolf(this);
		//Spout end
	}

	public boolean isAIEnabled() {
		return true;
	}

	public void func_48092_c(EntityLiving par1EntityLiving) {
		super.func_48092_c(par1EntityLiving);
		if (par1EntityLiving instanceof EntityPlayer) {
			this.setAngry(true);
		}

	}

	protected void func_48097_s_() {
		this.dataWatcher.updateObject(18, Integer.valueOf(this.getEntityHealth()));
	}

	public int getMaxHealth() {
		return this.func_48139_F_()?20:8;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(18, new Integer(this.getEntityHealth()));
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	public String getEntityTexture() {
		//Spout Start
		if(func_48139_F_()) {
			return this.getCustomTexture(EntitySkinType.WOLF_TAMED, "/mob/wolf_tame.png");
		}
		if(isAngry()){
			return this.getCustomTexture(EntitySkinType.WOLF_ANGRY, "/mob/wolf_angry.png");
		} else {
			return super.getEntityTexture();
		}
		//Spout End
	}

	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("Angry", this.isAngry());
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setAngry(par1NBTTagCompound.getBoolean("Angry"));
	}

	protected boolean canDespawn() {
		return this.isAngry();
	}

	protected String getLivingSound() {
		return this.isAngry()?"mob.wolf.growl":(this.rand.nextInt(3) == 0?(this.func_48139_F_() && this.dataWatcher.getWatchableObjectInt(18) < 10?"mob.wolf.whine":"mob.wolf.panting"):"mob.wolf.bark");
	}

	protected String getHurtSound() {
		return "mob.wolf.hurt";
	}

	protected String getDeathSound() {
		return "mob.wolf.death";
	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	protected int getDropItemId() {
		return -1;
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!this.worldObj.isRemote && this.isShaking && !this.field_25052_g && !this.hasPath() && this.onGround) {
			this.field_25052_g = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
			this.worldObj.setEntityState(this, (byte)8);
		}

	}

	public void onUpdate() {
		super.onUpdate();
		this.field_25054_c = this.field_25048_b;
		if (this.looksWithInterest) {
			this.field_25048_b += (1.0F - this.field_25048_b) * 0.4F;
		} else {
			this.field_25048_b += (0.0F - this.field_25048_b) * 0.4F;
		}

		if (this.looksWithInterest) {
			this.numTicksToChaseTarget = 10;
		}

		if (this.isWet()) {
			this.isShaking = true;
			this.field_25052_g = false;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		} else if ((this.isShaking || this.field_25052_g) && this.field_25052_g) {
			if (this.timeWolfIsShaking == 0.0F) {
				this.worldObj.playSoundAtEntity(this, "mob.wolf.shake", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}

			this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
			this.timeWolfIsShaking += 0.05F;
			if (this.prevTimeWolfIsShaking >= 2.0F) {
				this.isShaking = false;
				this.field_25052_g = false;
				this.prevTimeWolfIsShaking = 0.0F;
				this.timeWolfIsShaking = 0.0F;
			}

			if (this.timeWolfIsShaking > 0.4F) {
				float var1 = (float)this.boundingBox.minY;
				int var2 = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * 3.1415927F) * 7.0F);

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

		return MathHelper.sin(var3 * 3.1415927F) * MathHelper.sin(var3 * 3.1415927F * 11.0F) * 0.15F * 3.1415927F;
	}

	public float getInterestedAngle(float par1) {
		return (this.field_25054_c + (this.field_25048_b - this.field_25054_c) * par1) * 0.15F * 3.1415927F;
	}

	public float getEyeHeight() {
		return this.height * 0.8F;
	}

	public int getVerticalFaceSpeed() {
		return this.func_48141_af()?20:super.getVerticalFaceSpeed();
	}

	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		Entity var3 = par1DamageSource.getEntity();
		this.field_48146_a.func_48407_a(false);
		if (var3 != null && !(var3 instanceof EntityPlayer) && !(var3 instanceof EntityArrow)) {
			par2 = (par2 + 1) / 2;
		}

		return super.attackEntityFrom(par1DamageSource, par2);
	}

	public boolean attackEntityAsMob(Entity par1Entity) {
		int var2 = this.func_48139_F_()?4:2;
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), var2);
	}

	public boolean interact(EntityPlayer par1EntityPlayer) {
		ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();
		if (!this.func_48139_F_()) {
			if (var2 != null && var2.itemID == Item.bone.shiftedIndex && !this.isAngry()) {
				--var2.stackSize;
				if (var2.stackSize <= 0) {
					par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
				}

				if (!this.worldObj.isRemote) {
					if (this.rand.nextInt(3) == 0) {
						this.func_48138_b(true);
						this.setPathToEntity((PathEntity)null);
						this.func_48092_c((EntityLiving)null);
						this.field_48146_a.func_48407_a(true);
						this.setEntityHealth(20);
						this.func_48143_a(par1EntityPlayer.username);
						this.func_48142_a(true);
						this.worldObj.setEntityState(this, (byte)7);
					} else {
						this.func_48142_a(false);
						this.worldObj.setEntityState(this, (byte)6);
					}
				}

				return true;
			}
		} else {
			if (var2 != null && Item.itemsList[var2.itemID] instanceof ItemFood) {
				ItemFood var3 = (ItemFood)Item.itemsList[var2.itemID];
				if (var3.isWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectInt(18) < 20) {
					--var2.stackSize;
					this.heal(var3.getHealAmount());
					if (var2.stackSize <= 0) {
						par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
					}

					return true;
				}
			}

			if (par1EntityPlayer.username.equalsIgnoreCase(this.func_48145_ag()) && !this.worldObj.isRemote && !this.isWheat(var2)) {
				this.field_48146_a.func_48407_a(!this.func_48141_af());
				this.isJumping = false;
				this.setPathToEntity((PathEntity)null);
			}
		}

		return super.interact(par1EntityPlayer);
	}

	public void handleHealthUpdate(byte par1) {
		if (par1 == 8) {
			this.field_25052_g = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		} else {
			super.handleHealthUpdate(par1);
		}

	}

	public float setTailRotation() {
		return this.isAngry()?1.5393804F:(this.func_48139_F_()?(0.55F - (float)(20 - this.dataWatcher.getWatchableObjectInt(18)) * 0.02F) * 3.1415927F:0.62831855F);
	}

	public boolean isWheat(ItemStack par1ItemStack) {
		return par1ItemStack == null?false:(!(Item.itemsList[par1ItemStack.itemID] instanceof ItemFood)?false:((ItemFood)Item.itemsList[par1ItemStack.itemID]).isWolfsFavoriteMeat());
	}

	public int getMaxSpawnedInChunk() {
		return 8;
	}

	public boolean isAngry() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
	}

	public void setAngry(boolean par1) {
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);
		if (par1) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 2)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -3)));
		}

	}

	public EntityAnimal spawnBabyAnimal(EntityAnimal par1EntityAnimal) {
		EntityWolf var2 = new EntityWolf(this.worldObj);
		var2.func_48143_a(this.func_48145_ag());
		var2.func_48138_b(true);
		return var2;
	}

	public void func_48150_h(boolean par1) {
		this.looksWithInterest = par1;
	}

	public boolean func_48135_b(EntityAnimal par1EntityAnimal) {
		if (par1EntityAnimal == this) {
			return false;
		} else if (!this.func_48139_F_()) {
			return false;
		} else if (!(par1EntityAnimal instanceof EntityWolf)) {
			return false;
		} else {
			EntityWolf var2 = (EntityWolf)par1EntityAnimal;
			return !var2.func_48139_F_()?false:(var2.func_48141_af()?false:this.func_48136_o_() && var2.func_48136_o_());
		}
	}
}
