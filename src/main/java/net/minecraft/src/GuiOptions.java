package net.minecraft.src;

public class GuiOptions extends GuiScreen {

	/**
	 * An array of options that can be changed directly from the options GUI.
	 */
	private static final EnumOptions[] relevantOptions = new EnumOptions[] {EnumOptions.MUSIC, EnumOptions.SOUND, EnumOptions.INVERT_MOUSE, EnumOptions.SENSITIVITY, EnumOptions.FOV, EnumOptions.DIFFICULTY, EnumOptions.TOUCHSCREEN};

	/**
	 * A reference to the screen object that created this. Used for navigating between screens.
	 */
	private final GuiScreen parentScreen;

	/** Reference to the GameSettings object. */
	private final GameSettings options;

	/** The title string that is displayed in the top-center of the screen. */
	protected String screenTitle = "Options";

	public GuiOptions(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
		this.parentScreen = par1GuiScreen;
		this.options = par2GameSettings;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	// ToDo:  Need Spoutcraft API update for buttonList calls.
	public void initGui() {
		int var1 = 0;
		this.screenTitle = I18n.func_135053_a("options.title");
		EnumOptions[] var2 = relevantOptions;
		int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			EnumOptions var5 = var2[var4];

			if (var5.getEnumFloat()) {
				this.buttonList.add(new GuiSlider(var5.returnEnumOrdinal(), this.width / 2 - 155 + var1 % 2 * 160, this.height / 6 - 12 + 24 * (var1 >> 1), var5, this.options.getKeyBinding(var5), this.options.getOptionFloatValue(var5)));
			} else {
				GuiSmallButton var6 = new GuiSmallButton(var5.returnEnumOrdinal(), this.width / 2 - 155 + var1 % 2 * 160, this.height / 6 - 12 + 24 * (var1 >> 1), var5, this.options.getKeyBinding(var5));

				if (var5 == EnumOptions.DIFFICULTY && this.mc.theWorld != null && this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
					var6.enabled = false;
					var6.displayString = I18n.func_135053_a("options.difficulty") + ": " + I18n.func_135053_a("options.difficulty.hardcore");
				}

				this.buttonList.add(var6);
			}

			++var1;
		}

		this.buttonList.add(new GuiButton(101, this.width / 2 - 152, this.height / 6 + 96 - 6, 150, 20, I18n.func_135053_a("options.video")));
		this.buttonList.add(new GuiButton(100, this.width / 2 + 2, this.height / 6 + 96 - 6, 150, 20, I18n.func_135053_a("options.controls")));
		this.buttonList.add(new GuiButton(102, this.width / 2 - 152, this.height / 6 + 120 - 6, 150, 20, I18n.func_135053_a("options.language")));
		this.buttonList.add(new GuiButton(103, this.width / 2 + 2, this.height / 6 + 120 - 6, 150, 20, I18n.func_135053_a("options.multiplayer.title")));
		this.buttonList.add(new GuiButton(105, this.width / 2 - 152, this.height / 6 + 144 - 6, 150, 20, I18n.func_135053_a("options.resourcepack")));
		this.buttonList.add(new GuiButton(104, this.width / 2 + 2, this.height / 6 + 144 - 6, 150, 20, I18n.func_135053_a("options.snooper.view")));
		this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.func_135053_a("gui.done")));
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id < 100 && par1GuiButton instanceof GuiSmallButton) {
				this.options.setOptionValue(((GuiSmallButton)par1GuiButton).returnEnumOptions(), 1);
				par1GuiButton.displayString = this.options.getKeyBinding(EnumOptions.getEnumOptions(par1GuiButton.id));
			}

			if (par1GuiButton.id == 101) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new GuiVideoSettings(this, this.options));
			}

			if (par1GuiButton.id == 100) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new GuiControls(this, this.options));
			}

			if (par1GuiButton.id == 102) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new GuiLanguage(this, this.options, this.mc.func_135016_M()));
			}

			if (par1GuiButton.id == 103) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new ScreenChatOptions(this, this.options));
			}

			if (par1GuiButton.id == 104) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new GuiSnooper(this, this.options));
			}

			if (par1GuiButton.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.parentScreen);
			}

			if (par1GuiButton.id == 105) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new GuiScreenTemporaryResourcePackSelect(this, this.options));
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 15, 16777215);
		super.drawScreen(par1, par2, par3);
	}
}
