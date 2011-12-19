package org.spoutcraft.client.gui.shortcuts;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.controls.Shortcut;
import org.spoutcraft.client.controls.SimpleKeyBindingManager;
import org.spoutcraft.client.gui.controls.GuiControls;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;
import org.spoutcraft.spoutcraftapi.gui.Label;
import org.spoutcraft.spoutcraftapi.gui.RenderPriority;
import org.spoutcraft.spoutcraftapi.gui.TextField;
import org.spoutcraft.spoutcraftapi.gui.WidgetAnchor;

import net.minecraft.src.GuiScreen;

public class GuiEditShortcut extends GuiScreen {
	GuiControls parent;
	Shortcut item;
	Button recordButton, doneButton, addButton, editButton, removeButton;
	Label titleLabel, recordLabel, commandLabel, typeNameHereLabel, typeCommandHereLabel;
	TextField commandName, commandText;
	GuiCommandsSlot slot;
	int editingIndex = -1;
	Color grey = new Color(0xC0C0C0);
	
	boolean recording = false;
	public GuiEditShortcut(GuiControls guiControls, Shortcut item) {
		this.parent = guiControls;
		this.item = item;
	}
	
	public void drawScreen(int var1, int var2, float var3) {
		typeNameHereLabel.setVisible(commandName.getText().length() == 0 && !commandName.isFocus());
		typeCommandHereLabel.setVisible(commandText.getText().length() == 0 && !commandText.isFocus());
		addButton.setEnabled(commandName.getText().length() != 0 && commandText.getText().length() != 0 && item.getKey() != -1);
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
		recordLabel.setX(68 + width / 2).setY(10);
		getScreen().attachWidget(spoutcraft, recordLabel);
		
		recordButton = new GenericButton();
		recordButton.setAlign(WidgetAnchor.CENTER_CENTER);
		recordButton.setHeight(20).setWidth(100);
		recordButton.setX(90 + width / 2).setY(5);
		getScreen().attachWidget(spoutcraft, recordButton);
		updateRecordButton();
		
		titleLabel = new GenericLabel("Name:");
		titleLabel.setAlign(WidgetAnchor.TOP_LEFT);
		titleLabel.setWidth(50).setHeight(20);
		titleLabel.setX(10).setY(10);
		getScreen().attachWidget(spoutcraft, titleLabel);
		
		commandName = new GenericTextField();
		commandName.setHeight(16).setWidth(width / 2);
		commandName.setX(58).setY(8);
		commandName.setText(item.getTitle());
		commandName.setFocus(true);
		commandName.setMaximumCharacters(40);
		getScreen().attachWidget(spoutcraft, commandName);
		
		typeNameHereLabel = new GenericLabel("Enter a name for your shortcut");
		typeNameHereLabel.setAlign(WidgetAnchor.TOP_LEFT);
		typeNameHereLabel.setWidth(width / 2).setHeight(20);
		typeNameHereLabel.setX(61).setY(12);
		typeNameHereLabel.setTextColor(grey);
		typeNameHereLabel.setPriority(RenderPriority.Low);
		getScreen().attachWidget(spoutcraft, typeNameHereLabel);
		
		commandLabel = new GenericLabel("Command:");
		commandLabel.setAlign(WidgetAnchor.TOP_LEFT);
		commandLabel.setWidth(50).setHeight(20);
		commandLabel.setX(10).setY(36);
		getScreen().attachWidget(spoutcraft, commandLabel);
		
		commandText = new GenericTextField();
		commandText.setHeight(16).setWidth(355);
		commandText.setX(58).setY(33);
		commandText.setText(item.getTitle());
		commandText.setFocus(true);
		commandText.setMaximumCharacters(99);
		getScreen().attachWidget(spoutcraft, commandText);
		
		typeCommandHereLabel = new GenericLabel("Enter a command or chat message");
		typeCommandHereLabel.setAlign(WidgetAnchor.TOP_LEFT);
		typeCommandHereLabel.setWidth(width / 2).setHeight(20);
		typeCommandHereLabel.setX(61).setY(37);
		typeCommandHereLabel.setTextColor(grey);
		typeCommandHereLabel.setPriority(RenderPriority.Low);
		getScreen().attachWidget(spoutcraft, typeCommandHereLabel);
		
		slot = new GuiCommandsSlot(this);
		getScreen().attachWidget(spoutcraft, slot);
		
		doneButton = new GenericButton("Done");
		doneButton.setHeight(20).setWidth(50);
		doneButton.setX(10).setY(height-25);
		getScreen().attachWidget(spoutcraft, doneButton);
		
		addButton = new GenericButton("Add Command");
		addButton.setHeight(20).setWidth(100);
		addButton.setX(70).setY(height-25);
		getScreen().attachWidget(spoutcraft, addButton);
		
		editButton = new GenericButton("Edit Command");
		editButton.setHeight(20).setWidth(100);
		editButton.setX(180).setY(height-25);
		getScreen().attachWidget(spoutcraft, editButton);
		
		removeButton = new GenericButton("Remove Command");
		removeButton.setHeight(20).setWidth(100);
		removeButton.setX(290).setY(height-25);
		getScreen().attachWidget(spoutcraft, removeButton);
		
		updateButtons();
		
		commandName.setFocus(true);
	}
	
	private void updateRecordButton() {
		String keyname = recording?"Press a key!":"Click Here!";
		if(item.getKey()>=0 && !recording){
			keyname = item.toString();
		}
		String name = (recording?"> ":"")+keyname+(recording?" <":"");
		recordButton.setText(name);
	}

	@Override
	protected void buttonClicked(Button btn) {
		if (btn.equals(recordButton)){
			recording = !recording;
			updateRecordButton();
		}
		if (btn.equals(doneButton)){
			item.setTitle(commandName.getText());
			if(!item.getTitle().equals("") && item.getKey() != -1) {
				SimpleKeyBindingManager manager = (SimpleKeyBindingManager) SpoutClient.getInstance().getKeyBindingManager();
				manager.unregisterShortcut(item);
				manager.registerShortcut(item);
			}
			mc.displayGuiScreen(parent);
			parent.getModel().refresh();
		}
		if (btn.equals(addButton)){
			if (editingIndex != -1){
				addButton.setText("Add Command");
				item.setTitle(commandName.getText());
				item.getCommands().set(editingIndex, commandText.getText());
				editingIndex = -1;
			}
			else {
				item.addCommand(commandText.getText());
			}
			
			commandText.setText("");
			slot.updateItems();
			updateButtons();
		}
		if (btn.equals(editButton)){
			editCommand(slot.getSelectedRow());
		}
		if (btn.equals(removeButton)){
			item.removeCommand(slot.getSelectedRow());
			slot.updateItems();
			updateButtons();
		}
	}
	
	public void editCommand(int index) {
		if (index != -1) {
			editingIndex = index;
			commandText.setText(item.getCommands().get(index));
			addButton.setText("Finish Editing");
		}
	}
	
	public void updateButtons() {
		editButton.setEnabled(slot.getSelectedRow() != -1);
		removeButton.setEnabled(slot.getSelectedRow() != -1);
	}
	
	public Shortcut getShortcut() {
		return item;
	}
}