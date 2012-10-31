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

import java.util.List;

import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.api.gui.Widget;

public class TextSection extends Section {
	private GenericLabel labelText = new GenericLabel();

	public TextSection() {
		labelText.setWidth(100);
		labelText.setWrapLines(true);
		labelText.setTextColor(new Color(0xaaaaaa));
	}

	@Override
	public void setX(int x) {
		super.setX(x);
		labelText.setX(x);
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		labelText.setY(super.getHeight() + 5 + y);
	}

	public String getText() {
		return labelText.getText();
	}

	public Label setText(String text) {
		return labelText.setText(text);
	}

	@Override
	public void update() {
		super.update();
		labelText.setWidth(getWidth());
		labelText.recalculateLines();
	}

	@Override
	public int getHeight() {
		return (int) (super.getHeight() + labelText.getHeight() + 5);
	}

	@Override
	public void init(GuiNewAbout screen, String title, Object yaml) {
		setTitle(title);
		if (yaml instanceof String) {
			setText((String) yaml);
		}
	}

	@Override
	public List<Widget> getWidgets() {
		List<Widget> ret = super.getWidgets();
		ret.add(labelText);
		return ret;
	}
}
