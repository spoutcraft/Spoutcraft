package org.getspout.spout.gui.database;

import org.spoutcraft.spoutcraftapi.event.screen.TextFieldChangeEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;

public class SearchField extends GenericTextField implements UrlElement {
	private AbstractAPIModel model;
	
	public SearchField(AbstractAPIModel model) {
		setMaximumCharacters(0);
		this.model = model;
	}
	
	@Override
	public void onTextFieldChange(TextFieldChangeEvent event) {
		if(event.getNewText().isEmpty()) {
			model.updateUrl();
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
