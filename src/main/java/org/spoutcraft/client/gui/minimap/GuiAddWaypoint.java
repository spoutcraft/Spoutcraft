/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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

import org.lwjgl.input.Keyboard;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericTextField;
import org.spoutcraft.api.gui.RenderPriority;
import org.spoutcraft.api.gui.TextField;
import org.spoutcraft.client.SpoutClient;

public class GuiAddWaypoint extends GuiScreen {
	Button done, cancel, delete;
	TextField name;
	private GuiScreen parent;
	private int x,y,z;
	private Waypoint toEdit = null;
	private boolean existed = false;

	public GuiAddWaypoint(GuiScreen parent, int x, int y, int z) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.z = z;
		toEdit = new Waypoint("", x, y, z, true);
		existed = false;
	}

	public GuiAddWaypoint(GuiScreen parent, Waypoint edit) {
		this.parent = parent;
		this.toEdit = edit;
		x = edit.x;
		y = edit.y;
		z = edit.z;
		existed = true;
	}

	public void initGui() {
		GenericLabel label = new GenericLabel("Create Waypoint");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(10);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget("Spoutcraft", label);

		int left = (int)(width / 2  - 155);
		int right = (int)(width / 2 + 5);

		label = new GenericLabel("Waypoint Name:");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX(left).setY(70);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget("Spoutcraft", label);

		name = new GenericTextField();
		name.setHeight(20).setWidth(300).setX(left).setY(81);
		name.setMaximumCharacters(0);
		name.setFixed(true).setPriority(RenderPriority.Lowest);
		name.setText(toEdit.name);
		getScreen().attachWidget("Spoutcraft", name);

		String text = "(" + x + ", " + y + ", " + z + ")";
		if (!SpoutClient.getInstance().isCoordsCheat()) {
			text = "Coords not shown";
		}
		label = new GenericLabel(text);
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(106);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget("Spoutcraft", label);

		done = new GenericButton("Create");
		if (existed) {
			done.setText("Save");
		}
		done.setWidth(150).setHeight(20).setX(right).setY(200);
		getScreen().attachWidget("Spoutcraft", done);

		cancel = new GenericButton("Cancel");
		cancel.setWidth(150).setHeight(20).setX(left).setY(200);
		getScreen().attachWidget("Spoutcraft", cancel);

		if (existed) {
			delete = new GenericButton("Delete");
			delete.setGeometry(left, 175, 150, 20);
			getScreen().attachWidget("Spoutcraft", delete);
		}
	}

	@Override
	public void drawScreen(int x, int y, float z) {
		drawDefaultBackground();
		name.setFocus(true);
		done.setEnabled(name.getText().length() > 0);
		super.drawScreen(x, y, z);
	}

	@Override
	protected void keyTyped(char ch, int keycode) {
		if (keycode == Keyboard.KEY_RETURN) {
			buttonClicked(done);
		} else {
			super.keyTyped(ch, keycode);
		}
	}

	@Override
	protected void buttonClicked(Button btn) {
		if (existed && btn == delete) {
			MinimapConfig.getInstance().removeWaypoint(toEdit);
			MinimapConfig.getInstance().save();
			SpoutClient.getHandle().displayGuiScreen(parent);
			if (toEdit == MinimapConfig.getInstance().getFocussedWaypoint()) {
				MinimapConfig.getInstance().setFocussedWaypoint(null);
			}
		}
		if (btn.equals(done) && done.isEnabled()) {
			toEdit.name = name.getText();
			if (!existed) {
				MinimapConfig.getInstance().addWaypoint(MinimapUtils.getWorldName(), toEdit);
			}
			MinimapConfig.getInstance().save();
			SpoutClient.getHandle().displayGuiScreen(parent);
		}
		if (btn.equals(cancel)) {
			mc.displayGuiScreen(parent);
		}
	}
}
