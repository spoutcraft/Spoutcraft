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
		title = new GenericLabel("Enter Command / Chat message\nFor commands, use a '/'. If you omit the slash, it'll be sent as a chat message.");
		title.setHeight(20).setWidth(200);
		title.setX(10).setY(10);
		getScreen().attachWidget(Spoutcraft.getAddonManager().getAddon("Spoutcraft"), title);
		
		String text = "";
		if(item >= 0) {
			text = parent.getShortcut().getCommands().get(item);
		}
		if(cmd != null) {
			text = cmd.getText();
		}
		
		cmd = new GenericTextField();
		cmd.setX(10).setY(40);
		cmd.setWidth(width - 20);
		cmd.setHeight(height - 80);
		cmd.setFocus(true);
		cmd.setMaximumCharacters(0);
		cmd.setMaximumLines(0);
		cmd.setText(text);
		
		getScreen().attachWidget(Spoutcraft.getAddonManager().getAddon("Spoutcraft"), cmd);
		
		doneButton = new GenericButton("Done");
		doneButton.setHeight(20).setWidth(200).setX(width/2 - 100).setY(height - 30);
		getScreen().attachWidget(Spoutcraft.getAddonManager().getAddon("Spoutcraft"), doneButton);
	}

	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
	}

	protected void buttonClicked(Button btn) {
		if(btn.equals(doneButton)) {
			String command = cmd.getText(); 
			if(!command.trim().isEmpty()) {
				if(item == -1) {
					parent.getShortcut().addCommand(command);
				} else {
					parent.getShortcut().getCommands().remove(item);
					parent.getShortcut().getCommands().add(item, command);
				}
			}
			mc.displayGuiScreen(parent);
		}
	}
}
