package org.getspout.spout.gui.controls;

import java.util.List;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.controls.SimpleKeyBindingManager;
import org.spoutcraft.spoutcraftapi.gui.GenericListWidget;
import org.spoutcraft.spoutcraftapi.gui.Keyboard;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBinding;

public class PluginControlsList extends GenericListWidget {
	GuiPluginControls screen = null;
	List<KeyBinding> bindings;
	SimpleKeyBindingManager manager;
	boolean recording = false;
	boolean filter = false;
	
	public PluginControlsList(GuiPluginControls s) {
		screen = s;
		bindings = ((SimpleKeyBindingManager)SpoutClient.getInstance().getKeyBindingManager()).getAllBindings();
		manager = (SimpleKeyBindingManager) SpoutClient.getInstance().getKeyBindingManager();
		updateBindings();
	}
	
	public void updateBindings() {
		clear();
		for(KeyBinding binding:bindings) {
			if(filter) {
				if(binding.getUniqueId() == null) {
					continue;
				}
			}
			addItem(new KeyBindingLWI(binding));
		}
	}

	@Override
	public void onSelected(int item, boolean doubleClick) {
		if (doubleClick) {
			recording = !recording;
			getCastedItem(item).setRecording(recording);
		}
	}

	@Override
	public boolean onKeyPressed(Keyboard key) {
		if(recording) {
			KeyBindingLWI item = getCastedSelection();
			KeyBinding binding = item.getKeyBinding();
			binding.setKey(key.getKeyCode());
			recording = false;
			item.setRecording(false);
			manager.updateBindings();
			manager.save();
			return true;
		}
		return false;
	}
	
	private KeyBindingLWI getCastedItem(int n) {
		return (KeyBindingLWI)super.getItem(n);
	}
	
	private KeyBindingLWI getCastedSelection() {
		return (KeyBindingLWI)getSelectedItem();
	}

	public void setFilered(boolean checked) {
		filter = checked;
	}

}
