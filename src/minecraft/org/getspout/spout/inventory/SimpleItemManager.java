/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.getspout.spout.inventory;

import java.util.HashMap;

import org.getspout.spout.io.CustomTextureManager;
import org.spoutcraft.spoutcraftapi.inventory.ItemManager;

public class SimpleItemManager implements ItemManager{
	private final HashMap<ItemData, String> itemNames;
	private final HashMap<ItemData, String> customTextures;
	private final HashMap<ItemData, String> customTexturesPlugin;
	private final HashMap<ItemData, String> customNames;
	public SimpleItemManager() {
		itemNames = new HashMap<ItemData, String>(500);
		customTextures = new HashMap<ItemData, String>(100);
		customTexturesPlugin = new HashMap<ItemData, String>(100);
		customNames = new HashMap<ItemData, String>(100);
		itemNames.put(ItemData.getItemData(1), "Stone");
		itemNames.put(ItemData.getItemData(2), "Grass");
		itemNames.put(ItemData.getItemData(3), "Dirt");
		itemNames.put(ItemData.getItemData(4), "Cobblestone");
		itemNames.put(ItemData.getItemData(5), "Wooden Planks");
		itemNames.put(ItemData.getItemData(6, 0), "Sapling");
		itemNames.put(ItemData.getItemData(6, 1), "Spruce Sapling");
		itemNames.put(ItemData.getItemData(6, 2), "Birch Sapling");
		itemNames.put(ItemData.getItemData(7), "Bedrock");
		itemNames.put(ItemData.getItemData(8), "Water");
		itemNames.put(ItemData.getItemData(9), "Stationary Water");
		itemNames.put(ItemData.getItemData(10), "Lava");
		itemNames.put(ItemData.getItemData(11), "Stationary Lava");
		itemNames.put(ItemData.getItemData(12), "Sand");
		itemNames.put(ItemData.getItemData(13), "Gravel");
		itemNames.put(ItemData.getItemData(14), "Gold Ore");
		itemNames.put(ItemData.getItemData(15), "Iron Ore");
		itemNames.put(ItemData.getItemData(16), "Coal Ore");
		itemNames.put(ItemData.getItemData(17), "Wood");
		itemNames.put(ItemData.getItemData(18), "Leaves");
		itemNames.put(ItemData.getItemData(19), "Spone");
		itemNames.put(ItemData.getItemData(20), "Glass");
		itemNames.put(ItemData.getItemData(21), "Lapis Lazuli Ore");
		itemNames.put(ItemData.getItemData(22), "Lapis Lazuli Block");
		itemNames.put(ItemData.getItemData(23), "Dispenser");
		itemNames.put(ItemData.getItemData(24), "SandStone");
		itemNames.put(ItemData.getItemData(25), "Note Block");
		itemNames.put(ItemData.getItemData(26), "Bed");
		itemNames.put(ItemData.getItemData(27), "Powered Rail");
		itemNames.put(ItemData.getItemData(28), "Detector Rail");
		itemNames.put(ItemData.getItemData(29), "Sticky Piston");
		itemNames.put(ItemData.getItemData(30), "Cobweb");
		itemNames.put(ItemData.getItemData(31), "Tall Grass");
		itemNames.put(ItemData.getItemData(32), "Dead Shrubs");
		itemNames.put(ItemData.getItemData(33), "Piston");
		itemNames.put(ItemData.getItemData(34), "Piston (Head)");
		itemNames.put(ItemData.getItemData(35, 0), "Wool");
		itemNames.put(ItemData.getItemData(35, 1), "Orange Wool");
		itemNames.put(ItemData.getItemData(35, 2), "Magenta Wool");
		itemNames.put(ItemData.getItemData(35, 3), "Light Blue Wool");
		itemNames.put(ItemData.getItemData(35, 4), "Yellow Wool");
		itemNames.put(ItemData.getItemData(35, 5), "Light Green Wool");
		itemNames.put(ItemData.getItemData(35, 6), "Pink Wool");
		itemNames.put(ItemData.getItemData(35, 7), "Gray Wool");
		itemNames.put(ItemData.getItemData(35, 8), "Light Gray Wool");
		itemNames.put(ItemData.getItemData(35, 9), "Cyan Wool");
		itemNames.put(ItemData.getItemData(35, 10), "Purple Wool");
		itemNames.put(ItemData.getItemData(35, 11), "Blue Wool");
		itemNames.put(ItemData.getItemData(35, 12), "Brown Wool");
		itemNames.put(ItemData.getItemData(35, 13), "Dark Green Wool");
		itemNames.put(ItemData.getItemData(35, 14), "Red Wool");
		itemNames.put(ItemData.getItemData(35, 15), "Black Wool");
		itemNames.put(ItemData.getItemData(37), "Dandelion");
		itemNames.put(ItemData.getItemData(38), "Rose");
		itemNames.put(ItemData.getItemData(39), "Brown Mushroom");
		itemNames.put(ItemData.getItemData(40), "Red Mushroom");
		itemNames.put(ItemData.getItemData(41), "Gold Block");
		itemNames.put(ItemData.getItemData(42), "Iron Block");
		itemNames.put(ItemData.getItemData(43, 0), "Stone Double Slab");
		itemNames.put(ItemData.getItemData(43, 1), "Sandstone Double Slabs");
		itemNames.put(ItemData.getItemData(43, 2), "Wooden Double Slab");
		itemNames.put(ItemData.getItemData(43, 3), "Stone Double Slab");
		itemNames.put(ItemData.getItemData(44, 0), "Stone Slab");
		itemNames.put(ItemData.getItemData(44, 1), "Sandstone Slab");
		itemNames.put(ItemData.getItemData(44, 2), "Wooden Slab");
		itemNames.put(ItemData.getItemData(44, 3), "Stone Slab");
		itemNames.put(ItemData.getItemData(45), "Brick Block");
		itemNames.put(ItemData.getItemData(46), "TNT");
		itemNames.put(ItemData.getItemData(47), "Bookshelf");
		itemNames.put(ItemData.getItemData(48), "Moss Stone");
		itemNames.put(ItemData.getItemData(49), "Obsidian");
		itemNames.put(ItemData.getItemData(50), "Torch");
		itemNames.put(ItemData.getItemData(51), "Fire");
		itemNames.put(ItemData.getItemData(52), "Monster Spawner");
		itemNames.put(ItemData.getItemData(53), "Wooden Stairs");
		itemNames.put(ItemData.getItemData(54), "Chest");
		itemNames.put(ItemData.getItemData(55), "Redstone Wire");
		itemNames.put(ItemData.getItemData(56), "Diamond Ore");
		itemNames.put(ItemData.getItemData(57), "Diamond Block");
		itemNames.put(ItemData.getItemData(58), "Crafting Table");
		itemNames.put(ItemData.getItemData(59), "Seeds");
		itemNames.put(ItemData.getItemData(60), "Farmland");
		itemNames.put(ItemData.getItemData(61), "Furnace");
		itemNames.put(ItemData.getItemData(62), "Burning Furnace");
		itemNames.put(ItemData.getItemData(63), "Sign Post");
		itemNames.put(ItemData.getItemData(64), "Wooden Door");
		itemNames.put(ItemData.getItemData(65), "Ladders");
		itemNames.put(ItemData.getItemData(66), "Rails");
		itemNames.put(ItemData.getItemData(67), "Cobblestone Stairs");
		itemNames.put(ItemData.getItemData(68), "Wall Sign");
		itemNames.put(ItemData.getItemData(69), "Lever");
		itemNames.put(ItemData.getItemData(70), "Stone Pressure Plate");
		itemNames.put(ItemData.getItemData(71), "Iron Door");
		itemNames.put(ItemData.getItemData(72), "Wooden Pressure Plate");
		itemNames.put(ItemData.getItemData(73), "Redstone Ore");
		itemNames.put(ItemData.getItemData(74), "Glowing Redstone Ore");
		itemNames.put(ItemData.getItemData(75), "Redstone Torch");
		itemNames.put(ItemData.getItemData(76), "Redstone Torch (On)");
		itemNames.put(ItemData.getItemData(77), "Stone Button");
		itemNames.put(ItemData.getItemData(78), "Snow");
		itemNames.put(ItemData.getItemData(79), "Ice");
		itemNames.put(ItemData.getItemData(80), "Snow Block");
		itemNames.put(ItemData.getItemData(81), "Cactus");
		itemNames.put(ItemData.getItemData(82), "Clay Block");
		itemNames.put(ItemData.getItemData(83), "Sugar Cane");
		itemNames.put(ItemData.getItemData(84), "Jukebox");
		itemNames.put(ItemData.getItemData(85), "Fence");
		itemNames.put(ItemData.getItemData(86), "Pumpkin");
		itemNames.put(ItemData.getItemData(87), "Netherrack");
		itemNames.put(ItemData.getItemData(88), "Soul Sand");
		itemNames.put(ItemData.getItemData(89), "Glowstone Block");
		itemNames.put(ItemData.getItemData(90), "Portal");
		itemNames.put(ItemData.getItemData(91), "Jack 'o' Lantern");
		itemNames.put(ItemData.getItemData(92), "Cake Block");
		itemNames.put(ItemData.getItemData(93), "Redstone Repeater");
		itemNames.put(ItemData.getItemData(94), "Redstone Repeater (On)");
		itemNames.put(ItemData.getItemData(95), "Locked Chest");
		itemNames.put(ItemData.getItemData(96), "Trapdoor");
		itemNames.put(ItemData.getItemData(256), "Iron Shovel");
		itemNames.put(ItemData.getItemData(257), "Iron Pickaxe");
		itemNames.put(ItemData.getItemData(258), "Iron Axe");
		itemNames.put(ItemData.getItemData(259), "Flint and Steel");
		itemNames.put(ItemData.getItemData(260), "Apple");
		itemNames.put(ItemData.getItemData(261), "Bow");
		itemNames.put(ItemData.getItemData(262), "Arrow");
		itemNames.put(ItemData.getItemData(263, 0), "Coal");
		itemNames.put(ItemData.getItemData(263, 1), "Charcoal");
		itemNames.put(ItemData.getItemData(264), "Diamond");
		itemNames.put(ItemData.getItemData(265), "Iron Ingot");
		itemNames.put(ItemData.getItemData(266), "Gold Ingot");
		itemNames.put(ItemData.getItemData(267), "Iron Sword");
		itemNames.put(ItemData.getItemData(268), "Wooden Sword");
		itemNames.put(ItemData.getItemData(269), "Wooden Shovel");
		itemNames.put(ItemData.getItemData(270), "Wooden Pickaxe");
		itemNames.put(ItemData.getItemData(271), "Wooden Axe");
		itemNames.put(ItemData.getItemData(272), "Stone Sword");
		itemNames.put(ItemData.getItemData(273), "Stone Shovel");
		itemNames.put(ItemData.getItemData(274), "Stone Pickaxe");
		itemNames.put(ItemData.getItemData(275), "Stone Axe");
		itemNames.put(ItemData.getItemData(276), "Diamond Sword");
		itemNames.put(ItemData.getItemData(277), "Diamond Shovel");
		itemNames.put(ItemData.getItemData(278), "Diamond Pickaxe");
		itemNames.put(ItemData.getItemData(279), "Diamond Axe");
		itemNames.put(ItemData.getItemData(280), "Stick");
		itemNames.put(ItemData.getItemData(281), "Bowl");
		itemNames.put(ItemData.getItemData(282), "Mushroom Soup");
		itemNames.put(ItemData.getItemData(283), "Gold Sword");
		itemNames.put(ItemData.getItemData(284), "Gold Shovel");
		itemNames.put(ItemData.getItemData(285), "Gold Pickaxe");
		itemNames.put(ItemData.getItemData(286), "Gold Axe");
		itemNames.put(ItemData.getItemData(287), "String");
		itemNames.put(ItemData.getItemData(288), "Feather");
		itemNames.put(ItemData.getItemData(289), "Gunpowder");
		itemNames.put(ItemData.getItemData(290), "Wooden Hoe");
		itemNames.put(ItemData.getItemData(291), "Stone Hoe");
		itemNames.put(ItemData.getItemData(292), "Iron Hoe");
		itemNames.put(ItemData.getItemData(293), "Diamond Hoe");
		itemNames.put(ItemData.getItemData(294), "Gold Hoe");
		itemNames.put(ItemData.getItemData(295), "Seeds");
		itemNames.put(ItemData.getItemData(296), "Wheat");
		itemNames.put(ItemData.getItemData(297), "Bread");
		itemNames.put(ItemData.getItemData(298), "Leather Cap");
		itemNames.put(ItemData.getItemData(299), "Leather Tunic");
		itemNames.put(ItemData.getItemData(300), "Leather Boots");
		itemNames.put(ItemData.getItemData(301), "Leather Boots");
		itemNames.put(ItemData.getItemData(302), "Chain Helmet");
		itemNames.put(ItemData.getItemData(303), "Chain Chestplate");
		itemNames.put(ItemData.getItemData(304), "Chain Leggings");
		itemNames.put(ItemData.getItemData(305), "Chain Boots");
		itemNames.put(ItemData.getItemData(306), "Iron Helmet");
		itemNames.put(ItemData.getItemData(307), "Iron Chestplate");
		itemNames.put(ItemData.getItemData(308), "Iron Leggings");
		itemNames.put(ItemData.getItemData(309), "Iron Boots");
		itemNames.put(ItemData.getItemData(310), "Diamond Helmet");
		itemNames.put(ItemData.getItemData(311), "Diamond Chestplate");
		itemNames.put(ItemData.getItemData(312), "Diamond Leggings");
		itemNames.put(ItemData.getItemData(313), "Diamond Boots");
		itemNames.put(ItemData.getItemData(314), "Gold Helmet");
		itemNames.put(ItemData.getItemData(315), "Gold Chestplate");
		itemNames.put(ItemData.getItemData(316), "Gold Leggings");
		itemNames.put(ItemData.getItemData(317), "Gold Boots");
		itemNames.put(ItemData.getItemData(318), "Flint");
		itemNames.put(ItemData.getItemData(319), "Raw Porkchop");
		itemNames.put(ItemData.getItemData(320), "Cooked Porkchop");
		itemNames.put(ItemData.getItemData(321), "Paintings");
		itemNames.put(ItemData.getItemData(322), "Golden Apple");
		itemNames.put(ItemData.getItemData(323), "Sign");
		itemNames.put(ItemData.getItemData(324), "Wooden Door");
		itemNames.put(ItemData.getItemData(325), "Bucket");
		itemNames.put(ItemData.getItemData(326), "Water Bucket");
		itemNames.put(ItemData.getItemData(327), "Lava Bucket");
		itemNames.put(ItemData.getItemData(328), "Minecart");
		itemNames.put(ItemData.getItemData(329), "Saddle");
		itemNames.put(ItemData.getItemData(330), "Iron Door");
		itemNames.put(ItemData.getItemData(331), "Redstone");
		itemNames.put(ItemData.getItemData(332), "Snowball");
		itemNames.put(ItemData.getItemData(333), "Boat");
		itemNames.put(ItemData.getItemData(334), "Leather");
		itemNames.put(ItemData.getItemData(335), "Milk");
		itemNames.put(ItemData.getItemData(336), "Brick");
		itemNames.put(ItemData.getItemData(337), "Clay");
		itemNames.put(ItemData.getItemData(338), "Sugar Canes");
		itemNames.put(ItemData.getItemData(339), "Paper");
		itemNames.put(ItemData.getItemData(340), "Book");
		itemNames.put(ItemData.getItemData(341), "Slimeball");
		itemNames.put(ItemData.getItemData(342), "Minecart with Chest");
		itemNames.put(ItemData.getItemData(343), "Minecart with Furnace");
		itemNames.put(ItemData.getItemData(344), "Egg");
		itemNames.put(ItemData.getItemData(345), "Compass");
		itemNames.put(ItemData.getItemData(346), "Fishing Rod");
		itemNames.put(ItemData.getItemData(347), "Clock");
		itemNames.put(ItemData.getItemData(348), "Glowstone Dust");
		itemNames.put(ItemData.getItemData(349), "Raw Fish");
		itemNames.put(ItemData.getItemData(350), "Cooked Fish");
		itemNames.put(ItemData.getItemData(351, 0), "Ink Sac");
		itemNames.put(ItemData.getItemData(351, 1), "Rose Red");
		itemNames.put(ItemData.getItemData(351, 2), "Cactus Green");
		itemNames.put(ItemData.getItemData(351, 3), "Cocoa Beans");
		itemNames.put(ItemData.getItemData(351, 4), "Lapis Lazuli");
		itemNames.put(ItemData.getItemData(351, 5), "Purple Dye");
		itemNames.put(ItemData.getItemData(351, 6), "Cyan Dye");
		itemNames.put(ItemData.getItemData(351, 7), "Light Gray Dye");
		itemNames.put(ItemData.getItemData(351, 8), "Gray Dye");
		itemNames.put(ItemData.getItemData(351, 9), "Pink Dye");
		itemNames.put(ItemData.getItemData(351, 10), "Lime Dye");
		itemNames.put(ItemData.getItemData(351, 11), "Dandelion Yellow");
		itemNames.put(ItemData.getItemData(351, 12), "Light Blue Dye");
		itemNames.put(ItemData.getItemData(351, 13), "Magenta Dye");
		itemNames.put(ItemData.getItemData(351, 14), "Orange Dye");
		itemNames.put(ItemData.getItemData(351, 15), "Bone Meal");
		itemNames.put(ItemData.getItemData(352), "Bone");
		itemNames.put(ItemData.getItemData(353), "Sugar");
		itemNames.put(ItemData.getItemData(354), "Cake");
		itemNames.put(ItemData.getItemData(355), "Bed");
		itemNames.put(ItemData.getItemData(356), "Redstone Repeater");
		itemNames.put(ItemData.getItemData(357), "Cookie");
		itemNames.put(ItemData.getItemData(358), "Map");
		itemNames.put(ItemData.getItemData(359), "Shears");
		itemNames.put(ItemData.getItemData(2256), "Music Disc");
		itemNames.put(ItemData.getItemData(2257), "Music Disc");
	}

