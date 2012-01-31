package org.spoutcraft.client.gui;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.client.gui.GuiTextDialog.DialogEventHandler;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;

public class GuiTextDialog extends GuiSpoutScreen {

	private String title, text;
	private GenericLabel labelTitle;
	private GenericTextField textText;
	private GenericButton buttonCancel, buttonDone;
	private DialogEventHandler handler;
	private GuiScreen parent;
	
	public GuiTextDialog(String title, String text, DialogEventHandler handler, GuiScreen parent) {
		this.setTitle(title);
		this.setText(text);
		this.handler = handler;
		this.parent = parent;
	}
	
	protected void createInstances() {
		labelTitle = new GenericLabel(title);
		textText = new GenericTextField();
		textText.setWidth(200);
		textText.setText(text);
		
		buttonCancel = new GenericButton("Cancel");
		buttonDone = new GenericButton("Done");
		
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		getScreen().attachWidgets(spoutcraft, labelTitle, textText, buttonCancel, buttonDone);
	}

	protected void layoutWidgets() {
		final int totalHeight = 11 + 5 + 20 + 5 + 20;
		int top = height / 2 - totalHeight / 2;
		labelTitle.setWidth(200).setHeight(11).setX(width / 2 - 100).setY(top);
		top += 16;
		textText.setWidth(200).setHeight(20).setX(width / 2 - 100).setY(top);
		top += 25;
		buttonCancel.setWidth(95).setHeight(20).setX(width / 2 - 100).setY(top);
		buttonDone.setWidth(100).setHeight(20).setX(width / 2).setY(top);
	}

	protected void buttonClicked(Button btn) {
		if(btn == buttonCancel) {
			handler.onCancel(this);
			mc.displayGuiScreen(parent);
		}
		if(btn == buttonDone) {
			handler.onDone(this);
			mc.displayGuiScreen(parent);
		}
	}
	
	public void setTitle(String title) {
		this.title = title;
		if(labelTitle != null) {
			labelTitle.setText(title);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setText(String text) {
		this.text = text;
		if(textText != null) {
			textText.setText(text);  //redundant text is redundant
		}
	}

	public String getText() {
		text = textText.getText();
		return text;
	}
	
	public interface DialogEventHandler {
		public void onDone(GuiTextDialog dialog);
		public void onCancel(GuiTextDialog dialog);
	}

}
