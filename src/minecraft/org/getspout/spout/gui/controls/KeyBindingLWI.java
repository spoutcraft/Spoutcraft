package org.getspout.spout.gui.controls;

import org.lwjgl.input.Keyboard;
import org.spoutcraft.spoutcraftapi.gui.GenericListWidgetItem;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBinding;

public class KeyBindingLWI extends GenericListWidgetItem {

	KeyBinding myBinding = null;
	boolean rec = false;
	
	private KeyBindingLWI(String title, String text, String iconUrl) {
		super(title, text, iconUrl);
		// TODO Auto-generated constructor stub
	}
	
	public KeyBindingLWI(KeyBinding binding){
		super(binding.getDescription(), "Key: "+Keyboard.getKeyName(binding.getKey()), "");
		myBinding = binding;
	}
	
	public void updateBinding() {
		setTitle(myBinding.getDescription());
		if (!rec) {
			setText( "Key: "+Keyboard.getKeyName(myBinding.getKey()));
		} else {
			setText("> Press a key! <");
		}
	}

	public void setRecording(boolean rec) {
		this.rec = rec;
		updateBinding();
	}
	
	public KeyBinding getKeyBinding() {
		return myBinding;
	}
}
