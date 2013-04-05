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
package org.spoutcraft.client.gui.controls;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericTextField;
import org.spoutcraft.api.gui.GenericTextProcessor;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.api.gui.TextField;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.controls.Shortcut;
import org.spoutcraft.client.controls.SimpleKeyBindingManager;

public class GuiEditShortcut extends GuiScreen {
	GuiControls parent;
	Shortcut item;
	Button recordButton, doneButton, addButton, editButton, removeButton;
	Label titleLabel, recordLabel, commandLabel, typeNameHereLabel, typeCommandHereLabel, delayLabel;
	TextField commandName, commandText, delayText;
	GuiCommandsSlot slot;
	int editingIndex = -1;
	Color grey = new Color(0xC0C0C0);

	boolean recording = false;
	public GuiEditShortcut(GuiControls guiControls, Shortcut item) {
		this.parent = guiControls;
		this.item = item;
	}

	private static Color white = new Color(1f, 1f, 1f, 1f), red = new Color(1f, 0f, 0f, 1f);

	@Override
	public void updateScreen() {
		addButton.setEnabled(commandName.getText().length() != 0 && commandText.getText().length() != 0);
		titleLabel.setTextColor(commandName.getText().length() == 0 ? red:white);
		delayLabel.setTextColor(delayText.getText().length() == 0 ? red:white);
		recordButton.setTextColor(item.getKey() <= 0 ? red:white);
		commandLabel.setTextColor(item.getCommands().size() == 0 ? red:white);
		super.updateScreen();
	}

	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
		super.drawScreen(var1, var2, var3);
	}

	protected void keyTyped(char c, int i) {
		if (recording && !SimpleKeyBindingManager.isModifierKey(i)) {
			item.setRawModifiers(SimpleKeyBindingManager.getPressedModifiers());
			item.setKey(i);
			recording = false;
			updateRecordButton();
		} else {
			super.keyTyped(c, i);
		}
	}

	public void initGui() {
		int labelWidth = 75;
		int top = 10;
		int left = 10;
		int right = left + labelWidth + 10;
		int labelHeight = 20;

		recordButton = new GenericButton();
		recordButton.setGeometry(width - 160, top - 3, 150, labelHeight);
		getScreen().attachWidget("Spoutcraft", recordButton);
		updateRecordButton();

		titleLabel = new GenericLabel("Name:");
		titleLabel.setGeometry(left, top + 3, labelWidth, labelHeight);
		getScreen().attachWidget("Spoutcraft", titleLabel);

		commandName = new GenericTextField();
		commandName.setGeometry(right, top - 1, (int) (width - right - recordButton.getWidth() - 20), 16);
		commandName.setText(item.getTitle());
		commandName.setMaximumCharacters(0);
		commandName.setPlaceholder("Enter a name here");
		getScreen().attachWidget("Spoutcraft", commandName);

		top += 23;

		commandLabel = new GenericLabel("Command:");
		commandLabel.setGeometry(left, top + 3, labelWidth, labelHeight);
		getScreen().attachWidget("Spoutcraft", commandLabel);

		commandText = new GenericTextField();
		commandText.setGeometry(right, top - 1, width - right - 10, 16);
		commandText.setMaximumCharacters(0);
		commandText.setPlaceholder("Enter new command here, then click \"Add Command\"");
		getScreen().attachWidget("Spoutcraft", commandText);

		top += 23;

		delayLabel = new GenericLabel("Delay (ms)");
		delayLabel.setGeometry(left, top + 3, labelWidth, labelHeight);
		getScreen().attachWidget("Spoutcraft", delayLabel);

		delayText = new GenericTextField();
		delayText.setGeometry(right, top - 1, width - right - 10, 16);
		delayText.setText(item.getDelay() + "");
		delayText.setTextProcessor(new GenericTextProcessor() {
			protected boolean insert(char c) {
				if (c >= '0' && c <= '9') {
					return super.insert(c);
				}
				return false;
			};

			@Override
			protected boolean insert(String s) {
				for (int i = 0; i < s.length(); i++) {
					char c = s.charAt(i);
					if (!(c >= '0' && c <= '9')) {
						return false;
					}
				}
				return super.insert(s);
			}
		});
		getScreen().attachWidget("Spoutcraft", delayText);

		top += 23;

		slot = new GuiCommandsSlot(this);
		slot.setGeometry(0, top, width, this.height - top - 30);
		getScreen().attachWidget("Spoutcraft", slot);

		doneButton = new GenericButton("Done");
		doneButton.setHeight(20).setWidth(50);
		doneButton.setX(10).setY(height - 25);
		getScreen().attachWidget("Spoutcraft", doneButton);

		addButton = new GenericButton("Add Command");
		addButton.setHeight(20).setWidth(100);
		addButton.setX(70).setY(height - 25);
		getScreen().attachWidget("Spoutcraft", addButton);

		editButton = new GenericButton("Edit Command");
		editButton.setHeight(20).setWidth(100);
		editButton.setX(180).setY(height - 25);
		getScreen().attachWidget("Spoutcraft", editButton);

		removeButton = new GenericButton("Remove Command");
		removeButton.setHeight(20).setWidth(100);
		removeButton.setX(290).setY(height - 25);
		getScreen().attachWidget("Spoutcraft", removeButton);

		updateButtons();

		commandName.setFocus(true);
	}

	private void updateRecordButton() {
		String keyname = recording ? "Press a key!" : "Click Here!";
		if ((item.getKey()>=0 || item.getKey()<-1 )&& !recording) {
			keyname = "Key: " + item.toString();
		}
		String name = (recording ? "> " : "") + keyname + (recording ? " <" : "");
		recordButton.setText(name);
	}

	@Override
	protected void buttonClicked(Button btn) {
		if (btn.equals(recordButton)) {
			recording = !recording;
			updateRecordButton();
		}
		if (btn.equals(doneButton)) {
			item.setTitle(commandName.getText());
			item.setDelay(Integer.parseInt(delayText.getText()));
			if (!item.getTitle().equals("") && item.getKey() != -1) {
				SimpleKeyBindingManager manager = (SimpleKeyBindingManager) SpoutClient.getInstance().getKeyBindingManager();
				manager.unregisterShortcut(item);
				manager.registerShortcut(item);
			}
			mc.displayGuiScreen(parent);
			parent.getModel().refresh();
		}
		if (btn.equals(addButton)) {
			if (editingIndex != -1) {
				addButton.setText("Add Command");
				item.setTitle(commandName.getText());
				item.getCommands().set(editingIndex, commandText.getText());
				editingIndex = -1;
			} else {
				item.addCommand(commandText.getText());
			}

			commandText.setText("");
			slot.updateItems();
			updateButtons();
		}
		if (btn.equals(editButton)) {
			editCommand(slot.getSelectedRow());
			updateButtons();
		}
		if (btn.equals(removeButton)) {
			item.removeCommand(slot.getSelectedRow());
			slot.updateItems();
			updateButtons();
		}
	}

	public void editCommand(int index) {
		if (index != -1) {
			editingIndex = index;
			commandText.setText(item.getCommands().get(index));
			addButton.setText("Finish Editing");
		}
	}

	public void updateButtons() {
		editButton.setEnabled(slot.getSelectedRow() != -1);
		removeButton.setEnabled(slot.getSelectedRow() != -1 && editingIndex == -1);
	}

	public Shortcut getShortcut() {
		return item;
	}

	@Override
	protected void mouseClicked(int x, int y, int button) {
		if (recording) {
			System.out.println("Set mouse button to shortcut " + button);
			item.setRawModifiers(SimpleKeyBindingManager.getPressedModifiers());
			item.setKey(button + SimpleKeyBindingManager.MOUSE_OFFSET);
			recording = false;
			updateRecordButton();
		} else {
			super.mouseClicked(x, y, button);
		}
	}
}
