package org.getspout.spout.gui;

public interface Gradient extends Widget {
	
	/**
	 * Gets the top colour of the gradient to render
	 * @return color
	 */
	public Color getTopColor();
	
	/**
	 * Sets the top colour of the gradient to render
	 * @param color
	 * @return gradient
	 */
	public Gradient setTopColor(Color color);
	
	/**
	 * Gets the bottom colour of the gradient to render
	 * @return color
	 */
	public Color getBottomColor();
	
	/**
	 * Sets the bottom colour of the gradient to render
	 * @param color
	 * @return gradient
	 */
	public Gradient setBottomColor(Color color);

}
