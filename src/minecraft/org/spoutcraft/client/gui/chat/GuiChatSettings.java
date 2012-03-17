package org.spoutcraft.client.gui.chat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.client.gui.GuiSpoutScreen;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.ChatTextBox;
import org.spoutcraft.spoutcraftapi.gui.CheckBox;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericCheckBox;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.Label;

public class GuiChatSettings extends GuiSpoutScreen {
	private GuiScreen parent;
	private CheckBox checkShowMentions, checkShowJoins, checkShowColors, checkCloseOnDamage, checkGrabMouse, checkIgnorePeople, checkParseRegex;
	private Button buttonAdvancedMentions, buttonConfigureIgnores, buttonDone;
	private Label title;
	
	public GuiChatSettings(GuiScreen parent) {
		this.parent = parent;
	}
	
	@Override
	protected void createInstances() {
		title = new GenericLabel("Chat Options");
		
		checkShowMentions = new GenericCheckBox("Show mentions");
		checkShowMentions.setTooltip("Highlight chat messages where your name or words you specified are mentioned.");
		checkShowMentions.setChecked(ConfigReader.highlightMentions);
		
		checkShowJoins = new GenericCheckBox("Show joins");
		checkShowJoins.setTooltip("Show player join messages when a new player logs in.");
		checkShowJoins.setChecked(ConfigReader.showJoinMessages);
		
		checkShowColors = new GenericCheckBox("Show colors");
		checkShowColors.setTooltip("Shows the notation for chat color.");
		checkShowColors.setChecked(ConfigReader.showChatColors);
		
		checkCloseOnDamage = new GenericCheckBox("Close chat window on damage");
		checkCloseOnDamage.setTooltip("Close the chat screen if your player is damaged.\nYour message will be saved.");
		checkCloseOnDamage.setChecked(ConfigReader.showDamageAlerts);
		
		checkGrabMouse = new GenericCheckBox("Chat window grabs mouse");
		checkGrabMouse.setTooltip("Opening the chat screen grabs the mouse.\nON, players can not look around - default behavior\nOFF, players can look and pan the screen during chat.");
		checkGrabMouse.setChecked(ConfigReader.chatGrabsMouse);
		
		checkIgnorePeople = new GenericCheckBox("Ignore List");
		checkIgnorePeople.setTooltip("This will prevent displaying chat messages from players in your ignore list");
		checkIgnorePeople.setChecked(ConfigReader.ignorePeople);
		
		checkParseRegex = new GenericCheckBox("Use regex");
		checkParseRegex.setTooltip("Parse highlighted words and ignored players using regular expression syntax");
		checkParseRegex.setChecked(ConfigReader.chatUsesRegex);
		
		buttonAdvancedMentions = new GenericButton("Advanced options");
		buttonAdvancedMentions.setTooltip("Configure words to be highlighted");
		
		buttonConfigureIgnores = new GenericButton("Configure");
		buttonConfigureIgnores.setTooltip("Configure people to ignore messages from");
		
		buttonDone = new GenericButton("Done");
		
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		getScreen().attachWidgets(spoutcraft, title, checkShowColors, checkShowJoins, checkShowMentions, checkCloseOnDamage, checkGrabMouse, checkIgnorePeople, buttonAdvancedMentions, buttonConfigureIgnores, buttonDone, checkParseRegex);
	}

	@Override
	protected void layoutWidgets() {
		title.setX(width / 2 - SpoutClient.getHandle().fontRenderer.getStringWidth(title.getText()) / 2);
		title.setY(12);
		
		int top = title.getY() + 13;
		
		checkShowMentions.setGeometry(5, top, 200, 20);
		buttonAdvancedMentions.setGeometry(width - 205, top, 150, 20);
		top += 22;
		checkShowJoins.setGeometry(5, top, 200, 20);
		top += 22;
		checkShowColors.setGeometry(5, top, 200, 20);
		top += 22;
		checkCloseOnDamage.setGeometry(5, top, 200, 20);
		top += 22;
		checkGrabMouse.setGeometry(5, top, 200, 20);
		top += 22;
		checkIgnorePeople.setGeometry(5, top, 200, 20);
		buttonConfigureIgnores.setGeometry(width - 205, top, 150, 20);
		top += 22;
		checkParseRegex.setGeometry(5, top, 200, 20);
		top += 22;
		
		buttonDone.setGeometry(width - 205, height - 25, 200, 20);
	}

	@Override
	protected void buttonClicked(Button btn) {
		final ChatTextBox chat;
		if(Spoutcraft.getActivePlayer() != null) {
			chat = Spoutcraft.getActivePlayer().getMainScreen().getChatTextBox();
		} else {
			chat = null;
		}
		if(btn == buttonDone) {
			mc.displayGuiScreen(parent);
			return;
		}
		Runnable save = new Runnable() {
			
			@Override
			public void run() {
				SpoutClient.getInstance().getChatManager().save();
				chat.reparse();
			}
		};
		boolean regex = ConfigReader.chatUsesRegex;
		if(btn == buttonAdvancedMentions) {
			GuiListEdit editor = new GuiListEdit(save, "Edit word highlight list", regex?"You can use regular expressions.":"", this, SpoutClient.getInstance().getChatManager().wordHighlight);
			mc.displayGuiScreen(editor);
			return;
		}
		if(btn == buttonConfigureIgnores) {
			GuiListEdit editor = new GuiListEdit(save, "Edit people ignore list", regex?"You can use regular expressions.":"", this, SpoutClient.getInstance().getChatManager().ignorePeople);
			mc.displayGuiScreen(editor);
			return;
		}
		if(btn == checkShowMentions) {
			ConfigReader.highlightMentions = checkShowMentions.isChecked();
		}
		if(btn == checkShowJoins) {
			ConfigReader.showJoinMessages = checkShowJoins.isChecked();
		}
		if(btn == checkShowColors) {
			ConfigReader.showChatColors = checkShowColors.isChecked();
		}
		if(btn == checkCloseOnDamage) {
			ConfigReader.showDamageAlerts = checkCloseOnDamage.isChecked();
		}
		if(btn == checkIgnorePeople) {
			ConfigReader.ignorePeople = checkIgnorePeople.isChecked();
		}
		if(btn == checkGrabMouse) {
			ConfigReader.chatGrabsMouse = checkGrabMouse.isChecked();
		}
		if(btn == checkParseRegex) {
			ConfigReader.chatUsesRegex = checkParseRegex.isChecked();
			chat.reparse();
		}
		ConfigReader.write();
	}
	
	

}
