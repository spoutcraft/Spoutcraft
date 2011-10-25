package org.getspout.spout.gui.shortcuts;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.controls.Shortcut;
import org.getspout.spout.controls.SimpleKeyBindingManager;
import org.lwjgl.input.Keyboard;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;

public class GuiShortcutsSlot extends GuiSlot {
	
	int selectedItem = -1;
	GuiCommandShortcuts parent;
	SimpleKeyBindingManager manager = (SimpleKeyBindingManager) SpoutClient.getInstance().getKeyBindingManager();
	public GuiShortcutsSlot(GuiCommandShortcuts parent) {
		super(SpoutClient.getHandle(), parent.width, parent.height, 40, parent.height-40, 24);
		this.parent = parent;
	}
	
	protected int getSize() {
		return manager.getAllShortcuts().size();
	}

	protected void elementClicked(int i, boolean doubleClicked) {
		selectedItem = i;
		if(doubleClicked) {
			parent.editItem(getSelection());
		}
		parent.updateButtons();
	}

	protected boolean isSelected(int i) {
		return selectedItem == i;
	}

	protected void drawBackground() {
		parent.drawDefaultBackground();
	}

	protected void drawSlot(int i, int x, int y, int z, Tessellator tesselator) {
		Shortcut item = getItem(i);
		String title = item.getTitle();
		String key = "Shortcut: "+item.toString();
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		parent.drawString(font, title, x+2, y+1, 16777215);
		parent.drawString(font, key, x+2, y+12, 8421504);
	}
	
	public Shortcut getSelection() {
		return getItem(selectedItem);
	}

	public Shortcut getItem(int i) {
		if(i<0 || i > getSize()){
			return null;
		}
		return manager.getAllShortcuts().get(i);
	}
	
	public void updateSelection() {
		if(getSize() == 0) {
			selectedItem = -1;
		}
	}
}
