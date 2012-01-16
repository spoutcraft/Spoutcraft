/*
 * This file is part of Spoutcraft (http://spout.org).
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.GuiSpoutScreen;
import org.spoutcraft.client.util.NetworkUtils;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollArea;
import org.spoutcraft.spoutcraftapi.gui.GenericTexture;
import org.yaml.snakeyaml.Yaml;

public class GuiServerInfo extends GuiSpoutScreen {

	private GenericButton buttonDone, buttonOpenBrowser, buttonRefresh, buttonAddFavorite, buttonJoin;
	private GenericLabel labelTitle, labelAddressLabel, labelAddress, labelMotdLabel, labelMotd, labelDescriptionLabel, labelDescription,
	labelPlayersLabel, labelPlayers, labelSpoutcraftLabel, labelSpoutcraft, labelAccessLabel, labelAccess,
	labelMCVersionLabel, labelMCVersion, labelCategoryLabel, labelCategory;
	private LinkButton linkForum, linkSite;
	private GenericScrollArea content;
	private List<GenericLabel> labels = new ArrayList<GenericLabel>();
	int labelWidth = 0;
	private GenericTexture textureIcon;

	static final int MAX_HEIGHT = 128;

	private GuiScreen back;
	private ServerItem item;
	private Thread loadThread;

	public GuiServerInfo(ServerItem item, GuiScreen back) {
		this.back = back;
		this.item = item;
	}

	@Override
	protected void createInstances() {
		labels.clear();
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		buttonDone = new GenericButton("Done");
		getScreen().attachWidget(spoutcraft, buttonDone);

		buttonRefresh = new GenericButton();
		getScreen().attachWidget(spoutcraft, buttonRefresh);
		
		buttonAddFavorite = new GenericButton("Add Favorite");
		getScreen().attachWidget(spoutcraft, buttonAddFavorite);
		
		buttonJoin = new GenericButton("Join");
		getScreen().attachWidget(spoutcraft, buttonJoin);

		content = new GenericScrollArea();
		getScreen().attachWidget(spoutcraft, content);

		labelTitle = new GenericLabel(item.getTitle());
		getScreen().attachWidget(spoutcraft, labelTitle);
		
		buttonOpenBrowser = new GenericButton("More Info...");
		content.attachWidget(spoutcraft, buttonOpenBrowser);

		labelCategoryLabel = new GenericLabel("Category");
		content.attachWidget(spoutcraft, labelCategoryLabel);
		labels.add(labelCategoryLabel);

		labelCategory = new GenericLabel("...");
		labelCategory.setTextColor(new Color(0xffaaaaaa));
		content.attachWidget(spoutcraft, labelCategory);

		labelMCVersionLabel = new GenericLabel("Minecraft Version");
		content.attachWidget(spoutcraft, labelMCVersionLabel);
		labels.add(labelMCVersionLabel);

		labelMCVersion = new GenericLabel("...");
		labelMCVersion.setTextColor(new Color(0xffaaaaaa));
		content.attachWidget(spoutcraft, labelMCVersion);

		labelAccessLabel = new GenericLabel("Access Type");
		content.attachWidget(spoutcraft, labelAccessLabel);
		labels.add(labelAccessLabel);


		String access = "Open";
		switch(item.accessType) {
		case ServerItem.WHITELIST:
			access = "Whitelist";
			break;
		case ServerItem.GRAYLIST:
			access = "Graylist";
			break;
		case ServerItem.BLACKLIST:
			access = "Blacklist";
			break;
		}
		labelAccess = new GenericLabel(access);
		labelAccess.setTextColor(new Color(0xffaaaaaa));
		content.attachWidget(spoutcraft, labelAccess);

		labelAddress = new GenericLabel(item.getIp() + (item.getPort()!=25565?item.getPort():""));
		labelAddress.setTextColor(new Color(0xffaaaaaa));
		content.attachWidget(spoutcraft, labelAddress);

		labelAddressLabel = new GenericLabel("Address");
		content.attachWidget(spoutcraft, labelAddressLabel);
		labels.add(labelAddressLabel);

		labelMotd = new GenericLabel(item.getMotd());
		content.attachWidget(spoutcraft, labelMotd);
		labelMotd.setTextColor(new Color(0xffaaaaaa));

		labelMotdLabel = new GenericLabel("MOTD");
		content.attachWidget(spoutcraft, labelMotdLabel);
		labels.add(labelMotdLabel);

		labelDescription = new GenericLabel("...");
		content.attachWidget(spoutcraft, labelDescription);
		labelDescription.setTextColor(new Color(0xffaaaaaa));
		labelDescription.setWrapLines(true);

		labelDescriptionLabel = new GenericLabel("Description");
		labels.add(labelDescriptionLabel);
		content.attachWidget(spoutcraft, labelDescriptionLabel);

		labelPlayersLabel = new GenericLabel("Players");
		content.attachWidget(spoutcraft, labelPlayersLabel);
		labels.add(labelPlayersLabel);

		labelPlayers = new GenericLabel();
		labelPlayers.setTextColor(new Color(0xffaaaaaa));
		content.attachWidget(spoutcraft, labelPlayers);

		linkForum = new LinkButton("Go to forum", "");
		content.attachWidget(spoutcraft, linkForum);
		linkSite = new LinkButton("Go to website", "");
		content.attachWidget(spoutcraft, linkSite);

		labelSpoutcraftLabel = new GenericLabel("Spoutcraft required");
		content.attachWidget(spoutcraft, labelSpoutcraftLabel);
		labels.add(labelSpoutcraftLabel);

		labelSpoutcraft = new GenericLabel("...");
		labelSpoutcraft.setTextColor(new Color(0xffaaaaaa));
		content.attachWidget(spoutcraft, labelSpoutcraft);

		textureIcon = new GenericTexture("http://servers.spout.org/preview/"+ item.getDatabaseId() +".png");
		textureIcon.setFinishDelegate(new ImageUpdate());
		textureIcon.setWidth(48).setHeight(48);
		content.attachWidget(spoutcraft, textureIcon);

		for(GenericLabel lbl:labels) {
			labelWidth = (int) Math.max(labelWidth, lbl.getTextWidth());
		}
		updateButtons();
		refresh();
		updateData();
	}

	public void updateButtons() {
		linkForum.setEnabled(!linkForum.getUrl().isEmpty());
		linkSite.setEnabled(!linkSite.getUrl().isEmpty());

		boolean updating = loadThread != null;
		buttonRefresh.setEnabled(!updating);
		if(updating) {
			buttonRefresh.setText("Loading...");
		} else {
			buttonRefresh.setText("Refresh");
		}
		
		buttonAddFavorite.setEnabled(!SpoutClient.getInstance().getServerManager().getFavorites().containsSever(item));
		if(!buttonAddFavorite.isEnabled()) {
			buttonAddFavorite.setTooltip("You already have this server in your favorites");
		} else {
			buttonAddFavorite.setTooltip("");
		}
	}

	@Override
	protected void layoutWidgets() {
		int w = Spoutcraft.getMinecraftFont().getTextWidth(labelTitle.getText());

		int totalWidth = Math.min(width - 10 - 16, 200*3+10);
		int cellWidth = (totalWidth - 10)/3;
		int left = width / 2 - totalWidth / 2;
		int center = left + cellWidth + 5;
		int right = center + cellWidth + 5;


		labelTitle.setX(width / 2 - w / 2).setY(5 + 7).setWidth(w).setHeight(11);

		if(labelTitle.getX() + w > width - 110) {
			labelTitle.setX(width - 110 - w);
		}

		buttonRefresh.setX(width - 105).setY(5).setWidth(100).setHeight(20);

		content.setX(0).setY(5+7+11+5).setWidth(width).setHeight(height - 30 - (5+7+11+5));

		int valueLeft = 10 + labelWidth;
		int labelLeft = 5;

		int top = 5;

		textureIcon.setX(5).setY(top);
		updateImageWidth();

		top += textureIcon.getHeight() + 5;

		labelAddressLabel.setX(labelLeft).setY(top).setWidth(width - 10).setHeight(11);
		labelAddress.setX(valueLeft).setY(top).setWidth(width - valueLeft - 5).setHeight(11);

		top += 16;

		labelMotdLabel.setX(labelLeft).setY(top).setWidth(width - 10).setHeight(11);
		labelMotd.setX(valueLeft).setY(top).setWidth(width - valueLeft - 5).setHeight(11);

		top += 16;

		labelPlayersLabel.setX(labelLeft).setY(top).setWidth(width - 10).setHeight(11);
		labelPlayers.setX(valueLeft).setY(top).setWidth(width - valueLeft - 5).setHeight(11);

		top += 16;

		labelSpoutcraftLabel.setX(labelLeft).setY(top).setWidth(width - 10).setHeight(11);
		labelSpoutcraft.setX(valueLeft).setY(top).setWidth(width - valueLeft - 5).setHeight(11);

		top += 16;

		labelAccessLabel.setX(labelLeft).setY(top).setWidth(width - 10).setHeight(11);
		labelAccess.setX(valueLeft).setY(top).setWidth(width - valueLeft - 5).setHeight(11);

		top += 16;

		labelMCVersionLabel.setX(labelLeft).setY(top).setWidth(width - 10).setHeight(11);
		labelMCVersion.setX(valueLeft).setY(top).setWidth(width - valueLeft - 5).setHeight(11);

		top += 16;

		labelCategoryLabel.setX(labelLeft).setY(top).setWidth(width - 10).setHeight(11);
		labelCategory.setX(valueLeft).setY(top).setWidth(width - valueLeft - 5).setHeight(11);

		top += 16;

		labelDescriptionLabel.setX(labelLeft).setY(top).setWidth(width - 10).setHeight(11);
		labelDescription.setX(valueLeft).setY(top).setWidth(width - valueLeft - 5 - 16).setHeight(11);
		labelDescription.recalculateLines();

		top += labelDescription.getHeight() + 5;

		linkForum.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		linkSite.setX(center).setY(top).setWidth(cellWidth).setHeight(20);
		buttonOpenBrowser.setX(right).setY(top).setHeight(20).setWidth(cellWidth);

		top += 25;
		
		buttonJoin.setX(left).setY(height - 25).setHeight(20).setWidth(cellWidth);
		buttonAddFavorite.setX(center).setY(height - 25).setHeight(20).setWidth(cellWidth);
		buttonDone.setX(right).setY(height - 25).setHeight(20).setWidth(cellWidth);

		content.updateInnerSize();
	}

	@Override
	protected void buttonClicked(Button btn) {
		if(btn == buttonDone) {
			mc.displayGuiScreen(back);
		}
		if(btn == buttonOpenBrowser) {
			NetworkUtils.openInBrowser("http://servers.spout.org/info/" + item.getDatabaseId());
		}
		if(btn == buttonRefresh) {
			refresh();
		}
		if(btn == buttonAddFavorite) {
			SpoutClient.getInstance().getServerManager().getFavorites().addServer(item);
			SpoutClient.getInstance().getServerManager().getFavorites().save();
			updateButtons();
		}
		if(btn == buttonJoin) {
			item.onClick(-1, -1, true);
		}
	}

	private void refresh() {
		loadThread = new Thread() {
			public void run() {
				try {
					URL url = new URL("http://servers.spout.org/api2.php?id=" + item.getDatabaseId());
					BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
					Yaml yaml = new Yaml();
					ArrayList<Map<String, String>> list = (ArrayList<Map<String, String>>) yaml.load(reader);
					reader.close();
					parseYaml(list);
				} catch(IOException e) {
					e.printStackTrace();
					parseYaml(null);
				}
			}
		};
		loadThread.start();
		updateButtons();
	}

	protected void parseYaml(ArrayList<Map<String, String>> list) {
		loadThread = null;
		updateButtons();
		try {
			if(list != null && list.size() > 0) {
				Map<String, String> i = list.get(1);
				labelDescription.setText(URLDecoder.decode(i.get("longdescription"), "UTF-8"));
				linkSite.setUrl(URLDecoder.decode(i.get("site"), "UTF-8"));
				linkForum.setUrl(URLDecoder.decode(i.get("forumurl"), "UTF-8"));
				boolean spoutcraft = i.get("spoutcraft").equals("1");
				labelSpoutcraft.setText(spoutcraft?"Yes":"No");
				labelMCVersion.setText(URLDecoder.decode(i.get("mcversion"), "UTF-8"));
				labelCategory.setText(URLDecoder.decode(i.get("category"), "UTF-8"));
			}
		} catch(UnsupportedEncodingException e){}
		layoutWidgets();
		updateButtons();
	}

	@Override
	public void updateScreen() {
		if(loadThread != null) {
			Color color = new Color(0, 1f, 0);
			double darkness = 0;
			long t = System.currentTimeMillis() % 1000;
			darkness = Math.cos(t * 2 * Math.PI / 1000) * 0.2 + 0.2;
			color.setGreen(1f - (float)darkness);
			buttonRefresh.setDisabledColor(color);
		}
		super.updateScreen();
	}

	public void updateData() {
		labelMotd.setText(item.getMotd());
		labelPlayers.setText(item.getPlayers() + " / "+item.getMaxPlayers());
	}

	private class ImageUpdate implements Runnable {

		public void run() {
			updateImageWidth();
			layoutWidgets();
		}
	}

	public void updateImageWidth() {
		int imgwidth, imgheight;
		int MAX_WIDTH = width - 10 - 16;
		imgwidth = textureIcon.getOriginalWidth();
		imgheight = textureIcon.getOriginalHeight();

		System.out.println(imgwidth+"x"+imgheight);

		double ratio = (double) imgwidth / (double) imgheight;

		if(imgheight > MAX_HEIGHT) {
			imgheight = MAX_HEIGHT;
			imgwidth = (int) ((double) imgheight * ratio);
		}
		
		if(imgwidth > MAX_WIDTH) {
			imgwidth = MAX_WIDTH;
			imgheight = (int) ((double) imgwidth * (1.0/ratio));
		}

		textureIcon.setWidth(imgwidth).setHeight(imgheight);
	}
}