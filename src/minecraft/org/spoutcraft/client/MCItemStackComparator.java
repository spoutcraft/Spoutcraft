/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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

import java.util.Comparator;

import net.minecraft.src.ItemStack;

public class MCItemStackComparator implements Comparator {
	public int compare(ItemStack o1, ItemStack o2) {
		int idDiff = o1.itemID - o2.itemID;
		int dataDiff = o1.getItemDamage() - o2.getItemDamage();
		return idDiff * 1000 - dataDiff;
	}

	public int compare(Object o1, Object o2) {
		return compare((ItemStack)o1, (ItemStack)o2);
	}
}
