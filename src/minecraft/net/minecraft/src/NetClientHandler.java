package net.minecraft.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
//Spout start
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.FileDownloadThread;
//SPout end
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.Container;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityCrit2FX;
import net.minecraft.src.EntityEgg;
import net.minecraft.src.EntityEnderCrystal;
import net.minecraft.src.EntityEnderEye;
import net.minecraft.src.EntityEnderPearl;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityFireball;
import net.minecraft.src.EntityFishHook;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLightningBolt;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityOtherPlayerMP;
import net.minecraft.src.EntityPainting;
import net.minecraft.src.EntityPickupFX;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EntityPotion;
import net.minecraft.src.EntitySmallFireball;
import net.minecraft.src.EntitySnowball;
import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.Explosion;
import net.minecraft.src.GuiDisconnected;
import net.minecraft.src.GuiDownloadTerrain;
import net.minecraft.src.GuiSavingLevelString;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiWinGame;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.InventoryBasic;
import net.minecraft.src.Item;
import net.minecraft.src.ItemMap;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MapStorage;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet0KeepAlive;
import net.minecraft.src.Packet100OpenWindow;
import net.minecraft.src.Packet101CloseWindow;
import net.minecraft.src.Packet103SetSlot;
import net.minecraft.src.Packet104WindowItems;
import net.minecraft.src.Packet105UpdateProgressbar;
import net.minecraft.src.Packet106Transaction;
import net.minecraft.src.Packet10Flying;
import net.minecraft.src.Packet130UpdateSign;
import net.minecraft.src.Packet131MapData;
import net.minecraft.src.Packet17Sleep;
import net.minecraft.src.Packet18Animation;
import net.minecraft.src.Packet1Login;
import net.minecraft.src.Packet200Statistic;
import net.minecraft.src.Packet201PlayerInfo;
import net.minecraft.src.Packet20NamedEntitySpawn;
import net.minecraft.src.Packet21PickupSpawn;
import net.minecraft.src.Packet22Collect;
import net.minecraft.src.Packet23VehicleSpawn;
import net.minecraft.src.Packet24MobSpawn;
import net.minecraft.src.Packet255KickDisconnect;
import net.minecraft.src.Packet25EntityPainting;
import net.minecraft.src.Packet26EntityExpOrb;
import net.minecraft.src.Packet28EntityVelocity;
import net.minecraft.src.Packet29DestroyEntity;
import net.minecraft.src.Packet2Handshake;
import net.minecraft.src.Packet30Entity;
import net.minecraft.src.Packet34EntityTeleport;
import net.minecraft.src.Packet38EntityStatus;
import net.minecraft.src.Packet39AttachEntity;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.Packet40EntityMetadata;
import net.minecraft.src.Packet41EntityEffect;
import net.minecraft.src.Packet42RemoveEntityEffect;
import net.minecraft.src.Packet43Experience;
import net.minecraft.src.Packet4UpdateTime;
import net.minecraft.src.Packet50PreChunk;
import net.minecraft.src.Packet51MapChunk;
import net.minecraft.src.Packet52MultiBlockChange;
import net.minecraft.src.Packet53BlockChange;
import net.minecraft.src.Packet54PlayNoteBlock;
import net.minecraft.src.Packet5PlayerInventory;
import net.minecraft.src.Packet60Explosion;
import net.minecraft.src.Packet61DoorChange;
import net.minecraft.src.Packet6SpawnPosition;
import net.minecraft.src.Packet70Bed;
import net.minecraft.src.Packet71Weather;
import net.minecraft.src.Packet8UpdateHealth;
import net.minecraft.src.Packet9Respawn;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.StatList;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityBrewingStand;
import net.minecraft.src.TileEntityDispenser;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.TileEntitySign;
import net.minecraft.src.World;
import net.minecraft.src.WorldClient;
import net.minecraft.src.WorldSettings;

public class NetClientHandler extends NetHandler {

	private boolean disconnected = false;
	private NetworkManager netManager;
	public String field_1209_a;
	private Minecraft mc;
	private WorldClient worldClient;
	private boolean field_1210_g = false;
	public MapStorage mapStorage = new MapStorage((ISaveHandler)null);
	private Map playerInfoMap = new HashMap();
	public List playerNames = new ArrayList();
	public int currentServerMaxPlayers = 20;
	Random rand = new Random();
	//Spout start
	long timeout = System.currentTimeMillis() + 5000;
	
	public NetClientHandler(Minecraft var1, String var2, int var3) throws UnknownHostException, IOException {
		this.mc = var1;
		Socket var4 = new Socket(InetAddress.getByName(var2), var3);
		this.netManager = new NetworkManager(var4, "Client", this);
		//Spout start
		org.spoutcraft.client.gui.error.GuiConnectionLost.lastServerIp = var2;
		org.spoutcraft.client.gui.error.GuiConnectionLost.lastServerPort = var3;
		//Spout end
	}

