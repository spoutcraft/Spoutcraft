package net.minecraft.src;

import org.spoutcraft.client.gui.mainmenu.MainMenu; // Spout

import net.minecraft.client.Minecraft;

public class GuiConnecting extends GuiScreen {

	/** A reference to the NetClientHandler. */
	private NetClientHandler clientHandler;

	/** True if the connection attempt has been cancelled. */
	private boolean cancelled = false;

	public GuiConnecting(Minecraft par1Minecraft, ServerData par2ServerData) {
		this.mc = par1Minecraft;
		ServerAddress var3 = ServerAddress.func_78860_a(par2ServerData.serverIP);
		par1Minecraft.loadWorld((WorldClient)null);
		par1Minecraft.setServerData(par2ServerData);
		this.spawnNewServerThread(var3.getIP(), var3.getPort());
	}

	public GuiConnecting(Minecraft par1Minecraft, String par2Str, int par3) {
		this.mc = par1Minecraft;
		par1Minecraft.loadWorld((WorldClient)null);
		this.spawnNewServerThread(par2Str, par3);
	}

	private void spawnNewServerThread(String par1Str, int par2) {
		System.out.println("Connecting to " + par1Str + ", " + par2);
		(new ThreadConnectToServer(this, par1Str, par2)).start();
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		if (this.clientHandler != null) {
			this.clientHandler.processReadPackets();
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 0) {
			this.cancelled = true;

			if (this.clientHandler != null) {
				this.clientHandler.disconnect();
			}

			this.mc.displayGuiScreen(new MainMenu()); // Spout
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		StringTranslate var4 = StringTranslate.getInstance();

		if (this.clientHandler == null) {
			this.drawCenteredString(this.fontRenderer, var4.translateKey("connect.connecting"), this.width / 2, this.height / 2 - 50, 16777215);
			this.drawCenteredString(this.fontRenderer, "", this.width / 2, this.height / 2 - 10, 16777215);
		} else {
			this.drawCenteredString(this.fontRenderer, var4.translateKey("connect.authorizing"), this.width / 2, this.height / 2 - 50, 16777215);
			this.drawCenteredString(this.fontRenderer, this.clientHandler.field_72560_a, this.width / 2, this.height / 2 - 10, 16777215);
		}

		super.drawScreen(par1, par2, par3);
	}

	/**
	 * Sets the NetClientHandler.
	 */
	static NetClientHandler setNetClientHandler(GuiConnecting par0GuiConnecting, NetClientHandler par1NetClientHandler) {
		return par0GuiConnecting.clientHandler = par1NetClientHandler;
	}

	static Minecraft func_74256_a(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.mc;
	}

	static boolean isCancelled(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.cancelled;
	}

	static Minecraft func_74254_c(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.mc;
	}

	/**
	 * Gets the NetClientHandler.
	 */
	static NetClientHandler getNetClientHandler(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.clientHandler;
	}

	static Minecraft func_74249_e(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.mc;
	}

	static Minecraft func_74250_f(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.mc;
	}

	static Minecraft func_74251_g(GuiConnecting par0GuiConnecting) {
		return par0GuiConnecting.mc;
	}
}
