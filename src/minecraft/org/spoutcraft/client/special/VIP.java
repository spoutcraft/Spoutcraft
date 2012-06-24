/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

public class VIP {
	private final String username;
	private final String title;
	private final String cape;
	private final String particles;

	public VIP(String username, String title, String cape, String particles) {
		this.username = username;
		this.title = title;
		this.cape = cape;
		this.particles = particles;
	}

	public String getTitle() {
		return title;
	}

	public String getParticles() {
		return particles;
	}

	public String getCape() {
		return cape;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof VIP) {
			VIP other = (VIP) obj;
			if (other.username.equals(username)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return username.hashCode();
	}

	public String getUsername() {
		return username;
	}
}
