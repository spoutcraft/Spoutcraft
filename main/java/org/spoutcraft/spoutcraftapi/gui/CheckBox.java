package org.spoutcraft.spoutcraftapi.gui;

import org.spoutcraft.spoutcraftapi.UnsafeClass;

@UnsafeClass
public interface CheckBox extends Button {
	/**
	 * Is true if the checkbox is checked
	 * @return checked
	 */
	public boolean isChecked();
	
	/**
	 * Sets the checked state of the checkbox
	 * @param checked
	 * @return this
	 */
	public CheckBox setChecked(boolean checked);
}
