package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelEnderman;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.RenderLiving;
import org.lwjgl.opengl.GL11;

//Spout start
import org.spoutcraft.spoutcraftapi.entity.EntitySkinType;
//Spout end

public class RenderEnderman extends RenderLiving {

	private ModelEnderman endermanModel;
	private Random rnd = new Random();

	public RenderEnderman() {
		super(new ModelEnderman(), 0.5F);
		this.endermanModel = (ModelEnderman) super.mainModel;
		this.setRenderPassModel(this.endermanModel);
	}

	public void renderEnderman(EntityEnderman var1, double var2, double var4, double var6, float var8, float var9) {
		this.endermanModel.isCarrying = var1.getCarried() > 0;
		this.endermanModel.isAttacking = var1.isAttacking;
		if (var1.isAttacking) {
			double var10 = 0.02D;
			var2 += this.rnd.nextGaussian() * var10;
			var6 += this.rnd.nextGaussian() * var10;
		}

		super.doRenderLiving(var1, var2, var4, var6, var8, var9);
	}

	protected void renderCarrying(EntityEnderman var1, float var2) {
		super.renderEquippedItems(var1, var2);
		if (var1.getCarried() > 0) {
			GL11.glEnable('\u803a');
			GL11.glPushMatrix();
			float var3 = 0.5F;
			GL11.glTranslatef(0.0F, 0.6875F, -0.75F);
			var3 *= 1.0F;
			GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(var3, -var3, var3);
			int var4 = var1.getEntityBrightnessForRender(var2);
			int var5 = var4 % 65536;
			int var6 = var4 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapEnabled, (float) var5 / 1.0F, (float) var6 / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.loadTexture("/terrain.png");
			this.renderBlocks.renderBlockOnInventory(Block.blocksList[var1.getCarried()], var1.getCarryingData(), 1.0F);
			GL11.glPopMatrix();
			GL11.glDisable('\u803a');
		}

	}

	protected int renderEyes(EntityEnderman var1, int var2, float var3) {
		if (var2 != 0) {
			return -1;
		} else {
			// Spout Start
			loadTexture(var1.getCustomTexture(EntitySkinType.ENDERMAN_EYES, "/mob/enderman_eyes.png"));
			// Spout End
			float var4 = 1.0F;
			GL11.glEnable(3042 /* GL_BLEND */);
			GL11.glDisable(3008 /* GL_ALPHA_TEST */);
			GL11.glBlendFunc(1, 1);
			GL11.glDisable(2896 /* GL_LIGHTING */);
			char var5 = '\uf0f0';
			int var6 = var5 % 65536;
			int var7 = var5 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapEnabled, (float) var6 / 1.0F, (float) var7 / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(2896 /* GL_LIGHTING */);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
			return 1;
		}
	}

	// $FF: synthetic method
	// $FF: bridge method
	protected int shouldRenderPass(EntityLiving var1, int var2, float var3) {
		return this.renderEyes((EntityEnderman) var1, var2, var3);
	}

	// $FF: synthetic method
	// $FF: bridge method
	protected void renderEquippedItems(EntityLiving var1, float var2) {
		this.renderCarrying((EntityEnderman) var1, var2);
	}

	// $FF: synthetic method
	// $FF: bridge method
	public void doRenderLiving(EntityLiving var1, double var2, double var4, double var6, float var8, float var9) {
		this.renderEnderman((EntityEnderman) var1, var2, var4, var6, var8, var9);
	}

	// $FF: synthetic method
	// $FF: bridge method
	public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
		this.renderEnderman((EntityEnderman) var1, var2, var4, var6, var8, var9);
	}
}
