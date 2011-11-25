package net.minecraft.src;

import net.minecraft.src.EntityFX;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

public class EntityLargeExplodeFX extends EntityFX {

   private int field_35130_a = 0;
   private int field_35129_ay = 0;
   private RenderEngine field_35128_az;
   private float field_35131_aA;


   public EntityLargeExplodeFX(RenderEngine var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      super(var2, var3, var5, var7, 0.0D, 0.0D, 0.0D);
      this.field_35128_az = var1;
      this.field_35129_ay = 6 + this.rand.nextInt(4);
      this.particleRed = this.particleGreen = this.particleBlue = this.rand.nextFloat() * 0.6F + 0.4F;
      this.field_35131_aA = 1.0F - (float)var9 * 0.5F;
   }

   public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      int var8 = (int)(((float)this.field_35130_a + var2) * 15.0F / (float)this.field_35129_ay);
      if(var8 <= 15) {
         this.field_35128_az.bindTexture(this.field_35128_az.getTexture("/misc/explosion.png"));
         float var9 = (float)(var8 % 4) / 4.0F;
         float var10 = var9 + 0.24975F;
         float var11 = (float)(var8 / 4) / 4.0F;
         float var12 = var11 + 0.24975F;
         float var13 = 2.0F * this.field_35131_aA;
         float var14 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)var2 - interpPosX);
         float var15 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)var2 - interpPosY);
         float var16 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)var2 - interpPosZ);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(2896 /*GL_LIGHTING*/);
         RenderHelper.disableStandardItemLighting();
         var1.startDrawingQuads();
         var1.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, 1.0F);
         var1.setNormal(0.0F, 1.0F, 0.0F);
         var1.setBrightness(240);
         var1.addVertexWithUV((double)(var14 - var3 * var13 - var6 * var13), (double)(var15 - var4 * var13), (double)(var16 - var5 * var13 - var7 * var13), (double)var10, (double)var12);
         var1.addVertexWithUV((double)(var14 - var3 * var13 + var6 * var13), (double)(var15 + var4 * var13), (double)(var16 - var5 * var13 + var7 * var13), (double)var10, (double)var11);
         var1.addVertexWithUV((double)(var14 + var3 * var13 + var6 * var13), (double)(var15 + var4 * var13), (double)(var16 + var5 * var13 + var7 * var13), (double)var9, (double)var11);
         var1.addVertexWithUV((double)(var14 + var3 * var13 - var6 * var13), (double)(var15 - var4 * var13), (double)(var16 + var5 * var13 - var7 * var13), (double)var9, (double)var12);
         var1.draw();
         GL11.glPolygonOffset(0.0F, 0.0F);
         GL11.glEnable(2896 /*GL_LIGHTING*/);
      }
   }

   public int getEntityBrightnessForRender(float var1) {
      return '\uf0f0';
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      ++this.field_35130_a;
      if(this.field_35130_a == this.field_35129_ay) {
         this.setEntityDead();
      }

   }

   public int getFXLayer() {
      return 3;
   }
}
