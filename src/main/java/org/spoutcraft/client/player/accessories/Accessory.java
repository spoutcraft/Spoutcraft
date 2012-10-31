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
package org.spoutcraft.client.player.accessories;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModelBiped;

public abstract class Accessory {
	private ModelBiped model;

	public Accessory(ModelBiped model) {
		this.model = model;
	}

	public ModelBiped getModel() {
		return model;
	}

	public void render(EntityPlayer player, float f) {
	}

	public void render(EntityPlayer player, float f, float par2) {
		render(player, f);
	}

	public abstract AccessoryType getType();
}
