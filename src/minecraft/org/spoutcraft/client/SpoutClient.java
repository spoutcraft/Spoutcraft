/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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
package org.spoutcraft.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet;
import net.minecraft.src.WorldClient;

import org.newdawn.slick.util.Log;
import org.spoutcraft.client.addon.SimpleAddonStore;
import org.spoutcraft.client.block.SpoutcraftChunk;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.client.config.MipMapUtils;
import org.spoutcraft.client.controls.SimpleKeyBindingManager;
import org.spoutcraft.client.entity.CraftCameraEntity;
import org.spoutcraft.client.entity.CraftEntity;
import org.spoutcraft.client.gui.MCRenderDelegate;
import org.spoutcraft.client.gui.SimpleKeyManager;
import org.spoutcraft.client.gui.SimpleWidgetManager;
import org.spoutcraft.client.gui.server.ServerManager;
import org.spoutcraft.client.gui.texturepacks.TexturePacksDatabaseModel;
import org.spoutcraft.client.gui.texturepacks.TexturePacksModel;
import org.spoutcraft.client.inventory.SimpleMaterialManager;
import org.spoutcraft.client.io.CRCManager;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.io.FileDownloadThread;
import org.spoutcraft.client.io.FileUtil;
import org.spoutcraft.client.packet.CustomPacket;
import org.spoutcraft.client.packet.PacketAddonData;
import org.spoutcraft.client.packet.PacketEntityInformation;
import org.spoutcraft.client.packet.PacketManager;
import org.spoutcraft.client.player.ChatManager;
import org.spoutcraft.client.player.ClientPlayer;
import org.spoutcraft.client.player.SimpleBiomeManager;
import org.spoutcraft.client.player.SimpleSkyManager;
import org.spoutcraft.spoutcraftapi.Client;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.addon.AddonLoadOrder;
import org.spoutcraft.spoutcraftapi.addon.AddonManager;
import org.spoutcraft.spoutcraftapi.addon.ServerAddon;
import org.spoutcraft.spoutcraftapi.addon.SimpleAddonManager;
import org.spoutcraft.spoutcraftapi.addon.SimpleSecurityManager;
import org.spoutcraft.spoutcraftapi.addon.java.JavaAddonLoader;
import org.spoutcraft.spoutcraftapi.command.AddonCommand;
import org.spoutcraft.spoutcraftapi.command.Command;
import org.spoutcraft.spoutcraftapi.command.CommandSender;
import org.spoutcraft.spoutcraftapi.command.SimpleCommandMap;
import org.spoutcraft.spoutcraftapi.entity.ActivePlayer;
import org.spoutcraft.spoutcraftapi.entity.CameraEntity;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.gui.Keyboard;
import org.spoutcraft.spoutcraftapi.gui.RenderDelegate;
import org.spoutcraft.spoutcraftapi.gui.WidgetManager;
import org.spoutcraft.spoutcraftapi.inventory.MaterialManager;
import org.spoutcraft.spoutcraftapi.io.AddonPacket;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBindingManager;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.player.BiomeManager;
import org.spoutcraft.spoutcraftapi.player.SkyManager;
import org.spoutcraft.spoutcraftapi.property.PropertyObject;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;
import org.spoutcraft.spoutcraftapi.util.Location;

public class SpoutClient extends PropertyObject implements Client {
	private static SpoutClient instance = null;
	private static final Thread dataMiningThread = new DataMiningThread();
	private static final long version = 0L;

