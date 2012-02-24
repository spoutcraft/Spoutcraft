package net.minecraft.src;

import net.minecraft.src.Block;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Packet130UpdateSign;
import net.minecraft.src.TileEntityRenderer;
import net.minecraft.src.TileEntitySign;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiEditSign extends GuiScreen {

	protected String screenTitle = "Edit sign message:";
	private TileEntitySign entitySign;
	private int updateCounter;
	private int editLine = 0;
	private int editColumn = 0; //Spoutcraft
	private static final String allowedCharacters = ChatAllowedCharacters.allowedCharacters;


	public GuiEditSign(TileEntitySign tileentitysign) {
		this.entitySign = tileentitysign;
	}

	public void initGui() {
		controlList.clear();
		Keyboard.enableRepeatEvents(true);
		controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120, "Done"));
	}

	public void onGuiClosed() {
		//Spout start
		entitySign.lineBeingEdited = -1; 
		entitySign.columnBeingEdited = -1;
		entitySign.recalculateText();
		//Colorize text
		for (int i = 0; i < entitySign.signText.length; i++) {
			if (entitySign.signText[i] != null)
				entitySign.signText[i] = entitySign.signText[i].replaceAll("(&([a-fA-F0-9]))", "\u00A7$2");
		}
		//Spout end
		Keyboard.enableRepeatEvents(false);
		if(this.mc.theWorld.multiplayerWorld) {
			this.mc.getSendQueue().addToSendQueue(new Packet130UpdateSign(this.entitySign.xCoord, this.entitySign.yCoord, this.entitySign.zCoord, this.entitySign.signText));
		}
	}

	public void updateScreen() {
		++this.updateCounter;
	}

	protected void actionPerformed(GuiButton guibutton) {
		if(!guibutton.enabled) {
			return;
		}
		if(guibutton.id == 0) {
			this.entitySign.onInventoryChanged();
			this.mc.displayGuiScreen((GuiScreen)null);
		}
	}

	//Spoutcraft - rewritten method
	//Spoutcraft Start
	protected void keyTyped(char var1, int var2) {
		if(var2 == 200) { //up
			this.editLine = this.editLine - 1 & 3;
			editColumn = entitySign.signText[editLine].length();
		}

		if(var2 == 208 || var2 == 28) { //down
			this.editLine = this.editLine + 1 & 3;
			editColumn = entitySign.signText[editLine].length();
		}
		if(var2 == 205) { //right
			editColumn++;
			if(editColumn > entitySign.signText[editLine].length()){
				editColumn--;
		}
		}
		if(var2 == 203) {//left
			editColumn--;
			if(editColumn < 0){
				editColumn = 0;
			}
		}
		if(var2 == 14 && this.entitySign.signText[this.editLine].length() > 0) { //backsp
			String line = entitySign.signText[editLine];
			int endColumnStart = Math.min(editColumn,  line.length());
			String before = "";
			if (endColumnStart > 0) {
				before = line.substring(0, endColumnStart);
			}
			String after = "";
			if(line.length() - editColumn > 0)
			{
				after = line.substring(editColumn, line.length());
			}
			if(before.length() > 0){
				before = before.substring(0, before.length()-1);
				line = before + after;
				entitySign.signText[editLine] = line;
				endColumnStart--;
				editColumn = endColumnStart;
				if (editColumn < 0)	{
					editColumn = 0;
				}
			}
		}
		if(allowedCharacters.indexOf(var1) >= 0 && this.entitySign.signText[this.editLine].length() < 15) { //enter
			String line = entitySign.signText[editLine];
			
			//prevent out of bounds on the substring call
			int endColumnStart = Math.min(editColumn,  line.length());
			String before = "";
			if (endColumnStart > 0) {
				before = line.substring(0, endColumnStart);
			}
			String after = "";
			if(line.length() - endColumnStart > 0) {
				after = line.substring(endColumnStart, line.length());
			}
			before += var1;
			line = before + after;
			entitySign.signText[editLine] = line;
			endColumnStart++;
			editColumn = endColumnStart;
		}
		if(var2 == 211) //del 
		{
			String line = entitySign.signText[editLine];
			String before = line.substring(0, editColumn);
			String after = "";
			if(line.length() - editColumn > 0) {
				after = line.substring(editColumn, line.length());
			}
			if(after.length() > 0){
				after = after.substring(1, after.length());
				line = before + after;
				entitySign.signText[editLine] = line;
			}
		}
		entitySign.recalculateText();
	}
	//Spoutcraft End

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 40, 16777215);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(this.width / 2), 0.0F, 50.0F);
		float var4 = 93.75F;
		GL11.glScalef(-var4, -var4, -var4);
		GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
		Block var5 = this.entitySign.getBlockType();
		if(var5 == Block.signPost) {
			float var6 = (float)(this.entitySign.getBlockMetadata() * 360) / 16.0F;
			GL11.glRotatef(var6, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
		} else {
			int var8 = this.entitySign.getBlockMetadata();
			float var7 = 0.0F;
			if(var8 == 2) {
				var7 = 180.0F;
			}

			if(var8 == 4) {
				var7 = 90.0F;
			}

			if(var8 == 5) {
				var7 = -90.0F;
			}

			GL11.glRotatef(var7, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
		}

		//Spout start
		//if(this.updateCounter / 6 % 2 == 0) {
			this.entitySign.lineBeingEdited = this.editLine;
			entitySign.columnBeingEdited = editColumn;
		//}
		
		if (org.spoutcraft.client.config.ConfigReader.showChatColors) {
			drawString(fontRenderer, ChatColor.BLACK + "&0 - Black", width - 90, 10, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.DARK_BLUE + "&1 - Dark Blue", width - 90, 20, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.DARK_GREEN + "&2 - Dark Green", width - 90, 30, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.DARK_AQUA + "&3 - Dark Aqua", width - 90, 40, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.DARK_RED + "&4 - Dark Red", width - 90, 50, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.DARK_PURPLE + "&5 - Dark Purple", width - 90, 60, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.GOLD + "&6 - Gold", width - 90, 70, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.GRAY + "&7 - Grey", width - 90, 80, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.DARK_GRAY + "&8 - Dark Grey", width - 90, 90, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.BLUE + "&9 - Blue", width - 90, 100, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.GREEN + "&a - Green", width - 90, 110, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.AQUA + "&b - Aqua", width - 90, 120, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.RED + "&c - Red", width - 90, 130, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.LIGHT_PURPLE + "&d - Light Purple", width - 90, 140, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.YELLOW + "&e - Yellow", width - 90, 150, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.WHITE + "&f - White", width - 90, 160, 0xFFFFFFF);
		}
		//Spout end

		TileEntityRenderer.instance.renderTileEntityAt(this.entitySign, -0.5D, -0.75D, -0.5D, 0.0F);
		//Spout start
		this.entitySign.lineBeingEdited = -1; 
		entitySign.columnBeingEdited = -1;
		//Spout end
		GL11.glPopMatrix();
		super.drawScreen(var1, var2, var3);
	}

}
