// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import java.util.Random;

//Spout Start
import org.getspout.spout.entity.EntitySkinType;
//Spout End

// Referenced classes of package net.minecraft.src:
//            EntityFlying, IMob, DataWatcher, World, 
//            MathHelper, Entity, AxisAlignedBB, EntityFireball, 
//            Vec3D, Item

public class EntityGhast extends EntityFlying
    implements IMob
{

    public EntityGhast(World world)
    {
        super(world);
        courseChangeCooldown = 0;
        targetedEntity = null;
        aggroCooldown = 0;
        prevAttackCounter = 0;
        attackCounter = 0;
        texture = "/mob/ghast.png";
        setSize(4F, 4F);
        isImmuneToFire = true;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public void onUpdate()
    {
        super.onUpdate();
        byte byte0 = dataWatcher.getWatchableObjectByte(16);
        texture = byte0 != 1 ? "/mob/ghast.png" : "/mob/ghast_fire.png";
        //Spout Start
        setTextureToRender((byte) (byte0 == 1? EntitySkinType.GHAST_MOUTH.getId() : 0));
        //Spout End
    }

    protected void updatePlayerActionState()
    {
        if(!worldObj.multiplayerWorld && worldObj.difficultySetting == 0)
        {
            setEntityDead();
        }
        despawnEntity();
        prevAttackCounter = attackCounter;
        double d = waypointX - posX;
        double d1 = waypointY - posY;
        double d2 = waypointZ - posZ;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        if(d3 < 1.0D || d3 > 60D)
        {
            waypointX = posX + (double)((rand.nextFloat() * 2.0F - 1.0F) * 16F);
            waypointY = posY + (double)((rand.nextFloat() * 2.0F - 1.0F) * 16F);
            waypointZ = posZ + (double)((rand.nextFloat() * 2.0F - 1.0F) * 16F);
        }
        if(courseChangeCooldown-- <= 0)
        {
            courseChangeCooldown += rand.nextInt(5) + 2;
            if(isCourseTraversable(waypointX, waypointY, waypointZ, d3))
            {
                motionX += (d / d3) * 0.10000000000000001D;
                motionY += (d1 / d3) * 0.10000000000000001D;
                motionZ += (d2 / d3) * 0.10000000000000001D;
            } else
            {
                waypointX = posX;
                waypointY = posY;
                waypointZ = posZ;
            }
        }
        if(targetedEntity != null && targetedEntity.isDead)
        {
            targetedEntity = null;
        }
        if(targetedEntity == null || aggroCooldown-- <= 0)
        {
            targetedEntity = worldObj.getClosestPlayerToEntity(this, 100D);
            if(targetedEntity != null)
            {
                aggroCooldown = 20;
            }
        }
        double d4 = 64D;
        if(targetedEntity != null && targetedEntity.getDistanceSqToEntity(this) < d4 * d4)
        {
            double d5 = targetedEntity.posX - posX;
            double d6 = (targetedEntity.boundingBox.minY + (double)(targetedEntity.height / 2.0F)) - (posY + (double)(height / 2.0F));
            double d7 = targetedEntity.posZ - posZ;
            renderYawOffset = rotationYaw = (-(float)Math.atan2(d5, d7) * 180F) / 3.141593F;
            if(canEntityBeSeen(targetedEntity))
            {
                if(attackCounter == 10)
                {
                    worldObj.playSoundAtEntity(this, "mob.ghast.charge", getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }
                attackCounter++;
                if(attackCounter == 20)
                {
                    worldObj.playSoundAtEntity(this, "mob.ghast.fireball", getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    EntityFireball entityfireball = new EntityFireball(worldObj, this, d5, d6, d7);
                    double d8 = 4D;
                    Vec3D vec3d = getLook(1.0F);
                    entityfireball.posX = posX + vec3d.xCoord * d8;
                    entityfireball.posY = posY + (double)(height / 2.0F) + 0.5D;
                    entityfireball.posZ = posZ + vec3d.zCoord * d8;
                    worldObj.entityJoinedWorld(entityfireball);
                    attackCounter = -40;
                }
            } else
            if(attackCounter > 0)
            {
                attackCounter--;
            }
        } else
        {
            renderYawOffset = rotationYaw = (-(float)Math.atan2(motionX, motionZ) * 180F) / 3.141593F;
            if(attackCounter > 0)
            {
                attackCounter--;
            }
        }
        if(!worldObj.multiplayerWorld)
        {
            byte byte0 = dataWatcher.getWatchableObjectByte(16);
            byte byte1 = (byte)(attackCounter <= 10 ? 0 : 1);
            if(byte0 != byte1)
            {
                dataWatcher.updateObject(16, Byte.valueOf(byte1));
            }
        }
    }

    private boolean isCourseTraversable(double d, double d1, double d2, double d3)
    {
        double d4 = (waypointX - posX) / d3;
        double d5 = (waypointY - posY) / d3;
        double d6 = (waypointZ - posZ) / d3;
        AxisAlignedBB axisalignedbb = boundingBox.copy();
        for(int i = 1; (double)i < d3; i++)
        {
            axisalignedbb.offset(d4, d5, d6);
            if(worldObj.getCollidingBoundingBoxes(this, axisalignedbb).size() > 0)
            {
                return false;
            }
        }

        return true;
    }

    protected String getLivingSound()
    {
        return "mob.ghast.moan";
    }

    protected String getHurtSound()
    {
        return "mob.ghast.scream";
    }

    protected String getDeathSound()
    {
        return "mob.ghast.death";
    }

    protected int getDropItemId()
    {
        return Item.gunpowder.shiftedIndex;
    }

    protected float getSoundVolume()
    {
        return 10F;
    }

    public boolean getCanSpawnHere()
    {
        return rand.nextInt(20) == 0 && super.getCanSpawnHere() && worldObj.difficultySetting > 0;
    }

    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    public int courseChangeCooldown;
    public double waypointX;
    public double waypointY;
    public double waypointZ;
    private Entity targetedEntity;
    private int aggroCooldown;
    public int prevAttackCounter;
    public int attackCounter;
}
