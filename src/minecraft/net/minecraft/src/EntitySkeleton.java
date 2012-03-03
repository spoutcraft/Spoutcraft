package net.minecraft.src;

import org.spoutcraft.client.entity.CraftSkeleton;

import net.minecraft.src.AchievementList;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.EntityAIArrowAttack;
import net.minecraft.src.EntityAIFleeSun;
import net.minecraft.src.EntityAIHurtByTarget;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAINearestAttackableTarget;
import net.minecraft.src.EntityAIRestrictSun;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumCreatureAttribute;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class EntitySkeleton extends EntityMob {

	private static final ItemStack defaultHeldItem = new ItemStack(Item.bow, 1);

	public EntitySkeleton(World par1World) {
		super(par1World);
		this.texture = "/mob/skeleton.png";
		this.moveSpeed = 0.25F;
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(3, new EntityAIFleeSun(this, this.moveSpeed));
		this.tasks.addTask(4, new EntityAIArrowAttack(this, this.moveSpeed, 1, 60));
		this.tasks.addTask(5, new EntityAIWander(this, this.moveSpeed));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.field_48105_bU.addTask(1, new EntityAIHurtByTarget(this, false));
		this.field_48105_bU.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, false));
		//Spout start
		this.spoutEntity = new CraftSkeleton(this);
		//Spout end
	}

	public boolean isAIEnabled() {
		return true;
	}

	public int getMaxHealth() {
		return 20;
	}

	protected String getLivingSound() {
		return "mob.skeleton";
	}

	protected String getHurtSound() {
		return "mob.skeletonhurt";
	}

	protected String getDeathSound() {
		return "mob.skeletonhurt";
	}

	public ItemStack getHeldItem() {
		return defaultHeldItem;
	}

	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	public void onLivingUpdate() {
		if (this.worldObj.isDaytime() && !this.worldObj.isRemote) {
			float var1 = this.getEntityBrightness(1.0F);
			if (var1 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F) {
				this.setFire(8);
			}
		}

		super.onLivingUpdate();
	}

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

	protected int getDropItemId() {
		return Item.arrow.shiftedIndex;
	}

	protected void dropFewItems(boolean par1, int par2) {
		int var3 = this.rand.nextInt(3 + par2);

		int var4;
		for (var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.arrow.shiftedIndex, 1);
		}

		var3 = this.rand.nextInt(3 + par2);

		for (var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.bone.shiftedIndex, 1);
		}

	}

	protected void func_48085_j_(int par1) {
		if (par1 > 0) {
			ItemStack var2 = new ItemStack(Item.bow);
			EnchantmentHelper.func_48441_a(this.rand, var2, 5);
			this.entityDropItem(var2, 0.0F);
		} else {
			this.dropItem(Item.bow.shiftedIndex, 1);
		}

	}

}
