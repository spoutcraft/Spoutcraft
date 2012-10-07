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
package org.spoutcraft.client.gui.about;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.Widget;

public class TableSection extends Section {
	private int maxCaption = 0;
	private int height = 0;
	protected class Row {
		public GenericLabel caption = new GenericLabel("aasdasd");
		public GenericLabel text = new GenericLabel("basdasd");
		public Row() {
			caption.setTextColor(new Color(0xaaaaaa));
			caption.setWidth(20);
			caption.setWrapLines(true);
			text.setTextColor(new Color(0xaaaaaa));
			text.setWidth(20);
			text.setWrapLines(true);
		}
	}

	private LinkedList<Row> rows = new LinkedList<TableSection.Row>();

	@Override
	public void setX(int x) {
		super.setX(x);
		update();
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		update();
	}

	@Override
	public int getHeight() {
		return super.getHeight() + height + 5;
	}

	@Override
	public void update() {
		super.update();
		int y = super.getHeight() + 5 + getY();
		int left = getX();
		int right = left + maxCaption + 5;
		int textRightWidth = getWidth() - right + left;
		int textLeftWidth = right - left;
		if (right - left > getWidth() || right - left <= 30) {
			textRightWidth = textLeftWidth = getWidth() / 2 - 5;
			right = left + textLeftWidth + 5;
		}
		height = 0;
		for (Row row:rows) {
			row.caption.setX(left);
			row.caption.setY(y);
			row.caption.setWidth(textLeftWidth);
			row.caption.recalculateLines();
			row.text.setX(right);
			row.text.setY(y);
			row.text.setWidth(textRightWidth);
			row.text.recalculateLines();
			int h = (int) Math.max(row.text.getHeight(), row.caption.getHeight()) + 2;
			y += h;
			height += h;
		}
	}

	@Override
	public List<Widget> getWidgets() {
		List<Widget> ret = super.getWidgets();
		for (Row row:rows) {
			ret.add(row.caption);
			ret.add(row.text);
		}
		return ret;
	}

	@Override
	public void init(GuiNewAbout screen, String title, Object yaml) {
		setTitle(title);
		LinkedHashMap<String, String> r = (LinkedHashMap<String, String>) yaml;
		for (Entry<String, String> entry:r.entrySet()) {
			Row row = new Row();
			row.caption.setText(entry.getKey());
			maxCaption = Math.max(maxCaption, Spoutcraft.getRenderDelegate().getMinecraftFont().getTextWidth(row.caption.getText()));
			row.text.setText(entry.getValue());
			rows.add(row);
		}
	}
}
