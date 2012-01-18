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
import org.spoutcraft.client.controls.Shortcut;
import org.spoutcraft.client.controls.SimpleKeyBindingManager;

public class ShortcutBindingItem extends ControlsBasicItem {
	private Shortcut shortcut;
	private ControlsModel parent;
	private SimpleKeyBindingManager manager = (SimpleKeyBindingManager) SpoutClient.getInstance().getKeyBindingManager();
	public ShortcutBindingItem(Shortcut sh, ControlsModel model) {
		super(model);
		this.parent = model;
		shortcut = sh;
	}

	public int getHeight() {
		return 11;
	}

	public void render(int x, int y, int width, int height) {
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		font.drawStringWithShadow("S", x+2, y+2, 0xff00ff00);
		int w = font.getStringWidth("S");
		font.drawStringWithShadow(getName(), x+w+4, y+2, !isConflicting()?0xffffffff:0xffff0000);
		font.drawStringWithShadow(parent.getEditingItem() == this?"> <":shortcut.toString(), x + width / 2, y+2, 0xffcccccc);
	}

	@Override
	public void setKey(int id) {
		shortcut.setKey(id);
		manager.updateBindings();
		manager.save();
	}

	@Override
	public int getKey() {
		return shortcut.getKey();
	}

	@Override
	public boolean useModifiers() {
		return true;
	}

	@Override
	public boolean useMouseButtons() {
		return false;
	}

	@Override
	public String getName() {
		return shortcut.getTitle();
	}

	@Override
	public void setModifiers(int m) {
		shortcut.setRawModifiers((byte) m);
	}

	@Override
	public int getModifiers() {
		return shortcut.getModifiers();
	}

	public Shortcut getShortcut() {
		return shortcut;
	}
}
