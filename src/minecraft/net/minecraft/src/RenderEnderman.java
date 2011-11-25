/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.minecraft.src;

import java.util.Random;

import org.getspout.spout.entity.EntitySkinType;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class RenderEnderman extends RenderLiving
{
	 private ModelEnderman field_35444_c;
	 private Random field_35445_h;

	 public RenderEnderman()
	 {
		  super(new ModelEnderman(), 0.5F);
		  field_35445_h = new Random();
		  field_35444_c = (ModelEnderman)super.mainModel;
		  setRenderPassModel(field_35444_c);
	 }

	 public void func_35442_a(EntityEnderman entityenderman, double d, double d1, double d2, 
				float f, float f1)
	 {
		  field_35444_c.field_35407_a = entityenderman.func_35176_r() > 0;
		  field_35444_c.field_35406_b = entityenderman.field_35187_a;
		  if(entityenderman.field_35187_a)
		  {
				double d3 = 0.02D;
				d += field_35445_h.nextGaussian() * d3;
				d2 += field_35445_h.nextGaussian() * d3;
		  }
		  super.doRenderLiving(entityenderman, d, d1, d2, f, f1);
	 }

	 protected void func_35443_a(EntityEnderman entityenderman, float f)
	 {
		  super.renderEquippedItems(entityenderman, f);
		  if(entityenderman.func_35176_r() > 0)
		  {
				GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
				GL11.glPushMatrix();
				float f1 = 0.5F;
				GL11.glTranslatef(0.0F, 0.6875F, -0.75F);
				f1 *= 1.0F;
				GL11.glRotatef(20F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f1, -f1, f1);
				int i = entityenderman.func_35115_a(f);
				int j = i % 0x10000;
				int k = i / 0x10000;
				GL13.glMultiTexCoord2f(33985 /*GL_TEXTURE1_ARB*/, (float)j / 1.0F, (float)k / 1.0F);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				loadTexture("/terrain.png");
				renderBlocks.renderBlockOnInventory(Block.blocksList[entityenderman.func_35176_r()], entityenderman.func_35180_s(), 1.0F);
				GL11.glPopMatrix();
				GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		  }
	 }

	 protected boolean func_35441_a(EntityEnderman entityenderman, int i, float f)
	 {
		  if(i != 0)
		  {
				return false;
		  } else
		  {
		  	//Spout Start
		  	loadTexture(EntitySkinType.getTexture(EntitySkinType.ENDERMAN_EYES, entityenderman, "/mob/enderman_eyes.png"));
		  	//Spout End
				float f1 = 1.0F;
				GL11.glEnable(3042 /*GL_BLEND*/);
				GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
				GL11.glBlendFunc(1, 1);
				GL11.glDisable(2896 /*GL_LIGHTING*/);
				int j = 61680;
				int k = j % 0x10000;
				int l = j / 0x10000;
				GL13.glMultiTexCoord2f(33985 /*GL_TEXTURE1_ARB*/, (float)k / 1.0F, (float)l / 1.0F);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(2896 /*GL_LIGHTING*/);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
				return true;
		  }
	 }

	 protected boolean shouldRenderPass(Entity entityliving, int i, float f)
	 {
		  return func_35441_a((EntityEnderman)entityliving, i, f);
	 }

	 protected void renderEquippedItems(EntityLiving entityliving, float f)
	 {
		  func_35443_a((EntityEnderman)entityliving, f);
	 }

	 public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, 
				float f, float f1)
	 {
		  func_35442_a((EntityEnderman)entityliving, d, d1, d2, f, f1);
	 }

	 public void doRender(Entity entity, double d, double d1, double d2, 
				float f, float f1)
	 {
		  func_35442_a((EntityEnderman)entity, d, d1, d2, f, f1);
	 }
}
