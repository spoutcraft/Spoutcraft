package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class GuiSleepMP extends GuiChat {
	public GuiSleepMP() {
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		StringTranslate stringtranslate = StringTranslate.getInstance();
		controlList.add(new GuiButton(1, width / 2 - 100, height - 40, stringtranslate.translateKey("multiplayer.stopSleeping")));
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void keyTyped(char c, int i) {
		if (i == 1) {
			func_22115_j();
		}
		else if (i == 28) {
			String s = message.trim();
			if (s.length() > 0) {
				mc.thePlayer.sendChatMessage(message.trim());
			}
			message = "";
		}
		else {
			super.keyTyped(c, i);
		}
	}

	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
	}

	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 1) {
			func_22115_j();
		}
		else {
			super.actionPerformed(guibutton);
		}
	}

	private void func_22115_j() {
		if (mc.thePlayer instanceof EntityClientPlayerMP) {
			NetClientHandler netclienthandler = ((EntityClientPlayerMP)mc.thePlayer).sendQueue;
			netclienthandler.addToSendQueue(new Packet19EntityAction(mc.thePlayer, 3));
		}
	}
}
