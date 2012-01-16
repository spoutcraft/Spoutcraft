package org.spoutcraft.client.gui.controls;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.controls.Shortcut;
import org.spoutcraft.client.controls.SimpleKeyBindingManager;
import org.spoutcraft.spoutcraftapi.gui.AbstractListModel;

public class ControlsModel extends AbstractListModel {
	private GuiControls gui;
	protected GameSettings options = SpoutClient.getHandle().gameSettings;
	private List<ControlsBasicItem> items = new LinkedList<ControlsBasicItem>();
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
		
		if(gui.checkSpoutcraft.isChecked()) {
			//Spoutcraft items
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
	
	public void setEditing(ControlsBasicItem item) {
		editing = true;
		lastEdit = item;
	}
	
	public ControlsBasicItem getEditingItem() {
		return editing?lastEdit:null;
	}

	public void finishEdit() {
		editing = false;
		refresh();
	}


	

	
	
}
