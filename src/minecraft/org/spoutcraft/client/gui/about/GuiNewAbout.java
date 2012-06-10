package org.spoutcraft.client.gui.about;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.src.GuiScreen;

import org.spoutcraft.client.gui.GuiSpoutScreen;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollArea;
import org.spoutcraft.spoutcraftapi.gui.Widget;
import org.yaml.snakeyaml.Yaml;

public class GuiNewAbout extends GuiSpoutScreen {
	private GuiScreen parent;
	private GenericButton buttonDone;
	private GenericLabel title;
	private GenericScrollArea scroll;
	private List<List<Section>> columns = new LinkedList<List<Section>>();
	private int sectionMargin = 20, columnMargin = 20;

	public GuiNewAbout(GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	protected void createInstances() {
		title = new GenericLabel("About");
		buttonDone = new GenericButton("Main Menu");
		scroll = new GenericScrollArea();

		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		getScreen().attachWidgets(spoutcraft, title, buttonDone, scroll);

		Thread load = new Thread() {
			@Override
			public void run() {
				try {
					load((new URL("http://get.spout.org/about.yml"))
							.openStream());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		load.start();
	}

	@SuppressWarnings("unchecked")
	public void load(InputStream input) {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		scroll.removeWidgets(spoutcraft);
		try {
			Yaml yaml = new Yaml();
			HashMap<String, Object> root = (HashMap<String, Object>) yaml
					.load(input);
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
					System.out.println("Column");
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
						scroll.attachWidgets(spoutcraft, sectionObject
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

		scroll.setGeometry(5, title.getY() + 16, width - 10,
				height - title.getY() - 16 - 30);

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
	protected void buttonClicked(Button btn) {
		if (btn == buttonDone) {
			mc.displayGuiScreen(parent);
		}
	}

	public void update() {
		layoutWidgets();
	}

}
