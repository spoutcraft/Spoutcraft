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
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

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

	@SuppressWarnings("unchecked")
	public void run() {
		try {
			synchronized(this.parentScreen.serverInfo) {
				this.parentScreen.serverInfo.status = "Retrieving server list...";
			}
			URL url = new URL(this.addr);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			Yaml yaml = new Yaml();
			ArrayList<Map<String, String>> list = (ArrayList<Map<String, String>>) yaml.load(reader);
			reader.close();
			
			synchronized(this.parentScreen.serverInfo) {
				this.parentScreen.serverInfo.status = "Processing servers...";
			}
			
			int j = 0;
			for (Map<String, String> i : list) {
				ServerSlot slot = new ServerSlot(++j);
				slot.ip = i.get("ip");
				slot.port = i.get("port");
				slot.country = i.get("country");
				slot.players = Integer.parseInt(i.get("players"));
				slot.maxPlayers = Integer.parseInt(i.get("maxplayers"));
				slot.name = URLDecoder.decode(i.get("name"), "UTF-8");
				slot.site = URLDecoder.decode(i.get("site"), "UTF-8");
				slot.forum = URLDecoder.decode(i.get("forumurl"), "UTF-8");
				slot.description = URLDecoder.decode(i.get("longdescription"), "UTF-8");
				slot.uniqueid = Integer.parseInt(i.get("uniqueid"));
				slot.loaded = true;
				
				ArrayList<ServerSlot> clist = tempCountryMappings.get(slot.country);
				if (clist == null) {
					clist = new ArrayList<ServerSlot>();
					tempCountryMappings.put(slot.country, clist);
				}
				clist.add(slot);
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
