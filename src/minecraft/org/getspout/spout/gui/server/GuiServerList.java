package org.getspout.spout.gui.server;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.database.FilterButton;
import org.getspout.spout.gui.database.GuiAPIDisplay;
import org.getspout.spout.gui.database.RandomButton;
import org.getspout.spout.gui.database.SearchField;
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
import org.spoutcraft.spoutcraftapi.gui.Orientation;
import org.spoutcraft.spoutcraftapi.gui.Widget;

import net.minecraft.src.GuiMainMenu;

public class GuiServerList extends GuiAPIDisplay {


	private ServerListModel model = SpoutClient.getInstance().getServerManager().getServerList();
	
	private Label labelTitle, filterTitle;
	private GenericListView view;
	private GenericScrollArea filters;
	private Button buttonJoin, buttonMainMenu, buttonFavorites, buttonAddFavorite, buttonRefresh, buttonReset, buttonAddServer;
	SortButton featured, popular, byName, byFreeSlots, byPing, byPlayers;
	RandomButton random;
	FilterButton hasPlayers, notFull, noWhitelist;
	CountryButton buttonCountry;
	SearchField search;
	
	boolean instancesCreated = false;
	
	public GuiServerList() {
		model.setCurrentGui(this);
		
		if(!model.getCurrentUrl().equals(model.getDefaultUrl())) {
			model.refreshAPIData(model.getDefaultUrl(), 0, true);
		}
	}
	
	private void createInstances() {
		labelTitle = new GenericLabel("Public Server List");
		filters = new GenericScrollArea();
		filterTitle = new GenericLabel("Sort & Filter");
		featured = new SortButton("Featured", "featured", model);
		popular = new SortButton("Popular", "popular", model);
		byName = new SortButton("Name", "sortBy=name", model);
		byFreeSlots = new SortButton("Free Slots", "sortBy=freeslots", false, model);
		byPlayers = new SortButton("Players Online", "sortBy=players", false, model);
		byPing = new SortButton("Ping", "sortBy=ping", model);
		random = new RandomButton(model);
		hasPlayers = new FilterButton("Has Players", "hasplayers", model);
		notFull = new FilterButton("Not Full", "notfull", model);
		noWhitelist = new FilterButton("No Whitelist", "notwhitelisted", model);
		search = new SearchField(model);
		buttonCountry = new CountryButton();
		view = new GenericListView(model);
		buttonJoin = new GenericButton("Join Server");
		buttonAddFavorite = new GenericButton("Add Favorite");
		buttonMainMenu = new GenericButton("Main Menu");
		buttonFavorites = new GenericButton("Favorites");
		buttonRefresh = new GenericButton("Refresh");
		buttonReset = new GenericButton("Reset Filters");
		buttonAddServer = new GenericButton("Add Your Server");
	}
	
	public void initGui() {
		
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		
		model.clearUrlElements();

		if(!instancesCreated) {
			createInstances();
		}
		
		int top = 5;
		labelTitle.setY(top + 7).setX(width/2 - mc.fontRenderer.getStringWidth("Public Server List")/2);
		getScreen().attachWidget(spoutcraft, labelTitle);
		
		buttonRefresh.setX(width - 5 - 100).setY(top).setWidth(100).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonRefresh);
		
		search.setWidth(130).setHeight(20).setX(5).setY(top);
		getScreen().attachWidget(spoutcraft, search);
		model.addUrlElement(search);
		
		top+=25;
		
		filters.setWidth(130).setHeight(height - top - 55);
		filters.setX(5).setY(top);
		getScreen().attachWidget(spoutcraft, filters);
		
		//Filter init {
		int ftop = 5;
		filterTitle.setX(5).setY(ftop).setHeight(11).setWidth(100);
		filters.attachWidget(spoutcraft, filterTitle);
		ftop += 16;
		
		featured.setAllowSorting(false);
		featured.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget(spoutcraft, featured);
		model.addUrlElement(featured);
		ftop += 25;
		
		popular.setAllowSorting(false);
		popular.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget(spoutcraft, popular);
		model.addUrlElement(popular);
		ftop += 25;
		
		random.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget(spoutcraft, random);
		model.addUrlElement(random);
		ftop += 25;
		
		byName.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget(spoutcraft, byName);
		model.addUrlElement(byName);
		ftop += 25;
		
		byFreeSlots.setWidth(100).setHeight(20).setX(5).setY(ftop);
		byFreeSlots.setTooltip("Sorts by the number of free slots\non the server (maxplayers - players)");
		filters.attachWidget(spoutcraft, byFreeSlots);
		model.addUrlElement(byFreeSlots);
		ftop += 25;
		
		byPlayers.setWidth(100).setHeight(20).setX(5).setY(ftop);
		byPlayers.setTooltip("Sorts by the actual number of\nonline players on the server");
		filters.attachWidget(spoutcraft, byPlayers);
		model.addUrlElement(byPlayers);
		ftop += 25;
		
