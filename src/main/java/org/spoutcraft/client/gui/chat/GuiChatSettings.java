/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.gui.chat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.ChatTextBox;
import org.spoutcraft.api.gui.CheckBox;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericCheckBox;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericScrollArea;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.gui.GuiSpoutScreen;

public class GuiChatSettings extends GuiSpoutScreen {
	private GuiScreen parent;
	private CheckBox checkShowMentions, checkShowJoins, checkShowColors, checkCloseOnDamage, checkGrabMouse, checkIgnorePeople, checkParseRegex;
	private Button buttonAdvancedMentions, buttonConfigureIgnores, buttonDone;
	private ChatOpacitySlider opacity;
	private GenericScrollArea scroll;
	private Label title;

	public GuiChatSettings(GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	protected void createInstances() {
		title = new GenericLabel("Chat Options");

		scroll = new GenericScrollArea();

		checkShowMentions = new GenericCheckBox("Show Highlights/Mentions");
		checkShowMentions.setTooltip("Highlight chat messages where your name or words you specified are mentioned.");
		checkShowMentions.setChecked(Configuration.isHighlightMentions());

		checkShowJoins = new GenericCheckBox("Show Joins");
		checkShowJoins.setTooltip("Show player join messages when a new player logs in.");
		checkShowJoins.setChecked(Configuration.isShowJoinMessages());

		checkShowColors = new GenericCheckBox("Show Color Help/Guide");
		checkShowColors.setTooltip("Shows an on-screen guide for chat colors.");
		checkShowColors.setChecked(Configuration.isShowChatColors());

		checkCloseOnDamage = new GenericCheckBox("Close Chat Window on Damage");
		checkCloseOnDamage.setTooltip("Close the chat screen if your player is damaged.\nYour message will be saved.");
		checkCloseOnDamage.setChecked(Configuration.isShowDamageAlerts());

		checkGrabMouse = new GenericCheckBox("Chat Window Grabs Mouse");
		checkGrabMouse.setTooltip("Opening the chat screen grabs the mouse.\nON, players can not look around - default behavior\nOFF, players can look and pan the screen during chat.");
		checkGrabMouse.setChecked(Configuration.isChatGrabsMouse());

		checkIgnorePeople = new GenericCheckBox("Ignore List");
		checkIgnorePeople.setTooltip("Prevents displaying chat messages from players in your ignore list.");
		checkIgnorePeople.setChecked(Configuration.isIgnorePeople());

		checkParseRegex = new GenericCheckBox("Enable Regex Usage");
		checkParseRegex.setTooltip("Parse highlighted words and ignored players using regular expression syntax.");
		checkParseRegex.setChecked(Configuration.isChatUsesRegex());

		buttonAdvancedMentions = new GenericButton("Configure");
		buttonAdvancedMentions.setTooltip("Configure words to be highlighted.");

		buttonConfigureIgnores = new GenericButton("Configure");
		buttonConfigureIgnores.setTooltip("Configure people to ignore messages from.");

		opacity = new ChatOpacitySlider();

		buttonDone = new GenericButton("Done");

		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		scroll.attachWidgets(spoutcraft, checkShowColors, checkShowJoins, checkShowMentions, checkCloseOnDamage, checkGrabMouse, checkIgnorePeople, buttonAdvancedMentions, buttonConfigureIgnores, checkParseRegex, opacity);
		getScreen().attachWidgets(spoutcraft, title, scroll, buttonDone);
	}

	@Override
	protected void layoutWidgets() {
		title.setX(width / 2 - SpoutClient.getHandle().fontRenderer.getStringWidth(title.getText()) / 2);
		title.setY(10);

		int top = title.getY() + 13;

		scroll.setGeometry(0, top, width, height - top - 30);
		scroll.updateInnerSize();

		int ftop = 5;
		int left = (int)(width / 2  - 155);
		int right = (int)(width / 2 + 5);
		int cellWidth = 150;

		checkShowMentions.setGeometry(left, ftop, cellWidth, 20);
		buttonAdvancedMentions.setGeometry(right, ftop, cellWidth, 20);
		ftop += 22;
		checkShowJoins.setGeometry(left, ftop, cellWidth, 20);
		ftop += 22;
		checkShowColors.setGeometry(left, ftop, cellWidth, 20);
		ftop += 22;
		checkCloseOnDamage.setGeometry(left, ftop, cellWidth, 20);
		ftop += 22;
		checkGrabMouse.setGeometry(left, ftop, cellWidth, 20);
		ftop += 22;
		checkIgnorePeople.setGeometry(left, ftop, cellWidth, 20);
		buttonConfigureIgnores.setGeometry(right, ftop, cellWidth, 20);
		ftop += 22;
		checkParseRegex.setGeometry(left, ftop, cellWidth, 20);
		ftop += 22;
		opacity.setGeometry(left, ftop, cellWidth, 20);
		ftop += 22;

		buttonDone.setGeometry(right, height - 25, cellWidth, 20);
	}

	@Override
	protected void buttonClicked(Button btn) {
		final ChatTextBox chat;
		if (Spoutcraft.getActivePlayer() != null) {
			chat = Spoutcraft.getActivePlayer().getMainScreen().getChatTextBox();
		} else {
			chat = null;
		}
		if (btn == buttonDone) {
			mc.displayGuiScreen(parent);
			return;
		}
		Runnable save = new Runnable() {
			public void run() {
				SpoutClient.getInstance().getChatManager().save();
				if (chat != null) {
					chat.reparse();
				}
			}
		};
		boolean regex = Configuration.isChatUsesRegex();
		if (btn == buttonAdvancedMentions) {
			GuiListEdit editor = new GuiListEdit(save, "Highlight List", regex?"You can use regular expressions.":"", this, SpoutClient.getInstance().getChatManager().wordHighlight);
			mc.displayGuiScreen(editor);
			return;
		}
		if (btn == buttonConfigureIgnores) {
			GuiListEdit editor = new GuiListEdit(save, "Ignore List", regex?"You can use regular expressions.":"", this, SpoutClient.getInstance().getChatManager().ignorePeople);
			mc.displayGuiScreen(editor);
			return;
		}
		if (btn == checkShowMentions) {
			Configuration.setHighlightMentions(checkShowMentions.isChecked());
		}
		if (btn == checkShowJoins) {
			Configuration.setShowJoinMessages(checkShowJoins.isChecked());
			if (chat != null) {
				chat.reparse();
			}
		}
		if (btn == checkShowColors) {
			Configuration.setShowChatColors(checkShowColors.isChecked());
		}
		if (btn == checkCloseOnDamage) {
			Configuration.setShowDamageAlerts(checkCloseOnDamage.isChecked());
		}
		if (btn == checkIgnorePeople) {
			Configuration.setIgnorePeople(checkIgnorePeople.isChecked());
		}
		if (btn == checkGrabMouse) {
			Configuration.setChatGrabsMouse(checkGrabMouse.isChecked());
		}
		if (btn == checkParseRegex) {
			Configuration.setChatUsesRegex(checkParseRegex.isChecked());
			if (chat != null) {
				chat.reparse();
			}
		}
		Configuration.write();
	}
}
