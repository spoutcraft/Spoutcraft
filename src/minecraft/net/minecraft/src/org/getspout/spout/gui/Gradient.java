package org.getspout.spout.gui;

public interface Gradient extends Widget {
	
	/**
	 * Gets the top colour of the gradient to render
	 * @return color
	 */
	public int getTopColor();
	
	/**
	 * Sets the top colour of the gradient to render
	 * @param color ie 0xFFFFFFFF
	 * @return gradient
	 */
	public Gradient setTopColor(int color);
	
	/**
	 * Gets the bottom colour of the gradient to render
	 * @return color
	 */
	public int getBottomColor();
	
	/**
	 * Sets the bottom colour of the gradient to render
	 * @param color ie 0xFFFFFFFF
	 * @return gradient
	 */
	public Gradient setBottomColor(int color);

}
