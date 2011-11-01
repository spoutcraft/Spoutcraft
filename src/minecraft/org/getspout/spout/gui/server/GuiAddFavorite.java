package org.getspout.spout.gui.server;

import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;
import org.spoutcraft.spoutcraftapi.gui.Label;
import org.spoutcraft.spoutcraftapi.gui.TextField;

import net.minecraft.src.GuiScreen;

public class GuiAddFavorite extends GuiScreen {

	private TextField textIp, textTitle;
	private ServerItem item = null;
	private Label labelTitle, labelIp;
	private boolean update = false;
	private Button buttonDone;
	private GuiScreen parent;
	
	public GuiAddFavorite(GuiScreen parent) {
		item = new ServerItem("", "", 25565, -1);
		update = false;
		this.parent = parent;
	}
	
	public GuiAddFavorite(ServerItem toUpdate, GuiScreen parent) {
		item = toUpdate;
		update = true;
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		int top = height / 2 - 101/2;
		int left = width / 2 - 250 / 2;
		
		updateItem();
		
		labelTitle = new GenericLabel("Server Name");
		labelTitle.setHeight(11).setWidth(250).setX(left).setY(top);
		getScreen().attachWidget(spoutcraft, labelTitle);
		top+=13;
		
		textTitle = new GenericTextField();
		textTitle.setMaximumCharacters(0).setWidth(250).setHeight(20).setX(left).setY(top);
		textTitle.setText(item.getTitle());
		getScreen().attachWidget(spoutcraft, textTitle);
		top+=25;
		
		labelIp = new GenericLabel("Server Adress");
		labelIp.setHeight(11).setWidth(250).setX(left).setY(top);
		getScreen().attachWidget(spoutcraft, labelIp);
		top+=13;
		
		textIp = new GenericTextField();
		textIp.setMaximumCharacters(0);
		textIp.setWidth(250);
		textIp.setHeight(20);
		textIp.setX(left).setY(top);
		getScreen().attachWidget(spoutcraft, textIp);
		textIp.setText(item.getIp() + (item.getPort()!=25565?":"+item.getPort():""));
		top+=25;
		
		buttonDone = new GenericButton("Done");
		buttonDone.setWidth(200).setHeight(20).setX(left+25).setY(top);
		getScreen().attachWidget(spoutcraft, buttonDone);
	}
	
	@Override
	protected void buttonClicked(Button btn) {
		if(btn.equals(buttonDone)) {
			updateItem();
			if(!update) {
				SpoutClient.getInstance().getServerManager().getFavorites().addServer(item);
			}
			SpoutClient.getInstance().getServerManager().getFavorites().save();
			SpoutClient.getHandle().displayGuiScreen(parent);
		}
	}
	
	private void updateItem() {
		if(textTitle != null) {
			item.setTitle(textTitle.getText());
		}
		if(textIp != null) {
			String split[] = textIp.getText().split(":");
			item.setIp(split[0]);
			try {
				item.setPort(Integer.valueOf(split[1]));
			} catch(Exception e){
				//Handles both InvalidNumber and OutOfRange exceptions, yay
				item.setPort(25565);
			}
		}
		
	}
	
	@Override
	public void drawScreen(int a, int b, float f) {
		drawDefaultBackground();
	}
}
