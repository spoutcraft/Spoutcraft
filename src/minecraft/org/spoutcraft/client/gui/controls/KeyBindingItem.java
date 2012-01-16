/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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

import org.lwjgl.input.Keyboard;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.controls.SimpleKeyBindingManager;

public class KeyBindingItem extends ControlsBasicItem {
	private org.spoutcraft.spoutcraftapi.keyboard.KeyBinding binding;
	private ControlsModel parent;
	private SimpleKeyBindingManager manager = (SimpleKeyBindingManager) SpoutClient.getInstance().getKeyBindingManager();
	public KeyBindingItem(org.spoutcraft.spoutcraftapi.keyboard.KeyBinding binding, ControlsModel model) {
		super(model);
		this.parent = model;
		this.binding = binding;
	}

	public int getHeight() {
		return 20;
	}

	public void render(int x, int y, int width, int height) {
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		font.drawStringWithShadow("B", x+2, y+2, 0xff0000ff);
		int w = font.getStringWidth("B");
		font.drawStringWithShadow(getName(), x+w+4, y+2, !isConflicting()?0xffffffff:0xffff0000);
		font.drawStringWithShadow(parent.getEditingItem() == this?"> <":Keyboard.getKeyName(getKey()), x + width / 2, y+2, 0xffcccccc);
		font.drawStringWithShadow(binding.getAddonName(), x+w+4, y+11, 0xffffffff);
	}

	@Override
	public void setKey(int id) {
		binding.setKey(id);
		manager.updateBindings();
		manager.save();
	}

	@Override
	public int getKey() {
		return binding.getKey();
	}

	@Override
	public boolean useModifiers() {
		return false;
	}

	@Override
	public boolean useMouseButtons() {
		return false;
	}

	@Override
	public String getName() {
		return binding.getDescription();
	}

	public org.spoutcraft.spoutcraftapi.keyboard.KeyBinding getBinding() {
		return binding;
	}
}