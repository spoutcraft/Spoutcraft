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

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import net.minecraft.src.FontRenderer;

import org.bukkit.ChatColor;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.ListWidget;
import org.spoutcraft.api.gui.ListWidgetItem;
import org.spoutcraft.api.gui.RenderUtil;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.MCRenderDelegate;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.io.MirrorUtils;
import org.spoutcraft.client.util.NetworkUtils;

public class ServerItem implements ListWidgetItem {
	/**
	 * The default Minecraft server port.
	 */
	public static final int DEFAULT_PORT = 25565;

	protected ListWidget widget;

	protected String ip;
	protected int port;
	protected String title;

	protected int databaseId = -1;

	protected Boolean acceptsTextures;

	// Options from the serverlist API
	protected String country = null;

	public static final int OPEN = 0;
	public static final int WHITELIST = 1;
	public static final int GRAYLIST = 2;
	public static final int BLACKLIST = 3;
	public int versionWidth = 10;

	protected byte accessType = ServerItem.OPEN;

	protected boolean showPingWhilePolling = false;

	protected ServerModel favorites = SpoutClient.getInstance().getServerManager().getFavorites();
	protected ServerListModel serverList = SpoutClient.getInstance().getServerManager().getServerList();

	protected boolean isFavorite = true;

	protected PollResult pollResult;

	private static final String latestMC = "1.4.7";
	protected String mcversion = latestMC;
	public boolean showPing = false;

	public ServerItem clone() {
		ServerItem clone = new ServerItem(getTitle(), getIp(), getPort(), getDatabaseId(),  mcversion);
		return clone;
	}

	public void setShowPing(boolean showPing) {
		this.showPing = showPing;
	}

	public boolean isShowPing() {
		return showPing;
	}

	public void update(ServerItem other) {
		this.ip = other.ip;
		this.port = other.port;
		this.title = other.title;
		this.databaseId = other.databaseId;
		this.pollResult = PollResult.getPoll(ip, port, databaseId);
	}

	public ServerItem(String title, String ip, int port, int dbId) {
		this(title, ip, port, dbId, latestMC);
	}

	public ServerItem(String title, String ip, int port, int dbId, String version) {
		this.ip = ip;
		this.port = port;
		this.title = title;
		this.databaseId = dbId;
		this.pollResult = PollResult.getPoll(ip, port, dbId);
		mcversion = version;
	}

	public void setFavorite(boolean b) {
		isFavorite = b;
	}

	public void setListWidget(ListWidget widget) {
		this.widget = widget;
	}

	public ListWidget getListWidget() {
		return widget;
	}

	public int getHeight() {
		return 33;
	}

