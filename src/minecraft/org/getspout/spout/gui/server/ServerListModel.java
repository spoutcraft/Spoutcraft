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
import org.spoutcraft.spoutcraftapi.gui.AbstractListModel;
import org.spoutcraft.spoutcraftapi.gui.GenericListView;
import org.spoutcraft.spoutcraftapi.gui.GenericListWidgetItem;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;
import org.spoutcraft.spoutcraftapi.gui.Orientation;
import org.yaml.snakeyaml.Yaml;

public class ServerListModel extends AbstractListModel {
	
	protected List<ServerItem> servers = new ArrayList<ServerItem>();
	protected List<ListWidgetItem> effectiveCache = null;
	protected List<UrlElement> urlElements = new LinkedList<UrlElement>();
	protected ArrayList<Object> apiData = null;
	protected TIntObjectHashMap<ServerDataBaseEntry> dbEntries = new TIntObjectHashMap<ServerDataBaseEntry>();
	protected GuiServerList currentGui = null;
	protected int lastPage = 1;
	protected String currentUrl;
	protected boolean moreItems = false;
	protected GenericListWidgetItem itemLoadNextItems = null;
	protected Thread currentLoader = null;
	protected List<String> countries = new LinkedList<String>();
	public static final String API = "http://servers.getspout.org/api2.php";
	protected boolean loading = false;
	
	public ServerListModel() {
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
	
	public void setCurrentGui(GuiServerList gui) {
		currentGui = gui;
	}
	
	protected List<ListWidgetItem> getEffectiveList() {
		List<ListWidgetItem> ret = new ArrayList<ListWidgetItem>();
		
		for(ServerItem item: servers) {
			ret.add(item);
		}
		
		if(moreItems) {
			if(itemLoadNextItems == null) {
				itemLoadNextItems = new GenericListWidgetItem("More items on the server.", "Double click to load", "");
			}
			ret.add(itemLoadNextItems);
		}
		
		
		return ret;
	}
	
	public void loadNextPage() {
		lastPage++;
		refreshAPIData(getCurrentUrl(), lastPage, false);
	}
	
	public String getCurrentUrl() {
		return currentUrl;
	}

	@Override
	public ListWidgetItem getItem(int row) {
		if(effectiveCache == null) {
			effectiveCache = getEffectiveList();
			sizeChanged();
		}
		return effectiveCache.get(row);
	}

	@Override
	public int getSize() {
		if(effectiveCache == null) {
			effectiveCache = getEffectiveList();
			sizeChanged();
		}
		return effectiveCache.size();
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
		currentGui.updateButtons();
		if(effectiveCache.get(item) == itemLoadNextItems && doubleClick) {
			loadNextPage();
			itemLoadNextItems.setTitle("Loading ...");
			itemLoadNextItems.setText("Please wait ...");
		}
	}
	
	public ArrayList<Object> getAPIData() {
		synchronized (apiData) {
			return apiData;
		}
	}
	
	public String getDefaultUrl() {
		return API+"?featured";
	}
	
	public void refreshAPIData(final String url, final int page, final boolean clear) {
		currentUrl = url;
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if(wasSandboxed) SpoutClient.disableSandbox();
		
		if(currentLoader != null && currentLoader.isAlive()) {
			currentLoader.interrupt();
			System.out.println("Stopped previous loading");
		}
		
		currentLoader = new Thread() {
			@Override
			public void run() {
				setLoading(true);
				long start = System.currentTimeMillis();
				URL url1;
				try {
					url1 = new URL(url+"&page="+page);
				} catch (MalformedURLException e) {
					return;
				}
				System.out.println("Loading "+url1.toString());
				BufferedReader reader;
				try {
					reader = new BufferedReader(new InputStreamReader(url1.openStream()));
				} catch (IOException e1) {
					//Put a fancy error message on the list!
					servers.clear();
					effectiveCache = new LinkedList<ListWidgetItem>();
					String error = e1.getClass().getSimpleName().replaceAll("Exception", "");
					error = error.replaceAll("([A-Z])", " $1").trim();
					effectiveCache.add(new GenericListWidgetItem(ChatColor.RED+"Could not load servers!", error, ""));
					return;
				}
				Yaml yaml = new Yaml();
				ArrayList<Object> yamlObj = (ArrayList<Object>) yaml.load(reader);
				System.out.println("Loaded in " + (System.currentTimeMillis() - start) + " ms");
				apiData = yamlObj;
				refreshList(clear);
				setLoading(false);
				try {
					reader.close();
				} catch (IOException e) {
					return;
				}
			}
		};
		currentLoader.start();
		if(wasSandboxed) SpoutClient.enableSandbox();
	}
	
	/**
	 * Processes loaded API data.
	 * @param clear
	 */
	protected void refreshList(boolean clear) {
		System.out.println("refreshing list.");
		if(clear) {
			lastPage = 0;
			servers.clear();
			for(GenericListView view:getViews()) {
				view.setScrollPosition(Orientation.VERTICAL, 0);
			}
		}
		int i = 0;
		for(Object item:apiData) {
			if(i != 0) {
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
//					server.setPlayers(players);
//					server.setMaxPlayers(maxplayers);
					server.setCountry(country);
					server.setWhitelisted(whitelisted);
//					server.setShowPingWhilePolling(true);
					servers.add(server);
				} catch(UnsupportedEncodingException e){}
				catch(Exception e2) { continue; }
			} else {
				HashMap<String, Object> hash = (HashMap<String, Object>) item;
				int after = Integer.valueOf((String) hash.get("after"));
				moreItems = after > 0;
			}
			i++;
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

	public void update() {
		effectiveCache = null;
	}
	
	public void updateUrl() {
		String url = API+"?";
		int i = 0;
		for(UrlElement element:urlElements) {
			if(element.isActive()) {
				if(i>0) url+="&";
				url += element.getUrlPart();
				i++; //Only increment for active elements
			}
		}
		if(i == 0) {
			Thread.dumpStack();
		} else {
			refreshAPIData(url, 0, true);
		}
	}
	
	public void addUrlElement(UrlElement el) {
		urlElements.add(el);
	}
	
	public void clearUrlElements() {
		urlElements.clear();
	}
	
	public List<ServerItem> getServers() {
		return Collections.unmodifiableList(servers);
	}
	
	public List<String> getCountries() {
		return Collections.unmodifiableList(countries);
	}
	
	public boolean isLoading() {
		return loading;
	}
	
	public void setLoading(boolean l) {
		loading = l;
		if(currentGui != null)
			currentGui.updateButtons();
	}

	public void clearElementFilters() {
		for(UrlElement element: urlElements) {
			element.clear();
		}
	}

	public GuiServerList getCurrentGui() {
		return currentGui;
	}
}
