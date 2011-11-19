package org.getspout.spout.gui.controls;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.controls.Shortcut;
import org.getspout.spout.controls.SimpleKeyBindingManager;
import org.lwjgl.input.Keyboard;
import org.spoutcraft.spoutcraftapi.gui.AbstractListModel;
import org.spoutcraft.spoutcraftapi.gui.ListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;

public class ControlsModel extends AbstractListModel {
	private GuiControls gui;
	protected GameSettings options = SpoutClient.getHandle().gameSettings;
	private List<ControlsBasicItem> items = new LinkedList<ControlsModel.ControlsBasicItem>();
	private SimpleKeyBindingManager manager = (SimpleKeyBindingManager) SpoutClient.getInstance().getKeyBindingManager();
	private boolean editing = false;
	private ControlsBasicItem lastEdit = null;
	
	public static final int MOUSE_OFFSET = -100;
	
	public ControlsModel(GuiControls gui) {
		this.gui = gui;
	}
	
	public void refresh() {
		items.clear();
		
		if(gui.checkVanilla.isChecked()) {
			//Vanilla items
			int n = 0;
			for(KeyBinding binding:options.keyBindings) {
				items.add(new VanillaBindingItem(n, binding, this));
				n++;
			}
		}
		
		if(gui.checkShortcuts.isChecked()) {
			//Shortcuts
			for(Shortcut sh:manager.getAllShortcuts()) {
				items.add(new ShortcutBindingItem(sh, this));
			}
		}
		
		if(gui.checkBindings.isChecked()) {
			//Plugin controls
			for(org.spoutcraft.spoutcraftapi.keyboard.KeyBinding binding:manager.getAllBindings()) {
				items.add(new KeyBindingItem(binding, this));
			}
		}
		
		//Check for conflicting keys
		outer: for(ControlsBasicItem item1:items) {
			for(ControlsBasicItem item2:items) {
				if(!item1.equals(item2)) {
					item1.setConflicting(item1.conflicts(item2));
					if(item1.isConflicting()) {
						continue outer;
					}
				}
			}
		}
		
		if(!gui.search.getText().isEmpty()) {
			for(int i = 0; i < items.size(); i++) {
				ControlsBasicItem item = items.get(i);
				if(!item.getName().toLowerCase().contains(gui.search.getText().toLowerCase())) {
					items.remove(i);
					i--;
				}
			}
		}
		
		sizeChanged();
	}
	
	public void setCurrentGui(GuiControls gui) {
		this.gui = gui;
	}

	@Override
	public ControlsBasicItem getItem(int row) {
		if(row < 0 || row >= items.size()) {
			return null;
		}
		return items.get(row);
	}

	@Override
	public int getSize() {
		return items.size();
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
		gui.updateButtons();
	}
	
	protected void onItemClicked(ControlsBasicItem item, boolean doubleClicked) {
		if(doubleClicked) {
			editing = !editing;
			if(item != lastEdit) {
				editing = true;
			}
			lastEdit = item;
		}
		gui.updateButtons();
	}
	
	public ControlsBasicItem getEditingItem() {
		return editing?lastEdit:null;
	}

	public void finishEdit() {
		editing = false;
		refresh();
	}

	public abstract class ControlsBasicItem implements ListWidgetItem {
		
		private ListWidget widget;
		protected ControlsModel model;
		private boolean conflicts = false;
		
		public ControlsBasicItem(ControlsModel model) {
			this.model = model;
		}

		@Override
		public ListWidget getListWidget() {
			return widget;
		}

		@Override
		public void onClick(int x, int y, boolean doubleClick) {
			model.onItemClicked(this, doubleClick);
		}

		@Override
		public void setListWidget(ListWidget widget) {
			this.widget = widget;
		}
		
		public abstract void setKey(int id);
		public abstract int getKey();
		
		public boolean conflicts(ControlsBasicItem other) {
			//TODO better handling for modifiers
			return getKey() == other.getKey() && getModifiers() == other.getModifiers();
		}
		
		/**
		 * Tell if this item accepts modifier keys such as SHIFT, CONTROL or ALT.
		 * @return true, if the item accepts modifier keys.
		 */
		public abstract boolean useModifiers();
		
