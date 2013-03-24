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
package org.spoutcraft.api.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.io.SpoutInputStream;

public class GenericComboBox extends GenericButton implements ComboBox {
	private List<String> items = new ArrayList<String>();
	private ComboBoxModel model;
	private final GenericListView view;
	private Screen screen;
	private boolean open = false;
	private String format = "%text%: %selected%";

	public GenericComboBox() {
		model = new ComboBoxModel();
		view = new ComboBoxView(model, this);
		view.setSelection(0);
		view.setVisible(false);
	}

	public GenericComboBox(ComboBoxModel model) {
		this.model = model;
		view = new ComboBoxView(model, this);
		view.setSelection(0);
		view.setVisible(false);
	}

	public WidgetType getType() {
		return WidgetType.ComboBox;
	}

	public void render() {
		setAlign(WidgetAnchor.TOP_LEFT);
		Spoutcraft.getRenderDelegate().render(this);
	}

	public ComboBox setItems(List<String> items) {
		this.items = items;
		model.setList(items);
		setSelection(0);
		return this;
	}

	public List<String> getItems() {
		return Collections.unmodifiableList(items);
	}

	public ComboBox openList() {
		if (open) {
			return this;
		}
		open = true;
		screen = getScreen();
		while (!(screen instanceof GenericScreen)) {
			if (screen.getScreen() != null) {
				screen = screen.getScreen();
			} else {
				break;
			}
		}

		screen.attachWidget(getAddon(), view);
		view.setVisible(true);
		view.setPriority(RenderPriority.Lowest); // Makes it the top-most widget
		view.setFocus(true);
		view.setSelection(view.getSelectedRow());
		return this;
	}

	public ComboBox closeList() {
		if (!open) {
			return this;
		}
		open = false;
		view.setVisible(false);
		screen.removeWidget(view);
		return this;
	}

	public boolean isOpen() {
		return open;
	}

	public String getSelectedItem() {
		ComboBoxItem item = model.getItem(view.getSelectedRow());
		return item == null ? "" : item.getText();
	}

	public int getSelectedRow() {
		return view.getSelectedRow();
	}

	public void onSelectionChanged(int i, String text) {}

	@Override
	public String getText() {
		if (StringUtils.isEmpty(super.getText())) {
			return getSelectedItem();
		} else {
			String text = format.replaceAll("%text%", super.getText()).replaceAll("%selected%", getSelectedItem());
			return text;
		}
	}

	@Override
	public void onButtonClick() {
		setOpen(!isOpen());
		super.onButtonClick();
	}

	public ComboBox setSelection(int row) {
		view.setSelection(row);
		return this;
	}

	@Override
	public Widget setScreen(String addon, Screen screen) {
		super.setScreen(addon, screen);
		if (screen == null) {
			return this;
		}

		screen = getScreen();
		while (!(screen instanceof GenericScreen)) {
			if (screen.getScreen() != null) {
				screen = screen.getScreen();
			} else {
				break;
			}
		}

		screen.attachWidget(addon, view);

		return this;
	}

	@Override
	public Widget setScreen(Screen screen) {
		return setScreen(null, screen);
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		view.setSelection(input.readInt());
		int count = input.readInt();
		items.clear();
		for (int i = 0; i < count; i++) {
			String item = input.readString();
			items.add(item);
		}
		model.setList(items);
		format = input.readString();
	}

	@Override
	public int getVersion() {
		return super.getVersion() + 1;
	}

	protected class ComboBoxModel extends AbstractListModel {
		List<String> list = new ArrayList<String>();
		List<ComboBoxItem> items = new ArrayList<GenericComboBox.ComboBoxItem>();

		public void setList(List<String> list) {
			this.list = list;
			updateItems();
		}

		private void updateItems() {
			items.clear();
			for (String l:list) {
				ComboBoxItem item = new ComboBoxItem(this);
				item.setTitle(l);
				items.add(item);
			}
		}

		@Override
		public ComboBoxItem getItem(int row) {
			if (row < 0 || row >= getSize()) {
				return null;
			}
			return items.get(row);
		}

