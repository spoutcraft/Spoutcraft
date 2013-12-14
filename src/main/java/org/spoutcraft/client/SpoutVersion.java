/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org/>
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
package org.spoutcraft.client;

public class SpoutVersion {
	private final int major;
	private final int minor;
	private final int build;
	private final int subbuild;

	public SpoutVersion() {
		major = 0;
		minor = 0;
		build = -1;
		subbuild = 0;
	}

	public SpoutVersion(int major, int minor, int build, int subbuild) {
		this.major = major;
		this.minor = minor;
		this.build = build;
		this.subbuild = subbuild;
	}

	public long getVersion() {
		return major * 100 + minor * 10 + build;
	}

	public String toString() {
		return (new StringBuilder("")).append(major).append(".").append(minor).append(".").append(build).append(".").append(subbuild).toString();
	}
}
