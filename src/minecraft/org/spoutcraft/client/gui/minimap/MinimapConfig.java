/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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
package org.spoutcraft.client.gui.minimap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.FileUtil;

public class MinimapConfig {
	private static volatile MinimapConfig instance;
	private final Configuration config;

	private boolean enabled = true;
	private boolean coords = true;
	private int zoom = 1;
	private boolean color = true;
	private boolean square = false;
	private boolean lightmap = false;
	private boolean heightmap = true;
	private boolean cavemap = false;
	private boolean firstrun = true;
	private boolean scale = false;
	private float xAdjust = 0;
	private float yAdjust = 0;
	private float sizeAdjust = 1F;
	private boolean directions = true;
	private boolean deathpoints = true;
	private boolean background = true;
	private final Map<String, List<Waypoint>> waypoints = new HashMap<String, List<Waypoint>>();
	private Waypoint focussedWaypoint = null;

	/*
	 * minimap: enabled: true ... waypoints: world1: home: x: 128 z: -82
	 * enabled: 1 work: ...
	 */
	private MinimapConfig(boolean load) {
		File file = new File(FileUtil.getSpoutcraftDirectory(), "minimap.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) { }
		}
		config = new Configuration(file);
		if (load) {
			config.load();
			enabled = config.getBoolean("minimap.enabled", enabled);
			coords = config.getBoolean("minimap.coords", coords);
			zoom = config.getInt("minimap.zoom", zoom);
			color = config.getBoolean("minimap.color", color);
			square = config.getBoolean("minimap.square", square);
			lightmap = config.getBoolean("minimap.lightmap", lightmap);
			heightmap = config.getBoolean("minimap.heightmap", heightmap);
			cavemap = config.getBoolean("minimap.cavemap", cavemap);
			scale = config.getBoolean("minimap.scale", scale);
			deathpoints = config.getBoolean("minimap.deathpoints", deathpoints);
			background = config.getBoolean("minimap.background", background);
			xAdjust = (float) config.getDouble("minimap.xAdjust", xAdjust);
			yAdjust = (float) config.getDouble("minimap.yAdjust", yAdjust);
			sizeAdjust = (float) config.getDouble("minimap.sizeAdjust", sizeAdjust);
			directions = config.getBoolean("minimap.directions", directions);
		}
		firstrun = config.getBoolean("minimap.firstrun", firstrun);
		Map<?, ?> worlds = config.getNodes("waypoints");
		if (worlds != null) {
			Iterator<?> i = worlds.entrySet().iterator();
			while (i.hasNext()) {
				Object o = i.next();
				if (o instanceof Entry) {
					@SuppressWarnings("rawtypes")
					Entry<?, ?> e = (Entry) o;
					if (e.getKey() instanceof String) {
						try {
							String world = (String) e.getKey();
							ConfigurationNode waypoints = (ConfigurationNode) e.getValue();
							for(String name:waypoints.getKeys()) {
								Map<String, Object> locations = waypoints.getNode(name).getAll();
								int x, y = 64, z;
								x = (Integer) locations.get("x");
								if(locations.containsKey("y")) {
									y = (Integer) locations.get("y");
								}
								z = (Integer) locations.get("z");
								boolean enabled = (Integer) locations.get("enabled") == 1;
								boolean deathpoint = (Integer) locations.get("deathpoint") == 1;
								Waypoint waypoint = new Waypoint(name, x, y, z, enabled);
								waypoint.deathpoint = deathpoint;
								addWaypoint(world, waypoint);
							}
						} catch (Exception ex) {
							System.err.println("Error while reading waypoints: ");
							ex.printStackTrace();
						}
					} else {
						System.out.println("Entry did not satisfy needs... key: " + e.getKey() + " value: " + e.getValue());
					}
				}
			}
		}
	}

	public static void initialize() {
		instance = new MinimapConfig(true);
	}

	public static void initialize(boolean load) {
		instance = new MinimapConfig(load);
	}

