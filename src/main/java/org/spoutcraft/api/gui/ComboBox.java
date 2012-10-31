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
package org.spoutcraft.api.gui;

import java.util.List;

public interface ComboBox extends Button {
	public ComboBox setItems(List<String> items);
	public List<String> getItems();
	public ComboBox openList();
	public ComboBox closeList();
	public String getSelectedItem();
	public int getSelectedRow();
	public ComboBox setSelection(int row);
	public void onSelectionChanged(int i, String text);
	public boolean isOpen();
	/**
	 * Sets the format of the text on the button. Default is "%text%: %selected%"
	 * 
	 * %text% will be replaced with whatever can be obtained by Button.getText()
	 * %selected% will be replaced with the text of the selected item
	 * @param format the format of the text on the button
	 * @return the instance
	 */
	public ComboBox setFormat(String format);
	public String getFormat();
}
