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

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet;
import net.minecraft.src.WorldClient;

import org.getspout.spout.ClipboardThread;
import org.getspout.spout.DataMiningThread;
import org.getspout.spout.PacketDecompressionThread;
import org.getspout.spout.block.SpoutcraftChunk;
import org.getspout.spout.config.ConfigReader;
import org.getspout.spout.controls.SimpleKeyBindingManager;
import org.getspout.spout.entity.CraftEntity;
import org.getspout.spout.entity.EntityManager;
import org.getspout.spout.entity.SimpleEntityManager;
import org.getspout.spout.gui.MCRenderDelegate;
import org.getspout.spout.gui.SimpleKeyManager;
import org.getspout.spout.inventory.SimpleItemManager;
import org.getspout.spout.io.CRCManager;
import org.getspout.spout.io.CustomTextureManager;
import org.getspout.spout.io.FileDownloadThread;
import org.getspout.spout.io.FileUtil;
import org.getspout.spout.item.SpoutItem;
import org.getspout.spout.packet.CustomPacket;
import org.getspout.spout.packet.PacketManager;
import org.getspout.spout.player.ChatManager;
import org.getspout.spout.player.ClientPlayer;
import org.getspout.spout.player.SimpleBiomeManager;
import org.getspout.spout.player.SimpleSkyManager;
import org.spoutcraft.spoutcraftapi.AnimatableLocation;
import org.spoutcraft.spoutcraftapi.Client;
import org.spoutcraft.spoutcraftapi.SpoutVersion;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.addon.AddonLoadOrder;
import org.spoutcraft.spoutcraftapi.addon.AddonManager;
import org.spoutcraft.spoutcraftapi.addon.SimpleAddonManager;
import org.spoutcraft.spoutcraftapi.addon.java.JavaAddonLoader;
import org.spoutcraft.spoutcraftapi.command.AddonCommand;
import org.spoutcraft.spoutcraftapi.command.Command;
import org.spoutcraft.spoutcraftapi.command.CommandSender;
import org.spoutcraft.spoutcraftapi.command.SimpleCommandMap;
import org.spoutcraft.spoutcraftapi.entity.ActivePlayer;
import org.spoutcraft.spoutcraftapi.gui.Keyboard;
import org.spoutcraft.spoutcraftapi.gui.RenderDelegate;
import org.spoutcraft.spoutcraftapi.inventory.ItemManager;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBindingManager;
import org.spoutcraft.spoutcraftapi.player.BiomeManager;
import org.spoutcraft.spoutcraftapi.player.SkyManager;
import org.spoutcraft.spoutcraftapi.property.PropertyObject;
import org.spoutcraft.spoutcraftapi.util.Location;

public class SpoutClient extends PropertyObject implements Client {
	private static SpoutClient instance = null;
	private static Thread dataMiningThread = new DataMiningThread();
	private static final SpoutVersion clientVersion = new SpoutVersion(1, 0, 6, 0);
	private SpoutVersion server = new SpoutVersion();
	private SimpleItemManager itemManager = new SimpleItemManager();
	private SimpleSkyManager skyManager = new SimpleSkyManager();
	private ChatManager chatManager = new ChatManager();
	private PacketManager packetManager = new PacketManager();
	private EntityManager entityManager = new SimpleEntityManager();
	private BiomeManager biomeManager = new SimpleBiomeManager();
	private long tick = 0;
	private Thread clipboardThread = null;
	public ClientPlayer player = null;
	private boolean cheating = true;
	private boolean sky = true;
	private boolean clearwater = true;
	private boolean cloudheight = true;
	private boolean stars = true;
	private boolean weather = true;
	private boolean time = true;
	private RenderDelegate render = new MCRenderDelegate();
	private KeyBindingManager bindingManager = new SimpleKeyBindingManager();
	private SimpleCommandMap commandMap = new SimpleCommandMap(this);
	private SimpleAddonManager addonManager = new SimpleAddonManager(this, commandMap);
	private Logger log = Logger.getLogger(SpoutClient.class.getName());
	private Mode clientMode = Mode.Menu;
	
	static {
		dataMiningThread.start();
		Packet.addIdClassMapping(195, true, true, CustomPacket.class);
		ConfigReader.read();
		Keyboard.setKeyManager(new SimpleKeyManager());
		CraftEntity.registerTypes();
	}
	
