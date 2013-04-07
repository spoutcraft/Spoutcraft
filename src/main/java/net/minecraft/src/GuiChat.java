package net.minecraft.src;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

// Spout Start
import org.bukkit.ChatColor;
import org.spoutcraft.client.config.Configuration;
// Spout End

public class GuiChat extends GuiScreen {
	private String field_73898_b = "";

	/**
	 * keeps position of which chat message you will select when you press up, (does not increase for duplicated messages
	 * sent immediately after each other)
	 */
	private int sentHistoryCursor = -1;
	private boolean field_73897_d = false;
	private boolean field_73905_m = false;
	private int field_73903_n = 0;
	private List field_73904_o = new ArrayList();

	/** used to pass around the URI to various dialogues and to the host os */
	private URI clickedURI = null;

	/** Chat entry field */
	protected GuiTextField inputField;

	/**
	 * is the text that appears when you press the chat key and the input box appears pre-filled
	 */
	private String defaultInputFieldText = "";

	public GuiChat() {}

	public GuiChat(String par1Str) {
		this.defaultInputFieldText = par1Str;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
		this.inputField = new GuiTextField(this.fontRenderer, 4, this.height - 12, this.width - 4, 12);
		this.inputField.setMaxStringLength(100);
		this.inputField.setEnableBackgroundDrawing(false);
		this.inputField.setFocused(true);
		this.inputField.setText(this.defaultInputFieldText);
		this.inputField.setCanLoseFocus(false);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		this.mc.ingameGUI.getChatGUI().resetScroll();
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		this.inputField.updateCursorCounter();
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		this.field_73905_m = false;

		if (par2 == 15) {
			this.completePlayerName();
		} else {
			this.field_73897_d = false;
		}

		if (par2 == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
		} else if (par2 == 28) {
			String var3 = this.inputField.getText().trim();

			if (var3.length() > 0) {
				this.mc.ingameGUI.getChatGUI().addToSentMessages(var3);

				if (!this.mc.handleClientCommand(var3)) {
					this.mc.thePlayer.sendChatMessage(var3);
				}
			}

			this.mc.displayGuiScreen((GuiScreen)null);
		} else if (par2 == 200) {
			this.getSentHistory(-1);
		} else if (par2 == 208) {
			this.getSentHistory(1);
		} else if (par2 == 201) {
			this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().func_96127_i() - 1);
		} else if (par2 == 209) {
			this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().func_96127_i() + 1);
		} else {
			this.inputField.textboxKeyTyped(par1, par2);
		}
	}

	/**
	 * Handles mouse input.
	 */
	public void handleMouseInput() {
		super.handleMouseInput();
		int var1 = Mouse.getEventDWheel();

		if (var1 != 0) {
			if (var1 > 1) {
				var1 = 1;
			}

			if (var1 < -1) {
				var1 = -1;
			}

			if (!isShiftKeyDown()) {
				var1 *= 7;
			}

			this.mc.ingameGUI.getChatGUI().scroll(var1);
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		if (par3 == 0 && this.mc.gameSettings.chatLinks) {
			ChatClickData var4 = this.mc.ingameGUI.getChatGUI().func_73766_a(Mouse.getX(), Mouse.getY());

			if (var4 != null) {
				URI var5 = var4.getURI();

				if (var5 != null) {
					if (this.mc.gameSettings.chatLinksPrompt) {
						this.clickedURI = var5;
						this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, var4.getClickedUrl(), 0));
					} else {
						this.func_73896_a(var5);
					}

					return;
				}
			}
		}

		this.inputField.mouseClicked(par1, par2, par3);
		super.mouseClicked(par1, par2, par3);
	}

	public void confirmClicked(boolean par1, int par2) {
		if (par2 == 0) {
			if (par1) {
				this.func_73896_a(this.clickedURI);
			}

			this.clickedURI = null;
			this.mc.displayGuiScreen(this);
		}
	}

	private void func_73896_a(URI par1URI) {
		try {
			Class var2 = Class.forName("java.awt.Desktop");
			Object var3 = var2.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
			var2.getMethod("browse", new Class[] {URI.class}).invoke(var3, new Object[] {par1URI});
		} catch (Throwable var4) {
			var4.printStackTrace();
		}
	}

	/**
	 * Autocompletes player name
	 */
	public void completePlayerName() {
		String var3;

		if (this.field_73897_d) {
			this.inputField.deleteFromCursor(this.inputField.func_73798_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());

			if (this.field_73903_n >= this.field_73904_o.size()) {
				this.field_73903_n = 0;
			}
		} else {
			int var1 = this.inputField.func_73798_a(-1, this.inputField.getCursorPosition(), false);
			this.field_73904_o.clear();
			this.field_73903_n = 0;
			String var2 = this.inputField.getText().substring(var1).toLowerCase();
			var3 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
			this.func_73893_a(var3, var2);

			if (this.field_73904_o.isEmpty()) {
				return;
			}

			this.field_73897_d = true;
			this.inputField.deleteFromCursor(var1 - this.inputField.getCursorPosition());
		}

		if (this.field_73904_o.size() > 1) {
			StringBuilder var4 = new StringBuilder();

			for (Iterator var5 = this.field_73904_o.iterator(); var5.hasNext(); var4.append(var3)) {
				var3 = (String)var5.next();

				if (var4.length() > 0) {
					var4.append(", ");
				}
			}

			this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(var4.toString(), 1);
		}

		this.inputField.writeText((String)this.field_73904_o.get(this.field_73903_n++));
	}

	private void func_73893_a(String par1Str, String par2Str) {
		if (par1Str.length() >= 1) {
			this.mc.thePlayer.sendQueue.addToSendQueue(new Packet203AutoComplete(par1Str));
			this.field_73905_m = true;
		}
	}

	/**
	 * input is relative and is applied directly to the sentHistoryCursor so -1 is the previous message, 1 is the next
	 * message from the current cursor position
	 */
	public void getSentHistory(int par1) {
		int var2 = this.sentHistoryCursor + par1;
		int var3 = this.mc.ingameGUI.getChatGUI().getSentMessages().size();

		if (var2 < 0) {
			var2 = 0;
		}

		if (var2 > var3) {
			var2 = var3;
		}

		if (var2 != this.sentHistoryCursor) {
			if (var2 == var3) {
				this.sentHistoryCursor = var3;
				this.inputField.setText(this.field_73898_b);
			} else {
				if (this.sentHistoryCursor == var3) {
					this.field_73898_b = this.inputField.getText();
				}

				this.inputField.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(var2));
				this.sentHistoryCursor = var2;
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
		this.inputField.drawTextBox();
		// Spout Start
		if (Configuration.isShowingChatColorAssist()) {
			for(int c = 0; c < 16; c++) {
				ChatColor value = ChatColor.getByCode(c);
				String name = value.name().toLowerCase();
				boolean lastUnderscore = true;
				String parsedName = "";
				for(int chr = 0; chr < name.length(); chr++) {
					char ch = name.charAt(chr);
					if(lastUnderscore) {
						ch = Character.toUpperCase(ch);
					}
					if(ch == '_') {
						lastUnderscore = true;
						ch = ' ';
					} else {
						lastUnderscore = false;
					}
					parsedName += ch;
				}
				char code = (char) ('0' + c);
				if(c >= 10) {
					code = (char) ('a' + c - 10);
				}
				fontRenderer.drawStringWithShadow("&" + code + " - " + value + parsedName, width - 90, 70 + c * 10, 0xffffffff);
			}
		}
		// Spout End
		super.drawScreen(par1, par2, par3);
	}

	public void func_73894_a(String[] par1ArrayOfStr) {
		if (this.field_73905_m) {
			this.field_73904_o.clear();
			String[] var2 = par1ArrayOfStr;
			int var3 = par1ArrayOfStr.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				String var5 = var2[var4];

				if (var5.length() > 0) {
					this.field_73904_o.add(var5);
				}
			}

			if (this.field_73904_o.size() > 0) {
				this.field_73897_d = true;
				this.completePlayerName();
			}
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}
}
