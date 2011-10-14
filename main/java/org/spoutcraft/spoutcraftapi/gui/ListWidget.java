package org.spoutcraft.spoutcraftapi.gui;

public interface ListWidget extends Scrollable {
	/**
	 * Get all the items from the list widget
	 * @return the assigned ListWidgetItems
	 */
	public ListWidgetItem[] getItems();
	
	/**
	 * Returns the nth item from the listwidget
	 * @param n which item to get
	 * @return nth item from the list
	 */
	public ListWidgetItem getItem(int n);
	
	/**
	 * Adds an item to the list
	 * @param item the item to add.
	 * @return instance of the ListWidget
	 */
	public ListWidget addItem(ListWidgetItem item);
	
	/**
	 * Removes an item from the list.
	 * @param item to remove
	 * @return if item was found.
	 */
	public boolean removeItem(ListWidgetItem item);
	
	/**
	 * @return the currently selected item.
	 * @returns null when no item is selected.
	 */
	public ListWidgetItem getSelectedItem();
	
	/**
	 * Sets the selected item to be the nth in the list.
	 * @param n the number of the item or -1 to clear the selection
	 * @return instance of the ListWidget
	 */
	public ListWidget setSelection(int n);
	
	/**
	 * Clears the selection
	 * @return instance of the ListWidget
	 */
	public ListWidget clearSelection();
	
	/**
	 * @param n item to check
	 * @returns if the nth item is selected
	 */
	public boolean isSelected(int n);
	
	/**
	 * @param item to check
	 * @returns if the item is selected
	 */
	boolean isSelected(ListWidgetItem item);

	/**
	 * Moves the selection up or down by n
	 * @param n
	 * @return
	 */
	public ListWidget shiftSelection(int n);
	
	/**
	 * Sets the scroll position
	 * @param position
	 * @return instance of the ListWidget
	 * @deprecated use Scrollable.setScrollPosition(Orientation.VERTICAL, value) instead.
	 */
	@Deprecated
	public ListWidget setScrollPosition(int position);
	
	/**
	 * @return the scroll position
	 * @deprecated use Scrollable.getScrollPosition(Orientation.VERTICAL) instead.
	 */
	@Deprecated
	public int getScrollPosition();
	
	/**
	 * Calculates the total height of all elements.
	 * @return
	 * @deprecated use Scrollable.getInnerSize(Orientation.VERTICAL) instead.
	 */
	@Deprecated
	public int getTotalHeight();
	
	/**
	 * @return the scroll position that is needed to show the very last item.
	 * @deprecated use Scrollable.getMaximumScrollPosition(Orientation.VERTICAL) instead
	 */
	@Deprecated
	public int getMaxScrollPosition();
}
