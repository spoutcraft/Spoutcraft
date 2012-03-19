package org.spoutcraft.client.gui.settings;

import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.chat.GuiChatSettings;

public class ChatButton extends GenericButton {
	GameSettingsScreen parent;

	public ChatButton(GameSettingsScreen parent) {
		super("Chat Options");
		this.parent = parent;
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		GuiChatSettings chatSettings = new GuiChatSettings(parent);
		SpoutClient.getHandle().displayGuiScreen(chatSettings);
	}
}
