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
package org.spoutcraft.client.gui.chat;

import java.util.List;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.GuiSpoutScreen;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericListWidget;
import org.spoutcraft.spoutcraftapi.gui.GenericListWidgetItem;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;
import org.spoutcraft.spoutcraftapi.gui.Label;
import org.spoutcraft.spoutcraftapi.gui.ListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;
import org.spoutcraft.spoutcraftapi.gui.TextField;

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
		for(String item:items) {
			list.addItem(new ListItem(item));
		}
		editor = new GenericTextField();
		editor.setPlaceholder("Enter new item ...");
		
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		getScreen().attachWidgets(spoutcraft, title, helpLabel, buttonDone, buttonAdd, buttonEdit, buttonRemove, list, editor);
		
		updateButtons();
	}

	@Override
	protected void layoutWidgets() {
		title.setX(width / 2 - SpoutClient.getHandle().fontRenderer.getStringWidth(title.getText()) / 2);
		title.setY(5);
		
		int top = 5 + 13;
		
		editor.setGeometry(5, top, 250, 20);
		buttonAdd.setGeometry(260, top, 100, 20);
		
		top += 25;
		
		list.setGeometry(0, top, width, height - top - (2 + 11 + 2 + 20 + 5));
		list.updateSize();
		
		top += (int) list.getHeight() + 2;
		
		helpLabel.setGeometry(5, top, width - 10, 11);
		
		top += 13;
		
		int totalWidth = Math.min(width - 9, 200*3+10);
		int cellWidth = (totalWidth - 10)/3;
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
		
		@Override
		public void setListWidget(ListWidget widget) {
			this.widget = widget;
		}

		@Override
		public ListWidget getListWidget() {
			return widget;
		}

		@Override
		public int getHeight() {
			return 13;
		}

		@Override
		public void render(int x, int y, int width, int height) {
			Spoutcraft.getMinecraftFont().drawString(text, x + 2, y + 2, 0xffffffff);
		}

		@Override
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
