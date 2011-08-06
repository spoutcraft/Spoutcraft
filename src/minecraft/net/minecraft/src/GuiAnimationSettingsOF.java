package net.minecraft.src;

import net.minecraft.src.EnumOptions;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSlider;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StringTranslate;

public class GuiAnimationSettingsOF extends GuiScreen {

	private GuiScreen prevScreen;
	protected String title = "Animation Settings";
	private GameSettings settings;
	private static EnumOptions[] enumOptions = new EnumOptions[]{EnumOptions.ANIMATED_WATER, EnumOptions.ANIMATED_LAVA, EnumOptions.ANIMATED_FIRE, EnumOptions.ANIMATED_PORTAL, EnumOptions.ANIMATED_REDSTONE, EnumOptions.ANIMATED_EXPLOSION, EnumOptions.ANIMATED_FLAME, EnumOptions.ANIMATED_SMOKE};


	public GuiAnimationSettingsOF(GuiScreen var1, GameSettings var2) {
		this.prevScreen = var1;
		this.settings = var2;
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		int var2 = 0;
		EnumOptions[] var3 = enumOptions;
		int var4 = var3.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			EnumOptions var6 = var3[var5];
			int var7 = this.width / 2 - 155 + var2 % 2 * 160;
			int var8 = this.height / 6 + 21 * (var2 / 2) - 10;
			if(!var6.getEnumFloat()) {
				this.controlList.add(new GuiSmallButton(var6.returnEnumOrdinal(), var7, var8, var6, this.settings.getKeyBinding(var6)));
			} else {
				this.controlList.add(new GuiSlider(var6.returnEnumOrdinal(), var7, var8, var6, this.settings.getKeyBinding(var6), this.settings.getOptionFloatValue(var6)));
			}

			++var2;
		}

		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, var1.translateKey("gui.done")));
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id < 100 && var1 instanceof GuiSmallButton) {
				this.settings.setOptionValue(((GuiSmallButton)var1).returnEnumOptions(), 1);
				var1.displayString = this.settings.getKeyBinding(EnumOptions.getEnumOptions(var1.id));
			}

			if(var1.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.prevScreen);
			}

			if(var1.id != EnumOptions.CLOUD_HEIGHT.ordinal()) {
				ScaledResolution var2 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
				int var3 = var2.getScaledWidth();
				int var4 = var2.getScaledHeight();
				this.setWorldAndResolution(this.mc, var3, var4);
			}

		}
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
		super.drawScreen(var1, var2, var3);
	}

}
