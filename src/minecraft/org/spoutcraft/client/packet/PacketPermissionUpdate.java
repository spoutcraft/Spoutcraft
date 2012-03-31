package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class PacketPermissionUpdate implements SpoutPacket {
private Map<String, Boolean> permissions;

	public PacketPermissionUpdate() {
		permissions = new HashMap<String, Boolean>();
	}
	
	public PacketPermissionUpdate(Map<String, Boolean> permissions) {
		this.permissions = permissions;
	}

	@Override
	public int getNumBytes() {
		int num = 0;
		num += 4; //Number of permissions as int
		for(String perm:permissions.keySet()) {
			num += PacketUtil.getNumBytes(perm) + 1; //Size of the string + 1 byte for the bool
		}
		return num;
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(permissions.size());
		for(Entry<String, Boolean> perm:permissions.entrySet()) {
			PacketUtil.writeString(output, perm.getKey());
			output.writeBoolean(perm.getValue());
		}
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		int num = input.readInt();
		for(int i = 0; i < num; i++) {
			String perm = PacketUtil.readString(input);
			boolean allowed = input.readBoolean();
			permissions.put(perm, allowed);
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketPermissionUpdate;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void run(int playerId) {
		for(Entry<String, Boolean> perm:permissions.entrySet()) {
			SpoutClient.getInstance().setPermission(perm.getKey(), perm.getValue());
		}
	}

	@Override
	public void failure(int playerId) {}

}
