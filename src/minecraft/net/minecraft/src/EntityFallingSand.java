package net.minecraft.src;

import org.getspout.spout.entity.CraftFallingSand; // Spout

import net.minecraft.src.Block;
import net.minecraft.src.BlockSand;
import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntityFallingSand extends Entity {

   public int blockID;
   public int fallTime = 0;


   public EntityFallingSand(World var1) {
      super(var1);
   }

   public EntityFallingSand(World var1, double var2, double var4, double var6, int var8) {
      super(var1);
      this.blockID = var8;
      this.preventEntitySpawning = true;
      this.setSize(0.98F, 0.98F);
      this.yOffset = this.height / 2.0F;
      this.setPosition(var2, var4, var6);
      this.motionX = 0.0D;
      this.motionY = 0.0D;
      this.motionZ = 0.0D;
      this.prevPosX = var2;
      this.prevPosY = var4;
      this.prevPosZ = var6;
		//Spout start
		this.spoutEntity = new CraftFallingSand(this);
		//Spout end
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   protected void entityInit() {}

   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   public void onUpdate() {
      if(this.blockID == 0) {
         this.setEntityDead();
      } else {
         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         ++this.fallTime;
         this.motionY -= 0.03999999910593033D;
         this.moveEntity(this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.9800000190734863D;
         this.motionY *= 0.9800000190734863D;
         this.motionZ *= 0.9800000190734863D;
         int var1 = MathHelper.floor_double(this.posX);
         int var2 = MathHelper.floor_double(this.posY);
         int var3 = MathHelper.floor_double(this.posZ);
         if(this.fallTime == 1 && this.worldObj.getBlockId(var1, var2, var3) == this.blockID) {
            this.worldObj.setBlockWithNotify(var1, var2, var3, 0);
         } else if(!this.worldObj.multiplayerWorld && this.fallTime == 1) {
            this.setEntityDead();
         }

         if(this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
            if(this.worldObj.getBlockId(var1, var2, var3) != Block.pistonMoving.blockID) {
               this.setEntityDead();
               if((!this.worldObj.canBlockBePlacedAt(this.blockID, var1, var2, var3, true, 1) || BlockSand.canFallBelow(this.worldObj, var1, var2 - 1, var3) || !this.worldObj.setBlockWithNotify(var1, var2, var3, this.blockID)) && !this.worldObj.multiplayerWorld) {
                  this.dropItem(this.blockID, 1);
               }
            }
         } else if(this.fallTime > 100 && !this.worldObj.multiplayerWorld) {
            this.dropItem(this.blockID, 1);
            this.setEntityDead();
         }

      }
   }

   protected void writeEntityToNBT(NBTTagCompound var1) {
      var1.setByte("Tile", (byte)this.blockID);
   }

   protected void readEntityFromNBT(NBTTagCompound var1) {
      this.blockID = var1.getByte("Tile") & 255;
   }

   public float getShadowSize() {
      return 0.0F;
   }

   public World getWorld() {
      return this.worldObj;
   }
}
