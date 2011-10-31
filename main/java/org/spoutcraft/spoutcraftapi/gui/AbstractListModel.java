package org.spoutcraft.spoutcraftapi.gui;

import java.util.HashSet;

public abstract class AbstractListModel {
	
	private HashSet<GenericListView> views = new HashSet<GenericListView>();

	public abstract ListWidgetItem getItem(int row);

	public abstract int getSize();

	public abstract void onSelected(int item, boolean doubleClick);
	
	public void addView(GenericListView view) {
		views.add(view);
	}
	
	public void removeView(GenericListView view) {
		views.remove(view);
	}
	
	public void sizeChanged() {
		for(GenericListView view:views) {
			view.sizeChanged();
		}
	}

}
