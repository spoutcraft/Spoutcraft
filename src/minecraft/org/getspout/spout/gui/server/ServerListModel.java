package org.getspout.spout.gui.server;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.database.AbstractAPIModel;
import org.spoutcraft.spoutcraftapi.gui.AbstractListModel;
import org.spoutcraft.spoutcraftapi.gui.GenericListView;
import org.spoutcraft.spoutcraftapi.gui.GenericListWidgetItem;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;
import org.spoutcraft.spoutcraftapi.gui.Orientation;
import org.yaml.snakeyaml.Yaml;

public class ServerListModel extends AbstractAPIModel {

	protected TIntObjectHashMap<ServerDataBaseEntry> dbEntries = new TIntObjectHashMap<ServerDataBaseEntry>();
	protected List<String> countries = new LinkedList<String>();

	public ServerListModel() {
		API = "http://servers.getspout.org/api2.php";
		refreshAPIData(getDefaultUrl(), 0, true);
		loadCountries();
	}

	private void loadCountries() {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if(wasSandboxed) SpoutClient.disableSandbox();
		new Thread() {
			public void run() {
				long start = System.currentTimeMillis();
				URL url1;
				try {
					url1 = new URL(API + "?countries");
				} catch (MalformedURLException e) {
					return;
				}
				System.out.println("Loading "+url1.toString());
				BufferedReader reader;
				try {
					reader = new BufferedReader(new InputStreamReader(url1.openStream()));
				} catch (IOException e1) {
					System.out.println(e1.getClass().toString());
					return;
				}
				Yaml yaml = new Yaml();
				ArrayList<String> yamlObj = (ArrayList<String>) yaml.load(reader);
				System.out.println("Loaded in " + (System.currentTimeMillis() - start) + " ms");
				synchronized (countries) {
					countries.clear();
					for(String c:yamlObj) {
						countries.add(c);
					}
				}
				try {
					reader.close();
				} catch (IOException e) {
					return;
				}
			}
		}.start();
		if(wasSandboxed) SpoutClient.enableSandbox();
	}

	public String getDefaultUrl() {
		return API+"?featured";
	}

	/**
	 * Processes loaded API data.
	 * @param clear
	 */
	protected void refreshList(boolean clear) {
		System.out.println("refreshing list.");
		if(clear) {
			lastPage = 0;
			entries.clear();
			for(GenericListView view:getViews()) {
				view.setScrollPosition(Orientation.VERTICAL, 0);
			}
		}
		for(Object item:apiData) {
			try{
				HashMap<String, Object> hash = (HashMap<String, Object>) item;
				String name = URLDecoder.decode((String) hash.get("name"), "UTF-8");
				int uid = Integer.valueOf((String)hash.get("uniqueid"));
				int port = Integer.valueOf((String)hash.get("port"));
				String adress = (String) hash.get("ip");
				//int ping = Integer.valueOf((String)hash.get("ping"));
				//int players = Integer.valueOf((String)hash.get("numplayers"));
				//int maxplayers = Integer.valueOf((String)hash.get("maxplayers"));
				boolean whitelisted = ((String)hash.get("whitelist")).equals("1")?true:false;
				String country = (String) hash.get("country");
				ServerItem server = new ServerItem(name, adress, port, uid);
				server.setFavorite(false);
				//server.setPing(ping);
				//server.setPlayers(players);
				//server.setMaxPlayers(maxplayers);
				server.setCountry(country);
				server.setWhitelisted(whitelisted);
				//server.setShowPingWhilePolling(true);
				entries.add(server);
			} catch(UnsupportedEncodingException e){}
			catch(Exception e2) { continue; }
		}
		update();
	}

	public ServerDataBaseEntry getServerDBEntry(int uid) {
		if(dbEntries.contains(uid)) {
			return dbEntries.get(uid);
		} else {
			return null;
		}
	}

	public List<ServerItem> getServers() {
		ArrayList<ServerItem> servers = new ArrayList<ServerItem>();
		for(ListWidgetItem item:entries) {
			if(item instanceof ServerItem) {
				servers.add((ServerItem)item);
			}
		}
		return servers;
	}

	public List<String> getCountries() {
		return Collections.unmodifiableList(countries);
	}
}
