package org.spoutcraft.client.gui.controls;

import org.spoutcraft.client.SpoutClient;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.KeyBinding;

public class VanillaBindingItem extends ControlsBasicItem {

	private KeyBinding binding;
	private int n ;
	private ControlsModel parent;
	public VanillaBindingItem(int n, KeyBinding binding, ControlsModel model) {
		super(model);
		this.binding = binding;
		this.n = n;
		this.parent = model;
	}
	
	public int getHeight() {
		return 11;
	}

	public void render(int x, int y, int width, int height) {
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		font.drawStringWithShadow("V", x+2, y+2, 0xffffff00);
		int w = font.getStringWidth("V");
		font.drawStringWithShadow(getName(), x+w+4, y+2, !isConflicting()?0xffffffff:0xffff0000);
		font.drawStringWithShadow(parent.getEditingItem() == this?"> <":SpoutClient.getHandle().gameSettings.getOptionDisplayString(n), x + width / 2, y+2, 0xffcccccc);
		
	}

	@Override
	public void setKey(int id) {
		SpoutClient.getHandle().gameSettings.setKeyBinding(n, id);
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
		return SpoutClient.getHandle().gameSettings.getKeyBindingDescription(n);
	}
}