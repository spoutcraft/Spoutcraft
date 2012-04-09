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
			int window = Minecraft.theMinecraft.thePlayer.craftingInventory.windowId;
			ItemStack replacement = inventory.mainInventory[slot].copy();

			Minecraft.theMinecraft.playerController.windowClick(window, slot < 9 ? slot + 36 : slot, 0, false, Minecraft.theMinecraft.thePlayer);
			Minecraft.theMinecraft.playerController.windowClick(window, inventory.currentItem + 36, 0, false, Minecraft.theMinecraft.thePlayer);
			((EntityClientPlayerMP)Minecraft.theMinecraft.thePlayer).sendQueue.addToSendQueue(new Packet101CloseWindow(window));
			((EntityClientPlayerMP)Minecraft.theMinecraft.thePlayer).sendQueue.queued = true;
			((EntityClientPlayerMP)Minecraft.theMinecraft.thePlayer).sendQueue.packetQueueTime = System.currentTimeMillis() + 30L;

			ItemStack current = inventory.mainInventory[inventory.currentItem];
			current.stackSize = replacement.stackSize;
			current.setItemDamage(replacement.getItemDamage());
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
