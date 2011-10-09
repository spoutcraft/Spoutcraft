/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spout.gui.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.src.*;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.*;
import org.lwjgl.input.Keyboard;

public class GuiFavorites extends GuiScreen {

	public List serverList = new ArrayList();
	private final DateFormat dateFormatter = new SimpleDateFormat();
	protected GuiScreen parentScreen;
	protected String screenTitle = "Favorite Servers";
	private int selectedWorld;
	private GuiFavoritesSlot worldSlotContainer;
	private String field_22098_o;
	private String field_22097_p;
	private boolean deleting;
	private GuiButton buttonSelect;
	private GuiButton buttonDelete;
	private GuiButton buttonRename;
	private GuiButton buttonUp;
	private GuiButton buttonDown;
	private GuiTextField quickJoinText;
	private GuiButton quickJoin;
	private static final int JOIN_SERVER = 0;
	private static final int DELETE_SERVER = 1;
	private static final int ADD_SERVER = 2;
	private static final int PUBLIC_SERVER_LIST = 3;
	private static final int EDIT_SERVER = 4;
	private static final int ARROW_UP = 5;
	private static final int ARROW_DOWN = 6;
	private static final int MAIN_MENU = 7;
	private static final int QUICK_JOIN = 8;
	private String tooltip = null;


