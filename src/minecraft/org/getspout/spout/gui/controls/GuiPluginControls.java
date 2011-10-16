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
	Label title;
	PluginControlsList list;
	
	public GuiPluginControls(GuiControls parent) {
		super();
		parentScreen = parent;
	}
	
	public void initGui(){
		title = new GenericLabel("Plugin Controls");
		title.setX(10).setY(10).setHeight(20).setWidth(width - 20);
		title.setScale(2);
		getScreen().attachWidget(title);
		
		closeButton = new GenericButton("Done");
		closeButton.setAlign(WidgetAnchor.CENTER_CENTER);
		closeButton.setWidth(50).setHeight(20).setX(10).setY((int)getScreen().getHeight()-30);
		getScreen().attachWidget(closeButton);
		description = new GenericLabel("Doubleclick a keybinding and press a key to assign!");
		description.setHeight(20).setWidth(200).setX(70).setY((int)getScreen().getHeight()-30);
		getScreen().attachWidget(description);
		
		list = new PluginControlsList(this);
		list.setX(0).setY(32).setWidth(width).setHeight(height - 32 - 40);
		getScreen().attachWidget(list);
	}
	
	@Override
	public void drawScreen(int x, int y, float z) {
		drawDefaultBackground();
	}

	@Override
	protected void buttonClicked(Button btn) {
		if(btn.equals(closeButton)){
			mc.displayGuiScreen(parentScreen);
		}
	}
}
