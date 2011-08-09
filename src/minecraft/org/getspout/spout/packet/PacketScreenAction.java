package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.CustomScreen;

public class PacketScreenAction implements SpoutPacket{
	protected byte action = -1;
	protected byte accepted = 1;
	
	public PacketScreenAction() {
	
	}
	
	public PacketScreenAction(ScreenAction action) {
		this.action = (byte)action.getId();
	}
	
	@Override
	public int getNumBytes() {
		return 2;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		action = input.readByte();
		accepted = input.readByte();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeByte(action);
		output.writeByte(accepted);
	}

	@Override
	public void run(int playerId) {
		if (action == ScreenAction.ScreenClose.getId()) {
			if (SpoutClient.getInstance().getActivePlayer().getMainScreen().getActivePopup() != null) {
				if (SpoutClient.getHandle().currentScreen != null && SpoutClient.getHandle().currentScreen instanceof CustomScreen) {
					if (accepted == 1) {
						((CustomScreen)SpoutClient.getHandle().currentScreen).closeScreen();
					}
					else if (accepted == 2) {
						((CustomScreen)SpoutClient.getHandle().currentScreen).waiting = true;
						((CustomScreen)SpoutClient.getHandle().currentScreen).closeScreen();
					}
					else {
						((CustomScreen)SpoutClient.getHandle().currentScreen).failedCloseScreen();
					}
				}
			}
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketScreenAction;
	}
	
	@Override
	public int getVersion() {
		return 0;
	}

}
