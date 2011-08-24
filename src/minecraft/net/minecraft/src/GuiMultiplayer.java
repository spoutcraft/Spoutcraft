package net.minecraft.src;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Collections;

import net.minecraft.src.GuiButton;

import org.getspout.spout.gui.server.*;

public class GuiMultiplayer extends GuiScreen {

	public static final String version = "1.7.3";
	public final ServerListInfo serverInfo = new ServerListInfo();
	public String indexString = "";
	private final DateFormat dateFormatter = new SimpleDateFormat();
	protected GuiScreen parentScreen;
	protected String screenTitle = "Select server";
	private int selectedWorld;
	private GuiServerSlot worldSlotContainer;
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


	public GuiMultiplayer(GuiScreen var1) {
		this.parentScreen = var1;
	}

	public void initGui() {
		synchronized(serverInfo) {
			this.serverInfo.status = "Done";
		}
		StringTranslate var1 = StringTranslate.getInstance();
		this.screenTitle = var1.translateKey("Select server");
		this.field_22098_o = var1.translateKey("Unknown");
		this.field_22097_p = var1.translateKey("aaa");
		this.loadSaves();
		this.worldSlotContainer = new GuiServerSlot(this);
		this.worldSlotContainer.registerScrollButtons(this.controlList, 4, 5);
		this.initButtons();
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
					this.mc.displayGuiScreen(new GuiAddFav(this, ((ServerSlot)this.serverInfo.serverList.get(this.selectedWorld)).ip + ":" + ((ServerSlot)this.serverInfo.serverList.get(this.selectedWorld)).port, ((ServerSlot)this.serverInfo.serverList.get(this.selectedWorld)).name));
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

	public void updateList() {
		synchronized(serverInfo) {
			worldSlotContainer.amountScrolled = 0f;
			if (serverInfo.countries.size() == 0) {
				indexString = "Empty (0/0)";
				serverInfo.serverList = new ArrayList();
			} else {
				String country = serverInfo.countries.get(serverInfo.activeCountry);
				ArrayList fullList = serverInfo.countryMappings.get(country);
				Collections.sort(fullList);
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
			this.buttonPrevCountry.enabled = serverInfo.activeCountry > 0;
			this.buttonNextCountry.enabled = serverInfo.activeCountry < serverInfo.countries.size() - 1;
		}
	}

	public void deleteWorld(boolean var1, int var2) {}

	public void drawScreen(int x, int y, float z) {
		synchronized(serverInfo) {
			this.worldSlotContainer.drawScreen(x, y, z);
			this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
			this.drawCenteredString(this.fontRenderer, "Spoutcraft Server Browser", this.width / 2, this.height - 86, 5263440);
			this.drawCenteredString(this.fontRenderer, indexString , this.width / 2, this.height - 71, 5263440);
			this.drawString(this.fontRenderer, "Displaying " + this.serverInfo.serverList.size() + " servers", this.width - this.fontRenderer.getStringWidth("Displaying " + this.serverInfo.serverList.size() + " servers") - 2, 20, 5263440);
			this.drawString(this.fontRenderer, "Status: " + this.serverInfo.status, 2, 20, 5263440);
		}
		super.drawScreen(x, y, z);
	}

	public void getServer() {
		ServerListThread serverList = new ServerListThread(this, "http://list.mcmyadmin.com/getdata.aspx?req=serverlist");
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

	public static DateFormat getDateFormatter(GuiMultiplayer var0) {
		return var0.dateFormatter;
	}

	public static String func_22088_h(GuiMultiplayer var0) {
		return var0.field_22097_p;
	}

}
