package org.getspout.spout.gui;

public interface Label extends Widget{
	/**
	 * Gets the text of the label
	 * @return text
	 */
	public String getText();
	
	/**
	 * Sets the text of the label
	 * @param text to set
	 * @return label
	 */
	public Label setText(String text);
	
	/**
	 * Gets the hex color code for the text
	 * @return color code
	 */
	public int getHexColor();
	
	/** 
	 * Sets the hex color code for the text
	 * @param hex color code to set
	 * @return label
	 */
	public Label setHexColor(int hex);
	
	/** 
	 * Determines if text expands to fill width and height
	 * @param auto
	 * @return label
	 */
	public Label setAuto(boolean auto);
	
	/** 
	 * Gets if the text will expand to fill width and height
	 * @param auto
	 * @return label
	 */
	public boolean getAuto();
	
	public WidgetAnchor getAlign();
	
	public Widget setAlign(WidgetAnchor pos);
}
