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
package org.spoutcraft.client.gui.database;

import org.spoutcraft.api.gui.CheckBox;
import org.spoutcraft.api.gui.GenericCheckBox;

public class FilterButton extends GenericCheckBox implements UrlElement {
	protected AbstractAPIModel model;
	protected String url = "";

	public FilterButton(String text, String url, AbstractAPIModel model) {
		super(text);
		this.url = url;
		this.model = model;
	}

	public CheckBox setChecked(boolean check, boolean update) {
		super.setChecked(check);
		if (update) {
			model.updateUrl();
		}
		return this;
	}

	@Override
	public CheckBox setChecked(boolean b) {
		return setChecked(b, true);
	}

	public boolean isActive() {
		return isChecked();
	}

	public String getUrlPart() {
		return url;
	}

	public void clear() {
		setChecked(false);
	}
}
