package org.spoutcraft.spoutcraftapi.gui;

import org.spoutcraft.spoutcraftapi.UnsafeClass;

@UnsafeClass
public interface RadioButton extends Button {
	/**
	 * Is true if the radio button is selected
	 * @return selected
	 */
	public boolean isSelected();
	
	/**
	 * Sets this radio button as selected
	 * @param selected
	 * @return this
	 */
	public RadioButton setSelected(boolean selected);
	
	/**
	 * Gets the group id for this radio button. Radio buttons on the same screen, with the same group id will be grouped together
	 * @return group id
	 */
	public int getGroup();
	
	/**
	 * Sets the group id for this radio button
	 * @param group id to set
	 * @return this
	 */
	public RadioButton setGroup(int group);
}
