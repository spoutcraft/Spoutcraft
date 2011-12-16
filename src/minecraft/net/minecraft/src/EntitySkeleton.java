package net.minecraft.src;

import org.spoutcraft.client.entity.CraftSkeleton;

import net.minecraft.src.AchievementList;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumCreatureAttribute;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntitySkeleton extends EntityMob {

	private static final ItemStack defaultHeldItem = new ItemStack(Item.bow, 1);


	public EntitySkeleton(World var1) {
		super(var1);
		this.texture = "/mob/skeleton.png";
		//Spout start
		this.spoutEntity = new CraftSkeleton(this);
		//Spout end
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

	public boolean attackEntityFrom(DamageSource var1, int var2) {
		return super.attackEntityFrom(var1, var2);
	}

	public void onDeath(DamageSource var1) {
		super.onDeath(var1);
		if(var1.getSourceOfDamage() instanceof EntityArrow && var1.getEntity() instanceof EntityPlayer) {
			EntityPlayer var2 = (EntityPlayer)var1.getEntity();
			double var3 = var2.posX - this.posX;
			double var5 = var2.posZ - this.posZ;
			if(var3 * var3 + var5 * var5 >= 2500.0D) {
				var2.triggerAchievement(AchievementList.snipeSkeleton);
			}
		}

	}

	public void onLivingUpdate() {
		if(this.worldObj.isDaytime() && !this.worldObj.multiplayerWorld) {
			float var1 = this.getEntityBrightness(1.0F);
			if(var1 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F) {
				this.func_40046_d(8);
			}
		}

		super.onLivingUpdate();
	}

	protected void attackEntity(Entity var1, float var2) {
		if(var2 < 10.0F) {
			double var3 = var1.posX - this.posX;
			double var5 = var1.posZ - this.posZ;
			if(this.attackTime == 0) {
				EntityArrow var7 = new EntityArrow(this.worldObj, this, 1.0F);
				double var8 = var1.posY + (double)var1.getEyeHeight() - 0.699999988079071D - var7.posY;
				float var10 = MathHelper.sqrt_double(var3 * var3 + var5 * var5) * 0.2F;
				this.worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
				this.worldObj.entityJoinedWorld(var7);
				var7.setArrowHeading(var3, var8 + (double)var10, var5, 1.6F, 12.0F);
				this.attackTime = 60;
			}

			this.rotationYaw = (float)(Math.atan2(var5, var3) * 180.0D / 3.1415927410125732D) - 90.0F;
			this.hasAttacked = true;
		}

	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	protected int getDropItemId() {
		return Item.arrow.shiftedIndex;
	}

	protected void dropFewItems(boolean var1, int var2) {
		int var3 = this.rand.nextInt(3 + var2);

		int var4;
		for(var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.arrow.shiftedIndex, 1);
		}

		var3 = this.rand.nextInt(3 + var2);

		for(var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.bone.shiftedIndex, 1);
		}

	}

	public ItemStack getHeldItem() {
		return defaultHeldItem;
	}

	public EnumCreatureAttribute func_40124_t() {
		return EnumCreatureAttribute.UNDEAD;
	}

}
