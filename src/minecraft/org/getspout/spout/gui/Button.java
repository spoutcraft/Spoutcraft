package org.getspout.spout.gui;

public interface Button extends Control, Label{
	
	/**
	 * Get's the text that is displayed when the control is disabled
	 * @return disabled text
	 */
	public String getDisabledText();
	
	/**
	 * Sets the text that is displayed when the control is disabled
	 * @param text to display
	 * @return Button
	 */
	public Button setDisabledText(String text);
	
	/**
	 * Get's the color of the control while the mouse is hovering over it
	 * @return color
	 */
	public Color getHoverColor();
	
	/**
	 * Sets the color of the control while the mouse is hovering over it
	 * @param color
	 * @return Button
	 */
	public Button setHoverColor(Color color);

}
