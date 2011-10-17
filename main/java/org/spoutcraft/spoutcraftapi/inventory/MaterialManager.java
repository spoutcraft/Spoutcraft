package org.spoutcraft.spoutcraftapi.inventory;

import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.design.BlockDesign;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.CustomItem;
import org.spoutcraft.spoutcraftapi.material.Material;

public interface MaterialManager {

	public float getFriction(org.spoutcraft.spoutcraftapi.material.Block block);
	
	public void setFriction(org.spoutcraft.spoutcraftapi.material.Block block, float friction);
	
	public void resetFriction(org.spoutcraft.spoutcraftapi.material.Block block);
	
	public float getHardness(org.spoutcraft.spoutcraftapi.material.Block block);
	
	public void setHardness(org.spoutcraft.spoutcraftapi.material.Block block, float hardness);
	
	public void resetHardness(org.spoutcraft.spoutcraftapi.material.Block block);
	
	public boolean isOpaque(org.spoutcraft.spoutcraftapi.material.Block block);
	
	public void setOpaque(org.spoutcraft.spoutcraftapi.material.Block block, boolean opacity);
	
	public void resetOpacity(org.spoutcraft.spoutcraftapi.material.Block block);
	
	public int getLightLevel(org.spoutcraft.spoutcraftapi.material.Block block);
	
	public void setLightLevel(org.spoutcraft.spoutcraftapi.material.Block block, int level);
	
	public void resetLightLevel(org.spoutcraft.spoutcraftapi.material.Block block);
	
	/**
	 * Sets the name of the item
	 * @param item to name
	 * @param name to set
	 */
	public void setItemName(Material item, String name);
	
	/**
	 * Resets the name of the item back to the notchian default
	 * @param item to reset
	 */
	public void resetName(Material item);
	
	 /**
	 * Sets the texture of the item, for use with pre-caching
	 * @param item to texture
	 * @param addon the addon to associate with the texture
	 * @param texture to set
	 */
	public void setItemTexture(Material item, Addon addon, String texture);
	
	/**
	 * Gets the custom texture of the item, or null if none exists, for use with pre-caching
	 * @param item to get the texture of
	 * @return texture
	 */

	public String getCustomItemTexture(Material item);
	
	/**
	 * Gets the addon associated with the custom texture of the item, or null if none exists
	 * @param item to get the texture of
	 * @return texture
	 */
	public String getCustomItemTextureAddon(Material item);

	/**
	 * Resets the texture of the item back to the notchian default
	 * @param item to reset
	 */
	public void resetTexture(Material item);
	
	/**
	 * Resets the names and textures of all items to the notchian defaults. Use with care.
	 */
	public void reset();
	
	/**
	 * Registers the id for a custom item.  The key should be unique.
	 * 
	 * The returned id should be used for accessing the item and is persistent between server restarts and reloads
	 *
	 * @param key Key of the new item
	 * @return the unique id or null on error
	 */
	public int registerCustomItemName(Addon addon, String key);
	
	/**
	 * Registers the CustomBlock to be placed by the specified CustomItem
	 * 
	 * @param item to use
	 * @param block to place
	 */
	public void setCustomItemBlock(CustomItem item, CustomBlock block);
	
	/**
	 * Creates an item stack of a custom block.
	 *
	 * @param block to make a stack of
	 * @param size the size of the item stack
	 * @return an ItemStack of that item
	 */
	public ItemStack getCustomItemStack(CustomBlock block, int size);
	
	/**
	 * Creates an item stack of a custom item.
	 * 
	 * @param item to make a stack of
	 * @param size of the item stack
	 * @return an ItemStack of that item
	 */
	public ItemStack getCustomItemStack(CustomItem item, int size);
	
	/**
	 * Removes the custom block override from this block
	 * 
	 * @param block to remove the override
	 * @return true if successful
	 */
	boolean removeBlockOverride(Block block);
	
	/**
	 * Overrides the block to be the customBlock
	 * 
	 * This can be used to set custom blocks at the location.
	 *
	 * @param block the block to override
	 * @param customBlock the custom block to use instead of the block
	 */
	public boolean overrideBlock(Block block, CustomBlock customBlock);
	
	/**
	 * Overrides the block at x y z to be the customBlock
	 * 
	 * This can be used to set custom blocks at the location.
	 *
	 * @param world the location is in
	 * @param x location
	 * @param y location
	 * @param z location
	 * @param customBlock the custom block to use at the location
	 */
	public boolean overrideBlock(World world, int x, int y, int z, CustomBlock customBlock);
	
	/**
	 * Sets the custom design for a blockId and meta data combination
	 * 
	 * @param blockId the blockId to override
	 * @param metaData the meta data to override
	 * @param design the design to use instead of the block
	 */
	public void setCustomBlockDesign(Material material, BlockDesign design);
	
	/**
	 * Checks if the specified block is a custom block or not
	 * 
	 * @param block to check
	 * @return true if block is custom
	 */
	public boolean isCustomBlock(Block block);
	
	/**
	 * Registers a SpoutRecipe to the server
	 * 
	 * @param recipe to register
	 * @return true if successful
	 */
	public boolean registerSpoutRecipe(Recipe recipe);
	
	/**
	 * Checks if the this item is a custom item or not
	 * 
	 * @param item to check
	 * @return true if it is a custom item
	 */
	public boolean isCustomItem(ItemStack item);
	
	/**
	 * Gets the custom item from an item stack
	 * 
	 * @param item stack to get the custom item from
	 * @return the custom item
	 */
	public CustomItem getCustomItem(ItemStack item);
	
	/**
	 * Registers an ItemStack to drop from the specified block
	 * 
	 * @param block that breaks
	 * @param item to drop
	 * @return the block that breaks
	 */
	public CustomBlock registerItemDrop(CustomBlock block, ItemStack item);
	
	/**
	 * Checks if the block has an item drop registered to it
	 * 
	 * @param block to check
	 * @return true if it has an item drop
	 */
	public boolean hasItemDrop(CustomBlock block);
	
	/**
	 * Gets the ItemStack that drops from the specified block, if it has one
	 * 
	 * @param block to get the drop from
	 * @return item stack of the drop
	 */
	public ItemStack getItemDrop(CustomBlock block);
}
