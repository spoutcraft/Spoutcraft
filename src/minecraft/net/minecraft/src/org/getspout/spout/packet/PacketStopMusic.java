package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.*;

public class PacketStopMusic implements SpoutPacket{
	private boolean resetTimer = false;
	private int fadeTime = -1;
	public PacketStopMusic() {
		
	}
	
	public PacketStopMusic(boolean resetTimer, int fadeTime) {
		this.resetTimer = resetTimer;
		this.fadeTime = fadeTime;
	}

	@Override
	public int getNumBytes() {
		return 5;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		resetTimer = input.readBoolean();
		fadeTime = input.readInt();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeBoolean(resetTimer);
		output.writeInt(fadeTime);
	}

	@Override
	public void run(int PlayerId) {
		if (fadeTime == -1){
			Spout.getGameInstance().sndManager.stopMusic();
		}
		else {
			Spout.getGameInstance().sndManager.fadeOut(fadeTime);
		}
		if (resetTimer) {
			Spout.getGameInstance().sndManager.resetTime();
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketStopMusic;
	}

}
