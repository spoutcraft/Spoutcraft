/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org//>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.api.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.src.Enchantment;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagString;

import org.spoutcraft.api.material.CustomBlock;
import org.spoutcraft.api.material.CustomItem;
import org.spoutcraft.api.material.Material;
import org.spoutcraft.api.material.MaterialData;

/**
 * Represents a stack of items
 */
public class ItemStack implements Cloneable{
	private int type;
	private int amount = 0;
	private short durability = 0;

	private String displayName = null;
	private List<String> lore = null;
	private HashMap<Enchantment, Integer> enchants = null;

	public ItemStack(final int type) {
		this(type, 0);
	}

	public ItemStack(final Material type) {
		this(type, 0);
	}

	public ItemStack(final int type, final int amount) {
		this(type, amount, (short) 0);
	}

	public ItemStack(final Material type, final int amount) {
		this(type.getRawId(), amount);
	}

	public ItemStack(final int type, final int amount, final short damage) {
		this(type, amount, damage, null);
	}

	public ItemStack(final Material type, final int amount, final short damage) {
		this(type.getRawId(), amount, damage);
	}

	public ItemStack(final int type, final int amount, final short damage, final Byte data) {
		this.type = type;
		this.amount = amount;
		this.durability = damage;
		if (data != null) {
			this.durability = data;
		}
	}

	public ItemStack(final Material type, final int amount, final short damage, final Byte data) {
		this(type.getRawId(), amount, damage, data);
	}

	public ItemStack(CustomItem item) {
		this(item.getRawId(), 1, (short)item.getRawData());
	}

	public ItemStack(CustomItem item, int amount) {
		this(item.getRawId(), amount, (short)item.getRawData());
	}

	public ItemStack(CustomBlock block) {
		this(block.getBlockItem());
	}

	public ItemStack(CustomBlock block, int amount) {
		this(block.getBlockItem(), amount);
	}

	/**
	 * Checks if this item has a custom display name
	 * @return - true if this item has a custom display name, false otherwise.
	 */
	public boolean hasDisplayName() {
		if (displayName != null && !displayName.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * sets the custom display name for this item
	 * @param name - The custom name for this item
	 */
	public void setDisplayName(String name) {
		this.displayName = name;
	}

	/**
	 * gets the custom display name for this item
	 * @return - the custom display name.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Checks if this item has enchantments.
	 * @return - true if this item has enchantments.
	 */
	public boolean hasEnchants() {
		if (enchants != null && enchants.size() > 0){
			return true;
		}
		return false;
	}

	/**
	 * sets the enchantments for this item.
	 * @param enchants - a hashmap of enchantments.
	 */
	public void setEnchants(HashMap<Enchantment, Integer> enchants) {
		this.enchants = enchants;
	}

	/**
	 * gets this items enchantments
	 * @return - the hashmap of this items enchantments.
	 */
	public HashMap<Enchantment, Integer> getEnchants() {
		return enchants;
	}

	/**
	 * Checks if this item has lore
	 * @return - true if this item has lore, false otherwise.
	 */
	public boolean hasLore() {
		if (lore != null && lore.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Sets this item lore
	 * @param lore - a List of Strings representing the lore for this item.
	 */
	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	/**
	 * gets this items lore.
	 * @return - A list of Strings representing this item's lore.
	 */
	public List<String> getLore() {
		return lore;
	}

	/**
	 * gets the net.minecraft.src.ItemStack version of this item
	 * @return - this itemstack in NMS form.
	 */
	public net.minecraft.src.ItemStack asNMSItenStack() {
		net.minecraft.src.ItemStack itemstack = new net.minecraft.src.ItemStack(getTypeId(), getAmount(), getDurability());
		if (hasDisplayName()) {
			itemstack.setItemName(getDisplayName());
		}

		if (hasEnchants()) {
			for (Entry<Enchantment, Integer> entry : getEnchants().entrySet()) {
				itemstack.addEnchantment(entry.getKey(), entry.getValue());
			}
		}

		if (hasLore()) {
			if (itemstack.stackTagCompound == null) {
				itemstack.stackTagCompound = new NBTTagCompound();
			}
			if (!itemstack.stackTagCompound.hasKey("display")) {
				itemstack.stackTagCompound.setCompoundTag("display", new NBTTagCompound());
			}

			NBTTagList loreTagList = new NBTTagList();

			for (String l : getLore()) {
				loreTagList.appendTag(new NBTTagString(l));
			}

			itemstack.stackTagCompound.getCompoundTag("display").setTag("Lore", loreTagList);
		}

		return itemstack;
	}

	/**
	 * Is true if the item is a custom item, not in the vanilla game
	 * @return true if custom item
	 */
	public boolean isCustomItem() {
		return getType() instanceof CustomItem;
	}

	/**
	 * Gets the type of this item
	 *
	 * @return Type of the items in this stack
	 */
	public Material getType() {
		return MaterialData.getMaterial(type);
	}

	/**
	 * Sets the type of this item<br />
	 * <br />
	 * Note that in doing so you will reset the MaterialData for this stack
	 *
	 * @param type New type to set the items in this stack to
	 */
	public void setType(Material type) {
		setTypeId(type.getRawId());
	}

	/**
	 * Gets the type id of this item
	 *
	 * @return Type Id of the items in this stack
	 */
	public int getTypeId() {
		return type;
	}

	/**
	 * Sets the type id of this item<br />
	 * <br />
	 * Note that in doing so you will reset the MaterialData for this stack
	 *
	 * @param type New type id to set the items in this stack to
	 */
	public void setTypeId(int type) {
		this.type = type;
	}

	/**
	 * Gets the amount of items in this stack
	 *
	 * @return Amount of items in this stick
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Sets the amount of items in this stack
	 *
	 * @param amount New amount of items in this stack
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * Sets the MaterialData for this stack of items
	 *
	 * @param amount New MaterialData for this item
	 */
	public void setData(Material data) {
		setTypeId(data.getRawId());
		setDurability((short) data.getRawData());
	}

	/**
	 * Sets the durability of this item
	 *
	 * @param durability Durability of this item
	 */
	public void setDurability(final short durability) {
		this.durability = durability;
	}

	/**
	 * Gets the durability of this item
	 *
	 * @return Durability of this item
	 */
	public short getDurability() {
		return durability;
	}

	/**
	 * Get the maximum stacksize for the material hold in this ItemStack
	 * Returns -1 if it has no idea.
	 *
	 * @return The maximum you can stack this material to.
	 */
	public int getMaxStackSize() {
		return -1;
	}

	@Override
	public String toString() {
		return "ItemStack{" + getType().getName() + " x " + getAmount() + "}";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ItemStack)) {
			return false;
		}

		ItemStack item = (ItemStack) obj;

		return item.getAmount() == getAmount() && item.getTypeId() == getTypeId();
	}

	@Override
	public ItemStack clone() {
		ItemStack item = new ItemStack(type, amount, durability);
		item.setDisplayName(displayName);
		item.setEnchants(enchants);
		item.setLore(lore);
		return item;
	}

	@Override
	public int hashCode() {
		int hash = 11;

		hash = hash * 19 + 7 * getTypeId(); // Overriding hashCode since equals is overridden, it's just
		hash = hash * 7 + 23 * getAmount(); // too bad these are mutable values... Q_Q
		return hash;
	}
}
