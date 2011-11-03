package org.getspout.spout.gui.server;

import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.event.screen.TextFieldChangeEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;

public class SearchField extends GenericTextField implements UrlElement {
	public SearchField() {
		setMaximumCharacters(0);
	}
	
	@Override
	public void onTextFieldChange(TextFieldChangeEvent event) {
		if(event.getNewText().isEmpty()) {
			SpoutClient.getInstance().getServerManager().getServerList().updateUrl();
		}
	}

	public boolean isActive() {
		return !getText().isEmpty();
	}

	public String getUrlPart() {
		return "terms="+getText().replaceAll(" ", ",");
	}

	public void clear() {
		setText("");
	}
}
