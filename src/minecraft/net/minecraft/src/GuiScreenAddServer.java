package net.minecraft.src;

import java.util.List;
import org.lwjgl.input.Keyboard;

public class GuiScreenAddServer extends GuiScreen {
	private GuiScreen field_35362_a;
	private GuiTextField serverAddress;
	private GuiTextField serverName;
	private ServerNBTStorage serverNBTStorage;

	public GuiScreenAddServer(GuiScreen guiscreen, ServerNBTStorage servernbtstorage) {
		field_35362_a = guiscreen;
		serverNBTStorage = servernbtstorage;
	}

	public void updateScreen() {
		serverName.updateCursorCounter();
		serverAddress.updateCursorCounter();
	}

	public void initGui() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		controlList.clear();
		controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, stringtranslate.translateKey("addServer.add")));
		controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, stringtranslate.translateKey("gui.cancel")));
		serverName = new GuiTextField(this, fontRenderer, width / 2 - 100, 76, 200, 20, serverNBTStorage.name);
		serverName.isFocused = true;
		serverName.setMaxStringLength(32);
		serverAddress = new GuiTextField(this, fontRenderer, width / 2 - 100, 116, 200, 20, serverNBTStorage.host);
		serverAddress.setMaxStringLength(128);
		((GuiButton)controlList.get(0)).enabled = serverAddress.getText().length() > 0 && serverName.getText().length() > 0;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 1) {
			field_35362_a.deleteWorld(false, 0);
		}
		else if (guibutton.id == 0) {
			serverNBTStorage.name = serverName.getText();
			serverNBTStorage.host = serverAddress.getText();
			field_35362_a.deleteWorld(true, 0);
		}
	}

	protected void keyTyped(char c, int i) {
		serverName.textboxKeyTyped(c, i);
		serverAddress.textboxKeyTyped(c, i);
		if (c == '\t') {
			if (serverName.isFocused) {
				serverName.isFocused = false;
				serverAddress.isFocused = true;
			}
			else {
				serverName.isFocused = true;
				serverAddress.isFocused = false;
			}
		}
		if (c == '\r') {
			actionPerformed((GuiButton)controlList.get(0));
		}
		((GuiButton)controlList.get(0)).enabled = serverAddress.getText().length() > 0 && serverName.getText().length() > 0;
		if (((GuiButton)controlList.get(0)).enabled) {
			String s = serverAddress.getText().trim();
			String as[] = s.split(":");
			if (as.length > 2) {
				((GuiButton)controlList.get(0)).enabled = false;
			}
		}
	}

	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		serverAddress.mouseClicked(i, j, k);
		serverName.mouseClicked(i, j, k);
	}

	public void drawScreen(int i, int j, float f) {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		drawDefaultBackground();
		drawCenteredString(fontRenderer, stringtranslate.translateKey("addServer.title"), width / 2, (height / 4 - 60) + 20, 0xffffff);
		drawString(fontRenderer, stringtranslate.translateKey("addServer.enterName"), width / 2 - 100, 63, 0xa0a0a0);
		drawString(fontRenderer, stringtranslate.translateKey("addServer.enterIp"), width / 2 - 100, 104, 0xa0a0a0);
		serverName.drawTextBox();
		serverAddress.drawTextBox();
		super.drawScreen(i, j, f);
	}
}