	public GuiFavorites(GuiScreen var1) {
		this.parentScreen = var1;
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		this.field_22098_o = var1.translateKey("Unknown");
		this.field_22097_p = var1.translateKey("aaa");
		this.loadSaves();
		this.worldSlotContainer = new GuiFavoritesSlot(this);
		this.worldSlotContainer.registerScrollButtons(this.controlList, 4, 5);
		this.initButtons();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	private void loadSaves() {
		this.selectedWorld = -1;
		this.serverList.clear();
		this.requestServer("");
		Collections.sort(this.serverList, new SortByID());
	}

	protected String getServerName(int var1) {
		return ((ServerSlot)this.serverList.get(var1)).name;
	}

	protected String getCountry(int var1) {
		return ((ServerSlot)this.serverList.get(var1)).country;
	}
	
	@Override
	public void updateScreen() {
		quickJoinText.updateCursorCounter();
	}
	
	@Override
	public void keyTyped(char letter, int key) {
		if (key == Keyboard.KEY_RETURN && quickJoinText.isFocused) {
			actionPerformed(quickJoin);
		}
		else {
			quickJoinText.textboxKeyTyped(letter, key);
			super.keyTyped(letter, key);
		}
	}
	
	@Override
	public void mouseClicked(int var1, int var2, int var3) {
		super.mouseClicked(var1, var2, var3);
		quickJoinText.mouseClicked(var1, var2, var3);
		worldSlotContainer.onClick(var1, var2, var3);
	}

	public void initButtons() {
		//Top Row
		quickJoinText = new GuiTextField(this, SpoutClient.getHandle().fontRenderer, this.width / 2 - 200, height - 70, 263, 20, "");
		quickJoinText.setMaxStringLength(40);
		quickJoinText.setText(SpoutClient.getHandle().gameSettings.lastServer.replace("_", ":"));
		controlList.add(quickJoin = new GuiButton(QUICK_JOIN, width / 2 + 67, height - 70, 129, 20, "Quick Join"));
		
		//Middle row
		controlList.add(buttonSelect = new GuiButton(JOIN_SERVER, width / 2 - 200, height - 46, 130, 20, "Join"));
		controlList.add(buttonDelete = new GuiButton(DELETE_SERVER, width / 2 - 66, height - 46, 130, 20, "Delete"));
		controlList.add(buttonRename = new GuiButton(EDIT_SERVER, width / 2 + 67, height - 46, 129, 20, "Edit"));
		
		//Bottom Row
		controlList.add(new GuiButton(ADD_SERVER, width / 2 - 200, height - 22, 130, 20, "Add Server"));
		controlList.add(new GuiButton(PUBLIC_SERVER_LIST, width / 2 - 66, height - 22, 130, 20, "Public Server List"));
		controlList.add(new GuiButton(MAIN_MENU, width / 2 + 67, height - 22, 129, 20, "Main Menu"));
		
		//Arrow keys
		controlList.add(buttonUp = new GuiButton(ARROW_UP, width - 30, 25, 20, 20, "/\\"));
		controlList.add(buttonDown = new GuiButton(ARROW_DOWN, width - 30, height - 101, 20, 20, "\\/"));
		
		buttonRename.enabled = false;
		buttonSelect.enabled = false;
		buttonDelete.enabled = false;
		buttonUp.enabled = false;
		buttonDown.enabled = false;
	}

	public void actionPerformed(GuiButton button) {
		if(button.enabled) {
			String serverName = null;
			switch(button.id) {
				case JOIN_SERVER:
					selectWorld(selectedWorld);
					break;
				case DELETE_SERVER:
					if (selectedWorld > -1) {
						serverName = ((ServerSlot)serverList.get(selectedWorld)).name;
						if(serverName != null) {
							deleting = true;
							SpoutClient.getHandle().displayGuiScreen(new GuiYesNo(this, "Are you sure you want to remove '" + serverName + "' from your favorites?", "This action can not be undone", StringTranslate.getInstance().translateKey("selectWorld.deleteButton"), StringTranslate.getInstance().translateKey("gui.cancel"), this.selectedWorld));
						}
					}
					break;
				case ADD_SERVER:
					SpoutClient.getHandle().displayGuiScreen(new GuiAddFav(this, "", "", 0));
					break;
				case PUBLIC_SERVER_LIST:
					SpoutClient.getHandle().displayGuiScreen(new GuiMultiplayer(this));
					break;
				case EDIT_SERVER:
					if (selectedWorld > -1) {
						ServerSlot slot = (ServerSlot)this.serverList.get(selectedWorld);
						serverName = slot.name;
						if(serverName != null) {
							String ip = slot.ip + (slot.port.length() > 0 ? ":" : "") + slot.port;
							deleting = true;
							deleteWorld(true, selectedWorld);
							SpoutClient.getHandle().displayGuiScreen(new GuiAddFav(this, serverName, ip, slot.uniqueid, true));
						}
					}
					break;
				case ARROW_UP:
					shiftUp();
					break;
				case ARROW_DOWN:
					shiftDown();
					break;
				case MAIN_MENU:
					SpoutClient.getHandle().displayGuiScreen(new GuiMainMenu());
					break;
				case QUICK_JOIN:
					try	{
						if (!quickJoinText.getText().isEmpty()) {
							String split[] = quickJoinText.getText().split(":");
							String ip = split[0];
							int port = split.length > 1 ? Integer.parseInt(split[1]) : 25565;
							SpoutClient.getHandle().gameSettings.lastServer = quickJoinText.getText().replace(":", "_");
							SpoutClient.getHandle().gameSettings.saveOptions();
							SpoutClient.getHandle().displayGuiScreen(new GuiConnecting(SpoutClient.getHandle(), ip, port));
						}
					}
					catch (Exception e) { }
					break;
				default:
					worldSlotContainer.actionPerformed(button);
					break;
			}

		}
	}

	public void selectWorld(int index) {
		if (index > -1) {
			try {
				ServerSlot info = ((ServerSlot)this.serverList.get(index));
				SpoutClient.getHandle().displayGuiScreen(new GuiConnecting(SpoutClient.getHandle(), info.ip, (info.port.length() > 0 ? Integer.parseInt(info.port) : 25565)));
			}
			catch (Exception e) { }
		}
	}
	
	public void elementInfo(int id) {
		ServerSlot info = (ServerSlot)this.serverList.get(id);
		SpoutClient.getHandle().displayGuiScreen(new GuiServerInfo(info, this));
	}

	public void deleteWorld(boolean var1, int var2) {
		if(this.deleting) {
			this.deleting = false;
			if(var1) {
				this.delSave();
				Iterator var3 = this.serverList.iterator();

				while(var3.hasNext()) {
					ServerSlot var4 = (ServerSlot)var3.next();
					if(var2 != var4.ID) {
						GuiFavorites.writeFav(var4.name, var4.ip, var4.uniqueid);
					}
				}

				this.loadSaves();
			}
		}

		SpoutClient.getHandle().displayGuiScreen(this);
	}

	public void shiftUp() {
		if(((ServerSlot)this.serverList.get(this.selectedWorld)).ID > 0) {
			this.delSave();
			--((ServerSlot)this.serverList.get(this.selectedWorld)).ID;
			++((ServerSlot)this.serverList.get(this.selectedWorld - 1)).ID;
			Collections.sort(this.serverList, new SortByID());
			Iterator var1 = this.serverList.iterator();

			while(var1.hasNext()) {
				ServerSlot var2 = (ServerSlot)var1.next();
				GuiFavorites.writeFav(var2.name, var2.ip, var2.uniqueid);
			}

			--this.selectedWorld;
		}

	}

	public void shiftDown() {
		if(((ServerSlot)this.serverList.get(this.selectedWorld)).ID < this.serverList.size() - 1) {
			this.delSave();
			++((ServerSlot)this.serverList.get(this.selectedWorld)).ID;
			--((ServerSlot)this.serverList.get(this.selectedWorld + 1)).ID;
			Collections.sort(this.serverList, new SortByID());
			Iterator var1 = this.serverList.iterator();

			while(var1.hasNext()) {
				ServerSlot var2 = (ServerSlot)var1.next();
				GuiFavorites.writeFav(var2.name, var2.ip, var2.uniqueid);
			}

			++this.selectedWorld;
		}

	}

	public void requestServer(String var1) {
		try {
			int serverSlot = 0;
			FileInputStream input = new FileInputStream(getFavoriteServerFile());
			DataInputStream data = new DataInputStream(input);

			String line;
			for(BufferedReader reader = new BufferedReader(new InputStreamReader(data)); (line = reader.readLine()) != null; serverSlot++) {
				ServerSlot slot = new ServerSlot(serverSlot);
				String[] serverData = line.split(">");
				if (serverData.length == 2) {
					slot = this.setServer(serverSlot, slot, serverData[1], serverData[0]);
				} else if (serverData.length == 3) {
					slot = this.setServer(serverSlot, slot, serverData[0], serverData[1]);
					slot.uniqueid = Integer.parseInt(serverData[2]);
				} else {
					continue;
				}
				this.serverList.add(slot);
			}

			data.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ServerSlot setServer(int var1, ServerSlot var2, String ip, String name) {
		String[] var5 = ip.split(":");
		if(var5.length >= 2) {
			var2.port = var5[1];
		}

		var2.ip = var5[0];
		var2.name = name;
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

	public static void writeFav(String name, String ip, int uid) {
		try {
			FileWriter writer = new FileWriter(getFavoriteServerFile(), true);
			writer.write(ip + ">" + name + ">" + uid + "\n");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void delSave() {
		try {
			File var1 = getFavoriteServerFile();
			if(!var1.delete()) {
				return;
			}

			(new FileWriter(getFavoriteServerFile())).close();
		} catch (IOException var2) {
			;
		}

	}
	
	private int parseIntWithDefault(String var1, int var2) {
		try {
			return Integer.parseInt(var1.trim());
		} catch (Exception var4) {
			return var2;
		}
	}

	private void func_35328_b(ServerSlot var1) throws IOException {
		String var2 = var1.ip + ":" + var1.port;
		String[] var3 = var2.split(":");
		if(var2.startsWith("[")) {
			int var4 = var2.indexOf("]");
			if(var4 > 0) {
				String var5 = var2.substring(1, var4);
				String var6 = var2.substring(var4 + 1).trim();
				if(var6.startsWith(":") && var6.length() > 0) {
					var6 = var6.substring(1);
					var3 = new String[]{var5, var6};
				} else {
					var3 = new String[]{var5};
				}
			}
		}

		if(var3.length > 2) {
			var3 = new String[]{var2};
		}

		String var29 = var3[0];
		int var30 = var3.length > 1?this.parseIntWithDefault(var3[1], 25565):25565;
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

			String var9 = Packet.readString(var7, 64);
			char[] var10 = var9.toCharArray();

			int var11;
			for(var11 = 0; var11 < var10.length; ++var11) {
				if(var10[var11] != 167 && ChatAllowedCharacters.allowedCharacters.indexOf(var10[var11]) < 0) {
					var10[var11] = 63;
				}
			}

			var9 = new String(var10);
			var3 = var9.split("\u00a7");
			var9 = var3[0];
			var11 = -1;
			int var12 = -1;

			try {
				var11 = Integer.parseInt(var3[1]);
				var12 = Integer.parseInt(var3[2]);
			} catch (Exception var27) { }

			var1.msg = "\u00a77" + var9;
			if(var11 >= 0 && var12 > 0) {
				var1.status = "\u00a77" + var11 + "\u00a78/\u00a77" + var12;
			} else {
				var1.status = "\u00a78???";
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
	
	public void drawScreen(int var1, int var2, float var3) {
		this.tooltip = null;
		buttonRename.enabled = buttonDelete.enabled;
		this.worldSlotContainer.drawScreen(var1, var2, var3);
		this.drawCenteredString(SpoutClient.getHandle().fontRenderer, "Favorite Servers", this.width / 2, 5, 16777215);
		quickJoinText.drawTextBox();
		if (quickJoinText.getText().isEmpty() && !quickJoinText.isFocused) {
			this.drawString(SpoutClient.getHandle().fontRenderer, "Quick Join: ", this.width / 2 - 196, height - 64, 5263440);
		}
		if(this.tooltip != null) {
			this.drawTooltip(this.tooltip, var1, var2);
		}
		super.drawScreen(var1, var2, var3);
	}

	public static List getSize(GuiFavorites screen) {
		return screen.serverList;
	}

	public static int onElementSelected(GuiFavorites screen, int var1) {
		return screen.selectedWorld = var1;
	}

	public static int getSelectedWorld(GuiFavorites screen) {
		return screen.selectedWorld;
	}

	public static GuiButton getSelectButton(GuiFavorites screen) {
		return screen.buttonSelect;
	}

	public static GuiButton getDeleteButton(GuiFavorites screen) {
		return screen.buttonDelete;
	}

	public static GuiButton getUpButton(GuiFavorites screen) {
		return screen.buttonUp;
	}

	public static GuiButton getDownButton(GuiFavorites screen) {
		return screen.buttonDown;
	}
	
	public static GuiButton getRenameButton(GuiFavorites screen) {
		return screen.buttonRename;
	}

	public static String func_22087_f(GuiFavorites screen) {
		return screen.field_22098_o;
	}

	public static DateFormat getDateFormatter(GuiFavorites screen) {
		return screen.dateFormatter;
	}

	public static String func_22088_h(GuiFavorites screen) {
		return screen.field_22097_p;
	}
	
	public static void updateServerNBT(GuiFavorites var0, ServerSlot var1) throws IOException {
		var0.func_35328_b(var1);
	}
	
	public static void onElementInfo(GuiFavorites var0, int var1) {
		var0.elementInfo(var1);
	}
	
	public static File getFavoriteServerFile() {
		File favorites = new File(FileUtil.getCacheDirectory(), "favorites.txt");
		if (!favorites.exists()) {
			try {
				favorites.createNewFile();
			}
			catch (IOException io) {io.printStackTrace();}
		}
		return favorites;
	}
	
	public static void tooltip(GuiFavorites var0, String message) {
		var0.tooltip = message;
	}
}
