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
package org.spoutcraft.client.gui.controls;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;

import org.spoutcraft.client.SpoutClient;

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
		font.drawStringWithShadow("S", x + 2, y + 2, 0xffff0000);
		int w = font.getStringWidth("S");
		font.drawStringWithShadow(getName(), x + w + 4, y + 2, !isConflicting() ? 0xffffffff:0xffff0000);
		String keyString = parent.getEditingItem() == this ? "> <" : getDisplayKey();
		w = font.getStringWidth(keyString);
		font.drawStringWithShadow(keyString, width - w, y + 2, 0xffcccccc);
	}

	public String getDisplayKey() {
		if (binding.keyCode == -128) {
			return "Unbound";
		}
		return GameSettings.getKeyDisplayString(getKey());
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
			// Allow overlaps
			if (flightKey && movementKey) {
				return false;
			}
		}
		return super.conflicts(other);
	}
}
