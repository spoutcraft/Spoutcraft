package net.minecraft.src;

import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.IMob;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Potion;
import net.minecraft.src.World;

public abstract class EntityMob extends EntityCreature implements IMob {

   protected int attackStrength = 2;


   public EntityMob(World var1) {
      super(var1);
      this.field_35171_bJ = 5;
   }

   public void onLivingUpdate() {
      float var1 = this.getEntityBrightness(1.0F);
      if(var1 > 0.5F) {
         this.entityAge += 2;
      }

      super.onLivingUpdate();
   }

   public void onUpdate() {
      super.onUpdate();
      if(!this.worldObj.multiplayerWorld && this.worldObj.difficultySetting == 0) {
         this.setEntityDead();
      }

   }

   protected Entity findPlayerToAttack() {
      EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
      return var1 != null && this.canEntityBeSeen(var1)?var1:null;
   }

   public boolean attackEntityFrom(DamageSource var1, int var2) {
      if(super.attackEntityFrom(var1, var2)) {
         Entity var3 = var1.getEntity();
         if(this.riddenByEntity != var3 && this.ridingEntity != var3) {
            if(var3 != this) {
               this.entityToAttack = var3;
            }

            return true;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   protected boolean attackEntityAsMob(Entity var1) {
      int var2 = this.attackStrength;
      if(this.isPotionActive(Potion.potionDamageBoost)) {
         var2 += 3 << this.getActivePotionEffect(Potion.potionDamageBoost).getAmplifier();
      }

      if(this.isPotionActive(Potion.potionWeakness)) {
         var2 -= 2 << this.getActivePotionEffect(Potion.potionWeakness).getAmplifier();
      }

      return var1.attackEntityFrom(DamageSource.causeMobDamage(this), var2);
   }

   protected void attackEntity(Entity var1, float var2) {
      if(this.attackTime <= 0 && var2 < 2.0F && var1.boundingBox.maxY > this.boundingBox.minY && var1.boundingBox.minY < this.boundingBox.maxY) {
         this.attackTime = 20;
         this.attackEntityAsMob(var1);
      }

   }

   protected float getBlockPathWeight(int var1, int var2, int var3) {
      return 0.5F - this.worldObj.getLightBrightness(var1, var2, var3);
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
   }

   protected boolean func_40147_Y() {
      int var1 = MathHelper.floor_double(this.posX);
      int var2 = MathHelper.floor_double(this.boundingBox.minY);
      int var3 = MathHelper.floor_double(this.posZ);
      if(this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, var1, var2, var3) > this.rand.nextInt(32)) {
         return false;
      } else {
         int var4 = this.worldObj.getBlockLightValue(var1, var2, var3);
         if(this.worldObj.getIsThundering()) {
            int var5 = this.worldObj.skylightSubtracted;
            this.worldObj.skylightSubtracted = 10;
            var4 = this.worldObj.getBlockLightValue(var1, var2, var3);
            this.worldObj.skylightSubtracted = var5;
         }

         return var4 <= this.rand.nextInt(8);
      }
   }

   public boolean getCanSpawnHere() {
      return this.func_40147_Y() && super.getCanSpawnHere();
   }
}
