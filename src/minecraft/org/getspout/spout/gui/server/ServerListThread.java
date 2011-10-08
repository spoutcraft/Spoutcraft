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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import net.minecraft.src.GuiMultiplayer;

public class ServerListThread implements Runnable {

	GuiMultiplayer parentScreen;
	String addr;
	private static Thread instance = null;
	private LinkedHashMap<String,ArrayList<ServerSlot>> tempCountryMappings = new LinkedHashMap<String,ArrayList<ServerSlot>>();


	public ServerListThread(GuiMultiplayer var1, String var2) {
		this.parentScreen = var1;
		this.addr = var2;
	}

	public void init() {
		if (instance == null) {
			instance = new Thread(this);
			instance.start();
		}
	}

	public void run() {
		try {
			synchronized(this.parentScreen.serverInfo) {
				this.parentScreen.serverInfo.status = "Retrieving server list...";
			}
			String[] var1 = null;
			URL url = new URL(this.addr);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = reader.readLine();
			reader.close();
			synchronized(this.parentScreen.serverInfo) {
				this.parentScreen.serverInfo.status = "Processing servers...";
			}
			String[] text = line.split("\\{");

			for(int i = 1; i < text.length; ++i) {
				text[i].replaceAll("'","");
				ServerSlot slot = new ServerSlot(i);
				String[] var8 = text[i].split("\\,");
				
				for(int var9 = 0; var9 < var8.length; ++var9) {
					var1 = var8[var9].split("\\:");

					for(int var10 = 0; var10 < var1.length; ++var10) {
						var1[var10] = stripLeadingAndTrailingQuotes(var1[var10]);
					}

					slot = this.setServer(i, var1, slot);
				}

				ArrayList<ServerSlot> list = tempCountryMappings.get(slot.country);
				if (list == null) {
					list = new ArrayList<ServerSlot>();
					tempCountryMappings.put(slot.country, list);
				}
				list.add(slot);
			}

			for (String country : tempCountryMappings.keySet()) {
				Collections.sort(tempCountryMappings.get(country), new Comparator() {
					public int compare(Object arg0, Object arg1) {
						if (!(arg1 instanceof ServerSlot) || !(arg0 instanceof ServerSlot)) {
							return arg0.hashCode() - arg1.hashCode();
						}
						ServerSlot server1 = (ServerSlot)arg0;
						ServerSlot server2 = (ServerSlot)arg1;
						return server1.name.toLowerCase().compareTo(server2.name.toLowerCase());
					}

				});
			}
			synchronized(this.parentScreen.serverInfo) {
				ArrayList tempCountries = new ArrayList(tempCountryMappings.keySet());
				Collections.sort(tempCountries);
				
				this.parentScreen.serverInfo.serverList.clear();
				this.parentScreen.serverInfo.activeCountry = tempCountries.size() - 1;
				this.parentScreen.serverInfo.countries = tempCountries;
				this.parentScreen.serverInfo.countryMappings = tempCountryMappings;
				this.parentScreen.serverInfo.page = 0;
				this.parentScreen.serverInfo.status = "Done";
				this.parentScreen.updateList();
			}
		} catch (IOException var11) {
			synchronized(this.parentScreen.serverInfo) {
				this.parentScreen.serverInfo.status = "An Error Occured.";
			}
			var11.printStackTrace();
		}
		instance = null;
	}

	private ServerSlot setServer(int var1, String[] var2, ServerSlot var3) {
		if(var2.length >= 2) {
			if(var2[0].equalsIgnoreCase("ip")) {
				var3.ip = var2[1];
			} else if(var2[0].equalsIgnoreCase("port")) {
				var3.port = var2[1];
			} else if(var2[0].equalsIgnoreCase("name")) {
				var3.name = var2[1];
				while (var3.name.contains("0027")) {
					var3.name = var3.name.substring(0, var3.name.indexOf("0027") - 2) + "'" +  var3.name.substring(var3.name.indexOf("0027") + 4);
				}
				while (var3.name.contains("&amp;")) {
					var3.name = var3.name.substring(0, var3.name.indexOf("&amp;")) + "&" +  var3.name.substring(var3.name.indexOf("&amp;") + 5);
				}
				while (var3.name.contains("&quot;")) {
					var3.name = var3.name.substring(0, var3.name.indexOf("&quot;")) + "\"" +  var3.name.substring(var3.name.indexOf("&quot;") + 6);
				}
				while (var3.name.contains("&#233;")) {
					var3.name = var3.name.substring(0, var3.name.indexOf("&#233;")) + "e" +  var3.name.substring(var3.name.indexOf("&#233;") + 6);
				}
				while (var3.name.contains("&#180;")) {
					var3.name = var3.name.substring(0, var3.name.indexOf("&#180;")) + "'" +  var3.name.substring(var3.name.indexOf("&#180;") + 6);
				}
				while (var3.name.contains("&#194;")) {
					var3.name = var3.name.substring(0, var3.name.indexOf("&#194;")) + "a" +  var3.name.substring(var3.name.indexOf("&#194;") + 6);
				}
				if(var3.name.length() > 41) {
					var3.name = var3.name.substring(0, 41) + "...";
				}
			} else if(var2[0].equalsIgnoreCase("players")) {
				var3.players = Integer.parseInt(var2[1]);
			} else if(var2[0].equalsIgnoreCase("maxplayers")) {
				var3.maxPlayers = Integer.parseInt(var2[1]);
			} else if(var2[0].equalsIgnoreCase("country")) {
				var3.country = var2[1];
			} else if (var2[0].equalsIgnoreCase("uniqueid")) {
				var3.uniqueid = Integer.parseInt(var2[1]);
			}
		}

		return var3;
	}

	static String stripLeadingAndTrailingQuotes(String var0) {
		if(var0.startsWith("\"")) {
			var0 = var0.substring(1, var0.length());
		}

		if(var0.endsWith("\"")) {
			var0 = var0.substring(0, var0.length() - 1);
		}

		return var0;
	}
}
