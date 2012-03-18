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
package org.spoutcraft.client.gui.minimap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.util.config.Configuration;
import org.spoutcraft.client.io.FileUtil;

public class MinimapConfig {
	private static MinimapConfig instance;
	private final Configuration config;
	
	private boolean enabled = true;
	private boolean coords = true;
	private int zoom = 2;
	private boolean color = true;
	private boolean square = false;
	private boolean lightmap = false;
	private boolean heightmap = true;
	private boolean cavemap = false;
	private boolean firstrun = true;
	private final Map<String, List<Waypoint>> waypoints = new HashMap<String, List<Waypoint>>();
	
	/*
	minimap:
		enabled: true
		...
	waypoints:
		world1:
			home:
				x: 128
				z: -82
				enabled: 1
			work:
				...
	*/
	private MinimapConfig() {
		config = new Configuration(new File(new File(FileUtil.getSpoutcraftDirectory(), "spoutcraft"), "minimap.yml"));
		config.load();
		enabled = config.getBoolean("minimap.enabled", enabled);
		coords = config.getBoolean("minimap.coords", coords);
		zoom = config.getInt("minimap.zoom", zoom);
		color = config.getBoolean("minimap.color", color);
		square = config.getBoolean("minimap.square", square);
		lightmap = config.getBoolean("minimap.lightmap", lightmap);
		heightmap = config.getBoolean("minimap.heightmap", heightmap);
		cavemap = config.getBoolean("minimap.cavemap", cavemap);
		firstrun = config.getBoolean("minimap.firstrun", firstrun);
		Map<?, ?> worlds = config.getNodes("waypoints");
		if (worlds != null) {
			Iterator<?> i = worlds.entrySet().iterator();
			while (i.hasNext()) {
				Object o = i.next();
				if (o instanceof Entry) {
					Entry<?, ?> e = (Entry)o;
					if (e.getKey() instanceof String && e.getValue() instanceof Map<?, ?>) {
						try {
							String world = (String)e.getKey();
							Map<String, Map<String, Number>> waypoints = (Map<String, Map<String, Number>>) e.getValue();
							Iterator<Entry<String, Map<String, Number>>> j = waypoints.entrySet().iterator();
							while (i.hasNext()) {
								Entry<String, Map<String, Number>> entry = j.next();
								int x, z;
								String name = entry.getKey();
								Map<String, Number> locations = entry.getValue();
								x = locations.get("x").intValue();
								z = locations.get("z").intValue();
								enabled = locations.get("enabled").intValue() == 1;
								addWaypoint(world, name, x, z, enabled);
							}
						}
						catch (Exception ex) {
							System.err.println("Error while reading waypoints: ");
							ex.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	public static void initialize() {
		if (instance == null) {
			instance = new MinimapConfig();
		}
		else {
			throw new IllegalStateException("Minimap config can not be initialized twice");
		}
	}
	
	public void save() {
		config.setProperty("minimap.enabled", enabled);
		config.setProperty("minimap.coords", coords);
		config.setProperty("minimap.zoom", zoom);
		config.setProperty("minimap.color", color);
		config.setProperty("minimap.square", square);
		config.setProperty("minimap.lightmap", lightmap);
		config.setProperty("minimap.heightmap", heightmap);
		config.setProperty("minimap.cavemap", cavemap);
		config.setProperty("minimap.firstrun", firstrun);
		HashMap<String, Map<String, Map<String, Integer>>> worlds = new HashMap<String, Map<String, Map<String, Integer>>>();
		Iterator<Entry<String, List<Waypoint>>> i = waypoints.entrySet().iterator();
		while (i.hasNext()) {
			Entry<String, List<Waypoint>> e = i.next();
			String world = e.getKey();
			HashMap<String, Map<String, Integer>> waypoints = new HashMap<String, Map<String, Integer>>();
			for (Waypoint waypoint : e.getValue()) {
				HashMap<String, Integer> values = new HashMap<String, Integer>();
				values.put("x", waypoint.x);
				values.put("z", waypoint.z);
				values.put("enabled", waypoint.enabled ? 1 : 0);
				waypoints.put(waypoint.name, values);
			}
			worlds.put(world, waypoints);
		}
		config.setProperty("waypoints", worlds);
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
		return coords;
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

	public List<Waypoint> getWaypoints(String world) {
		List<Waypoint> list = waypoints.get(world);
		if (list == null) {
			list = new ArrayList<Waypoint>();
			waypoints.put(world, list);
		}
		return list;
	}
	
	public void removeWaypoint(String world, String name) {
		Iterator<Waypoint> i = getWaypoints(world).iterator();
		while (i.hasNext()) {
			if (i.next().name.equalsIgnoreCase(name)) {
				i.remove();
			}
		}
	}
	
	public void addWaypoint(String world, String name, int x, int z, boolean enabled) {
		getWaypoints(world).add(new Waypoint(name, x, z, enabled));
	}
}
