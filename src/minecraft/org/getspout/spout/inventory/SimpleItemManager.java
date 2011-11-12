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

import java.util.Iterator;

import gnu.trove.map.hash.TIntByteHashMap;
import gnu.trove.map.hash.TIntIntHashMap;
import org.spoutcraft.spoutcraftapi.inventory.ItemManager;
import org.spoutcraft.spoutcraftapi.material.Block;
import org.spoutcraft.spoutcraftapi.material.Material;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.util.map.TIntPairFloatHashMap;
import org.spoutcraft.spoutcraftapi.util.map.TIntPairObjectHashMap;

@SuppressWarnings("deprecation")
public class SimpleItemManager implements ItemManager {
	private final TIntPairFloatHashMap originalHardness = new TIntPairFloatHashMap();
	private final TIntPairFloatHashMap originalFriction = new TIntPairFloatHashMap();
	private final TIntByteHashMap originalOpacity = new TIntByteHashMap();
	private final TIntIntHashMap originalLight = new TIntIntHashMap();

	private final TIntPairObjectHashMap<String> itemNames = new TIntPairObjectHashMap<String>(500);
	private final TIntPairObjectHashMap<String> customNames = new TIntPairObjectHashMap<String>(100);
	private final TIntPairObjectHashMap<String> customTextures = new TIntPairObjectHashMap<String>(100);
	private final TIntPairObjectHashMap<String> customTexturesPlugin = new TIntPairObjectHashMap<String>(100);

