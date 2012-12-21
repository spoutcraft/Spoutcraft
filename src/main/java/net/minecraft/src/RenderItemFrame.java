package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderItemFrame extends Render {
	private final RenderBlocks renderBlocksInstance = new RenderBlocks();

	public void func_82404_a(EntityItemFrame par1EntityItemFrame, double par2, double par4, double par6, float par8, float par9) {
		GL11.glPushMatrix();
		float var10 = (float)(par1EntityItemFrame.posX - par2) - 0.5F;
		float var11 = (float)(par1EntityItemFrame.posY - par4) - 0.5F;
		float var12 = (float)(par1EntityItemFrame.posZ - par6) - 0.5F;
		int var13 = par1EntityItemFrame.xPosition + Direction.offsetX[par1EntityItemFrame.hangingDirection];
		int var14 = par1EntityItemFrame.yPosition;
		int var15 = par1EntityItemFrame.zPosition + Direction.offsetZ[par1EntityItemFrame.hangingDirection];
		GL11.glTranslatef((float)var13 - var10, (float)var14 - var11, (float)var15 - var12);
		this.renderFrameItemAsBlock(par1EntityItemFrame);
		this.func_82402_b(par1EntityItemFrame);
		GL11.glPopMatrix();
	}

	/**
	 * Render the item frame's item as a block.
	 */
	private void renderFrameItemAsBlock(EntityItemFrame par1EntityItemFrame) {
		GL11.glPushMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderManager.renderEngine.getTexture("/terrain.png"));
		GL11.glRotatef(par1EntityItemFrame.rotationYaw, 0.0F, 1.0F, 0.0F);
		Block var2 = Block.planks;
		float var3 = 0.0625F;
		float var4 = 0.75F;
		float var5 = var4 / 2.0F;
		GL11.glPushMatrix();
		this.renderBlocksInstance.setCustomBlockBounds(0.0D, (double)(0.5F - var5 + 0.0625F), (double)(0.5F - var5 + 0.0625F), (double)(var3 * 0.5F), (double)(0.5F + var5 - 0.0625F), (double)(0.5F + var5 - 0.0625F));
		this.renderBlocksInstance.setOverrideBlockTexture(185);
		this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
		this.renderBlocksInstance.clearOverrideBlockTexture();
		this.renderBlocksInstance.resetCustomBlockBounds();
		GL11.glPopMatrix();
		this.renderBlocksInstance.setOverrideBlockTexture(214);
		GL11.glPushMatrix();
		this.renderBlocksInstance.setCustomBlockBounds(0.0D, (double)(0.5F - var5), (double)(0.5F - var5), (double)(var3 + 1.0E-4F), (double)(var3 + 0.5F - var5), (double)(0.5F + var5));
		this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		this.renderBlocksInstance.setCustomBlockBounds(0.0D, (double)(0.5F + var5 - var3), (double)(0.5F - var5), (double)(var3 + 1.0E-4F), (double)(0.5F + var5), (double)(0.5F + var5));
		this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		this.renderBlocksInstance.setCustomBlockBounds(0.0D, (double)(0.5F - var5), (double)(0.5F - var5), (double)var3, (double)(0.5F + var5), (double)(var3 + 0.5F - var5));
		this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		this.renderBlocksInstance.setCustomBlockBounds(0.0D, (double)(0.5F - var5), (double)(0.5F + var5 - var3), (double)var3, (double)(0.5F + var5), (double)(0.5F + var5));
		this.renderBlocksInstance.renderBlockAsItem(var2, 0, 1.0F);
		GL11.glPopMatrix();
		this.renderBlocksInstance.resetCustomBlockBounds();
		this.renderBlocksInstance.clearOverrideBlockTexture();
		GL11.glPopMatrix();
	}

	private void func_82402_b(EntityItemFrame par1EntityItemFrame) {
		ItemStack var2 = par1EntityItemFrame.getDisplayedItem();

		if (var2 != null) {
			EntityItem var3 = new EntityItem(par1EntityItemFrame.worldObj, 0.0D, 0.0D, 0.0D, var2);
			var3.item().stackSize = 1;
			var3.hoverStart = 0.0F;
			// Spout Start
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderManager.renderEngine.getTexture("/terrain.png"));
			// Spout End
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.453125F * (float)Direction.offsetX[par1EntityItemFrame.hangingDirection], -0.18F, -0.453125F * (float)Direction.offsetZ[par1EntityItemFrame.hangingDirection]);
			GL11.glRotatef(180.0F + par1EntityItemFrame.rotationYaw, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef((float)(-90 * par1EntityItemFrame.getRotation()), 0.0F, 0.0F, 1.0F);

			switch (par1EntityItemFrame.getRotation()) {
				case 1:
					GL11.glTranslatef(-0.16F, -0.16F, 0.0F);
					break;

				case 2:
					GL11.glTranslatef(0.0F, -0.32F, 0.0F);
					break;

				case 3:
					GL11.glTranslatef(0.16F, -0.16F, 0.0F);
			}

			if (var3.item().getItem() == Item.map) {
				this.renderManager.renderEngine.bindTexture(this.renderManager.renderEngine.getTexture("/misc/mapbg.png"));
				Tessellator var4 = Tessellator.instance;
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
				GL11.glScalef(0.00390625F, 0.00390625F, 0.00390625F);
				GL11.glTranslatef(-65.0F, -107.0F, -3.0F);
				GL11.glNormal3f(0.0F, 0.0F, -1.0F);
				var4.startDrawingQuads();
				byte var5 = 7;
				var4.addVertexWithUV((double)(0 - var5), (double)(128 + var5), 0.0D, 0.0D, 1.0D);
				var4.addVertexWithUV((double)(128 + var5), (double)(128 + var5), 0.0D, 1.0D, 1.0D);
				var4.addVertexWithUV((double)(128 + var5), (double)(0 - var5), 0.0D, 1.0D, 0.0D);
				var4.addVertexWithUV((double)(0 - var5), (double)(0 - var5), 0.0D, 0.0D, 0.0D);
				var4.draw();
				MapData var6 = Item.map.getMapData(var3.item(), par1EntityItemFrame.worldObj);

				if (var6 != null) {
					this.renderManager.itemRenderer.mapItemRenderer.renderMap((EntityPlayer)null, this.renderManager.renderEngine, var6);
				}
			} else {
				if (var3.item().getItem() == Item.compass) {
					// Spout Start
					double var8 = TextureCompassFX.field_82391_c.field_76868_i;
					double var10 = TextureCompassFX.field_82391_c.field_76866_j;
					TextureCompassFX.field_82391_c.field_76868_i = 0.0D;
					TextureCompassFX.field_82391_c.field_76866_j = 0.0D;
					TextureCompassFX.func_82390_a(par1EntityItemFrame.posX, par1EntityItemFrame.posZ, (double)MathHelper.wrapAngleTo180_float((float)(180 + par1EntityItemFrame.hangingDirection * 90)), false, true);
					TextureCompassFX.field_82391_c.field_76868_i = var8;
					TextureCompassFX.field_82391_c.field_76866_j = var10;
					this.renderManager.renderEngine.updateDynamicTexture(TextureCompassFX.field_82391_c, -1);
					// Spout End
				}

				float var9 = this.renderManager.playerViewY;
				this.renderManager.playerViewY = 180.0F;
				RenderItem.field_82407_g = true;
				RenderManager.instance.renderEntityWithPosYaw(var3, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
				RenderItem.field_82407_g = false;
				this.renderManager.playerViewY = var9;
				GL11.glEnable(GL11.GL_LIGHTING);

				if (var3.item().getItem() == Item.compass) {
					TextureCompassFX.field_82391_c.onTick();
					this.renderManager.renderEngine.updateDynamicTexture(TextureCompassFX.field_82391_c, -1);
				}
			}

			GL11.glPopMatrix();
		}
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.func_82404_a((EntityItemFrame)par1Entity, par2, par4, par6, par8, par9);
	}
}
