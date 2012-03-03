package net.minecraft.src;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetworkMasterThread;
import net.minecraft.src.NetworkReaderThread;
import net.minecraft.src.NetworkWriterThread;
import net.minecraft.src.Packet;
import net.minecraft.src.ThreadMonitorConnection;

//Spout start
import org.spoutcraft.client.chunkcache.ChunkCache;
//Spout end

public class NetworkManager {

	public static final Object threadSyncObject = new Object();
	public static int numReadThreads;
	public static int numWriteThreads;
	private Object sendQueueLock = new Object();
	private Socket networkSocket;
	private final SocketAddress remoteSocketAddress;
	private DataInputStream socketInputStream;
	private DataOutputStream socketOutputStream;
	private boolean isRunning = true;
	private List readPackets = Collections.synchronizedList(new ArrayList());
	private List dataPackets = Collections.synchronizedList(new ArrayList());
	private List chunkDataPackets = Collections.synchronizedList(new ArrayList());
	private NetHandler netHandler;
	private boolean isServerTerminating = false;
	private Thread writeThread;
	private Thread readThread;
	private boolean isTerminating = false;
	private String terminationReason = "";
	private Object[] field_20101_t;
	private int timeSinceLastRead = 0;
	private int sendQueueByteLength = 0;
	public static int[] field_28145_d = new int[256];
	public static int[] field_28144_e = new int[256];
	public int chunkDataSendCounter = 0;
	private int field_20100_w = 50;

	public NetworkManager(Socket par1Socket, String par2Str, NetHandler par3NetHandler) throws IOException {
		this.networkSocket = par1Socket;
		this.remoteSocketAddress = par1Socket.getRemoteSocketAddress();
		this.netHandler = par3NetHandler;

		try {
			par1Socket.setSoTimeout(30000);
			par1Socket.setTrafficClass(24);
		} catch (SocketException var5) {
			System.err.println(var5.getMessage());
		}

		this.socketInputStream = new DataInputStream(par1Socket.getInputStream());
		this.socketOutputStream = new DataOutputStream(new BufferedOutputStream(par1Socket.getOutputStream(), 5120));
		this.readThread = new NetworkReaderThread(this, par2Str + " read thread");
		this.writeThread = new NetworkWriterThread(this, par2Str + " write thread");
		this.readThread.start();
		this.writeThread.start();
	}

	public void addToSendQueue(Packet par1Packet) {
		if (!this.isServerTerminating) {
			ChunkCache.totalPacketUp.addAndGet(par1Packet.getPacketSize()); // Spout
			Object var2 = this.sendQueueLock;
			synchronized(this.sendQueueLock) {
				this.sendQueueByteLength += par1Packet.getPacketSize() + 1;
				if (par1Packet.isChunkDataPacket) {
					this.chunkDataPackets.add(par1Packet);
				} else {
					this.dataPackets.add(par1Packet);
				}

			}
		}
	}

	private boolean sendPacket() {
		boolean var1 = false;

		try {
			Packet var2;
			Object var3;
			int var10001;
			int[] var10000;
			if (!this.dataPackets.isEmpty() && (this.chunkDataSendCounter == 0 || System.currentTimeMillis() - ((Packet)this.dataPackets.get(0)).creationTimeMillis >= (long)this.chunkDataSendCounter)) {
				var3 = this.sendQueueLock;
				synchronized(this.sendQueueLock) {
					var2 = (Packet)this.dataPackets.remove(0);
					this.sendQueueByteLength -= var2.getPacketSize() + 1;
				}

				Packet.writePacket(var2, this.socketOutputStream);
				var10000 = field_28144_e;
				var10001 = var2.getPacketId();
				var10000[var10001] += var2.getPacketSize() + 1;
				var1 = true;
			}

			if (this.field_20100_w-- <= 0 && !this.chunkDataPackets.isEmpty() && (this.chunkDataSendCounter == 0 || System.currentTimeMillis() - ((Packet)this.chunkDataPackets.get(0)).creationTimeMillis >= (long)this.chunkDataSendCounter)) {
				var3 = this.sendQueueLock;
				synchronized(this.sendQueueLock) {
					var2 = (Packet)this.chunkDataPackets.remove(0);
					this.sendQueueByteLength -= var2.getPacketSize() + 1;
				}

				Packet.writePacket(var2, this.socketOutputStream);
				var10000 = field_28144_e;
				var10001 = var2.getPacketId();
				var10000[var10001] += var2.getPacketSize() + 1;
				this.field_20100_w = 0;
				var1 = true;
			}

			return var1;
		} catch (Exception var8) {
			if (!this.isTerminating) {
				this.onNetworkError(var8);
			}

			return false;
		}
	}

