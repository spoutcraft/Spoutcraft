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

import net.minecraft.src.FontRenderer;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.controls.SimpleKeyBindingManager;
import org.spoutcraft.client.gui.MCRenderDelegate;

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
		MCRenderDelegate r = (MCRenderDelegate) SpoutClient.getInstance().getRenderDelegate();
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		font.drawStringWithShadow("B", x + 2, y + 2, 0xff0000ff);
		int w = font.getStringWidth("B");
		String keyString = parent.getEditingItem() == this ? "> <" : binding.toString();
		int w2 = font.getStringWidth(keyString);
		font.drawStringWithShadow(keyString, width - w2, y + 2, 0xffcccccc);
		font.drawStringWithShadow(binding.getAddonName(), x + w + 4, y + 11, 0xffffffff);
		String fitting = r.getFittingText(getName(), width - w - w2 - 4);

		font.drawStringWithShadow(fitting, x + w + 4, y + 2, !isConflicting() ? 0xffffffff : 0xffff0000);
	}

	@Override
	public void setModifiers(int m) {
		binding.setRawModifiers((byte) m);
	}

	@Override
	public int getModifiers() {
		return binding.getModifiers();
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
		return true;
	}

	@Override
	public boolean useMouseButtons() {
		return true;
	}

	@Override
	public String getName() {
		return binding.getDescription();
	}

	public org.spoutcraft.spoutcraftapi.keyboard.KeyBinding getBinding() {
		return binding;
	}
}
