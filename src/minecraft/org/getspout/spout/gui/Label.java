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
	

	public Align getAlignX();
	
	public Align getAlignY();

	public Widget setAlignX(Align pos);
	
	public Widget setAlignY(Align pos);
}
