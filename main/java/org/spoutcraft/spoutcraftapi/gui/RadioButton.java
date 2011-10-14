package org.spoutcraft.spoutcraftapi.gui;

public interface RadioButton extends Button {
	public boolean isSelected();
	public RadioButton setSelected(boolean selected);
	public int getGroup();
	public RadioButton setGroup(int group);
}
