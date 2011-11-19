package net.minecraft.src;

import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.StringTranslate;

public class GuiControls extends GuiScreen {

	private GuiScreen parentScreen;
	protected String screenTitle = "Controls";
	private GameSettings options;
	private int buttonId = -1;


	public GuiControls(GuiScreen var1, GameSettings var2) {
		this.parentScreen = var1;
		this.options = var2;
	}

	private int func_20080_j() {
		return this.width / 2 - 155;
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		int var2 = this.func_20080_j();

		for(int var3 = 0; var3 < this.options.keyBindings.length; ++var3) {
			this.controlList.add(new GuiSmallButton(var3, var2 + var3 % 2 * 160, this.height / 6 + 24 * (var3 >> 1), 70, 20, this.options.getOptionDisplayString(var3)));
		}

		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, var1.translateKey("gui.done")));
		this.screenTitle = var1.translateKey("controls.title");
	}

	protected void actionPerformed(GuiButton var1) {
		for(int var2 = 0; var2 < this.options.keyBindings.length; ++var2) {
			((GuiButton)this.controlList.get(var2)).displayString = this.options.getOptionDisplayString(var2);
		}

		if(var1.id == 200) {
			this.mc.displayGuiScreen(this.parentScreen);
		} else {
			this.buttonId = var1.id;
			var1.displayString = "> " + this.options.getOptionDisplayString(var1.id) + " <";
		}

	}

	protected void mouseClicked(int var1, int var2, int var3) {
		if(this.buttonId >= 0) {
			this.options.setKeyBinding(this.buttonId, -100 + var3);
			((GuiButton)this.controlList.get(this.buttonId)).displayString = this.options.getOptionDisplayString(this.buttonId);
			this.buttonId = -1;
			KeyBinding.resetKeyBindingArrayAndHash();
		} else {
			super.mouseClicked(var1, var2, var3);
		}

	}

	protected void keyTyped(char var1, int var2) {
		if(this.buttonId >= 0) {
			this.options.setKeyBinding(this.buttonId, var2);
			((GuiButton)this.controlList.get(this.buttonId)).displayString = this.options.getOptionDisplayString(this.buttonId);
			this.buttonId = -1;
			KeyBinding.resetKeyBindingArrayAndHash();
		} else {
			super.keyTyped(var1, var2);
		}

	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
		int var4 = this.func_20080_j();

		for(int var5 = 0; var5 < this.options.keyBindings.length; ++var5) {
			boolean var6 = false;

			for(int var7 = 0; var7 < this.options.keyBindings.length; ++var7) {
				if(var7 != var5 && this.options.keyBindings[var5].keyCode == this.options.keyBindings[var7].keyCode) {
					var6 = true;
				}
			}

			if(this.buttonId == var5) {
				((GuiButton)this.controlList.get(var5)).displayString = "\u00a7f> \u00a7e??? \u00a7f<";
			} else if(var6) {
				((GuiButton)this.controlList.get(var5)).displayString = "\u00a7c" + this.options.getOptionDisplayString(var5);
			} else {
				((GuiButton)this.controlList.get(var5)).displayString = this.options.getOptionDisplayString(var5);
			}

			this.drawString(this.fontRenderer, this.options.getKeyBindingDescription(var5), var4 + var5 % 2 * 160 + 70 + 6, this.height / 6 + 24 * (var5 >> 1) + 7, -1);
		}

		super.drawScreen(var1, var2, var3);
	}
}