		@Override
		public int getSize() {
			return items.size();
		}

		@Override
		public void onSelected(int item, boolean doubleClick) {
		}

		public void handleClick(ComboBoxItem comboBoxItem) {
			int i = items.indexOf(comboBoxItem);
			onSelectionChanged(i, getItem(i).getText());
			closeList();
		}

		public boolean isLast(ComboBoxItem comboBoxItem) {
			return items.indexOf(comboBoxItem) == items.size() - 1;
		}
	}

	public class ComboBoxItem implements ListWidgetItem {
		private String text;
		private ListWidget widget;
		private ComboBoxModel model;
		private GenericGradient gradient = new GenericGradient();

		public ComboBoxItem(ComboBoxModel model) {
			this.model = model;
		}

		public void setListWidget(ListWidget widget) {
			this.widget = widget;
		}

		public ListWidget getListWidget() {
			return widget;
		}

		public int getHeight() {
			return 11;
		}

		public void render(int x, int y, int width, int height) {
			gradient.setX(x).setY(y).setWidth(width).setHeight(height);
			gradient.setTopColor(new Color(0.6f,0.6f,0.6f,0.5f)).setBottomColor(new Color(0.6f,0.6f,0.6f,0f));
			gradient.setOrientation(Orientation.VERTICAL);
			Spoutcraft.getRenderDelegate().render(gradient);
			Spoutcraft.getRenderDelegate().getMinecraftFont().drawString(text, x+2, y+2, 0xffffffff);
		}

		public void onClick(int x, int y, boolean doubleClick) {
			if (x != -1) {
				model.handleClick(this);
			} else if (doubleClick) {
				model.handleClick(this);
			}
		}

		public String getText() {
			return text;
		}

		public void setTitle(String title) {
			this.text = title;
		}
	}

	public class ComboBoxView extends GenericListView {
		GenericComboBox combo;
		public ComboBoxView(AbstractListModel model, GenericComboBox box) {
			super(model);
			combo = box;
			setBackgroundColor(new Color(0.5f,0.5f,0.5f,0.9f));
		}

		@Override
		public String getTooltip() {
			if (!super.getTooltip().equals("")) {
				return super.getTooltip();
			}
			return null;
		}

		@Override
		public double getWidth() {
			setWidth((int) combo.getWidth());
			return combo.getWidth();
		}

		@Override
		public double getHeight() {
			int a = getAvailableHeight(false) - 5;
			if (a < 30) {
				a = getAvailableHeight(true) - 5;
			}
			double res = Math.min(a, getInnerSize(Orientation.VERTICAL));
			setHeight((int) res);
			return res;
		}

		@Override
		public int getX() {
			return (int) combo.getActualX();
		}

		@Override
		public int getY() {
			int h = (int) getHeight();
			int a = getAvailableHeight(false);
			if (a < 30) {
				return (int) (combo.getActualY()-h);
			} else {
				return (int) (combo.getActualY() + combo.getHeight());
			}
		}

		@Override
		public double getActualX() {
			setX(getX());
			return super.getActualX();
		}

		@Override
		public double getActualY() {
			setY(getY());
			return super.getActualY();
		}

		protected int getAvailableHeight(boolean top) {
			if (!top) {
				return (int) (Spoutcraft.getClient().getRenderDelegate().getScreenHeight() - combo.getActualY() - combo.getHeight() - 5);
			} else {
				return (int) (combo.getActualY() - 5);
			}
		}

		public GenericComboBox getComboBox() {
			return combo;
		}
	}

	public ComboBox setOpen(boolean open) {
		if (open) {
			openList();
		} else {
			closeList();
		}
		return this;
	}

	@Override
	public Control setFocus(boolean focus) {
		if (!focus && !view.isFocus() && isOpen()) {
			closeList();
		}
		return super.setFocus(focus);
	}

	public String getFormat() {
		return format;
	}

	public ComboBox setFormat(String format) {
		this.format = format;
		return this;
	}
}
