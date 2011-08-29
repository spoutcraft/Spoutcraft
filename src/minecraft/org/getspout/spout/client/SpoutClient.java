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
package org.getspout.spout.client;

import org.getspout.spout.ClipboardThread;
import org.getspout.spout.DataMiningThread;
import org.getspout.spout.SpoutVersion;
import org.getspout.spout.config.ConfigReader;
import org.getspout.spout.entity.EntityManager;
import org.getspout.spout.entity.SimpleEntityManager;
import org.getspout.spout.inventory.ItemManager;
import org.getspout.spout.inventory.SimpleItemManager;
import org.getspout.spout.io.CustomTextureManager;
import org.getspout.spout.io.FileDownloadThread;
import org.getspout.spout.io.FileUtil;
import org.getspout.spout.packet.CustomPacket;
import org.getspout.spout.packet.PacketManager;
import org.getspout.spout.packet.SpoutPacket;
import org.getspout.spout.player.ActivePlayer;
import org.getspout.spout.player.BiomeManager;
import org.getspout.spout.player.ChatManager;
import org.getspout.spout.player.ClientPlayer;
import org.getspout.spout.player.SimpleBiomeManager;
import org.getspout.spout.player.SimpleSkyManager;
import org.getspout.spout.player.SkyManager;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet;
import net.minecraft.src.WorldClient;

public class SpoutClient implements Client {
	private static SpoutClient instance = null;
	private static Thread dataMiningThread = new DataMiningThread();
	private static final SpoutVersion clientVersion = new SpoutVersion(1, 0, 5, 0);
	private SpoutVersion server = new SpoutVersion();
	private SimpleItemManager itemManager = new SimpleItemManager();
	private SimpleSkyManager skyManager = new SimpleSkyManager();
	private ChatManager chatManager = new ChatManager();
	private PacketManager packetManager = new PacketManager();
	private EntityManager entityManager = new SimpleEntityManager();
	private BiomeManager biomeManager = new SimpleBiomeManager();
	private long tick = 0;
	private Thread clipboardThread = null;
	private ClientPlayer player = null;
	private boolean cheating = true;
	private static SpoutPacket reloadPacket = null;
	
	static {
		dataMiningThread.start();
		Packet.addIdClassMapping(195, true, true, CustomPacket.class);
		ConfigReader.read();
	}
	
	public static SpoutClient getInstance() {
		if (instance == null) {
			instance = new SpoutClient();
		}
		return instance;
	}
	
	public static SpoutVersion getClientVersion() {
		return clientVersion;
	}
	
	public SpoutVersion getServerVersion() {
		return server;
	}
	
	public ItemManager getItemManager() {
		return itemManager;
	}
	
	public SkyManager getSkyManager() {
		return skyManager;
	}
	
	public ChatManager getChatManager() {
		return chatManager;
	}
	
	public PacketManager getPacketManager() {
		return packetManager;
	}
	
	public ActivePlayer getActivePlayer() {
		return player;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public BiomeManager getBiomeManager() {
		return biomeManager;
	}
	
	public boolean isCheatMode() {
		return cheating || !getHandle().isMultiplayerWorld() || getHandle().theWorld == null;
	}
	
	public void setCheatMode(boolean cheat) {
		cheating = cheat;
	}
	
	public boolean isSpoutEnabled() {
		return server.getVersion() >= 100;
	}
	
	public void setSpoutVersion(SpoutVersion version) {
		server = version;
	}

	public void onTick() {
		tick++;
		FileDownloadThread.getInstance().onTick();
		player.getMainScreen().onTick();
		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.theWorld.doColorfulStuff();
		}
	}

	public long getTick() {
		return tick;
	}

	public void onWorldExit() {
		FileUtil.deleteTempDirectory();
		CustomTextureManager.resetTextures();
		instance = null;
		if (clipboardThread != null) {
			clipboardThread.interrupt();
			clipboardThread = null;
		}
		Minecraft.theMinecraft.sndManager.stopMusic();
	}
	
	public void onWorldEnter() {
		if (player == null) {
			player = new ClientPlayer(getHandle().thePlayer);
		}
		if (player.getHandle() instanceof EntityClientPlayerMP && isSpoutEnabled() && ConfigReader.isHasClipboardAccess()) {
			clipboardThread = new ClipboardThread((EntityClientPlayerMP)player.getHandle());
			clipboardThread.start();
		}
		else if (clipboardThread != null){
			clipboardThread.interrupt();
			clipboardThread = null;
		}
	}
	
	public static Minecraft getHandle() {
		return Minecraft.theMinecraft;
	}

	@Override
	public EntityPlayer getPlayerFromId(int id) {
		if (getHandle().thePlayer.entityId == id) {
			return getHandle().thePlayer;
		}
		WorldClient world = (WorldClient)getHandle().theWorld;
		Entity e = world.func_709_b(id);
		if (e instanceof EntityPlayer) {
			return (EntityPlayer) e;
		}
		return null;
	}
	
	public Entity getEntityFromId(int id) {
		if (getHandle().thePlayer.entityId == id) {
			return getHandle().thePlayer;
		}
		WorldClient world = (WorldClient)getHandle().theWorld;
		return world.func_709_b(id);
	}
	
	public static void setReloadPacket(SpoutPacket packet) {
		reloadPacket = packet;
		if (packet != null) {
			instance = null; //dump all saved data
			getInstance().player = new ClientPlayer(getHandle().thePlayer);
		}
	}
	
	public static SpoutPacket getReloadPacket() {
		return reloadPacket;
	}



}
