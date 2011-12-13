package org.getspout.spout.gui.server;

import org.getspout.spout.util.NetworkUtils;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;

public class LinkButton extends GenericButton {
	private String url = "";
	
	public LinkButton(String text, String url) {
		super(text);
		setUrl(url);
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		NetworkUtils.openInBrowser(url);
	}
}
