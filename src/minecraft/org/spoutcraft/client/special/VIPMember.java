/*
 * This file is part of Vanilla.
 *
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.special;



public class VIPMember {
	private String name;
	private String cape, title;
	private String particles;
	
	public VIPMember(String name) {
		this.name = name;
		cape = null;
		title = null;
	}

	public boolean hasCape() {
		return cape != null;
	}

	public String getCape() {
		return cape;
	}

	public boolean hasTitle() {
		return title != null;
	}

	public String getTitle() {
		return title;
	}
	
	public String getName() {
		return name;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCape(String cape) {
		this.cape = cape;
	}

	public void setParticles(String particles) {
		this.particles = particles;
	}
	
	public boolean hasParticles() {
		return this.particles!=null&&!"false".equals(this.particles);
	}
	
	public String getParticles() {
		return this.particles;
	}
}