		/**
		 * Tell if this item accepts mouse buttons to summon the action
		 * @return true, if the item accepts mouse buttons
		 */
		public abstract boolean useMouseButtons();
		
		public void setConflicting(boolean c) {
			this.conflicts = c;
		}
		
		public void setModifiers(int m) {
			//Unused
		}
		
		public int getModifiers() {
			return 0;
		}
		
		public abstract String getName();

		public boolean isConflicting() {
			return conflicts;
		}
	}
	
	public class VanillaBindingItem extends ControlsBasicItem {

		private KeyBinding binding;
		private int n ;
		
		public VanillaBindingItem(int n, KeyBinding binding, ControlsModel model) {
			super(model);
			this.binding = binding;
			this.n = n;
		}
		
		public int getHeight() {
			return 11;
		}

		@Override
		public void render(int x, int y, int width, int height) {
			FontRenderer font = SpoutClient.getHandle().fontRenderer;
			font.drawStringWithShadow("V", x+2, y+2, 0xffffff00);
			int w = font.getStringWidth("V");
			font.drawStringWithShadow(getName(), x+w+4, y+2, !isConflicting()?0xffffffff:0xffff0000);
			font.drawStringWithShadow(getEditingItem() == this?"> <":options.getOptionDisplayString(n), x + width / 2, y+2, 0xffcccccc);
			
		}

		@Override
		public void setKey(int id) {
            options.setKeyBinding(n, id);
            KeyBinding.resetKeyBindingArrayAndHash();
		}

		@Override
		public int getKey() {
			return binding.keyCode;
		}
		
		public boolean useModifiers() {
			return false;
		}
		
		public boolean useMouseButtons() {
			return true;
		}
		
		public String getName() {
			return options.getKeyBindingDescription(n);
		}
	}
	
	public class ShortcutBindingItem extends ControlsBasicItem {

		private Shortcut shortcut;
		
		public ShortcutBindingItem(Shortcut sh, ControlsModel model) {
			super(model);
			shortcut = sh;
		}

		@Override
		public int getHeight() {
			return 11;
		}

		@Override
		public void render(int x, int y, int width, int height) {
			FontRenderer font = SpoutClient.getHandle().fontRenderer;
			font.drawStringWithShadow("S", x+2, y+2, 0xff00ff00);
			int w = font.getStringWidth("S");
			font.drawStringWithShadow(getName(), x+w+4, y+2, !isConflicting()?0xffffffff:0xffff0000);
			font.drawStringWithShadow(getEditingItem() == this?"> <":shortcut.toString(), x + width / 2, y+2, 0xffcccccc);
		}

		@Override
		public void setKey(int id) {
			shortcut.setKey(id);
			manager.save();
			manager.updateBindings();
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
	
	public class KeyBindingItem extends ControlsBasicItem {
		private org.spoutcraft.spoutcraftapi.keyboard.KeyBinding binding;
		public KeyBindingItem(org.spoutcraft.spoutcraftapi.keyboard.KeyBinding binding, ControlsModel model) {
			super(model);
			this.binding = binding;
		}

		@Override
		public int getHeight() {
			return 11;
		}

		@Override
		public void render(int x, int y, int width, int height) {
			FontRenderer font = SpoutClient.getHandle().fontRenderer;
			font.drawStringWithShadow("B", x+2, y+2, 0xff0000ff);
			int w = font.getStringWidth("B");
			font.drawStringWithShadow(getName(), x+w+4, y+2, !isConflicting()?0xffffffff:0xffff0000);
			font.drawStringWithShadow(getEditingItem() == this?"> <":Keyboard.getKeyName(getKey()), x + width / 2, y+2, 0xffcccccc);
		}

		@Override
		public void setKey(int id) {
			binding.setKey(id);
			manager.save();
			manager.updateBindings();
		}

		@Override
		public int getKey() {
			return binding.getKey();
		}

		@Override
		public boolean useModifiers() {
			return false;
		}

		@Override
		public boolean useMouseButtons() {
			return false;
		}

		@Override
		public String getName() {
			return binding.getDescription();
		}
		
	}

}
