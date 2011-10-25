package org.getspout.spout.gui.shortcuts;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.controls.Shortcut;
import org.getspout.spout.controls.SimpleKeyBindingManager;
import org.lwjgl.input.Keyboard;
import org.spoutcraft.spoutcraftapi.gui.GenericListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;

public class GuiShortcutsSlot extends GenericListWidget {
	GuiCommandShortcuts parent;
	SimpleKeyBindingManager manager = (SimpleKeyBindingManager) SpoutClient.getInstance().getKeyBindingManager();
	
	public GuiShortcutsSlot(GuiCommandShortcuts parent) {
		setWidth(parent.width);
		setHeight(parent.height - 80);
		setX(0);
		setY(40);
		this.parent = parent;
		
		updateItems();
	}
	
	public void updateItems() {
		clear();
		for(Shortcut sh:manager.getAllShortcuts()){
			addItem(new ShortcutLWI(sh));
		}
	}

	protected int getSize() {
		return manager.getAllShortcuts().size();
	}
	
	public Shortcut getShortcutItem(int i) {
		if(i<0 || i > getSize()){
			return null;
		}
		return manager.getAllShortcuts().get(i);
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
		if(doubleClick) {
			parent.editItem(getSelectedShortcut());
		}
		parent.updateButtons();
	}

	public Shortcut getSelectedShortcut() {
		if(getSelectedItem() == null) {
			return null;
		}
		return ((ShortcutLWI)getSelectedItem()).getShortcut();
	}
	
	private class ShortcutLWI implements ListWidgetItem {

		private Shortcut shortcut;
		private ListWidget widget;
		
		public ShortcutLWI(Shortcut sh) {
			shortcut = sh;
		}
		
		public void setListWidget(ListWidget widget) {
			this.widget = widget;
		}

		public Shortcut getShortcut() {
			return shortcut;
		}

		public ListWidget getListWidget() {
			return widget;
		}

		public int getHeight() {
			return 23 + (getNumCommands()>0?9:0) + (getNumChatMessages()>0?9:0);
		}
		
		public int getNumChatMessages() {
			int i = 0;
			for(String cmd:shortcut.getCommands()) {
				if(!cmd.startsWith("/")) i++;
			}
			return i;
		}

		public int getNumCommands() {
			int i = 0;
			for(String cmd:shortcut.getCommands()) {
				if(cmd.startsWith("/")) i++;
			}
			return i;
		}
		
		public void render(int x, int y, int width, int height) {
			String title = shortcut.getTitle();
			String key = "Shortcut: "+shortcut.toString();
			FontRenderer font = SpoutClient.getHandle().fontRenderer;
			y += 2;
			parent.drawString(font, title, x+2, y, 0xffffff);
			y += 9;
			parent.drawString(font, key, x+2, y, 0xaaaaaa);
			y += 9;
			int cmd = getNumCommands();
			int chat = getNumChatMessages();
			if(cmd > 0) {
				parent.drawString(font, cmd + " Command" + (cmd != 1?"s":""), x+2, y, 0xaaaaaa);
				y += 9;
			}
			if(chat > 0) {
				parent.drawString(font, chat + " Chat message" + (chat != 1?"s":""), x+2, y, 0xaaaaaa);
			}
		}
	}
}
