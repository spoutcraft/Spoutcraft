package org.getspout.spout.gui.texturepacks;

import net.minecraft.src.GuiMainMenu;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.database.GuiAPIDisplay;
import org.getspout.spout.gui.database.RandomButton;
import org.getspout.spout.gui.database.SortButton;
import org.lwjgl.Sys;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericListView;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollArea;
import org.spoutcraft.spoutcraftapi.gui.Label;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;
import org.spoutcraft.spoutcraftapi.gui.Orientation;
import org.spoutcraft.spoutcraftapi.gui.Widget;

public class GuiTexturePacksDatabase extends GuiAPIDisplay {

	private Label screenTitle, sortFilterTitle;
	private Button buttonMainMenu, buttonLocal, buttonDownload, buttonAdd, buttonRefresh, buttonForum;
	private boolean instancesCreated = false;
	private GenericListView view;
	private TexturePacksDatabaseModel model = SpoutClient.getInstance().getTexturePacksDatabaseModel();
	private GenericScrollArea filter;
	private SortButton featured, popular, byName;
	private RandomButton random;
	private ResolutionFilter filterResolution;
	
	private void createInstances() {
		buttonMainMenu = new GenericButton("Main Menu");
		buttonLocal = new GenericButton("Installed Textures");
		buttonDownload = new GenericButton("Download Texture");
		buttonAdd = new GenericButton("Add Texture");
		buttonRefresh = new GenericButton("Refresh");
		screenTitle = new GenericLabel("Texture Packs Database");
		buttonForum = new GenericButton("Show Forum Thread");
		view = new GenericListView(model);
		model.setCurrentGui(this);
		model.refreshAPIData(model.getDefaultUrl(), 0, true);
		featured = new SortButton("Featured", "featured", model);
		featured.setAllowSorting(false);
		popular = new SortButton("Popular", "popular", model);
		popular.setAllowSorting(false);
		byName = new SortButton("Name", "sortBy=name", model);
		random = new RandomButton(model);
		filterResolution = new ResolutionFilter();
		sortFilterTitle = new GenericLabel("Sort & Filter");
		model.clearUrlElements();
		model.addUrlElement(popular);
		model.addUrlElement(random);
		model.addUrlElement(featured);
		model.addUrlElement(byName);
		model.addUrlElement(filterResolution);
		filter = new GenericScrollArea();
	}
	
	public void initGui() {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		
		if(!instancesCreated) createInstances();
		
		int top = 5;
		
		int swidth = mc.fontRenderer.getStringWidth(screenTitle.getText());
		screenTitle.setY(top + 7).setX(width / 2 - swidth / 2).setHeight(11).setWidth(swidth);
		getScreen().attachWidget(spoutcraft, screenTitle);

		buttonRefresh.setX(width - 5 - 100).setY(top).setWidth(100).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonRefresh);
		
		top+=25;
		
		int sheight = height - top - 55;
		
		filter.setX(5).setY(top).setWidth(130).setHeight(sheight);
		getScreen().attachWidget(spoutcraft, filter);
		
		view.setX((int) (5 + filter.getX() + filter.getWidth())).setY(top).setWidth((int) (width - 15 - filter.getWidth())).setHeight(sheight);
		getScreen().attachWidget(spoutcraft, view);
		
		top += 5 + view.getHeight();
		
		int totalWidth = Math.min(width - 10, 200*3+10);
		int cellWidth = (totalWidth - 10) / 3;
		int left = width / 2 - totalWidth / 2;
		int center = left + 5 + cellWidth;
		int right = center + 5 + cellWidth;
		
		buttonForum.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonForum);
		
		buttonDownload.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonDownload);
		
		top += 25;
		
		buttonAdd.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonAdd);
		
		buttonLocal.setX(center).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonLocal);
		
		buttonMainMenu.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonMainMenu);
		
		//Filter init
		
		int ftop = 5;
		sortFilterTitle.setX(5).setY(ftop).setHeight(11).setWidth(100);
		filter.attachWidget(spoutcraft, sortFilterTitle);
		ftop += 16;
		
		featured.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filter.attachWidget(spoutcraft, featured);
		ftop += 25;
		
		popular.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filter.attachWidget(spoutcraft, popular);
		ftop += 25;
		
		random.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filter.attachWidget(spoutcraft, random);
		ftop += 25;
		
		byName.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filter.attachWidget(spoutcraft, byName);
		ftop += 25;
		
		filterResolution.setWidth(100).setHeight(20).setY(ftop).setX(5);
		filter.attachWidget(spoutcraft, filterResolution);
		ftop += 25;
		
		//Stretch to real width
		int fw = filter.getViewportSize(Orientation.HORIZONTAL);
		fw-=10;
		for(Widget w:filter.getAttachedWidgets()) {
			w.setWidth(fw);
		}
		
		if(!instancesCreated) 
			featured.setSelected(true);
		
		updateButtons();
		instancesCreated = true;
	}
	
	public void drawScreen(int x, int y, float f) {
		drawDefaultBackground();
	}
	
	public void updateButtons() {
		int sel = view.getSelectedRow();
		boolean enable = false;
		boolean allowForum = false;
		boolean allowDownload = false;
		if(sel >= 0) {
			ListWidgetItem item = model.getItem(sel);
			if(item instanceof TextureItem) {
				TextureItem t = (TextureItem)item;
				enable = true;
				allowDownload = !(t.isDownloading() || t.isInstalled());
				allowForum = !t.getForumlink().isEmpty();
			}
		}
		buttonDownload.setEnabled(allowDownload);
		buttonForum.setEnabled(allowForum);
		
		if(model.isLoading()) {
			buttonRefresh.setEnabled(false);
			buttonRefresh.setText("Loading...");
			buttonRefresh.setDisabledColor(new Color(0f,1f,0f));
		} else {
			buttonRefresh.setEnabled(true);
			buttonRefresh.setText("Refresh");
		}
	}
	
	public void buttonClicked(Button btn) {
		if(btn.equals(buttonLocal)) {
			mc.displayGuiScreen(new GuiTexturePacks());
		}
		if(btn.equals(buttonMainMenu)) {
			mc.displayGuiScreen(new GuiMainMenu());
		}
		if(btn.equals(buttonAdd)) {
			Sys.openURL("http://textures.getspout.org/submit.php");
		}
		if(btn.equals(buttonDownload)) {
			int sel = view.getSelectedRow();
			if(sel >= 0) {
				ListWidgetItem item = model.getItem(sel);
				if(item instanceof TextureItem) {
					((TextureItem)item).download();
					updateButtons();
				}
			}
		}
		if(btn.equals(buttonRefresh)) {
			model.refreshAPIData(model.getCurrentUrl(), 0, true);
		}
		if(btn.equals(buttonForum)) {
			int sel = view.getSelectedRow();
			if(sel >= 0) {
				ListWidgetItem item = model.getItem(sel);
				if(item instanceof TextureItem) {
					((TextureItem)item).download();
					Sys.openURL(((TextureItem)item).getForumlink());
				}
			}
		}
	}
	
	@Override
	public void updateScreen() {
		if(model.isLoading()) {
			Color color = new Color(0, 1f, 0);
			double darkness = 0;
			long t = System.currentTimeMillis() % 1000;
			darkness = Math.cos(t * 2 * Math.PI / 1000) * 0.2 + 0.2;
			color.setGreen(1f - (float)darkness);
			buttonRefresh.setDisabledColor(color);
		}
		super.updateScreen();
	}

}
