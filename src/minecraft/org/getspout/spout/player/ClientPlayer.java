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

import java.net.InetSocketAddress;
import java.util.HashMap;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.InGameScreen;
import org.getspout.spout.gui.ScreenUtil;
import org.lwjgl.input.Keyboard;
import org.spoutcraft.spoutcraftapi.Achievement;
import org.spoutcraft.spoutcraftapi.Statistic;
import org.spoutcraft.spoutcraftapi.entity.ActivePlayer;
import org.spoutcraft.spoutcraftapi.gui.InGameHUD;
import org.spoutcraft.spoutcraftapi.gui.Screen;
import org.spoutcraft.spoutcraftapi.gui.ScreenType;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.player.RenderDistance;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;
import org.spoutcraft.spoutcraftapi.util.Location;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayerSP;

public class ClientPlayer extends SpoutPlayer implements ActivePlayer{
	private static ClientPlayer instance = null;
	private RenderDistance min, max;
	private InGameScreen mainScreen = new InGameScreen();
	private HashMap<Integer, String> titles = new HashMap<Integer, String>();
	private Screen currentScreen = null;
	private ScreenType screenType;

	public static ClientPlayer getInstance() {
		if (instance == null) {
			instance = new ClientPlayer();
			instance.setPlayer(Minecraft.theMinecraft.thePlayer);
			SpoutClient.getInstance().player = (ClientPlayer) instance;
		}
		return instance;
	}
	
	private ClientPlayer() {
		min = RenderDistance.TINY;
		max = RenderDistance.FAR;
	}
	
	public EntityPlayerSP getHandle() {
		return (EntityPlayerSP)super.getMCPlayer();
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
	
	public void setCurrentView(RenderDistance view){
		Minecraft.theMinecraft.gameSettings.renderDistance = view.getValue();
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
	
	public void resetMainScreen() {
		mainScreen = new InGameScreen();
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

	public void sendMessage(String paramString) {
		// TODO Auto-generated method stub
		
	}

	public void setCompassTarget(Location loc) {
		// TODO Auto-generated method stub
		
	}

	public Location getCompassTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public InetSocketAddress getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendRawMessage(String message) {
		SpoutClient.getHandle().thePlayer.sendChatMessage(message);
	}

	public void disconnect(String message) {
		Minecraft.theMinecraft.theWorld.sendQuittingDisconnectingPacket();
		Minecraft.theMinecraft.changeWorld1(null);
	}

	public void chat(String msg) {
		SpoutClient.getInstance().getChatManager().sendChat(msg);
	}

	public boolean performCommand(String command) {
		// TODO Auto-generated method stub
		return false;
	}

	public void saveData() {
		// TODO Auto-generated method stub
		
	}

	public void loadData() {
		// TODO Auto-generated method stub
		
	}

	public void awardAchievement(Achievement achievement) {
		// TODO Auto-generated method stub
		
	}

	public void incrementStatistic(Statistic statistic) {
		// TODO Auto-generated method stub
		
	}

	public void incrementStatistic(Statistic statistic, int amount) {
		// TODO Auto-generated method stub
		
	}

	public void incrementStatistic(Statistic statistic, MaterialData material) {
		// TODO Auto-generated method stub
		
	}

	public void incrementStatistic(Statistic statistic, MaterialData material,
			int amount) {
		// TODO Auto-generated method stub
		
	}
}
