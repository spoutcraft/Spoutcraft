/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spoutcraft.client.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.spoutcraft.api.player.BiomeManager;

public class SimpleBiomeManager implements BiomeManager {
	private HashMap<String,Boolean> changedSnow = new HashMap<String,Boolean>();
	private HashMap<String,Boolean> changedRain = new HashMap<String,Boolean>();
	private List<String> defaultSnow = new ArrayList<String>();
	private List<String> defaultRain = new ArrayList<String>();

	public SimpleBiomeManager() {
		defaultSnow.add("Tundra");
		defaultSnow.add("Taiga");
		defaultSnow.add("Ice Desert");

		defaultRain.add("Desert");
		defaultRain.add("Ice Desert");
		defaultRain.add("Hell");
		defaultRain.add("Sky");
	}

	public void setSnowEnabled(String biome, boolean bool) {
		changedSnow.put(biome, bool);
	}

	public void setRainEnabled(String biome, boolean bool) {
		changedRain.put(biome, bool);
	}

	public boolean getSnowChanged(String biome) {
		boolean bool = false;
		if (changedSnow.containsKey(biome)) {
			bool = true;
		}

		return bool;
	}

	public boolean getRainChanged(String biome) {
		boolean bool = false;
		if (changedRain.containsKey(biome)) {
			bool = true;
		}

		return bool;
	}

	public boolean getSnowEnabled(String biome) {
		boolean bool = false;
		bool = changedSnow.get(biome);

		return bool;
	}

	public boolean getRainEnabled(String biome) {
		boolean bool = false;
		bool = changedRain.get(biome);

		return bool;
	}

	public void resetWeather(String biome) {
		if (defaultSnow.contains(biome)) {
			changedSnow.put(biome, true);
		} else {
			changedSnow.put(biome,false);
		}

		if (defaultRain.contains(biome)) {
			changedRain.put(biome, false);
		} else {
			changedRain.put(biome,true);
		}
	}
}
