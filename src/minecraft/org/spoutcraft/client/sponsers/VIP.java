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
package org.spoutcraft.client.sponsers;

public class VIP {
	private final String username;
	private final String title;
	private final String cape;
	private final String particle;

	public VIP(String username, String title, String cape, String particle) {
		this.username = username;
		this.title = title;
		this.cape = cape;
		this.particle = particle;
	}

	public String getTitle() {
		return title;
	}

	public String getParticle() {
		return particle;
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