	public synchronized void save() {
		config.setProperty("minimap.enabled", enabled);
		config.setProperty("minimap.coords", coords);
		config.setProperty("minimap.zoom", zoom);
		config.setProperty("minimap.color", color);
		config.setProperty("minimap.square", square);
		config.setProperty("minimap.lightmap", lightmap);
		config.setProperty("minimap.heightmap", heightmap);
		config.setProperty("minimap.cavemap", cavemap);
		config.setProperty("minimap.firstrun", firstrun);
		config.setProperty("minimap.scale", scale);
		config.setProperty("minimap.deathpoints", deathpoints);
		config.setProperty("minimap.background", background);
		config.setProperty("minimap.xAdjust", xAdjust);
		config.setProperty("minimap.yAdjust", yAdjust);
		config.setProperty("minimap.sizeAdjust", sizeAdjust);
		config.setProperty("minimap.directions", directions);
		HashMap<String, Map<String, Map<String, Integer>>> worlds = new HashMap<String, Map<String, Map<String, Integer>>>();
		Iterator<Entry<String, List<Waypoint>>> i = waypoints.entrySet().iterator();
		while (i.hasNext()) {
			Entry<String, List<Waypoint>> e = i.next();
			String world = e.getKey();
			HashMap<String, Map<String, Integer>> waypoints = new HashMap<String, Map<String, Integer>>();
			for (Waypoint waypoint : e.getValue()) {
				if (!waypoint.server) { 
					HashMap<String, Integer> values = new HashMap<String, Integer>();
					values.put("x", waypoint.x);
					values.put("y", waypoint.y);
					values.put("z", waypoint.z);
					values.put("enabled", waypoint.enabled ? 1 : 0);
					values.put("deathpoint", waypoint.deathpoint ? 1 : 0);
					waypoints.put(waypoint.name, values);
				}
			}
			worlds.put(world, waypoints);
		}
		config.setProperty("waypoints", worlds);
		config.save();
	}

	public static MinimapConfig getInstance() {
		return instance;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isCoords() {
		return coords && SpoutClient.getInstance().isCoordsCheat();
	}

	public void setCoords(boolean coords) {
		this.coords = coords;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public boolean isColor() {
		return color;
	}

	public void setColor(boolean color) {
		this.color = color;
	}

	public boolean isSquare() {
		return square;
	}

	public void setSquare(boolean square) {
		this.square = square;
	}

	public boolean isLightmap() {
		return lightmap;
	}

	public void setLightmap(boolean lightmap) {
		this.lightmap = lightmap;
	}

	public boolean isHeightmap() {
		return heightmap;
	}

	public void setHeightmap(boolean heightmap) {
		this.heightmap = heightmap;
	}

	public boolean isCavemap() {
		return cavemap;
	}

	public void setCavemap(boolean cavemap) {
		this.cavemap = cavemap;
	}

	public boolean isFirstrun() {
		return firstrun;
	}

	public void setFirstrun(boolean firstrun) {
		this.firstrun = firstrun;
	}

	public synchronized List<Waypoint> getWaypoints(String world) {
		List<Waypoint> list = waypoints.get(world);
		if (list == null) {
			list = new ArrayList<Waypoint>();
			waypoints.put(world, list);
		}
		return list;
	}

	public synchronized void removeWaypoint(String world, String name) {
		Iterator<Waypoint> i = getWaypoints(world).iterator();
		while (i.hasNext()) {
			if (i.next().name.equalsIgnoreCase(name)) {
				i.remove();
			}
		}
	}

	public synchronized void addWaypoint(String world, String name, int x, int y, int z, boolean enabled) {
		getWaypoints(world).add(new Waypoint(name, x, y, z, enabled));
	}

	public boolean isScale() {
		return scale;
	}

	public void setScale(boolean scale) {
		this.scale = scale;
	}

	public float getAdjustX() {
		return xAdjust;
	}

	public void setAdjustX(float xAdjust) {
		this.xAdjust = xAdjust;
	}

	public float getAdjustY() {
		return yAdjust;
	}

	public void setAdjustY(float yAdjust) {
		this.yAdjust = yAdjust;
	}

	public float getSizeAdjust() {
		return sizeAdjust;
	}

	public void setSizeAdjust(float sizeAdjust) {
		this.sizeAdjust = sizeAdjust;
	}

	public boolean isDirections() {
		return directions;
	}

	public void setDirections(boolean directions) {
		this.directions = directions;
	}

	public Waypoint getFocussedWaypoint() {
		return focussedWaypoint;
	}

	public void setFocussedWaypoint(Waypoint focussedWaypoint) {
		this.focussedWaypoint = focussedWaypoint;
	}

	public void removeWaypoint(Waypoint clickedWaypoint) {
		for(List<Waypoint> list:waypoints.values()) {
			list.remove(clickedWaypoint);
		}
	}
	
	public void addWaypoint(Waypoint waypoint) {
		addWaypoint(MinimapUtils.getWorldName(), waypoint);
	}

	public void addWaypoint(String worldName, Waypoint waypoint) {
		List<Waypoint> list = waypoints.get(worldName);
		if(list == null) {
			list = new LinkedList<Waypoint>();
			waypoints.put(worldName, list);
		}
		list.add(waypoint);
	}

	public boolean isDeathpoints() {
		return deathpoints;
	}

	public void setDeathpoints(boolean deathpoints) {
		this.deathpoints = deathpoints;
	}

	public boolean isShowBackground() {
		return background;
	}

	public void setShowBackground(boolean background) {
		this.background = background;
	}
}
