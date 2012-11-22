package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import org.lwjgl.input.Keyboard;

public class GuiMultiplayer extends GuiScreen {

	/** Number of outstanding ThreadPollServers threads */
	private static int threadsPending = 0;

	/** Lock object for use with synchronized() */
	private static Object lock = new Object();

	/**
	 * A reference to the screen object that created this. Used for navigating between screens.
	 */
	private GuiScreen parentScreen;

	/** Slot container for the server list */
	private GuiSlotServer serverSlotContainer;
	private ServerList internetServerList;

	/** Index of the currently selected server */
	private int selectedServer = -1;

	/** The 'Edit' button */
	private GuiButton buttonEdit;

	/** The 'Join Server' button */
	private GuiButton buttonSelect;

	/** The 'Delete' button */
	private GuiButton buttonDelete;

	/** The 'Delete' button was clicked */
	private boolean deleteClicked = false;

	/** The 'Add server' button was clicked */
	private boolean addClicked = false;

	/** The 'Edit' button was clicked */
	private boolean editClicked = false;

	/** The 'Direct Connect' button was clicked */
	private boolean directClicked = false;

	/** This GUI's lag tooltip text or null if no lag icon is being hovered. */
	private String lagTooltip = null;

	/** Instance of ServerData. */
	private ServerData theServerData = null;
	private LanServerList localNetworkServerList;
	private ThreadLanServerFind localServerFindThread;
	private int field_74039_z;
	private boolean field_74024_A;
	private List listofLanServers = Collections.emptyList();

	public GuiMultiplayer(GuiScreen par1GuiScreen) {
		this.parentScreen = par1GuiScreen;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();

		if (!this.field_74024_A) {
			this.field_74024_A = true;
			this.internetServerList = new ServerList(this.mc);
			this.internetServerList.loadServerList();
			this.localNetworkServerList = new LanServerList();

			try {
				this.localServerFindThread = new ThreadLanServerFind(this.localNetworkServerList);
				this.localServerFindThread.start();
			} catch (Exception var2) {
				System.out.println("Unable to start LAN server detection: " + var2.getMessage());
			}

			this.serverSlotContainer = new GuiSlotServer(this);
		} else {
			this.serverSlotContainer.func_77207_a(this.width, this.height, 32, this.height - 64);
		}

		this.initGuiControls();
	}

