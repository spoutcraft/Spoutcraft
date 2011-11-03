package org.getspout.spout.gui.server;

import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;
import org.spoutcraft.spoutcraftapi.gui.Label;
import org.spoutcraft.spoutcraftapi.gui.TextField;
import org.spoutcraft.spoutcraftapi.gui.GenericListView;

import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;

public class GuiFavorites2 extends GuiScreen {
	@SuppressWarnings("unused")
	private GuiScreen parent;
	
	//GUI stuff
	private Button buttonJoin, buttonAdd, buttonDelete, buttonEdit, 
		buttonMainMenu, buttonServerList, buttonQuickJoin, 
		buttonMoveUp, buttonMoveDown, buttonRefresh;
	private TextField textQuickJoin;
	private GenericListView view;
	private Label title;
	public FavoritesModel model = SpoutClient.getInstance().getServerManager().getFavorites();

	public GuiFavorites2(GuiScreen parent) {
		model.setCurrentGUI(this);
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		Addon spoutcraft = SpoutClient.getInstance().getAddonManager().getAddon("spoutcraft");
		
		title = new GenericLabel("Favorite Servers");
		title.setX(width / 2 - SpoutClient.getHandle().fontRenderer.getStringWidth(title.getText()) / 2);
		title.setY(5);
		title.setHeight(15).setWidth(SpoutClient.getHandle().fontRenderer.getStringWidth(title.getText()) / 2);
		getScreen().attachWidget(spoutcraft, title);
		
		buttonMoveUp = new GenericButton("/\\");
		buttonMoveUp.setX(width - 50).setY(0);
		buttonMoveUp.setHeight(20).setWidth(20);
		getScreen().attachWidget(spoutcraft, buttonMoveUp);
		
		buttonMoveDown = new GenericButton("\\/");
		buttonMoveDown.setX(width - 25).setY(0);
		buttonMoveDown.setHeight(20).setWidth(20);
		getScreen().attachWidget(spoutcraft, buttonMoveDown);
		
		buttonRefresh = new GenericButton("Poll again");
		buttonRefresh.setHeight(20).setWidth(150).setX(5).setY(0);
		getScreen().attachWidget(spoutcraft, buttonRefresh);
		
		view = new GenericListView(model);
		view.setX(0).setY(20).setWidth(width).setHeight(height - 100);
		getScreen().attachWidget(spoutcraft, view);
		
		int top = (int) (view.getY() + view.getHeight() + 5);
		
		int totalWidth = 401;
		int cellWidth = (totalWidth - 20)/3;
		int left = width / 2 - totalWidth / 2 + 5;
		int center = left + cellWidth + 5;
		int right = center + cellWidth + 5;
		
		String text = SpoutClient.getHandle().gameSettings.lastServer.replace("_", ":");
		if(textQuickJoin != null) text = textQuickJoin.getText();
		textQuickJoin = new GenericTextField();
		textQuickJoin.setX(left+2).setY(top+2).setHeight(16).setWidth(cellWidth*2+5-4);
		textQuickJoin.setMaximumCharacters(0);
		textQuickJoin.setText(text);
		getScreen().attachWidget(spoutcraft, textQuickJoin);
		
		buttonQuickJoin = new GenericButton("Quick Join");
		buttonQuickJoin.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonQuickJoin);
		
		top += 25;
		
		buttonJoin = new GenericButton("Join Server");
		buttonJoin.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonJoin);
		
		buttonEdit = new GenericButton("Edit");
		buttonEdit.setX(center).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonEdit);
		
		buttonDelete = new GenericButton("Delete");
		buttonDelete.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonDelete);
		
		top += 25;
		
		buttonAdd = new GenericButton("Add Favorite");
		buttonAdd.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonAdd);
		
		buttonServerList = new GenericButton("Server List");
		buttonServerList.setX(center).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonServerList);
		
		buttonMainMenu = new GenericButton("Main Menu");
		buttonMainMenu.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget(spoutcraft, buttonMainMenu);
		
		updateButtons();
	}
	
	@Override
	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
		
		//TODO: Draw the refresh button thingie
	}
	
	@Override
	public void buttonClicked(Button btn) {
		if(btn.equals(buttonMainMenu)) {
			SpoutClient.getHandle().displayGuiScreen(new GuiMainMenu());
		}
		if(btn.equals(buttonServerList)) {
			SpoutClient.getHandle().displayGuiScreen(new GuiServerList());
		}
		if(btn.equals(buttonQuickJoin)) {
			doQuickJoin();
		}
		if(btn.equals(buttonAdd)) {
			SpoutClient.getHandle().displayGuiScreen(new GuiAddFavorite(this));
		}
		if(btn.equals(buttonEdit)) {
			ServerItem item = (ServerItem)view.getSelectedItem();
			//Produces a "hang" why ever :(
			if(item != null) {
				SpoutClient.getHandle().displayGuiScreen(new GuiAddFavorite(item, this));
			} else {
				updateButtons();
			}
		}
		if(btn.equals(buttonDelete)) {
			model.removeServer((ServerItem)view.getSelectedItem());
			model.save();
		}
		if(btn.equals(buttonJoin)) {
			ServerItem item = null;
			if(view.getSelectedRow() > -1) {
				item = (ServerItem) model.getItem(view.getSelectedRow());
			}
			if(item != null) {
				SpoutClient.getInstance().getServerManager().join(item);
			} else {
				//Just in case something weird happens
				updateButtons();
			}
		}
		if(btn.equals(buttonMoveUp)) {
			if(view.getSelectedRow() != -1){
				model.move(view.getSelectedRow(), view.getSelectedRow()-1);
				view.shiftSelection(-1);
				model.save();
			}
		}
		if(btn.equals(buttonMoveDown)) {
			if(view.getSelectedRow() != -1) {
				model.move(view.getSelectedRow(), view.getSelectedRow()+1);
				view.shiftSelection(1);
				model.save();
			}
		}
		if(btn.equals(buttonRefresh)) {
			for(int i = 0; i<model.getSize(); i++) {
				ServerItem item = (ServerItem) model.getItem(i);
				item.poll();
			}
		}
	}

	public void doQuickJoin() {
		try	{
			String adress = textQuickJoin.getText();
			if (!adress.isEmpty()) {
				String split[] = adress.split(":");
				String ip = split[0];
				int port = split.length > 1 ? Integer.parseInt(split[1]) : 25565;
				SpoutClient.getHandle().gameSettings.lastServer = adress.replace(":", "_");
				SpoutClient.getHandle().gameSettings.saveOptions();
				SpoutClient.getInstance().getServerManager().join(ip, port);
			}
		}
		catch (Exception e) { }
	}
	
	public void updateButtons() {
		boolean enable = true;
		if(view.getSelectedRow() == -1) {
			enable = false;
		}
		buttonEdit.setEnabled(enable);
		buttonDelete.setEnabled(enable);
		buttonJoin.setEnabled(enable);
	}
}
