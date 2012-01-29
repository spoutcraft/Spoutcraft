package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class EntityPickupFX extends EntityFX {
	private Entity field_675_a;
	private Entity field_679_o;
	private int age;
	private int maxAge;
	private float field_676_r;

	public EntityPickupFX(World world, Entity entity, Entity entity1, float f) {
		super(world, entity.posX, entity.posY, entity.posZ, entity.motionX, entity.motionY, entity.motionZ);
		age = 0;
		maxAge = 0;
		field_675_a = entity;
		field_679_o = entity1;
		maxAge = 3;
		field_676_r = f;
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		float f6 = ((float)age + f) / (float)maxAge;
		f6 *= f6;
		double d = field_675_a.posX;
		double d1 = field_675_a.posY;
		double d2 = field_675_a.posZ;
		double d3 = field_679_o.lastTickPosX + (field_679_o.posX - field_679_o.lastTickPosX) * (double)f;
		double d4 = field_679_o.lastTickPosY + (field_679_o.posY - field_679_o.lastTickPosY) * (double)f + (double)field_676_r;
		double d5 = field_679_o.lastTickPosZ + (field_679_o.posZ - field_679_o.lastTickPosZ) * (double)f;
		double d6 = d + (d3 - d) * (double)f6;
		double d7 = d1 + (d4 - d1) * (double)f6;
		double d8 = d2 + (d5 - d2) * (double)f6;
		int i = MathHelper.floor_double(d6);
		int j = MathHelper.floor_double(d7 + (double)(yOffset / 2.0F));
		int k = MathHelper.floor_double(d8);
		int l = getEntityBrightnessForRender(f);
		int i1 = l % 0x10000;
		int j1 = l / 0x10000;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapEnabled, (float)i1 / 1.0F, (float)j1 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		d6 -= interpPosX;
		d7 -= interpPosY;
		d8 -= interpPosZ;
		RenderManager.instance.renderEntityWithPosYaw(field_675_a, (float)d6, (float)d7, (float)d8, field_675_a.rotationYaw, f);
	}

	public void onUpdate() {
		age++;
		if (age == maxAge) {
			setEntityDead();
		}
	}

	public int getFXLayer() {
		return 3;
	}
}
