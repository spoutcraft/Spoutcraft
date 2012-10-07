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
package org.spoutcraft.client.gui.minimap;

import net.minecraft.src.Entity;

import org.spoutcraft.api.gui.CheckBox;
import org.spoutcraft.api.gui.GenericCheckBox;

public class EntityVisibilityCheckbox extends GenericCheckBox {
	private Class<? extends Entity> clazz;
	private boolean save = true;

	public EntityVisibilityCheckbox(Class<? extends Entity> clazz, String texture) {
		this.clazz = clazz;
		String name = texture.substring(0, texture.length() - 4).replaceAll("_", " ").trim();
		setText(name);
		save = false;
		setChecked(MinimapConfig.getInstance().isEntityVisible(clazz));
		save = true;
	}

	@Override
	public CheckBox setChecked(boolean checked) {
		if (save) {
			MinimapConfig.getInstance().setEntityVisible(clazz, checked);
			MinimapConfig.getInstance().save();
		}
		return super.setChecked(checked);
	}
}
