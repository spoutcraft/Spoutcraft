package net.minecraft.src;

import org.getspout.spout.gui.CustomScreen;
import org.getspout.spout.gui.settings.VideoSettings;

import net.minecraft.src.EnumOptions;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiControls;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSlider;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.GuiVideoSettings;
import net.minecraft.src.StringTranslate;

public class GuiOptions extends GuiScreen {

	private GuiScreen parentScreen;
	protected String screenTitle = "Options";
	private GameSettings options;
	private static EnumOptions[] relevantOptions = new EnumOptions[]{EnumOptions.MUSIC, EnumOptions.SOUND, EnumOptions.INVERT_MOUSE, EnumOptions.SENSITIVITY, EnumOptions.FOV, EnumOptions.DIFFICULTY};


	public GuiOptions(GuiScreen var1, GameSettings var2) {
		this.parentScreen = var1;
		this.options = var2;
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.screenTitle = var1.translateKey("options.title");
		int var2 = 0;
		EnumOptions[] var3 = relevantOptions;
		int var4 = var3.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			EnumOptions var6 = var3[var5];
			if(!var6.getEnumFloat()) {
				this.controlList.add(new GuiSmallButton(var6.returnEnumOrdinal(), this.width / 2 - 155 + var2 % 2 * 160, this.height / 6 + 24 * (var2 >> 1), var6, this.options.getKeyBinding(var6)));
			} else {
				this.controlList.add(new GuiSlider(var6.returnEnumOrdinal(), this.width / 2 - 155 + var2 % 2 * 160, this.height / 6 + 24 * (var2 >> 1), var6, this.options.getKeyBinding(var6), this.options.getOptionFloatValue(var6)));
			}

			++var2;
		}

		this.controlList.add(new GuiButton(101, this.width / 2 - 100, this.height / 6 + 96 + 12, var1.translateKey("options.video")));
		this.controlList.add(new GuiButton(100, this.width / 2 - 100, this.height / 6 + 120 + 12, var1.translateKey("options.controls")));
		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, var1.translateKey("gui.done")));
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id < 100 && var1 instanceof GuiSmallButton) {
				this.options.setOptionValue(((GuiSmallButton)var1).returnEnumOptions(), 1);
				var1.displayString = this.options.getKeyBinding(EnumOptions.getEnumOptions(var1.id));
			}

			if(var1.id == 101) {
				this.mc.gameSettings.saveOptions();
				//Spout start
				//this.mc.displayGuiScreen(new GuiVideoSettings(this, this.options));
				this.mc.displayGuiScreen(new VideoSettings(this));
				//Spout end
			}

			if(var1.id == 100) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new org.getspout.spout.gui.controls.GuiControls(this)); //Spout
			}

			if(var1.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.parentScreen);
			}

		}
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
		super.drawScreen(var1, var2, var3);
	}

}
