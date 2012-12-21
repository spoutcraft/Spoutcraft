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
package org.spoutcraft.client.precache;

public class PrecacheTuple {
	private String plugin;
	private String version;
	private long crc;

	/**
	 * A tuple for zip -> crc
	 * @param plugin - Name of the plugin
	 * @param version - plugin version
	 * @param crc - crc of the precache file
	 */
	public PrecacheTuple(String plugin, String version, long crc) {
		this.plugin = plugin;
		this.version = version;
		this.crc = crc;
	}

	public String getPlugin() {
		return plugin;
	}

	public String getVersion() {
		return version;
	}

	public long getCrc() {
		return crc;
	}

	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setCrc(long crc) {
		this.crc = crc;
	}
}
