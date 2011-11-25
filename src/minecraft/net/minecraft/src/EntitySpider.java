package net.minecraft.src;

import org.getspout.spout.entity.CraftSpider;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EnumCreatureAttribute;
import net.minecraft.src.Item;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public class EntitySpider extends EntityMob {

   public EntitySpider(World var1) {
      super(var1);
      this.texture = "/mob/spider.png";
      this.setSize(1.4F, 0.9F);
      this.moveSpeed = 0.8F;
		//Spout start
		this.spoutEntity = new CraftSpider(this);
		//Spout end
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, new Byte((byte)0));
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
   }

   public void onUpdate() {
      super.onUpdate();
      if(!this.worldObj.multiplayerWorld) {
         this.func_40148_a(this.isCollidedHorizontally);
      }

   }

   public int getMaxHealth() {
      return 16;
   }

   public double getMountedYOffset() {
      return (double)this.height * 0.75D - 0.5D;
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   protected Entity findPlayerToAttack() {
      float var1 = this.getEntityBrightness(1.0F);
      if(var1 < 0.5F) {
         double var2 = 16.0D;
         return this.worldObj.getClosestVulnerablePlayerToEntity(this, var2);
      } else {
         return null;
      }
   }

   protected String getLivingSound() {
      return "mob.spider";
   }

   protected String getHurtSound() {
      return "mob.spider";
   }

   protected String getDeathSound() {
      return "mob.spiderdeath";
   }

   protected void attackEntity(Entity var1, float var2) {
      float var3 = this.getEntityBrightness(1.0F);
      if(var3 > 0.5F && this.rand.nextInt(100) == 0) {
         this.entityToAttack = null;
      } else {
         if(var2 > 2.0F && var2 < 6.0F && this.rand.nextInt(10) == 0) {
            if(this.onGround) {
               double var4 = var1.posX - this.posX;
               double var6 = var1.posZ - this.posZ;
               float var8 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
               this.motionX = var4 / (double)var8 * 0.5D * 0.800000011920929D + this.motionX * 0.20000000298023224D;
               this.motionZ = var6 / (double)var8 * 0.5D * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
               this.motionY = 0.4000000059604645D;
            }
         } else {
            super.attackEntity(var1, var2);
         }

      }
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
   }

   protected int getDropItemId() {
      return Item.silk.shiftedIndex;
   }

   protected void dropFewItems(boolean var1, int var2) {
      super.dropFewItems(var1, var2);
      if(var1 && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + var2) > 0)) {
         this.dropItem(Item.spiderEye.shiftedIndex, 1);
      }

   }

   public boolean isOnLadder() {
      return this.func_40149_l_();
   }

   public void setInWeb() {}

   public float spiderScaleAmount() {
      return 1.0F;
   }

   public EnumCreatureAttribute func_40124_t() {
      return EnumCreatureAttribute.ARTHROPOD;
   }

   public boolean func_40126_a(PotionEffect var1) {
      return var1.getPotionID() == Potion.potionPoison.id?false:super.func_40126_a(var1);
   }

   public boolean func_40149_l_() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
   }

   public void func_40148_a(boolean var1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if(var1) {
         var2 = (byte)(var2 | 1);
      } else {
         var2 &= -2;
      }

      this.dataWatcher.updateObject(16, Byte.valueOf(var2));
   }
}
