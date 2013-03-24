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
package org.spoutcraft.client.gui.about;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.yaml.snakeyaml.Yaml;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericScrollArea;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.api.gui.WidgetAnchor;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.ClientTexture;
import org.spoutcraft.client.gui.GuiSpoutScreen;
import org.spoutcraft.client.gui.mainmenu.MainMenu;

public class GuiNewAbout extends GuiSpoutScreen {
	private GuiScreen parent;
	private GenericButton buttonDone;
	private GenericLabel title, labelSpoutcraftVersion, labelMinecraftVersion;
	private GenericScrollArea scroll;
	private List<List<Section>> columns = new LinkedList<List<Section>>();
	private int sectionMargin = 20, columnMargin = 20;
	private ClientTexture textureSpoutcraft, textureMinecraft;
	private static HashMap<String, Object> root;

	static {
		updateRoot();
	}

	private static void updateRoot() {
		try {
			root = (HashMap<String, Object>) (new Yaml()).load((new URL("http://get.spout.org/about.yml")).openStream());
		} catch (Exception ex) {
			Logger.getLogger(GuiNewAbout.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public GuiNewAbout(GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	protected void createInstances() {
		title = new GenericLabel("About");
		buttonDone = new GenericButton("Main Menu");
		scroll = new GenericScrollArea();
		labelSpoutcraftVersion = new GenericLabel(SpoutClient.getClientVersion() + "\nLicensed under LGPLv3");
		labelMinecraftVersion = new GenericLabel(MainMenu.mcVersion + "\nCopyright Mojang AB" );
		labelSpoutcraftVersion.setAlign(WidgetAnchor.TOP_RIGHT);
		textureSpoutcraft = new ClientTexture("/res/logo/spoutcraft.png");
		textureMinecraft = new ClientTexture("/res/logo/minecraft.png");

		getScreen().attachWidgets("Spoutcraft", title, buttonDone, scroll, labelMinecraftVersion, labelSpoutcraftVersion, textureMinecraft, textureSpoutcraft);

		load();
	}

	@SuppressWarnings("unchecked")
	public void load() {
		scroll.removeWidgets("Spoutcraft");
		try {
			if (root.containsKey("options")) {
				HashMap<String, Object> options = (HashMap<String, Object>) root
						.get("options");
				if (options.containsKey("section-margin")) {
					sectionMargin = (Integer) options.get("section-margin");
				}
				if (options.containsKey("column-margin")) {
					columnMargin = (Integer) options.get("column-margin");
				}
			}
			if (root.containsKey("columns")) {
				LinkedHashMap<String, Object> columns = (LinkedHashMap<String, Object>) root
						.get("columns");
				for (Object col : columns.values()) {
					List<Section> sections = new LinkedList<Section>();
					LinkedHashMap<String, Object> secs = (LinkedHashMap<String, Object>) col;
					for (Object sec : secs.values()) {
						LinkedHashMap<String, Object> section = (LinkedHashMap<String, Object>) sec;
						String title = "Untitled";
						if (section.containsKey("title")) {
							title = (String) section.get("title");
						}
						Set<String> keys = section.keySet();
						if (keys.size() > 2) {
							continue;
						}
						String sectionType = "";
						for (String key : keys) {
							if (!key.equals("title")) {
								sectionType = key;
							}
						}
						Section sectionObject = Section.getSection(sectionType);
						if (sectionObject == null) {
							continue;
						}
						sectionObject.init(this, title, section.get(sectionType));
						scroll.attachWidgets("Spoutcraft", sectionObject
								.getWidgets().toArray(new Widget[0]));
						sections.add(sectionObject);
					}
					this.columns.add(sections);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			layoutWidgets();
		}
	}

	@Override
	protected void layoutWidgets() {
		int top = 10;

		int swidth = mc.fontRenderer.getStringWidth(title.getText());
		title.setY(top).setX(width / 2 - swidth / 2).setHeight(11)
				.setWidth(swidth);

		int viewheight = height - title.getY() - 16 - 53;

		scroll.setGeometry(5, title.getY() + 16, width - 10, viewheight);

		textureSpoutcraft.setGeometry(width - 133, height - 48, 128, 32);
		textureMinecraft.setGeometry(5, height - 46, 128, 20);

		labelMinecraftVersion.setGeometry(5, height - 25, width - (width / 2 - 50) - 5, 21);
		labelSpoutcraftVersion.setGeometry(width - 5, height - 25, width - (width / 2 + 55), 21);
		buttonDone.setGeometry(width / 2 - 50, height - 25, 100, 20);

		int columnCount = columns.size();
		if (columnCount > 0) {
			int columnWidth = (width - 10 - 16 - (columnCount + 1)
					* columnMargin)
					/ (columnCount);
			int columnX = columnMargin;
			Iterator<List<Section>> iter = columns.iterator();
			while (iter.hasNext()) {
				List<Section> column = iter.next();
				int sectionY = columnMargin;
				for (Section section : column) {
					section.setX(columnX);
					section.setY(sectionY);
					section.setWidth(columnWidth);
					sectionY += section.getHeight() + sectionMargin;
				}
				columnX += columnWidth + columnMargin;
			}
			scroll.updateInnerSize();
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	@Override
	protected void buttonClicked(Button btn) {
		if (btn == buttonDone) {
			mc.displayGuiScreen(parent);
		}
	}

	public void update() {
		layoutWidgets();
	}
}