	public void processReadPackets() {
		if(!this.disconnected) {
			this.netManager.processReadPackets();
		}

		this.netManager.wakeThreads();
		
		//Spout start
		if (mc.currentScreen instanceof GuiDownloadTerrain) {
			if (System.currentTimeMillis() > timeout) {
				mc.displayGuiScreen(null, false);
			}
		}
	}

	public void handleLogin(Packet1Login var1) {
		this.mc.playerController = new PlayerControllerMP(this.mc, this);
		this.mc.statFileWriter.readStat(StatList.joinMultiplayerStat, 1);
		this.worldClient = new WorldClient(this, new WorldSettings(var1.mapSeed, var1.serverMode, false, false, var1.field_46032_d), var1.worldType, var1.difficultySetting);
		this.worldClient.multiplayerWorld = true;
		//Spout start
		int height = 128;
		if (var1.worldHeight > 0 && var1.worldHeight < 12) {
			height = 2 << var1.worldHeight;
		}
		else if (var1.worldHeight >= 32) {
			height = var1.worldHeight;
		}
		worldClient.setMapHeight(height);
		//Spout end
		this.mc.changeWorld1(this.worldClient);
		this.mc.thePlayer.dimension = var1.worldType;
		this.mc.displayGuiScreen(new GuiDownloadTerrain(this));
		this.mc.thePlayer.entityId = var1.protocolVersion;
		this.currentServerMaxPlayers = var1.maxPlayers;
		((PlayerControllerMP)this.mc.playerController).setCreative(var1.serverMode == 1);
	}

	public void handlePickupSpawn(Packet21PickupSpawn var1) {
		double var2 = (double)var1.xPosition / 32.0D;
		double var4 = (double)var1.yPosition / 32.0D;
		double var6 = (double)var1.zPosition / 32.0D;
		EntityItem var8 = new EntityItem(this.worldClient, var2, var4, var6, new ItemStack(var1.itemID, var1.count, var1.itemDamage));
		var8.motionX = (double)var1.rotation / 128.0D;
		var8.motionY = (double)var1.pitch / 128.0D;
		var8.motionZ = (double)var1.roll / 128.0D;
		var8.serverPosX = var1.xPosition;
		var8.serverPosY = var1.yPosition;
		var8.serverPosZ = var1.zPosition;
		this.worldClient.addEntityToWorld(var1.entityId, var8);
	}

	public void handleVehicleSpawn(Packet23VehicleSpawn var1) {
		double var2 = (double)var1.xPosition / 32.0D;
		double var4 = (double)var1.yPosition / 32.0D;
		double var6 = (double)var1.zPosition / 32.0D;
		Object var8 = null;
		if(var1.type == 10) {
			var8 = new EntityMinecart(this.worldClient, var2, var4, var6, 0);
		}

		if(var1.type == 11) {
			var8 = new EntityMinecart(this.worldClient, var2, var4, var6, 1);
		}

		if(var1.type == 12) {
			var8 = new EntityMinecart(this.worldClient, var2, var4, var6, 2);
		}

		if(var1.type == 90) {
			var8 = new EntityFishHook(this.worldClient, var2, var4, var6);
		}

		if(var1.type == 60) {
			var8 = new EntityArrow(this.worldClient, var2, var4, var6);
		}

		if(var1.type == 61) {
			var8 = new EntitySnowball(this.worldClient, var2, var4, var6);
		}

		if(var1.type == 65) {
			var8 = new EntityEnderPearl(this.worldClient, var2, var4, var6);
		}

		if(var1.type == 72) {
			var8 = new EntityEnderEye(this.worldClient, var2, var4, var6);
		}

		if(var1.type == 63) {
			var8 = new EntityFireball(this.worldClient, var2, var4, var6, (double)var1.speedX / 8000.0D, (double)var1.speedY / 8000.0D, (double)var1.speedZ / 8000.0D);
			var1.throwerEntityId = 0;
		}

		if(var1.type == 64) {
			var8 = new EntitySmallFireball(this.worldClient, var2, var4, var6, (double)var1.speedX / 8000.0D, (double)var1.speedY / 8000.0D, (double)var1.speedZ / 8000.0D);
			var1.throwerEntityId = 0;
		}

		if(var1.type == 62) {
			var8 = new EntityEgg(this.worldClient, var2, var4, var6);
		}

		if(var1.type == 73) {
			var8 = new EntityPotion(this.worldClient, var2, var4, var6, var1.throwerEntityId);
			var1.throwerEntityId = 0;
		}

		if(var1.type == 1) {
			var8 = new EntityBoat(this.worldClient, var2, var4, var6);
		}

		if(var1.type == 50) {
			var8 = new EntityTNTPrimed(this.worldClient, var2, var4, var6);
		}

		if(var1.type == 51) {
			var8 = new EntityEnderCrystal(this.worldClient, var2, var4, var6);
		}

		if(var1.type == 70) {
			var8 = new EntityFallingSand(this.worldClient, var2, var4, var6, Block.sand.blockID);
		}

		if(var1.type == 71) {
			var8 = new EntityFallingSand(this.worldClient, var2, var4, var6, Block.gravel.blockID);
		}

		if(var1.type == 74) {
			var8 = new EntityFallingSand(this.worldClient, var2, var4, var6, Block.dragonEgg.blockID);
		}

		if(var8 != null) {
			((Entity)var8).serverPosX = var1.xPosition;
			((Entity)var8).serverPosY = var1.yPosition;
			((Entity)var8).serverPosZ = var1.zPosition;
			((Entity)var8).rotationYaw = 0.0F;
			((Entity)var8).rotationPitch = 0.0F;
			Entity[] var9 = ((Entity)var8).getParts();
			if(var9 != null) {
				int var10 = var1.entityId - ((Entity)var8).entityId;

				for(int var11 = 0; var11 < var9.length; ++var11) {
					var9[var11].entityId += var10;
					System.out.println(var9[var11].entityId);
				}
			}

			((Entity)var8).entityId = var1.entityId;
			this.worldClient.addEntityToWorld(var1.entityId, (Entity)var8);
			if(var1.throwerEntityId > 0) {
				if(var1.type == 60) {
					Entity var12 = this.getEntityByID(var1.throwerEntityId);
					if(var12 instanceof EntityLiving) {
						((EntityArrow)var8).shootingEntity = (EntityLiving)var12;
					}
				}

				((Entity)var8).setVelocity((double)var1.speedX / 8000.0D, (double)var1.speedY / 8000.0D, (double)var1.speedZ / 8000.0D);
			}
		}
	}

