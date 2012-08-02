package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

public abstract class Packet {
	public static IntHashMap packetIdToClassMap = new IntHashMap();
	private static Map packetClassToIdMap = new HashMap();
	private static Set clientPacketIdList = new HashSet();
	private static Set serverPacketIdList = new HashSet();
	public final long creationTimeMillis = System.currentTimeMillis();
	public static long field_73292_n;
	public static long field_73293_o;
	public static long field_73290_p;
	public static long field_73289_q;
	public boolean isChunkDataPacket = false;

	public static void addIdClassMapping(int par0, boolean par1, boolean par2, Class par3Class) { // Spout default -> public
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

	public static Packet getNewPacket(int par0) {
		try {
			Class var1 = (Class)packetIdToClassMap.lookup(par0);
			return var1 == null ? null : (Packet)var1.newInstance();
		} catch (Exception var2) {
			var2.printStackTrace();
			System.out.println("Skipping packet with id " + par0);
			return null;
		}
	}

	public static void func_73274_a(DataOutputStream par0DataOutputStream, byte[] par1ArrayOfByte) throws IOException {
		par0DataOutputStream.writeShort(par1ArrayOfByte.length);
		par0DataOutputStream.write(par1ArrayOfByte);
	}

	public static byte[] func_73280_b(DataInputStream par0DataInputStream) throws IOException {
		short var1 = par0DataInputStream.readShort();

		if (var1 < 0) {
			throw new IOException("Key was smaller than nothing!  Weird key!");
		} else {
			byte[] var2 = new byte[var1];
			par0DataInputStream.read(var2);
			return var2;
		}
	}

	public final int getPacketId() {
		return ((Integer)packetClassToIdMap.get(this.getClass())).intValue();
	}

	public static Packet readPacket(DataInputStream par0DataInputStream, boolean par1) throws IOException {
		boolean var2 = false;
		Packet var3 = null;
		int var6;

		try {
			var6 = par0DataInputStream.read();
			if (var6 == -1) {
				return null;
			}

			if (par1 && !serverPacketIdList.contains(Integer.valueOf(var6)) || !par1 && !clientPacketIdList.contains(Integer.valueOf(var6))) {
				throw new IOException("Bad packet id " + var6);
			}

			var3 = getNewPacket(var6);
			if (var3 == null) {
				throw new IOException("Bad packet id " + var6);
			}

			var3.readPacketData(par0DataInputStream);
			++field_73292_n;
			field_73293_o += (long)var3.getPacketSize();
		} catch (EOFException var5) {
			System.out.println("Reached end of stream");
			return null;
		}

		PacketCount.countPacket(var6, (long)var3.getPacketSize());
		++field_73292_n;
		field_73293_o += (long)var3.getPacketSize();
		return var3;
	}

	public static void writePacket(Packet par0Packet, DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.write(par0Packet.getPacketId());
		par0Packet.writePacketData(par1DataOutputStream);
		++field_73290_p;
		field_73289_q += (long)par0Packet.getPacketSize();
	}

	public static void writeString(String par0Str, DataOutputStream par1DataOutputStream) throws IOException {
		if (par0Str.length() > 32767) {
			throw new IOException("String too big");
		} else {
			par1DataOutputStream.writeShort(par0Str.length());
			par1DataOutputStream.writeChars(par0Str);
		}
	}

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

	public abstract void readPacketData(DataInputStream var1) throws IOException;

	public abstract void writePacketData(DataOutputStream var1) throws IOException;

	public abstract void processPacket(NetHandler var1);

	public abstract int getPacketSize();

	public boolean func_73278_e() {
		return false;
	}

	public boolean func_73268_a(Packet par1Packet) {
		return false;
	}

	public boolean func_73277_a_() {
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
			/*boolean override = SpoutClient.getInstance().isSpoutActive() && var3==MaterialData.flint.getRawId(); // Spout
			if (Item.itemsList[var3].isDamageable() || Item.itemsList[var3].func_46056_k() || override) { // Spout || override*/
			var1.stackTagCompound = readNBTTagCompound(par0DataInputStream);
		}

		return var1;
	}

	public static void writeItemStack(ItemStack par0ItemStack, DataOutputStream par1DataOutputStream) throws IOException {
		if (par0ItemStack == null) {
			par1DataOutputStream.writeShort(-1);
		} else {
			par1DataOutputStream.writeShort(par0ItemStack.itemID);
			par1DataOutputStream.writeByte(par0ItemStack.stackSize);
			par1DataOutputStream.writeShort(par0ItemStack.getItemDamage());
			NBTTagCompound var2 = null;

			if (par0ItemStack.getItem().isDamageable() || par0ItemStack.getItem().func_77651_p()) {
				var2 = par0ItemStack.stackTagCompound;
			}

			writeNBTTagCompound(var2, par1DataOutputStream);
		}
	}

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
		addIdClassMapping(16, false, true, Packet16BlockItemSwitch.class);
		addIdClassMapping(17, true, false, Packet17Sleep.class);
		addIdClassMapping(18, true, true, Packet18Animation.class);
		addIdClassMapping(19, false, true, Packet19EntityAction.class);
		addIdClassMapping(20, true, false, Packet20NamedEntitySpawn.class);
		addIdClassMapping(21, true, false, Packet21PickupSpawn.class);
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
		addIdClassMapping(250, true, true, Packet250CustomPayload.class);
		addIdClassMapping(252, true, true, Packet252SharedKey.class);
		addIdClassMapping(253, true, false, Packet253ServerAuthData.class);
		addIdClassMapping(254, false, true, Packet254ServerPing.class);
		addIdClassMapping(255, true, true, Packet255KickDisconnect.class);
	}
}
