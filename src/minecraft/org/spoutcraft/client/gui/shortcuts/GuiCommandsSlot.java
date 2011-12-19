package org.spoutcraft.client.gui.shortcuts;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.controls.Shortcut;
import org.spoutcraft.spoutcraftapi.gui.GenericListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;

import net.minecraft.src.FontRenderer;

public class GuiCommandsSlot extends GenericListWidget {



	GuiEditShortcut parent;
	Shortcut shortcut;
	
	public GuiCommandsSlot(GuiEditShortcut parent) {
		setWidth(parent.width);
		setHeight(parent.height - 70 - 25);
		setX(0).setY(60);
		this.parent = parent;
		this.shortcut = parent.getShortcut();
		updateItems();
	}

	public void updateItems() {
		clear();
		for(String cmd:shortcut.getCommands()) {
			addItem(new CommandLWI(cmd));
		}
	}
	
	@Override
	public void onSelected(int item, boolean doubleClick) {
		if(doubleClick) {
			parent.editCommand(item);
		}
		parent.updateButtons();
	}
	
	private class CommandLWI implements ListWidgetItem {

		ListWidget widget;
		String cmd;
		public CommandLWI(String cmd) {
			this.cmd = cmd;
		}
		
		public void setListWidget(ListWidget widget) {
			this.widget = widget;
		}

		public ListWidget getListWidget() {
			return widget;
		}

		public int getHeight() {
			return 23;
		}

		public void render(int x, int y, int width, int height) {
			FontRenderer font = SpoutClient.getHandle().fontRenderer;
			parent.drawString(font, cmd, x+2, y+2, 0xffffff);
			if(cmd.startsWith("/")) {
				parent.drawString(font, "Command", x+2, y+13, 0xaaaaaa);
			} else {
				parent.drawString(font, "Chat Message", x+2, y+13, 0xaaaaaa);
			}
		}

		public void onClick(int x, int y, boolean d) {
		}
	}
}