	private final SimpleSkyManager skyManager = new SimpleSkyManager();
	private final ChatManager chatManager = new ChatManager();
	private final PacketManager packetManager = new PacketManager();
	private final BiomeManager biomeManager = new SimpleBiomeManager();
	private final MaterialManager materialManager = new SimpleMaterialManager();
	private final RenderDelegate render = new MCRenderDelegate();
	private final KeyBindingManager bindingManager = new SimpleKeyBindingManager();
	private final SimpleCommandMap commandMap = new SimpleCommandMap(this);
	private final Logger log = new SpoutcraftLogger();
	private final SimpleAddonManager addonManager;
	private final SimpleSecurityManager securityManager;
	private final ServerManager serverManager = new ServerManager();
	private final double securityKey;
	private long tick = 0;
	private long inWorldTicks = 0;
	private Thread clipboardThread = null;
	private long server = -1L;
	public ClientPlayer player = null;
	private boolean sky = false;
	private boolean clearwater = false;
	private boolean stars = false;
	private boolean weather = false;
	private boolean time = false;
	private boolean coords = false;
	private boolean entitylabel = false;
	private boolean voidfog = false;
	private Mode clientMode = Mode.Menu;
	private TexturePacksModel textureModel = new TexturePacksModel();
	private TexturePacksDatabaseModel textureDatabaseModel = new TexturePacksDatabaseModel();
	private String addonFolder = Minecraft.getMinecraftDir() + File.separator + "addons";
	private final ThreadGroup securityThreadGroup;
	private final SimpleAddonStore addonStore = new SimpleAddonStore();
	private final WidgetManager widgetManager = new SimpleWidgetManager();
	private final HashMap<String, Boolean> permissions = new HashMap<String, Boolean>();

	private SpoutClient() {
		instance = this;
		securityKey = (new Random()).nextDouble();
		securityThreadGroup = new ThreadGroup("Sandboxed Threads");
		securityManager = new SimpleSecurityManager(securityKey, securityThreadGroup, Thread.currentThread());
		if (!Thread.currentThread().getName().equals("Minecraft main thread")) {
			throw new SecurityException("Main thread name mismatch");
		}
		addonManager = new SimpleAddonManager(this, commandMap, securityManager, securityKey);
		//System.setSecurityManager(securityManager);

		((SimpleKeyBindingManager)bindingManager).load();
		addonStore.load();
		serverManager.init();
		chatManager.load();
		Log.setVerbose(false);
	}

	static {
		dataMiningThread.start();
		Packet.addIdClassMapping(195, true, true, CustomPacket.class);
		ConfigReader.read();
		Keyboard.setKeyManager(new SimpleKeyManager());
		CraftEntity.registerTypes();
		FileUtil.migrateOldFiles();
		new File(Minecraft.getMinecraftDir(), "shaders").mkdir();
	}

	public static SpoutClient getInstance() {
		if (instance == null) {
			new SpoutClient();
			Spoutcraft.setClient(instance);

			//must be done after construtor
			ServerAddon addon = new ServerAddon("Spoutcraft", Long.toString(version), null);
			instance.addonManager.addFakeAddon(addon);
		}
		return instance;
	}

	public static long getClientVersion() {
		return version;
	}

	public static boolean enableSandbox() {
		return getInstance().securityManager.lock(getInstance().securityKey);
	}

	public static boolean enableSandbox(boolean enable) {
		return getInstance().securityManager.lock(enable, getInstance().securityKey);
	}

	public static boolean disableSandbox() {
		return getInstance().securityManager.unlock(getInstance().securityKey);
	}

	public static boolean isSandboxed() {
		return getInstance().securityManager.isLocked();
	}

