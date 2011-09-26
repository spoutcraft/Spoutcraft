package org.spoutcraft.spoutcraftapi.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenericListWidget extends GenericControl implements ListWidget {
	private ArrayList<ListWidgetItem> items = new ArrayList<ListWidgetItem>();
	@Override
	public WidgetType getType() {
		return null;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ListWidgetItem> getItems() {
		return Collections.unmodifiableList(items);
	}

	@Override
	public ListWidget addItem(ListWidgetItem item) {
		items.add(item);
		return this;
	}

	@Override
	public ListWidget removeItem(ListWidgetItem item) {
		items.remove(item);
		return this;
	}

	@Override
	public ListWidgetItem getItemAt(int i) {
		return items.get(i);
	}
}
