package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;

import org.getspout.spout.gui.ScreenType;

public class PacketScreenAction implements SpoutPacket{
	protected byte action = -1;
	protected byte screen = -1; // UnknownScreen
	
	public PacketScreenAction() {
	
	}
	
	public PacketScreenAction(ScreenAction action, ScreenType screen) {
 		this.action = (byte)action.getId();
		this.screen = (byte)screen.getCode();
	}
	
	@Override
	public int getNumBytes() {
		return 2;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		action = input.readByte();
		screen = input.readByte();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeByte(action);
		output.writeByte(screen);
	}

	@Override
	public void run(int playerId) {
		switch(ScreenAction.getScreenActionFromId(action)) {
			case Open:
				SpoutClient.getHandle().displayGuiScreen();
				break;
			case Close:
				SpoutClient.getHandle().displayGuiScreen();
				break;
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketScreenAction;
	}
	
	@Override
	public int getVersion() {
		return 1;
	}

}