	@Override
	public String getItemName(int item) {
		return getItemName(item, (short)0);
	}

	@Override
	public String getItemName(int item, short data) {
		ItemData info = ItemData.getItemData(item, data);
		if (customNames.containsKey(info)) {
			return customNames.get(info);
		}
		return itemNames.get(info);
	}

	@Override
	public void setItemName(int item, String name) {
		setItemName(item, (short)0, name);
	}

	@Override
	public void setItemName(int item, short data, String name) {
		customNames.put(ItemData.getItemData(item, data), name);
	}
	
	@Override
	public void resetName(int item) {
		resetName(item,(byte) 0);
	}

	@Override
	public void resetName(int item, short data) {
		ItemData info = ItemData.getItemData(item, data);
		if (customNames.containsKey(info)) {
			customNames.remove(info);
		}
	}

	@Override
	public String getCustomItemName(int item) {
		return getCustomItemName(item, (short)0);
	}

	@Override
	public String getCustomItemName(int item, short data) {
		ItemData info = ItemData.getItemData(item, data);
		if (customNames.containsKey(info)) {
			return customNames.get(info);
		}
		return null;
	}

	@Override
	public void setItemTexture(int item, String texture) {
		setItemTexture(item, (short) 0, texture);
	}

	@Override
	public void setItemTexture(int item, short data, String texture) {
		setItemTexture(item, data, null, texture);
	}
	
