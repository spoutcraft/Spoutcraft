package net.minecraft.src;

public class GuiShareToLan extends GuiScreen {
	private final GuiScreen field_74092_a;
	private GuiButton field_74090_b;
	private GuiButton field_74091_c;
	private String field_74089_d = "survival";
	private boolean field_74093_m = false;

	public GuiShareToLan(GuiScreen par1GuiScreen) {
		this.field_74092_a = par1GuiScreen;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(101, this.width / 2 - 155, this.height - 28, 150, 20, StatCollector.translateToLocal("lanServer.start")));
		this.controlList.add(new GuiButton(102, this.width / 2 + 5, this.height - 28, 150, 20, StatCollector.translateToLocal("gui.cancel")));
		this.controlList.add(this.field_74091_c = new GuiButton(104, this.width / 2 - 155, 100, 150, 20, StatCollector.translateToLocal("selectWorld.gameMode")));
		this.controlList.add(this.field_74090_b = new GuiButton(103, this.width / 2 + 5, 100, 150, 20, StatCollector.translateToLocal("selectWorld.allowCommands")));
		this.func_74088_g();
	}

	private void func_74088_g() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.field_74091_c.displayString = var1.translateKey("selectWorld.gameMode") + " " + var1.translateKey("selectWorld.gameMode." + this.field_74089_d);
		this.field_74090_b.displayString = var1.translateKey("selectWorld.allowCommands") + " ";

		if (this.field_74093_m) {
			this.field_74090_b.displayString = this.field_74090_b.displayString + var1.translateKey("options.on");
		} else {
			this.field_74090_b.displayString = this.field_74090_b.displayString + var1.translateKey("options.off");
		}
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 102) {
			this.mc.displayGuiScreen(this.field_74092_a);
		} else if (par1GuiButton.id == 104) {
			if (this.field_74089_d.equals("survival")) {
				this.field_74089_d = "creative";
			} else if (this.field_74089_d.equals("creative")) {
				this.field_74089_d = "adventure";
			} else {
				this.field_74089_d = "survival";
			}

			this.func_74088_g();
		} else if (par1GuiButton.id == 103) {
			this.field_74093_m = !this.field_74093_m;
			this.func_74088_g();
		} else if (par1GuiButton.id == 101) {
			this.mc.displayGuiScreen((GuiScreen)null);
			String var2 = this.mc.getIntegratedServer().shareToLAN(EnumGameType.getByName(this.field_74089_d), this.field_74093_m);
			String var3 = "";

			if (var2 != null) {
				var3 = this.mc.thePlayer.translateString("commands.publish.started", new Object[] {var2});
			} else {
				var3 = this.mc.thePlayer.translateString("commands.publish.failed", new Object[0]);
			}

			this.mc.ingameGUI.addChatMessage(var3);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, StatCollector.translateToLocal("lanServer.title"), this.width / 2, 50, 16777215);
		this.drawCenteredString(this.fontRenderer, StatCollector.translateToLocal("lanServer.otherPlayers"), this.width / 2, 82, 16777215);
		super.drawScreen(par1, par2, par3);
	}
}
