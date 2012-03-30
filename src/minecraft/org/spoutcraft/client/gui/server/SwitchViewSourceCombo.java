package org.spoutcraft.client.gui.server;

import java.util.ArrayList;
import java.util.List;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.gui.GenericComboBox;

public class SwitchViewSourceCombo extends GenericComboBox {
	private GuiFavorites gui;

	public SwitchViewSourceCombo(GuiFavorites gui) {
		List<String> items = new ArrayList<String>();
		items.add("Favorite Servers");
		items.add("LAN Servers");
		setItems(items);
		this.gui = gui;
	}

	@Override
	public void onSelectionChanged(int i, String text) {
		switch(i) {
		case 0:
			gui.setModel(SpoutClient.getInstance().getServerManager().getFavorites());
			break;
		case 1:
			gui.setModel(SpoutClient.getInstance().getServerManager().getLANModel());
			break;
		}
		gui.updateButtons();
		super.onSelectionChanged(i, text);
	}
}