	public void handleEntityExpOrb(Packet26EntityExpOrb var1) {
		EntityXPOrb var2 = new EntityXPOrb(this.worldClient, (double)var1.posX, (double)var1.posY, (double)var1.posZ, var1.count);
		var2.serverPosX = var1.posX;
		var2.serverPosY = var1.posY;
		var2.serverPosZ = var1.posZ;
		var2.rotationYaw = 0.0F;
		var2.rotationPitch = 0.0F;
		var2.entityId = var1.entityId;
		this.worldClient.addEntityToWorld(var1.entityId, var2);
	}

	public void handleWeather(Packet71Weather var1) {
		double var2 = (double)var1.posX / 32.0D;
		double var4 = (double)var1.posY / 32.0D;
		double var6 = (double)var1.posZ / 32.0D;
		EntityLightningBolt var8 = null;
		if(var1.isLightningBolt == 1) {
			var8 = new EntityLightningBolt(this.worldClient, var2, var4, var6);
		}

		if(var8 != null) {
			var8.serverPosX = var1.posX;
			var8.serverPosY = var1.posY;
			var8.serverPosZ = var1.posZ;
			var8.rotationYaw = 0.0F;
			var8.rotationPitch = 0.0F;
			var8.entityId = var1.entityID;
			this.worldClient.addWeatherEffect(var8);
		}
	}

	public void handleEntityPainting(Packet25EntityPainting var1) {
		EntityPainting var2 = new EntityPainting(this.worldClient, var1.xPosition, var1.yPosition, var1.zPosition, var1.direction, var1.title);
		this.worldClient.addEntityToWorld(var1.entityId, var2);
	}

	public void handleEntityVelocity(Packet28EntityVelocity var1) {
		Entity var2 = this.getEntityByID(var1.entityId);
		if(var2 != null) {
			var2.setVelocity((double)var1.motionX / 8000.0D, (double)var1.motionY / 8000.0D, (double)var1.motionZ / 8000.0D);
		}
	}

	public void handleEntityMetadata(Packet40EntityMetadata var1) {
		Entity var2 = this.getEntityByID(var1.entityId);
		if(var2 != null && var1.getMetadata() != null) {
			var2.getDataWatcher().updateWatchedObjectsFromList(var1.getMetadata());
		}
	}

	public void handleNamedEntitySpawn(Packet20NamedEntitySpawn var1) {
		double var2 = (double)var1.xPosition / 32.0D;
		double var4 = (double)var1.yPosition / 32.0D;
		double var6 = (double)var1.zPosition / 32.0D;
		float var8 = (float)(var1.rotation * 360) / 256.0F;
		float var9 = (float)(var1.pitch * 360) / 256.0F;
		EntityOtherPlayerMP var10 = new EntityOtherPlayerMP(this.mc.theWorld, var1.name);
		var10.prevPosX = var10.lastTickPosX = (double)(var10.serverPosX = var1.xPosition);
		var10.prevPosY = var10.lastTickPosY = (double)(var10.serverPosY = var1.yPosition);
		var10.prevPosZ = var10.lastTickPosZ = (double)(var10.serverPosZ = var1.zPosition);
		int var11 = var1.currentItem;
		if(var11 == 0) {
			var10.inventory.mainInventory[var10.inventory.currentItem] = null;
		}
		else {
			var10.inventory.mainInventory[var10.inventory.currentItem] = new ItemStack(var11, 1, 0);
		}

		var10.setPositionAndRotation(var2, var4, var6, var8, var9);
		this.worldClient.addEntityToWorld(var1.entityId, var10);
	}

