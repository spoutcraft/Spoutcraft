/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.player;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.EntityPlayerSP;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.InGameScreen;
import org.spoutcraft.spoutcraftapi.GameMode;
import org.spoutcraft.spoutcraftapi.entity.ActivePlayer;
import org.spoutcraft.spoutcraftapi.gui.InGameHUD;
import org.spoutcraft.spoutcraftapi.player.RenderDistance;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;
import org.spoutcraft.spoutcraftapi.util.Location;
import org.spoutcraft.spoutcraftapi.util.MutableLocation;

public class ClientPlayer extends SpoutPlayer implements ActivePlayer{
	private static ClientPlayer instance = null;
	private RenderDistance min, max;
	private InGameScreen mainScreen = new InGameScreen();

	public static ClientPlayer getInstance() {
		if (instance == null) {
			instance = new ClientPlayer(SpoutClient.getHandle().thePlayer);
			instance.setPlayer(SpoutClient.getHandle().thePlayer);
			SpoutClient.getInstance().player = (ClientPlayer) instance;
		}
		return instance;
	}

	private ClientPlayer(EntityPlayerSP entity) {
		super(entity);
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
		return RenderDistance.getRenderDistanceFromValue(SpoutClient.getHandle().gameSettings.renderDistance);
	}

	public void setCurrentView(RenderDistance view) {
		SpoutClient.getHandle().gameSettings.renderDistance = view.getValue();
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

	public FixedLocation getLastClickedLocation() {
		return getHandle().lastClickLocation;
	}

	public void sendMessage(String msg) {
		SpoutClient.getHandle().ingameGUI.addChatMessage(msg);
	}

	public void setCompassTarget(Location loc) {
		SpoutClient.getHandle().thePlayer.setSpawnChunk(new ChunkCoordinates(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		SpoutClient.getHandle().theWorld.getWorldInfo().setSpawnPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

	public Location getCompassTarget() {
		ChunkCoordinates coords = SpoutClient.getHandle().thePlayer.getSpawnChunk();
		return new MutableLocation(SpoutClient.getInstance().getWorld(), coords.posX, coords.posY, coords.posZ);
	}

	public void sendRawMessage(String message) {
		SpoutClient.getHandle().thePlayer.sendChatMessage(message);
	}

	public void disconnect(String message) {
		SpoutClient.getHandle().theWorld.sendQuittingDisconnectingPacket();
		SpoutClient.getHandle().changeWorld1(null);
	}

	public void chat(String msg) {
		SpoutClient.getInstance().getChatManager().sendChat(msg);
	}

	public boolean performCommand(String command) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSprinting() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setSprinting(boolean sprinting) {
		// TODO Auto-generated method stub

	}

	public int getExperience() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setExperience(int exp) {
		// TODO Auto-generated method stub

	}

	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setLevel(int level) {
		// TODO Auto-generated method stub

	}

	public int getTotalExperience() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setTotalExperience(int exp) {
		// TODO Auto-generated method stub

	}

	public float getExhaustion() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setExhaustion(float value) {
		// TODO Auto-generated method stub

	}

	public float getSaturation() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setSaturation(float value) {
		// TODO Auto-generated method stub

	}

	public int getFoodLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setFoodLevel(int value) {
		// TODO Auto-generated method stub

	}

	public GameMode getGameMode() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setGameMode(GameMode mode) {
		// TODO Auto-generated method stub

	}
}
