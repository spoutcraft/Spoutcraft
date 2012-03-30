package org.spoutcraft.client.gui.server;

import java.util.ArrayList;
import java.util.List;

import org.spoutcraft.spoutcraftapi.gui.AbstractListModel;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;

public class ServerModel extends AbstractListModel {
	
	protected List<ServerItem> items = new ArrayList<ServerItem>();
	protected GuiFavorites gui;
	private boolean polling = false;

	@Override
	public ListWidgetItem getItem(int row) {
		if(row >= 0 && row < getSize()) {
			return items.get(row);
		}
		return null;
	}

	@Override
	public int getSize() {
		return items.size();
	}
	
	public void onSelected(int item, boolean doubleClick) {
		gui.updateButtons();
	}

	public synchronized boolean isPolling() {
		return polling;
	}

	public synchronized void setPolling(boolean poll) {
		polling = poll;
		if (gui != null) {
			gui.updateButtons();
		}
	}

	public void setCurrentGUI(GuiFavorites gui) {
		this.gui = gui;
	}

	public GuiFavorites getCurrentGui() {
		return gui;
	}

}