	public void handleEntityTeleport(Packet34EntityTeleport var1) {
		Entity var2 = this.getEntityByID(var1.entityId);
		if(var2 != null) {
			var2.serverPosX = var1.xPosition;
			var2.serverPosY = var1.yPosition;
			var2.serverPosZ = var1.zPosition;
			double var3 = (double)var2.serverPosX / 32.0D;
			double var5 = (double)var2.serverPosY / 32.0D + 0.015625D;
			double var7 = (double)var2.serverPosZ / 32.0D;
			float var9 = (float)(var1.yaw * 360) / 256.0F;
			float var10 = (float)(var1.pitch * 360) / 256.0F;
			var2.setPositionAndRotation2(var3, var5, var7, var9, var10, 3);
		}
	}

	public void handleEntity(Packet30Entity var1) {
		Entity var2 = this.getEntityByID(var1.entityId);
		if(var2 != null) {
			var2.serverPosX += var1.xPosition;
			var2.serverPosY += var1.yPosition;
			var2.serverPosZ += var1.zPosition;
			double var3 = (double)var2.serverPosX / 32.0D;
			double var5 = (double)var2.serverPosY / 32.0D;
			double var7 = (double)var2.serverPosZ / 32.0D;
			float var9 = var1.rotating?(float)(var1.yaw * 360) / 256.0F:var2.rotationYaw;
			float var10 = var1.rotating?(float)(var1.pitch * 360) / 256.0F:var2.rotationPitch;
			var2.setPositionAndRotation2(var3, var5, var7, var9, var10, 3);
		}
	}

	public void handleDestroyEntity(Packet29DestroyEntity var1) {
		this.worldClient.removeEntityFromWorld(var1.entityId);
	}

	public void handleFlying(Packet10Flying var1) {
		EntityPlayerSP var2 = this.mc.thePlayer;
		double var3 = var2.posX;
		double var5 = var2.posY;
		double var7 = var2.posZ;
		float var9 = var2.rotationYaw;
		float var10 = var2.rotationPitch;
		if(var1.moving) {
			var3 = var1.xPosition;
			var5 = var1.yPosition;
			var7 = var1.zPosition;
		}

		if(var1.rotating) {
			var9 = var1.yaw;
			var10 = var1.pitch;
		}

		var2.ySize = 0.0F;
		var2.motionX = var2.motionY = var2.motionZ = 0.0D;
		var2.setPositionAndRotation(var3, var5, var7, var9, var10);
		var1.xPosition = var2.posX;
		var1.yPosition = var2.boundingBox.minY;
		var1.zPosition = var2.posZ;
		var1.stance = var2.posY;
		this.netManager.addToSendQueue(var1);
		if(!this.field_1210_g) {
			this.mc.thePlayer.prevPosX = this.mc.thePlayer.posX;
			this.mc.thePlayer.prevPosY = this.mc.thePlayer.posY;
			this.mc.thePlayer.prevPosZ = this.mc.thePlayer.posZ;
			this.field_1210_g = true;
			
			//Spout Start
			if (SpoutClient.getInstance().isSpoutEnabled()) {
				if (FileDownloadThread.preCacheCompleted.get() == 0L) {
					return;
				}
			}
			//Spout End
			this.mc.displayGuiScreen((GuiScreen)null);
		}
	}

	public void handlePreChunk(Packet50PreChunk var1) {
		this.worldClient.doPreChunk(var1.xPosition, var1.yPosition, var1.mode);
	}

	public void handleMultiBlockChange(Packet52MultiBlockChange var1) {
		Chunk var2 = this.worldClient.getChunkFromChunkCoords(var1.xPosition, var1.zPosition);
		int var3 = var1.xPosition * 16;
		int var4 = var1.zPosition * 16;

		for(int var5 = 0; var5 < var1.size; ++var5) {
			short var6 = var1.coordinateArray[var5];
			int var7 = var1.typeArray[var5] & 255;
			byte var8 = var1.metadataArray[var5];
			int var9 = var6 >> 12 & 15;
			int var10 = var6 >> 8 & 15;
			int var11 = var6 & 255;
			var2.setBlockIDWithMetadata(var9, var11, var10, var7, var8);
			this.worldClient.invalidateBlockReceiveRegion(var9 + var3, var11, var10 + var4, var9 + var3, var11, var10 + var4);
			this.worldClient.markBlocksDirty(var9 + var3, var11, var10 + var4, var9 + var3, var11, var10 + var4);
		}
	}

