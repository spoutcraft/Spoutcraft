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
package org.spoutcraft.client.special;

public class Holiday {
	private final String name;
	private final long start;
	private final long end;
	private final String cape;
	private final String splash;
	private final boolean particles;

	public Holiday(String name, long start, long end, String cape, String splash, boolean particles) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.cape = cape;
		this.splash = splash;
		this.particles = particles;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public String getCape() {
		return cape;
	}

	public String getSplash() {
		return splash;
	}

	public boolean isParticles() {
		return particles;
	}

	public String getName() {
		return name;
	}
}
