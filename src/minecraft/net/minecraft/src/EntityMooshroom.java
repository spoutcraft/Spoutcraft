package net.minecraft.src;

import net.minecraft.src.Block;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import org.spoutcraft.client.entity.CraftMooshroom; //Spout

public class EntityMooshroom extends EntityCow {

	public EntityMooshroom(World par1World) {
		super(par1World);
		this.texture = "/mob/redcow.png";
		this.setSize(0.9F, 1.3F);
		//Spout start
        this.spoutEntity = new CraftMooshroom(this);
        //Spout end
	}

	public boolean interact(EntityPlayer par1EntityPlayer) {
		ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();
		if (var2 != null && var2.itemID == Item.bowlEmpty.shiftedIndex && this.func_48123_at() >= 0) {
			par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, new ItemStack(Item.bowlSoup));
			return true;
		} else if (var2 != null && var2.itemID == Item.shears.shiftedIndex && this.func_48123_at() >= 0) {
			this.setEntityDead();
			this.worldObj.spawnParticle("largeexplode", this.posX, this.posY + (double)(this.height / 2.0F), this.posZ, 0.0D, 0.0D, 0.0D);
			if (!this.worldObj.isRemote) {
				EntityCow var3 = new EntityCow(this.worldObj);
				var3.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
				var3.setEntityHealth(this.getEntityHealth());
				var3.renderYawOffset = this.renderYawOffset;
				this.worldObj.spawnEntityInWorld(var3);

				for (int var4 = 0; var4 < 5; ++var4) {
					this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY + (double)this.height, this.posZ, new ItemStack(Block.mushroomRed)));
				}
			}

			return true;
		} else {
			return super.interact(par1EntityPlayer);
		}
	}

	public EntityAnimal spawnBabyAnimal(EntityAnimal par1EntityAnimal) {
		return new EntityMooshroom(this.worldObj);
	}
}
