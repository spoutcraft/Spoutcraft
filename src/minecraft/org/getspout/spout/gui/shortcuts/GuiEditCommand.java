package org.getspout.spout.gui.shortcuts;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;
import org.spoutcraft.spoutcraftapi.gui.Label;
import org.spoutcraft.spoutcraftapi.gui.TextField;
import net.minecraft.src.GuiScreen;

public class GuiEditCommand extends GuiScreen{
	private GuiEditShortcut parent;
	private int item;
	Button doneButton;
	Label title;
	TextField cmd;

	public GuiEditCommand(GuiEditShortcut parent, int i) {
		this.parent = parent;
		this.item = i;
	}
	
	public void initGui() {
		title = new GenericLabel("Enter Command");
		title.setHeight(20).setWidth(200);
		title.setX(width/2-100).setY(100);
		getScreen().attachWidget(Spoutcraft.getAddonManager().getAddon("Spoutcraft"), title);
		
		cmd = new GenericTextField();
		cmd.setHeight(20).setWidth(200);
		cmd.setMaximumCharacters(Integer.MAX_VALUE);
		cmd.setX(width/2-100).setY(130);
		cmd.setFocus(true);
		if(item >= 0) {
			cmd.setText(parent.getShortcut().getCommands().get(item));
		}
		getScreen().attachWidget(Spoutcraft.getAddonManager().getAddon("Spoutcraft"), cmd);
		
		doneButton = new GenericButton("Done");
		doneButton.setHeight(20).setWidth(200).setX(width/2-100).setY(160);
		getScreen().attachWidget(Spoutcraft.getAddonManager().getAddon("Spoutcraft"), doneButton);
	}

	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
	}

	protected void buttonClicked(Button btn) {
		if(btn.equals(doneButton)) {
			String command = cmd.getText(); 
			if(item == -1) {
				parent.getShortcut().addCommand(command);
			} else {
				parent.getShortcut().getCommands().remove(item);
				parent.getShortcut().getCommands().add(item, command);
			}
			mc.displayGuiScreen(parent);
		}
	}
}
