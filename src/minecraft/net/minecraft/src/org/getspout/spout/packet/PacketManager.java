package org.getspout.spout.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Spout;
import net.minecraft.src.EntityClientPlayerMP;

public class PacketManager {
	private static Minecraft mc;
	
	public PacketManager(){
		this.mc = Spout.getGameInstance();
	}
	
	/**
	 * Sends the packet to the plugin when it is enabled on the server
	 * @param packet the Packet to send
	 * @returns if the sending was successful
	 */
	public boolean sendSpoutPacket(SpoutPacket packet){
		if(mc.thePlayer instanceof EntityClientPlayerMP && Spout.isEnabled()){
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
		if(Spout.getVersion() >= pluginVersion){
			return sendSpoutPacket(packet);
		}	
		return false;
	}
}