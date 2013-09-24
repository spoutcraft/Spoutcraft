/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
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
package org.spoutcraft.client.gui.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

import org.bukkit.ChatColor;

import org.spoutcraft.api.gui.AbstractListModel;
import org.spoutcraft.api.gui.GenericListWidgetItem;
import org.spoutcraft.api.gui.ListWidgetItem;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.database.UrlElement;

public abstract class AbstractAPIModel extends AbstractListModel {
	protected List<UrlElement> urlElements = new LinkedList<UrlElement>();
	protected String API = "";
	protected boolean loading = false;
	protected Thread currentLoader = null;
	protected int lastPage = 1;
	protected String currentUrl = "";
	protected boolean moreItems = false;
	protected GenericListWidgetItem itemLoadNextItems = null;
	protected ArrayList<Object> apiData = null;
	protected GuiAPIDisplay currentGui = null;
	protected List<ListWidgetItem> effectiveCache = null;
	protected List<ListWidgetItem> entries = new ArrayList<ListWidgetItem>();

	public void loadNextPage() {
		lastPage++;
		refreshAPIData(getCurrentUrl(), lastPage, false);
	}

	protected List<ListWidgetItem> getEffectiveList() {
		List<ListWidgetItem> ret = new ArrayList<ListWidgetItem>();

		for (ListWidgetItem item:entries) {
			ret.add(item);
		}

		if (moreItems) {
			itemLoadNextItems = new GenericListWidgetItem("More items on the server.", "Click to load", "");
			ret.add(itemLoadNextItems);
		}
		return ret;
	}

	@Override
	public ListWidgetItem getItem(int row) {
		if (effectiveCache == null) {
			effectiveCache = getEffectiveList();
			sizeChanged();
		}
		if (row < 0 || row >= effectiveCache.size()) {
			return null;
		}
		return effectiveCache.get(row);
	}

	@Override
	public int getSize() {
		if (effectiveCache == null) {
			effectiveCache = getEffectiveList();
			sizeChanged();
		}
		return effectiveCache.size();
	}

	public void refreshAPIData(final String url, final int page, final boolean clear) {
		currentUrl = url;
		if (currentLoader != null && currentLoader.isAlive()) {
			currentLoader.interrupt();
			System.out.println("Stopped previous loading");
		}

		currentLoader = new Thread() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				BufferedReader reader = null;
				try {
					System.setProperty("http.agent", "");
					setLoading(true);
					//long start = System.currentTimeMillis();
					URL url1 = new URL(url + "&page=" + page);
					//System.out.println("Loading " + url1.toString());
					URLConnection conn = url1.openConnection();
					conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

					reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

					Yaml yaml = new Yaml();
					ArrayList<Object> yamlObj = (ArrayList<Object>) yaml.load(reader);
					//System.out.println("Loaded in " + (System.currentTimeMillis() - start) + " ms");
					apiData = yamlObj;
					HashMap<String, Object> hash = (HashMap<String, Object>) apiData.remove(0);
					int after = Integer.valueOf((String) hash.get("after"));
					moreItems = after > 0;

					refreshList(clear);

				} catch (IOException e1) {
					e1.printStackTrace();
					// Put a fancy error message on the list!
					clear();
					effectiveCache = new LinkedList<ListWidgetItem>();
					//String error = e1.getClass().getSimpleName().replaceAll("Exception", "");
					//error = error.replaceAll("([A-Z])", " $1").trim();
					effectiveCache.add(new GenericListWidgetItem(ChatColor.RED + "Could not load items!", e1.getMessage(), ""));
					return;
				} catch (Exception e) {
				}
				finally {
					setLoading(false);
					try {
						reader.close();
					} catch (Exception e) {
					}
				}
			}
		};
		currentLoader.start();
	}

	public void clear() {
		entries.clear();
		if (effectiveCache != null) {
			effectiveCache.clear();
		}
		moreItems = false;
	}

	protected abstract void refreshList(boolean clear);

	public boolean isLoading() {
		return loading;
	}

	public void setLoading(boolean l) {
		loading = l;
		if (currentGui != null) {
			currentGui.updateButtons();
		}
	}

	public void clearElementFilters() {
		for (UrlElement element: urlElements) {
			element.clear();
		}
	}

	public void addUrlElement(UrlElement el) {
		urlElements.add(el);
	}

	public void clearUrlElements() {
		urlElements.clear();
	}

	public GuiAPIDisplay getCurrentGui() {
		return currentGui;
	}

	public void updateUrl() {
		String url = API + "?";
		int i = 0;
		for (UrlElement element:urlElements) {
			if (element.isActive()) {
				if (i > 0) {
					url+="&";
				}
				url += element.getUrlPart();
				i++; // Only increment for active elements
			}
		}
		if (i != 0) {
			refreshAPIData(url, 0, true);
		}
	}

	public void update() {
		effectiveCache = null;
	}

	public void setCurrentGui(GuiAPIDisplay gui) {
		currentGui = gui;
	}

	public abstract String getDefaultUrl();

	public String getCurrentUrl() {
		return currentUrl;
	}

	public ArrayList<Object> getAPIData() {
		synchronized (apiData) {
			return apiData;
		}
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
		if (currentGui != null) {
			currentGui.updateButtons();
		}
		if (effectiveCache.get(item) == itemLoadNextItems) {
			loadNextPage();
			itemLoadNextItems.setTitle("Loading ...");
			itemLoadNextItems.setText("Please wait ...");
		}
	}
}
