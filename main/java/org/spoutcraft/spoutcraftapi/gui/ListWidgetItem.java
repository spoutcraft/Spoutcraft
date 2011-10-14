package org.spoutcraft.spoutcraftapi.gui;

public interface ListWidgetItem {
	
	/**
	 * Set the parent listwidget
	 * @param widget the parent widget
	 */
	public void setListWidget(ListWidget widget);
	
	/**
	 * Gets the parent listwidget
	 * @returns parent widget
	 */
	public ListWidget getListWidget();
	
	/**
	 * @return the height of the content of the item.
	 */
	public int getHeight();
	
	/**
	 * Renders the item.
	 * @param x position of the item
	 * @param y position of the item
	 * @param width of the item
	 * @param height of the item
	 */
	public void render(int x, int y, int width, int height);
}
