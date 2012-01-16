package org.spoutcraft.client.gui.controls;

import org.spoutcraft.spoutcraftapi.gui.ListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;

public abstract class ControlsBasicItem implements ListWidgetItem {
	
	private ListWidget widget;
	protected ControlsModel model;
	private boolean conflicts = false;
	
	public ControlsBasicItem(ControlsModel model) {
		this.model = model;
	}

	public ListWidget getListWidget() {
		return widget;
	}

	public void onClick(int x, int y, boolean doubleClick) {
		model.onItemClicked(this, doubleClick);
	}

	public void setListWidget(ListWidget widget) {
		this.widget = widget;
	}
	
	public abstract void setKey(int id);
	public abstract int getKey();
	
	public boolean conflicts(ControlsBasicItem other) {
		//TODO better handling for modifiers
		return getKey() == other.getKey() && getModifiers() == other.getModifiers();
	}
	
	/**
	 * Tell if this item accepts modifier keys such as SHIFT, CONTROL or ALT.
	 * @return true, if the item accepts modifier keys.
	 */
	public abstract boolean useModifiers();
	
	/**
	 * Tell if this item accepts mouse buttons to summon the action
	 * @return true, if the item accepts mouse buttons
	 */
	public abstract boolean useMouseButtons();
	
	public void setConflicting(boolean c) {
		this.conflicts = c;
	}
	
	public void setModifiers(int m) {
		//Unused
	}
	
	public int getModifiers() {
		return 0;
	}
	
	public abstract String getName();

	public boolean isConflicting() {
		return conflicts;
	}
}