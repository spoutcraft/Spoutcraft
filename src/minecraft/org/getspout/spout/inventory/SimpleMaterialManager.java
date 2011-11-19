package org.getspout.spout.inventory;

import java.util.Iterator;

import gnu.trove.map.hash.TIntByteHashMap;
import gnu.trove.map.hash.TIntIntHashMap;

import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.design.BlockDesign;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;
import org.spoutcraft.spoutcraftapi.inventory.MaterialManager;
import org.spoutcraft.spoutcraftapi.inventory.Recipe;
import org.spoutcraft.spoutcraftapi.inventory.ShapedRecipe;
import org.spoutcraft.spoutcraftapi.inventory.ShapelessRecipe;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.CustomItem;
import org.spoutcraft.spoutcraftapi.material.Material;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.util.map.TIntPairFloatHashMap;
import org.spoutcraft.spoutcraftapi.util.map.TIntPairObjectHashMap;

public class SimpleMaterialManager implements MaterialManager {
	private final TIntPairFloatHashMap originalHardness = new TIntPairFloatHashMap();
	private final TIntPairFloatHashMap originalFriction = new TIntPairFloatHashMap();
	private final TIntByteHashMap originalOpacity = new TIntByteHashMap();
	private final TIntIntHashMap originalLight = new TIntIntHashMap();

	private final TIntPairObjectHashMap<String> customNames = new TIntPairObjectHashMap<String>(100);
	private final TIntPairObjectHashMap<String> customTextures = new TIntPairObjectHashMap<String>(100);
	private final TIntPairObjectHashMap<String> customTexturesPlugin = new TIntPairObjectHashMap<String>(100);
	
