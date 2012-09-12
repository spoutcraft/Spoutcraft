package net.minecraft.src;

import java.util.Random;
import org.lwjgl.input.Keyboard;

public class GuiCreateWorld extends GuiScreen {
	private GuiScreen parentGuiScreen;
	private GuiTextField textboxWorldName;
	private GuiTextField textboxSeed;
	private String folderName;
	private String gameMode = "survival";
	private boolean field_35365_g = true;
	private boolean field_40232_h = false;
	private boolean createClicked;
	private boolean moreOptions;
	private GuiButton gameModeButton;
	private GuiButton moreWorldOptions;
	private GuiButton generateStructuresButton;
	private GuiButton worldTypeButton;
	private String gameModeDescriptionLine1;
	private String gameModeDescriptionLine2;
	private String seed;
	private String localizedNewWorldText;
	private int field_46030_z = 0;

	public GuiCreateWorld(GuiScreen par1GuiScreen) {
		this.parentGuiScreen = par1GuiScreen;
		this.seed = "";
		this.localizedNewWorldText = StatCollector.translateToLocal("selectWorld.newWorld");
	}

	public void updateScreen() {
		this.textboxWorldName.updateCursorCounter();
		this.textboxSeed.updateCursorCounter();
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, var1.translateKey("selectWorld.create")));
		this.controlList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, var1.translateKey("gui.cancel")));
		this.controlList.add(this.gameModeButton = new GuiButton(2, this.width / 2 - 75, 100, 150, 20, var1.translateKey("selectWorld.gameMode")));
		this.controlList.add(this.moreWorldOptions = new GuiButton(3, this.width / 2 - 75, 172, 150, 20, var1.translateKey("selectWorld.moreWorldOptions")));
		this.controlList.add(this.generateStructuresButton = new GuiButton(4, this.width / 2 - 155, 100, 150, 20, var1.translateKey("selectWorld.mapFeatures")));
		this.generateStructuresButton.drawButton = false;
		this.controlList.add(this.worldTypeButton = new GuiButton(5, this.width / 2 + 5, 100, 150, 20, var1.translateKey("selectWorld.mapType")));
		this.worldTypeButton.drawButton = false;
		this.textboxWorldName = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
		this.textboxWorldName.setFocused(true);
		this.textboxWorldName.setText(this.localizedNewWorldText);
		this.textboxSeed = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
		this.textboxSeed.setText(this.seed);
		this.makeUseableName();
		this.func_35363_g();
	}

	private void makeUseableName() {
		this.folderName = this.textboxWorldName.getText().trim();
		char[] var1 = ChatAllowedCharacters.invalidFilenameCharacters;
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			char var4 = var1[var3];
			this.folderName = this.folderName.replace(var4, '_');
		}

		if (MathHelper.stringNullOrLengthZero(this.folderName)) {
			this.folderName = "World";
		}

		this.folderName = func_25097_a(this.mc.getSaveLoader(), this.folderName);
	}

	private void func_35363_g() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.gameModeButton.displayString = var1.translateKey("selectWorld.gameMode") + " " + var1.translateKey("selectWorld.gameMode." + this.gameMode);
		this.gameModeDescriptionLine1 = var1.translateKey("selectWorld.gameMode." + this.gameMode + ".line1");
		this.gameModeDescriptionLine2 = var1.translateKey("selectWorld.gameMode." + this.gameMode + ".line2");
		this.generateStructuresButton.displayString = var1.translateKey("selectWorld.mapFeatures") + " ";
		if (this.field_35365_g) {
			this.generateStructuresButton.displayString = this.generateStructuresButton.displayString + var1.translateKey("options.on");
		} else {
			this.generateStructuresButton.displayString = this.generateStructuresButton.displayString + var1.translateKey("options.off");
		}

		this.worldTypeButton.displayString = var1.translateKey("selectWorld.mapType") + " " + var1.translateKey(WorldType.worldTypes[this.field_46030_z].getTranslateName());
	}

	public static String func_25097_a(ISaveFormat par0ISaveFormat, String par1Str) {
		for (par1Str = par1Str.replaceAll("[\\./\"]|COM", "_"); par0ISaveFormat.getWorldInfo(par1Str) != null; par1Str = par1Str + "-") {
			;
		}

		return par1Str;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id == 1) {
				this.mc.displayGuiScreen(this.parentGuiScreen);
			} else if (par1GuiButton.id == 0) {
				this.mc.displayGuiScreen((GuiScreen)null);
				if (this.createClicked) {
					return;
				}

				this.createClicked = true;
				long var2 = (new Random()).nextLong();
				String var4 = this.textboxSeed.getText();
				if (!MathHelper.stringNullOrLengthZero(var4)) {
					try {
						long var5 = Long.parseLong(var4);
						if (var5 != 0L) {
							var2 = var5;
						}
					} catch (NumberFormatException var7) {
						var2 = (long)var4.hashCode();
					}
				}

				this.mc.launchIntegratedServer(this.folderName, this.textboxWorldName.getText(), new WorldSettings(var2, EnumGameType.getByName(this.gameMode), this.field_35365_g, this.field_40232_h, WorldType.worldTypes[this.field_46030_z]));
				this.mc.displayGuiScreen((GuiScreen)null);
			} else if (par1GuiButton.id == 3) {
				this.moreOptions = !this.moreOptions;
				this.gameModeButton.drawButton = !this.moreOptions;
				this.generateStructuresButton.drawButton = this.moreOptions;
				this.worldTypeButton.drawButton = this.moreOptions;
				StringTranslate var8;
				if (this.moreOptions) {
					var8 = StringTranslate.getInstance();
					this.moreWorldOptions.displayString = var8.translateKey("gui.done");
				} else {
					var8 = StringTranslate.getInstance();
					this.moreWorldOptions.displayString = var8.translateKey("selectWorld.moreWorldOptions");
				}
			} else if (par1GuiButton.id == 2) {
				if (this.gameMode.equals("survival")) {
					this.field_40232_h = false;
					this.gameMode = "hardcore";
					this.field_40232_h = true;
					this.func_35363_g();
				} else if (this.gameMode.equals("hardcore")) {
					this.field_40232_h = false;
					this.gameMode = "creative";
					this.func_35363_g();
					this.field_40232_h = false;
				} else {
					this.gameMode = "survival";
					this.func_35363_g();
					this.field_40232_h = false;
				}

				this.func_35363_g();
			} else if (par1GuiButton.id == 4) {
				this.field_35365_g = !this.field_35365_g;
				this.func_35363_g();
			} else if (par1GuiButton.id == 5) {
				++this.field_46030_z;
				if (this.field_46030_z >= WorldType.worldTypes.length) {
					this.field_46030_z = 0;
				}

				while (WorldType.worldTypes[this.field_46030_z] == null || !WorldType.worldTypes[this.field_46030_z].getCanBeCreated()) {
					++this.field_46030_z;
					if (this.field_46030_z >= WorldType.worldTypes.length) {
						this.field_46030_z = 0;
					}
				}

				this.func_35363_g();
			}
		}
	}

	protected void keyTyped(char par1, int par2) {
		if (this.textboxWorldName.isFocused() && !this.moreOptions) {
			this.textboxWorldName.textboxKeyTyped(par1, par2);
			this.localizedNewWorldText = this.textboxWorldName.getText();
		} else if (this.textboxSeed.isFocused() && this.moreOptions) {
			this.textboxSeed.textboxKeyTyped(par1, par2);
			this.seed = this.textboxSeed.getText();
		}

		if (par1 == 13) {
			this.actionPerformed((GuiButton)this.controlList.get(0));
		}

		((GuiButton)this.controlList.get(0)).enabled = this.textboxWorldName.getText().length() > 0;
		this.makeUseableName();
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		if (!this.moreOptions) {
			this.textboxWorldName.mouseClicked(par1, par2, par3);
		} else {
			this.textboxSeed.mouseClicked(par1, par2, par3);
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, var4.translateKey("selectWorld.create"), this.width / 2, 20, 16777215);
		if (!this.moreOptions) {
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.enterName"), this.width / 2 - 100, 47, 10526880);
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.resultFolder") + " " + this.folderName, this.width / 2 - 100, 85, 10526880);
			this.textboxWorldName.drawTextBox();
			this.drawString(this.fontRenderer, this.gameModeDescriptionLine1, this.width / 2 - 100, 122, 10526880);
			this.drawString(this.fontRenderer, this.gameModeDescriptionLine2, this.width / 2 - 100, 134, 10526880);
		} else {
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.enterSeed"), this.width / 2 - 100, 47, 10526880);
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.seedInfo"), this.width / 2 - 100, 85, 10526880);
			this.drawString(this.fontRenderer, var4.translateKey("selectWorld.mapFeatures.info"), this.width / 2 - 150, 122, 10526880);
			this.textboxSeed.drawTextBox();
		}

		super.drawScreen(par1, par2, par3);
	}
}
