package org.getspout.spoutapi.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.packet.PacketWidget;
import org.getspout.spoutapi.packet.PacketWidgetRemove;
import org.getspout.spoutapi.player.SpoutPlayer;

public abstract class GenericScreen extends GenericWidget implements Screen{
	protected HashMap<Widget, Plugin> widgets = new HashMap<Widget, Plugin>();
	protected int playerId;
	protected boolean bg = true;
	public GenericScreen() {
		
	}
	
	@Override
	public int getVersion() {
		return super.getVersion() + 0;
	}
	
	public GenericScreen(int playerId) {
		this.playerId = playerId;
	}

	@Override
	public Widget[] getAttachedWidgets() {
		Widget[] list = new Widget[widgets.size()];
		widgets.keySet().toArray(list);
		return list;
	}

	@Override
	@Deprecated
	public Screen attachWidget(Widget widget) {
		return attachWidget(null, widget);
	}
	
	@Override
	public Screen attachWidget(Plugin plugin, Widget widget) {
		widgets.put(widget, plugin);
		widget.setDirty(true);
		widget.setScreen(this);
		return this;
	}

	@Override
	public Screen removeWidget(Widget widget) {
		if (widgets.containsKey(widget)) {
			widgets.remove(widget);
			if(!widget.getType().isServerOnly()) {
				SpoutManager.getPlayerFromId(playerId).sendPacket(new PacketWidgetRemove(widget, getId()));
			}
			widget.setScreen(null);
		}
		return this;
	}
	
	@Override
	public Screen removeWidgets(Plugin p) {
		for (Widget i : getAttachedWidgets()) {
			if (widgets.get(i) != null && widgets.get(i).equals(p)) {
				removeWidget(i);
			}
		}
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
		for (Widget w : widgets.keySet()) {
			if (w.getId().equals(id)) {
				return w;
			}
		}
		return null;
	}
	
	@Override
	public boolean updateWidget(Widget widget) {
		if (widgets.containsKey(widget)) {
			Plugin plugin = widgets.get(widget);
			widgets.remove(widget);
			widgets.put(widget, plugin);
			widget.setScreen(this);
			return true;
		}
		return false;
	}
	
	@Override
	public void onTick() {
		SpoutPlayer player = SpoutManager.getPlayerFromId(playerId);
		if (player != null && player.getVersion() > 17) {
			for (Widget widget : widgets.keySet()) {
				widget.onTick();
				if (widget.isDirty()) {
					if(! widget.getType().isServerOnly()){
						player.sendPacket(new PacketWidget(widget, getId()));
					}
					widget.setDirty(false);
				}
			}
		}
	}
	
	public GenericScreen setBgVisible(boolean enable) {
		bg = enable;
		return this;
	}
	
	public boolean isBgVisible() {
		return bg;
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
	
	@Override
	public void setDirty(boolean dirty) {
		super.setDirty(dirty);
		if (dirty) {
			for (Widget widget : getAttachedWidgets()){
				widget.setDirty(true);
			}
		}
	}
	
	@Override
	public void render() {}
}
