package org.getspout.spout.gui.controls;

import java.util.List;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.controls.SimpleKeyBindingManager;
import org.lwjgl.input.Keyboard;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBinding;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;

public class GuiControlSlot extends GuiSlot {
	public List<KeyBinding> bindings;
	public SimpleKeyBindingManager manager;
	public int selectedBinding = -1;
	public boolean isSelectedNew = false;
	public GuiPluginControls parentGui;

	public GuiControlSlot(GuiPluginControls parent) {
		super(SpoutClient.getHandle(), parent.width, parent.height, 32, parent.height - 50, 24);
		parentGui = parent;
	}
	
	protected int getSize() {
		return bindings.size();
	}

	protected void elementClicked(int item, boolean doubleClicked) {
		selectedBinding = item;
		isSelectedNew = doubleClicked;
	}

	protected boolean isSelected(int i) {
		return i == selectedBinding;
	}

	protected void drawBackground() {
		//this.parentGui.drawDefaultBackground();
	}

	protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
		KeyBinding item = bindings.get(var1);
		String name = item.getDescription();
		String key = "Key: "+Keyboard.getKeyName(item.getKey());
		if(isSelectedNew){
			key = "> ??? <";
		}
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		parentGui.drawString(font, name, var2+2, var3+1, 16777215);
		parentGui.drawString(font, key, var2+2, var3+12, 8421504);
	}

	public boolean keyTyped(char c, int i) {
		if(isSelectedNew) {
			KeyBinding binding = bindings.get(selectedBinding);
			binding.setKey(i);
			manager.updateBindings();
			isSelectedNew = false;
			return true;
		}
		return false;
	}
}