	public void handleMapChunk(Packet51MapChunk var1) {
		this.worldClient.invalidateBlockReceiveRegion(var1.xPosition, var1.yPosition, var1.zPosition, var1.xPosition + var1.xSize - 1, var1.yPosition + var1.ySize - 1, var1.zPosition + var1.zSize - 1);
		this.worldClient.setChunkData(var1.xPosition, var1.yPosition, var1.zPosition, var1.xSize, var1.ySize, var1.zSize, var1.chunk);
	}

	public void handleBlockChange(Packet53BlockChange var1) {
		this.worldClient.setBlockAndMetadataAndInvalidate(var1.xPosition, var1.yPosition, var1.zPosition, var1.type, var1.metadata);
	}

	public void handleKickDisconnect(Packet255KickDisconnect var1) {
		this.netManager.networkShutdown("disconnect.kicked", new Object[0]);
		this.disconnected = true;
		this.mc.changeWorld1((World)null);
		this.mc.displayGuiScreen(new GuiDisconnected("disconnect.disconnected", "disconnect.genericReason", new Object[]{var1.reason}));
	}

	public void handleErrorMessage(String var1, Object[] var2) {
		if(!this.disconnected) {
			this.disconnected = true;
			this.mc.changeWorld1((World)null);
			//Spout start
			System.out.println(var1);
			if (var1 != null && var1.toLowerCase().contains("endofstream")) {
				this.mc.displayGuiScreen(new org.spoutcraft.client.gui.error.GuiConnectionLost());
			}
			else if (var2 == null || var2.length == 0 || !(var2[0] instanceof String)) {
				this.mc.displayGuiScreen(new GuiDisconnected("disconnect.lost", var1, var2));
			}
			else if (((String)var2[0]).toLowerCase().contains("connection reset")) {
				this.mc.displayGuiScreen(new org.spoutcraft.client.gui.error.GuiConnectionLost());
			}
			else if (((String)var2[0]).toLowerCase().contains("connection refused")) {
				this.mc.displayGuiScreen(new org.spoutcraft.client.gui.error.GuiConnectionLost("The server is not currently online!"));
			}
			else if (((String)var2[0]).toLowerCase().contains("overflow")) {
				this.mc.displayGuiScreen(new org.spoutcraft.client.gui.error.GuiConnectionLost("The server is currently experiencing heavy traffic. Try again later."));
			}
			else
			//Spout end
			this.mc.displayGuiScreen(new GuiDisconnected("disconnect.lost", var1, var2));
		}
	}

	public void func_28117_a(Packet var1) {
		if(!this.disconnected) {
			this.netManager.addToSendQueue(var1);
			this.netManager.serverShutdown();
		}
	}

	public void addToSendQueue(Packet var1) {
		if(!this.disconnected) {
			this.netManager.addToSendQueue(var1);
		}
	}

