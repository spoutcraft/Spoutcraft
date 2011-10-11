package org.spoutcraft.spoutcraftapi.gui;

import java.util.ArrayList;
import java.util.List;

import org.spoutcraft.spoutcraftapi.Spoutcraft;

public class GenericListWidget extends GenericControl implements ListWidget {
	private List<ListWidgetItem> items = new ArrayList<ListWidgetItem>();
	private int selected = -1;
	private int scroll = 0;
	private int cachedTotalHeight = -1;

	public WidgetType getType() {
		return WidgetType.ListWidget;
	}

	public void render() {
		Spoutcraft.getRenderDelegate().render(this);
	}

	public ListWidgetItem[] getItems() {
		ListWidgetItem[] sample = {};
		return items.toArray(sample);
	}

	public ListWidgetItem getItem(int i) {
		if(i == -1) {
			return null;
		}
		return items.get(i);
	}

	public ListWidget addItem(ListWidgetItem item) {
		items.add(item);
		item.setListWidget(this);
		cachedTotalHeight = -1;
		return this;
	}

	public boolean removeItem(ListWidgetItem item) {
		if(items.contains(item)){
			items.remove(item);
			item.setListWidget(null);
			cachedTotalHeight = -1;
			return true;
		}
		return false;
	}

	public ListWidgetItem getSelectedItem() {
		return getItem(selected);
	}

	public ListWidget setSelection(int n) {
		selected = n;
		if(selected < -1) {
			selected = -1;
		}
		if(selected > items.size()-1) {
			selected = items.size()-1;
		}
		
		//Check if selection is visible
		int h = getItemYOnScreen(selected);
		ListWidgetItem item = getSelectedItem();
		int scrollBottom = (int) (scroll + getHeight() - 10);
		if(h<scroll) {
			setScrollPosition(h);
		} else if(h + item.getHeight() > scrollBottom) {
			setScrollPosition((int) (h - getHeight() + 10 + item.getHeight()));
		}
		return this;
	}
	
	private int getItemYOnScreen(int n) {
		int height = 0;
		for(int i = 0; i<n && i<items.size(); i++) {
			height += getItem(n).getHeight();
		}
		return height;
	}

	public ListWidget clearSelection() {
		setSelection(-1);
		return this;
	}

	public boolean isSelected(int n) {
		return selected == n;
	}

	public ListWidget setScrollPosition(int position) {
		scroll = position;
		if(scroll<0) {
			scroll = 0;
		}
		if(scroll > getMaxScrollPosition()) {
			scroll = getMaxScrollPosition();
		}
		return this;
	}

	public int getScrollPosition() {
		return scroll;
	}

	public int getTotalHeight() {
		if(cachedTotalHeight == -1) {
			cachedTotalHeight = 0;
			for(ListWidgetItem item:items) {
				cachedTotalHeight+=item.getHeight();
			}
		}
		return cachedTotalHeight;
	}

	public int getMaxScrollPosition() {
		return Math.max(0, (int) (getTotalHeight() - getHeight() + 10));
	}

	public boolean isSelected(ListWidgetItem item) {
		if(getSelectedItem() == null)
			return false;
		return getSelectedItem().equals(item);
	}

	public ListWidget shiftSelection(int n) {
		if(selected + n < 0){
			setSelection(0);
		} else {
			setSelection(selected + n);
		}
		return this;
	}

}
