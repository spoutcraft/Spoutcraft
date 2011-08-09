package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.src.ScaledResolution;

import org.getspout.spout.client.SpoutClient;
import org.lwjgl.opengl.GL11;

public abstract class GenericScreen extends GenericWidget implements Screen{
	protected List<Widget> widgets = new ArrayList<Widget>();
	protected int playerId;
	protected boolean bgvis;
	public GenericScreen() {
		ScaledResolution resolution = new ScaledResolution(SpoutClient.getHandle().gameSettings, SpoutClient.getHandle().displayWidth, SpoutClient.getHandle().displayHeight);
		screenWidth = resolution.getScaledWidth();
		screenHeight = resolution.getScaledHeight();
	}
	
	public GenericScreen(int playerId) {
		this.playerId = playerId;
	}

	@Override
	public Widget[] getAttachedWidgets() {
		Widget[] list = new Widget[widgets.size()];
		widgets.toArray(list);
		return list;
	}

	@Override
	public Screen attachWidget(Widget widget) {
		widgets.add(widget);
		widget.setScreen(this);
		return this;
	}

	@Override
	public Screen removeWidget(Widget widget) {
		widgets.remove(widget);
		widget.setScreen(null);
		return this;
	}
	
	@Override
	public boolean containsWidget(Widget widget) {
		return containsWidget(widget.getId());
	}
	
	@Override
	public boolean containsWidget(UUID id) {
		return getWidget(id) != null;
	}
	
	@Override
	public Widget getWidget(UUID id) {
		for (Widget w : widgets) {
			if (w.getId().equals(id)) {
				return w;
			}
		}
		return null;
	}
	
	@Override
	public boolean updateWidget(Widget widget) {
		int index = widgets.indexOf(widget);
		if (index > -1) {
			widgets.remove(index);
			widget.setScreen(this);
			widgets.add(index, widget);
			return true;
		}
		return false;
	}
	
	@Override
	public void onTick() {
		for (Widget widget : widgets) {
			widget.onTick();
		}
		ScaledResolution resolution = new ScaledResolution(SpoutClient.getHandle().gameSettings, SpoutClient.getHandle().displayWidth, SpoutClient.getHandle().displayHeight);
		screenWidth = resolution.getScaledWidth();
		screenHeight = resolution.getScaledHeight();
	}
	
	private int screenHeight, screenWidth;
	@Override
	public int getHeight() {
		return screenHeight > 0 ? screenHeight : 427;
	}
	
	@Override
	public int getWidth() {
		return screenWidth > 0 ? screenWidth : 240;
	}
	
	public GenericScreen setBgVisible(boolean enable) {
		bgvis = enable;
		return this;
	}
	
	public boolean isBgVisible() {
		return bgvis;
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 1;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setBgVisible(input.readBoolean());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeBoolean(isBgVisible());
	}
	
	protected boolean canRender(Widget widget) {
		return widget.isVisible();
	}
	
	public void render() {
		for (RenderPriority priority : RenderPriority.values()) {	
			for (Widget widget : getAttachedWidgets()){
				if (widget.getPriority() == priority && canRender(widget)) {
					GL11.glPushMatrix();
					widget.render();
					GL11.glPopMatrix();
				}
			}
		}
	}
}
