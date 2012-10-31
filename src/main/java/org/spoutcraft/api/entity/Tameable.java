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
package org.spoutcraft.api.entity;

public interface Tameable {
	/**
	 * Check if this is tamed
	 *
	 * If something is tamed then a player can not tame it through normal methods, even if it does not belong to anyone in particular.
	 *
	 * @return true if this has been tamed
	 */
	public boolean isTamed();

	/**
	 * Sets if this has been tamed. Not necessary if the method setOwner has been used, as it tames automatically.
	 *
	 * If something is tamed then a player can not tame it through normal methods, even if it does not belong to anyone in particular.
	 *
	 * @param tame true if tame
	 */
	public void setTamed(boolean tame);

	/**
	 * Gets the current owning AnimalTamer
	 *
	 * @return the owning AnimalTamer, or null if not owned
	 */
	public AnimalTamer getOwner();

	/**
	 * Set this to be owned by given AnimalTamer.
	 * If the owner is not null, this will be tamed and will have any current path it is following removed.
	 * If the owner is set to null, this will be untamed, and the current owner removed.
	 *
	 * @param tamer the AnimalTamer who should own this
	 */
	public void setOwner(AnimalTamer tamer);
}
