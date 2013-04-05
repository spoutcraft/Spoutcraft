package net.minecraft.src;

import org.lwjgl.opengl.GL11;
// MCPatcher Start
import com.prupe.mcpatcher.mod.MobOverlay;
// MCPatcher End

public class RenderSnowMan extends RenderLiving {

	/** A reference to the Snowman model in RenderSnowMan. */
	private ModelSnowMan snowmanModel;

	public RenderSnowMan() {
		super(new ModelSnowMan(), 0.5F);
		this.snowmanModel = (ModelSnowMan)super.mainModel;
		this.setRenderPassModel(this.snowmanModel);
	}

	/**
	 * Renders this snowman's pumpkin.
	 */
	protected void renderSnowmanPumpkin(EntitySnowman par1EntitySnowman, float par2) {
		super.renderEquippedItems(par1EntitySnowman, par2);
		ItemStack var3 = new ItemStack(Block.pumpkin, 1);

		if (var3 != null && var3.getItem().itemID < 256) {
			GL11.glPushMatrix();
			this.snowmanModel.head.postRender(0.0625F);

			if (RenderBlocks.renderItemIn3d(Block.blocksList[var3.itemID].getRenderType())) {
				float var4 = 0.625F;
				GL11.glTranslatef(0.0F, -0.34375F, 0.0F);
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var4, -var4, var4);
			}

			// MCPatcher Start
			if (MobOverlay.setupSnowman(par1EntitySnowman)) {
				this.loadTexture(MobOverlay.snowmanOverlayTexture);
				MobOverlay.renderSnowmanOverlay();
			} else {
				this.renderManager.itemRenderer.renderItem(par1EntitySnowman, var3, 0);
			}
			// MCPatcher End

			GL11.glPopMatrix();
		}
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		this.renderSnowmanPumpkin((EntitySnowman)par1EntityLiving, par2);
	}
}
