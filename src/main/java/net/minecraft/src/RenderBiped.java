package net.minecraft.src;

import org.lwjgl.opengl.GL11;
// Spout Start
import org.spoutcraft.api.material.MaterialData;
// Spout End

public class RenderBiped extends RenderLiving {
	protected ModelBiped modelBipedMain;
	protected float field_77070_b;

	public RenderBiped(ModelBiped par1ModelBiped, float par2) {
		this(par1ModelBiped, par2, 1.0F);
		this.modelBipedMain = par1ModelBiped;
	}

	public RenderBiped(ModelBiped par1ModelBiped, float par2, float par3) {
		super(par1ModelBiped, par2);
		this.modelBipedMain = par1ModelBiped;
		this.field_77070_b = par3;
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		super.renderEquippedItems(par1EntityLiving, par2);
		ItemStack var3 = par1EntityLiving.getHeldItem();

		if (var3 != null) {
			GL11.glPushMatrix();
			this.modelBipedMain.bipedRightArm.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
			float var4;

			if (var3.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[var3.itemID].getRenderType())) {
				var4 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				var4 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var4, -var4, var4);
			} else if (var3.itemID == Item.bow.shiftedIndex) {
				var4 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var4, -var4, var4);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			// Spout Start
			} else if (Item.itemsList[var3.itemID].isFull3D() || var3.itemID == Item.flint.shiftedIndex && MaterialData.getCustomItem(var3.getItemDamage()) instanceof org.spoutcraft.api.material.Tool) {
			// Spout End
				var4 = 0.625F;
				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(var4, -var4, var4);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				var4 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(var4, var4, var4);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			this.renderManager.itemRenderer.renderItem(par1EntityLiving, var3, 0);

			if (var3.getItem().requiresMultipleRenderPasses()) {
				this.renderManager.itemRenderer.renderItem(par1EntityLiving, var3, 1);
			}

			GL11.glPopMatrix();
		}
	}
}
