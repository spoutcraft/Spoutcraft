package net.minecraft.src;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import net.minecraft.src.GuiButton;

import org.getspout.spout.gui.server.*;

public class GuiMultiplayer extends GuiScreen {

	public static final String version = "1.7.3";
	public String status = null;
	public List serverList = new ArrayList();
	public LinkedHashMap<String,ArrayList> countryMappings = new LinkedHashMap<String,ArrayList>();
	public List<String> countries = new ArrayList();
	public int activeCountry = 0;
	public int page = 0;
	public int pages = 0;
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
	private GuiButton buttonNextPage;
	private GuiButton buttonPrevPage;
	private boolean first = true;


	public GuiMultiplayer(GuiScreen var1) {
		this.parentScreen = var1;
	}

	public void initGui() {
		this.status = "Done";
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
		return ((mcSBServer)this.serverList.get(var1)).name;
	}

	public String getCountry(int var1) {
		return ((mcSBServer)this.serverList.get(var1)).country;
	}

	public void initButtons() {
		this.controlList.add(this.buttonSelect = new GuiButton(1, this.width / 2 - 74, this.height - 52, 70, 20, "Join Server"));
		this.controlList.add(new GuiButton(2, this.width / 2 - 154, this.height - 28, 150, 20, "Favorites"));
		this.controlList.add(new GuiButton(3, this.width / 2 + 4, this.height - 52, 150, 20, "Refresh"));
		this.controlList.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20, "Main Menu"));
		this.controlList.add(this.buttonPrevCountry = new GuiButton(6, this.width / 2 - 154, this.height - 76, 40, 20, "<<"));
		this.controlList.add(this.buttonNextCountry = new GuiButton(7, this.width / 2 + 114, this.height - 76, 40, 20, ">>"));
		this.controlList.add(this.buttonPrevPage = new GuiButton(8, this.width / 2 - 104, this.height - 76, 40, 20, "<"));
		this.controlList.add(this.buttonNextPage = new GuiButton(9, this.width / 2 + 64, this.height - 76, 40, 20, ">"));
		this.controlList.add(this.buttonAdd = new GuiButton(4, this.width / 2 - 154, this.height - 52, 70, 20, "Add Fav"));
		this.buttonSelect.enabled = false;
		this.buttonAdd.enabled = false;
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == 2) {
				this.mc.displayGuiScreen(new GuiFavorites(this));
			} else if(var1.id == 1) {
				this.selectWorld(this.selectedWorld);
			} else if(var1.id == 3) {
				this.getServer();
			} else if(var1.id == 4) {
				this.mc.displayGuiScreen(new GuiAddFav(this, ((mcSBServer)this.serverList.get(this.selectedWorld)).ip + ":" + ((mcSBServer)this.serverList.get(this.selectedWorld)).port, ((mcSBServer)this.serverList.get(this.selectedWorld)).name));
			} else if(var1.id == 0) {
				this.mc.displayGuiScreen(new GuiMainMenu());
			} else if(var1.id == 8) {
				if(page > 0) {
					page--;
					updateList();
				}
			} else if(var1.id == 9) {
				if(page < pages - 1) {
					page++;
					updateList();
				}
			} else if(var1.id == 6) {
				if(activeCountry > 0) {
					activeCountry--;
					page = 0;
					updateList();
				}
			} else if(var1.id == 7) {
				if(activeCountry < countryMappings.size() - 1) {
					activeCountry++;
					page = 0;
					updateList();
				}
			} else {
				this.worldSlotContainer.actionPerformed(var1);
			}

		}
	}

	public void selectWorld(int id) {
		this.mc.displayGuiScreen(new GuiConnecting(this.mc, ((mcSBServer)this.serverList.get(id)).ip, ((mcSBServer)this.serverList.get(id)).port == ""?25565:Integer.parseInt(((mcSBServer)this.serverList.get(id)).port)));
	}
	
	public void updateList() {
		if (countries.size() == 0) {
			indexString = "Empty (0/0)";
			serverList = new ArrayList();
		} else {
			String country = countries.get(activeCountry);
			ArrayList fullList = countryMappings.get(country);
			pages = (fullList.size() + 9) / 10;
			int last = Math.min(fullList.size(), (page + 1) * 10);
			int first = page * 10;
			serverList = fullList.subList(first, last);
			indexString = country + " (" + (page + 1) + "/" + pages + ")";
		}
		this.buttonNextPage.enabled = page < pages - 1;
		this.buttonPrevPage.enabled = page > 0;
		this.buttonPrevCountry.enabled = activeCountry > 0;
		this.buttonNextCountry.enabled = activeCountry < countries.size() - 1;
	}

	public void deleteWorld(boolean var1, int var2) {}

	public void drawScreen(int x, int y, float z) {
		this.worldSlotContainer.drawScreen(x, y, z);
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
		this.drawCenteredString(this.fontRenderer, "Spoutcraft Server Browser", this.width / 2, this.height - 86, 5263440);
		this.drawCenteredString(this.fontRenderer, indexString , this.width / 2, this.height - 71, 5263440);
		this.drawString(this.fontRenderer, "Displaying " + this.serverList.size() + " servers", this.width - this.fontRenderer.getStringWidth("Displaying " + this.serverList.size() + " servers") - 2, 20, 5263440);
		this.drawString(this.fontRenderer, "Status: " + this.status, 2, 20, 5263440);
		super.drawScreen(x, y, z);
	}

	public void getServer() {
		QuServer serverList = new QuServer(this, "http://list.mcmyadmin.com/getdata.aspx?req=serverlist");
		serverList.StartGet();
	}

	public static List getSize(GuiMultiplayer var0) {
		return var0.serverList;
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
