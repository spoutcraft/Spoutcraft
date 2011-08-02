package org.getspout.spout.gui.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;

public class GuiAddFav extends GuiScreen {

	private GuiScreen screen;
	private GuiTextField nameField;
	private GuiTextField Ipfield;
	private final String port;
	private final String ip;


	public GuiAddFav(GuiScreen screen, String port, String ip) {
		this.screen = screen;
		this.port = port;
		this.ip = ip;
	}

	public void updateScreen() {
		this.nameField.updateCursorCounter();
		this.Ipfield.updateCursorCounter();
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("Add")));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
		this.nameField = new GuiTextField(this, Spout.getGameInstance().fontRenderer, this.width / 2 - 100, 60, 200, 20, this.port);
		this.nameField.isFocused = true;
		this.nameField.setMaxStringLength(35);
		this.Ipfield = new GuiTextField(this, Spout.getGameInstance().fontRenderer, this.width / 2 - 100, 120, 200, 20, this.ip);
		this.Ipfield.isFocused = false;
		this.Ipfield.setMaxStringLength(35);
		((GuiButton)this.controlList.get(0)).enabled = this.nameField.getText().trim().length() > 0 && this.Ipfield.getText().trim().length() > 0;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == 1) {
				this.mc.displayGuiScreen(this.screen);
			} else if(var1.id == 0) {
				this.writeFav(this.nameField.getText(), this.Ipfield.getText());
				this.mc.displayGuiScreen(this.screen);
			}

		}
	}

	public void keyTyped(char var1, int var2) {
		this.nameField.textboxKeyTyped(var1, var2);
		this.Ipfield.textboxKeyTyped(var1, var2);
		((GuiButton)this.controlList.get(0)).enabled = this.nameField.getText().trim().length() > 0 && this.Ipfield.getText().trim().length() > 0;
		if(var1 == 13) {
			this.actionPerformed((GuiButton)this.controlList.get(0));
		}

	}

	public void writeFav(String port, String ip) {
		try {
			FileWriter writer = new FileWriter(Minecraft.getMinecraftDir().getPath() + "/Fav.serv", true);
			BufferedWriter bufferedWritter = new BufferedWriter(writer);
			bufferedWritter.write(port + ">" + ip);
			bufferedWritter.newLine();
			bufferedWritter.flush();
			bufferedWritter.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

	}

	public void mouseClicked(int var1, int var2, int var3) {
		super.mouseClicked(var1, var2, var3);
		this.nameField.mouseClicked(var1, var2, var3);
		this.Ipfield.mouseClicked(var1, var2, var3);
	}

	public void drawScreen(int var1, int var2, float var3) {
		StringTranslate translater = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(Spout.getGameInstance().fontRenderer, translater.translateKey("Add New Favorite"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
		this.drawString(Spout.getGameInstance().fontRenderer, translater.translateKey("Server IP:Port"), this.width / 2 - 100, 47, 10526880);
		this.drawString(Spout.getGameInstance().fontRenderer, translater.translateKey("Server Name"), this.width / 2 - 100, 107, 10526880);
		this.nameField.drawTextBox();
		this.Ipfield.drawTextBox();
		super.drawScreen(var1, var2, var3);
	}
}
