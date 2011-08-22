/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.getspout.spout.packet;

import org.getspout.spout.client.SpoutClient;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;

public class PacketManager {
	private static Minecraft mc;
	
	public PacketManager(){
		PacketManager.mc = SpoutClient.getHandle();
	}
	
	/**
	 * Sends the packet to the plugin when it is enabled on the server
	 * @param packet the Packet to send
	 * @returns if the sending was successful
	 */
	public boolean sendSpoutPacket(SpoutPacket packet){
		if(mc.thePlayer instanceof EntityClientPlayerMP && SpoutClient.getInstance().isSpoutEnabled()){
			EntityClientPlayerMP player = (EntityClientPlayerMP)mc.thePlayer;
			player.sendQueue.addToSendQueue(new CustomPacket(packet));
			return true;
		}
		return false;
	}
	
	/**
	 * Sends the packet to the plugin when it is enabled on the server and has version >= pluginVersion
	 * @param packet the Packet to send
	 * @param pluginVersion the minimum version of the plugin 
	 * @returns if the sending was successful
	 */
	public boolean sendSpoutPacket(SpoutPacket packet, int pluginVersion){
		if(SpoutClient.getInstance().getServerVersion().getVersion() >= pluginVersion){
			return sendSpoutPacket(packet);
		}	
		return false;
	}
}