	public static SpoutClient getInstance() {
		if (instance == null) {
			instance = new SpoutClient();
			Spoutcraft.setClient(instance);
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

	public void setVisualCheats(boolean sky, boolean clearwater, boolean cloudheight, boolean stars, boolean weather, boolean time) {
		this.sky = sky;
		this.clearwater = clearwater;
		this.cloudheight = cloudheight;
		this.stars = stars;
		this.weather = weather;
		this.time = time;
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
		PacketDecompressionThread.onTick();
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
		CRCManager.clear();
		SpoutcraftChunk.loadedChunks.clear();
		if (clipboardThread != null) {
			clipboardThread.interrupt();
			clipboardThread = null;
		}
		ClientPlayer.getInstance().resetMainScreen();
		Minecraft.theMinecraft.sndManager.stopMusic();
		PacketDecompressionThread.endThread();
		SpoutItem.wipeMap();
		itemManager.reset();
	}
	
	public void onWorldEnter() {
		if (player == null) {
			player = ClientPlayer.getInstance();
			player.setPlayer(getHandle().thePlayer);
			getHandle().thePlayer.spoutEntity = player;
		}
		if (player.getHandle() instanceof EntityClientPlayerMP && isSpoutEnabled() && ConfigReader.isHasClipboardAccess()) {
			clipboardThread = new ClipboardThread((EntityClientPlayerMP)player.getHandle());
			clipboardThread.start();
		}
		else if (clipboardThread != null){
			clipboardThread.interrupt();
			clipboardThread = null;
		}
		SpoutcraftChunk.loadedChunks.clear();
		PacketDecompressionThread.startThread();
		
		player.getMainScreen().toggleSurvivalHUD(!Minecraft.theMinecraft.playerController.isInCreativeMode());
	}
	
	public static Minecraft getHandle() {
		return Minecraft.theMinecraft;
	}

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
	
	public boolean dispatchCommand(CommandSender sender, String commandLine) {
		if (commandMap.dispatch(sender, commandLine)) {
            return true;
        }
        sender.sendMessage("Unknown command. Type \"help\" for help.");

        return false;
	}

	public AddonCommand getAddonCommand(String name) {
		Command command = commandMap.getCommand(name);

        if (command instanceof AddonCommand) {
            return (AddonCommand) command;
        } else {
            return null;
        }
	}

	public AddonManager getAddonManager() {
		return addonManager;
	}

	public Logger getLogger() {
		return log;
	}

	public Mode getMode() {
		return clientMode;
	}
	
	public void setMode(Mode clientMode) {
		this.clientMode = clientMode;
	}

	public String getName() {
		return "Spoutcraft_" + clientVersion.toString();
	}

	public RenderDelegate getRenderDelegate() {
		return render;
	}

	public File getUpdateFolder() {
		return new File(Minecraft.getMinecraftDir(), "addons" + File.separator + "updates");
	}

	public SpoutVersion getVersion() {
		return clientVersion;
	}

	public Location getCamera() {
		Location ret = null;
		EntityLiving cam = SpoutClient.getHandle().renderViewEntity;
		ret = new AnimatableLocation(null, cam.posX, cam.posY, cam.posZ);
		ret.setPitch(cam.rotationPitch);
		ret.setYaw(cam.rotationYaw);
		return ret;
	}

	public void setCamera(Location pos) {
		EntityLiving cam = SpoutClient.getHandle().renderViewEntity;
		cam.posX = pos.getX();
		cam.posY = pos.getY();
		cam.posZ = pos.getZ();
		cam.rotationPitch = (float) pos.getPitch();
		cam.rotationYaw = (float) pos.getYaw();
	}

	public void detachCamera(boolean detach) {
		// TODO Auto-generated method stub
		
	}

	public boolean isCameraDetached() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void enableAddons(AddonLoadOrder load) {
        Addon[] addons = addonManager.getAddons();

        for (Addon addon : addons) {
            if (!addon.isEnabled() && addon.getDescription().getLoad() == load) {
                loadAddon(addon);
            }
        }
    }
	
	private void loadAddon(Addon addon) {
        try {
            addonManager.enableAddon(addon);
        } catch (Throwable ex) {
            Logger.getLogger(SpoutClient.class.getName()).log(Level.SEVERE, ex.getMessage() + " loading " + addon.getDescription().getFullName() + " (Is it up to date?)", ex);
        }
    }


    public void disableAddons() {
        addonManager.disableAddons();
    }
	
	public void loadAddons() {
        addonManager.registerInterface(JavaAddonLoader.class);

        File addonFolder = new File(Minecraft.getMinecraftDir(), "addons");
        if (addonFolder.exists()) {
            Addon[] addons = addonManager.loadAddons(addonFolder);
            for (Addon addon : addons) {
                try {
                    addon.onLoad();
                } catch (Throwable ex) {
                    Logger.getLogger(SpoutClient.class.getName()).log(Level.SEVERE, ex.getMessage() + " initializing " + addon.getDescription().getFullName() + " (Is it up to date?)", ex);
                }
            }
        } else {
            addonFolder.mkdir();
        }
    }
	
	public KeyBindingManager getKeyBindingManager() {
		return bindingManager;
	}
}
