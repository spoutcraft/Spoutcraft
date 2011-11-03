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
	
	public CheckBox setChecked(boolean check, boolean update) {
		super.setChecked(check);
		if(update) {
			model.updateUrl();
		}
		return this;
	}
	
	@Override
	public CheckBox setChecked(boolean b) {
		return setChecked(b, true);
	}
	
	public boolean isActive() {
		return isChecked();
	}

	public String getUrlPart() {
		return url;
	}

}
