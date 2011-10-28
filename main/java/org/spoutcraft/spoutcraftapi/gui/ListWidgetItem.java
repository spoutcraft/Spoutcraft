package org.spoutcraft.spoutcraftapi.gui;

import org.spoutcraft.spoutcraftapi.UnsafeClass;

@UnsafeClass
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
	
	/**
	 * Will be called when someone clicks on this item
	 * @param x the relative x position where the item was clicked
	 * @param y the relative y position where the item was clicked
	 */
	public void onClick(int x, int y);
}
