package org.getspout.spout.gui.server;

import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.gui.CheckBox;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class FilterButton extends GenericCheckBox implements UrlElement{
	protected ServerListModel model = SpoutClient.getInstance().getServerManager().getServerList();
	protected String url = "";
	
	public FilterButton(String text, String url){
		super(text);
		this.url = url;
	}
	
	
	@Override
	public CheckBox setChecked(boolean b) {
		super.setChecked(b);
		model.updateUrl();
		return this;
	}
	
	public boolean isActive() {
		return isChecked();
	}

	public String getUrlPart() {
		return url;
	}

}
