package org.getspout.spout.gui.server;

import net.minecraft.src.FontRenderer;

import org.bukkit.ChatColor;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.MCRenderDelegate;
import org.getspout.spout.io.CustomTextureManager;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.ListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;
import org.spoutcraft.spoutcraftapi.gui.RenderUtil;

public class ServerItem implements ListWidgetItem {

	protected ListWidget widget;

	protected String ip;
	protected int port;
	protected String title;
	
	protected int databaseId = -1;

	//Options from the serverlist API
	protected String country = null;
	protected boolean whitelisted = false;
	
	protected boolean showPingWhilePolling = false;
	
	protected FavoritesModel favorites = SpoutClient.getInstance().getServerManager().getFavorites();
	protected ServerListModel serverList = SpoutClient.getInstance().getServerManager().getServerList();
	
	protected boolean isFavorite = true;
	
	protected PollResult pollResult;
	
	
	
	public ServerItem(String title, String ip, int port, int dbId) {
		this.ip = ip;
		this.port = port;
		this.title = title;
		this.databaseId = dbId;
		this.pollResult = PollResult.getPoll(ip, port, dbId);
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
		int ping = getPing();
		
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		
		MCRenderDelegate r = (MCRenderDelegate) Spoutcraft.getRenderDelegate();
		
		int margin1 = 0;
		int margin2 = 0;
		
		if(getPing() > 0 && (!isPolling() || showPingWhilePolling)) {
			String sping = getPing() + " ms";
			int pingwidth = font.getStringWidth(sping);
			margin1 = pingwidth + 15;
			font.drawStringWithShadow(sping, width - pingwidth - 20, y + 2, 0xaaaaaa);
			String sPlayers = getPlayers() + " / "+getMaxPlayers() + " players";
			int playerswidth = font.getStringWidth(sPlayers);
			margin2 = playerswidth;
			font.drawStringWithShadow(sPlayers, width - playerswidth - 5, y+11, 0xaaaaaa);
		}
		
		font.drawStringWithShadow(r.getFittingText(title, width - 2 - 10 - margin1), x + 2, y + 2, 0xffffff);
		String sMotd = "";
		if((getPing() == PollResult.PING_POLLING || isPolling()) && !showPingWhilePolling) {
			sMotd = "Polling...";
		} else if(!showPingWhilePolling) {
			switch(getPing()) {
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
		if((getPing() == PollResult.PING_POLLING || isPolling()) && !showPingWhilePolling) {
			Color c1 = new Color(0, 0, 0);
			double darkness = 0;
			long t = System.currentTimeMillis() % 1000;
			darkness = Math.cos(t * 2 * Math.PI / 1000) * 0.2 + 0.2;
			c1.setBlue(1f - (float)darkness);
			color = c1.toInt();
		}
		
		font.drawStringWithShadow(r.getFittingText(sMotd, width - 2 - 10 - margin2), x+2, y + 11, color);
		
		GL11.glColor4f(1f, 1f, 1f, 1f);
		
		//FANCY ICONS!
		int xOffset = 0;
		int yOffset = 0;
		if(isPolling()) {
			xOffset = 1;
			yOffset = (int)(System.currentTimeMillis() / 100L & 7L);
			if(yOffset > 4) {
				yOffset = 8 - yOffset;
			}
		} else {
			xOffset = 0;
			if(ping < 0L) {
				yOffset = 5;
			} else if(ping < 150L) {
				yOffset = 0;
			} else if(ping < 300L) {
				yOffset = 1;
			} else if(ping < 600L) {
				yOffset = 2;
			} else if(ping < 1000L) {
				yOffset = 3;
			} else {
				yOffset = 4;
			}
		}
		SpoutClient.getHandle().renderEngine.bindTexture(SpoutClient.getHandle().renderEngine.getTexture("/gui/icons.png"));
		RenderUtil.drawTexturedModalRectangle(width - 5 - 10, y + 2, 0 + xOffset * 10, 176 + yOffset * 8, 10, 8, 0f);
		if(port != 25565) {
			font.drawStringWithShadow(ip + ":" +port, x+2, y+20, 0xaaaaaa);
		} else {
			font.drawStringWithShadow(ip, x+2, y+20, 0xaaaaaa);
		}
		
		//Icon Drawing
		int iconMargin = 10;
		
		if(isWhitelisted()) {
			Texture lockIcon = CustomTextureManager.getTextureFromJar("/res/lock.png");
			GL11.glPushMatrix();
			GL11.glTranslatef(x + width - iconMargin - 7, y + 20, 0);
			r.drawTexture(lockIcon, 7, 11);
			GL11.glPopMatrix();
			iconMargin += 5 + 7;
		}
		
		if(country != null) {
			String url = "http://servers.getspout.org/images/flags/"+country.toLowerCase()+".png";
			Texture icon = CustomTextureManager.getTextureFromUrl("Spoutcraft", url);
			if(icon != null) {
				GL11.glPushMatrix();
				GL11.glTranslatef(x + width - iconMargin - 16, y + 20, 0);
				r.drawTexture(icon, 16, 11);
				GL11.glPopMatrix();
				iconMargin += 5 + 16;
			} else {
				CustomTextureManager.downloadTexture("Spoutcraft", url);
			}
		}
	}

	public void onClick(int x, int y, boolean doubleClick) {
		if(doubleClick) {
			SpoutClient.getInstance().getServerManager().join(this, isFavorite?favorites.getCurrentGui():serverList.getCurrentGui(), isFavorite?"Favorites":"Server List");
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
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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
	}
	
	public int getPing() {
		return pollResult.getPing();
	}

	public boolean isPolling() {
		return pollResult.isPolling();
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

	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public boolean isWhitelisted() {
		return whitelisted;
	}

	public void setWhitelisted(boolean whitelisted) {
		this.whitelisted = whitelisted;
	}

	public void setShowPingWhilePolling(boolean b) {
		this.showPingWhilePolling = b;
	}
}