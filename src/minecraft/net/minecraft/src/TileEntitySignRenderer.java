package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.SignModel;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySign;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class TileEntitySignRenderer extends TileEntitySpecialRenderer {

	private SignModel signModel = new SignModel();


	public void renderTileEntitySignAt(TileEntitySign var1, double var2, double var4, double var6, float var8) {
		Block var9 = var1.getBlockType();
		GL11.glPushMatrix();
		float var10 = 0.6666667F;
		float var12;
		if(var9 == Block.signPost) {
			GL11.glTranslatef((float)var2 + 0.5F, (float)var4 + 0.75F * var10, (float)var6 + 0.5F);
			var12 = (float)(var1.getBlockMetadata() * 360) / 16.0F; //Spout (swapped from var11 to var12, var12 is the pitch)
			GL11.glRotatef(-var12, 0.0F, 1.0F, 0.0F); //Spout (swapped from var11 to var12)
			this.signModel.signStick.showModel = true;
		} else {
			int var16 = var1.getBlockMetadata();
			var12 = 0.0F;
			if(var16 == 2) {
				var12 = 180.0F;
			}

			if(var16 == 4) {
				var12 = 90.0F;
			}

			if(var16 == 5) {
				var12 = -90.0F;
			}

			GL11.glTranslatef((float)var2 + 0.5F, (float)var4 + 0.75F * var10, (float)var6 + 0.5F);
			GL11.glRotatef(-var12, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -0.3125F, -0.4375F);
			this.signModel.signStick.showModel = false;
		}

		this.bindTextureByName("/item/sign.png");
		GL11.glPushMatrix();
		GL11.glScalef(var10, -var10, -var10);
		this.signModel.renderSign();
		GL11.glPopMatrix();
		//Spout start
		if (var1.hasText()) {
		EntityLiving viewer = Minecraft.theMinecraft.renderViewEntity;
		if (viewer == null) {
			viewer = Minecraft.theMinecraft.thePlayer;
		}
		if (viewer != null && var1.getDistanceFrom(viewer.posX, viewer.posY, viewer.posZ) < 64) {
		FontRenderer var17 = this.getFontRenderer();
		var12 = 0.016666668F * var10;
		GL11.glTranslatef(0.0F, 0.5F * var10, 0.07F * var10);
		GL11.glScalef(var12, -var12, var12);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * var12);
		GL11.glDepthMask(false);
		byte var13 = 0;

		for(int var14 = 0; var14 < var1.signText.length; ++var14) {
			String var15 = var1.signText[var14];
			if(var14 == var1.lineBeingEdited) {
				//Spoutcraft Start
				String before = var15.substring(0, var1.columnBeingEdited);
				String after = "";
				if(var15.length() - var1.columnBeingEdited > 0)
				{
					after = var15.substring(var1.columnBeingEdited, var15.length());
				}
				var15 = before + "_" + after;
				//Spoutcraft End
				var17.drawString(var15, -var17.getStringWidth(var15) / 2, var14 * 10 - var1.signText.length * 5, var13);
			} else {
				var17.drawString(var15, -var17.getStringWidth(var15) / 2, var14 * 10 - var1.signText.length * 5, var13);
			}
		}

		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
		}
		//Spout end
		GL11.glPopMatrix();
	}

	// $FF: synthetic method
	// $FF: bridge method
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
		this.renderTileEntitySignAt((TileEntitySign)var1, var2, var4, var6, var8);
	}
}
