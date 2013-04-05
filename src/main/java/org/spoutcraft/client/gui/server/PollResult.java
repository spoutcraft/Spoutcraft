/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
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
package org.spoutcraft.client.gui.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import gnu.trove.map.hash.TIntObjectHashMap;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.MirrorUtils;
import org.spoutcraft.client.util.NetworkUtils;

public class PollResult {
	protected int ping;
	protected int players;
	protected int maxPlayers;
	protected String motd = "";
	protected int protocolVersion;
	protected String version;
	protected String ip;
	protected int port;

	protected int databaseId = -1;

	public static final int PING_POLLING = -1;
	public static final int PING_UNKNOWN = -2;
	public static final int PING_TIMEOUT = -3;
	public static final int PING_BAD_MESSAGE = -4;
	protected boolean polling = false;
	protected long pollStart;
	protected static volatile int numPolling = 0;
	private static final int maxPollingThreads;

	protected PollThread currentThread;

	protected ServerModel favorites = SpoutClient.getInstance().getServerManager().getFavorites();
	protected ServerListModel serverList = SpoutClient.getInstance().getServerManager().getServerList();

	protected static TIntObjectHashMap<PollResult> recentResults = new TIntObjectHashMap<PollResult>();
	protected static Thread send = null;
	private boolean sent = false;

	static {
		maxPollingThreads = 5 + (5 * Runtime.getRuntime().availableProcessors());
	}

	protected PollResult(String ip, int port, int uid) {
		setIp(ip);
		setPort(port);
		setDatabaseId(uid);
	}

	public int getPing() {
		return ping;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

	public int getPlayers() {
		return players;
	}

	public void setPlayers(int players) {
		this.players = players;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void poll() {
		if (!isPolling()) {
			currentThread = new PollThread();
			currentThread.start();
		}
	}

	public static PollResult getPoll(String ip, int port, int uid) {
		String hash = ip + ":" + port;
		PollResult result = recentResults.get(hash.hashCode());
		if (result == null) {
			result = new PollResult(ip, port, uid);
			recentResults.put(hash.hashCode(), result);
			result.poll();
		}
		return result;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getMotd() {
		return motd;
	}

	public void setMotd(String motd) {
		this.motd = motd;
	}

	public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}

	public boolean isPolling() {
		return polling;
	}

	public void endPolling() {
		if (polling) {
			polling = false;
			numPolling--;
		}
	}

	protected class PollThread extends Thread {
		@Override
		public void run() {
			while (numPolling >= maxPollingThreads) {
				try {
					sleep(10);
				} catch (InterruptedException e) {}
			}
			numPolling ++;
			polling = true;
			favorites.setPolling(true);
			Socket sock = null;
			DataInputStream input = null;
			DataOutputStream output = null;
			try {
				long start = System.currentTimeMillis();
				sock = new Socket();
				sock.setSoTimeout(10000);
				InetSocketAddress address = NetworkUtils.resolve(ip, port);
				if (address.isUnresolved()) {
					ping = PING_UNKNOWN;
					return;
				}
				sock.connect(address, 10000);
				sock.setTcpNoDelay(true);
				sock.setTrafficClass(18);

				input = new DataInputStream(sock.getInputStream());
				output = new DataOutputStream(sock.getOutputStream());

				// Packet ID is 254
				output.write(254);
				// Writes 1 for getting extended server information since 1.4
				output.writeByte(1);

				// Server will return a packet 255 with the data as string
				if (input.read() != 255) {
					ping = PING_BAD_MESSAGE;
					return;
				}

				StringBuilder builder = new StringBuilder();
				short size = input.readShort();
				for (int i = 0; i < size; i++) {
					builder.append(input.readChar());
				}

				long end = System.currentTimeMillis();
				String sPacket = builder.toString();
				ping = (int) (end - start);

				// Check if we have new format here (1.4), and fall back to old format if not
				if (sPacket.startsWith("\u00a71")) {
					String split[] = sPacket.split("\u0000");
					protocolVersion = Integer.valueOf(split[1]);
					version = split[2];
					synchronized (motd) {
						motd = split[3];
					}
					players = Integer.valueOf(split[4]);
					maxPlayers = Integer.valueOf(split[5]);
				}
				else {
					String split[] = sPacket.split("\u00a7");
					synchronized (motd) {
						motd = split[0];
					}
					players = Integer.valueOf(split[1]);
					maxPlayers = Integer.valueOf(split[2]);
				}
			} catch(java.net.UnknownHostException e) {
				ping = PING_UNKNOWN;
			} catch(IOException e) {
				ping = PING_TIMEOUT;
			} catch (Exception e) {
				ping = PING_BAD_MESSAGE;
			} finally {
				polling = false;
				numPolling--;
				if (numPolling == 0) {
					favorites.setPolling(false);
				}
				if (SpoutClient.getHandle().currentScreen instanceof GuiServerInfo) {
					GuiServerInfo screen = (GuiServerInfo) SpoutClient.getHandle().currentScreen;
					screen.updateData();
				}
				sendDCData();
				try {
					sock.close();
					input.close();
					output.close();
				} catch(Exception e) {}
			}
		}
	}

	/**
	 * Sends ping, playercount and maximum players to the server list database.
	 * No personal data (aside your IP) will be transferred and the IP won't be
	 * saved. Sending this data to the server makes for a more accurate ping calculation
	 * and will give you better search results when you order by ping. Our server then
	 * doesn't have to ping all the servers on its own.
	 */
	protected static void sendDCData() {
		if (send != null) {
			return;
		}
		send = new Thread() {
			public void run() {
				while (numPolling > 0) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						return;
					}
				}
				String api = MirrorUtils.getMirrorUrl("/senddata.php", "http://servers.spout.org/senddata.php");
				String json = "{";
				int res = 0;
				for (PollResult result:recentResults.valueCollection()) {
					if (result.databaseId != -1 && !result.sent) {
						int ping = result.ping > 0 ? result.ping : -1;
						if (res > 0) {
							json += ",";
						}
						json +="\"" + res + "\":{";

						json += keyValue("uid", result.databaseId) + ",";
						json += keyValue("ping", ping) + ",";
						json += keyValue("players", result.players) + ",";
						json += keyValue("maxplayers", result.maxPlayers);
						json +="}";
						result.sent = true;
						res ++;
					}
				}
				json += "}";

				if (res > 0) {
					URL url;
					try {
						url = new URL(api);
					} catch (MalformedURLException e) {
						return;
					}
					try {
						String data = URLEncoder.encode("json", "UTF-8") + "=" + URLEncoder.encode(json, "UTF-8");
						URLConnection conn = url.openConnection();
						conn.setDoOutput(true);
						OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
						wr.write(data);
						wr.flush();

						BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						while (rd.readLine() != null) {
						}
						wr.close();
						rd.close();
					} catch (IOException e) {
					}

				}
				send = null;
			}

			public String keyValue(String key, Object value) {
				if (value instanceof Number) {
					return "\"" + key + "\":" + value;
				} else {
					return "\"" + key + "\":\"" + value + "\"";
				}
			}
		};
		send.start();
	}
}