	public long getServerVersion() {
		return server;
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

	public BiomeManager getBiomeManager() {
		return biomeManager;
	}

	public MaterialManager getMaterialManager() {
		return materialManager;
	}

	public World getWorld() {
		if (getHandle() == null || getHandle().theWorld == null) {
			return null;
		}
		return getHandle().theWorld.world;
	}

	public boolean isSkyCheat() {
		return sky || !getHandle().isMultiplayerWorld() || !isSpoutEnabled();
	}

	public boolean isClearWaterCheat() {
		return clearwater || !getHandle().isMultiplayerWorld() || !isSpoutEnabled();
	}

	public boolean isStarsCheat() {
		return stars || !getHandle().isMultiplayerWorld() || !isSpoutEnabled();
	}

	public boolean isWeatherCheat() {
		return weather || !getHandle().isMultiplayerWorld() || !isSpoutEnabled();
	}

	public boolean isTimeCheat() {
		return time || !getHandle().isMultiplayerWorld() || !isSpoutEnabled();
	}

	public boolean isCoordsCheat() {
		return coords || !getHandle().isMultiplayerWorld() || !isSpoutEnabled();
	}

	public boolean isEntityLabelCheat() {
		return entitylabel || !getHandle().isMultiplayerWorld() || !isSpoutEnabled();
	}

	public boolean isVoidFogCheat() {
		return voidfog || !getHandle().isMultiplayerWorld() || !isSpoutEnabled();
	}

	public void setVisualCheats(boolean tsky, boolean tclearwater, boolean tstars, boolean tweather, boolean ttime, boolean tcoords, boolean tentitylabel, boolean tvoidfog) {
		this.sky = tsky;
		this.clearwater = tclearwater;
		this.stars = tstars;
		this.weather = tweather;
		this.time = ttime;
		this.coords = tcoords;
		this.entitylabel = tentitylabel;
		this.voidfog = tvoidfog;

		//if (!isSkyCheat()) {
		//	ConfigReader.sky = true;
		//}
		if (!isClearWaterCheat()) {
			ConfigReader.clearWater = false;
		}
		//if (!isStarsCheat()) {
		//	ConfigReader.stars = true;
		//}
		if (!isWeatherCheat()) {
			ConfigReader.weather = true;
		}
		if (!isTimeCheat()) {
			ConfigReader.time = 0;
		}
		if (!isVoidFogCheat()) {
			ConfigReader.voidFog = true;
		}
	}

	public boolean isSpoutEnabled() {
		return server >= 0;
	}

	public void setSpoutVersion(long version) {
		server = version;
	}

	public void onTick() {
		tick++;
		FileDownloadThread.getInstance().onTick();
		PacketDecompressionThread.onTick();

		enableSandbox();
		player.getMainScreen().onTick();
		disableSandbox();

		MipMapUtils.onTick();

		if (Minecraft.theMinecraft.theWorld != null) {
			Minecraft.theMinecraft.theWorld.doColorfulStuff();
			inWorldTicks++;
		}
		if (isSpoutEnabled()) {
			LinkedList<org.spoutcraft.spoutcraftapi.entity.Entity> processed = new LinkedList<org.spoutcraft.spoutcraftapi.entity.Entity>();
			Iterator<Entity> i = Entity.toProcess.iterator();
			while (i.hasNext()) {
				Entity next = i.next();
				if (next.spoutEntity != null) {
					processed.add(next.spoutEntity);
				}
			}
			Entity.toProcess.clear();
			if (processed.size() > 0) {
				getPacketManager().sendSpoutPacket(new PacketEntityInformation(processed));
			}
		}
	}

	public long getTick() {
		return tick;
	}

	public long getInWorldTicks() {
		return inWorldTicks;
	}

	public void onWorldExit() {
		disableSandbox();
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
		MaterialData.reset();
		FileDownloadThread.preCacheCompleted.lazySet(0);
		server = -1L;
		inWorldTicks = 0L;
		MaterialData.reset();
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
		} else if (clipboardThread != null) {
			clipboardThread.interrupt();
			clipboardThread = null;
		}
		SpoutcraftChunk.loadedChunks.clear();
		PacketDecompressionThread.startThread();
		MipMapUtils.initializeMipMaps();
		player.getMainScreen().toggleSurvivalHUD(!Minecraft.theMinecraft.playerController.isInCreativeMode());
		inWorldTicks = 0L;
	}

	public static Minecraft getHandle() {
		return Minecraft.theMinecraft;
	}

	public EntityPlayer getPlayerFromId(int id) {
		if (getHandle().thePlayer.entityId == id) {
			return getHandle().thePlayer;
		}
		WorldClient world = (WorldClient)getHandle().theWorld;
		Entity e = world.getEntityByID(id);
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
		return world.getEntityByID(id);
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
		return "Spoutcraft_" + version;
	}

	public RenderDelegate getRenderDelegate() {
		return render;
	}

	public File getUpdateFolder() {
		return new File(Minecraft.getMinecraftDir(), "addons" + File.separator + "updates");
	}

	public long getVersion() {
		return version;
	}

	public CameraEntity getCamera() {
		if(!isCameraDetached())
			return null;
		
		return (CameraEntity)getHandle().renderViewEntity.spoutEntity;
	}

	public void setCamera(FixedLocation pos) {
		EntityLiving cam = SpoutClient.getHandle().renderViewEntity;
		if(!(cam.spoutEntity instanceof CameraEntity))
			return;
		
		((CameraEntity)cam.spoutEntity).teleport(pos);
	}

