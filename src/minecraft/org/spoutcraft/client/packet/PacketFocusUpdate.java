package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.gui.Control;
import org.spoutcraft.spoutcraftapi.gui.InGameHUD;
import org.spoutcraft.spoutcraftapi.gui.PopupScreen;
import org.spoutcraft.spoutcraftapi.gui.Widget;

public class PacketFocusUpdate implements SpoutPacket {

	private Control control;
	private boolean focus;
	private UUID widgetId;
	
	public PacketFocusUpdate() {
		
	}
	
	public PacketFocusUpdate(Control control, boolean focus) {
		this.control = control;
		this.focus = focus;
	}

	public int getNumBytes() {
		return 16 + 1;
	}

	public void readData(DataInputStream input) throws IOException {
		widgetId = new UUID(input.readLong(), input.readLong());
		focus = input.readBoolean();
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeLong(control.getId().getMostSignificantBits());
		output.writeLong(control.getId().getLeastSignificantBits());
		output.writeBoolean(focus);
	}

	public void run(int playerId) {
		InGameHUD screen = SpoutClient.getInstance().getActivePlayer().getMainScreen();
		PopupScreen popup = screen.getActivePopup();
		if(popup != null) {
			Widget w = popup.getWidget(widgetId);
			if(w != null && w instanceof Control) {
				((Control)w).setFocus(focus);
			}
		}
	}

	public void failure(int playerId) {}

	public PacketType getPacketType() {
		return PacketType.PacketFocusUpdate;
	}

	public int getVersion() {
		return 0;
	}

}
