package org.spoutcraft.client.gui;

import java.util.ArrayList;
import java.util.Iterator;

import org.spoutcraft.client.gui.singleplayer.WorldItem;
import org.spoutcraft.spoutcraftapi.gui.AbstractListModel;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;

public abstract class FilterModel extends AbstractListModel {
	protected ArrayList<ListWidgetItem> filteredItems = new ArrayList<ListWidgetItem>();
	protected ArrayList<ListWidgetItem> items = new ArrayList<ListWidgetItem>();
	private ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
	
	public ArrayList<FilterItem> getFilters() {
		return filters;
	}
	
	public void refresh() {
		items.clear();
		
		refreshContents();
		
		filteredItems.clear();
		
		for(ListWidgetItem item:items) {
			boolean matches = true;
			for(FilterItem filter:filters) {
				matches = filter.matches(item);
				if(!matches) {
					break;
				}
			}
			if(matches) {
				filteredItems.add(item);
			}
		}
		
		sizeChanged();
	}
	
	protected abstract void refreshContents();
	
	@Override
	public ListWidgetItem getItem(int row) {
		if(row >= 0 && row < filteredItems.size()) {			
			return filteredItems.get(row);
		}
		return null;
	}

	@Override
	public int getSize() {
		return filteredItems.size();
	}
}
