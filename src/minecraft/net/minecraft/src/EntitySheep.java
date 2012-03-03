package net.minecraft.src;

import java.util.Random;

import org.spoutcraft.client.entity.CraftSheep;

import net.minecraft.src.Block;
import net.minecraft.src.EntityAIEatGrass;
import net.minecraft.src.EntityAIFollowParent;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAIMate;
import net.minecraft.src.EntityAIPanic;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAITempt;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntitySheep extends EntityAnimal {

	public static float[][] fleeceColorTable = new float[][]{{1.0F, 1.0F, 1.0F}, {0.95F, 0.7F, 0.2F}, {0.9F, 0.5F, 0.85F}, {0.6F, 0.7F, 0.95F}, {0.9F, 0.9F, 0.2F}, {0.5F, 0.8F, 0.1F}, {0.95F, 0.7F, 0.8F}, {0.3F, 0.3F, 0.3F}, {0.6F, 0.6F, 0.6F}, {0.3F, 0.6F, 0.7F}, {0.7F, 0.4F, 0.9F}, {0.2F, 0.4F, 0.8F}, {0.5F, 0.4F, 0.3F}, {0.4F, 0.5F, 0.2F}, {0.8F, 0.3F, 0.3F}, {0.1F, 0.1F, 0.1F}};
	private int sheepTimer;
	private EntityAIEatGrass field_48137_c = new EntityAIEatGrass(this);
	public static float[][] origFleeceColorTable = (float[][])fleeceColorTable.clone();

	public EntitySheep(World par1World) {
		super(par1World);
		this.texture = "/mob/sheep.png";
		this.setSize(0.9F, 1.3F);
		float var2 = 0.23F;
		this.func_48084_aL().func_48664_a(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
		this.tasks.addTask(2, new EntityAIMate(this, var2));
		this.tasks.addTask(3, new EntityAITempt(this, 0.25F, Item.wheat.shiftedIndex, false));
		this.tasks.addTask(4, new EntityAIFollowParent(this, 0.25F));
		this.tasks.addTask(5, this.field_48137_c);
		this.tasks.addTask(6, new EntityAIWander(this, var2));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		//Spout start
		this.spoutEntity = new CraftSheep(this);
		//Spout end
	}

	protected boolean isAIEnabled() {
		return true;
	}

	protected void updateAITasks() {
		this.sheepTimer = this.field_48137_c.func_48396_h();
		super.updateAITasks();
	}

	public void onLivingUpdate() {
		if (this.worldObj.isRemote) {
			this.sheepTimer = Math.max(0, this.sheepTimer - 1);
		}

		super.onLivingUpdate();
	}

	public int getMaxHealth() {
		return 8;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte)0));
	}

	protected void dropFewItems(boolean par1, int par2) {
		if (!this.getSheared()) {
			this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 0.0F);
		}

	}

	protected int getDropItemId() {
		return Block.cloth.blockID;
	}

	public void handleHealthUpdate(byte par1) {
		if (par1 == 10) {
			this.sheepTimer = 40;
		} else {
			super.handleHealthUpdate(par1);
		}

	}

	public float func_44003_c(float par1) {
		return this.sheepTimer <= 0?0.0F:(this.sheepTimer >= 4 && this.sheepTimer <= 36?1.0F:(this.sheepTimer < 4?((float)this.sheepTimer - par1) / 4.0F:-((float)(this.sheepTimer - 40) - par1) / 4.0F));
	}

	public float func_44002_d(float par1) {
		if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
			float var2 = ((float)(this.sheepTimer - 4) - par1) / 32.0F;
			return 0.62831855F + 0.21991149F * MathHelper.sin(var2 * 28.7F);
		} else {
			return this.sheepTimer > 0?0.62831855F:this.rotationPitch / 57.295776F;
		}
	}

	public boolean interact(EntityPlayer par1EntityPlayer) {
		ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();
		if (var2 != null && var2.itemID == Item.shears.shiftedIndex && !this.getSheared() && !this.isChild()) {
			if (!this.worldObj.isRemote) {
				this.setSheared(true);
				int var3 = 1 + this.rand.nextInt(3);

				for (int var4 = 0; var4 < var3; ++var4) {
					EntityItem var5 = this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 1.0F);
					var5.motionY += (double)(this.rand.nextFloat() * 0.05F);
					var5.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
					var5.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				}
			}

			var2.damageItem(1, par1EntityPlayer);
		}

		return super.interact(par1EntityPlayer);
	}

	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("Sheared", this.getSheared());
		par1NBTTagCompound.setByte("Color", (byte)this.getFleeceColor());
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setSheared(par1NBTTagCompound.getBoolean("Sheared"));
		this.setFleeceColor(par1NBTTagCompound.getByte("Color"));
	}

	protected String getLivingSound() {
		return "mob.sheep";
	}

	protected String getHurtSound() {
		return "mob.sheep";
	}

	protected String getDeathSound() {
		return "mob.sheep";
	}

	public int getFleeceColor() {
		return this.dataWatcher.getWatchableObjectByte(16) & 15;
	}

	public void setFleeceColor(int par1) {
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);
		this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & 240 | par1 & 15)));
	}

	public boolean getSheared() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 16) != 0;
	}

	public void setSheared(boolean par1) {
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);
		if (par1) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 16)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -17)));
		}

	}

	public static int getRandomFleeceColor(Random par0Random) {
		int var1 = par0Random.nextInt(100);
		return var1 < 5?15:(var1 < 10?7:(var1 < 15?8:(var1 < 18?12:(par0Random.nextInt(500) == 0?6:0))));
	}

	public EntityAnimal spawnBabyAnimal(EntityAnimal par1EntityAnimal) {
		EntitySheep var2 = (EntitySheep)par1EntityAnimal;
		EntitySheep var3 = new EntitySheep(this.worldObj);
		if (this.rand.nextBoolean()) {
			var3.setFleeceColor(this.getFleeceColor());
		} else {
			var3.setFleeceColor(var2.getFleeceColor());
		}

		return var3;
	}

	public void func_48095_u() {
		this.setSheared(false);
		if (this.isChild()) {
			int var1 = this.func_48123_at() + 1200;
			if (var1 > 0) {
				var1 = 0;
			}

			this.func_48122_d(var1);
		}

	}

}
