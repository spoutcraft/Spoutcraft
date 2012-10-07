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
package org.spoutcraft.api.gui;

public interface BasicLabel extends Label {
	/**
	 * Recalculates the word wrapping result.
	 */
	public void recalculateLines();

	/**
	 * @return the lines of the text. If wordwrapping is enabled, this will already be wrapped to width.
	 */
	public String [] getLines();

	/**
	 * Enables word wrapping. Right now, this will wrap for spaces when a line is too big to be displayed in the width.
	 * If a single word is longer than the width, it will be cut off by char.
	 * @param wrapLines wether to enable the feature.
	 * @return instance of the label
	 */
	public Label setWrapLines(boolean wrapLines);

	/**
	 * @return if this label has word wrapping enabled.
	 */
	public boolean isWrapLines();
}
