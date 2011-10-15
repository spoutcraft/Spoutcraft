package org.getspout.spout.gui.controls;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.controls.SimpleKeyBindingManager;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.Label;
import org.spoutcraft.spoutcraftapi.gui.WidgetAnchor;

import net.minecraft.src.GuiControls;
import net.minecraft.src.GuiScreen;

public class GuiPluginControls extends GuiScreen {
	GuiScreen parentScreen;
	Button closeButton;
	Label description;
	GuiControlSlot slotContainer;
	
	public GuiPluginControls(GuiControls parent) {
		super();
		parentScreen = parent;
	}
	
	public void initGui(){
		closeButton = new GenericButton("Done");
		closeButton.setAlign(WidgetAnchor.CENTER_CENTER);
		closeButton.setWidth(50).setHeight(20).setX(10).setY((int)getScreen().getHeight()-30);
		getScreen().attachWidget("Spoutcraft", closeButton);
		description = new GenericLabel("Doubleclick a keybinding and press a key to assign!");
		description.setHeight(20).setWidth(200).setX(70).setY((int)getScreen().getHeight()-30);
		getScreen().attachWidget("Spoutcraft", description);
		slotContainer = new GuiControlSlot(this);
		slotContainer.bindings = ((SimpleKeyBindingManager)SpoutClient.getInstance().getKeyBindingManager()).getAllBindings();
		slotContainer.manager = (SimpleKeyBindingManager) SpoutClient.getInstance().getKeyBindingManager();
	}
	
	@Override
	public void drawScreen(int x, int y, float z) {
		slotContainer.drawScreen(x, y, z);
		super.drawScreen(x, y, z);
	}

	@Override
	protected void buttonClicked(Button btn) {
		if(btn.equals(closeButton)){
			mc.displayGuiScreen(parentScreen);
		}
	}
	
	@Override
	protected void keyTyped(char c, int i) {
		if(!slotContainer.keyTyped(c,i)){
			super.keyTyped(c, i);
		}
	}
}
