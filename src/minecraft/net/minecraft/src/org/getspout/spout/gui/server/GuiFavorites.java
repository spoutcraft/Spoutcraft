package org.getspout.spout.gui.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class GuiFavorites extends GuiScreen {

	public List serverList = new ArrayList();
	private final DateFormat dateFormatter = new SimpleDateFormat();
	protected GuiScreen parentScreen;
	protected String screenTitle = "Select server";
	private boolean selected = false;
	private int selectedWorld;
	private List saveList;
	private GuiFavoritesSlot worldSlotContainer;
	private String field_22098_o;
	private String field_22097_p;
	private boolean deleting;
	private GuiButton buttonSelect;
	private GuiButton buttonDelete;
	private GuiButton buttonUp;
	private GuiButton buttonDown;
	private boolean remove = false;


	public GuiFavorites(GuiScreen var1) {
		this.parentScreen = var1;
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.screenTitle = var1.translateKey("Select server");
		this.field_22098_o = var1.translateKey("Unknown");
		this.field_22097_p = var1.translateKey("aaa");
		this.loadSaves();
		this.worldSlotContainer = new GuiFavoritesSlot(this);
		this.worldSlotContainer.registerScrollButtons(this.controlList, 4, 5);
		this.initButtons();
	}

	private void loadSaves() {
		this.selectedWorld = -1;
		this.serverList.clear();
		this.requestServer("");
		Collections.sort(this.serverList, new SortByID());
	}

	protected String getServerName(int var1) {
		return ((mcSBServer)this.serverList.get(var1)).name;
	}

	protected String getCountry(int var1) {
		return ((mcSBServer)this.serverList.get(var1)).country;
	}

	public void initButtons() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.controlList.add(this.buttonSelect = new GuiButton(1, this.width / 2 - 154, this.height - 52, 150, 20, var1.translateKey("Join Server")));
		this.controlList.add(this.buttonDelete = new GuiButton(2, this.width / 2 + 4, this.height - 52, 150, 20, var1.translateKey("Delete Favorite")));
		this.controlList.add(new GuiButton(3, this.width / 2 - 154, this.height - 28, 150, 20, var1.translateKey("Add new Favorite")));
		this.controlList.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20, var1.translateKey("Back")));
		this.controlList.add(this.buttonUp = new GuiButton(10, this.width - 20 - 10, 35, 20, 20, var1.translateKey("/\\")));
		this.controlList.add(this.buttonDown = new GuiButton(11, this.width - 20 - 10, this.height - 90, 20, 20, var1.translateKey("\\/")));
		this.buttonSelect.enabled = false;
		this.buttonDelete.enabled = false;
		this.buttonUp.enabled = false;
		this.buttonDown.enabled = false;
	}

	public void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == 1) {
				this.selectWorld(this.selectedWorld);
			} else if(var1.id == 2) {
				String var2 = ((mcSBServer)this.serverList.get(this.selectedWorld)).name;
				if(var2 != null) {
					this.deleting = true;
					StringTranslate var3 = StringTranslate.getInstance();
					String var4 = "Are you sure you want to remove \'" + var2 + "\' From the Favorites list?";
					String var5 = "This action is not Undoable";
					String var6 = var3.translateKey("selectWorld.deleteButton");
					String var7 = var3.translateKey("gui.cancel");
					GuiYesNo var8 = new GuiYesNo(this, var4, var5, var6, var7, this.selectedWorld);
					Spout.getGameInstance().displayGuiScreen(var8);
				}
			} else if(var1.id == 3) {
				Spout.getGameInstance().displayGuiScreen(new GuiAddFav(this, "", ""));
			} else if(var1.id == 0) {
				Spout.getGameInstance().displayGuiScreen(this.parentScreen);
			} else if(var1.id == 10) {
				this.shiftUp();
			} else if(var1.id == 11) {
				this.shiftDown();
			} else {
				this.worldSlotContainer.actionPerformed(var1);
			}

		}
	}

	public void selectWorld(int var1) {
		String[] var2 = ((mcSBServer)this.serverList.get(var1)).ip.split(":");
		Spout.getGameInstance().displayGuiScreen(new GuiConnecting(Spout.getGameInstance(), ((mcSBServer)this.serverList.get(var1)).ip, ((mcSBServer)this.serverList.get(var1)).port == ""?25565:Integer.parseInt(((mcSBServer)this.serverList.get(var1)).port)));
	}

	public void deleteWorld(boolean var1, int var2) {
		if(this.deleting) {
			this.deleting = false;
			if(var1) {
				this.delSave();
				Iterator var3 = this.serverList.iterator();

				while(var3.hasNext()) {
					mcSBServer var4 = (mcSBServer)var3.next();
					if(var2 != var4.ID) {
						this.writeFav(var4.name, var4.ip);
					}
				}

				this.loadSaves();
			}
		}

		Spout.getGameInstance().displayGuiScreen(this);
	}

	public void shiftUp() {
		if(((mcSBServer)this.serverList.get(this.selectedWorld)).ID > 0) {
			this.delSave();
			--((mcSBServer)this.serverList.get(this.selectedWorld)).ID;
			++((mcSBServer)this.serverList.get(this.selectedWorld - 1)).ID;
			Collections.sort(this.serverList, new SortByID());
			Iterator var1 = this.serverList.iterator();

			while(var1.hasNext()) {
				mcSBServer var2 = (mcSBServer)var1.next();
				this.writeFav(var2.name, var2.ip);
			}

			--this.selectedWorld;
		}

	}

	public void shiftDown() {
		if(((mcSBServer)this.serverList.get(this.selectedWorld)).ID < this.serverList.size() - 1) {
			this.delSave();
			++((mcSBServer)this.serverList.get(this.selectedWorld)).ID;
			--((mcSBServer)this.serverList.get(this.selectedWorld + 1)).ID;
			Collections.sort(this.serverList, new SortByID());
			Iterator var1 = this.serverList.iterator();

			while(var1.hasNext()) {
				mcSBServer var2 = (mcSBServer)var1.next();
				this.writeFav(var2.name, var2.ip);
			}

			++this.selectedWorld;
		}

	}

	public void requestServer(String var1) {
		try {
			int var2 = 0;
			FileInputStream var3 = new FileInputStream(Minecraft.getMinecraftDir().getPath() + "/Fav.serv");
			DataInputStream var4 = new DataInputStream(var3);

			String var6;
			for(BufferedReader var5 = new BufferedReader(new InputStreamReader(var4)); (var6 = var5.readLine()) != null; ++var2) {
				mcSBServer var7 = new mcSBServer(var2);
				String[] var8 = var6.split(">");
				var7 = this.setServer(var2, var7, var8[0], var8[1]);
				this.serverList.add(var7);
			}

			var4.close();
		} catch (Exception var9) {
			System.err.println("Error: " + var9.getMessage());
		}

	}

	public mcSBServer setServer(int var1, mcSBServer var2, String var3, String var4) {
		String[] var5 = var3.split(":");
		if(var5.length >= 2) {
			var2.port = var5[1];
		}

		var2.ip = var5[0];
		var2.name = var4;
		return var2;
	}

	public static String stripLeadingAndTrailingQuotes(String var0) {
		if(var0.startsWith("\"")) {
			var0 = var0.substring(1, var0.length());
		}

		if(var0.endsWith("\"")) {
			var0 = var0.substring(0, var0.length() - 1);
		}

		return var0;
	}

	public void writeFav(String var1, String var2) {
		try {
			FileWriter var3 = new FileWriter(Minecraft.getMinecraftDir().getPath() + "/Fav.serv", true);
			BufferedWriter var4 = new BufferedWriter(var3);
			var4.write(var2 + ">" + var1);
			var4.newLine();
			var4.flush();
			var4.close();
		} catch (Exception var5) {
			System.err.println("Error: " + var5.getMessage());
		}

	}

	public void delSave() {
		try {
			File var1 = new File(Minecraft.getMinecraftDir().getPath() + "/Fav.serv");
			if(!var1.delete()) {
				return;
			}

			(new FileWriter(Minecraft.getMinecraftDir().getPath() + "/Fav.serv", true)).close();
		} catch (IOException var2) {
			;
		}

	}

	public void drawScreen(int var1, int var2, float var3) {
		this.worldSlotContainer.drawScreen(var1, var2, var3);
		this.drawCenteredString(Spout.getGameInstance().fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
		super.drawScreen(var1, var2, var3);
	}

	public static List getSize(GuiFavorites var0) {
		return var0.serverList;
	}

	public static int onElementSelected(GuiFavorites var0, int var1) {
		return var0.selectedWorld = var1;
	}

	public static int getSelectedWorld(GuiFavorites var0) {
		return var0.selectedWorld;
	}

	public static GuiButton getSelectButton(GuiFavorites var0) {
		return var0.buttonSelect;
	}

	public static GuiButton getDeleteButton(GuiFavorites var0) {
		return var0.buttonDelete;
	}

	public static GuiButton getUpButton(GuiFavorites var0) {
		return var0.buttonUp;
	}

	public static GuiButton getDownButton(GuiFavorites var0) {
		return var0.buttonDown;
	}

	public static String func_22087_f(GuiFavorites var0) {
		return var0.field_22098_o;
	}

	public static DateFormat getDateFormatter(GuiFavorites var0) {
		return var0.dateFormatter;
	}

	public static String func_22088_h(GuiFavorites var0) {
		return var0.field_22097_p;
	}
}
