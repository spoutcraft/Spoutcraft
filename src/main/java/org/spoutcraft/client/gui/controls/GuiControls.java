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

import org.spoutcraft.api.ChatColor;
import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.CheckBox;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericListView;
import org.spoutcraft.api.gui.GenericScrollArea;
import org.spoutcraft.api.gui.Keyboard;
import org.spoutcraft.api.gui.Orientation;
import org.spoutcraft.api.gui.ScrollArea;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.controls.Shortcut;
import org.spoutcraft.client.controls.SimpleKeyBindingManager;
import org.spoutcraft.client.gui.ButtonUpdater;
import org.spoutcraft.client.gui.GuiSpoutScreen;
import org.spoutcraft.client.gui.controls.ControlsBasicItem;
import org.spoutcraft.client.gui.controls.KeyBindingItem;
import org.spoutcraft.client.gui.controls.ShortcutBindingItem;

public class GuiControls extends GuiSpoutScreen implements ButtonUpdater {
	private GenericLabel labelTitle, labelDescription;
	private Button buttonDone, buttonAdd, buttonEdit, buttonRemove;
	public CheckBox checkVanilla, checkSpoutcraft, checkCustom, checkShortcuts;
	public ControlsSearch search;
	private ScrollArea filter;
	private GenericListView view;
	private GuiScreen parentScreen;
	private static ControlsModel model = null;

	public static final ChatColor VANILLA_COLOR = ChatColor.YELLOW;
	public static final ChatColor SPOUTCRAFT_COLOR = ChatColor.RED;
	public static final ChatColor CUSTOM_COLOR = ChatColor.BLUE;
	public static final ChatColor SHORTCUTS_COLOR = ChatColor.GREEN;

	public GuiControls(GuiScreen parent) {
		if (model == null) {
			model = new ControlsModel(this);
		}
		this.parentScreen = parent;
	}

	protected void createInstances() {
		labelTitle = new GenericLabel("Controls");
		buttonDone = new GenericButton("Done");
		buttonAdd = new GenericButton("Add Shortcut");
		buttonEdit = new GenericButton("Edit");
		buttonEdit.setTooltip("Edit Shortcut");
		buttonRemove = new DeleteControlButton(this);
		buttonRemove.setTooltip("Remove Shortcut");
		labelDescription = new GenericLabel();
		labelDescription.setText("Double-click an item, then press the key (combination).");
		labelDescription.setWrapLines(true);
		filter = new GenericScrollArea();
		view = new GenericListView(model);
		model.setCurrentGui(this);

		checkVanilla = new ControlsCheckBox(this, VANILLA_COLOR + "Minecraft Bindings");
		checkSpoutcraft = new ControlsCheckBox(this, SPOUTCRAFT_COLOR + "Spoutcraft Bindings");
		checkCustom = new ControlsCheckBox(this, CUSTOM_COLOR + "Custom Bindings");
		checkShortcuts = new ControlsCheckBox(this, SHORTCUTS_COLOR + "Shortcuts");
		search = new ControlsSearch(this);

		filter.attachWidget("Spoutcraft", checkVanilla);
		filter.attachWidget("Spoutcraft", checkSpoutcraft);
		filter.attachWidget("Spoutcraft", checkCustom);
		filter.attachWidget("Spoutcraft", checkShortcuts);

		getScreen().attachWidget("Spoutcraft", search);
		getScreen().attachWidget("Spoutcraft", labelTitle);
		getScreen().attachWidget("Spoutcraft", filter);
		getScreen().attachWidget("Spoutcraft", view);
		getScreen().attachWidget("Spoutcraft", buttonAdd);
		getScreen().attachWidget("Spoutcraft", buttonEdit);
		getScreen().attachWidget("Spoutcraft", buttonRemove);
		getScreen().attachWidget("Spoutcraft", buttonDone);
		getScreen().attachWidget("Spoutcraft", labelDescription);
		updateButtons();
		model.refresh();
	}

