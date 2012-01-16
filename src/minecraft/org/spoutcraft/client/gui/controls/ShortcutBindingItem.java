package org.spoutcraft.client.gui.controls;

import net.minecraft.src.FontRenderer;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.controls.Shortcut;
import org.spoutcraft.client.controls.SimpleKeyBindingManager;

public class ShortcutBindingItem extends ControlsBasicItem {

		private Shortcut shortcut;
		private ControlsModel parent;
		private SimpleKeyBindingManager manager = (SimpleKeyBindingManager) SpoutClient.getInstance().getKeyBindingManager();
		public ShortcutBindingItem(Shortcut sh, ControlsModel model) {
			super(model);
			this.parent = model;
			shortcut = sh;
		}

		public int getHeight() {
			return 11;
		}

		public void render(int x, int y, int width, int height) {
			FontRenderer font = SpoutClient.getHandle().fontRenderer;
			font.drawStringWithShadow("S", x+2, y+2, 0xff00ff00);
			int w = font.getStringWidth("S");
			font.drawStringWithShadow(getName(), x+w+4, y+2, !isConflicting()?0xffffffff:0xffff0000);
			font.drawStringWithShadow(parent.getEditingItem() == this?"> <":shortcut.toString(), x + width / 2, y+2, 0xffcccccc);
		}

		@Override
		public void setKey(int id) {
			shortcut.setKey(id);
			manager.updateBindings();
			manager.save();
		}

		@Override
		public int getKey() {
			return shortcut.getKey();
		}

		@Override
		public boolean useModifiers() {
			return true;
		}

		@Override
		public boolean useMouseButtons() {
			return false;
		}

		@Override
		public String getName() {
			return shortcut.getTitle();
		}
		
		@Override
		public void setModifiers(int m) {
			shortcut.setRawModifiers((byte) m);
		}
		
		@Override
		public int getModifiers() {
			return shortcut.getModifiers();
		}

		public Shortcut getShortcut() {
			return shortcut;
		}
		
	}