	/**
	 * Populate the GuiScreen controlList
	 */
	public void initGuiControls() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.controlList.add(this.buttonEdit = new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, var1.translateKey("selectServer.edit")));
		this.controlList.add(this.buttonDelete = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, var1.translateKey("selectServer.delete")));
		this.controlList.add(this.buttonSelect = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, var1.translateKey("selectServer.select")));
		this.controlList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, var1.translateKey("selectServer.direct")));
		this.controlList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, var1.translateKey("selectServer.add")));
		this.controlList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, var1.translateKey("selectServer.refresh")));
		this.controlList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, var1.translateKey("gui.cancel")));
		boolean var2 = this.selectedServer >= 0 && this.selectedServer < this.serverSlotContainer.getSize();
		this.buttonSelect.enabled = var2;
		this.buttonEdit.enabled = var2;
		this.buttonDelete.enabled = var2;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		super.updateScreen();
		++this.field_74039_z;

		if (this.localNetworkServerList.getWasUpdated()) {
			this.listofLanServers = this.localNetworkServerList.getLanServers();
			this.localNetworkServerList.setWasNotUpdated();
		}
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);

		if (this.localServerFindThread != null) {
			this.localServerFindThread.interrupt();
			this.localServerFindThread = null;
		}
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id == 2) {
				String var2 = this.internetServerList.getServerData(this.selectedServer).serverName;

				if (var2 != null) {
					this.deleteClicked = true;
					StringTranslate var3 = StringTranslate.getInstance();
					String var4 = var3.translateKey("selectServer.deleteQuestion");
					String var5 = "\'" + var2 + "\' " + var3.translateKey("selectServer.deleteWarning");
					String var6 = var3.translateKey("selectServer.deleteButton");
					String var7 = var3.translateKey("gui.cancel");
					GuiYesNo var8 = new GuiYesNo(this, var4, var5, var6, var7, this.selectedServer);
					this.mc.displayGuiScreen(var8);
				}
			} else if (par1GuiButton.id == 1) {
				this.joinServer(this.selectedServer);
			} else if (par1GuiButton.id == 4) {
				this.directClicked = true;
				this.mc.displayGuiScreen(new GuiScreenServerList(this, this.theServerData = new ServerData(StatCollector.translateToLocal("selectServer.defaultName"), "")));
			} else if (par1GuiButton.id == 3) {
				this.addClicked = true;
				this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.theServerData = new ServerData(StatCollector.translateToLocal("selectServer.defaultName"), "")));
			} else if (par1GuiButton.id == 7) {
				this.editClicked = true;
				ServerData var9 = this.internetServerList.getServerData(this.selectedServer);
				this.theServerData = new ServerData(var9.serverName, var9.serverIP);
				this.theServerData.setHideAddress(var9.isHidingAddress());
				this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.theServerData));
			} else if (par1GuiButton.id == 0) {
				this.mc.displayGuiScreen(this.parentScreen);
			} else if (par1GuiButton.id == 8) {
				this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
			} else {
				this.serverSlotContainer.actionPerformed(par1GuiButton);
			}
		}
	}

	public void confirmClicked(boolean par1, int par2) {
		if (this.deleteClicked) {
			this.deleteClicked = false;

			if (par1) {
				this.internetServerList.removeServerData(par2);
				this.internetServerList.saveServerList();
				this.selectedServer = -1;
			}

			this.mc.displayGuiScreen(this);
		} else if (this.directClicked) {
			this.directClicked = false;

			if (par1) {
				this.connectToServer(this.theServerData);
			} else {
				this.mc.displayGuiScreen(this);
			}
		} else if (this.addClicked) {
			this.addClicked = false;

			if (par1) {
				this.internetServerList.addServerData(this.theServerData);
				this.internetServerList.saveServerList();
				this.selectedServer = -1;
			}

			this.mc.displayGuiScreen(this);
		} else if (this.editClicked) {
			this.editClicked = false;

			if (par1) {
				ServerData var3 = this.internetServerList.getServerData(this.selectedServer);
				var3.serverName = this.theServerData.serverName;
				var3.serverIP = this.theServerData.serverIP;
				var3.setHideAddress(this.theServerData.isHidingAddress());
				this.internetServerList.saveServerList();
			}

			this.mc.displayGuiScreen(this);
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		int var3 = this.selectedServer;

		if (par2 == 59) {
			this.mc.gameSettings.hideServerAddress = !this.mc.gameSettings.hideServerAddress;
			this.mc.gameSettings.saveOptions();
		} else {
			if (isShiftKeyDown() && par2 == 200) {
				if (var3 > 0 && var3 < this.internetServerList.countServers()) {
					this.internetServerList.swapServers(var3, var3 - 1);
					--this.selectedServer;

					if (var3 < this.internetServerList.countServers() - 1) {
						this.serverSlotContainer.func_77208_b(-this.serverSlotContainer.slotHeight);
					}
				}
			} else if (isShiftKeyDown() && par2 == 208) {
				if (var3 < this.internetServerList.countServers() - 1) {
					this.internetServerList.swapServers(var3, var3 + 1);
					++this.selectedServer;

					if (var3 > 0) {
						this.serverSlotContainer.func_77208_b(this.serverSlotContainer.slotHeight);
					}
				}
			} else if (par1 == 13) {
				this.actionPerformed((GuiButton) this.controlList.get(2));
			} else {
				super.keyTyped(par1, par2);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.lagTooltip = null;
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.serverSlotContainer.drawScreen(par1, par2, par3);
		this.drawCenteredString(this.fontRenderer, var4.translateKey("multiplayer.title"), this.width / 2, 20, 16777215);
		super.drawScreen(par1, par2, par3);

		if (this.lagTooltip != null) {
			this.func_74007_a(this.lagTooltip, par1, par2);
		}
	}

	/**
	 * Join server by slot index
	 */
	private void joinServer(int par1) {
		if (par1 < this.internetServerList.countServers()) {
			this.connectToServer(this.internetServerList.getServerData(par1));
		} else {
			par1 -= this.internetServerList.countServers();

			if (par1 < this.listofLanServers.size()) {
				LanServer var2 = (LanServer) this.listofLanServers.get(par1);
				this.connectToServer(new ServerData(var2.getServerMotd(), var2.getServerIpPort()));
			}
		}
	}

	private void connectToServer(ServerData par1ServerData) {
		this.mc.displayGuiScreen(new GuiConnecting(this.mc, par1ServerData));
	}

	private static void func_74017_b(ServerData par1ServerData) throws IOException {
		ServerAddress var1 = ServerAddress.func_78860_a(par1ServerData.serverIP);
		Socket var2 = null;
		DataInputStream var3 = null;
		DataOutputStream var4 = null;

		try {
			var2 = new Socket();
			var2.setSoTimeout(3000);
			var2.setTcpNoDelay(true);
			var2.setTrafficClass(18);
			var2.connect(new InetSocketAddress(var1.getIP(), var1.getPort()), 3000);
			var3 = new DataInputStream(var2.getInputStream());
			var4 = new DataOutputStream(var2.getOutputStream());
			var4.write(254);
			var4.write(1);

			if (var3.read() != 255) {
				throw new IOException("Bad message");
			}

			String var5 = Packet.readString(var3, 256);
			char[] var6 = var5.toCharArray();

			for (int var7 = 0; var7 < var6.length; ++var7) {
				if (var6[var7] != 167 && var6[var7] != 0 && ChatAllowedCharacters.allowedCharacters.indexOf(var6[var7]) < 0) {
					var6[var7] = 63;
				}
			}

			var5 = new String(var6);
			int var8;
			int var9;
			String[] var26;

			if (var5.startsWith("\u00a7") && var5.length() > 1) {
				var26 = var5.substring(1).split("\u0000");

				if (MathHelper.func_82715_a(var26[0], 0) == 1) {
					par1ServerData.serverMOTD = var26[3];
					par1ServerData.field_82821_f = MathHelper.func_82715_a(var26[1], par1ServerData.field_82821_f);
					par1ServerData.gameVersion = var26[2];
					var8 = MathHelper.func_82715_a(var26[4], 0);
					var9 = MathHelper.func_82715_a(var26[5], 0);

					if (var8 >= 0 && var9 >= 0) {
						par1ServerData.populationInfo = "\u00a77" + var8 + "\u00a78/\u00a77" + var9;
					} else {
						par1ServerData.populationInfo = "\u00a78???";
					}
				} else {
					par1ServerData.gameVersion = "???";
					par1ServerData.serverMOTD = "\u00a78???";
					par1ServerData.field_82821_f = 50;
					par1ServerData.populationInfo = "\u00a78???";
				}
			} else {
				var26 = var5.split("\u00a7");
				var5 = var26[0];
				var8 = -1;
				var9 = -1;

				try {
					var8 = Integer.parseInt(var26[1]);
					var9 = Integer.parseInt(var26[2]);
				} catch (Exception var24) {
					;
				}

				par1ServerData.serverMOTD = "\u00a77" + var5;

				if (var8 >= 0 && var9 > 0) {
					par1ServerData.populationInfo = "\u00a77" + var8 + "\u00a78/\u00a77" + var9;
				} else {
					par1ServerData.populationInfo = "\u00a78???";
				}

				par1ServerData.gameVersion = "1.3";
				par1ServerData.field_82821_f = 48;
			}
		} finally {
			try {
				if (var3 != null) {
					var3.close();
				}
			} catch (Throwable var23) {
				;
			}

			try {
				if (var4 != null) {
					var4.close();
				}
			} catch (Throwable var22) {
				;
			}

			try {
				if (var2 != null) {
					var2.close();
				}
			} catch (Throwable var21) {
				;
			}
		}
	}

	protected void func_74007_a(String par1Str, int par2, int par3) {
		if (par1Str != null) {
			int var4 = par2 + 12;
			int var5 = par3 - 12;
			int var6 = this.fontRenderer.getStringWidth(par1Str);
			this.drawGradientRect(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + 8 + 3, -1073741824, -1073741824);
			this.fontRenderer.drawStringWithShadow(par1Str, var4, var5, -1);
		}
	}

	static ServerList getInternetServerList(GuiMultiplayer par0GuiMultiplayer) {
		return par0GuiMultiplayer.internetServerList;
	}

	static List getListOfLanServers(GuiMultiplayer par0GuiMultiplayer) {
		return par0GuiMultiplayer.listofLanServers;
	}

	static int getSelectedServer(GuiMultiplayer par0GuiMultiplayer) {
		return par0GuiMultiplayer.selectedServer;
	}

	static int getAndSetSelectedServer(GuiMultiplayer par0GuiMultiplayer, int par1) {
		return par0GuiMultiplayer.selectedServer = par1;
	}

	/**
	 * Return buttonSelect GuiButton
	 */
	static GuiButton getButtonSelect(GuiMultiplayer par0GuiMultiplayer) {
		return par0GuiMultiplayer.buttonSelect;
	}

	/**
	 * Return buttonEdit GuiButton
	 */
	static GuiButton getButtonEdit(GuiMultiplayer par0GuiMultiplayer) {
		return par0GuiMultiplayer.buttonEdit;
	}

	/**
	 * Return buttonDelete GuiButton
	 */
	static GuiButton getButtonDelete(GuiMultiplayer par0GuiMultiplayer) {
		return par0GuiMultiplayer.buttonDelete;
	}

	static void func_74008_b(GuiMultiplayer par0GuiMultiplayer, int par1) {
		par0GuiMultiplayer.joinServer(par1);
	}

	static int func_74010_g(GuiMultiplayer par0GuiMultiplayer) {
		return par0GuiMultiplayer.field_74039_z;
	}

	static Object func_74011_h() {
		return lock;
	}

	static int func_74012_i() {
		return threadsPending;
	}

	static int func_74021_j() {
		return threadsPending++;
	}

	static void func_82291_a(ServerData par0ServerData) throws IOException {
		func_74017_b(par0ServerData);
	}

	static int func_74018_k() {
		return threadsPending--;
	}

	static String func_74009_a(GuiMultiplayer par0GuiMultiplayer, String par1Str) {
		return par0GuiMultiplayer.lagTooltip = par1Str;
	}
}
