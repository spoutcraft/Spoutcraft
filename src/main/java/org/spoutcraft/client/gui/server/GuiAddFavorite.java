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
package org.spoutcraft.client.gui.server;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericTextField;
import org.spoutcraft.api.gui.Keyboard;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.api.gui.TextField;
import org.spoutcraft.client.SpoutClient;

public class GuiAddFavorite extends GuiScreen {
	private TextField textIp, textTitle;
	private ServerItem item = null;
	private ServerItem toUpdate = null;
	private Label labelTitle, labelIp;
	private boolean update = false;
	private Button buttonDone, buttonCancel, buttonClear;
	private GuiScreen parent;

	public GuiAddFavorite(String server, GuiScreen parent) {
		String splt[] = server.split(":");
		int port = ServerItem.DEFAULT_PORT;
		if (splt.length > 0) {
			server = splt[0];
			if (splt.length > 1) {
				try {
					port = Integer.valueOf(splt[1]);
				} catch (NumberFormatException e) {
					port = ServerItem.DEFAULT_PORT;
				}
			}
		} else {
			server = "";
		}
		item = new ServerItem("", server, port, -1);
		update = false;
		this.parent = parent;
	}

	public GuiAddFavorite(ServerItem toUpdate, GuiScreen parent) {
		item = toUpdate.clone();
		this.toUpdate = toUpdate;
		update = true;
		this.parent = parent;
	}

	@Override
	public void initGui() {
		int top = height / 2 - 101/2;
		int left = width / 2 - 250 / 2;

		updateItem();

		labelTitle = new GenericLabel("Server Name");
		labelTitle.setHeight(11).setWidth(250).setX(left).setY(top);
		getScreen().attachWidget("Spoutcraft", labelTitle);
		top+=13;

		textTitle = new TabField();
		textTitle.setMaximumCharacters(0).setWidth(250).setHeight(20).setX(left).setY(top);
		textTitle.setText(item.getTitle());
		getScreen().attachWidget("Spoutcraft", textTitle);
		top+=25;

		labelIp = new GenericLabel("Server Address");
		labelIp.setHeight(11).setWidth(250).setX(left).setY(top);
		getScreen().attachWidget("Spoutcraft", labelIp);
		top+=13;

		textIp = new TabField();
		textIp.setMaximumCharacters(0);
		textIp.setWidth(250);
		textIp.setHeight(20);
		textIp.setX(left).setY(top);
		getScreen().attachWidget("Spoutcraft", textIp);
		textIp.setText(item.getIp() + (item.getPort() != ServerItem.DEFAULT_PORT ? ":" + item.getPort() : ""));
		top+=25;

		buttonClear = new GenericButton("Clear");
		buttonClear.setWidth(100).setHeight(20).setX(textIp.getX()).setY(top);
		getScreen().attachWidget("Spoutcraft", buttonClear);

		buttonDone = new GenericButton("Done");
		buttonDone.setWidth(200).setHeight(20).setX(width - 205).setY(height - 25);
		getScreen().attachWidget("Spoutcraft", buttonDone);

		buttonCancel = new GenericButton("Cancel");
		buttonCancel.setWidth(200).setHeight(20).setX(width - 205 - 205).setY(height - 25);
		getScreen().attachWidget("Spoutcraft", buttonCancel);

		updateButtons();
	}

	private class TabField extends GenericTextField {
		@Override
		public void onTextFieldChange() {
			updateButtons();
		}

		@Override
		public boolean onKeyPressed(Keyboard key) {
			if (key == Keyboard.KEY_TAB) {
				if (textIp.isFocus()) {
					textIp.setFocus(false);
					textTitle.setFocus(true);
				} else {
					textTitle.setFocus(false);
					textIp.setFocus(true);
				}
				return true;
			}
			return false;
		}

	}

	@Override
	protected void buttonClicked(Button btn) {
		if (btn.equals(buttonDone)) {
			updateItem();
			if (item.getTitle().isEmpty() || item.getIp().isEmpty()) {
				SpoutClient.getHandle().displayGuiScreen(parent);
				return;
			}
			if (update) {
				// Update original item
				toUpdate.update(item);
			} else {
				SpoutClient.getInstance().getServerManager().getFavorites().addServer(item);
			}
			SpoutClient.getInstance().getServerManager().getFavorites().save();
			SpoutClient.getHandle().displayGuiScreen(parent);
		}
		if (btn.equals(buttonCancel)) {
			SpoutClient.getHandle().displayGuiScreen(parent);
		}
		if (btn == buttonClear) {
			textTitle.setText("");
			textIp.setText("");
			item.setTitle("");
			item.setIp("");
			item.setPort(ServerItem.DEFAULT_PORT);
			updateButtons();
		}
	}

	protected void updateButtons() {
		buttonDone.setEnabled(!textIp.getText().isEmpty() && !textTitle.getText().isEmpty());
	}

	private void updateItem() {
		if (textTitle != null) {
			item.setTitle(textTitle.getText());
		}
		if (textIp != null) {
			String split[] = textIp.getText().split(":");
			item.setIp(split[0]);
			try {
				item.setPort(Integer.valueOf(split[1]));
			} catch(Exception e) {
				// Handles both InvalidNumber and OutOfRange exceptions, yay
				item.setPort(ServerItem.DEFAULT_PORT);
			}
		}
	}

	@Override
	public void drawScreen(int a, int b, float f) {
		drawDefaultBackground();
	}
}
