package org.spoutcraft.client.gui.about;

import java.util.List;

import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.Label;
import org.spoutcraft.spoutcraftapi.gui.Widget;

public class TextSection extends Section {
	private GenericLabel labelText = new GenericLabel();
	
	public TextSection() {
		labelText.setWidth(100);
		labelText.setWrapLines(true);
		labelText.setTextColor(new Color(0xaaaaaa));
	}
	
	@Override
	public void setX(int x) {
		super.setX(x);
		labelText.setX(x);
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		labelText.setY(super.getHeight() + 5 + y);
	}

	public String getText() {
		return labelText.getText();
	}

	public Label setText(String text) {
		return labelText.setText(text);
	}
	
	@Override
	public void update() {
		super.update();
		labelText.setWidth(getWidth());
		labelText.recalculateLines();
	}

	@Override
	public int getHeight() {
		return (int) (super.getHeight() + labelText.getHeight() + 5);
	}

	@Override
	public void init(GuiNewAbout screen, String title, Object yaml) {
		setTitle(title);
		if(yaml instanceof String) {
			setText((String) yaml);
		}
	}

	@Override
	public List<Widget> getWidgets() {
		List<Widget> ret = super.getWidgets();
		ret.add(labelText);
		return ret;
	}

	
}
