package org.spoutcraft.spoutcraftapi.gui;

import java.util.ArrayList;
import java.util.List;

import org.spoutcraft.spoutcraftapi.Spoutcraft;

public class GenericListWidget extends GenericScrollable implements ListWidget {
	private List<ListWidgetItem> items = new ArrayList<ListWidgetItem>();
	private int selected = -1;
	private int cachedTotalHeight = -1;

	public WidgetType getType() {
		return WidgetType.ListWidget;
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
		ensureVisible(getItemRect(selected));
		return this;
	}
	
	private Rectangle getItemRect(int n) {
		Rectangle result = new Rectangle(0,0,0,0);
		result.setX(0);
		result.setY(getItemYOnScreen(n));
		result.setHeight(items.get(n).getHeight());
		result.setWidth(getInnerSize(Orientation.VERTICAL));
		return result;
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
		setScrollPosition(Orientation.VERTICAL, position);
		return this;
	}

	public int getScrollPosition() {
		return getScrollPosition(Orientation.VERTICAL);
	}
	
	@Override
	public int getInnerSize(Orientation axis) {
		if(axis == Orientation.HORIZONTAL) return getViewportSize(Orientation.HORIZONTAL);
		if(cachedTotalHeight == -1) {
			cachedTotalHeight = 0;
			for(ListWidgetItem item:items) {
				cachedTotalHeight+=item.getHeight();
			}
		}
		return cachedTotalHeight + 10;
	}
	
	public int getTotalHeight() {
		return getInnerSize(Orientation.VERTICAL);
	}

	public int getMaxScrollPosition() {
		return getMaximumScrollPosition(Orientation.VERTICAL);
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

	public void renderContents() {
		Spoutcraft.getRenderDelegate().renderContents(this);
	}

}
