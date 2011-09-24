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
package org.getspout.spout.player;

import java.util.HashMap;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.InGameScreen;
import org.getspout.spout.gui.ScreenUtil;
import org.lwjgl.input.Keyboard;
import org.spoutcraft.spoutcraftapi.entity.ActivePlayer;
import org.spoutcraft.spoutcraftapi.gui.InGameHUD;
import org.spoutcraft.spoutcraftapi.gui.Screen;
import org.spoutcraft.spoutcraftapi.gui.ScreenType;
import org.spoutcraft.spoutcraftapi.player.RenderDistance;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;

public class ClientPlayer extends SpoutPlayer implements ActivePlayer{
	private RenderDistance min, max;
	private InGameScreen mainScreen = new InGameScreen();
	private HashMap<Integer, String> titles = new HashMap<Integer, String>();
	private Screen currentScreen = null;
	private ScreenType screenType;

	public ClientPlayer(EntityPlayer player) {
		super(player);
		min = RenderDistance.TINY;
		max = RenderDistance.FAR;
	}
	
	public EntityPlayerSP getHandle() {
		return (EntityPlayerSP)super.getHandle();
	}

	public RenderDistance getMaximumView() {
		return max;
	}

	public RenderDistance getMinimumView() {
		return min;
	}

	public void setMaximumView(RenderDistance distance) {
		max = distance;
	}

	public void setMinimumView(RenderDistance distance) {
		min = distance;
	}
	
	public RenderDistance getCurrentView() {
		return RenderDistance.getRenderDistanceFromValue(Minecraft.theMinecraft.gameSettings.renderDistance);
	}
	
	public RenderDistance getNextRenderDistance() {
		int next = getCurrentView().getValue() + (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? -1 : 1);
		if (next > min.getValue()) {
			next = max.getValue();
		} else if (next < max.getValue()) {
			next = min.getValue();
		}
		return RenderDistance.getRenderDistanceFromValue(next);
	}
	
	public InGameHUD getMainScreen() {
		return mainScreen;
	}

	public void showAchievement(String title, String message, int id) {
		SpoutClient.getHandle().guiAchievement.queueNotification(title, message, id);
	}
	
	public void showAchievement(String title, String message, int id, int data, int time) {
		SpoutClient.getHandle().guiAchievement.queueNotification(title, message, id, (short) data, time);
	}

	public String getEntityTitle(int id) {
		return titles.get(id);
	}

	public void setEntityTitle(int id, String title) {
		titles.put(id, title);
	}
	
	public void resetEntityTitle(int id) {
		titles.remove(id);
	}
	
	public FixedLocation getLastClickedLocation() {
		return getHandle().lastClickLocation;
	}

	public Screen getCurrentScreen() {
		//See if the screen has changed since last get
		ScreenType currentType = ScreenUtil.getType(SpoutClient.getHandle().currentScreen);
		if(currentType != screenType){
			currentScreen = null;
		}
		screenType = currentType;
		return currentScreen;
	}
	
	public void setCurrentScreen(Screen screen) {
		currentScreen = screen;
	}
}
