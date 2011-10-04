package org.getspout.spout.gui.shortcuts;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.controls.Shortcut;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;

public class GuiCommandsSlot extends GuiSlot {

	GuiEditShortcut parent;
	Shortcut shortcut;
	int selection = -1;
	
	public GuiCommandsSlot(GuiEditShortcut parent) {
		super(SpoutClient.getHandle(), parent.width, parent.height, 70, parent.height-40, 14);
		this.parent = parent;
		this.shortcut = parent.getShortcut();
	}

	protected int getSize() {
		return shortcut.getCommands().size();
	}

	protected void elementClicked(int i, boolean doubleClicked) {
		selection = i;
		if(doubleClicked) {
			parent.editCommand(i);
		}
		parent.updateButtons();
	}

	protected boolean isSelected(int i) {
		return i == selection;
	}

	protected void drawBackground() {
		parent.drawDefaultBackground();
	}

	protected void drawSlot(int i, int x, int y, int z, Tessellator t) {
		String cmd = shortcut.getCommands().get(i);
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		parent.drawString(font, cmd, x+2, y+2, 0xffffff);
	}

	public int getSelected() {
		return selection;
	}

	public void updateSelected() {
		if(getSize()==0) {
			selection = -1;
		}
	}
}
