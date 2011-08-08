package org.getspout.spout.player;

import org.getspout.spout.gui.InGameHUD;
import org.getspout.spout.gui.InGameScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;

public class ClientPlayer extends SpoutPlayer implements ActivePlayer{
	private RenderDistance min, max;
	private InGameScreen mainScreen = new InGameScreen();

	public ClientPlayer(EntityPlayer player) {
		super(player);
		min = RenderDistance.TINY;
		max = RenderDistance.FAR;
	}
	
	public EntityClientPlayerMP getHandle() {
		return (EntityClientPlayerMP)super.getHandle();
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
		int next = getCurrentView().getValue() - 1;
		if (next < min.getValue()) {
			next = max.getValue();
		}
		return RenderDistance.getRenderDistanceFromValue(next);
	}
	
	@Override
	public InGameHUD getMainScreen() {
		return mainScreen;
	}

	@Override
	public void showAchievement(String title, String message, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getEntityTitle(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEntityTitle(int id, String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetEntityTitle(int id) {
		// TODO Auto-generated method stub
		
	}

}
