package net.minecraft.src;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
// Spout Start
import org.bukkit.ChatColor;
import org.spoutcraft.api.Spoutcraft;
// Spout End

public class GuiEditSign extends GuiScreen {

	/**
	 * This String is just a local copy of the characters allowed in text rendering of minecraft.
	 */
	private static final String allowedCharacters = ChatAllowedCharacters.allowedCharacters;

	/** The title string that is displayed in the top-center of the screen. */
	protected String screenTitle = "Edit sign message:";

	/** Reference to the sign object. */
	private TileEntitySign entitySign;

	/** Counts the number of screen updates. */
	private int updateCounter;

	/** The number of the line that is being edited. */
	private int editLine = 0;
	
	/** "Done" button for the GUI. */
	private GuiButton doneBtn;

	// Spout Start
	private int editColumn = 0;
	// Spout End

	public GuiEditSign(TileEntitySign par1TileEntitySign) {
		this.entitySign = par1TileEntitySign;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Done"));
		this.entitySign.setEditable(false);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		// Spout Start
		entitySign.lineBeingEdited = -1;
		entitySign.columnBeingEdited = -1;
		entitySign.recalculateText();
		// Colorize text
		if (sendAsUnicode()) {
			for (int i = 0; i < entitySign.signText.length; i++) {
				if (entitySign.signText[i] != null)
					entitySign.signText[i] = entitySign.signText[i].replaceAll("(&([a-fA-F0-9]))", "\u00A7$2");
			}
		}
		// Spout End
		Keyboard.enableRepeatEvents(false);
		NetClientHandler var1 = this.mc.getNetHandler();

		if (var1 != null) {
			var1.addToSendQueue(new Packet130UpdateSign(this.entitySign.xCoord, this.entitySign.yCoord, this.entitySign.zCoord, this.entitySign.signText));
		}

		this.entitySign.setEditable(true);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		++this.updateCounter;
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id == 0) {
				// Spout Start
				if (!Spoutcraft.hasPermission("spout.plugin.signcolors")) {
					for (int i = 0; i < entitySign.signText.length; i++) {
						entitySign.signText[i] = ChatColor.stripColor(entitySign.signText[i]);
					}
				}
				// Spout End
				this.entitySign.onInventoryChanged();
				this.mc.displayGuiScreen((GuiScreen)null);
			}
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	// Spout Start - Rewritten Method
	protected void keyTyped(char par1, int par2) {
		if (par2 == 200) { // Up
			this.editLine = this.editLine - 1 & 3;
			editColumn = entitySign.signText[editLine].length();
		}

		if (par2 == 208 || par2 == 28) { // Down
			this.editLine = this.editLine + 1 & 3;
			editColumn = entitySign.signText[editLine].length();
		}

		if (par2 == 205) { // Right
			editColumn++;
			if (editColumn > entitySign.signText[editLine].length()) {
				editColumn--;
			}
		}

		if (par2 == 203) {// Left
			editColumn--;
			if (editColumn < 0) {
				editColumn = 0;
			}
		}

		if (par2 == 14 && this.entitySign.signText[this.editLine].length() > 0) { // Backspace
			String line = entitySign.signText[editLine];
			int endColumnStart = Math.min(editColumn, line.length());
			String before = "";
			if (endColumnStart > 0) {
				before = line.substring(0, endColumnStart);
			}
			String after = "";
			if (line.length() - editColumn > 0) {
				after = line.substring(editColumn, line.length());
			}
			if (before.length() > 0) {
				before = before.substring(0, before.length() - 1);
				line = before + after;
				entitySign.signText[editLine] = line;
				endColumnStart--;
				editColumn = endColumnStart;
				if (editColumn < 0) {
					editColumn = 0;
				}
			}
		}

		if ((allowedCharacters.indexOf(par1) > -1 || par1 > 32) && this.entitySign.signText[this.editLine].length() < 15) { // Enter
			String line = entitySign.signText[editLine];

			// Prevent out of bounds on the substring call
			int endColumnStart = Math.min(editColumn, line.length());
			String before = "";
			if (endColumnStart > 0) {
				before = line.substring(0, endColumnStart);
			}
			String after = "";
			if (line.length() - endColumnStart > 0) {
				after = line.substring(endColumnStart, line.length());
			}
			before += par1;
			line = before + after;
			entitySign.signText[editLine] = line;
			endColumnStart++;
			editColumn = endColumnStart;
		}

		if (par2 == 211) { // Delete
			String line = entitySign.signText[editLine];
			String before = line.substring(0, editColumn);
			String after = "";
			if (line.length() - editColumn > 0) {
				after = line.substring(editColumn, line.length());
			}
			if (after.length() > 0) {
				after = after.substring(1, after.length());
				line = before + after;
				entitySign.signText[editLine] = line;
			}
		}		

		entitySign.recalculateText();
	}
	// Spout End

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 40, 16777215);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(this.width / 2), 0.0F, 50.0F);
		float var4 = 93.75F;
		GL11.glScalef(-var4, -var4, -var4);
		GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
		Block var5 = this.entitySign.getBlockType();

		if (var5 == Block.signPost) {
			float var6 = (float)(this.entitySign.getBlockMetadata() * 360) / 16.0F;
			GL11.glRotatef(var6, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
		} else {
			int var8 = this.entitySign.getBlockMetadata();
			float var7 = 0.0F;

			if (var8 == 2) {
				var7 = 180.0F;
			}

			if (var8 == 4) {
				var7 = 90.0F;
			}

			if (var8 == 5) {
				var7 = -90.0F;
			}

			GL11.glRotatef(var7, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
		}

		// Spout Start
		//if(this.updateCounter / 6 % 2 == 0) {
		this.entitySign.lineBeingEdited = this.editLine;
		entitySign.columnBeingEdited = editColumn;
		//}
		// Spout End

		TileEntityRenderer.instance.renderTileEntityAt(this.entitySign, -0.5D, -0.75D, -0.5D, 0.0F);
		this.entitySign.lineBeingEdited = -1;
		// Spout Start
		entitySign.columnBeingEdited = -1;
		// Spout End
		GL11.glPopMatrix();
		super.drawScreen(par1, par2, par3);
	}

	// Spout Start
	public boolean sendAsUnicode() {
		return !this.mc.theWorld.isRemote;
	}
	// Spout End
}
