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

	public EntityMooshroom(World var1) {
		super(var1);
		this.texture = "/mob/redcow.png";
		this.setSize(0.9F, 1.3F);
        //Spout start
        this.spoutEntity = new CraftMooshroom(this);
        //Spout end
	}

	public boolean interact(EntityPlayer var1) {
		ItemStack var2 = var1.inventory.getCurrentItem();
		if(var2 != null && var2.itemID == Item.bowlEmpty.shiftedIndex && this.func_40146_g() >= 0) {
			var1.inventory.setInventorySlotContents(var1.inventory.currentItem, new ItemStack(Item.bowlSoup));
			return true;
		} else if(var2 != null && var2.itemID == Item.shears.shiftedIndex && this.func_40146_g() >= 0) {
			this.setEntityDead();
			EntityCow var3 = new EntityCow(this.worldObj);
			var3.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			var3.setEntityHealth(this.getEntityHealth());
			var3.renderYawOffset = this.renderYawOffset;
			this.worldObj.entityJoinedWorld(var3);
			this.worldObj.spawnParticle("largeexplode", this.posX, this.posY + (double)(this.height / 2.0F), this.posZ, 0.0D, 0.0D, 0.0D);

			for(int var4 = 0; var4 < 5; ++var4) {
				this.worldObj.entityJoinedWorld(new EntityItem(this.worldObj, this.posX, this.posY + (double)this.height, this.posZ, new ItemStack(Block.mushroomRed)));
			}

			return true;
		} else {
			return super.interact(var1);
		}
	}

	protected EntityAnimal func_40145_a(EntityAnimal var1) {
		return new EntityMooshroom(this.worldObj);
	}
}
