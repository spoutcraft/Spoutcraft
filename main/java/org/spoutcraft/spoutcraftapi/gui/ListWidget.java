package org.spoutcraft.spoutcraftapi.gui;

import java.util.List;

public interface ListWidget extends Control {
	public List<ListWidgetItem> getItems();
	public ListWidget addItem(ListWidgetItem item);
	public ListWidget removeItem(ListWidgetItem item);
	public ListWidgetItem getItemAt(int i);
}
