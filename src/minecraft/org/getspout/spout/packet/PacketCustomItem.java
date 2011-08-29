package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.item.SpoutItemBlock;

public class PacketCustomItem implements SpoutPacket {
	
	private int itemId;
	private Integer blockId;
	private String textureURL;
	private String pluginName;
	
	public PacketCustomItem() {
	}
	
	public PacketCustomItem(int itemId, Integer blockId, String pluginName, String textureURL) {
		this.itemId = itemId;
		this.blockId = blockId;
		this.textureURL = textureURL;
		this.pluginName = pluginName;
	}
	
	@Override
	public int getNumBytes() {
		return 8 + PacketUtil.getNumBytes(getTextureURL()) + PacketUtil.getNumBytes(getPluginName());
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		itemId = input.readInt();
		blockId = input.readInt();
		if (blockId == -1) {
			blockId = null;
		}
		textureURL = PacketUtil.readString(input, 256);
		if (textureURL.equals("")) {
			textureURL = null;
		}
		pluginName = PacketUtil.readString(input, 256);
		if (pluginName == "") {
			pluginName = null;
		}
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(itemId);
		output.writeInt(blockId == null ? -1 : blockId);
		PacketUtil.writeString(output, getTextureURL());
		PacketUtil.writeString(output, getPluginName());
	}
	
	private String getTextureURL() {
		return textureURL == null ? "" : textureURL;
	}
	
	private String getPluginName() {
		return pluginName == null ? "" : pluginName;
	}
	
	@Override
	public void run(int PlayerId) {
		SpoutItemBlock.addItemBlockMap(itemId, blockId);
	}
	
	@Override
	public PacketType getPacketType() {
		return PacketType.PacketCustomItem;
	}
	
	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public void failure(int playerId) {
		
	}

}
