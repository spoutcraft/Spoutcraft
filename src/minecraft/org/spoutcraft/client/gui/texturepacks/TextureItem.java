/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.gui.texturepacks;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import net.minecraft.src.FontRenderer;

import org.bukkit.ChatColor;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.MCRenderDelegate;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.io.FileUtil;
import org.spoutcraft.spoutcraftapi.gui.ListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;

public class TextureItem implements ListWidgetItem {
	private int id;
	private int resolution;
	private String name;
	private String description;
	private String author;
	private String forumlink;
	private ListWidget widget;
	private Download download = null;
	private String downloadFail = null;
	private boolean installed = false;
	private long size;
	private static HashMap<Integer, Download> downloads = new HashMap<Integer, TextureItem.Download>();
	
	public static void registerDownload(int id, Download download) {
		downloads.put(id, download);
	}
	
	public static void unregisterDownload(int id) {
		downloads.remove(id);
	}
	
	public static Download getDownload(int id) {
		return downloads.get(id);
	}

	public void setListWidget(ListWidget widget) {
		this.widget = widget;
	}

	public ListWidget getListWidget() {
		return this.widget;
	}

	public int getHeight() {
		return 29;
	}

	public void render(int x, int y, int width, int height) {
		MCRenderDelegate r = (MCRenderDelegate) SpoutClient.getInstance().getRenderDelegate();
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		font.drawStringWithShadow(getName(), x+29, y+2, 0xffffffff);
		font.drawStringWithShadow("by "+ChatColor.WHITE + getAuthor(), x+29, y+11, 0xffaaaaaa);

		String sResolution = resolution + "x";
		int sWidth = font.getStringWidth(sResolution);
		font.drawStringWithShadow(sResolution, x + width - sWidth - 2, y+2, 0xffaaaaaa);
		String desc = r.getFittingText(getDescription(), width - 2 - 29);
		font.drawStringWithShadow(desc, x + 29, y + 20, 0xffaaaaaa);
		String sStatus = "";
		if (size > 1024*1024*9000) {
			sStatus = ChatColor.RED+"It's over 9000! ";
		}
		if (size > 1024*1024) {
			sStatus += size / (1024*1024) + " MB";
		} else if (size > 1024) {
			sStatus = size / 1024 + " KB";
		} else {
			sStatus = size + " Bytes";
		}
		if (isDownloading()) {
			sStatus = "Downloading: "+ChatColor.WHITE + download.getProgress() + "%";
		}
		if (downloadFail != null) {
			sStatus = downloadFail;
		}
		if (installed) {
			sStatus = ChatColor.GREEN + "Installed";
		}
		if (sStatus != null) {
			sWidth = font.getStringWidth(sStatus);
			font.drawStringWithShadow(sStatus, x+width-sWidth-2, y+11, 0xffaaaaaa);
		}

		String iconUrl = getIconUrl();
		Texture icon = CustomTextureManager.getTextureFromUrl(iconUrl);
		if (icon == null) {
			CustomTextureManager.downloadTexture(iconUrl, true);
		} else {
			GL11.glPushMatrix();
			GL11.glTranslated(x + 2, y + 2, 0);
			r.drawTexture(icon, 25, 25);
			GL11.glPopMatrix();
		}
	}

	public void onClick(int x, int y, boolean doubleClick) {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		download = getDownload(id);
	}

	public int getResolution() {
		return resolution;
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getForumlink() {
		return forumlink;
	}

	public void setForumlink(String forumlink) {
		this.forumlink = forumlink;
	}

	public void updateInstalled() {
		installed = (new File(SpoutClient.getInstance().getTexturePackFolder(), getFileName()).exists());
	}

	public boolean isInstalled() {
		return installed;
	}

	protected String getFileName() {
		return getName() + ".id_" + getId() + ".zip";
	}

	public void download() {
		if (download == null && !installed) {
			downloadFail = null;
			boolean wasSandboxed = SpoutClient.isSandboxed();
			if (wasSandboxed) SpoutClient.disableSandbox();
			download = new Download(this);
			download.start();
			if (wasSandboxed) SpoutClient.enableSandbox();
		}
	}

	public boolean isDownloading() {
		return download != null;
	}

	protected class Download extends Thread {
		private int progress;
		private String fileName;
		private File folder;
		private URL url;
		private TextureItem item;

		public Download(TextureItem item) {
			this.item = item;
		}

		public void run() {
			try {
				fileName = item.getFileName();
				folder = SpoutClient.getInstance().getTexturePackFolder();
				url = new URL("http://static.spout.org/texture/"+item.getId()+".zip");
				File temp = new File(FileUtil.getTempDirectory(), FileUtil.getFileName(url.toString()));
				URLConnection conn = url.openConnection();
				conn.setReadTimeout(10000);

				FileOutputStream fos = new FileOutputStream(temp);
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				InputStream in = conn.getInputStream();

				long length = conn.getContentLength();
				int bytes;
				long totalBytes = 0;
				long last = 0;
				final byte[] buffer = new byte[1024*1024];

				long step = Math.max(1024*1024, length / 8);

				while ((bytes = in.read(buffer)) >= 0) {
					bos.write(buffer, 0, bytes);
					totalBytes += bytes;
					progress = (int) (((double)totalBytes / (double)length) * 100);
					if (length > 0 && totalBytes > (last + step)) {
						last = totalBytes;
						long mb = totalBytes/(1024*1024);
						System.out.println("Downloading: " + url + " " + mb + "MB/" + (length/(1024*1024)));
					}
					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {

					}
				}
				in.close();
				bos.close();

				FileUtils.moveFile(temp, new File(folder, fileName));

			} catch(MalformedURLException e) {
			} catch (IOException e) {
				downloadFail = ChatColor.RED + e.getClass().getSimpleName();
				e.printStackTrace();
			} finally {
				download = null;
				updateInstalled();
			}
		}

		public synchronized int getProgress() {
			return progress;
		}
	}

	public String getIconUrl() {
		return "http://static.spout.org/texture/thumb/"+getId()+".png";
	}
}