	public void handleCollect(Packet22Collect var1) {
		Entity var2 = this.getEntityByID(var1.collectedEntityId);
		Object var3 = (EntityLiving)this.getEntityByID(var1.collectorEntityId);
		if(var3 == null) {
			var3 = this.mc.thePlayer;
		}

		if(var2 != null) {
			this.worldClient.playSoundAtEntity(var2, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, var2, (Entity)var3, -0.5F));
			this.worldClient.removeEntityFromWorld(var1.collectedEntityId);
		}
	}

	public void handleChat(Packet3Chat var1) {
		this.mc.ingameGUI.addChatMessage(var1.message);
	}

	public void handleArmAnimation(Packet18Animation var1) {
		Entity var2 = this.getEntityByID(var1.entityId);
		if(var2 != null) {
			EntityPlayer var3;
			if(var1.animate == 1) {
				var3 = (EntityPlayer)var2;
				var3.swingItem();
			}
			else if (var1.animate == 2) {
				var2.performHurtAnimation();
			}
			else if (var1.animate == 3) {
				var3 = (EntityPlayer)var2;
				var3.wakeUpPlayer(false, false, false);
			}
			else if (var1.animate == 4) {
				var3 = (EntityPlayer)var2;
				var3.func_6420_o();
			}
			else if (var1.animate == 6) {
				this.mc.effectRenderer.addEffect(new EntityCrit2FX(this.mc.theWorld, var2));
			}
			else if (var1.animate == 7) {
				EntityCrit2FX var4 = new EntityCrit2FX(this.mc.theWorld, var2, "magicCrit");
				this.mc.effectRenderer.addEffect(var4);
			}
			else if (var1.animate == 5 && var2 instanceof EntityOtherPlayerMP) {
				;
			}
		}
	}

	public void handleSleep(Packet17Sleep var1) {
		Entity var2 = this.getEntityByID(var1.entityID);
		if(var2 != null) {
			if(var1.field_22046_e == 0) {
				EntityPlayer var3 = (EntityPlayer)var2;
				var3.sleepInBedAt(var1.bedX, var1.bedY, var1.bedZ);
			}
		}
	}

	public void handleHandshake(Packet2Handshake var1) {
		boolean var2 = true;
		String var3 = var1.username;
		if(var3 != null && var3.trim().length() != 0) {
			if(!var3.equals("-")) {
				try {
					Long.parseLong(var3, 16);
				}
				catch (NumberFormatException var8) {
					var2 = false;
				}
			}
		}
		else {
			var2 = false;
		}

		if(!var2) {
			this.netManager.networkShutdown("disconnect.genericReason", new Object[]{"The server responded with an invalid server key"});
		}
		else if (var1.username.equals("-")) {
			this.addToSendQueue(new Packet1Login(this.mc.session.username, 23));
		}
		else {
			try {
				URL var4 = new URL("http://session.minecraft.net/game/joinserver.jsp?user=" + this.mc.session.username + "&sessionId=" + this.mc.session.sessionId + "&serverId=" + var1.username);
				BufferedReader var5 = new BufferedReader(new InputStreamReader(var4.openStream()));
				String var6 = var5.readLine();
				var5.close();
				if(var6.equalsIgnoreCase("ok")) {
					this.addToSendQueue(new Packet1Login(this.mc.session.username, 23));
				}
				else {
					this.netManager.networkShutdown("disconnect.loginFailedInfo", new Object[]{var6});
				}
			}
			catch (Exception var7) {
				var7.printStackTrace();
				this.netManager.networkShutdown("disconnect.genericReason", new Object[]{"Internal client error: " + var7.toString()});
			}
		}
	}

	public void disconnect() {
		this.disconnected = true;
		this.netManager.wakeThreads();
		this.netManager.networkShutdown("disconnect.closed", new Object[0]);
	}

	public void handleMobSpawn(Packet24MobSpawn var1) {
		double var2 = (double)var1.xPosition / 32.0D;
		double var4 = (double)var1.yPosition / 32.0D;
		double var6 = (double)var1.zPosition / 32.0D;
		float var8 = (float)(var1.yaw * 360) / 256.0F;
		float var9 = (float)(var1.pitch * 360) / 256.0F;
		EntityLiving var10 = (EntityLiving)EntityList.createEntity(var1.type, this.mc.theWorld);
		var10.serverPosX = var1.xPosition;
		var10.serverPosY = var1.yPosition;
		var10.serverPosZ = var1.zPosition;
		Entity[] var11 = var10.getParts();
		if(var11 != null) {
			int var12 = var1.entityId - var10.entityId;

			for(int var13 = 0; var13 < var11.length; ++var13) {
				var11[var13].entityId += var12;
			}
		}

		var10.entityId = var1.entityId;
		var10.setPositionAndRotation(var2, var4, var6, var8, var9);
		this.worldClient.addEntityToWorld(var1.entityId, var10);
		List var14 = var1.getMetadata();
		if(var14 != null) {
			var10.getDataWatcher().updateWatchedObjectsFromList(var14);
		}
	}

	public void handleUpdateTime(Packet4UpdateTime var1) {
		this.mc.theWorld.setWorldTime(var1.time);
	}

	public void handleSpawnPosition(Packet6SpawnPosition var1) {
		this.mc.thePlayer.setPlayerSpawnCoordinate(new ChunkCoordinates(var1.xPosition, var1.yPosition, var1.zPosition));
		this.mc.theWorld.getWorldInfo().setSpawn(var1.xPosition, var1.yPosition, var1.zPosition);
	}

	public void handleAttachEntity(Packet39AttachEntity var1) {
		Object var2 = this.getEntityByID(var1.entityId);
		Entity var3 = this.getEntityByID(var1.vehicleEntityId);
		if(var1.entityId == this.mc.thePlayer.entityId) {
			var2 = this.mc.thePlayer;
		}

		if(var2 != null) {
			((Entity)var2).mountEntity(var3);
		}
	}

	public void handleEntityStatus(Packet38EntityStatus var1) {
		Entity var2 = this.getEntityByID(var1.entityId);
		if(var2 != null) {
			var2.handleHealthUpdate(var1.entityStatus);
		}
	}

	private Entity getEntityByID(int var1) {
		return (Entity)(var1 == this.mc.thePlayer.entityId ? this.mc.thePlayer : this.worldClient.getEntityByID(var1));
	}

	public void handleHealth(Packet8UpdateHealth var1) {
		this.mc.thePlayer.setHealth(var1.healthMP);
		this.mc.thePlayer.getFoodStats().setFoodLevel(var1.food);
		this.mc.thePlayer.getFoodStats().setFoodSaturationLevel(var1.foodSaturation);
	}

	public void handleExperience(Packet43Experience var1) {
		this.mc.thePlayer.setXPStats(var1.experience, var1.experienceTotal, var1.experienceLevel);
	}

	public void handleRespawn(Packet9Respawn var1) {
		if(var1.respawnDimension != this.mc.thePlayer.dimension || var1.mapSeed != this.mc.thePlayer.worldObj.getWorldSeed()) {
			this.field_1210_g = false;
			this.worldClient = new WorldClient(this, new WorldSettings(var1.mapSeed, var1.creativeMode, false, false, var1.field_46031_f), var1.respawnDimension, var1.difficulty);
			this.worldClient.multiplayerWorld = true;
			//Spout start
			int height = 128;
			if (var1.worldHeight > 0 && var1.worldHeight < 12) {
				height = 2 << var1.worldHeight;
			}
			else if (var1.worldHeight >= 32) {
				height = var1.worldHeight;
			}
			worldClient.setMapHeight(height);
			//Spout end
			this.mc.changeWorld1(this.worldClient);
			this.mc.thePlayer.dimension = var1.respawnDimension;
			this.mc.displayGuiScreen(new GuiDownloadTerrain(this));
		}

		this.mc.respawn(true, var1.respawnDimension, false);
		((PlayerControllerMP)this.mc.playerController).setCreative(var1.creativeMode == 1);
	}

	public void handleExplosion(Packet60Explosion var1) {
		Explosion var2 = new Explosion(this.mc.theWorld, (Entity)null, var1.explosionX, var1.explosionY, var1.explosionZ, var1.explosionSize);
		var2.destroyedBlockPositions = var1.destroyedBlockPositions;
		var2.doExplosionB(true);
	}

	public void handleOpenWindow(Packet100OpenWindow var1) {
		if(var1.inventoryType == 0) {
			InventoryBasic var2 = new InventoryBasic(var1.windowTitle, var1.slotsCount);
			this.mc.thePlayer.displayGUIChest(var2);
			this.mc.thePlayer.craftingInventory.windowId = var1.windowId;
		}
		else if (var1.inventoryType == 2) {
			TileEntityFurnace var3 = new TileEntityFurnace();
			this.mc.thePlayer.displayGUIFurnace(var3);
			this.mc.thePlayer.craftingInventory.windowId = var1.windowId;
		}
		else if (var1.inventoryType == 5) {
			TileEntityBrewingStand var4 = new TileEntityBrewingStand();
			this.mc.thePlayer.displayGUIBrewingStand(var4);
			this.mc.thePlayer.craftingInventory.windowId = var1.windowId;
		}
		else if (var1.inventoryType == 3) {
			TileEntityDispenser var5 = new TileEntityDispenser();
			this.mc.thePlayer.displayGUIDispenser(var5);
			this.mc.thePlayer.craftingInventory.windowId = var1.windowId;
		}
		else {
			EntityPlayerSP var6;
			if(var1.inventoryType == 1) {
				var6 = this.mc.thePlayer;
				this.mc.thePlayer.displayWorkbenchGUI(MathHelper.floor_double(var6.posX), MathHelper.floor_double(var6.posY), MathHelper.floor_double(var6.posZ));
				this.mc.thePlayer.craftingInventory.windowId = var1.windowId;
			}
			else if (var1.inventoryType == 4) {
				var6 = this.mc.thePlayer;
				this.mc.thePlayer.displayGUIEnchantment(MathHelper.floor_double(var6.posX), MathHelper.floor_double(var6.posY), MathHelper.floor_double(var6.posZ));
				this.mc.thePlayer.craftingInventory.windowId = var1.windowId;
			}
		}
	}

	public void handleSetSlot(Packet103SetSlot var1) {
		if(var1.windowId == -1) {
			this.mc.thePlayer.inventory.setItemStack(var1.myItemStack);
		}
		else if (var1.windowId == 0 && var1.itemSlot >= 36 && var1.itemSlot < 45) {
			ItemStack var2 = this.mc.thePlayer.inventorySlots.getSlot(var1.itemSlot).getStack();
			if(var1.myItemStack != null && (var2 == null || var2.stackSize < var1.myItemStack.stackSize)) {
				var1.myItemStack.animationsToGo = 5;
			}

			this.mc.thePlayer.inventorySlots.putStackInSlot(var1.itemSlot, var1.myItemStack);
		}
		else if (var1.windowId == this.mc.thePlayer.craftingInventory.windowId) {
			this.mc.thePlayer.craftingInventory.putStackInSlot(var1.itemSlot, var1.myItemStack);
		}
	}

	public void handleContainerTransaction(Packet106Transaction var1) {
		Container var2 = null;
		if(var1.windowId == 0) {
			var2 = this.mc.thePlayer.inventorySlots;
		}
		else if (var1.windowId == this.mc.thePlayer.craftingInventory.windowId) {
			var2 = this.mc.thePlayer.craftingInventory;
		}

		if(var2 != null) {
			if(var1.accepted) {
				var2.func_20113_a(var1.shortWindowId);
			}
			else {
				var2.func_20110_b(var1.shortWindowId);
				this.addToSendQueue(new Packet106Transaction(var1.windowId, var1.shortWindowId, true));
			}
		}
	}

	public void handleWindowItems(Packet104WindowItems var1) {
		if(var1.windowId == 0) {
			this.mc.thePlayer.inventorySlots.putStacksInSlots(var1.itemStack);
		}
		else if (var1.windowId == this.mc.thePlayer.craftingInventory.windowId) {
			this.mc.thePlayer.craftingInventory.putStacksInSlots(var1.itemStack);
		}
	}

	public void handleUpdateSign(Packet130UpdateSign var1) {
		if(this.mc.theWorld.blockExists(var1.xPosition, var1.yPosition, var1.zPosition)) {
			TileEntity var2 = this.mc.theWorld.getBlockTileEntity(var1.xPosition, var1.yPosition, var1.zPosition);
			if(var2 instanceof TileEntitySign) {
				TileEntitySign var3 = (TileEntitySign)var2;

				for(int var4 = 0; var4 < 4; ++var4) {
					var3.signText[var4] = var1.signLines[var4];
				}

				var3.onInventoryChanged();
				
				//Spout start
				var3.recalculateText();
				//Spout end
			}
		}
	}

	public void handleUpdateProgressbar(Packet105UpdateProgressbar var1) {
		this.registerPacket(var1);
		if(this.mc.thePlayer.craftingInventory != null && this.mc.thePlayer.craftingInventory.windowId == var1.windowId) {
			this.mc.thePlayer.craftingInventory.updateProgressBar(var1.progressBar, var1.progressBarValue);
		}
	}

	public void handlePlayerInventory(Packet5PlayerInventory var1) {
		Entity var2 = this.getEntityByID(var1.entityID);
		if(var2 != null) {
			var2.outfitWithItem(var1.slot, var1.itemID, var1.itemDamage);
		}
	}

	public void handleCloseWindow(Packet101CloseWindow var1) {
		this.mc.thePlayer.closeScreen();
	}

	public void handleNotePlay(Packet54PlayNoteBlock var1) {
		this.mc.theWorld.playNoteAt(var1.xLocation, var1.yLocation, var1.zLocation, var1.instrumentType, var1.pitch);
	}

	public void handleBed(Packet70Bed var1) {
		int var2 = var1.bedState;
		if(var2 >= 0 && var2 < Packet70Bed.bedChat.length && Packet70Bed.bedChat[var2] != null) {
			this.mc.thePlayer.addChatMessage(Packet70Bed.bedChat[var2]);
		}

		if(var2 == 1) {
			this.worldClient.getWorldInfo().setIsRaining(true);
			this.worldClient.setRainStrength(1.0F);
		}
		else if (var2 == 2) {
			this.worldClient.getWorldInfo().setIsRaining(false);
			this.worldClient.setRainStrength(0.0F);
		}
		else if (var2 == 3) {
			((PlayerControllerMP)this.mc.playerController).setCreative(var1.gameMode == 1);
		}
		else if (var2 == 4) {
			this.mc.displayGuiScreen(new GuiWinGame());
		}
	}

	public void handleItemData(Packet131MapData var1) {
		if(var1.itemID == Item.map.shiftedIndex) {
			ItemMap.getMPMapData(var1.uniqueID, this.mc.theWorld).func_28171_a(var1.itemData);
		}
		else {
			System.out.println("Unknown itemid: " + var1.uniqueID);
		}
	}

	public void handleDoorChange(Packet61DoorChange var1) {
		this.mc.theWorld.playAuxSFX(var1.sfxID, var1.posX, var1.posY, var1.posZ, var1.auxData);
	}

	public void handleStatistic(Packet200Statistic var1) {
		((EntityClientPlayerMP)this.mc.thePlayer).incrementStat(StatList.getOneShotStat(var1.statisticId), var1.amount);
	}

	public void handleEntityEffect(Packet41EntityEffect var1) {
		Entity var2 = this.getEntityByID(var1.entityId);
		if(var2 != null && var2 instanceof EntityLiving) {
			((EntityLiving)var2).addPotionEffect(new PotionEffect(var1.effectId, var1.duration, var1.effectAmp));
		}
	}

	public void handleRemoveEntityEffect(Packet42RemoveEntityEffect var1) {
		Entity var2 = this.getEntityByID(var1.entityId);
		if(var2 != null && var2 instanceof EntityLiving) {
			((EntityLiving)var2).removePotionEffect(var1.effectId);
		}
	}

	public boolean isServerHandler() {
		return false;
	}

	public void handlePlayerInfo(Packet201PlayerInfo var1) {
		GuiSavingLevelString var2 = (GuiSavingLevelString)this.playerInfoMap.get(var1.playerName);
		if(var2 == null && var1.isConnected) {
			var2 = new GuiSavingLevelString(var1.playerName);
			this.playerInfoMap.put(var1.playerName, var2);
			this.playerNames.add(var2);
		}

		if(var2 != null && !var1.isConnected) {
			this.playerInfoMap.remove(var1.playerName);
			this.playerNames.remove(var2);
		}

		if(var1.isConnected && var2 != null) {
			var2.responseTime = var1.ping;
		}

	}

	public void handleKeepAlive(Packet0KeepAlive var1) {
		this.addToSendQueue(new Packet0KeepAlive(var1.randomId));
	}
}