	public SimpleItemManager() {
		itemNames.put(1, 0, "Stone");
		itemNames.put(2, 0, "Grass");
		itemNames.put(3, 0, "Dirt");
		itemNames.put(4, 0, "Cobblestone");
		itemNames.put(5, 0, "Wooden Planks");
		itemNames.put(6, 0, "Sapling");
		itemNames.put(6, 1, "Spruce Sapling");
		itemNames.put(6, 2, "Birch Sapling");
		itemNames.put(7, 0, "Bedrock");
		itemNames.put(8, 0, "Water");
		itemNames.put(9, 0, "Stationary Water");
		itemNames.put(10, 0, "Lava");
		itemNames.put(11, 0, "Stationary Lava");
		itemNames.put(12, 0, "Sand");
		itemNames.put(13, 0, "Gravel");
		itemNames.put(14, 0, "Gold Ore");
		itemNames.put(15, 0, "Iron Ore");
		itemNames.put(16, 0, "Coal Ore");
		itemNames.put(17, 0, "Wood");
		itemNames.put(18, 0, "Leaves");
		itemNames.put(19, 0, "Spone");
		itemNames.put(20, 0, "Glass");
		itemNames.put(21, 0, "Lapis Lazuli Ore");
		itemNames.put(22, 0, "Lapis Lazuli Block");
		itemNames.put(23, 0, "Dispenser");
		itemNames.put(24, 0, "SandStone");
		itemNames.put(25, 0, "Note Block");
		itemNames.put(26, 0, "Bed");
		itemNames.put(27, 0, "Powered Rail");
		itemNames.put(28, 0, "Detector Rail");
		itemNames.put(29, 0, "Sticky Piston");
		itemNames.put(30, 0, "Cobweb");
		itemNames.put(31, 0, "Dead Grass");
		itemNames.put(31, 1, "Tall Grass");
		itemNames.put(31, 2, "Fern");
		itemNames.put(32, 0, "Dead Shrubs");
		itemNames.put(33, 0, "Piston");
		itemNames.put(34, 0, "Piston (Head)");
		itemNames.put(35, 0, "Wool");
		itemNames.put(35, 1, "Orange Wool");
		itemNames.put(35, 2, "Magenta Wool");
		itemNames.put(35, 3, "Light Blue Wool");
		itemNames.put(35, 4, "Yellow Wool");
		itemNames.put(35, 5, "Light Green Wool");
		itemNames.put(35, 6, "Pink Wool");
		itemNames.put(35, 7, "Gray Wool");
		itemNames.put(35, 8, "Light Gray Wool");
		itemNames.put(35, 9, "Cyan Wool");
		itemNames.put(35, 10, "Purple Wool");
		itemNames.put(35, 11, "Blue Wool");
		itemNames.put(35, 12, "Brown Wool");
		itemNames.put(35, 13, "Dark Green Wool");
		itemNames.put(35, 14, "Red Wool");
		itemNames.put(35, 15, "Black Wool");
		itemNames.put(37, 0, "Dandelion");
		itemNames.put(38, 0, "Rose");
		itemNames.put(39, 0, "Brown Mushroom");
		itemNames.put(40, 0, "Red Mushroom");
		itemNames.put(41, 0, "Gold Block");
		itemNames.put(42, 0, "Iron Block");
		itemNames.put(43, 0, "Stone Double Slab");
		itemNames.put(43, 1, "Sandstone Double Slabs");
		itemNames.put(43, 2, "Wooden Double Slabs");
		itemNames.put(43, 3, "Stone Double Slabs");
		itemNames.put(43, 4, "Brick Double Slabs");
		itemNames.put(43, 5, "Stone Brick Double Slabs");
		itemNames.put(44, 0, "Stone Slab");
		itemNames.put(44, 1, "Sandstone Slab");
		itemNames.put(44, 2, "Wooden Slab");
		itemNames.put(44, 3, "Stone Slab");
		itemNames.put(44, 4, "Brick Slab");
		itemNames.put(44, 5, "Stone Brick Slab");
		itemNames.put(45, 0, "Brick Block");
		itemNames.put(46, 0, "TNT");
		itemNames.put(47, 0, "Bookshelf");
		itemNames.put(48, 0, "Moss Stone");
		itemNames.put(49, 0, "Obsidian");
		itemNames.put(50, 0, "Torch");
		itemNames.put(51, 0, "Fire");
		itemNames.put(52, 0, "Monster Spawner");
		itemNames.put(53, 0, "Wooden Stairs");
		itemNames.put(54, 0, "Chest");
		itemNames.put(55, 0, "Redstone Wire");
		itemNames.put(56, 0, "Diamond Ore");
		itemNames.put(57, 0, "Diamond Block");
		itemNames.put(58, 0, "Crafting Table");
		itemNames.put(59, 0, "Seeds");
		itemNames.put(60, 0, "Farmland");
		itemNames.put(61, 0, "Furnace");
		itemNames.put(62, 0, "Burning Furnace");
		itemNames.put(63, 0, "Sign Post");
		itemNames.put(64, 0, "Wooden Door");
		itemNames.put(65, 0, "Ladders");
		itemNames.put(66, 0, "Rails");
		itemNames.put(67, 0, "Cobblestone Stairs");
		itemNames.put(68, 0, "Wall Sign");
		itemNames.put(69, 0, "Lever");
		itemNames.put(70, 0, "Stone Pressure Plate");
		itemNames.put(71, 0, "Iron Door");
		itemNames.put(72, 0, "Wooden Pressure Plate");
		itemNames.put(73, 0, "Redstone Ore");
		itemNames.put(74, 0, "Glowing Redstone Ore");
		itemNames.put(75, 0, "Redstone Torch");
		itemNames.put(76, 0, "Redstone Torch (On)");
		itemNames.put(77, 0, "Stone Button");
		itemNames.put(78, 0, "Snow");
		itemNames.put(79, 0, "Ice");
		itemNames.put(80, 0, "Snow Block");
		itemNames.put(81, 0, "Cactus");
		itemNames.put(82, 0, "Clay Block");
		itemNames.put(83, 0, "Sugar Cane");
		itemNames.put(84, 0, "Jukebox");
		itemNames.put(85, 0, "Fence");
		itemNames.put(86, 0, "Pumpkin");
		itemNames.put(87, 0, "Netherrack");
		itemNames.put(88, 0, "Soul Sand");
		itemNames.put(89, 0, "Glowstone Block");
		itemNames.put(90, 0, "Portal");
		itemNames.put(91, 0, "Jack 'o' Lantern");
		itemNames.put(92, 0, "Cake Block");
		itemNames.put(93, 0, "Redstone Repeater");
		itemNames.put(94, 0, "Redstone Repeater (On)");
		itemNames.put(95, 0, "Locked Chest");
		itemNames.put(96, 0, "Trapdoor");
		itemNames.put(97, 0, "Silverfish Stone");
		itemNames.put(98, 0, "Stone Brick");
		itemNames.put(99, 0, "Huge Red Mushroom");
		itemNames.put(100, 0, "Huge Brown Mushroom");
		itemNames.put(101, 0, "Iron Bars");
		itemNames.put(102, 0, "Glass Pane");
		itemNames.put(103, 0, "Watermelon");
		itemNames.put(104, 0, "Pumpkin Stem");
		itemNames.put(105, 0, "Melon Stem");
		itemNames.put(106, 0, "Vines");
		itemNames.put(107, 0, "Fence Gate");
		itemNames.put(108, 0, "Brick Stairs");
		itemNames.put(109, 0, "Stone Brick Stairs");

		itemNames.put(256, 0, "Iron Shovel");
		itemNames.put(257, 0, "Iron Pickaxe");
		itemNames.put(258, 0, "Iron Axe");
		itemNames.put(259, 0, "Flint and Steel");
		itemNames.put(260, 0, "Apple");
		itemNames.put(261, 0, "Bow");
		itemNames.put(262, 0, "Arrow");
		itemNames.put(263, 0, "Coal");
		itemNames.put(263, 1, "Charcoal");
		itemNames.put(264, 0, "Diamond");
		itemNames.put(265, 0, "Iron Ingot");
		itemNames.put(266, 0, "Gold Ingot");
		itemNames.put(267, 0, "Iron Sword");
		itemNames.put(268, 0, "Wooden Sword");
		itemNames.put(269, 0, "Wooden Shovel");
		itemNames.put(270, 0, "Wooden Pickaxe");
		itemNames.put(271, 0, "Wooden Axe");
		itemNames.put(272, 0, "Stone Sword");
		itemNames.put(273, 0, "Stone Shovel");
		itemNames.put(274, 0, "Stone Pickaxe");
		itemNames.put(275, 0, "Stone Axe");
		itemNames.put(276, 0, "Diamond Sword");
		itemNames.put(277, 0, "Diamond Shovel");
		itemNames.put(278, 0, "Diamond Pickaxe");
		itemNames.put(279, 0, "Diamond Axe");
		itemNames.put(280, 0, "Stick");
		itemNames.put(281, 0, "Bowl");
		itemNames.put(282, 0, "Mushroom Soup");
		itemNames.put(283, 0, "Gold Sword");
		itemNames.put(284, 0, "Gold Shovel");
		itemNames.put(285, 0, "Gold Pickaxe");
		itemNames.put(286, 0, "Gold Axe");
		itemNames.put(287, 0, "String");
		itemNames.put(288, 0, "Feather");
		itemNames.put(289, 0, "Gunpowder");
		itemNames.put(290, 0, "Wooden Hoe");
		itemNames.put(291, 0, "Stone Hoe");
		itemNames.put(292, 0, "Iron Hoe");
		itemNames.put(293, 0, "Diamond Hoe");
		itemNames.put(294, 0, "Gold Hoe");
		itemNames.put(295, 0, "Seeds");
		itemNames.put(296, 0, "Wheat");
		itemNames.put(297, 0, "Bread");
		itemNames.put(298, 0, "Leather Cap");
		itemNames.put(299, 0, "Leather Tunic");
		itemNames.put(300, 0, "Leather Boots");
		itemNames.put(301, 0, "Leather Boots");
		itemNames.put(302, 0, "Chain Helmet");
		itemNames.put(303, 0, "Chain Chestplate");
		itemNames.put(304, 0, "Chain Leggings");
		itemNames.put(305, 0, "Chain Boots");
		itemNames.put(306, 0, "Iron Helmet");
		itemNames.put(307, 0, "Iron Chestplate");
		itemNames.put(308, 0, "Iron Leggings");
		itemNames.put(309, 0, "Iron Boots");
		itemNames.put(310, 0, "Diamond Helmet");
		itemNames.put(311, 0, "Diamond Chestplate");
		itemNames.put(312, 0, "Diamond Leggings");
		itemNames.put(313, 0, "Diamond Boots");
		itemNames.put(314, 0, "Gold Helmet");
		itemNames.put(315, 0, "Gold Chestplate");
		itemNames.put(316, 0, "Gold Leggings");
		itemNames.put(317, 0, "Gold Boots");
		itemNames.put(318, 0, "Flint");
		itemNames.put(319, 0, "Raw Porkchop");
		itemNames.put(320, 0, "Cooked Porkchop");
		itemNames.put(321, 0, "Paintings");
		itemNames.put(322, 0, "Golden Apple");
		itemNames.put(323, 0, "Sign");
		itemNames.put(324, 0, "Wooden Door");
		itemNames.put(325, 0, "Bucket");
		itemNames.put(326, 0, "Water Bucket");
		itemNames.put(327, 0, "Lava Bucket");
		itemNames.put(328, 0, "Minecart");
		itemNames.put(329, 0, "Saddle");
		itemNames.put(330, 0, "Iron Door");
		itemNames.put(331, 0, "Redstone");
		itemNames.put(332, 0, "Snowball");
		itemNames.put(333, 0, "Boat");
		itemNames.put(334, 0, "Leather");
		itemNames.put(335, 0, "Milk");
		itemNames.put(336, 0, "Brick");
		itemNames.put(337, 0, "Clay");
		itemNames.put(338, 0, "Sugar Canes");
		itemNames.put(339, 0, "Paper");
		itemNames.put(340, 0, "Book");
		itemNames.put(341, 0, "Slimeball");
		itemNames.put(342, 0, "Minecart with Chest");
		itemNames.put(343, 0, "Minecart with Furnace");
		itemNames.put(344, 0, "Egg");
		itemNames.put(345, 0, "Compass");
		itemNames.put(346, 0, "Fishing Rod");
		itemNames.put(347, 0, "Clock");
		itemNames.put(348, 0, "Glowstone Dust");
		itemNames.put(349, 0, "Raw Fish");
		itemNames.put(350, 0, "Cooked Fish");
		itemNames.put(351, 0, "Ink Sac");
		itemNames.put(351, 1, "Rose Red");
		itemNames.put(351, 2, "Cactus Green");
		itemNames.put(351, 3, "Cocoa Beans");
		itemNames.put(351, 4, "Lapis Lazuli");
		itemNames.put(351, 5, "Purple Dye");
		itemNames.put(351, 6, "Cyan Dye");
		itemNames.put(351, 7, "Light Gray Dye");
		itemNames.put(351, 8, "Gray Dye");
		itemNames.put(351, 9, "Pink Dye");
		itemNames.put(351, 10, "Lime Dye");
		itemNames.put(351, 11, "Dandelion Yellow");
		itemNames.put(351, 12, "Light Blue Dye");
		itemNames.put(351, 13, "Magenta Dye");
		itemNames.put(351, 14, "Orange Dye");
		itemNames.put(351, 15, "Bone Meal");
		itemNames.put(352, 0, "Bone");
		itemNames.put(353, 0, "Sugar");
		itemNames.put(354, 0, "Cake");
		itemNames.put(355, 0, "Bed");
		itemNames.put(356, 0, "Redstone Repeater");
		itemNames.put(357, 0, "Cookie");
		itemNames.put(358, 0, "Map");
		itemNames.put(359, 0, "Shears");
		itemNames.put(2256, 0, "Music Disc");
		itemNames.put(2257, 0, "Music Disc");
	}

