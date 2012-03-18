/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.gui.controls;

import org.spoutcraft.client.SpoutClient;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;

public class SpoutcraftBindingItem extends ControlsBasicItem {
	protected KeyBinding binding;
	private ControlsModel parent;
	public SpoutcraftBindingItem(KeyBinding binding, ControlsModel model) {
		super(model);
		this.binding = binding;
		this.parent = model;
	}

	public int getHeight() {
		return 11;
	}

	public void render(int x, int y, int width, int height) {
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		font.drawStringWithShadow("S", x+2, y+2, 0xffff0000);
		int w = font.getStringWidth("S");
		font.drawStringWithShadow(getName(), x+w+4, y+2, !isConflicting()?0xffffffff:0xffff0000);
		String keyString = parent.getEditingItem() == this?"> <": GameSettings.getKeyDisplayString(getKey());
		w = font.getStringWidth(keyString);
		font.drawStringWithShadow(keyString, width - w, y+2, 0xffcccccc);
	}

	@Override
	public void setKey(int id) {
		binding.keyCode = id;
		SpoutClient.getHandle().gameSettings.saveOptions();
		KeyBinding.resetKeyBindingArrayAndHash();
	}

	@Override
	public int getKey() {
		return binding.keyCode;
	}

	public boolean useModifiers() {
		return false;
	}

	public boolean useMouseButtons() {
		return true;
	}

	public String getName() {
		return binding.keyDescription;
	}
	
	@Override
	public boolean conflicts(ControlsBasicItem other) {
		if (other instanceof VanillaBindingItem) {
			GameSettings settings = SpoutClient.getHandle().gameSettings;
			VanillaBindingItem item = (VanillaBindingItem)other;
			boolean flightKey = binding == settings.keyFlyBack ||
				binding == settings.keyFlyLeft ||
				binding == settings.keyFlyForward ||
				binding == settings.keyFlyRight ||
				binding == settings.keyFlyUp ||
				binding == settings.keyFlyDown;
			boolean movementKey = item.binding == settings.keyBindBack ||
				item.binding == settings.keyBindLeft ||
				item.binding == settings.keyBindForward ||
				item.binding == settings.keyBindRight ||
				item.binding == settings.keyBindJump ||
				item.binding == settings.keyBindSneak;
			//Allow overlaps
			if (flightKey && movementKey) {
				return false;
			}
		}
		return super.conflicts(other);
	}
}
