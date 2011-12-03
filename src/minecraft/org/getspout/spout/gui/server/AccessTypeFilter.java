package org.getspout.spout.gui.server;

import java.util.ArrayList;
import java.util.List;

import org.getspout.spout.gui.database.AbstractAPIModel;
import org.getspout.spout.gui.database.UrlElement;
import org.spoutcraft.spoutcraftapi.gui.GenericComboBox;

public class AccessTypeFilter extends GenericComboBox implements UrlElement {

	private String[] strings = { "All", "Open", "Whitelisted", "Graylisted", "Blacklisted"};
	private int [] ids = {-1, 0, 1, 2, 3};
	AbstractAPIModel model;
	
	public AccessTypeFilter(AbstractAPIModel model) {
		this.model = model;
		List<String> l = new ArrayList<String>();
		for(String type:strings) {
			l.add(type);
		}
		setItems(l);
		setSelection(0);
	}
	
	public boolean isActive() {
		return ids[getSelectedRow()] != -1;
	}

	public String getUrlPart() {
		return "accessType="+ids[getSelectedRow()];
	}

	public void clear() {
		setSelection(0);
	}
	
	@Override
	public void onSelectionChanged(int i, String text) {
		model.updateUrl();
	}

	@Override
	public String getText() {
		return "Access: "+super.getText();
	}
}