	public String getItemName(int item) {
		return getItemName(item, (short) 0);
	}

	public String getItemName(int item, short data) {
		if (customNames.containsKey(item, data)) {
			return (String) customNames.get(item, data);
		}
		return (String) itemNames.get(item, data);
	}

	public void setItemName(int item, String name) {
		setItemName(item, (short) 0, name);
	}

	public void setItemName(int item, short data, String name) {
		customNames.put(item, data, name);
	}

	public void resetName(int item) {
		resetName(item, (byte) 0);
	}

	public void resetName(int item, short data) {
		if (customNames.containsKey(item, data)) {
			customNames.remove(item, data);
		}
	}

	public void reset() {
		customNames.clear();
		customTextures.clear();
		Iterator<Material> i = MaterialData.getMaterialIterator();
		while(i.hasNext()) {
			Material next = i.next();
			if (next instanceof Block) {
				Block block = (Block)next;
				resetFriction(block.getRawId(), (short) block.getRawData());
				resetHardness(block.getRawId(), (short) block.getRawData());
				resetOpacity(block.getRawId(), (short) block.getRawData());
				resetLightLevel(block.getRawId(), (short) block.getRawData());
			}
		}
	}

	public String getCustomItemName(int item) {
		return getCustomItemName(item, (short) 0);
	}

