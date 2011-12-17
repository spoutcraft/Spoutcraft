// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import org.spoutcraft.client.entity.CraftMooshroom; //Spout

// Referenced classes of package net.minecraft.src:
//            EntityCow, EntityPlayer, InventoryPlayer, ItemStack, 
//            Item, ItemShears, World, EntityItem, 
//            Block, EntityAnimal

public class EntityMooshroom extends EntityCow
{

    public EntityMooshroom(World world)
    {
        super(world);
        texture = "/mob/redcow.png";
        setSize(0.9F, 1.3F);
	//Spout start
	this.spoutEntity = new CraftMooshroom(this);
	//Spout end
    }

    public boolean interact(EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        if(itemstack != null && itemstack.itemID == Item.bowlEmpty.shiftedIndex && func_40146_g() >= 0)
        {
            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(Item.bowlSoup));
            return true;
        }
        if(itemstack != null && itemstack.itemID == Item.shears.shiftedIndex && func_40146_g() >= 0)
        {
            setEntityDead();
            EntityCow entitycow = new EntityCow(worldObj);
            entitycow.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
            entitycow.setEntityHealth(getEntityHealth());
            entitycow.renderYawOffset = renderYawOffset;
            worldObj.entityJoinedWorld(entitycow);
            worldObj.spawnParticle("largeexplode", posX, posY + (double)(height / 2.0F), posZ, 0.0D, 0.0D, 0.0D);
            for(int i = 0; i < 5; i++)
            {
                worldObj.entityJoinedWorld(new EntityItem(worldObj, posX, posY + (double)height, posZ, new ItemStack(Block.mushroomRed)));
            }

            return true;
        } else
        {
            return super.interact(entityplayer);
        }
    }

    protected EntityAnimal func_40145_a(EntityAnimal entityanimal)
    {
        return new EntityMooshroom(worldObj);
    }
}
