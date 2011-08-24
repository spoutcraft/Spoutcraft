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
import org.getspout.spout.gui.InGameHUD;
import org.getspout.spout.gui.InGameScreen;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;

public class ClientPlayer extends SpoutPlayer implements ActivePlayer{
	private RenderDistance min, max;
	private InGameScreen mainScreen = new InGameScreen();
	private HashMap<Integer, String> titles = new HashMap<Integer, String>();

	public ClientPlayer(EntityPlayer player) {
		super(player);
		min = RenderDistance.TINY;
		max = RenderDistance.FAR;
	}

	@Override
	public RenderDistance getMaximumView() {
		return max;
	}

	@Override
	public RenderDistance getMinimumView() {
		return min;
	}

	@Override
	public void setMaximumView(RenderDistance distance) {
		max = distance;
	}

	@Override
	public void setMinimumView(RenderDistance distance) {
		min = distance;
	}
	
	@Override
	public RenderDistance getCurrentView() {
		return RenderDistance.getRenderDistanceFromValue(Minecraft.theMinecraft.gameSettings.renderDistance);
	}
	
	@Override
	public RenderDistance getNextRenderDistance() {
		int next = getCurrentView().getValue() + (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? -1 : 1);
		if (next > min.getValue()) {
			next = max.getValue();
		} else if (next < max.getValue()) {
			next = min.getValue();
		}
		return RenderDistance.getRenderDistanceFromValue(next);
	}
	
	@Override
	public InGameHUD getMainScreen() {
		return mainScreen;
	}

	@Override
	public void showAchievement(String title, String message, int id) {
		SpoutClient.getHandle().guiAchievement.queueNotification(title, message, id);
	}
	
	@Override
	public void showAchievement(String title, String message, int id, int data, int time) {
		SpoutClient.getHandle().guiAchievement.queueNotification(title, message, id, (short) data, time);
	}

	@Override
	public String getEntityTitle(int id) {
		return titles.get(id);
	}

	@Override
	public void setEntityTitle(int id, String title) {
		titles.put(id, title);
	}
	
	@Override
	public void resetEntityTitle(int id) {
		titles.remove(id);
	}

}
