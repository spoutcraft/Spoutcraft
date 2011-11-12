package org.getspout.spout.gui.shortcuts;

import org.getspout.spout.controls.Shortcut;
import org.getspout.spout.controls.SimpleKeyBindingManager;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;
import org.spoutcraft.spoutcraftapi.gui.Label;
import org.spoutcraft.spoutcraftapi.gui.TextField;
import org.spoutcraft.spoutcraftapi.gui.WidgetAnchor;

import net.minecraft.src.GuiScreen;

public class GuiEditShortcut extends GuiScreen {
	GuiCommandShortcuts parent;
	Shortcut item;
	Button recordButton, doneButton, addButton, editButton, removeButton;
	Label titleLabel, recordLabel;
	TextField titleText;
	GuiCommandsSlot slot;
	
	boolean recording = false;
	public GuiEditShortcut(GuiCommandShortcuts parent, Shortcut item) {
		this.parent = parent;
		this.item = item;
	}
	
	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
		super.drawScreen(var1, var2, var3);
	}
	
	protected void keyTyped(char c, int i) {
		if(recording && !SimpleKeyBindingManager.isModifierKey(i)) {
			item.setKey(i);
			item.setRawModifiers((byte)0);
			SimpleKeyBindingManager.setModifiersToShortcut(item);
			recording = false;
			updateRecordButton();
		} else {
			super.keyTyped(c, i);
		}
	}
	
	public void initGui() {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		recordLabel = new GenericLabel("Key:");
		recordLabel.setAlign(WidgetAnchor.TOP_LEFT);
		recordLabel.setWidth(50).setHeight(20);
		recordLabel.setX(10).setY(40);
		getScreen().attachWidget(spoutcraft, recordLabel);
		
		recordButton = new GenericButton();
		recordButton.setAlign(WidgetAnchor.CENTER_CENTER);
		recordButton.setHeight(20).setWidth(200);
		recordButton.setX(70).setY(40);
		getScreen().attachWidget(spoutcraft, recordButton);
		updateRecordButton();
		
		titleLabel = new GenericLabel("Name:");
		titleLabel.setAlign(WidgetAnchor.TOP_LEFT);
		titleLabel.setWidth(50).setHeight(20);
		titleLabel.setX(10).setY(10);
		getScreen().attachWidget(spoutcraft, titleLabel);
		
		titleText = new GenericTextField();
		titleText.setHeight(20).setWidth(200);
		titleText.setX(70).setY(10);
		titleText.setText(item.getTitle());
		titleText.setFocus(true);
		getScreen().attachWidget(spoutcraft, titleText);
		
		slot = new GuiCommandsSlot(this);
		getScreen().attachWidget(spoutcraft, slot);
		
		doneButton = new GenericButton("Done");
		doneButton.setHeight(20).setWidth(50);
		doneButton.setX(10).setY(height-30);
		getScreen().attachWidget(spoutcraft, doneButton);
		
		addButton = new GenericButton("Add Command");
		addButton.setHeight(20).setWidth(100);
		addButton.setX(70).setY(height-30);
		getScreen().attachWidget(spoutcraft, addButton);
		
		editButton = new GenericButton("Edit Command");
		editButton.setHeight(20).setWidth(100);
		editButton.setX(180).setY(height-30);
		getScreen().attachWidget(spoutcraft, editButton);
		
		removeButton = new GenericButton("Remove Command");
		removeButton.setHeight(20).setWidth(100);
		removeButton.setX(290).setY(height-30);
		getScreen().attachWidget(spoutcraft, removeButton);
		
		updateButtons();
	}
	
	private void updateRecordButton() {
		String keyname = recording?"Press a key!":"Click Here!";
		if(item.getKey()>=0 && !recording){
			keyname = item.toString();
		}
		String name = (recording?"> ":"")+keyname+(recording?" <":"");
		recordButton.setText(name);
	}

	protected void buttonClicked(Button btn) {
		if(btn.equals(recordButton)){
			recording = !recording;
			updateRecordButton();
		}
		if(btn.equals(doneButton)){
			item.setTitle(titleText.getText());
			if(!item.getTitle().equals("") && item.getKey() != -1) {
				parent.getManager().unregisterShortcut(item);
				parent.getManager().registerShortcut(item);
			}
			mc.displayGuiScreen(parent);
			parent.slot.updateItems();
		}
		if(btn.equals(addButton)){
			editCommand(-1);
		}
		if(btn.equals(editButton)){
			editCommand(slot.getSelectedRow());
		}
		if(btn.equals(removeButton)){
			item.removeCommand(slot.getSelectedRow());
			slot.updateItems();
			updateButtons();
		}
	}
	
	public void updateButtons() {
		editButton.setEnabled(slot.getSelectedRow() != -1);
		removeButton.setEnabled(slot.getSelectedRow() != -1);
	}

	public void editCommand(int i) {
		item.setTitle(titleText.getText());
		GuiEditCommand gui = new GuiEditCommand(this, i);
		mc.displayGuiScreen(gui);
	}

	public Shortcut getShortcut() {
		return item;
	}
}
