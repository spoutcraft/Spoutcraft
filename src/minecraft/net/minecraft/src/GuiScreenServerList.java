package net.minecraft.src;

import java.util.List;
import org.lwjgl.input.Keyboard;

public class GuiScreenServerList extends GuiScreen {
	private GuiScreen guiScreen;
	private GuiTextField serverTextField;
	private ServerNBTStorage field_35318_c;

	public GuiScreenServerList(GuiScreen guiscreen, ServerNBTStorage servernbtstorage) {
		guiScreen = guiscreen;
		field_35318_c = servernbtstorage;
	}

	public void updateScreen() {
		serverTextField.updateCursorCounter();
	}

	public void initGui() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		controlList.clear();
		controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, stringtranslate.translateKey("selectServer.select")));
		controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, stringtranslate.translateKey("gui.cancel")));
		serverTextField = new GuiTextField(this, fontRenderer, width / 2 - 100, 116, 200, 20, field_35318_c.host);
		serverTextField.setMaxStringLength(128);
		((GuiButton)controlList.get(0)).enabled = serverTextField.getText().length() > 0;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 1) {
			guiScreen.deleteWorld(false, 0);
		}
		else if (guibutton.id == 0) {
			field_35318_c.host = serverTextField.getText();
			guiScreen.deleteWorld(true, 0);
		}
	}

	protected void keyTyped(char c, int i) {
		serverTextField.textboxKeyTyped(c, i);
		if (c == '\r') {
			actionPerformed((GuiButton)controlList.get(0));
		}
		((GuiButton)controlList.get(0)).enabled = serverTextField.getText().length() > 0;
	}

	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		serverTextField.mouseClicked(i, j, k);
	}

	public void drawScreen(int i, int j, float f) {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		drawDefaultBackground();
		drawCenteredString(fontRenderer, stringtranslate.translateKey("selectServer.direct"), width / 2, (height / 4 - 60) + 20, 0xffffff);
		drawString(fontRenderer, stringtranslate.translateKey("addServer.enterIp"), width / 2 - 100, 100, 0xa0a0a0);
		serverTextField.drawTextBox();
		super.drawScreen(i, j, f);
	}
}
