// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiEditSign extends GuiScreen
{
	protected String screenTitle;
	private TileEntitySign entitySign;
	private int updateCounter;
	private int editLine;
	private int editColumn; //Spoutcraft
	private static final String allowedCharacters;

	static 
	{
		allowedCharacters = ChatAllowedCharacters.allowedCharacters;
	}

	public GuiEditSign(TileEntitySign tileentitysign)
	{
		screenTitle = "Edit sign message:";
		editLine = 0;
		editColumn = 0; //Spoutcraft
		entitySign = tileentitysign;
	}

	public void initGui()
	{
		controlList.clear();
		Keyboard.enableRepeatEvents(true);
		controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120, "Done"));
	}

	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
		if(mc.theWorld.multiplayerWorld)
		{
			mc.getSendQueue().addToSendQueue(new Packet130UpdateSign(entitySign.xCoord, entitySign.yCoord, entitySign.zCoord, entitySign.signText));
		}
	}

	public void updateScreen()
	{
		updateCounter++;
	}

	protected void actionPerformed(GuiButton guibutton)
	{
		if(!guibutton.enabled)
		{
			return;
		}
		if(guibutton.id == 0)
		{
			entitySign.onInventoryChanged();
			mc.displayGuiScreen(null);
		}
	}

	//Spoutcraft - rewritten method
	//Spoutcraft Start
	protected void keyTyped(char c, int i)
	{
		if(i == 200) //up
		{
			editLine = editLine - 1 & 3;
			editColumn = entitySign.signText[editLine].length();
		}
		if(i == 208 || i == 28) //down
		{
			editLine = editLine + 1 & 3;
			editColumn = entitySign.signText[editLine].length();
		}
		if(i == 205) //right
		{
			editColumn++;
			if(editColumn > entitySign.signText[editLine].length()){
				editColumn--;
			}
		}
		if(i == 203) //left
		{
			editColumn--;
			if(editColumn < 0){
				editColumn = 0;
			}
		}
		if(i == 14 && entitySign.signText[editLine].length() > 0) //backsp
		{
			String line = entitySign.signText[editLine];
			String before = line.substring(0, editColumn);
			String after = "";
			if(line.length() - editColumn > 0)
			{
				after = line.substring(editColumn, line.length());
			}
			if(before.length() > 0){
				before = before.substring(0, before.length()-1);
				line = before + after;
				entitySign.signText[editLine] = line;
				editColumn--;
				if (editColumn<0)
				{
					editColumn = 0;
				}
			}
		}
		if(allowedCharacters.indexOf(c) >= 0 && entitySign.signText[editLine].length() < 15) //enter 
		{
			String line = entitySign.signText[editLine];
			String before = line.substring(0, editColumn);
			String after = "";
			if(line.length() - editColumn > 0)
			{
				after = line.substring(editColumn, line.length());
			}
			before += c;
			line = before + after;
			entitySign.signText[editLine] = line;
			editColumn++;
		}
		if(i == 211) //del 
		{
			String line = entitySign.signText[editLine];
			String before = line.substring(0, editColumn);
			String after = "";
			if(line.length() - editColumn > 0)
			{
				after = line.substring(editColumn, line.length());
			}
			if(after.length() > 0){
				after = after.substring(1, after.length());
				line = before + after;
				entitySign.signText[editLine] = line;
			}
		}
	}
	//Spoutcraft End

	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		drawCenteredString(fontRenderer, screenTitle, width / 2, 40, 0xffffff);
		GL11.glPushMatrix();
		GL11.glTranslatef(width / 2, 0.0F, 50F);
		float f1 = 93.75F;
		GL11.glScalef(-f1, -f1, -f1);
		GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
		Block block = entitySign.getBlockType();
		if(block == Block.signPost)
		{
			float f2 = (float)(entitySign.getBlockMetadata() * 360) / 16F;
			GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
		} else
		{
			int k = entitySign.getBlockMetadata();
			float f3 = 0.0F;
			if(k == 2)
			{
				f3 = 180F;
			}
			if(k == 4)
			{
				f3 = 90F;
			}
			if(k == 5)
			{
				f3 = -90F;
			}
			GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
		}
		if((updateCounter / 6) % 2 == 0)
		{
			entitySign.lineBeingEdited = editLine;
			entitySign.columnBeingEdited = editColumn; //Spoutcraft
		}
		TileEntityRenderer.instance.renderTileEntityAt(entitySign, -0.5D, -0.75D, -0.5D, 0.0F);
		entitySign.lineBeingEdited = -1;
		entitySign.columnBeingEdited = -1;
		GL11.glPopMatrix();
		super.drawScreen(i, j, f);
	}
}
