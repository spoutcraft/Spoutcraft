package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface SpoutPacket {
	
	/**
	 * The number of bytes of data contained inside of the packet. 
	 * Packet version and other meta-data are excluded, only count bytes read by {@link #readData(DataInputStream)}.
	 * @return Number of bytes
	 */
	public int getNumBytes();
	
	/**
	 * Reads the data from an input stream into member variables.
	 * The number of bytes read must be equal to the result of {@link #getNumBytes()}.
	 * @param input stream to read from
	 * @throws IOException
	 */
	public void readData(DataInputStream input) throws IOException;
	
	/**
	 * Writes the data from the packet to the output stream, to be serialized and sent to a player.
	 * The number of bytes written must be equal to the result of {@link #getNumBytes()}.
	 * @param output stream to write to
	 * @throws IOException
	 */
	public void writeData(DataOutputStream output) throws IOException;
	
	/**
	 * Performs any tasks for the packet after data has been successfully read into the packet.
	 * @param playerId for the packet
	 */
	public void run(int playerId);
	
	/**
	 * Performs any tasks for the packet after the data has NOT been successfully read into the packet.
	 * All values will be at defaults (0, null, etc) and are unsafe. 
	 * failure is run when the packet versions mismatch and data could not be safely read. It may not be called for all cases of failure.
	 * @param playerId
	 */
	public void failure(int playerId);
	
	/**
	 * The type of packet represented. Used to rebuild the correct packet on the client.
	 * @return packet type.
	 */
	public PacketType getPacketType();
	
	/**
	 * Version of the packet this represents. Version numbers should start with 0.
	 * Versions should be incremented any time the member variables or serialization of the packet changes, to prevent crashing.
	 * Mismatched packet versions are discarded, and {@link #failure(int)} is called.
	 * @return
	 */
	public int getVersion();

}