	@Override
	public void setItemTexture(int item, short data, String pluginName, String texture) {
		ItemData newKey = ItemData.getItemData(item, data);
		customTextures.put(newKey, texture);
		if (pluginName == null) {
			customTexturesPlugin.remove(newKey);
		} else {
			customTexturesPlugin.put(newKey, pluginName);
		}
		if (texture != null && pluginName == null) {
			CustomTextureManager.downloadTexture(texture);
		}
	}
	
	@Override
	public String getCustomItemTexture(int item) {
		return getCustomItemTexture(item, (short) 0);
	}

	@Override
	public String getCustomItemTexture(int item, short data) {
		ItemData info = ItemData.getItemData(item, data);
		if (customTextures.containsKey(info))
			return customTextures.get(info);
		return null;
	}
	
	@Override
	public String getCustomItemTexturePlugin(int item, short data) {
		ItemData info = ItemData.getItemData(item, data);
		if (customTexturesPlugin.containsKey(info))
			return customTexturesPlugin.get(info);
		return null;
	}

	@Override
	public void resetTexture(int item) {
		resetTexture(item, (short) 0);
	}

	@Override
	public void resetTexture(int item, short data) {
		ItemData info = ItemData.getItemData(item, data);
		if (customTextures.containsKey(info)) {
			customTextures.remove(info);
		}
	}

	@Override
	public void reset() {
		customNames.clear();
		customTextures.clear();
	}

}
