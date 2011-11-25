// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.List;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Entity, AxisAlignedBB, EntityLiving, MathHelper, 
//            World, Vec3D, MovingObjectPosition, NBTTagCompound, 
//            EntityPlayer

public abstract class EntityThrowable extends Entity
{

    private int field_40079_d;
    private int field_40080_e;
    private int field_40082_ao;
    private int field_40084_ap;
    protected boolean field_40085_a;
    public int field_40081_b;
    public EntityLiving field_40083_c; //Spout protected -> public
    private int field_40087_aq;
    private int field_40086_ar;

    public EntityThrowable(World world)
    {
        super(world);
        field_40079_d = -1;
        field_40080_e = -1;
        field_40082_ao = -1;
        field_40084_ap = 0;
        field_40085_a = false;
        field_40081_b = 0;
        field_40086_ar = 0;
        setSize(0.25F, 0.25F);
    }

    protected void entityInit()
    {
    }

    public boolean isInRangeToRenderDist(double d)
    {
        double d1 = boundingBox.getAverageEdgeLength() * 4D;
        d1 *= 64D;
        return d < d1 * d1;
    }

    public EntityThrowable(World world, EntityLiving entityliving)
    {
        super(world);
        field_40079_d = -1;
        field_40080_e = -1;
        field_40082_ao = -1;
        field_40084_ap = 0;
        field_40085_a = false;
        field_40081_b = 0;
        field_40086_ar = 0;
        field_40083_c = entityliving;
        setSize(0.25F, 0.25F);
        setLocationAndAngles(entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
        posY -= 0.10000000149011612D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
        setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        float f = 0.4F;
        motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
        motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
        motionY = -MathHelper.sin(((rotationPitch + func_40074_d()) / 180F) * 3.141593F) * f;
        setThrowableHeading(motionX, motionY, motionZ, func_40077_c(), 1.0F);
    }

    public EntityThrowable(World world, double d, double d1, double d2)
    {
        super(world);
        field_40079_d = -1;
        field_40080_e = -1;
        field_40082_ao = -1;
        field_40084_ap = 0;
        field_40085_a = false;
        field_40081_b = 0;
        field_40086_ar = 0;
        field_40087_aq = 0;
        setSize(0.25F, 0.25F);
        setPosition(d, d1, d2);
        yOffset = 0.0F;
    }

    protected float func_40077_c()
    {
        return 1.5F;
    }

    protected float func_40074_d()
    {
        return 0.0F;
    }

    public void setThrowableHeading(double d, double d1, double d2, float f, 
            float f1)
    {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
        d1 += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
        d2 += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        motionX = d;
        motionY = d1;
        motionZ = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / 3.1415927410125732D);
        prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f3) * 180D) / 3.1415927410125732D);
        field_40087_aq = 0;
    }

    public void setVelocity(double d, double d1, double d2)
    {
        motionX = d;
        motionY = d1;
        motionZ = d2;
        if(prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(d * d + d2 * d2);
            prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / 3.1415927410125732D);
            prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f) * 180D) / 3.1415927410125732D);
        }
    }

    public void onUpdate()
    {
        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
        super.onUpdate();
        if(field_40081_b > 0)
        {
            field_40081_b--;
        }
        if(field_40085_a)
        {
            int i = worldObj.getBlockId(field_40079_d, field_40080_e, field_40082_ao);
            if(i != field_40084_ap)
            {
                field_40085_a = false;
                motionX *= rand.nextFloat() * 0.2F;
                motionY *= rand.nextFloat() * 0.2F;
                motionZ *= rand.nextFloat() * 0.2F;
                field_40087_aq = 0;
                field_40086_ar = 0;
            } else
            {
                field_40087_aq++;
                if(field_40087_aq == 1200)
                {
                    setEntityDead();
                }
                return;
            }
        } else
        {
            field_40086_ar++;
        }
        Vec3D vec3d = Vec3D.createVector(posX, posY, posZ);
        Vec3D vec3d1 = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
        MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec3d, vec3d1);
        vec3d = Vec3D.createVector(posX, posY, posZ);
        vec3d1 = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
        if(movingobjectposition != null)
        {
            vec3d1 = Vec3D.createVector(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }
        if(!worldObj.multiplayerWorld)
        {
            Entity entity = null;
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
            double d = 0.0D;
            for(int k = 0; k < list.size(); k++)
            {
                Entity entity1 = (Entity)list.get(k);
                if(!entity1.canBeCollidedWith() || entity1 == field_40083_c && field_40086_ar < 5)
                {
                    continue;
                }
                float f4 = 0.3F;
                AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f4, f4, f4);
                MovingObjectPosition movingobjectposition1 = axisalignedbb.func_1169_a(vec3d, vec3d1);
                if(movingobjectposition1 == null)
                {
                    continue;
                }
                double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);
                if(d1 < d || d == 0.0D)
                {
                    entity = entity1;
                    d = d1;
                }
            }

            if(entity != null)
            {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }
        if(movingobjectposition != null)
        {
            func_40078_a(movingobjectposition);
        }
        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / 3.1415927410125732D);
        for(rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / 3.1415927410125732D); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }
        for(; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
        float f1 = 0.99F;
        float f2 = func_40075_e();
        if(isInWater())
        {
            for(int j = 0; j < 4; j++)
            {
                float f3 = 0.25F;
                worldObj.spawnParticle("bubble", posX - motionX * (double)f3, posY - motionY * (double)f3, posZ - motionZ * (double)f3, motionX, motionY, motionZ);
            }

            f1 = 0.8F;
        }
        motionX *= f1;
        motionY *= f1;
        motionZ *= f1;
        motionY -= f2;
        setPosition(posX, posY, posZ);
    }

    protected float func_40075_e()
    {
        return 0.03F;
    }

    protected abstract void func_40078_a(MovingObjectPosition movingobjectposition);

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("xTile", (short)field_40079_d);
        nbttagcompound.setShort("yTile", (short)field_40080_e);
        nbttagcompound.setShort("zTile", (short)field_40082_ao);
        nbttagcompound.setByte("inTile", (byte)field_40084_ap);
        nbttagcompound.setByte("shake", (byte)field_40081_b);
        nbttagcompound.setByte("inGround", (byte)(field_40085_a ? 1 : 0));
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        field_40079_d = nbttagcompound.getShort("xTile");
        field_40080_e = nbttagcompound.getShort("yTile");
        field_40082_ao = nbttagcompound.getShort("zTile");
        field_40084_ap = nbttagcompound.getByte("inTile") & 0xff;
        field_40081_b = nbttagcompound.getByte("shake") & 0xff;
        field_40085_a = nbttagcompound.getByte("inGround") == 1;
    }

    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
    }

    public float getShadowSize()
    {
        return 0.0F;
    }
}
