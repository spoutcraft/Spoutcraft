package org.spoutcraft.client.gui.server;

import java.util.ArrayList;
import java.util.List;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.database.UrlElement;
import org.spoutcraft.spoutcraftapi.gui.GenericComboBox;

public class CountryButton extends GenericComboBox implements UrlElement {

	ServerListModel model = SpoutClient.getInstance().getServerManager().getServerList();
	
	public CountryButton() {
		List<String> countries = new ArrayList<String>();
		countries.add("All");
		countries.addAll(model.getCountries());
		setItems(countries);
	}

	public boolean isActive() {
		return getSelectedRow() != 0;
	}

	public String getUrlPart() {
		return "country="+getSelectedItem();
	}
	
	@Override
	public void onSelectionChanged(int row, String text) {
		model.updateUrl();
	}
	
	public String getText() {
		return "Country: "+getSelectedItem();
	}

	public void setCurrentCountry(int i) {
		setSelection(i + 1);
	}

	public void clear() {
		setSelection(0);
	}

}
