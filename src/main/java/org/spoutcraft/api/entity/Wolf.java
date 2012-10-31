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

/**
 * Represents a Wolf
 */
public interface Wolf extends Animals, Tameable {
	/**
	 * Checks if this wolf is angry
	 *
	 * @return Anger true if angry
	 */
	public boolean isAngry();

	/**
	 * Sets the anger of this wolf
	 * An angry wolf can not be fed or tamed, and will actively look for targets to attack.
	 *
	 * @param angry true if angry
	 */
	public void setAngry(boolean angry);

	/**
	 * Checks if this wolf is sitting
	 *
	 * @return true if sitting
	 */
	public boolean isSitting();

	/**
	 * Sets if this wolf is sitting
	 * Will remove any path that the wolf was following beforehand.
	 *
	 * @param sitting true if sitting
	 */
	public void setSitting(boolean sitting);
}
