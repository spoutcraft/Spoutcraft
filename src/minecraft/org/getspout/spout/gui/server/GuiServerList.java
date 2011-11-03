package org.getspout.spout.gui.server;

import org.getspout.spout.client.SpoutClient;
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
import net.minecraft.src.GuiScreen;

public class GuiServerList extends GuiScreen {
	
	private ServerListModel model = SpoutClient.getInstance().getServerManager().getServerList();
	
	private Label labelTitle, filterTitle, labelSearch;
	private GenericListView view;
	private GenericScrollArea filters;
	private Button buttonJoin, buttonMainMenu, buttonFavorites, buttonAddFavorite, buttonSearch;
	SortButton featured, popular, byName, byFreeSlots, byPing;
	RandomButton random;
	FilterButton hasPlayers, notFull;
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
		featured = new SortButton("Featured", "featured");
		popular = new SortButton("Popular", "popular");
		byName = new SortButton("Name", "sortBy=name");
		byFreeSlots = new SortButton("Free slots", "sortBy=freeslots", false);
		byPing = new SortButton("Ping", "sortBy=ping");
		random = new RandomButton();
		hasPlayers = new FilterButton("Has Players", "hasplayers");
		notFull = new FilterButton("Not Full", "notfull");
		labelSearch = new GenericLabel("Search");
		search = new SearchField();
		buttonSearch = new GenericButton("Search");
		buttonCountry = new CountryButton();
		view = new GenericListView(model);
		buttonJoin = new GenericButton("Join");
		buttonAddFavorite = new GenericButton("Add as favorite");
		buttonMainMenu = new GenericButton("Main Menu");
		buttonFavorites = new GenericButton("Favorites");
	}
	
	public void initGui() {
		
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		
		model.clearUrlElements();

		if(!instancesCreated) {
			createInstances();
		}
		
		int top = 5;
		labelTitle.setY(top)
			.setX(width/2 - mc.fontRenderer.getStringWidth("Public Server List")/2);
		getScreen().attachWidget(spoutcraft, labelTitle);
		top+=11;
		
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
		filters.attachWidget(spoutcraft, byFreeSlots);
		model.addUrlElement(byFreeSlots);
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
		
		labelSearch.setX(5).setY(ftop).setHeight(11).setWidth(100);
		filters.attachWidget(spoutcraft, labelSearch);
		ftop += 13;
		
		search.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget(spoutcraft, search);
		model.addUrlElement(search);
		ftop += 25;
		
		buttonSearch.setWidth(100).setHeight(20).setX(5).setY(ftop);
		filters.attachWidget(spoutcraft, buttonSearch);
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
		
		int left = width / 2 - 405 / 2 + 5;
		int right = left + 205;
		
		buttonJoin.setHeight(20).setWidth(200).setX(left).setY(top);
		getScreen().attachWidget(spoutcraft, buttonJoin);
		
		buttonAddFavorite.setHeight(20).setWidth(200).setX(right).setY(top);
		getScreen().attachWidget(spoutcraft, buttonAddFavorite);
		
		top+=25;

		buttonMainMenu.setHeight(20).setWidth(200).setX(left).setY(top);
		getScreen().attachWidget(spoutcraft, buttonMainMenu);
		
		buttonFavorites.setHeight(20).setWidth(200).setX(right).setY(top);
		getScreen().attachWidget(spoutcraft, buttonFavorites);
		
		updateButtons();
		instancesCreated = true;
	}
	
	public void drawScreen(int a, int b, float c) {
		drawDefaultBackground();
		if(model.isLoading()) {
			Color color = new Color(0, 1f, 0);
			double darkness = 0;
			long t = System.currentTimeMillis() % 1000;
			darkness = Math.cos(t * 2 * Math.PI / 1000) * 0.2 + 0.2;
			color.setGreen(1f - (float)darkness);
			mc.fontRenderer.drawStringWithShadow("Loading...", 5, 5, color.toInt());
		}
	}
	
	public void buttonClicked(Button btn) {
		if(btn.equals(buttonMainMenu)) {
			mc.displayGuiScreen(new GuiMainMenu());
		}
		if(btn.equals(buttonFavorites)) {
			mc.displayGuiScreen(new GuiFavorites2(new GuiMainMenu()));
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
				SpoutClient.getInstance().getServerManager().join(item);
			} else {
				updateButtons();
			}
		}
		if(btn.equals(buttonSearch)) {
			model.updateUrl();
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
	}
}
