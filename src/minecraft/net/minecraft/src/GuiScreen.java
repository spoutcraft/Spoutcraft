package net.minecraft.src;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiParticle;
import net.minecraft.src.Tessellator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
//Spout Start
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.*;
import org.getspout.spout.packet.*;
//Spout End

public class GuiScreen extends Gui {

	protected Minecraft mc;
	public int width;
	public int height;
	protected List controlList = new ArrayList();
	public boolean field_948_f = false;
	protected FontRenderer fontRenderer;
	public GuiParticle guiParticles;
	private GuiButton selectedButton = null;
	public GenericGradient bg; //Spout

	public void drawScreen(int var1, int var2, float var3) {
		for(int var4 = 0; var4 < this.controlList.size(); ++var4) {
			GuiButton var5 = (GuiButton)this.controlList.get(var4);
			var5.drawButton(this.mc, var1, var2);
		}

	}

	protected void keyTyped(char var1, int var2) {
		if(var2 == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}

	}

	public static String getClipboardString() {
		try {
			Transferable var0 = Toolkit.getDefaultToolkit().getSystemClipboard().getContents((Object)null);
			if(var0 != null && var0.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				String var1 = (String)var0.getTransferData(DataFlavor.stringFlavor);
				return var1;
			}
		} catch (Exception var2) {
			;
		}

		return null;
	}

	protected void mouseClicked(int var1, int var2, int var3) {
		if(var3 == 0) {
			for(int var4 = 0; var4 < this.controlList.size(); ++var4) {
				GuiButton var5 = (GuiButton)this.controlList.get(var4);
				if(var5.mousePressed(this.mc, var1, var2)) {
					this.selectedButton = var5;
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					this.actionPerformed(var5);
				}
			}
		}

	}

	protected void mouseMovedOrUp(int var1, int var2, int var3) {
		if(this.selectedButton != null && var3 == 0) {
			this.selectedButton.mouseReleased(var1, var2);
			this.selectedButton = null;
		}

	}

	protected void actionPerformed(GuiButton var1) {}

	public void setWorldAndResolution(Minecraft var1, int var2, int var3) {
		this.guiParticles = new GuiParticle(var1);
		this.mc = var1;
		this.fontRenderer = var1.fontRenderer;
		this.width = var2;
		this.height = var3;
		this.controlList.clear();
		bg = (GenericGradient) new GenericGradient().setHeight(this.height).setWidth(this.width);
		this.initGui();
	}

	public void initGui() {}

	public void handleInput() {
		while(Mouse.next()) {
			this.handleMouseInput();
		}
		//Spout Start
		while(Keyboard.next()) {
			if(mc.thePlayer instanceof EntityClientPlayerMP && SpoutClient.getInstance().isSpoutEnabled()){
				EntityClientPlayerMP player = (EntityClientPlayerMP)mc.thePlayer;
				ScreenType screen = ScreenType.getType(this);
				int i = Keyboard.getEventKey();
				boolean keyReleased = Keyboard.getEventKeyState();
				PacketKeyPress packet = new PacketKeyPress((byte)i, keyReleased, (MovementInputFromOptions)player.movementInput, screen);
				SpoutClient.getInstance().getPacketManager().sendSpoutPacket(packet);
			}
		//Spout End
			this.handleKeyboardInput();
		}

	}

	public void handleMouseInput() {
		int var1;
		int var2;
		if(Mouse.getEventButtonState()) {
			var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
			var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			this.mouseClicked(var1, var2, Mouse.getEventButton());
		} else {
			var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
			var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			this.mouseMovedOrUp(var1, var2, Mouse.getEventButton());
		}

	}

	public void handleKeyboardInput() {
		if(Keyboard.getEventKeyState()) {
			if(Keyboard.getEventKey() == 87) {
				this.mc.toggleFullscreen();
				return;
			}

			this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}

	}

	public void updateScreen() {}

	public void onGuiClosed() {}

	public void drawDefaultBackground() {
		this.drawWorldBackground(0);
	}

	public void drawWorldBackground(int var1) {
		if(this.mc.theWorld != null) {
			if (bg.isVisible()) {
				bg.render();
			}
		} else {
			this.drawBackground(var1);
		}

	}

	public void drawBackground(int var1) {
		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glDisable(2912 /*GL_FOG*/);
		Tessellator var2 = Tessellator.instance;
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/gui/background.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var3 = 32.0F;
		var2.startDrawingQuads();
		var2.setColorOpaque_I(4210752);
		var2.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, (double)((float)this.height / var3 + (float)var1));
		var2.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)((float)this.width / var3), (double)((float)this.height / var3 + (float)var1));
		var2.addVertexWithUV((double)this.width, 0.0D, 0.0D, (double)((float)this.width / var3), (double)(0 + var1));
		var2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double)(0 + var1));
		var2.draw();
	}

	public boolean doesGuiPauseGame() {
		return true;
	}

	public void deleteWorld(boolean var1, int var2) {}

	public void selectNextField() {}
}