	public float getFriction(org.spoutcraft.spoutcraftapi.material.Block block) {
		int id = block.getRawId();
		if(block instanceof CustomBlock) {
			id = ((CustomBlock) block).getBlockId();
		}
		
		return net.minecraft.src.Block.blocksList[id].slipperiness;
	}
	public void setFriction(org.spoutcraft.spoutcraftapi.material.Block block, float friction) {
		int id = block.getRawId();
		int data = block.getRawData();
		if(block instanceof CustomBlock) {
			id = ((CustomBlock) block).getBlockId();
		}
		if (!originalFriction.containsKey(id, data)) {
			originalFriction.put(id, data, getFriction(block));
		}
		net.minecraft.src.Block.blocksList[id].slipperiness = friction;
	}
	public void resetFriction(org.spoutcraft.spoutcraftapi.material.Block block) {
		int id = block.getRawId();
		int data = block.getRawData();
		if(block instanceof CustomBlock) {
			id = ((CustomBlock) block).getBlockId();
		}
		if (originalFriction.containsKey(id, data)) {
			setFriction(block, originalFriction.get(id, data));
			originalFriction.remove(id, data);
		}
	}
	public float getHardness(org.spoutcraft.spoutcraftapi.material.Block block) {
		int id = block.getRawId();
		if(block instanceof CustomBlock) {
			id = ((CustomBlock) block).getBlockId();
		}
		return net.minecraft.src.Block.blocksList[id].getHardness();
	}
	public void setHardness(org.spoutcraft.spoutcraftapi.material.Block block, float hardness) {
		int id = block.getRawId();
		if(block instanceof CustomBlock) {
			id = ((CustomBlock) block).getBlockId();
		}
		net.minecraft.src.Block.blocksList[id].blockHardness = hardness;
	}
	public void resetHardness(org.spoutcraft.spoutcraftapi.material.Block block) {
		int id = block.getRawId();
		int data = block.getRawData();
		if(block instanceof CustomBlock) {
			id = ((CustomBlock) block).getBlockId();
		}
		if (originalHardness.containsKey(id, data)) {
			setHardness(block, originalHardness.get(id, data));
			originalHardness.remove(id, data);
		}
	}
	public boolean isOpaque(org.spoutcraft.spoutcraftapi.material.Block block) {
		int id = block.getRawId();
		if(block instanceof CustomBlock) {
			id = ((CustomBlock) block).getBlockId();
		}
		return net.minecraft.src.Block.opaqueCubeLookup[id];
	}
	public void setOpaque(org.spoutcraft.spoutcraftapi.material.Block block, boolean opacity) {
		int id = block.getRawId();
		if(block instanceof CustomBlock) {
			id = ((CustomBlock) block).getBlockId();
		}
		if (!originalOpacity.containsKey(id)) {
			originalOpacity.put(id, (byte) (isOpaque(block) ? 1 : 0));
		}
		net.minecraft.src.Block.opaqueCubeLookup[id] = opacity;
	}
	public void resetOpacity(org.spoutcraft.spoutcraftapi.material.Block block) {
		int id = block.getRawId();
		if(block instanceof CustomBlock) {
			id = ((CustomBlock) block).getBlockId();
		}
		if (originalOpacity.containsKey(id)) {
			setOpaque(block, originalOpacity.get(id) != 0);
			originalOpacity.remove(id);
		}
		
	}
	public int getLightLevel(org.spoutcraft.spoutcraftapi.material.Block block) {
		int id = block.getRawId();
		if(block instanceof CustomBlock) {
			id = ((CustomBlock) block).getBlockId();
		}
		return net.minecraft.src.Block.lightValue[id];
	}
	public void setLightLevel(org.spoutcraft.spoutcraftapi.material.Block block, int level) {
		int id = block.getRawId();
		if(block instanceof CustomBlock) {
			id = ((CustomBlock) block).getBlockId();
		}
		if (!originalLight.containsKey(id)) {
			originalLight.put(id, getLightLevel(block));
		}
		net.minecraft.src.Block.lightValue[id] = level;
	}
	public void resetLightLevel(org.spoutcraft.spoutcraftapi.material.Block block) {
		int id = block.getRawId();
		if(block instanceof CustomBlock) {
			id = ((CustomBlock) block).getBlockId();
		}
		if (originalLight.containsKey(id)) {
			setLightLevel(block, originalLight.get(id));
			originalLight.remove(id);
		}
		
	}
	public void setItemName(Material item, String name) {
		customNames.put(item.getRawId(), item.getRawData(), name);
	}
	public void resetName(Material item) {
		int id = item.getRawId();
		int data = item.getRawData();
		if (customNames.containsKey(id, data)) {
			customNames.remove(id, data);
		}
		
	}
	public void setItemTexture(Material item, Addon addon, String texture) {
		int id = item.getRawId();
		int data = item.getRawData();
		String addonName;
		if (addon == null) {
			addonName = null;
		} else {
			addonName = addon.getDescription().getName();
		}
		customTextures.put(id, data, texture);
		if (addonName == null) {
			customTexturesPlugin.remove(id, data);
		} else {
			customTexturesPlugin.put(id, data, addonName);
		}
		
	}
	public String getCustomItemTexture(Material item) {
		if (item == null) return null;
		int id = item.getRawId();
		int data = item.getRawData();
		if (customTextures.containsKey(id, data)) {
			return (String) customTextures.get(id, data);
		}
		return null;
	}
	public String getCustomItemTextureAddon(Material item) {
		if (item == null) return null;
		int id = item.getRawId();
		int data = item.getRawData();
		if (customTexturesPlugin.containsKey(id, data)) {
			return (String) customTexturesPlugin.get(id, data);
		}
		return null;
	}
	public void resetTexture(Material item) {
		int id = item.getRawId();
		int data = item.getRawData();
		if (customTextures.containsKey(id, data)) {
			customTextures.remove(id, data);
		}
	}
	public void reset() {
		customNames.clear();
		customTextures.clear();
		Iterator<Material> i = MaterialData.getMaterialIterator();
		while(i.hasNext()) {
			Material next = i.next();
			if (next instanceof org.spoutcraft.spoutcraftapi.material.Block) {
				org.spoutcraft.spoutcraftapi.material.Block block = (org.spoutcraft.spoutcraftapi.material.Block)next;
				resetFriction(block);
				resetHardness(block);
				resetOpacity(block);
				resetLightLevel(block);
			}
		}
	}
	public int registerCustomItemName(Addon addon, String key) {
		// TODO Auto-generated method stub
		return 0;
	}
	public void setCustomItemBlock(CustomItem item, CustomBlock block) {
		// TODO Auto-generated method stub
		
	}
	public ItemStack getCustomItemStack(CustomBlock block, int size) {
		// TODO Auto-generated method stub
		return null;
	}
	public ItemStack getCustomItemStack(CustomItem item, int size) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean removeBlockOverride(Block block) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean overrideBlock(Block block, CustomBlock customBlock) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean overrideBlock(World world, int x, int y, int z, CustomBlock customBlock) {
		// TODO Auto-generated method stub
		return false;
	}
	public void setCustomBlockDesign(Material material, BlockDesign design) {
		// TODO Auto-generated method stub
		
	}
	public boolean isCustomBlock(Block block) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean registerRecipe(Recipe recipe) {
		SpoutcraftRecipe toAdd;
		if (recipe instanceof Recipe) {
			toAdd = (SpoutcraftRecipe) recipe;
		} else {
			if (recipe instanceof ShapedRecipe) {
				toAdd = SimpleShapedRecipe.fromSpoutRecipe((ShapedRecipe) recipe);
			} else if (recipe instanceof ShapelessRecipe) {
				toAdd = SimpleShapelessRecipe.fromSpoutRecipe((ShapelessRecipe) recipe);
			} else {
				return false;
			}
		}
		toAdd.addToCraftingManager();
		return true;
	}
	public boolean isCustomItem(ItemStack item) {
		// TODO Auto-generated method stub
		return false;
	}
	public CustomItem getCustomItem(ItemStack item) {
		// TODO Auto-generated method stub
		return null;
	}
	public CustomBlock registerItemDrop(CustomBlock block, ItemStack item) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean hasItemDrop(CustomBlock block) {
		// TODO Auto-generated method stub
		return false;
	}
}
