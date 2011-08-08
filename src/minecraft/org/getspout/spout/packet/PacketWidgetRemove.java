package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.*;

public class PacketWidgetRemove implements SpoutPacket {
	protected Widget widget;
	protected UUID screen;
	public PacketWidgetRemove() {

	}
	
	public PacketWidgetRemove(Widget widget, UUID screen) {
		this.widget = widget;
		this.screen = screen;
	}

	@Override
	public int getNumBytes() {
		return widget.getNumBytes() + 20;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		int id = input.readInt();
		long msb = input.readLong();
		long lsb = input.readLong();
		screen = new UUID(msb, lsb);
		WidgetType widgetType = WidgetType.getWidgetFromId(id);
		if (widgetType != null) {
			try {
				widget = widgetType.getWidgetClass().newInstance();
				widget.readData(input);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(widget.getType().getId());
		output.writeLong(screen.getMostSignificantBits());
		output.writeLong(screen.getLeastSignificantBits());
		widget.writeData(output);
	}
	
	@Override
	public void run(int playerId) {
		InGameHUD mainScreen = SpoutClient.getInstance().getActivePlayer().getMainScreen();
		PopupScreen popup = mainScreen.getActivePopup();
		//Determine if this is a popup screen and if we need to update it
		if (widget instanceof PopupScreen && popup.getId().equals(widget.getId())) {
			mainScreen.closePopup();
		}
		//Determine if this is a widget on the main screen
		else if (screen.equals(mainScreen.getId())) {
			mainScreen.removeWidget(widget);
		}
		//Determine if this is a widget on the popup screen
		else if (popup != null && screen.equals(popup.getId())) {
			popup.removeWidget(widget);
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketWidgetRemove;
	}
	
	@Override
	public int getVersion() {
		return 0;
	}

}
