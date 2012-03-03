package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

import org.bukkit.ChatColor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spoutcraft.client.config.ConfigReader;

public class GuiEditSign extends GuiScreen
{
	/** The title string that is displayed in the top-center of the screen. */
	protected String screenTitle;

	/** Reference to the sign object. */
	private TileEntitySign entitySign;

	/** Counts the number of screen updates. */
	private int updateCounter;

	/** The number of the line that is being edited. */
	private int editLine;

	/**
	 * This String is just a local copy of the characters allowed in text rendering of minecraft.
	 */
	private static final String allowedCharacters;

	//Spout start
	private int editColumn = 0;
	GuiButton unicode;
	//Spout end

	public GuiEditSign(TileEntitySign par1TileEntitySign)
	{
		screenTitle = "Edit sign message:";
		editLine = 0;
		entitySign = par1TileEntitySign;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui()
	{
		controlList.clear();
		Keyboard.enableRepeatEvents(true);
		controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120, "Done"));
		//Spout start
		controlList.add(unicode = new GuiButton(1, width / 2 - 100, height / 4 + 142, "Send As Unicode"));
		if (!ConfigReader.sendColorsAsUnicode) {
			unicode.displayString = "Send As Plain Text";
		}
		if (!this.mc.theWorld.isRemote) {
			unicode.drawButton = false;
			unicode.enabled = false;
		}
		//Spout end
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed()
	{
		//Spout start
		entitySign.lineBeingEdited = -1; 
		entitySign.columnBeingEdited = -1;
		entitySign.recalculateText();
		//Colorize text
		if (sendAsUnicode()) {
			for (int i = 0; i < entitySign.signText.length; i++) {
				if (entitySign.signText[i] != null)
					entitySign.signText[i] = entitySign.signText[i].replaceAll("(&([a-fA-F0-9]))", "\u00A7$2");
			}
		}
		//Spout end
		Keyboard.enableRepeatEvents(false);

		if (mc.theWorld.isRemote)
		{
			mc.getSendQueue().addToSendQueue(new Packet130UpdateSign(entitySign.xCoord, entitySign.yCoord, entitySign.zCoord, entitySign.signText));
		}
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		updateCounter++;
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (!par1GuiButton.enabled)
		{
			return;
		}

		if (par1GuiButton.id == 0)
		{
			entitySign.onInventoryChanged();
			mc.displayGuiScreen(null);
		}
		//Spout start
		else if (par1GuiButton.id == 1 && unicode.enabled) {
			ConfigReader.sendColorsAsUnicode = !ConfigReader.sendColorsAsUnicode;
			if (ConfigReader.sendColorsAsUnicode) {
				unicode.displayString = "Send As Unicode";
			}
			else {
				unicode.displayString = "Send As Plain Text";
			}
		}
		//Spout end
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	//Spout - rewritten method
	//Spout Start
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
	//Spout End

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int x, int y, float z)
	{
		drawDefaultBackground();
		drawCenteredString(fontRenderer, screenTitle, width / 2, 40, 0xffffff);

		//Spout Start
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

		GL11.glPushMatrix();
		GL11.glTranslatef(width / 2, 0.0F, 50F);
		float f = 93.75F;
		GL11.glScalef(-f, -f, -f);
		GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
		Block block = entitySign.getBlockType();

		if (block == Block.signPost)
		{
			float f1 = (float)(entitySign.getBlockMetadata() * 360) / 16F;
			GL11.glRotatef(f1, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
		}
		else
		{
			int i = entitySign.getBlockMetadata();
			float f2 = 0.0F;

			if (i == 2)
			{
				f2 = 180F;
			}

			if (i == 4)
			{
				f2 = 90F;
			}

			if (i == 5)
			{
				f2 = -90F;
			}

			GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
		}

		//Spout start
		//if(this.updateCounter / 6 % 2 == 0) {
		this.entitySign.lineBeingEdited = this.editLine;
		entitySign.columnBeingEdited = editColumn;
		//}
		//Spout end

		TileEntityRenderer.instance.renderTileEntityAt(entitySign, -0.5D, -0.75D, -0.5D, 0.0F);
		//Spout start
		this.entitySign.lineBeingEdited = -1; 
		entitySign.columnBeingEdited = -1;
		//Spout end
		GL11.glPopMatrix();
		super.drawScreen(x, y, z);
		//Spout start
		if (unicode.enabled && isInBoundingRect(unicode.xPosition, unicode.yPosition, unicode.height, unicode.width, x, y)) {
			this.drawTooltip("Some servers censor unicode characters. \nIf yours does, try sending as plain text.", x, y);
		}
		//Spout end
	}

	//Spout start
	public boolean sendAsUnicode() {
		return ConfigReader.sendColorsAsUnicode && this.mc.theWorld.isRemote;
	}
	//Spout end

	static
	{
		allowedCharacters = ChatAllowedCharacters.allowedCharacters;
	}
}
