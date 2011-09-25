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
package org.spoutcraft.spoutcraftapi.addon;

import org.spoutcraft.spoutcraftapi.command.CommandExecutor;

public abstract interface Addon extends CommandExecutor {

	public abstract AddonDescriptionFile getDescription();

	public abstract void onEnable();

	public abstract void onDisable();

	public abstract void onLoad();

	public abstract AddonLoader getAddonLoader();

	public abstract boolean isEnabled();

	public abstract void setEnabled(boolean arg);

	public abstract boolean isNaggable();

	public abstract void setNaggable(boolean b);

	public enum Mode {
		SINGLE_PLAYER,
		MULTIPLAYER,
		BOTH;
	}

}
