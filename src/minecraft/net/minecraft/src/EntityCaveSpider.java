package net.minecraft.src;

import org.getspout.spout.entity.CraftCaveSpider; // Spout
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public class EntityCaveSpider extends EntitySpider {

   public EntityCaveSpider(World var1) {
      super(var1);
      this.texture = "/mob/cavespider.png";
      this.setSize(0.7F, 0.5F);
		//Spout start
		this.spoutEntity = new CraftCaveSpider(this);
		//Spout end
   }

   public int getMaxHealth() {
      return 12;
   }

   public float spiderScaleAmount() {
      return 0.7F;
   }

   protected boolean attackEntityAsMob(Entity var1) {
      if(super.attackEntityAsMob(var1)) {
         if(var1 instanceof EntityLiving) {
            byte var2 = 0;
            if(this.worldObj.difficultySetting > 1) {
               if(this.worldObj.difficultySetting == 2) {
                  var2 = 7;
               } else if(this.worldObj.difficultySetting == 3) {
                  var2 = 15;
               }
            }

            if(var2 > 0) {
               ((EntityLiving)var1).addPotionEffect(new PotionEffect(Potion.potionPoison.id, var2 * 20, 0));
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
