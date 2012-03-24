package net.minecraft.src;

public class GuiSleepMP extends GuiChat {
	public void initGui() {
		super.initGui();
		StringTranslate var1 = StringTranslate.getInstance();
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, var1.translateKey("multiplayer.stopSleeping")));
	}

	protected void keyTyped(char par1, int par2) {
		if (par2 == 1) {
			this.wakeEntity();
		} else if (par2 == 28) {
			String var3 = this.message.trim();
			if (var3.length() > 0) {
				this.mc.thePlayer.sendChatMessage(var3);
			}

			this.message = "";
		} else {
			super.keyTyped(par1, par2);
		}
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 1) {
			this.wakeEntity();
		} else {
			super.actionPerformed(par1GuiButton);
		}
	}

	private void wakeEntity() {
		if (this.mc.thePlayer instanceof EntityClientPlayerMP) {
			NetClientHandler var1 = ((EntityClientPlayerMP)this.mc.thePlayer).sendQueue;
			var1.addToSendQueue(new Packet19EntityAction(this.mc.thePlayer, 3));
		}
	}
}
