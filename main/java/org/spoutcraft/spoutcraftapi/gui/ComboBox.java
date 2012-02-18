package org.spoutcraft.spoutcraftapi.gui;

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