	public void render(int x, int y, int width, int height) {
		MCRenderDelegate r = (MCRenderDelegate) Spoutcraft.getRenderDelegate();
		if (databaseId != -1) {
			String iconUrl = "http://cdn.spout.org/server/thumb/" + databaseId + ".png";
			Texture icon = CustomTextureManager.getTextureFromUrl("Spoutcraft", iconUrl);
			if (icon == null) {
				CustomTextureManager.downloadTexture("Spoutcraft", iconUrl, true);
				icon = CustomTextureManager.getTextureFromJar("/res/icon/unknown_server.png");
			}
			GL11.glPushMatrix();
			GL11.glTranslated(x + 2, y + 2, 0);
			r.drawTexture(icon, 25, 25);
			GL11.glPopMatrix();
		}

		int marginleft = 29;

		int ping = getPing();

		FontRenderer font = SpoutClient.getHandle().fontRenderer;

		int margin1 = 0;
		int margin2 = 0;

		if (getPing() > 0 && (!isPolling() || showPingWhilePolling)) {
			if (isShowPing()) {
				String sping = getPing() + " ms";
				int pingwidth = font.getStringWidth(sping);
				margin1 = pingwidth + 14;
				font.drawStringWithShadow(sping, x + width - pingwidth - 14, y + 2, 0xaaaaaa);
			}
			String sPlayers = getPlayers() + " / " + getMaxPlayers() + " players";
			int playerswidth = font.getStringWidth(sPlayers);
			margin2 = playerswidth;
			font.drawStringWithShadow(sPlayers, x + width - playerswidth - 2, y + 11, 0xaaaaaa);
		}

		font.drawStringWithShadow(r.getFittingText(title, width - margin1 - marginleft), x + marginleft, y + 2, 0xffffff);
		String sMotd = "";
		if ((getPing() == PollResult.PING_POLLING || isPolling()) && !showPingWhilePolling) {
			sMotd = showPing ? "Polling ..." : "Retrieving MOTD";
		} else if (!showPingWhilePolling) {
			switch (getPing()) {
				case PollResult.PING_UNKNOWN:
					sMotd = ChatColor.RED + "Unknown Host!";
					break;
				case PollResult.PING_TIMEOUT:
					sMotd = ChatColor.RED + "Operation timed out!";
					break;
				case PollResult.PING_BAD_MESSAGE:
					sMotd = ChatColor.RED + "Bad Message (Server version likely outdated)!";
					break;
				default:
					sMotd = ChatColor.GREEN + getMotd();
					break;
			}
		}

		int color = 0xffffff;
		if ((getPing() == PollResult.PING_POLLING || isPolling()) && !showPingWhilePolling) {
			Color c1 = new Color(0, 0, 0);
			double darkness = 0;
			long t = System.currentTimeMillis() % 1000;
			darkness = Math.cos(t * 2 * Math.PI / 1000) * 0.2 + 0.2;
			c1.setBlue(1f - (float)darkness);
			color = c1.toInt();
		}

		font.drawStringWithShadow(r.getFittingText(sMotd, width - 10 - margin2 - marginleft), x + marginleft, y + 11, color);

		GL11.glColor4f(1f, 1f, 1f, 1f);

		// Fancy icons
		int xOffset = 0;
		int yOffset = 0;
		if (isPolling() && isShowPing()) {
			xOffset = 1;
			yOffset = (int)(System.currentTimeMillis() / 100L & 7L);
			if (yOffset > 4) {
				yOffset = 8 - yOffset;
			}
		} else if (isShowPing()) {
			xOffset = 0;
			if (ping < 0L) {
				yOffset = 5;
			} else if (ping < 150L) {
				yOffset = 0;
			} else if (ping < 300L) {
				yOffset = 1;
			} else if (ping < 600L) {
				yOffset = 2;
			} else if (ping < 1000L) {
				yOffset = 3;
			} else {
				yOffset = 4;
			}
		}
		if (isShowPing()) {
			SpoutClient.getHandle().renderEngine.bindTexture("/gui/icons.png");
			RenderUtil.drawTexturedModalRectangle(x + width - 2 - 10, y + 2, 0 + xOffset * 10, 176 + yOffset * 8, 10, 8, 0f);
		}
		if (port != DEFAULT_PORT) {
			font.drawStringWithShadow(ip + ":" +port, x+marginleft, y+20, 0xaaaaaa);
		} else {
			font.drawStringWithShadow(ip, x+marginleft, y+20, 0xaaaaaa);
		}

		// Icon drawing
		int iconMargin = 2;

		if (country != null) {
			String url = "http://cdn.spout.org/img/flag/" + country.toLowerCase() + ".png";
			Texture icon = CustomTextureManager.getTextureFromUrl("Spoutcraft", url);
			if (icon != null) {
				GL11.glPushMatrix();
				GL11.glTranslatef(x + width - iconMargin - 16, y + 20, 0);
				r.drawTexture(icon, 16, 11);
				GL11.glPopMatrix();
				iconMargin += 5 + 16;
			} else {
				CustomTextureManager.downloadTexture("Spoutcraft", url);
			}
		}

		if (accessType != OPEN) {
			String name = "lock";
			switch(accessType) {
			case WHITELIST:
				name = "whitelist";
				break;
			case GRAYLIST:
				name = "graylist";
				break;
			case BLACKLIST:
				name = "blacklist";
				break;
			}
			Texture lockIcon = CustomTextureManager.getTextureFromJar("/res/" + name + ".png");
			GL11.glPushMatrix();
			GL11.glTranslatef(x + width - iconMargin - 7, y + 20, 0);
			r.drawTexture(lockIcon, 7, 11);
			GL11.glPopMatrix();
			iconMargin += 5 + 7;
		}

		if (pollResult.getVersion() != null) {
			GL11.glPushMatrix();
			versionWidth = font.getStringWidth("1.0.0");
				if (isCompatible(SpoutClient.spoutcraftVersion)) {
					font.drawStringWithShadow(pollResult.getVersion(), x + width - versionWidth - 20, y + 21, 0x00FF00);
				} else {
					font.drawStringWithShadow(pollResult.getVersion(), x + width - versionWidth - 20, y + 21, 0xF44607);
				}
			GL11.glPopMatrix();
		} else {
			GL11.glPushMatrix();
			versionWidth = font.getStringWidth("Unknown");
			font.drawStringWithShadow("Unknown", x + width - versionWidth - 20, y + 21, 0xFF0000);
			GL11.glPopMatrix();
		}
	}

