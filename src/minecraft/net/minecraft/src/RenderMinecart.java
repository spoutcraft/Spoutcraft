package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderMinecart extends Render {
	protected ModelBase modelMinecart;

	public RenderMinecart() {
		shadowSize = 0.5F;
		modelMinecart = new ModelMinecart();
	}

	public void func_152_a(EntityMinecart entityminecart, double d, double d1, double d2,
	        float f, float f1) {
		GL11.glPushMatrix();
		long l = (long)entityminecart.entityId * 0x1d66f537L;
		l = l * l * 0x105cb26d1L + l * 0x181c9L;
		float f2 = (((float)(l >> 16 & 7L) + 0.5F) / 8F - 0.5F) * 0.004F;
		float f3 = (((float)(l >> 20 & 7L) + 0.5F) / 8F - 0.5F) * 0.004F;
		float f4 = (((float)(l >> 24 & 7L) + 0.5F) / 8F - 0.5F) * 0.004F;
		GL11.glTranslatef(f2, f3, f4);
		double d3 = entityminecart.lastTickPosX + (entityminecart.posX - entityminecart.lastTickPosX) * (double)f1;
		double d4 = entityminecart.lastTickPosY + (entityminecart.posY - entityminecart.lastTickPosY) * (double)f1;
		double d5 = entityminecart.lastTickPosZ + (entityminecart.posZ - entityminecart.lastTickPosZ) * (double)f1;
		double d6 = 0.30000001192092896D;
		Vec3D vec3d = entityminecart.func_514_g(d3, d4, d5);
		float f5 = entityminecart.prevRotationPitch + (entityminecart.rotationPitch - entityminecart.prevRotationPitch) * f1;
		if (vec3d != null) {
			Vec3D vec3d1 = entityminecart.func_515_a(d3, d4, d5, d6);
			Vec3D vec3d2 = entityminecart.func_515_a(d3, d4, d5, -d6);
			if (vec3d1 == null) {
				vec3d1 = vec3d;
			}
			if (vec3d2 == null) {
				vec3d2 = vec3d;
			}
			d += vec3d.xCoord - d3;
			d1 += (vec3d1.yCoord + vec3d2.yCoord) / 2D - d4;
			d2 += vec3d.zCoord - d5;
			Vec3D vec3d3 = vec3d2.addVector(-vec3d1.xCoord, -vec3d1.yCoord, -vec3d1.zCoord);
			if (vec3d3.lengthVector() != 0.0D) {
				vec3d3 = vec3d3.normalize();
				f = (float)((Math.atan2(vec3d3.zCoord, vec3d3.xCoord) * 180D) / 3.1415926535897931D);
				f5 = (float)(Math.atan(vec3d3.yCoord) * 73D);
			}
		}
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glRotatef(180F - f, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-f5, 0.0F, 0.0F, 1.0F);
		float f6 = (float)entityminecart.func_41023_l() - f1;
		float f7 = (float)entityminecart.func_41025_i() - f1;
		if (f7 < 0.0F) {
			f7 = 0.0F;
		}
		if (f6 > 0.0F) {
			GL11.glRotatef(((MathHelper.sin(f6) * f6 * f7) / 10F) * (float)entityminecart.func_41030_m(), 1.0F, 0.0F, 0.0F);
		}
		if (entityminecart.minecartType != 0) {
			loadTexture("/terrain.png");
			float f8 = 0.75F;
			GL11.glScalef(f8, f8, f8);
			if (entityminecart.minecartType == 1) {
				GL11.glTranslatef(-0.5F, 0.0F, 0.5F);
				GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
				(new RenderBlocks()).renderBlockAsItem(Block.chest, 0, entityminecart.getEntityBrightness(f1));
				GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0.5F, 0.0F, -0.5F);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
			else if (entityminecart.minecartType == 2) {
				GL11.glTranslatef(0.0F, 0.3125F, 0.0F);
				GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
				(new RenderBlocks()).renderBlockAsItem(Block.stoneOvenIdle, 0, entityminecart.getEntityBrightness(f1));
				GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0.0F, -0.3125F, 0.0F);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
			GL11.glScalef(1.0F / f8, 1.0F / f8, 1.0F / f8);
		}
		loadTexture("/item/cart.png");
		GL11.glScalef(-1F, -1F, 1.0F);
		modelMinecart.render(entityminecart, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity, double d, double d1, double d2,
	        float f, float f1) {
		func_152_a((EntityMinecart)entity, d, d1, d2, f, f1);
	}
}
