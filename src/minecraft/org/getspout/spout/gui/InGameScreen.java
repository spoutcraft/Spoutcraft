/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.getspout.spout.gui;

import java.util.UUID;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.predownload.GuiPredownload;
import org.spoutcraft.spoutcraftapi.gui.*;

public class InGameScreen extends GenericScreen implements InGameHUD{
	protected HealthBar health;
	protected BubbleBar bubble;
	protected ChatBar chat;
	protected ChatTextBox chatText;
	protected ArmorBar armor;
	protected HungerBar hunger;
	protected PopupScreen activePopup = null;
	
	public InGameScreen() {
		this.health = new HealthBar();
		this.bubble = new BubbleBar();
		this.chat = new ChatBar();
		this.chatText = new ChatTextBox();
		this.armor = new ArmorBar();
		this.hunger = new HungerBar();
		
		attachWidget(health).attachWidget(bubble).attachWidget(chat).attachWidget(chatText).attachWidget(armor).attachWidget(hunger);
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
		}
		super.onTick();
	}
	
	@Override
	public InGameScreen attachWidget(Widget widget) {
		if (canAttachWidget(widget)) {
			super.attachWidget(widget);
			return this;
		}
		throw new UnsupportedOperationException("Unsupported widget type");
	}
	
	@Override
	public boolean updateWidget(Widget widget) {
		if (widget instanceof HealthBar)
			health = (HealthBar)widget;
		else if (widget instanceof BubbleBar)
			bubble = (BubbleBar)widget;
		else if (widget instanceof ChatTextBox)
			chatText = (ChatTextBox)widget;
		else if (widget instanceof ChatBar)
			chat = (ChatBar)widget;
		else if (widget instanceof ArmorBar)
			armor = (ArmorBar)widget;
		else if (widget instanceof HungerBar)
			hunger = (HungerBar)widget;
		return super.updateWidget(widget);
	}

	@Override
	public Screen removeWidget(Widget widget) {
		if (widget instanceof HealthBar)
			throw new UnsupportedOperationException("Cannot remove the health bar. Use setVisible(false) to hide it instead");
		if (widget instanceof BubbleBar)
			throw new UnsupportedOperationException("Cannot remove the bubble bar. Use setVisible(false) to hide it instead");
		if (widget instanceof ChatTextBox)
			throw new UnsupportedOperationException("Cannot remove the chat text box. Use setVisible(false) to hide it instead");
		if (widget instanceof ChatBar)
			throw new UnsupportedOperationException("Cannot remove the chat bar. Use setVisible(false) to hide it instead");
		if (widget instanceof ArmorBar)
			throw new UnsupportedOperationException("Cannot remove the armor bar. Use setVisible(false) to hide it instead");
		if (widget instanceof HungerBar)
			throw new UnsupportedOperationException("Cannot remove the hunger bar. Use setVisible(false) to hide it instead");
		return super.removeWidget(widget);
	}
	
	@Override
	public UUID getId() {
		return new UUID(0, 0);
	}
	
	public boolean closePopup() {
		if (getActivePopup() == null) {
			return false;
		}
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
	
	public PopupScreen getActivePopup() {
		return activePopup;
	}
	
	public boolean attachPopupScreen(PopupScreen screen) {
		if (getActivePopup() == null) {
			activePopup = screen;
			if (SpoutClient.getHandle().currentScreen instanceof GuiPredownload) {
				((GuiPredownload)SpoutClient.getHandle().currentScreen).queuedScreen = new CustomScreen(screen);
			}
			else {
				SpoutClient.getHandle().displayGuiScreen(new CustomScreen(screen));
			}
			return true;
		}
		return false;
	}
	
	public boolean canAttachWidget(Widget widget) {
		if (widget instanceof Screen) {
			return false;
		}
		if (widget instanceof Control) {
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
		return widget instanceof HealthBar || widget instanceof BubbleBar || widget instanceof ChatTextBox || widget instanceof ChatBar || widget instanceof ArmorBar || widget instanceof HungerBar;
	}

	public ScreenType getScreenType() {
		return ScreenType.GAME_SCREEN;
	}
}
