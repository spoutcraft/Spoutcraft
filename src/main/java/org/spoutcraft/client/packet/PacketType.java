/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.packet;

import java.util.HashMap;

public enum PacketType {
	PacketKeyPress(0, org.spoutcraft.client.packet.builtin.PacketKeyPress.class),
	PacketAirTime(1, org.spoutcraft.client.packet.builtin.PacketAirTime.class),
	PacketSkinURL(2, org.spoutcraft.client.packet.builtin.PacketSkinURL.class),
	PacketEntityTitle(3, org.spoutcraft.client.packet.builtin.PacketEntityTitle.class),
	//PacketPluginReload(4, PacketPluginReload.class),
	PacketRenderDistance(5, org.spoutcraft.client.packet.builtin.PacketRenderDistance.class),
	PacketAlert(6, org.spoutcraft.client.packet.builtin.PacketAlert.class),
	PacketPlaySound(7, org.spoutcraft.client.packet.builtin.PacketPlaySound.class),
	PacketDownloadMusic(8, org.spoutcraft.client.packet.builtin.PacketDownloadMusic.class),
	PacketClipboardText(9, org.spoutcraft.client.packet.builtin.PacketClipboardText.class),
	PacketMusicChange(10, org.spoutcraft.client.packet.builtin.PacketMusicChange.class),
	PacketWidget(11, org.spoutcraft.client.packet.builtin.PacketWidget.class),
	PacketStopMusic(12, org.spoutcraft.client.packet.builtin.PacketStopMusic.class),
	PacketItemName(13, org.spoutcraft.client.packet.builtin.PacketItemName.class),
	PacketSky(14, org.spoutcraft.client.packet.builtin.PacketSky.class),
	//PacketTexturePack(15, PacketTexturePack.class),
	//PacketWorldSeed(16, PacketWorldSeed.class),
	PacketNotification(17, org.spoutcraft.client.packet.builtin.PacketNotification.class),
	PacketScreenAction(18, org.spoutcraft.client.packet.builtin.PacketScreenAction.class),
	PacketControlAction(19, org.spoutcraft.client.packet.builtin.PacketControlAction.class),
	//PacketCacheHashUpdate(20, PacketCacheHashUpdate.class),
	PacketAllowVisualCheats(21, org.spoutcraft.client.packet.builtin.PacketAllowVisualCheats.class),
	PacketWidgetRemove(22, org.spoutcraft.client.packet.builtin.PacketWidgetRemove.class),
	PacketEntitySkin(23, org.spoutcraft.client.packet.builtin.PacketEntitySkin.class),
	PacketBiomeWeather(24, org.spoutcraft.client.packet.builtin.PacketBiomeWeather.class),
	PacketChunkRefresh(25, org.spoutcraft.client.packet.builtin.PacketChunkRefresh.class),
	PacketOpenScreen(26, org.spoutcraft.client.packet.builtin.PacketOpenScreen.class),
	PacketPreCacheFile(27, org.spoutcraft.client.packet.builtin.PacketPreCacheFile.class),
	PacketCacheFile(28, org.spoutcraft.client.packet.builtin.PacketCacheFile.class),
	PacketCacheDeleteFile(29, org.spoutcraft.client.packet.builtin.PacketCacheDeleteFile.class),
	PacketPreCacheCompleted(30, org.spoutcraft.client.packet.builtin.PacketPreCacheCompleted.class),
	PacketMovementModifiers(31, org.spoutcraft.client.packet.builtin.PacketMovementModifiers.class),
	PacketSetVelocity(32, org.spoutcraft.client.packet.builtin.PacketSetVelocity.class),
	PacketFullVersion(33, org.spoutcraft.client.packet.builtin.PacketFullVersion.class),
	//PacketCustomId(34, PacketCustomId.class),
	//PacketItemTexture(35, PacketItemTexture.class),
	//PacketBlockHardness(36, PacketBlockHardness.class),
	PacketOpenSignGUI(37, org.spoutcraft.client.packet.builtin.PacketOpenSignGUI.class),
	PacketCustomBlockOverride(38, org.spoutcraft.client.packet.builtin.PacketCustomBlockOverride.class),
	PacketCustomBlockDesign(39, org.spoutcraft.client.packet.builtin.PacketCustomBlockDesign.class),
	//PacketUniqueId(40, PacketUniqueId.class),
	PacketKeyBinding(41, org.spoutcraft.client.packet.builtin.PacketKeyBinding.class),
	PacketBlockData(42, org.spoutcraft.client.packet.builtin.PacketBlockData.class),
	PacketCustomMultiBlockOverride(43, org.spoutcraft.client.packet.builtin.PacketCustomMultiBlockOverride.class),
	//PacketServerPlugins(44, PacketServerPlugins.class),
	//PacketAddonData(45, PacketAddonData.class),
	//PacketCustomMaterial(46, PacketCustomMaterial.class),
	PacketScreenshot(47, org.spoutcraft.client.packet.builtin.PacketScreenshot.class),
	PacketGenericItem(48, org.spoutcraft.client.packet.builtin.PacketGenericItem.class),
	PacketGenericTool(49, org.spoutcraft.client.packet.builtin.PacketGenericTool.class),
	PacketGenericBlock(50, org.spoutcraft.client.packet.builtin.PacketGenericBlock.class),
	PacketCustomBlockChunkOverride(51, org.spoutcraft.client.packet.builtin.PacketCustomBlockChunkOverride.class),
	PacketGenericFood(52, org.spoutcraft.client.packet.builtin.PacketGenericFood.class),
	PacketEntityInformation(53, org.spoutcraft.client.packet.builtin.PacketEntityInformation.class),
	PacketComboBox(54, org.spoutcraft.client.packet.builtin.PacketComboBox.class),
	PacketFocusUpdate(55, org.spoutcraft.client.packet.builtin.PacketFocusUpdate.class),
	//PacketClientAddons(56, PacketClientAddons.class),
	PacketPermissionUpdate(57, org.spoutcraft.client.packet.builtin.PacketPermissionUpdate.class),
	PacketSpawnTextEntity(58, org.spoutcraft.client.packet.builtin.PacketSpawnTextEntity.class),
	PacketSlotClick(59, org.spoutcraft.client.packet.builtin.PacketSlotClick.class),
	PacketWaypoint(60, org.spoutcraft.client.packet.builtin.PacketWaypoint.class),
	PacketParticle(61, org.spoutcraft.client.packet.builtin.PacketParticle.class),
	PacketAccessory(62, org.spoutcraft.client.packet.builtin.PacketAccessory.class),
	PacketValidatePrecache(63, org.spoutcraft.client.packet.builtin.PacketValidatePrecache.class),
	PacketRequestPrecache(64, org.spoutcraft.client.packet.builtin.PacketRequestPrecache.class),
	PacketSendPrecache(65, org.spoutcraft.client.packet.builtin.PacketSendPrecache.class),
    PacketSendLink(66, org.spoutcraft.client.packet.builtin.PacketSendLink.class);
    
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
