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
package org.spoutcraft.api.command;

/**
 * Represents a command that delegates to one or more other commands
 */
public class MultipleCommandAlias extends Command {
	private Command[] commands;

	public MultipleCommandAlias(String name, Command[] commands) {
		super(name);
		this.commands = commands;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		boolean result = false;

		for (Command command : commands) {
			result |= command.execute(sender, commandLabel, args);
		}

		return result;
	}
}