	protected void layoutWidgets() {
		int top = 5;

		int swidth = mc.fontRenderer.getStringWidth(labelTitle.getText());
		labelTitle.setY(top + 7).setX(width / 2 - swidth / 2).setHeight(11).setWidth(swidth);

		search.setX(5).setY(top).setWidth(150).setHeight(20);

		top+=25;

		int sheight = height - top - 55;

		filter.setX(5).setY(top).setWidth(130).setHeight(sheight);

		view.setX((int) (5 + filter.getX() + filter.getWidth())).setY(top).setWidth((int) (width - 15 - filter.getWidth())).setHeight(sheight);

		int ftop = 5;
		checkVanilla.setX(5).setY(ftop).setWidth(100).setHeight(20);
		ftop += 25;
		checkSpoutcraft.setX(5).setY(ftop).setWidth(100).setHeight(20);
		ftop += 25;
		checkCustom.setX(5).setY(ftop).setWidth(100).setHeight(20);
		ftop += 25;
		checkShortcuts.setX(5).setY(ftop).setWidth(100).setHeight(20);

		for (Widget w:filter.getAttachedWidgets()) {
			w.setWidth(filter.getViewportSize(Orientation.HORIZONTAL) - 10);
		}
		search.setWidth((int) filter.getWidth());

		top += 5 + view.getHeight();

		int totalWidth = Math.min(width - 10, 200 * 3 + 10);
		int cellWidth = (totalWidth - 10) / 3;
		int left = width / 2 - totalWidth / 2;
		int center = left + 5 + cellWidth;
		int right = center + 5 + cellWidth;

		buttonAdd.setHeight(20).setWidth(cellWidth).setX(left).setY(top);

		buttonEdit.setHeight(20).setWidth(cellWidth).setX(center).setY(top);

		buttonRemove.setHeight(20).setWidth(cellWidth).setX(right).setY(top);

		top+=25;

		labelDescription.setHeight(20).setWidth(cellWidth * 2 + 5).setX(left).setY(top);
		labelDescription.recalculateLines();

		buttonDone.setHeight(20).setWidth(cellWidth).setX(right).setY(top);
	}

	@Override
	protected void buttonClicked(Button btn) {
		if (btn.equals(buttonDone)) {
			mc.displayGuiScreen(parentScreen);
			return;
		}

		if (btn.equals(buttonAdd)) {
			Shortcut sh = new Shortcut();
			sh.setTitle("");
			sh.setKey(Keyboard.KEY_UNKNOWN.getKeyCode());
			editItem(sh);
			return;
		}
		ControlsBasicItem item = model.getItem(view.getSelectedRow());
		ShortcutBindingItem sh = null;
		if (item != null && item instanceof ShortcutBindingItem) {
			sh = (ShortcutBindingItem) item;
		}
		if (sh != null && btn.equals(buttonEdit)) {
			editItem(sh.getShortcut());
		} else if (btn.equals(buttonEdit) && item != null) {
			model.setEditing(item);
		}
	}

	public void editItem(Shortcut item) {
		GuiEditShortcut gui = new GuiEditShortcut(this, item);
		mc.displayGuiScreen(gui);
	}

	public void updateButtons() {
		ControlsBasicItem item = model.getItem(view.getSelectedRow());
		buttonEdit.setEnabled(item != null);
		((DeleteControlButton)buttonRemove).setReallyShown(false);
		//buttonRemove.setEnabled(item instanceof ShortcutBindingItem || item instanceof KeyBindingItem);
	}

	protected void keyTyped(char c, int i, boolean pressed) {
		ControlsBasicItem item = model.getEditingItem();
		if (item != null) {
			if (item.useModifiers() && (!SimpleKeyBindingManager.isModifierKey(i) && pressed) || (SimpleKeyBindingManager.isModifierKey(i) && !pressed)) {
				item.setModifiers(SimpleKeyBindingManager.getPressedModifiers());
				item.setKey(i);
				model.finishEdit();
			} else if (!item.useModifiers()) {
				item.setKey(i);
				model.finishEdit();
			}
		}
	}

	@Override
	public void handleKeyboardInput() {
		ControlsBasicItem item = model.getEditingItem();
		if (item != null) {
			this.keyTyped(org.lwjgl.input.Keyboard.getEventCharacter(), org.lwjgl.input.Keyboard.getEventKey(), org.lwjgl.input.Keyboard.getEventKeyState());
		} else {
			super.handleKeyboardInput();
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int button) {
		ControlsBasicItem item = model.getEditingItem();
		if (item != null && item.useMouseButtons()) {
			item.setModifiers(SimpleKeyBindingManager.getPressedModifiers());
			item.setKey(SimpleKeyBindingManager.MOUSE_OFFSET + button);
			model.finishEdit();
		}
	}

	public ControlsModel getModel() {
		return model;
	}

	public void deleteCurrentControl() {
		SimpleKeyBindingManager man = (SimpleKeyBindingManager) SpoutClient.getInstance().getKeyBindingManager();
		ControlsBasicItem item = model.getItem(view.getSelectedRow());
		ShortcutBindingItem sh = null;
		if (item != null && item instanceof ShortcutBindingItem) {
			sh = (ShortcutBindingItem) item;
		}
		KeyBindingItem binding = null;
		if (item != null && item instanceof KeyBindingItem) {
			binding = (KeyBindingItem) item;
		}
		if (sh != null) {
			man.unregisterShortcut(sh.getShortcut());
			man.save();
		} else if (binding != null) {
			man.unregisterControl(binding.getBinding());
			man.save();
			model.refresh();
		} else {
			item.setKey(-128);
		}
		model.refresh();
	}
}
