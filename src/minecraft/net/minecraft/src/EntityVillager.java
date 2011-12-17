// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import org.spoutcraft.client.entity.CraftVillager; //Spout

// Referenced classes of package net.minecraft.src:
//            EntityCreature, NBTTagCompound, World

public class EntityVillager extends EntityCreature
{

    private int field_40141_a;

    public EntityVillager(World world)
    {
        this(world, 0);
	//Spout start
	this.spoutEntity = new CraftVillager(this);
	//Spout end
    }

    public EntityVillager(World world, int i)
    {
        super(world);
        field_40141_a = i;
        func_40140_ac();
        moveSpeed = 0.5F;
    }

    public int getMaxHealth()
    {
        return 20;
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Profession", field_40141_a);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        field_40141_a = nbttagcompound.getInteger("Profession");
        func_40140_ac();
    }

    private void func_40140_ac()
    {
        texture = "/mob/villager/villager.png";
        if(field_40141_a == 0)
        {
            texture = "/mob/villager/farmer.png";
        }
        if(field_40141_a == 1)
        {
            texture = "/mob/villager/librarian.png";
        }
        if(field_40141_a == 2)
        {
            texture = "/mob/villager/priest.png";
        }
        if(field_40141_a == 3)
        {
            texture = "/mob/villager/smith.png";
        }
        if(field_40141_a == 4)
        {
            texture = "/mob/villager/butcher.png";
        }
    }

    protected boolean canDespawn()
    {
        return false;
    }

    protected String getLivingSound()
    {
        return "mob.villager.default";
    }

    protected String getHurtSound()
    {
        return "mob.villager.defaulthurt";
    }

    protected String getDeathSound()
    {
        return "mob.villager.defaultdeath";
    }
}
