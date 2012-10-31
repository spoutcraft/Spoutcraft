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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.api.gui.Widget;

public abstract class Section {
	private int x = 0;
	private int y = 0;
	private int width = 100;
	private Label labelTitle;

	private static HashMap<String, Class<? extends Section>> types = new HashMap<String, Class<? extends Section>>();
	static {
		type("text", TextSection.class);
		type("table", TableSection.class);
		type("images", ImageSection.class);
	}
	private static void type(String identify, Class<? extends Section> clazz) {
		types.put(identify, clazz);
	}
	public static Section getSection(String type) {
		if (types.containsKey(type)) {
			try {
				return types.get(type).newInstance();
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}
		return null;
	}

	public Section() {
		labelTitle = new GenericLabel("Untitled");
	}

	public String getTitle() {
		return labelTitle.getText();
	}

	public void setTitle(String title) {
		labelTitle.setText(title);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		labelTitle.setX(x);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		labelTitle.setY(y);
	}

	public int getHeight() {
		return 13;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		update();
	}

	public void update() {
		labelTitle.setWidth(getWidth());
	}

	public abstract void init(GuiNewAbout screen, String title, Object yaml);

	public List<Widget> getWidgets() {
		List<Widget> ret = new LinkedList<Widget>();
		ret.add(labelTitle);
		return ret;
	}
}
