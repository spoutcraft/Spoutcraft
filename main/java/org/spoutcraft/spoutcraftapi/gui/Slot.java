package org.spoutcraft.spoutcraftapi.gui;

import org.spoutcraft.spoutcraftapi.inventory.ItemStack;

/**
 * Represents one item slot where items can be put in and out.
 */
public interface Slot extends Control {
	
	/**
	 * Gets the item of this slot
	 * @return the item of the slot
	 */
	public ItemStack getItem();
	
	/**
	 * Sets a new item to this slot
	 * @param item the new item
	 * @return the instance
	 */
	public Slot setItem(ItemStack item);
	
	/**
	 * Called when the user puts an item into the slot.
	 * @param item the stack that the user wants to put in. \
	 * The amount property in the stack will be calculated correctly, for example when the user right-clicks on the slot, it will only give one item.
	 * @return false if you want to cancel that.
	 */
	public boolean onItemPut(ItemStack item);
	
	/**
	 * Called when the user takes the item out of the slot
	 * @param item the stack that the user will get. \
	 * The amount property in the stack will be calculated correctly,for example when the user right-clicks on the slot, it would split the amount.
	 * @return false if you want to cancel that.
	 */
	public boolean onItemTake(ItemStack item);
	
	/**
	 * Called when the user shift-clicks on the slot.
	 * If the user holds shift while left-clicking, no other actions will be done.
	 */
	public void onItemShiftClicked();
	
	/**
	 * Called when the user wants to exchange the item on his cursor with that in the slot.
	 * @param current the item that is currently in the slot
	 * @param cursor the item that is on the cursor
	 * @return false, if you want to cancel the exchange
	 */
	public boolean onItemExchange(ItemStack current, ItemStack cursor);
	
	public Slot setDepth(int depth);
	public int getDepth();
}
