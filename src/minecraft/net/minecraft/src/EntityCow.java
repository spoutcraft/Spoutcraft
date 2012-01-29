package net.minecraft.src;

import org.spoutcraft.client.entity.CraftCow;

import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntityCow extends EntityAnimal {

	public EntityCow(World var1) {
		super(var1);
		this.texture = "/mob/cow.png";
		this.setSize(0.9F, 1.3F);
		//Spout start
		this.spoutEntity = new CraftCow(this);
		//Spout end
	}

	public int getMaxHealth() {
		return 10;
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	protected String getLivingSound() {
		return "mob.cow";
	}

	protected String getHurtSound() {
		return "mob.cowhurt";
	}

	protected String getDeathSound() {
		return "mob.cowhurt";
	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	protected int getDropItemId() {
		return Item.leather.shiftedIndex;
	}

	protected void dropFewItems(boolean var1, int var2) {
		int var3 = this.rand.nextInt(3) + this.rand.nextInt(1 + var2);

		int var4;
		for(var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.leather.shiftedIndex, 1);
		}

		var3 = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + var2);

		for(var4 = 0; var4 < var3; ++var4) {
			if(this.isBurning()) {
				this.dropItem(Item.beefCooked.shiftedIndex, 1);
			} else {
				this.dropItem(Item.beefRaw.shiftedIndex, 1);
			}
		}

	}

	public boolean interact(EntityPlayer var1) {
		ItemStack var2 = var1.inventory.getCurrentItem();
		if(var2 != null && var2.itemID == Item.bucketEmpty.shiftedIndex) {
			var1.inventory.setInventorySlotContents(var1.inventory.currentItem, new ItemStack(Item.bucketMilk));
			return true;
		} else {
			return super.interact(var1);
		}
	}

	protected EntityAnimal spawnBabyAnimal(EntityAnimal var1) {
		return new EntityCow(this.worldObj);
	}
}