	public void onClick(int x, int y, boolean doubleClick) {
		if (doubleClick) {
			if (isCompatible(SpoutClient.spoutcraftVersion)) {
				if (databaseId != -1) {
					String url = MirrorUtils.getMirrorUrl("/popular.php?uid=", "http://servers.spout.org/popular.php?uid=");
					NetworkUtils.pingUrl(url + databaseId);
				}
				SpoutClient.getInstance().getServerManager().join(this, isFavorite?favorites.getCurrentGui():serverList.getCurrentGui(), isFavorite?"Favorites":"Server List");
			}
		}
	}

	public void poll() {
		pollResult.poll();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
		this.pollResult = PollResult.getPoll(ip, port, databaseId); // Force poll update
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
		this.pollResult = PollResult.getPoll(ip, port, databaseId); // Force poll update
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
		this.pollResult = PollResult.getPoll(ip, port, databaseId); // Force poll update
	}

	public int getPing() {
		return pollResult.getPing();
	}

	public boolean isPolling() {
		return pollResult.isPolling();
	}

	public void endPolling() {
		pollResult.endPolling();
	}

	public String getMotd() {
		return pollResult.getMotd();
	}

	public int getPlayers() {
		return pollResult.getPlayers();
	}

	public int getMaxPlayers() {
		return pollResult.getMaxPlayers();
	}

	public String getVersion() {
		String version = pollResult.getVersion();
		if (version != null) {
			return version;
		} 
		return "0.0.0";				
	}
	
	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return this.country;
	}

	public boolean isWhitelisted() {
		return accessType == ServerItem.WHITELIST;
	}

	public boolean isGraylisted() {
		return accessType == ServerItem.GRAYLIST;
	}

	public boolean isBlacklisted() {
		return accessType == ServerItem.BLACKLIST;
	}

	public boolean isOpen() {
		return accessType == ServerItem.OPEN;
	}

	public void setAccessType(byte access) {
		this.accessType = access;
	}

	public void setShowPingWhilePolling(boolean b) {
		this.showPingWhilePolling = b;
	}

	public Boolean getAcceptsTextures() {
		return this.acceptsTextures;
	}

	public void setAcceptsTextures(boolean acceptsTextures) {
		this.acceptsTextures = acceptsTextures;
	}
	
	public boolean isCompatible(String version) {
		// Update the following method to allow users to login to server based on conditional versioning response.
		if (version.equals("1.5.1")) {
			if (getVersion().equals("1.5")) {
				return true;
			}
			if (getVersion().equals("1.5.1")) {
				return true;
			}
		}
		return false;
	}
}
