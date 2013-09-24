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
package org.spoutcraft.client.gui.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.FileUtil;

public class FavoritesModel extends ServerModel {
	public FavoritesModel() {
	}

	public void load() {
		try {
			if (!getFile().exists()) {
				importVanilla();
				importLegacyTXT();
				save();
			} else {
				Yaml yaml = new Yaml();

				try {
					ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) yaml.load(new FileReader(getFile()));
					for (HashMap<String, Object> item : list) {
						String title = "";
						String ip = "";
						String country = "";
						int port = ServerItem.DEFAULT_PORT;
						int databaseId = -1;
						Boolean acceptsTextures = null;
						if (item.containsKey("title")) {
							title = (String) item.get("title");
						}
						if (item.containsKey("ip")) {
							ip = (String) item.get("ip");
						}
						if (item.containsKey("port")) {
							port = (Integer) item.get("port");
						}
						if (item.containsKey("databaseid")) {
							databaseId = (Integer) item.get("databaseid");
						}
						if (item.containsKey("acceptsTextures")) {
							acceptsTextures = (Boolean) item.get("acceptsTextures");
						}

						if (item.containsKey("country")) {
							country = (String) item.get("country");
						}

						addServer(title, ip, port, databaseId, acceptsTextures, country);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void importLegacyTXT() {
		File favorites = new File(FileUtil.getCacheDir(), "favorites.txt");
		if (favorites.exists()) {
			FileReader reader;
			try {
				reader = new FileReader(favorites);

				BufferedReader buffer = new BufferedReader(reader);
				while (true) {
					String line = buffer.readLine();
					if (line == null) {
						break;
					}
					String args[] = line.split(">");
					if (args.length == 2) {
						String title = args[0];
						String split[] = args[1].split(":");
						String ip = split[0];
						int port = split.length > 1 ? Integer.parseInt(split[1]) : ServerItem.DEFAULT_PORT;
						addServer(title, ip, port);
					}
					if (args.length == 3) {
						String title = args[1];
						String split[] = args[0].split(":");
						String ip = split[0];
						int port = split.length > 1 ? Integer.parseInt(split[1]) : ServerItem.DEFAULT_PORT;
						int databaseId = Integer.parseInt(args[2]);
						addServer(title, ip, port, databaseId);
					}
				}
				buffer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//favorites.delete(); // TODO Check if that is working first...
		}
	}

	private void importVanilla() {
		// TODO Auto-generated method stub

	}

	public void save() {
		Yaml yaml = new Yaml();
		ArrayList<Object> list = new ArrayList<Object>();
		for (ServerItem item : items) {
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("title", item.getTitle());
			data.put("ip", item.getIp());
			data.put("port", item.getPort());
			data.put("databaseid", item.getDatabaseId());
			data.put("country", item.getCountry());
			item.showPing = true;
			if (item.getAcceptsTextures() != null) {
				data.put("acceptsTextures", item.getAcceptsTextures().booleanValue());
			}
			list.add(data);
		}
		try {
			yaml.dump(list, new FileWriter(getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File getFile() {
		return new File(FileUtil.getConfigDir(), "favorites.yml");
	}

	public void addServer(String title, String ip, int port) {
		ServerItem item = new ServerItem(title, ip, port, -1);
		item.poll();
		items.add(item);
		sizeChanged();
		if (gui != null) {
			gui.updateButtons();
		}
	}

	public void addServer(String title, String ip, int port, int databaseId) {
		ServerItem item = new ServerItem(title, ip, port, databaseId);
		item.setShowPing(true);
		item.poll();
		items.add(item);
		sizeChanged();
		if (gui != null) {
			gui.updateButtons();
		}
	}

	public void addServer(String title, String ip, int port, int databaseId, Boolean acceptsTextures) {
		ServerItem item = new ServerItem(title, ip, port, databaseId);
		item.setShowPing(true);
		if (acceptsTextures != null) {
			item.setAcceptsTextures(acceptsTextures);
		}
		item.poll();
		items.add(item);
		sizeChanged();
		if (gui != null) {
			gui.updateButtons();
		}
	}

	public void addServer(String title, String ip, int port, int databaseId, Boolean acceptsTextures, String country) {
		ServerItem item = new ServerItem(title, ip, port, databaseId);
		item.setShowPing(true);
		if (acceptsTextures != null) {
			item.setAcceptsTextures(acceptsTextures);
		}
		item.poll();
		item.country = country;
		items.add(item);
		sizeChanged();
		if (gui != null) {
			gui.updateButtons();
		}
	}

	public void removeServer(ServerItem selectedItem) {
		items.remove(selectedItem);
		sizeChanged();
	}

	public void addServer(ServerItem item) {
		items.add(item);
		sizeChanged();
	}

	public void move(int selectedRow, int i) {
		ServerItem item = items.get(selectedRow);
		i = Math.max(0, i);
		i = Math.min(i, getSize() - 1);
		items.remove(selectedRow);
		items.add(i, item);
	}

	public boolean containsSever(ServerItem item) {
		for (ServerItem obj : items) {
			if (obj.getIp().equals(item.getIp()) && obj.getPort() == item.getPort()) {
				return true;
			}
		}
		return false;
	}
}