		byPing.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget(spoutcraft, byPing);
		model.addUrlElement(byPing);
		ftop += 25;
		
		hasPlayers.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget(spoutcraft, hasPlayers);
		model.addUrlElement(hasPlayers);
		ftop += 25;
		
		notFull.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget(spoutcraft, notFull);
		model.addUrlElement(notFull);
		ftop += 25;
		
		noWhitelist.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget(spoutcraft, noWhitelist);
		model.addUrlElement(noWhitelist);
		ftop += 25;
		
		buttonCountry.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget(spoutcraft, buttonCountry);
		model.addUrlElement(buttonCountry);
		ftop += 25;
		
		
		
		//Stretch to real width
		int fw = filters.getViewportSize(Orientation.HORIZONTAL);
		fw-=10;
		for(Widget w:filters.getAttachedWidgets()) {
			w.setWidth(fw);
		}
		
		if(!instancesCreated) featured.setSelected(true);
		//Filter init }
		
		view.setX((int) filters.getWidth() + filters.getX() + 5).setY(top)
			.setWidth((int) (width - filters.getWidth() - 10 - filters.getX())).setHeight(height - top - 55);
		getScreen().attachWidget(spoutcraft, view);
		
		top += view.getHeight() + 5;
		
		int totalWidth = Math.min(width - 9, 200*3+10);
		int cellWidth = (totalWidth - 10)/3;
		int left = width / 2 - totalWidth / 2;
		int center = left + cellWidth + 5;
		int right = center + cellWidth + 5;
		
		buttonReset.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonReset);
		
		buttonAddFavorite.setHeight(20).setWidth(cellWidth).setX(center).setY(top);
		getScreen().attachWidget(spoutcraft, buttonAddFavorite);
		
		buttonJoin.setHeight(20).setWidth(cellWidth).setX(right).setY(top);
		getScreen().attachWidget(spoutcraft, buttonJoin);
		
		top+=25;
		
		buttonAddServer.setHeight(20).setWidth(cellWidth).setX(left).setY(top);
		getScreen().attachWidget(spoutcraft, buttonAddServer);
		
		buttonFavorites.setHeight(20).setWidth(cellWidth).setX(center).setY(top);
		getScreen().attachWidget(spoutcraft, buttonFavorites);

		buttonMainMenu.setHeight(20).setWidth(cellWidth).setX(right).setY(top);
		getScreen().attachWidget(spoutcraft, buttonMainMenu);
		
		updateButtons();
		instancesCreated = true;
	}
	
	public void drawScreen(int a, int b, float c) {
		drawDefaultBackground();
	}
	
	public void buttonClicked(Button btn) {
		if(btn.equals(buttonMainMenu)) {
			mc.displayGuiScreen(new GuiMainMenu());
		}
		if(btn.equals(buttonFavorites)) {
			mc.displayGuiScreen(new GuiFavorites(new GuiMainMenu()));
		}
		if(btn.equals(buttonAddFavorite)) {
			if(view.getSelectedItem() instanceof ServerItem) {
				FavoritesModel favs = SpoutClient.getInstance().getServerManager().getFavorites();
				favs.addServer((ServerItem) view.getSelectedItem());
				favs.save();
				updateButtons();
			}
		}
		if(btn.equals(buttonJoin)) {
			ServerItem item = (ServerItem) view.getSelectedItem();
			if(item != null) {
				SpoutClient.getInstance().getServerManager().join(item, this, "Server List");
			} else {
				updateButtons();
			}
		}
		if(btn.equals(buttonRefresh)) {
			model.updateUrl();
		}
		if(btn.equals(buttonReset)) {
			model.clearElementFilters();
			featured.setSelected(true);
			model.updateUrl();
		}
		if(btn.equals(buttonAddServer)) {
			Sys.openURL("http://servers.getspout.org/submit.php");
		}
	}

	public void updateButtons() {
		boolean b = true;
		if(view.getSelectedRow() == -1 || !(view.getSelectedItem() instanceof ServerItem)) {
			b = false;
		}
		buttonJoin.setEnabled(b);
		buttonAddFavorite.setEnabled(b);
		buttonAddFavorite.setTooltip("");
		
		if(b) {
			ServerItem item = (ServerItem) view.getSelectedItem();
			if(SpoutClient.getInstance().getServerManager().getFavorites().containsSever(item)) {
				buttonAddFavorite.setEnabled(false);
				buttonAddFavorite.setTooltip("You already have this server in your favorites");
			}
		}
		
		if(model.isLoading()) {
			buttonRefresh.setEnabled(false);
			buttonRefresh.setText("Loading...");
			buttonRefresh.setDisabledColor(new Color(0f,1f,0f));
		} else {
			buttonRefresh.setEnabled(true);
			buttonRefresh.setText("Refresh");
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
