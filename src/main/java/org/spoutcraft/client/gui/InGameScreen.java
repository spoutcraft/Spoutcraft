/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spoutcraft.client.gui;

import java.util.LinkedList;
import java.util.UUID;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.*;
import org.spoutcraft.client.SpoutClient;

public class InGameScreen extends GenericScreen implements InGameHUD {
	protected HealthBar health;
	protected BubbleBar bubble;
	protected ChatBar chat;
	protected ChatTextBox chatText;
	protected ArmorBar armor;
	protected HungerBar hunger;
	protected ExpBar exp;
	protected ServerPlayerList playerList;
	protected PopupScreen activePopup = null;
	protected LinkedList<PopupScreen> queuedScreens = new LinkedList<PopupScreen>();

	public InGameScreen() {
		this.health = new HealthBar();
		this.bubble = new BubbleBar();
		this.chat = new ChatBar();
		this.chatText = new ChatTextBox();
		this.armor = new ArmorBar();
		this.hunger = new HungerBar();
		this.exp = new ExpBar();
		this.playerList = new ServerPlayerList();

		attachWidgets("Spoutcraft", health, bubble, chat, chatText, armor, hunger, exp, playerList);
	}

	public int getVersion() {
		return 0;
	}

	@Override
	public void onTick() {
		if (SpoutClient.getHandle().currentScreen == null) {
			activePopup = null;
		}
		if (activePopup != null) {
			activePopup.onTick();
		} else {
			PopupScreen queued = queuedScreens.poll();
			if (queued != null) {
				attachPopupScreen(queued);
			}
		}
		super.onTick();
	}

	@Override
	public InGameScreen attachWidget(String addon, Widget widget) {
		if (canAttachWidget(widget)) {
			super.attachWidget(addon, widget);
			return this;
		}
		throw new UnsupportedOperationException("Unsupported widget type");
	}

	@Override
	public boolean updateWidget(Widget widget) {
		if (widget instanceof HealthBar) {
			health = (HealthBar)widget;
		} else if (widget instanceof BubbleBar) {
			bubble = (BubbleBar)widget;
		} else if (widget instanceof ChatTextBox) {
			chatText = (ChatTextBox)widget;
		} else if (widget instanceof ChatBar) {
			chat = (ChatBar)widget;
			updateChatWindowSize((int) getWidth(), (int) getHeight());
		} else if (widget instanceof ArmorBar) {
			armor = (ArmorBar)widget;
		} else if (widget instanceof HungerBar) {
			hunger = (HungerBar)widget;
		} else if (widget instanceof ExpBar) {
			exp = (ExpBar)widget;
		} else if (widget instanceof ServerPlayerList) {
			playerList = (ServerPlayerList)widget;
		}
		return super.updateWidget(widget);
	}

	@Override
	public Screen removeWidget(Widget widget) {
		if (widget instanceof HealthBar) {
			throw new UnsupportedOperationException("Cannot remove the health bar. Use setVisible(false) to hide it instead");
		}
		if (widget instanceof BubbleBar) {
			throw new UnsupportedOperationException("Cannot remove the bubble bar. Use setVisible(false) to hide it instead");
		}
		if (widget instanceof ChatTextBox) {
			throw new UnsupportedOperationException("Cannot remove the chat text box. Use setVisible(false) to hide it instead");
		}
		if (widget instanceof ChatBar) {
			throw new UnsupportedOperationException("Cannot remove the chat bar. Use setVisible(false) to hide it instead");
		}
		if (widget instanceof ArmorBar) {
			throw new UnsupportedOperationException("Cannot remove the armor bar. Use setVisible(false) to hide it instead");
		}
		if (widget instanceof HungerBar) {
			throw new UnsupportedOperationException("Cannot remove the hunger bar. Use setVisible(false) to hide it instead");
		}
		if (widget instanceof ExpBar) {
			throw new UnsupportedOperationException("Cannot remove the exp bar. Use setVisible(false) to hide it instead");
		}
		if (widget instanceof ServerPlayerList) {
			throw new UnsupportedOperationException("Cannot remove the player list. Use setVisisble(false) to hide it instead");
		}
		return super.removeWidget(widget);
	}

	@Override
	public UUID getId() {
		return new UUID(0, 0);
	}

	public boolean closePopup() {
		SpoutClient.getHandle().displayGuiScreen(null);
		activePopup = null;
		return true;
	}

	public HealthBar getHealthBar() {
		return health;
	}

	public BubbleBar getBubbleBar() {
		return bubble;
	}

	public ChatBar getChatBar() {
		return chat;
	}

	public ChatTextBox getChatTextBox() {
		return chatText;
	}

	public ArmorBar getArmorBar() {
		return armor;
	}

	public HungerBar getHungerBar() {
		return hunger;
	}

	public ExpBar getExpBar() {
		return exp;
	}

	public ServerPlayerList getServerPlayerList() {
		return playerList;
	}

	public PopupScreen getActivePopup() {
		return activePopup;
	}

	public boolean attachPopupScreen(PopupScreen screen) {
		if (getActivePopup() == null) {
			activePopup = screen;
			SpoutClient.getHandle().displayGuiScreen(new CustomScreen(screen));
			return true;
		}
		queuedScreens.add(screen);
		return false;
	}

	public boolean canAttachWidget(Widget widget) {
		if (widget instanceof Screen) {
			return false;
		}
		return true;
	}

	public WidgetType getType() {
		return WidgetType.InGameScreen;
	}

	public void clearPopup() {
		activePopup = null;
	}

	@Override
	protected boolean canRender(Widget widget) {
		return super.canRender(widget) && !isCustomWidget(widget);
	}

	public static boolean isCustomWidget(Widget widget) {
		return widget instanceof HealthBar || widget instanceof BubbleBar || widget instanceof ChatTextBox || widget instanceof ChatBar || widget instanceof ArmorBar || widget instanceof HungerBar || widget instanceof ExpBar || widget instanceof ServerPlayerList;
	}

	public ScreenType getScreenType() {
		return ScreenType.GAME_SCREEN;
	}

	public void toggleSurvivalHUD(boolean toggle) {
		health.setVisible(toggle);
		bubble.setVisible(toggle);
		armor.setVisible(toggle);
		hunger.setVisible(toggle);
		exp.setVisible(toggle);
	}

	private void updateChatWindowSize(int screenWidth, int screenHeight) {
		chatText.setGeometry(0, 0, 320, screenHeight);
		//chat.setGeometry(0, 0, 320, screenHeight);
	}

	@Override
	protected void onScreenResized(int oldWidth, int oldHeight, int newWidth, int newHeight) {
		updateChatWindowSize(newWidth, newHeight);
		super.onScreenResized(oldWidth, oldHeight, newWidth, newHeight);
	}
}
