/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.spoutcraft.client.gui.inventory;

import java.util.List;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.GuiContainerCreative;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.material.CustomItem;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;

/**
 *
 * @author ZNickq
 */
public class CreativeTabCustom extends CreativeTabs{
	
	public CreativeTabCustom(int inty, String string) {
		super(inty, string);
	}

	@Override
	public Item getTabIconItem() {
		return Item.flint;
	}

	@Override
	public String getTranslatedTabLabel() {
		return "Custom Items";
	}

	@Override
	public void displayAllReleventItems(List par1List) {
		CustomItem[] iteml = MaterialData.getCustomItems();
		
		for(CustomItem ci : iteml) {
			ItemStack is = new ItemStack(ci.getRawId(), 1, ci.getRawData());
			par1List.add(is);
		}
	}
	
}