	public void wakeThreads() {
		this.readThread.interrupt();
		this.writeThread.interrupt();
	}

	private boolean readPacket() {
		boolean var1 = false;

		try {
			Packet var2 = Packet.readPacket(this.socketInputStream, this.netHandler.isServerHandler());
			if (var2 != null) {
				int[] var10000 = field_28145_d;
				int var10001 = var2.getPacketId();
				var10000[var10001] += var2.getPacketSize() + 1;
				if (!this.isServerTerminating) {
					this.readPackets.add(var2);
				}

				var1 = true;
			} else {
				this.networkShutdown("disconnect.endOfStream", new Object[0]);
			}

			return var1;
		} catch (Exception var3) {
			if (!this.isTerminating) {
				this.onNetworkError(var3);
			}

			return false;
		}
	}

	private void onNetworkError(Exception par1Exception) {
		par1Exception.printStackTrace();
		this.networkShutdown("disconnect.genericReason", new Object[]{"Internal exception: " + par1Exception.toString()});
	}

	public void networkShutdown(String par1Str, Object ... par2ArrayOfObj) {
		if (this.isRunning) {
			this.isTerminating = true;
			this.terminationReason = par1Str;
			this.field_20101_t = par2ArrayOfObj;
			(new NetworkMasterThread(this)).start();
			this.isRunning = false;

			try {
				this.socketInputStream.close();
				this.socketInputStream = null;
			} catch (Throwable var6) {
				;
			}

			try {
				this.socketOutputStream.close();
				this.socketOutputStream = null;
			} catch (Throwable var5) {
				;
			}

			try {
				this.networkSocket.close();
				this.networkSocket = null;
			} catch (Throwable var4) {
				;
			}

		}
	}

	public void processReadPackets() {
		if(this.sendQueueByteLength > (1048576 * 10)) {//Spout increased overflow from 1mb to 10mb
			this.networkShutdown("disconnect.overflow", new Object[0]);
		}

		if (this.readPackets.isEmpty()) {
			if (this.timeSinceLastRead++ == 1200) {
				this.networkShutdown("disconnect.timeout", new Object[0]);
			}
		} else {
			this.timeSinceLastRead = 0;
		}

		int var1 = 1000;

		while (!this.readPackets.isEmpty() && var1-- >= 0) {
			Packet var2 = (Packet)this.readPackets.remove(0);
			ChunkCache.totalPacketDown.addAndGet(var2.getPacketSize()); // Spout
			var2.processPacket(this.netHandler);
		}

		this.wakeThreads();
		if (this.isTerminating && this.readPackets.isEmpty()) {
			this.netHandler.handleErrorMessage(this.terminationReason, this.field_20101_t);
		}

	}

	public void serverShutdown() {
		if (!this.isServerTerminating) {
			this.wakeThreads();
			this.isServerTerminating = true;
			this.readThread.interrupt();
			(new ThreadMonitorConnection(this)).start();
		}
	}

	
	static boolean isRunning(NetworkManager par0NetworkManager) {
		return par0NetworkManager.isRunning;
	}

	
	static boolean isServerTerminating(NetworkManager par0NetworkManager) {
		return par0NetworkManager.isServerTerminating;
	}

	
	static boolean readNetworkPacket(NetworkManager par0NetworkManager) {
		return par0NetworkManager.readPacket();
	}

	
	static boolean sendNetworkPacket(NetworkManager par0NetworkManager) {
		return par0NetworkManager.sendPacket();
	}

	
	static DataOutputStream getOutputStream(NetworkManager par0NetworkManager) {
		return par0NetworkManager.socketOutputStream;
	}

	
	static boolean isTerminating(NetworkManager par0NetworkManager) {
		return par0NetworkManager.isTerminating;
	}

	
	static void sendError(NetworkManager par0NetworkManager, Exception par1Exception) {
		par0NetworkManager.onNetworkError(par1Exception);
	}

	
	static Thread getReadThread(NetworkManager par0NetworkManager) {
		return par0NetworkManager.readThread;
	}

	
	static Thread getWriteThread(NetworkManager par0NetworkManager) {
		return par0NetworkManager.writeThread;
	}

}
