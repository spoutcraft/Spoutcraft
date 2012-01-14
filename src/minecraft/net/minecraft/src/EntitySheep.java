package net.minecraft.src;

import java.util.Random;

import org.spoutcraft.client.entity.CraftSheep;

import net.minecraft.src.Block;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntitySheep extends EntityAnimal {

	public static final float[][] fleeceColorTable = new float[][]{{1.0F, 1.0F, 1.0F}, {0.95F, 0.7F, 0.2F}, {0.9F, 0.5F, 0.85F}, {0.6F, 0.7F, 0.95F}, {0.9F, 0.9F, 0.2F}, {0.5F, 0.8F, 0.1F}, {0.95F, 0.7F, 0.8F}, {0.3F, 0.3F, 0.3F}, {0.6F, 0.6F, 0.6F}, {0.3F, 0.6F, 0.7F}, {0.7F, 0.4F, 0.9F}, {0.2F, 0.4F, 0.8F}, {0.5F, 0.4F, 0.3F}, {0.4F, 0.5F, 0.2F}, {0.8F, 0.3F, 0.3F}, {0.1F, 0.1F, 0.1F}};
	private int field_44004_b;


	public EntitySheep(World var1) {
		super(var1);
		this.texture = "/mob/sheep.png";
		this.setSize(0.9F, 1.3F);
		
		//Spout start
		this.spoutEntity = new CraftSheep(this);
		//Spout end
	}

	public int getMaxHealth() {
		return 8;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte)0));
	}

	protected void dropFewItems(boolean var1, int var2) {
		if(!this.getSheared()) {
			this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 0.0F);
		}

	}

	protected int getDropItemId() {
		return Block.cloth.blockID;
	}

	public void onLivingUpdate()
 {

		super.onLivingUpdate();

		if (this.field_44004_b > 0)
 {

			--this.field_44004_b;

		}

	}



	protected void jump()
 {

		if (this.field_44004_b <= 0) {

			super.jump();

		}

	}



	protected void updateEntityActionState() {

		super.updateEntityActionState();

		int var1;

		int var2;

		int var3;

		if (!this.hasPath() && this.field_44004_b <= 0 && (this.isChild() && this.rand.nextInt(50) == 0 || this.rand.nextInt(1000) == 0))
 {

			var1 = MathHelper.floor_double(this.posX);

			var2 = MathHelper.floor_double(this.posY);

			var3 = MathHelper.floor_double(this.posZ);

			if (this.worldObj.getBlockId(var1, var2, var3) == Block.tallGrass.blockID && this.worldObj.getBlockMetadata(var1, var2, var3) == 1 || this.worldObj.getBlockId(var1, var2 - 1, var3) == Block.grass.blockID)
 {

				this.field_44004_b = 40;

				this.worldObj.setEntityState(this, (byte)10);

			}

		}
 else if (this.field_44004_b == 4)
 {

			var1 = MathHelper.floor_double(this.posX);

			var2 = MathHelper.floor_double(this.posY);

			var3 = MathHelper.floor_double(this.posZ);

			boolean var4 = false;

			if (this.worldObj.getBlockId(var1, var2, var3) == Block.tallGrass.blockID)
 {

				this.worldObj.playAuxSFX(2001, var1, var2, var3, Block.tallGrass.blockID + 256);

				this.worldObj.setBlockWithNotify(var1, var2, var3, 0);

				var4 = true;

			}
 else if (this.worldObj.getBlockId(var1, var2 - 1, var3) == Block.grass.blockID)
 {
				this.worldObj.playAuxSFX(2001, var1, var2 - 1, var3, Block.grass.blockID);

				this.worldObj.setBlockWithNotify(var1, var2 - 1, var3, Block.dirt.blockID);

				var4 = true;

			}


			if (var4)
 {

				this.setSheared(false);
				if (this.isChild()) {

					int var5 = this.getDelay() + 1200;

					if (var5 > 0) {

						var5 = 0;

					}


					this.setDelay(var5);

				}

			}

		}

	}



	public void handleHealthUpdate(byte var1) {
		if (var1 == 10)
 {

			this.field_44004_b = 40;

		}
 else
 {

			super.handleHealthUpdate(var1);

		}

	}



	protected boolean isMovementCeased()
 {

		return this.field_44004_b > 0;

	}



	public float func_44003_c(float var1)
 {

		return this.field_44004_b <= 0 ? 0.0F : (this.field_44004_b >= 4 && this.field_44004_b <= 36 ? 1.0F : (this.field_44004_b < 4 ? ((float)this.field_44004_b - var1) / 4.0F : -((float)(this.field_44004_b - 40) - var1) / 4.0F));

	}



	public float func_44002_d(float var1)
 {

		if (this.field_44004_b > 4 && this.field_44004_b <= 36)
 {

			float var2 = ((float)(this.field_44004_b - 4) - var1) / 32.0F;

			return 0.62831855F + 0.21991149F * MathHelper.sin(var2 * 28.7F);

		}
 else
 {

			return this.field_44004_b > 0 ? 0.62831855F : this.rotationPitch / 57.295776F;

		}

	}

	public boolean interact(EntityPlayer var1) {
		ItemStack var2 = var1.inventory.getCurrentItem();
		if(var2 != null && var2.itemID == Item.shears.shiftedIndex && !this.getSheared() && !this.isChild()) {
			if(!this.worldObj.multiplayerWorld) {
				this.setSheared(true);
				int var3 = 1 + this.rand.nextInt(3);

				for(int var4 = 0; var4 < var3; ++var4) {
					EntityItem var5 = this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 1.0F);
					var5.motionY += (double)(this.rand.nextFloat() * 0.05F);
					var5.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
					var5.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				}
			}

			var2.damageItem(1, var1);
		}

		return super.interact(var1);
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setBoolean("Sheared", this.getSheared());
		var1.setByte("Color", (byte)this.getFleeceColor());
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.setSheared(var1.getBoolean("Sheared"));
		this.setFleeceColor(var1.getByte("Color"));
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

	public void setFleeceColor(int var1) {
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);
		this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & 240 | var1 & 15)));
	}

	public boolean getSheared() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 16) != 0;
	}

	public void setSheared(boolean var1) {
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);
		if(var1) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 16)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -17)));
		}

	}

	public static int getRandomFleeceColor(Random var0) {
		int var1 = var0.nextInt(100);
		return var1 < 5?15:(var1 < 10?7:(var1 < 15?8:(var1 < 18?12:(var0.nextInt(500) == 0?6:0))));
	}

	protected EntityAnimal spawnBabyAnimal(EntityAnimal var1) {
		EntitySheep var2 = (EntitySheep)var1;
		EntitySheep var3 = new EntitySheep(this.worldObj);
		if(this.rand.nextBoolean()) {
			var3.setFleeceColor(this.getFleeceColor());
		} else {
			var3.setFleeceColor(var2.getFleeceColor());
		}

		return var3;
	}

}
