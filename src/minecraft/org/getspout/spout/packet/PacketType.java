package org.getspout.spout.packet;

import java.util.HashMap;

public enum PacketType {
	PacketKeyPress(0, PacketKeyPress.class),
	PacketAirTime(1, PacketAirTime.class),
	PacketSkinURL(2, PacketSkinURL.class),
	PacketEntityTitle(3, PacketEntityTitle.class),
	PacketPluginReload(4, PacketPluginReload.class),
	PacketRenderDistance(5, PacketRenderDistance.class),
	PacketAlert(6, PacketAlert.class),
	PacketPlaySound(7, PacketPlaySound.class),
	PacketDownloadMusic(8, PacketDownloadMusic.class),
	PacketClipboardText(9, PacketClipboardText.class),
	PacketMusicChange(10, PacketMusicChange.class),
	PacketWidget(11, PacketWidget.class),
	PacketStopMusic(12, PacketStopMusic.class),
	PacketItemName(13, PacketItemName.class),
	PacketSky(14, PacketSky.class),
	PacketTexturePack(15, PacketTexturePack.class),
	PacketWorldSeed(16, PacketWorldSeed.class),
	PacketNotification(17, PacketNotification.class),
	PacketScreenAction(18, PacketScreenAction.class),
	PacketControlAction(19, PacketControlAction.class),
	PacketCacheHashUpdate(20, PacketCacheHashUpdate.class),
	PacketAllowVisualCheats(21, PacketAllowVisualCheats.class),
	;
	
	private final int id;
	private final Class<? extends SpoutPacket> packetClass;
	private static final HashMap<Integer, PacketType> lookupId = new HashMap<Integer, PacketType>();
	PacketType(final int type, final Class<? extends SpoutPacket> packetClass) {
		this.id = type;
		this.packetClass = packetClass;
	}
	
	public int getId() {
		return id;
	}
	
	public Class<? extends SpoutPacket> getPacketClass() {
		return packetClass;
	}
	
	public static PacketType getPacketFromId(int id) {
		return lookupId.get(id);
	}
	
	
	static {
		for (PacketType packet : values()) {
			lookupId.put(packet.getId(), packet);
		}
	}
}