	public String getCustomItemName(int item, short data) {
		if (customNames.containsKey(item, data)) {
			return (String) customNames.get(item, data);
		}
		return null;
	}

	public void setItemTexture(int item, String texture) {
		setItemTexture(item, (short) 0, null, texture);
	}

	public void setItemTexture(int item, short data, String texture) {
		setItemTexture(item, data, null, texture);
	}

	public void setItemTexture(int item, short data, String pluginName, String texture) {
		customTextures.put(item, data, texture);
		if (pluginName == null) {
			customTexturesPlugin.remove(item, data);
		} else {
			customTexturesPlugin.put(item, data, pluginName);
		}
	}

	public String getCustomItemTexture(int item) {
		return getCustomItemTexture(item, (short) 0);
	}

	public String getCustomItemTexturePlugin(int item) {
		return getCustomItemTexturePlugin(item, (short) 0);
	}

	public String getCustomItemTexture(int item, short data) {
		if (customTextures.containsKey(item, data)) {
			return (String) customTextures.get(item, data);
		}
		return null;
	}

	public String getCustomItemTexturePlugin(int item, short data) {
		if (customTexturesPlugin.containsKey(item, data)) {
			return (String) customTexturesPlugin.get(item, data);
		}
		return null;
	}

	public void resetTexture(int item) {
		resetTexture(item, (short) 0);
	}

