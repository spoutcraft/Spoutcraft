package org.spoutcraft.client.gui.database;

import org.spoutcraft.spoutcraftapi.gui.CheckBox;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;

public class FilterButton extends GenericCheckBox implements UrlElement{
	protected AbstractAPIModel model;
	protected String url = "";
	
	public FilterButton(String text, String url, AbstractAPIModel model){
		super(text);
		this.url = url;
		this.model = model;
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

	public void clear() {
		setChecked(false);
	}

}
