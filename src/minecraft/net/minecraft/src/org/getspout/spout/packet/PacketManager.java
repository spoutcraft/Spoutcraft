package org.getspout.spout.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Spout;
import net.minecraft.src.EntityClientPlayerMP;

public class PacketManager {
	private static Minecraft mc;
	
	public PacketManager(){
		this.mc = Spout.getGameInstance();
	}
	
	public boolean sendSpoutPacket(SpoutPacket packet){
		if(mc.thePlayer instanceof EntityClientPlayerMP && Spout.isEnabled()){
			EntityClientPlayerMP player = (EntityClientPlayerMP)mc.thePlayer;
			player.sendQueue.addToSendQueue(new CustomPacket(packet));
			return true;
		}
		return false;
	}
	
	public boolean sendSpoutPacket(SpoutPacket packet, int pluginVersion){
		if(Spout.getVersion() >= pluginVersion){
			return sendSpoutPacket(packet);
		}	
		return false;
	}
}