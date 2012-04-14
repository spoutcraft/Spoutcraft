/*
 * This file is part of SpoutAPI (http://wiki.getspout.org/).
 * 
 * SpoutAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.material.item;

import java.io.IOException;

import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;
import org.spoutcraft.spoutcraftapi.material.Food;

public class GenericCustomFood extends GenericCustomItem implements Food{
	private int hunger;
	public GenericCustomFood(Addon addon, String name, String texture, int hungerRestored) {
		super(addon, name, texture);
		hunger = hungerRestored;
	}
	
	public GenericCustomFood() {
		
	}

	public int getHungerRestored() {
		return hunger;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		hunger = input.read();
	}
	
	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.write(getHungerRestored());
	}
}
