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

import org.spoutcraft.spoutcraftapi.material.MaterialData;

import net.minecraft.src.ItemStack;

public class MCItemStackComparator implements Comparator {
	final boolean byName;
	public MCItemStackComparator() {
		this(false);
	}
	public MCItemStackComparator(boolean byName) {
		this.byName = byName;
	}
	public int compare(ItemStack o1, ItemStack o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		if (o1 != null && o2 == null) {
			return o1.itemID;
		}
		if (o2 != null && o1 == null) {
			return -o2.itemID;
		}
		if (byName) {
			org.spoutcraft.spoutcraftapi.material.Material other1 = MaterialData.getMaterial(o1.itemID, (short)(o1.getItemDamage()));
			org.spoutcraft.spoutcraftapi.material.Material other2 = MaterialData.getMaterial(o2.itemID, (short)(o2.getItemDamage()));
			if (other1 == null && other2 == null) {
				return 0;
			}
			if (other1 != null && other2 == null) {
				return o1.itemID;
			}
			if (other2 != null && other1 == null) {
				return -o2.itemID;
			}
			return other1.getName().compareTo(other2.getName());
		}
		else {
			int idDiff = o1.itemID - o2.itemID;
			int dataDiff = o1.getItemDamage() - o2.getItemDamage();
			return idDiff * 1000 - dataDiff;
		}
	}

	public int compare(Object o1, Object o2) {
		return compare((ItemStack)o1, (ItemStack)o2);
	}
}
