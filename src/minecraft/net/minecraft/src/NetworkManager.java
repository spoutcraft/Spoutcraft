package net.minecraft.src;

import java.net.SocketAddress;

public interface NetworkManager {
	void func_74425_a(NetHandler var1);

	/**
	 * Adds the packet to the correct send queue (chunk data packets go to a separate queue).
	 */
	void addToSendQueue(Packet var1);

	void func_74427_a();

	/**
	 * Checks timeouts and processes all pending read packets.
	 */
	void processReadPackets();

	SocketAddress func_74430_c();

	/**
	 * Shuts down the server. (Only actually used on the server)
	 */
	void serverShutdown();

	int func_74426_e();

	/**
	 * Shuts down the network with the specified reason. Closes all streams and sockets, spawns NetworkMasterThread to stop
	 * reading and writing threads.
	 */
	void networkShutdown(String var1, Object ... var2);

	void func_74431_f();
}
