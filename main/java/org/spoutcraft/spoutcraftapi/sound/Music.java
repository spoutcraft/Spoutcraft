/*
 * This file is part of Spoutcraft API (http://wiki.getspout.org/).
 * 
 * Spoutcraft API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.sound;

import java.util.HashMap;
import java.util.Map;

public enum Music {
	MINECRAFT_THEME(0, "calm", 0),
	CLARK(1, "calm", 1),
	SWEDEN(2, "calm", 2),
	SUBWOOFER_LULLABY(3, "hal", 0),
	LIVING_MICE(4, "hal", 1),
	HAGGSTROM(5, "hal", 2),
	DANNY(6, "hal", 3),
	KEY(7, "nuance", 0),
	OXYGENE(8, "nuance", 1),
	DRY_HANDS(9, "piano", 0),
	WET_HANDS(10, "piano", 1),
	MICE_ON_VENUS(11, "piano", 2),

	CUSTOM(-1, null, -1), ;

	final int id;
	final String name;
	private final int soundId;
	private static final Map<String, Music> lookupName = new HashMap<String, Music>();
	private static final Map<Integer, Music> lookupId = new HashMap<Integer, Music>();
	private static int last = 0;

	Music(final int id, final String name, final int soundId) {
		this.id = id;
		this.name = name;
		this.soundId = soundId;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getSoundId() {
		return soundId;
	}

	public static Music getMusicFromId(int id) {
		return lookupId.get(id);
	}

	public static Music getMusicFromName(String name) {
		return lookupName.get(name);
	}

	public static int getMaxId() {
		return last;
	}

	static {
		for (Music i : values()) {
			lookupName.put(i.getName() + (1 + i.getSoundId()) + ".ogg", i);
			lookupId.put(i.getId(), i);
			if (i.getId() > last) {
				last = i.getId();
			}
		}
	}
}
