package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Packet {

	/** Maps packet id to packet class */
	public static IntHashMap packetIdToClassMap = new IntHashMap();

	/** Maps packet class to packet id */
	private static Map packetClassToIdMap = new HashMap();

	/** List of the client's packet IDs. */
	private static Set clientPacketIdList = new HashSet();

	/** List of the server's packet IDs. */
	private static Set serverPacketIdList = new HashSet();
	protected ILogAgent field_98193_m;

	/** the system time in milliseconds when this packet was created. */
	public final long creationTimeMillis = System.currentTimeMillis();
	public static long receivedID;
	public static long receivedSize;

	/** Assumed to be sequential by the profiler. */
	public static long sentID;
	public static long sentSize;

	/**
	 * Only true for Packet51MapChunk, Packet52MultiBlockChange, Packet53BlockChange and Packet59ComplexEntity. Used to
	 * separate them into a different send queue.
	 */
	public boolean isChunkDataPacket = false;

	/**
	 * Adds a two way mapping between the packet ID and packet class.
	 */
	// Spout Start - static to public static
	public static void addIdClassMapping(int par0, boolean par1, boolean par2, Class par3Class) {
	// Spout End
		if (packetIdToClassMap.containsItem(par0)) {
			throw new IllegalArgumentException("Duplicate packet id:" + par0);
		} else if (packetClassToIdMap.containsKey(par3Class)) {
			throw new IllegalArgumentException("Duplicate packet class:" + par3Class);
		} else {
			packetIdToClassMap.addKey(par0, par3Class);
			packetClassToIdMap.put(par3Class, Integer.valueOf(par0));

			if (par1) {
				clientPacketIdList.add(Integer.valueOf(par0));
			}

			if (par2) {
				serverPacketIdList.add(Integer.valueOf(par0));
			}
		}
	}

	/**
	 * Returns a new instance of the specified Packet class.
	 */
	public static Packet getNewPacket(ILogAgent par0ILogAgent, int par1) {
		try {
			Class var2 = (Class)packetIdToClassMap.lookup(par1);
			return var2 == null ? null : (Packet)var2.newInstance();
		} catch (Exception var3) {
			var3.printStackTrace();
			par0ILogAgent.func_98232_c("Skipping packet with id " + par1);
			return null;
		}
	}

	/**
	 * Writes a byte array to the DataOutputStream
	 */
	public static void writeByteArray(DataOutputStream par0DataOutputStream, byte[] par1ArrayOfByte) throws IOException {
		par0DataOutputStream.writeShort(par1ArrayOfByte.length);
		par0DataOutputStream.write(par1ArrayOfByte);
	}

	/**
	 * the first short in the stream indicates the number of bytes to read
	 */
	public static byte[] readBytesFromStream(DataInputStream par0DataInputStream) throws IOException {
		short var1 = par0DataInputStream.readShort();

		if (var1 < 0) {
			throw new IOException("Key was smaller than nothing!  Weird key!");
		} else {
			byte[] var2 = new byte[var1];
			par0DataInputStream.readFully(var2);
			return var2;
		}
	}

	/**
	 * Returns the ID of this packet.
	 */
	public final int getPacketId() {
		return ((Integer)packetClassToIdMap.get(this.getClass())).intValue();
	}

	/**
	 * Read a packet, prefixed by its ID, from the data stream.
	 */
	public static Packet readPacket(ILogAgent par0ILogAgent, DataInputStream par1DataInputStream, boolean par2, Socket par3Socket) throws IOException {
		boolean var4 = false;
		Packet var5 = null;
		int var6 = par3Socket.getSoTimeout();
		int var9;

		try {
			var9 = par1DataInputStream.read();

			if (var9 == -1) {
				return null;
			}

			if (par2 && !serverPacketIdList.contains(Integer.valueOf(var9)) || !par2 && !clientPacketIdList.contains(Integer.valueOf(var9))) {
				throw new IOException("Bad packet id " + var9);
			}

			var5 = getNewPacket(par0ILogAgent, var9);

			if (var5 == null) {
				throw new IOException("Bad packet id " + var9);
			}

			var5.field_98193_m = par0ILogAgent;

			if (var5 instanceof Packet254ServerPing) {
				par3Socket.setSoTimeout(1500);
			}

			var5.readPacketData(par1DataInputStream);
			++receivedID;
			receivedSize += (long)var5.getPacketSize();
		} catch (EOFException var8) {
			par0ILogAgent.func_98232_c("Reached end of stream");
			return null;
		}

		PacketCount.countPacket(var9, (long)var5.getPacketSize());
		++receivedID;
		receivedSize += (long)var5.getPacketSize();
		par3Socket.setSoTimeout(var6);
		return var5;
	}

	/**
	 * Writes a packet, prefixed by its ID, to the data stream.
	 */
	public static void writePacket(Packet par0Packet, DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.write(par0Packet.getPacketId());
		par0Packet.writePacketData(par1DataOutputStream);
		++sentID;
		sentSize += (long)par0Packet.getPacketSize();
	}

	/**
	 * Writes a String to the DataOutputStream
	 */
	public static void writeString(String par0Str, DataOutputStream par1DataOutputStream) throws IOException {
		if (par0Str.length() > 32767) {
			throw new IOException("String too big");
		} else {
			par1DataOutputStream.writeShort(par0Str.length());
			par1DataOutputStream.writeChars(par0Str);
		}
	}

	/**
	 * Reads a string from a packet
	 */
	public static String readString(DataInputStream par0DataInputStream, int par1) throws IOException {
		short var2 = par0DataInputStream.readShort();

		if (var2 > par1) {
			throw new IOException("Received string length longer than maximum allowed (" + var2 + " > " + par1 + ")");
		} else if (var2 < 0) {
			throw new IOException("Received string length is less than zero! Weird string!");
		} else {
			StringBuilder var3 = new StringBuilder();

			for (int var4 = 0; var4 < var2; ++var4) {
				var3.append(par0DataInputStream.readChar());
			}

			return var3.toString();
		}
	}

	/**
	 * Abstract. Reads the raw packet data from the data stream.
	 */
	public abstract void readPacketData(DataInputStream var1) throws IOException;

	/**
	 * Abstract. Writes the raw packet data to the data stream.
	 */
	public abstract void writePacketData(DataOutputStream var1) throws IOException;

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public abstract void processPacket(NetHandler var1);

	/**
	 * Abstract. Return the size of the packet (not counting the header).
	 */
	public abstract int getPacketSize();

	/**
	 * only false for the abstract Packet class, all real packets return true
	 */
	public boolean isRealPacket() {
		return false;
	}

	/**
	 * eg return packet30entity.entityId == entityId; WARNING : will throw if you compare a packet to a different packet
	 * class
	 */
	public boolean containsSameEntityIDAs(Packet par1Packet) {
		return false;
	}

	/**
	 * If this returns true, the packet may be processed on any thread; otherwise it is queued for the main thread to
	 * handle.
	 */
	public boolean canProcessAsync() {
		return false;
	}

	public String toString() {
		String var1 = this.getClass().getSimpleName();
		return var1;
	}

	/**
	 * Reads a ItemStack from the InputStream
	 */
	public static ItemStack readItemStack(DataInputStream par0DataInputStream) throws IOException {
		ItemStack var1 = null;
		short var2 = par0DataInputStream.readShort();

		if (var2 >= 0) {
			byte var3 = par0DataInputStream.readByte();
			short var4 = par0DataInputStream.readShort();
			var1 = new ItemStack(var2, var3, var4);
			var1.stackTagCompound = readNBTTagCompound(par0DataInputStream);
		}

		return var1;
	}

	/**
	 * Writes the ItemStack's ID (short), then size (byte), then damage. (short)
	 */
	public static void writeItemStack(ItemStack par0ItemStack, DataOutputStream par1DataOutputStream) throws IOException {
		if (par0ItemStack == null) {
			par1DataOutputStream.writeShort(-1);
		} else {
			par1DataOutputStream.writeShort(par0ItemStack.itemID);
			par1DataOutputStream.writeByte(par0ItemStack.stackSize);
			par1DataOutputStream.writeShort(par0ItemStack.getItemDamage());
			NBTTagCompound var2 = null;

			if (par0ItemStack.getItem().isDamageable() || par0ItemStack.getItem().getShareTag()) {
				var2 = par0ItemStack.stackTagCompound;
			}

			writeNBTTagCompound(var2, par1DataOutputStream);
		}
	}

	/**
	 * Reads a compressed NBTTagCompound from the InputStream
	 */
	public static NBTTagCompound readNBTTagCompound(DataInputStream par0DataInputStream) throws IOException {
		short var1 = par0DataInputStream.readShort();

		if (var1 < 0) {
			return null;
		} else {
			byte[] var2 = new byte[var1];
			par0DataInputStream.readFully(var2);
			return CompressedStreamTools.decompress(var2);
		}
	}

	/**
	 * Writes a compressed NBTTagCompound to the OutputStream
	 */
	protected static void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound, DataOutputStream par1DataOutputStream) throws IOException {
		if (par0NBTTagCompound == null) {
			par1DataOutputStream.writeShort(-1);
		} else {
			byte[] var2 = CompressedStreamTools.compress(par0NBTTagCompound);
			par1DataOutputStream.writeShort((short)var2.length);
			par1DataOutputStream.write(var2);
		}
	}

	static {
		addIdClassMapping(0, true, true, Packet0KeepAlive.class);
		addIdClassMapping(1, true, true, Packet1Login.class);
		addIdClassMapping(2, false, true, Packet2ClientProtocol.class);
		addIdClassMapping(3, true, true, Packet3Chat.class);
		addIdClassMapping(4, true, false, Packet4UpdateTime.class);
		addIdClassMapping(5, true, false, Packet5PlayerInventory.class);
		addIdClassMapping(6, true, false, Packet6SpawnPosition.class);
		addIdClassMapping(7, false, true, Packet7UseEntity.class);
		addIdClassMapping(8, true, false, Packet8UpdateHealth.class);
		addIdClassMapping(9, true, true, Packet9Respawn.class);
		addIdClassMapping(10, true, true, Packet10Flying.class);
		addIdClassMapping(11, true, true, Packet11PlayerPosition.class);
		addIdClassMapping(12, true, true, Packet12PlayerLook.class);
		addIdClassMapping(13, true, true, Packet13PlayerLookMove.class);
		addIdClassMapping(14, false, true, Packet14BlockDig.class);
		addIdClassMapping(15, false, true, Packet15Place.class);
		addIdClassMapping(16, true, true, Packet16BlockItemSwitch.class);
		addIdClassMapping(17, true, false, Packet17Sleep.class);
		addIdClassMapping(18, true, true, Packet18Animation.class);
		addIdClassMapping(19, false, true, Packet19EntityAction.class);
		addIdClassMapping(20, true, false, Packet20NamedEntitySpawn.class);
		addIdClassMapping(22, true, false, Packet22Collect.class);
		addIdClassMapping(23, true, false, Packet23VehicleSpawn.class);
		addIdClassMapping(24, true, false, Packet24MobSpawn.class);
		addIdClassMapping(25, true, false, Packet25EntityPainting.class);
		addIdClassMapping(26, true, false, Packet26EntityExpOrb.class);
		addIdClassMapping(28, true, false, Packet28EntityVelocity.class);
		addIdClassMapping(29, true, false, Packet29DestroyEntity.class);
		addIdClassMapping(30, true, false, Packet30Entity.class);
		addIdClassMapping(31, true, false, Packet31RelEntityMove.class);
		addIdClassMapping(32, true, false, Packet32EntityLook.class);
		addIdClassMapping(33, true, false, Packet33RelEntityMoveLook.class);
		addIdClassMapping(34, true, false, Packet34EntityTeleport.class);
		addIdClassMapping(35, true, false, Packet35EntityHeadRotation.class);
		addIdClassMapping(38, true, false, Packet38EntityStatus.class);
		addIdClassMapping(39, true, false, Packet39AttachEntity.class);
		addIdClassMapping(40, true, false, Packet40EntityMetadata.class);
		addIdClassMapping(41, true, false, Packet41EntityEffect.class);
		addIdClassMapping(42, true, false, Packet42RemoveEntityEffect.class);
		addIdClassMapping(43, true, false, Packet43Experience.class);
		addIdClassMapping(51, true, false, Packet51MapChunk.class);
		addIdClassMapping(52, true, false, Packet52MultiBlockChange.class);
		addIdClassMapping(53, true, false, Packet53BlockChange.class);
		addIdClassMapping(54, true, false, Packet54PlayNoteBlock.class);
		addIdClassMapping(55, true, false, Packet55BlockDestroy.class);
		addIdClassMapping(56, true, false, Packet56MapChunks.class);
		addIdClassMapping(60, true, false, Packet60Explosion.class);
		addIdClassMapping(61, true, false, Packet61DoorChange.class);
		addIdClassMapping(62, true, false, Packet62LevelSound.class);
		addIdClassMapping(63, true, false, Packet63WorldParticles.class);
		addIdClassMapping(70, true, false, Packet70GameEvent.class);
		addIdClassMapping(71, true, false, Packet71Weather.class);
		addIdClassMapping(100, true, false, Packet100OpenWindow.class);
		addIdClassMapping(101, true, true, Packet101CloseWindow.class);
		addIdClassMapping(102, false, true, Packet102WindowClick.class);
		addIdClassMapping(103, true, false, Packet103SetSlot.class);
		addIdClassMapping(104, true, false, Packet104WindowItems.class);
		addIdClassMapping(105, true, false, Packet105UpdateProgressbar.class);
		addIdClassMapping(106, true, true, Packet106Transaction.class);
		addIdClassMapping(107, true, true, Packet107CreativeSetSlot.class);
		addIdClassMapping(108, false, true, Packet108EnchantItem.class);
		addIdClassMapping(130, true, true, Packet130UpdateSign.class);
		addIdClassMapping(131, true, false, Packet131MapData.class);
		addIdClassMapping(132, true, false, Packet132TileEntityData.class);
		addIdClassMapping(200, true, false, Packet200Statistic.class);
		addIdClassMapping(201, true, false, Packet201PlayerInfo.class);
		addIdClassMapping(202, true, true, Packet202PlayerAbilities.class);
		addIdClassMapping(203, true, true, Packet203AutoComplete.class);
		addIdClassMapping(204, false, true, Packet204ClientInfo.class);
		addIdClassMapping(205, false, true, Packet205ClientCommand.class);
		addIdClassMapping(206, true, false, Packet206SetObjective.class);
		addIdClassMapping(207, true, false, Packet207SetScore.class);
		addIdClassMapping(208, true, false, Packet208SetDisplayObjective.class);
		addIdClassMapping(209, true, false, Packet209SetPlayerTeam.class);
		addIdClassMapping(250, true, true, Packet250CustomPayload.class);
		addIdClassMapping(252, true, true, Packet252SharedKey.class);
		addIdClassMapping(253, true, false, Packet253ServerAuthData.class);
		addIdClassMapping(254, false, true, Packet254ServerPing.class);
		addIdClassMapping(255, true, true, Packet255KickDisconnect.class);
	}
}
