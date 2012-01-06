package org.spoutcraft.client.gui;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.packet.PacketFocusUpdate;
import org.spoutcraft.client.packet.PacketWidget;
import org.spoutcraft.spoutcraftapi.gui.Control;
import org.spoutcraft.spoutcraftapi.gui.Widget;
import org.spoutcraft.spoutcraftapi.gui.WidgetManager;

public class SimpleWidgetManager implements WidgetManager {

	public void sendWidgetUpdate(Widget widget) {
		if(widget.getScreen() == null) {
			return;
		}
		PacketWidget update = new PacketWidget(widget, widget.getScreen().getId());
		SpoutClient.getInstance().getPacketManager().sendSpoutPacket(update);
	}
	
	public void sendFocusUpdate(Control control, boolean focus) {
		PacketFocusUpdate update = new PacketFocusUpdate(control, focus);
		SpoutClient.getInstance().getPacketManager().sendSpoutPacket(update);
	}

}
