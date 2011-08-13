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
	 * Get the horizontal, x, alignment of text within it's area
	 * @return alignment enum, ( FIRST | SECOND | THIRD )
	 */
	public Align getAlignX();
	
	/** 
	 * Get the vertical, y, alignment of text within it's area
	 * @return alignment enum, ( FIRST | SECOND | THIRD )
	 */
	public Align getAlignY();
	
	/** 
	 * Sets the horizontal, x, alignment of text within it's area providing it's on auto
	 * @return label
	 */
	public Widget setAlignX(Align pos);

	/** 
	 * Sets the vertical, y, alignment of text within it's area providing it's on auto
	 * @return label
	 */
	public Widget setAlignY(Align pos);
	
	/** 
	 * Determines if text expands to fill width and height
	 * @param auto
	 * @return label
	 */
	Label setAuto(boolean auto);
	
	/** 
	 * Gets if the text will expand to fill width and height
	 * @param auto
	 * @return label
	 */
	boolean getAuto();
}
