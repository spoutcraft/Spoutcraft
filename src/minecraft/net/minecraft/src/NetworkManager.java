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

import org.getspout.spout.chunkcache.ChunkCache;

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


   public NetworkManager(Socket var1, String var2, NetHandler var3) throws IOException {
      this.networkSocket = var1;
      this.remoteSocketAddress = var1.getRemoteSocketAddress();
      this.netHandler = var3;

      try {
         var1.setSoTimeout(30000);
         var1.setTrafficClass(24);
      } catch (SocketException var5) {
         System.err.println(var5.getMessage());
      }

      this.socketInputStream = new DataInputStream(var1.getInputStream());
      this.socketOutputStream = new DataOutputStream(new BufferedOutputStream(var1.getOutputStream(), 5120));
      this.readThread = new NetworkReaderThread(this, var2 + " read thread");
      this.writeThread = new NetworkWriterThread(this, var2 + " write thread");
      this.readThread.start();
      this.writeThread.start();
   }

   public void addToSendQueue(Packet var1) {
      if(!this.isServerTerminating) {
         ChunkCache.totalPacketUp.addAndGet(var1.getPacketSize()); // Spout
         Object var2 = this.sendQueueLock;
         synchronized(this.sendQueueLock) {
            this.sendQueueByteLength += var1.getPacketSize() + 1;
            if(var1.isChunkDataPacket) {
               this.chunkDataPackets.add(var1);
            } else {
               this.dataPackets.add(var1);
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
         if(!this.dataPackets.isEmpty() && (this.chunkDataSendCounter == 0 || System.currentTimeMillis() - ((Packet)this.dataPackets.get(0)).creationTimeMillis >= (long)this.chunkDataSendCounter)) {
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

         if(this.field_20100_w-- <= 0 && !this.chunkDataPackets.isEmpty() && (this.chunkDataSendCounter == 0 || System.currentTimeMillis() - ((Packet)this.chunkDataPackets.get(0)).creationTimeMillis >= (long)this.chunkDataSendCounter)) {
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
         if(!this.isTerminating) {
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
         if(var2 != null) {
            int[] var10000 = field_28145_d;
            int var10001 = var2.getPacketId();
            var10000[var10001] += var2.getPacketSize() + 1;
            this.readPackets.add(var2);
            var1 = true;
         } else {
            this.networkShutdown("disconnect.endOfStream", new Object[0]);
         }

         return var1;
      } catch (Exception var3) {
         if(!this.isTerminating) {
            this.onNetworkError(var3);
         }

         return false;
      }
   }

   private void onNetworkError(Exception var1) {
      var1.printStackTrace();
      this.networkShutdown("disconnect.genericReason", new Object[]{"Internal exception: " + var1.toString()});
   }

   public void networkShutdown(String var1, Object ... var2) {
      if(this.isRunning) {
         this.isTerminating = true;
         this.terminationReason = var1;
         this.field_20101_t = var2;
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
      if(this.sendQueueByteLength > 1048576) {
         this.networkShutdown("disconnect.overflow", new Object[0]);
      }

      if(this.readPackets.isEmpty()) {
         if(this.timeSinceLastRead++ == 1200) {
            this.networkShutdown("disconnect.timeout", new Object[0]);
         }
      } else {
         this.timeSinceLastRead = 0;
      }

      int var1 = 100;

      while(!this.readPackets.isEmpty() && var1-- >= 0) {
         Packet var2 = (Packet)this.readPackets.remove(0);
         ChunkCache.totalPacketDown.addAndGet(var2.getPacketSize()); // Spout
         var2.processPacket(this.netHandler);
      }

      this.wakeThreads();
      if(this.isTerminating && this.readPackets.isEmpty()) {
         this.netHandler.handleErrorMessage(this.terminationReason, this.field_20101_t);
      }

   }

   public void func_28142_c() {
      this.wakeThreads();
      this.isServerTerminating = true;
      this.readThread.interrupt();
      (new ThreadCloseConnection(this)).start();
   }

   // $FF: synthetic method
   static boolean isRunning(NetworkManager var0) {
      return var0.isRunning;
   }

   // $FF: synthetic method
   static boolean isServerTerminating(NetworkManager var0) {
      return var0.isServerTerminating;
   }

   // $FF: synthetic method
   static boolean readNetworkPacket(NetworkManager var0) {
      return var0.readPacket();
   }

   // $FF: synthetic method
   static boolean sendNetworkPacket(NetworkManager var0) {
      return var0.sendPacket();
   }

   // $FF: synthetic method
   static DataOutputStream func_28140_f(NetworkManager var0) {
      return var0.socketOutputStream;
   }

   // $FF: synthetic method
   static boolean func_28138_e(NetworkManager var0) {
      return var0.isTerminating;
   }

   // $FF: synthetic method
   static void func_30005_a(NetworkManager var0, Exception var1) {
      var0.onNetworkError(var1);
   }

   // $FF: synthetic method
   static Thread getReadThread(NetworkManager var0) {
      return var0.readThread;
   }

   // $FF: synthetic method
   static Thread getWriteThread(NetworkManager var0) {
      return var0.writeThread;
   }

}
