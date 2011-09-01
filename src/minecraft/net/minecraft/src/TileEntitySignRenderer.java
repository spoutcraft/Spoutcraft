// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class TileEntitySignRenderer extends TileEntitySpecialRenderer
{
	private SignModel signModel;

	public TileEntitySignRenderer()
	{
		signModel = new SignModel();
	}

	public void renderTileEntitySignAt(TileEntitySign tileentitysign, double d, double d1, double d2, 
			float f)
	{
		Block block = tileentitysign.getBlockType();
		GL11.glPushMatrix();
		float f1 = 0.6666667F;
		if(block == Block.signPost)
		{
			GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.75F * f1, (float)d2 + 0.5F);
			float f2 = (float)(tileentitysign.getBlockMetadata() * 360) / 16F;
			GL11.glRotatef(-f2, 0.0F, 1.0F, 0.0F);
			signModel.signStick.showModel = true;
		} else
		{
			int i = tileentitysign.getBlockMetadata();
			float f3 = 0.0F;
			if(i == 2)
			{
				f3 = 180F;
			}
			if(i == 4)
			{
				f3 = 90F;
			}
			if(i == 5)
			{
				f3 = -90F;
			}
			GL11.glTranslatef((float)d + 0.5F, (float)d1 + 0.75F * f1, (float)d2 + 0.5F);
			GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -0.3125F, -0.4375F);
			signModel.signStick.showModel = false;
		}
		bindTextureByName("/item/sign.png");
		GL11.glPushMatrix();
		GL11.glScalef(f1, -f1, -f1);
		signModel.renderSign();
		GL11.glPopMatrix();
		FontRenderer fontrenderer = getFontRenderer();
		float f4 = 0.01666667F * f1;
		GL11.glTranslatef(0.0F, 0.5F * f1, 0.07F * f1);
		GL11.glScalef(f4, -f4, f4);
		GL11.glNormal3f(0.0F, 0.0F, -1F * f4);
		GL11.glDepthMask(false);
		int j = 0;
		for(int k = 0; k < tileentitysign.signText.length; k++)
		{
			String s = tileentitysign.signText[k];
			if(k == tileentitysign.lineBeingEdited)
			{
				//Spoutcraft Start
				String before = s.substring(0, tileentitysign.columnBeingEdited);
				String after = "";
				if(s.length() - tileentitysign.columnBeingEdited > 0)
				{
					after = s.substring(tileentitysign.columnBeingEdited, s.length());
				}
				s = before + "_" + after;
				//Spoutcraft End
				fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, k * 10 - tileentitysign.signText.length * 5, j);
			} else
			{
				fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, k * 10 - tileentitysign.signText.length * 5, j);
			}
		}

		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, 
			float f)
	{
		renderTileEntitySignAt((TileEntitySign)tileentity, d, d1, d2, f);
	}
}
