package org.getspout.spout.gui;

public interface Control extends Widget{

	/**
	 * True if the control is enabled and can receive input
	 * @return enabled
	 */
	public boolean isEnabled();
	
	/**
	 * Disables input to the control, but still allows it to be visible
	 * @param enable
	 * @return Control
	 */
	public Control setEnabled(boolean enable);

	/**
	 * Gets the color of this control
	 * @return color
	 */
	public Color getColor();
	
	/**
	 * Sets the color of this control
	 * @param color to set
	 * @return Control
	 */
	public Control setColor(Color color);
	
	/**
	 * Gets the color of this control when it is disabled
	 * @return disabled color
	 */
	public Color getDisabledColor();
	
	/**
	 * Sets the color of this control when it is disabled
	 * @param color to set
	 * @return Control
	 */
	public Control setDisabledColor(Color color);
}
