package org.spoutcraft.client.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Container;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet101CloseWindow;
import net.minecraft.src.Slot;

public class InventoryUtil {
	
	public static void replaceTool(int id) {
		int slot = -1;
		InventoryPlayer inventory = Minecraft.theMinecraft.thePlayer.inventory;
		for (int i = 0; i < inventory.mainInventory.length; i++) {
			if (inventory.mainInventory[i] != null && i != inventory.currentItem) {
				if (inventory.mainInventory[i].itemID == id) {
					if (!Minecraft.theMinecraft.isMultiplayerWorld()) {
						inventory.mainInventory[inventory.currentItem].stackSize = inventory.mainInventory[i].stackSize;
						inventory.mainInventory[inventory.currentItem].setItemDamage(inventory.mainInventory[i].getItemDamage());
						inventory.mainInventory[i] = null;
					}
					slot = i;
					break;
				}
			}
		}
		if (Minecraft.theMinecraft.isMultiplayerWorld() && slot > -1) {
			ItemStack replacement = inventory.mainInventory[slot].copy();
			
			int window = Minecraft.theMinecraft.thePlayer.craftingInventory.windowId;
			Minecraft.theMinecraft.playerController.windowClick(window, slot + 36, 0, false, Minecraft.theMinecraft.thePlayer);
			Minecraft.theMinecraft.playerController.windowClick(window, inventory.currentItem + 36, 0, false, Minecraft.theMinecraft.thePlayer);
			
			((EntityClientPlayerMP)Minecraft.theMinecraft.thePlayer).sendQueue.addToSendQueue(new Packet101CloseWindow(window));
			
			inventory.mainInventory[inventory.currentItem].stackSize = replacement.stackSize;
			inventory.mainInventory[inventory.currentItem].setItemDamage(replacement.getItemDamage());
			inventory.mainInventory[slot] = null;
		}
	}
	
	public static Slot getSlotFromPosition(int pos, Container inventorySlots) {
		for (int i = 0; i < inventorySlots.inventorySlots.size(); i++) {
			if (inventorySlots.inventorySlots.get(i) != null) {
				if (((Slot)inventorySlots.inventorySlots.get(i)).slotIndex == pos) {
					return ((Slot)inventorySlots.inventorySlots.get(i));
				}
			}
		}
		return null;
	}
}