	public void resetTexture(int item, short data) {
		if (customTextures.containsKey(item, data)) {
			customTextures.remove(item, data);
		}
	}

	public float getFriction(int id, short data) {
		return net.minecraft.src.Block.blocksList[id].slipperiness;
	}

	public void setFriction(int id, short data, float friction) {
		if (!originalFriction.containsKey(id, data)) {
			originalFriction.put(id, data, getFriction(id, data));
		}
		net.minecraft.src.Block.blocksList[id].slipperiness = friction;
	}

	public void resetFriction(int id, short data) {
		if (originalFriction.containsKey(id, data)) {
			setFriction(id, data, originalFriction.get(id, data));
			originalFriction.remove(id, data);
		}
	}

	public float getHardness(int id, short data) {
		return net.minecraft.src.Block.blocksList[id].getHardness();
	}

	public void setHardness(int id, short data, float hardness) {
		if (!originalHardness.containsKey(id, data)) {
			originalHardness.put(id, data, getHardness(id, data));
		}
		net.minecraft.src.Block.blocksList[id].blockHardness = hardness;
	}

	public void resetHardness(int id, short data) {
		if (originalHardness.containsKey(id, data)) {
			setHardness(id, data, originalHardness.get(id, data));
			originalHardness.remove(id, data);
		}
	}

	public boolean isOpaque(int id, short data) {
		return net.minecraft.src.Block.opaqueCubeLookup[id];
	}

	public void setOpaque(int id, short data, boolean opacity) {
		if (!originalOpacity.containsKey(id)) {
			originalOpacity.put(id, (byte) (isOpaque(id, data) ? 1 : 0));
		}
		net.minecraft.src.Block.opaqueCubeLookup[id] = opacity;
	}

	public void resetOpacity(int id, short data) {
		if (originalOpacity.containsKey(id)) {
			setOpaque(id, data, originalOpacity.get(id) != 0);
			originalOpacity.remove(id);
		}
	}

	public int getLightLevel(int id, short data) {
		return net.minecraft.src.Block.lightValue[id];
	}

	public void setLightLevel(int id, short data, int level) {
		if (!originalLight.containsKey(id)) {
			originalLight.put(id, getLightLevel(id, data));
		}
		net.minecraft.src.Block.lightValue[id] = level;
	}

	public void resetLightLevel(int id, short data) {
		if (originalLight.containsKey(id)) {
			setLightLevel(id, data, originalLight.get(id));
			originalLight.remove(id);
		}
	}
}
