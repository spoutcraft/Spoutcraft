package org.getspout.spout.gui.server;

import org.lwjgl.input.Keyboard;
import net.minecraft.src.*;

public class GuiFilters extends GuiScreen {

	private GuiScreen parentScreen;
	private GuiTextField field_22114_h;


	public GuiFilters(GuiMultiplayer var1) {
		this.parentScreen = var1;
	}

	public void updateScreen() {
		this.field_22114_h.updateCursorCounter();
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("selectWorld.renameButton")));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == 1) {
				Spout.getGameInstance().displayGuiScreen(this.parentScreen);
			} else if(var1.id == 0) {
				ISaveFormat var2 = Spout.getGameInstance().getSaveLoader();
				Spout.getGameInstance().displayGuiScreen(this.parentScreen);
			}

		}
	}

	public void keyTyped(char var1, int var2) {
		this.field_22114_h.textboxKeyTyped(var1, var2);
		((GuiButton)this.controlList.get(0)).enabled = this.field_22114_h.getText().trim().length() > 0;
		if(var1 == 13) {
			this.actionPerformed((GuiButton)this.controlList.get(0));
		}

	}

	public void mouseClicked(int var1, int var2, int var3) {
		super.mouseClicked(var1, var2, var3);
		this.field_22114_h.mouseClicked(var1, var2, var3);
	}

	public void drawScreen(int var1, int var2, float var3) {
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(Spout.getGameInstance().fontRenderer, var4.translateKey("selectWorld.renameTitle"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
		this.drawString(Spout.getGameInstance().fontRenderer, var4.translateKey("selectWorld.enterName"), this.width / 2 - 100, 47, 10526880);
		this.field_22114_h.drawTextBox();
		super.drawScreen(var1, var2, var3);
	}
}
