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
package org.spoutcraft.client.gui.chat;

import java.util.List;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericListWidget;
import org.spoutcraft.api.gui.GenericListWidgetItem;
import org.spoutcraft.api.gui.GenericTextField;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.api.gui.ListWidget;
import org.spoutcraft.api.gui.ListWidgetItem;
import org.spoutcraft.api.gui.TextField;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.GuiSpoutScreen;

public class GuiListEdit extends GuiSpoutScreen {
	private Label title, helpLabel;
	private Button buttonDone, buttonAdd, buttonEdit, buttonRemove;
	private ListWidget list;
	private TextField editor;

	private List<String> items;

	private GuiScreen parent;

	private Runnable onSave;

	private int editing = -1;
	private ListItem editingItem = null;

	public GuiListEdit(Runnable onSave, String titleText, String helpText, GuiScreen parent, List<String> items) {
		this.items = items;
		this.parent = parent;
		title = new GenericLabel(titleText);
		helpLabel = new GenericLabel(helpText);
		this.onSave = onSave;
	}

	@Override
	protected void createInstances() {
		buttonDone = new GenericButton("Done");
		buttonAdd = new GenericButton("Add");
		buttonEdit = new GenericButton("Edit");
		buttonRemove = new GenericButton("Remove");
		list = new GenericListWidget();
		for (String item:items) {
			list.addItem(new ListItem(item));
		}
		editor = new GenericTextField();
		editor.setPlaceholder("Enter new item ...");
		editor.setMaximumCharacters(0);

		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		getScreen().attachWidgets(spoutcraft, title, helpLabel, buttonDone, buttonAdd, buttonEdit, buttonRemove, list, editor);

		updateButtons();
	}

	@Override
	protected void layoutWidgets() {
		title.setX(width / 2 - SpoutClient.getHandle().fontRenderer.getStringWidth(title.getText()) / 2);
		title.setY(5);

		int top = 5 + 13;

		editor.setGeometry(5, top + 1, width - 115, 18);
		buttonAdd.setGeometry(width - 105, top, 100, 20);

		top += 25;

		list.setGeometry(0, top, width, height - top - 30);
		list.updateSize();

		top += (int) list.getHeight();

		helpLabel.setGeometry(5, top, width - 10, 11);

		top += 5;

		int totalWidth = Math.min(width - 9, 200 * 3 + 10);
		int cellWidth = (totalWidth - 10) / 3;
		int left = width / 2 - totalWidth / 2;
		int center = left + cellWidth + 5;
		int right = center + cellWidth + 5;

		buttonEdit.setGeometry(left, top, cellWidth, 20);
		buttonRemove.setGeometry(center, top, cellWidth, 20);
		buttonDone.setGeometry(right, top, cellWidth, 20);
	}

	@Override
	protected void buttonClicked(Button btn) {
		if (btn == buttonDone) {
			if (onSave != null) {
				onSave.run();
			}
			mc.displayGuiScreen(parent);
			return;
		}

		ListItem listItem = (ListItem) list.getSelectedItem();
		if (btn == buttonRemove && listItem != null) {
			items.remove(listItem.getText());
			list.removeItem(listItem);
			if (onSave != null) {
				onSave.run();
			}
			list.setSelection(-1);
		}
		if (btn == buttonEdit && listItem != null) {
			editing = items.indexOf(listItem.getText());
			editor.setText(listItem.getText());
			editor.setPlaceholder("Enter new text for item");
			buttonAdd.setText("Save");
			editingItem = listItem;
		}
		if (btn == buttonAdd) {
			if (editing != -1) {
				items.remove(editing);
				items.add(editing, editor.getText());
				editingItem.setText(editor.getText());
			} else {
				items.add(editor.getText());
				list.addItem(new ListItem(editor.getText()));
			}
			if (onSave != null) {
				onSave.run();
			}

			editingItem = null;
			editing = -1;

			buttonAdd.setText("Add");
			editor.setPlaceholder("Add new item ...");
			editor.setText("");
		}
	}

	protected class ListItem implements ListWidgetItem {
		private String text;
		private ListWidget widget;

		public ListItem(String text) {
			this.text = text;
		}

		public void setListWidget(ListWidget widget) {
			this.widget = widget;
		}

		public ListWidget getListWidget() {
			return widget;
		}

		public int getHeight() {
			return 13;
		}

		public void render(int x, int y, int width, int height) {
			Spoutcraft.getMinecraftFont().drawString(text, x + 2, y + 2, 0xffffffff);
		}

		public void onClick(int x, int y, boolean doubleClick) {
			updateButtons();
		}

		String getText() {
			return text;
		}

		void setText(String text) {
			this.text = text;
		}
	}

	public void updateButtons() {
		ListItem listItem = (ListItem) list.getSelectedItem();
		boolean enable = true;
		if (listItem == null) {
			enable = false;
		}
		buttonEdit.setEnabled(enable);
		buttonRemove.setEnabled(enable);

		buttonAdd.setEnabled(!editor.getText().isEmpty());
	}

	@Override
	public void updateScreen() {
		buttonAdd.setEnabled(!editor.getText().isEmpty());
		super.updateScreen();
	}
}
