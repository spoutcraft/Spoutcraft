package org.getspout.spout.gui.server;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.database.UrlElement;
import org.lwjgl.input.Keyboard;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;

public class CountryButton extends GenericButton implements UrlElement {

	String currentCountry = "all";
	int item = -1;
	ServerListModel model = SpoutClient.getInstance().getServerManager().getServerList();

	public boolean isActive() {
		return item != -1;
	}

	public String getUrlPart() {
		return "country="+currentCountry;
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		item ++;
		if(model.getCountries().size() <= item || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			item = -1;
		}
		if(item != -1) {
			currentCountry = model.getCountries().get(item);
		}
		model.updateUrl();
	}
	
	public String getText() {
		return "Country: "+(item != -1 ?currentCountry + " ("+(item+1)+"/"+model.getCountries().size()+")":"All");
	}

	public void setCurrentCountry(int i) {
		item = i;
	}

	public void clear() {
		item = -1;
	}

}
