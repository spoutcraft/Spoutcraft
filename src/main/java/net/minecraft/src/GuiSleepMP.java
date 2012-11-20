package net.minecraft.src;

public class GuiSleepMP extends GuiChat {

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		super.initGui();
		StringTranslate var1 = StringTranslate.getInstance();
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, var1.translateKey("multiplayer.stopSleeping")));
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		if (par2 == 1) {
			this.wakeEntity();
		} else if (par2 == 28) {
			String var3 = this.inputField.getText().trim();

			if (var3.length() > 0) {
				this.mc.thePlayer.sendChatMessage(var3);
			}

			this.inputField.setText("");
			this.mc.ingameGUI.getChatGUI().resetScroll();
		} else {
			super.keyTyped(par1, par2);
		}
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 1) {
			this.wakeEntity();
		} else {
			super.actionPerformed(par1GuiButton);
		}
	}

	/**
	 * Wakes the entity from the bed
	 */
	private void wakeEntity() {
		NetClientHandler var1 = this.mc.thePlayer.sendQueue;
		var1.addToSendQueue(new Packet19EntityAction(this.mc.thePlayer, 3));
	}
}
