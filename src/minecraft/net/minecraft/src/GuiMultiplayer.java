package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import net.minecraft.src.GuiButton;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.server.*;
import org.getspout.spout.io.CustomTextureManager;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class GuiMultiplayer extends GuiScreen {

	public static int pinglimit = 0;
	public static final String version = "1.8.1";
	private static Object synchronize = new Object();
	public final ServerListInfo serverInfo = new ServerListInfo();
	public String indexString = "";
	private final DateFormat dateFormatter = new SimpleDateFormat();
	protected GuiScreen parentScreen;
	protected String screenTitle = "Server Browser";
	private int selectedWorld;
	private GuiSlotServer worldSlotContainer;
	private String field_22098_o;
	private String field_22097_p;
	private GuiButton buttonSelect;
	private GuiButton buttonAdd;
	private GuiButton buttonNextCountry;
	private GuiButton buttonPrevCountry;
	private GuiButton buttonNextTenPage;
	private GuiButton buttonPrevTenPage;
	private GuiButton buttonNextPage;
	private GuiButton buttonPrevPage;
	private boolean first = true;
	private String tooltip = null;


	public GuiMultiplayer(GuiScreen var1) {	
		this.parentScreen = var1;
		tabs.add(new ServerTab("Featured", "http://servers.getspout.org/api.php?type=1&featured"));
		tabs.add(new ServerTab("Popular", "http://servers.getspout.org/api.php?type=1&popular"));
		tabs.add(new ServerTab("Country", "http://servers.getspout.org/api.php?type=1&all", true));
		tabs.add(new ServerTab("A-Z", "http://servers.getspout.org/api.php?type=1&az"));
		tabs.add(new ServerTab("Z-A", "http://servers.getspout.org/api.php?type=1&za"));
		tabs.add(new ServerTab("Random", "http://servers.getspout.org/api.php?type=1&random"));
	}

	public void initGui() {
		synchronized(serverInfo) {
			this.serverInfo.status = "Done";
		}
		StringTranslate var1 = StringTranslate.getInstance();
		this.screenTitle = var1.translateKey("Server Browser");
		this.field_22098_o = var1.translateKey("Unknown");
		this.field_22097_p = var1.translateKey("aaa");
		this.worldSlotContainer = new GuiSlotServer(this);
		this.worldSlotContainer.registerScrollButtons(this.controlList, 4, 5);
		this.initButtons();
		this.loadSaves();
	}

	public void loadSaves() {
		this.selectedWorld = -1;
		if(this.first) {
			this.first = false;
			this.getServer();
		}

	}

	public String getServerName(int var1) {
		synchronized(serverInfo) {
			return ((ServerSlot)this.serverInfo.serverList.get(var1)).name;
		}
	}

	public String getCountry(int var1) {
		synchronized(serverInfo) {
			return ((ServerSlot)this.serverInfo.serverList.get(var1)).country;
		}
	}

	public void initButtons() {
		this.controlList.add(this.buttonSelect = new GuiButton(1, this.width / 2 - 74, this.height - 52, 70, 20, "Join Server"));
		this.controlList.add(new GuiButton(2, this.width / 2 - 154, this.height - 28, 150, 20, "Favorites"));
		this.controlList.add(new GuiButton(3, this.width / 2 + 4, this.height - 52, 150, 20, "Refresh"));
		this.controlList.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20, "Main Menu"));
		this.controlList.add(this.buttonPrevCountry = new GuiButton(6, this.width / 2 - 154, this.height - 76, 25, 20, "-"));
		this.controlList.add(this.buttonNextCountry = new GuiButton(7, this.width / 2 + 130, this.height - 76, 25, 20, "+"));
		this.controlList.add(this.buttonPrevPage = new GuiButton(8, this.width / 2 - 94, this.height - 76, 25, 20, "<"));
		this.controlList.add(this.buttonNextPage = new GuiButton(9, this.width / 2 + 70, this.height - 76, 25, 20, ">"));
		this.controlList.add(this.buttonPrevTenPage = new GuiButton(10, this.width / 2 - 124, this.height - 76, 25, 20, "<<"));
		this.controlList.add(this.buttonNextTenPage = new GuiButton(11, this.width / 2 + 100, this.height - 76, 25, 20, ">>"));
		this.controlList.add(this.buttonAdd = new GuiButton(4, this.width / 2 - 154, this.height - 52, 70, 20, "Add Fav"));
		this.buttonSelect.enabled = false;
		this.buttonAdd.enabled = false;
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			synchronized(serverInfo) {
				if(var1.id == 2) {
					this.mc.displayGuiScreen(new GuiFavorites(this));
				} else if(var1.id == 1) {
					this.selectWorld(this.selectedWorld);
				} else if(var1.id == 3) {
					this.getServer();
				} else if(var1.id == 4) {
					ServerSlot slot = (ServerSlot)this.serverInfo.serverList.get(this.selectedWorld);
					SpoutClient.getInstance().getServerManager().getFavorites().addServer(slot.name, slot.ip, slot.port.equals("")?25565:Integer.valueOf(slot.port), slot.uniqueid);
					SpoutClient.getInstance().getServerManager().getFavorites().save();
					mc.displayGuiScreen(new GuiFavorites(this));
				} else if(var1.id == 0) {
					this.mc.displayGuiScreen(new GuiMainMenu());
				} else if(var1.id == 8) {
					if(serverInfo.page > 0) {
						serverInfo.page--;
						updateList();
					}
				} else if(var1.id == 9) {
					if(serverInfo.page < serverInfo.pages - 1) {
						serverInfo.page++;
						updateList();
					}
				} else if(var1.id == 10) {
					if(serverInfo.page > 0) {
						serverInfo.page-=10;
						if(serverInfo.page < 0) {
							serverInfo.page = 0;
						}
						updateList();
					}
				} else if(var1.id == 11) {
					if(serverInfo.page < serverInfo.pages - 1) {
						serverInfo.page+=10;
						if(serverInfo.page > serverInfo.pages - 1) {
							serverInfo.page = serverInfo.pages - 1;
						}
						updateList();
					}
				} else if(var1.id == 6) {
					if(serverInfo.activeCountry > 0) {
						serverInfo.activeCountry--;
						serverInfo.page = 0;
						updateList();
					}
				} else if(var1.id == 7) {
					if(serverInfo.activeCountry < serverInfo.countryMappings.size() - 1) {
						serverInfo.activeCountry++;
						serverInfo.page = 0;
						updateList();
					}
				} else {
					this.worldSlotContainer.actionPerformed(var1);
				}
			}

		}
	}

	public void selectWorld(int id) {
		synchronized(serverInfo) {
			this.mc.displayGuiScreen(new GuiConnecting(this.mc, ((ServerSlot)this.serverInfo.serverList.get(id)).ip, ((ServerSlot)this.serverInfo.serverList.get(id)).port == ""?25565:Integer.parseInt(((ServerSlot)this.serverInfo.serverList.get(id)).port)));
		}
	}
	
	public void elementInfo(int id) {
		synchronized(serverInfo) {
			ServerSlot info = (ServerSlot)this.serverInfo.serverList.get(id);
			this.mc.displayGuiScreen(new GuiServerInfo(info, this));
		}
	}

	public void updateList() {
		synchronized(serverInfo) {
			worldSlotContainer.amountScrolled = 0f;
			if (serverInfo.countries.size() == 0) {
				indexString = "Empty (0/0)";
				serverInfo.serverList = new ArrayList();
			} else {
				String country = serverInfo.countries.get(serverInfo.activeCountry);
				ArrayList fullList = serverInfo.countryMappings.get(country);
				if (tabs.get(current_tab).pages) { Collections.sort(fullList); }
				serverInfo.pages = (fullList.size() + 9) / 10;
				int last = Math.min(fullList.size(), (serverInfo.page + 1) * 10);
				int first = serverInfo.page * 10;
				serverInfo.serverList = fullList.subList(first, last);
				indexString = country + " (" + (serverInfo.page + 1) + "/" + serverInfo.pages + ")";
			}
			this.buttonNextPage.enabled = serverInfo.page < serverInfo.pages - 1;
			this.buttonPrevPage.enabled = serverInfo.page > 0;
			this.buttonNextTenPage.enabled = serverInfo.page < serverInfo.pages - 1;
			this.buttonPrevTenPage.enabled = serverInfo.page > 0;
			this.buttonPrevCountry.enabled = serverInfo.activeCountry > 0 && tabs.get(current_tab).pages;
			this.buttonNextCountry.enabled = serverInfo.activeCountry < serverInfo.countries.size() - 1 && tabs.get(current_tab).pages;
		}
	}

	public void deleteWorld(boolean var1, int var2) {}

	ArrayList<ServerTab> tabs = new ArrayList<ServerTab>();
	int current_tab = 0;
	
	public void drawScreen(int x, int y, float z) {
		synchronized(serverInfo) {
			this.tooltip = null;
			this.worldSlotContainer.drawScreen(x, y, z);
			this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 8, 16777215);
			renderTabs();
			//this.drawCenteredString(this.fontRenderer, "Spoutcraft Server Browser", this.width / 2, this.height - 86, 5263440);
			this.drawCenteredString(this.fontRenderer, indexString , this.width / 2, this.height - 71, 5263440);
			this.drawString(this.fontRenderer, "Displaying " + this.serverInfo.serverList.size() + " servers", 2, 20, 5263440);
			this.drawString(this.fontRenderer, "Status: " + this.serverInfo.status, 2, 10, 5263440);
			if(this.tooltip != null) {
				this.drawTooltip(this.tooltip, x, y);
			}
		}
		super.drawScreen(x, y, z);
	}
	
	public void renderTabs() {
		int offset = 0;
		double s = 0.5;
		Texture tabTexture = CustomTextureManager.getTextureFromJar("/res/tab.png");
		Texture tabSelectedTexture = CustomTextureManager.getTextureFromJar("/res/tab_s.png");
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND); 
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA); 
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		for (ServerTab tab : tabs) {
			if (tabTexture != null) {
				GL11.glPushMatrix();
				Texture cTexture = tabs.indexOf(tab) == current_tab ? tabSelectedTexture : tabTexture;
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, cTexture.getTextureID());
				GL11.glTranslatef(this.width / 2 + offset, 20, 0); // moves texture into place
				Tessellator tessellator = Tessellator.instance;
				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV(0.0D, 15, -90, 0.0D, 0.0D); // draw corners
				tessellator.addVertexWithUV(34, 15, -90, cTexture.getWidth(), 0.0D);
				tessellator.addVertexWithUV(34, 0.0D, -90, cTexture.getWidth(), cTexture.getHeight());
				tessellator.addVertexWithUV(0.0D, 0.0D, -90, 0.0D, cTexture.getHeight());
				tessellator.draw();
				GL11.glPopMatrix();
	
				GL11.glPushMatrix();
				GL11.glTranslatef((float) (this.width / 2 + 17 - this.fontRenderer.getStringWidth(tab.title) / 4 + offset), 24F, 0F);
				GL11.glScaled(s, s, s);
				this.drawString(this.fontRenderer, tab.title, 0, 0, 0xFFFFFF);
				GL11.glPopMatrix();
				offset += 35;
			}
		}
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
	
	protected void drawTooltip(String var1, int var2, int var3) {
		if(var1 != null) {
			int var4 = var2 + 12;
			int var5 = var3 - 12;
			int var6 = this.fontRenderer.getStringWidth(var1);
			this.drawGradientRect(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + 8 + 3, -1073741824, -1073741824);
			this.fontRenderer.drawStringWithShadow(var1, var4, var5, -1);
		}
	}
	
	private int parseIntWithDefault(String var1, int var2) {
		try {
			return Integer.parseInt(var1.trim());
		} catch (Exception var4) {
			return var2;
		}
	}
	
	@Override
	protected void mouseClicked(int var1, int var2, int var3) {
		super.mouseClicked(var1, var2, var3);
		if (var1 >= this.width / 2 && var1 <= this.width / 2 + tabs.size() * 35 - 2 && var2 >= 20 && var2 <= 33) {
			int var4 = (var1 - this.width / 2) / 35;
			current_tab = var4;
			getServer();
		}
		worldSlotContainer.onClick(var1, var2, var3);
	}
	
	private void func_35328_b(ServerSlot var1) throws IOException {
		String var2 = var1.ip + ":" + var1.port;
		String[] splitPacket = var2.split(":");
		if(var2.startsWith("[")) {
			int var4 = var2.indexOf("]");
			if(var4 > 0) {
				String var5 = var2.substring(1, var4);
				String var6 = var2.substring(var4 + 1).trim();
				if(var6.startsWith(":") && var6.length() > 0) {
					var6 = var6.substring(1);
					splitPacket = new String[]{var5, var6};
				} else {
					splitPacket = new String[]{var5};
				}
			}
		}

		if(splitPacket.length > 2) {
			splitPacket = new String[]{var2};
		}

		String var29 = splitPacket[0];
		int var30 = splitPacket.length > 1?this.parseIntWithDefault(splitPacket[1], 25565):25565;
		Socket var31 = null;
		DataInputStream var7 = null;
		DataOutputStream var8 = null;

		try {
			var31 = new Socket();
			var31.setSoTimeout(3000);
			var31.setTcpNoDelay(true);
			var31.setTrafficClass(18);
			var31.connect(new InetSocketAddress(var29, var30), 3000);
			var7 = new DataInputStream(var31.getInputStream());
			var8 = new DataOutputStream(var31.getOutputStream());
			var8.write(254);
			if(var7.read() != 255) {
				throw new IOException("Bad message");
			}
		
			String sPacket = Packet.readString(var7, 256);

			char[] cPacket = sPacket.toCharArray();

			int i;
			for(i = 0; i < cPacket.length; ++i) {
				if(cPacket[i] != 167 && ChatAllowedCharacters.allowedCharacters.indexOf(cPacket[i]) < 0) {
					cPacket[i] = 63;
				}
			}

			sPacket = new String(cPacket);
			splitPacket = sPacket.split("\u00a7");

			i = -1;
			int var12 = -1;

			try {
				i = Integer.parseInt(splitPacket[1]);
				var12 = Integer.parseInt(splitPacket[2]);
			} catch (Exception var27) { }

			String sMOTD = "???";
			if(splitPacket[0].length() > 34)
			{
				sMOTD = splitPacket[0].substring(0, 29) + " ...";
			}
			else sMOTD = splitPacket[0];
	
			var1.msg = "\u00a77" + sMOTD;
			if(i >= 0 && var12 > 0) {
				var1.status = "\u00a77" + i + "\u00a78/\u00a77" + var12;
				var1.players = i;
				var1.maxPlayers = var12;
			} else {
				var1.status = "\u00a78???";
				var1.players = 0;
				var1.maxPlayers = 0;
			}

		} finally {
			try {
				if(var7 != null) {
					var7.close();
				}
			} catch (Throwable var26) { }
			try {
				if(var8 != null) {
					var8.close();
				}
			} catch (Throwable var25) { }
			try {
				if(var31 != null) {
					var31.close();
				}
			} catch (Throwable var24) { }
		}
	}

	public void getServer() {
		ServerListThread serverList = new ServerListThread(this, tabs.get(current_tab));
		serverList.init();
	}

	public static List getSize(GuiMultiplayer var0) {
		synchronized(var0.serverInfo) {
			return var0.serverInfo.serverList;
		}
	}

	public static int onElementSelected(GuiMultiplayer var0, int var1) {
		return var0.selectedWorld = var1;
	}
	
	public static void onElementInfo(GuiMultiplayer var0, int var1) {
		var0.elementInfo(var1);
	}

	public static int getSelectedWorld(GuiMultiplayer var0) {
		return var0.selectedWorld;
	}

	public static GuiButton getSelectButton(GuiMultiplayer var0) {
		return var0.buttonSelect;
	}

	public static GuiButton getSelectAdd(GuiMultiplayer var0) {
		return var0.buttonAdd;
	}

	public static String func_22087_f(GuiMultiplayer var0) {
		return var0.field_22098_o;
	}

	public static void selectWorld(GuiMultiplayer var0, int var1) {
		var0.selectWorld(var1);
	}
	
	public static DateFormat getDateFormatter(GuiMultiplayer var0) {
		return var0.dateFormatter;
	}
	
	public static Object getSyncObject() {
		return synchronize;
	}

	public static String func_22088_h(GuiMultiplayer var0) {
		return var0.field_22097_p;
	}
	
	public static int getPingLimit() {
		return pinglimit;
	}
	
	public static void incrementPingLimit() {
		pinglimit++;
	}
	
	public static void decrementPingLimit() {
		pinglimit--;
	}
	
	public static void updateServerNBT(GuiMultiplayer var0, ServerSlot var1) throws IOException {
		var0.func_35328_b(var1);
	}
	
	public static void tooltip(GuiMultiplayer var0, String message) {
		var0.tooltip = message;
	}

}
