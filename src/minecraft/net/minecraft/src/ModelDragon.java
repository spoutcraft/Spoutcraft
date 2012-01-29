package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class ModelDragon extends ModelBase {
	private ModelRenderer head;
	private ModelRenderer neck;
	private ModelRenderer jaw;
	private ModelRenderer body;
	private ModelRenderer rearLeg;
	private ModelRenderer frontLeg;
	private ModelRenderer rearLegTip;
	private ModelRenderer frontLegTip;
	private ModelRenderer rearFoot;
	private ModelRenderer frontFoot;
	private ModelRenderer wing;
	private ModelRenderer wingTip;
	private float field_40317_s;

	public ModelDragon(float f) {
		textureWidth = 256;
		textureHeight = 256;
		setTextureOffset("body.body", 0, 0);
		setTextureOffset("wing.skin", -56, 88);
		setTextureOffset("wingtip.skin", -56, 144);
		setTextureOffset("rearleg.main", 0, 0);
		setTextureOffset("rearfoot.main", 112, 0);
		setTextureOffset("rearlegtip.main", 196, 0);
		setTextureOffset("head.upperhead", 112, 30);
		setTextureOffset("wing.bone", 112, 88);
		setTextureOffset("head.upperlip", 176, 44);
		setTextureOffset("jaw.jaw", 176, 65);
		setTextureOffset("frontleg.main", 112, 104);
		setTextureOffset("wingtip.bone", 112, 136);
		setTextureOffset("frontfoot.main", 144, 104);
		setTextureOffset("neck.box", 192, 104);
		setTextureOffset("frontlegtip.main", 226, 138);
		setTextureOffset("body.scale", 220, 53);
		setTextureOffset("head.scale", 0, 0);
		setTextureOffset("neck.scale", 48, 0);
		setTextureOffset("head.nostril", 112, 0);
		float f1 = -16F;
		head = new ModelRenderer(this, "head");
		head.addBox("upperlip", -6F, -1F, -8F + f1, 12, 5, 16);
		head.addBox("upperhead", -8F, -8F, 6F + f1, 16, 16, 16);
		head.mirror = true;
		head.addBox("scale", -5F, -12F, 12F + f1, 2, 4, 6);
		head.addBox("nostril", -5F, -3F, -6F + f1, 2, 2, 4);
		head.mirror = false;
		head.addBox("scale", 3F, -12F, 12F + f1, 2, 4, 6);
		head.addBox("nostril", 3F, -3F, -6F + f1, 2, 2, 4);
		jaw = new ModelRenderer(this, "jaw");
		jaw.setRotationPoint(0.0F, 4F, 8F + f1);
		jaw.addBox("jaw", -6F, 0.0F, -16F, 12, 4, 16);
		head.addChild(jaw);
		neck = new ModelRenderer(this, "neck");
		neck.addBox("box", -5F, -5F, -5F, 10, 10, 10);
		neck.addBox("scale", -1F, -9F, -3F, 2, 4, 6);
		body = new ModelRenderer(this, "body");
		body.setRotationPoint(0.0F, 4F, 8F);
		body.addBox("body", -12F, 0.0F, -16F, 24, 24, 64);
		body.addBox("scale", -1F, -6F, -10F, 2, 6, 12);
		body.addBox("scale", -1F, -6F, 10F, 2, 6, 12);
		body.addBox("scale", -1F, -6F, 30F, 2, 6, 12);
		wing = new ModelRenderer(this, "wing");
		wing.setRotationPoint(-12F, 5F, 2.0F);
		wing.addBox("bone", -56F, -4F, -4F, 56, 8, 8);
		wing.addBox("skin", -56F, 0.0F, 2.0F, 56, 0, 56);
		wingTip = new ModelRenderer(this, "wingtip");
		wingTip.setRotationPoint(-56F, 0.0F, 0.0F);
		wingTip.addBox("bone", -56F, -2F, -2F, 56, 4, 4);
		wingTip.addBox("skin", -56F, 0.0F, 2.0F, 56, 0, 56);
		wing.addChild(wingTip);
		frontLeg = new ModelRenderer(this, "frontleg");
		frontLeg.setRotationPoint(-12F, 20F, 2.0F);
		frontLeg.addBox("main", -4F, -4F, -4F, 8, 24, 8);
		frontLegTip = new ModelRenderer(this, "frontlegtip");
		frontLegTip.setRotationPoint(0.0F, 20F, -1F);
		frontLegTip.addBox("main", -3F, -1F, -3F, 6, 24, 6);
		frontLeg.addChild(frontLegTip);
		frontFoot = new ModelRenderer(this, "frontfoot");
		frontFoot.setRotationPoint(0.0F, 23F, 0.0F);
		frontFoot.addBox("main", -4F, 0.0F, -12F, 8, 4, 16);
		frontLegTip.addChild(frontFoot);
		rearLeg = new ModelRenderer(this, "rearleg");
		rearLeg.setRotationPoint(-16F, 16F, 42F);
		rearLeg.addBox("main", -8F, -4F, -8F, 16, 32, 16);
		rearLegTip = new ModelRenderer(this, "rearlegtip");
		rearLegTip.setRotationPoint(0.0F, 32F, -4F);
		rearLegTip.addBox("main", -6F, -2F, 0.0F, 12, 32, 12);
		rearLeg.addChild(rearLegTip);
		rearFoot = new ModelRenderer(this, "rearfoot");
		rearFoot.setRotationPoint(0.0F, 31F, 4F);
		rearFoot.addBox("main", -9F, 0.0F, -20F, 18, 6, 24);
		rearLegTip.addChild(rearFoot);
	}

	public void setLivingAnimations(EntityLiving entityliving, float f, float f1, float f2) {
		field_40317_s = f2;
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		GL11.glPushMatrix();
		EntityDragon entitydragon = (EntityDragon)entity;
		float f6 = entitydragon.field_40173_aw + (entitydragon.field_40172_ax - entitydragon.field_40173_aw) * field_40317_s;
		jaw.rotateAngleX = (float)(Math.sin(f6 * 3.141593F * 2.0F) + 1.0D) * 0.2F;
		float f7 = (float)(Math.sin(f6 * 3.141593F * 2.0F - 1.0F) + 1.0D);
		f7 = (f7 * f7 * 1.0F + f7 * 2.0F) * 0.05F;
		GL11.glTranslatef(0.0F, f7 - 2.0F, -3F);
		GL11.glRotatef(f7 * 2.0F, 1.0F, 0.0F, 0.0F);
		float f8 = -30F;
		float f9 = 22F;
		float f10 = 0.0F;
		float f11 = 1.5F;
		double ad[] = entitydragon.func_40160_a(6, field_40317_s);
		float f12 = updateRotations(entitydragon.func_40160_a(5, field_40317_s)[0] - entitydragon.func_40160_a(10, field_40317_s)[0]);
		float f13 = updateRotations(entitydragon.func_40160_a(5, field_40317_s)[0] + (double)(f12 / 2.0F));
		f8 += 2.0F;
		float f14 = 0.0F;
		float f15 = f6 * 3.141593F * 2.0F;
		f8 = 20F;
		f9 = -12F;
		for (int i = 0; i < 5; i++) {
			double ad3[] = entitydragon.func_40160_a(5 - i, field_40317_s);
			f14 = (float)Math.cos((float)i * 0.45F + f15) * 0.15F;
			neck.rotateAngleY = ((updateRotations(ad3[0] - ad[0]) * 3.141593F) / 180F) * f11;
			neck.rotateAngleX = f14 + (((float)(ad3[1] - ad[1]) * 3.141593F) / 180F) * f11 * 5F;
			neck.rotateAngleZ = ((-updateRotations(ad3[0] - (double)f13) * 3.141593F) / 180F) * f11;
			neck.rotationPointY = f8;
			neck.rotationPointZ = f9;
			neck.rotationPointX = f10;
			f8 = (float)((double)f8 + Math.sin(neck.rotateAngleX) * 10D);
			f9 = (float)((double)f9 - Math.cos(neck.rotateAngleY) * Math.cos(neck.rotateAngleX) * 10D);
			f10 = (float)((double)f10 - Math.sin(neck.rotateAngleY) * Math.cos(neck.rotateAngleX) * 10D);
			neck.render(f5);
		}

		head.rotationPointY = f8;
		head.rotationPointZ = f9;
		head.rotationPointX = f10;
		double ad1[] = entitydragon.func_40160_a(0, field_40317_s);
		head.rotateAngleY = ((updateRotations(ad1[0] - ad[0]) * 3.141593F) / 180F) * 1.0F;
		head.rotateAngleZ = ((-updateRotations(ad1[0] - (double)f13) * 3.141593F) / 180F) * 1.0F;
		head.render(f5);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-f12 * f11 * 1.0F, 0.0F, 0.0F, 1.0F);
		GL11.glTranslatef(0.0F, -1F, 0.0F);
		body.rotateAngleZ = 0.0F;
		body.render(f5);
		for (int j = 0; j < 2; j++) {
			GL11.glEnable(2884 /*GL_CULL_FACE*/);
			float f16 = f6 * 3.141593F * 2.0F;
			wing.rotateAngleX = 0.125F - (float)Math.cos(f16) * 0.2F;
			wing.rotateAngleY = 0.25F;
			wing.rotateAngleZ = (float)(Math.sin(f16) + 0.125D) * 0.8F;
			wingTip.rotateAngleZ = -(float)(Math.sin(f16 + 2.0F) + 0.5D) * 0.75F;
			rearLeg.rotateAngleX = 1.0F + f7 * 0.1F;
			rearLegTip.rotateAngleX = 0.5F + f7 * 0.1F;
			rearFoot.rotateAngleX = 0.75F + f7 * 0.1F;
			frontLeg.rotateAngleX = 1.3F + f7 * 0.1F;
			frontLegTip.rotateAngleX = -0.5F - f7 * 0.1F;
			frontFoot.rotateAngleX = 0.75F + f7 * 0.1F;
			wing.render(f5);
			frontLeg.render(f5);
			rearLeg.render(f5);
			GL11.glScalef(-1F, 1.0F, 1.0F);
			if (j == 0) {
				GL11.glCullFace(1028 /*GL_FRONT*/);
			}
		}

		GL11.glPopMatrix();
		GL11.glCullFace(1029 /*GL_BACK*/);
		GL11.glDisable(2884 /*GL_CULL_FACE*/);
		f14 = -(float)Math.sin(f6 * 3.141593F * 2.0F) * 0.0F;
		f15 = f6 * 3.141593F * 2.0F;
		f8 = 10F;
		f9 = 60F;
		f10 = 0.0F;
		ad = entitydragon.func_40160_a(11, field_40317_s);
		for (int k = 0; k < 12; k++) {
			double ad2[] = entitydragon.func_40160_a(12 + k, field_40317_s);
			f14 = (float)((double)f14 + Math.sin((float)k * 0.45F + f15) * 0.05000000074505806D);
			neck.rotateAngleY = ((updateRotations(ad2[0] - ad[0]) * f11 + 180F) * 3.141593F) / 180F;
			neck.rotateAngleX = f14 + (((float)(ad2[1] - ad[1]) * 3.141593F) / 180F) * f11 * 5F;
			neck.rotateAngleZ = ((updateRotations(ad2[0] - (double)f13) * 3.141593F) / 180F) * f11;
			neck.rotationPointY = f8;
			neck.rotationPointZ = f9;
			neck.rotationPointX = f10;
			f8 = (float)((double)f8 + Math.sin(neck.rotateAngleX) * 10D);
			f9 = (float)((double)f9 - Math.cos(neck.rotateAngleY) * Math.cos(neck.rotateAngleX) * 10D);
			f10 = (float)((double)f10 - Math.sin(neck.rotateAngleY) * Math.cos(neck.rotateAngleX) * 10D);
			neck.render(f5);
		}

		GL11.glPopMatrix();
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5);
	}

	private float updateRotations(double d) {
		for (; d >= 180D; d -= 360D) { }
		for (; d < -180D; d += 360D) { }
		return (float)d;
	}
}
