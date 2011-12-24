package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.entity.ActivePlayer;
import org.spoutcraft.spoutcraftapi.gui.GenericComboBox;
import org.spoutcraft.spoutcraftapi.gui.Widget;

public class PacketComboBox implements SpoutPacket {
	private GenericComboBox box;
	private UUID uuid;
	private boolean open;
	private int selection;
	
	public PacketComboBox() {
	}
	
	public PacketComboBox(GenericComboBox box) {
		this.box = box;
		this.uuid = box.getId();
		this.open = box.isOpen();
		this.selection = box.getSelectedRow();
	}
	
	public int getNumBytes() {
		return 8 + 8 + 1 + 4;
	}

	public void readData(DataInputStream input) throws IOException {
		uuid = new UUID(input.readLong(), input.readLong());
		open = input.readBoolean();
		selection = input.readInt();
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeLong(uuid.getMostSignificantBits());
		output.writeLong(uuid.getLeastSignificantBits());
		output.writeBoolean(open);
		output.writeInt(selection);
	}

	public void run(int playerId) {
		ActivePlayer player = SpoutClient.getInstance().getActivePlayer();
		if(player.getMainScreen().getActivePopup() != null) {
			Widget w = player.getMainScreen().getActivePopup().getWidget(uuid);
			if(w != null && w instanceof GenericComboBox) {
				box = (GenericComboBox) w;
				box.setOpen(open);
				box.setSelection(selection);
			}
		}
	}

	public void failure(int playerId) {}

	public PacketType getPacketType() {
		return PacketType.PacketComboBox;
	}

	public int getVersion() {
		return 0;
	}

}
