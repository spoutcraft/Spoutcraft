package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.*;

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
			if (Spout.getMainScreen().getActivePopup() != null) {
				if (Spout.getGameInstance().currentScreen != null && Spout.getGameInstance().currentScreen instanceof CustomScreen) {
					if (accepted == 1) {
						((CustomScreen)Spout.getGameInstance().currentScreen).closeScreen();
					}
					else if (accepted == 2) {
						((CustomScreen)Spout.getGameInstance().currentScreen).waiting = true;
						((CustomScreen)Spout.getGameInstance().currentScreen).closeScreen();
					}
					else {
						((CustomScreen)Spout.getGameInstance().currentScreen).failedCloseScreen();
					}
				}
			}
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketScreenAction;
	}

}
