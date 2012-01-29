package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class GuiRenameWorld extends GuiScreen {
	private GuiScreen parentGuiScreen;
	private GuiTextField theGuiTextField;
	private final String worldName;

	public GuiRenameWorld(GuiScreen guiscreen, String s) {
		parentGuiScreen = guiscreen;
		worldName = s;
	}

	public void updateScreen() {
		theGuiTextField.updateCursorCounter();
	}

	public void initGui() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		controlList.clear();
		controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, stringtranslate.translateKey("selectWorld.renameButton")));
		controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, stringtranslate.translateKey("gui.cancel")));
		ISaveFormat isaveformat = mc.getSaveLoader();
		WorldInfo worldinfo = isaveformat.getWorldInfo(worldName);
		String s = worldinfo.getWorldName();
		theGuiTextField = new GuiTextField(this, fontRenderer, width / 2 - 100, 60, 200, 20, s);
		theGuiTextField.isFocused = true;
		theGuiTextField.setMaxStringLength(32);
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 1) {
			mc.displayGuiScreen(parentGuiScreen);
		}
		else if (guibutton.id == 0) {
			ISaveFormat isaveformat = mc.getSaveLoader();
			isaveformat.renameWorld(worldName, theGuiTextField.getText().trim());
			mc.displayGuiScreen(parentGuiScreen);
		}
	}

	protected void keyTyped(char c, int i) {
		theGuiTextField.textboxKeyTyped(c, i);
		((GuiButton)controlList.get(0)).enabled = theGuiTextField.getText().trim().length() > 0;
		if (c == '\r') {
			actionPerformed((GuiButton)controlList.get(0));
		}
	}

	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		theGuiTextField.mouseClicked(i, j, k);
	}

	public void drawScreen(int i, int j, float f) {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		drawDefaultBackground();
		drawCenteredString(fontRenderer, stringtranslate.translateKey("selectWorld.renameTitle"), width / 2, (height / 4 - 60) + 20, 0xffffff);
		drawString(fontRenderer, stringtranslate.translateKey("selectWorld.enterName"), width / 2 - 100, 47, 0xa0a0a0);
		theGuiTextField.drawTextBox();
		super.drawScreen(i, j, f);
	}
}