	public void detachCamera(boolean detach) {
		if(detach) {
			if(getHandle().renderViewEntity.spoutEntity instanceof CameraEntity) {
				setCamera(getActivePlayer().getLocation());
				return;
			}
			getHandle().renderViewEntity = (new CraftCameraEntity(getActivePlayer().getLocation())).getHandle();
		}
		else {
			if(getHandle().renderViewEntity.spoutEntity instanceof CameraEntity) {
				getHandle().renderViewEntity.spoutEntity.remove();
				getHandle().renderViewEntity = getHandle().thePlayer;
			}
		}
	}

	public boolean isCameraDetached() {
		return getHandle().renderViewEntity.spoutEntity instanceof CameraEntity;
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

		File addonDir = new File(addonFolder);
		if (addonDir.exists()) {
			Addon[] addons = addonManager.loadAddons(addonDir);
			for (Addon addon : addons) {
				try {
					addon.onLoad();
				} catch (Throwable ex) {
					Logger.getLogger(SpoutClient.class.getName()).log(Level.SEVERE, ex.getMessage() + " initializing " + addon.getDescription().getFullName() + " (Is it up to date?)", ex);
				}
			}
		} else {
			addonDir.mkdir();
		}
	}

	public KeyBindingManager getKeyBindingManager() {
		return bindingManager;
	}

	public File getAddonFolder() {
		return new File(addonFolder);
	}

	public File getAudioCache() {
		return getTemporaryCache();
	}

	public File getTemporaryCache() {
		return FileUtil.getTempDirectory();
	}

	public File getTextureCache() {
		return getTemporaryCache();
	}

	public File getTexturePackFolder() {
		return FileUtil.getTexturePackDirectory();
	}

	public File getSelectedTexturePackZip() {
		return FileUtil.getSelectedTexturePackZip();
	}

	public File getStatsFolder() {
		return FileUtil.getStatsDirectory();
	}

	public ServerManager getServerManager() {
		return instance.serverManager;
	}

	public void send(AddonPacket packet) {
		getPacketManager().sendSpoutPacket(new PacketAddonData(packet));
	}

	public TexturePacksModel getTexturePacksModel() {
		return textureModel;
	}

	public TexturePacksDatabaseModel getTexturePacksDatabaseModel() {
		return textureDatabaseModel;
	}

	public Player[] getPlayers() {
		if (getWorld() == null) {
			return new Player[0];
		}
		List<Player> playerList = getWorld().getPlayers();
		Player[] players = new Player[playerList.size()];
		for (int i = 0; i < playerList.size(); i++) {
			players[i] = playerList.get(i);
		}
		return players;
	}

	public Player getPlayer(String name) {
		Player[] players = getPlayers();

		Player found = null;
		String lowerName = name.toLowerCase();
		int delta = Integer.MAX_VALUE;
		for (Player player : players) {
			if (player.getName().toLowerCase().startsWith(lowerName)) {
				int curDelta = player.getName().length() - lowerName.length();
				if (curDelta < delta) {
					found = player;
					delta = curDelta;
				}
				if (curDelta == 0) {
					break;
				}
			}
		}
		return found;
	}

	public Player getPlayerExact(String name) {
		String lname = name.toLowerCase();

		for (Player player : getPlayers()) {
			if (player.getName().equalsIgnoreCase(lname)) {
				return player;
			}
		}
		return null;
	}

	public List<Player> matchPlayer(String partialName) {
		List<Player> matchedPlayers = new ArrayList<Player>();

		for (Player iterPlayer : this.getPlayers()) {
			String iterPlayerName = iterPlayer.getName();

			if (partialName.equalsIgnoreCase(iterPlayerName)) {
				// Exact match
				matchedPlayers.clear();
				matchedPlayers.add(iterPlayer);
				break;
			}
			if (iterPlayerName.toLowerCase().indexOf(partialName.toLowerCase()) != -1) {
				// Partial match
				matchedPlayers.add(iterPlayer);
			}
		}

		return matchedPlayers;
	}

	public SimpleAddonStore getAddonStore() {
		return addonStore;
	}

	public WidgetManager getWidgetManager() {
		return widgetManager;
	}

	@Override
	public boolean hasPermission(String node) {
		Boolean allow = permissions.get(node);
		if(allow != null) {
			return allow;
		} else {
			return true;
		}
	}
	
	public void setPermission(String node, boolean allow) {
		permissions.put(node, allow);
	}
	
	public void clearPermissions() {
		permissions.clear();
	}
}
