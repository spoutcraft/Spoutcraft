package net.minecraft.src;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap; //Spout
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
import org.spoutcraft.spoutcraftapi.gui.*;

//Spout End

public class GuiScreen extends Gui {

	protected Minecraft mc;
	public int width;
	public int height;
	protected List controlList = new ArrayList();
	public boolean allowUserInput = false;
	protected FontRenderer fontRenderer;
	public GuiParticle guiParticles;
	private GuiButton selectedButton = null;
	//Spout Start
	public GenericGradient bg; 
	public Screen screen = null;
	//Spout End
	
	public void drawScreenPre(int x, int y, float z) {
		drawWidgets(x, y, z);
		drawScreen(x,y,z);
	}
	
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
	
	//Spout Start
	public void update(Screen screen){
		this.screen = screen;
	}
	//Spout End

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

	public ArrayList<GuiButton> getControlList() {
		return (ArrayList<GuiButton>)this.controlList;
	}
	
	//Wrap ALL the methods!!

	private void mouseClickedPre(int mouseX, int mouseY, int eventButton) {
		mouseClicked(mouseX, mouseY, eventButton); // Call vanilla method
		if(getScreen() == null) {
			return;
		}
		screen.setMouseX(mouseX);
		screen.setMouseY(mouseY);
		if (eventButton == 0) {
			Keyboard.enableRepeatEvents(false);
			for (Widget widget : screen.getAttachedWidgets()) {
				if (widget instanceof Control) {
					Control control = (Control)widget;
					if (control.isEnabled() && control.isVisible() && isInBoundingRect(control, mouseX, mouseY)) {
						control.setFocus(true);
						this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
						if (control instanceof Button) {
							SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketControlAction(screen, control, 1));
						}
						else if (control instanceof Slider) {
							//((Slider)control).setSliderPosition((float)(mouseX - (((Slider)control).getScreenX() + 4)) / (float)(((Slider)control).getWidth() - 8));
							((Slider)control).setDragging(true);
						}
						else if (control instanceof TextField) {
							((TextField)control).setCursorPosition(((TextField)control).getText().length());
						}
						break;
					}
				}
			}
		}
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
	
	private void mouseMovedOrUpPre(int mouseX, int mouseY, int eventButton) {
		mouseMovedOrUp(mouseX, mouseY, eventButton);
		if(getScreen() == null) {
			return;
		}
		screen.setMouseX(mouseX);
		screen.setMouseY(mouseY);
		for (Widget widget : screen.getAttachedWidgets()) {
			if (widget instanceof Control) {
				Control control = (Control)widget;
				if (control.isEnabled() && control.isVisible()) {
					if (eventButton == 0) {
						if (control.isFocus() && !isInBoundingRect(control, mouseX, mouseY)) { //released control
							control.setFocus(false);
						}
						if (control instanceof Slider) {
							((Slider)control).setDragging(false);
							SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketControlAction(screen, control, ((Slider)control).getSliderPosition()));
						}
					}
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
				ScreenType screen = ScreenUtil.getType(this);
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
			this.mouseClickedPre(var1, var2, Mouse.getEventButton());
		} else {
			var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
			var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			this.mouseMovedOrUpPre(var1, var2, Mouse.getEventButton());
		}

	}

	public void handleKeyboardInput() {
		//Spout Start
		boolean handled = false;
		if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
			Keyboard.enableRepeatEvents(false);
		} else if(Keyboard.getEventKeyState() && getScreen() != null) {
			boolean tab = Keyboard.getEventKey() == Keyboard.KEY_TAB;
			TextField focusedTF = null;
			ConcurrentSkipListMap<Integer, TextField> tabIndexMap = tab ? new ConcurrentSkipListMap<Integer, TextField>() : null;
			
			for (Widget widget : screen.getAttachedWidgets()) {
				if (widget instanceof TextField) {
					TextField tf = (TextField) widget;
					// handle tabbing
					// get all textfields of this screen and start looking for the next bigger tab-index
					if (tab) {
						if (tf.isFocus()) focusedTF = tf;
						tabIndexMap.put(tf.getTabIndex(), tf);
					}
					// pass typed key to text processor
					else if (tf.isEnabled() && tf.isFocus()) {
						if (tf.getTextProcessor().handleInput(Keyboard.getEventCharacter(), Keyboard.getEventKey())) {
							((EntityClientPlayerMP)Minecraft.theMinecraft.thePlayer).sendQueue.addToSendQueue(new CustomPacket(new PacketControlAction(screen, tf, tf.getText(), tf.getCursorPosition())));
						}
						handled = true;
						break;
					}
				}
			}
			
			// start looking for the next bigger tab-index
			if (tab && focusedTF != null) {
				Integer index = tabIndexMap.higherKey(focusedTF.getTabIndex());
				if(index == null) index = tabIndexMap.ceilingKey(0);
				if(index != null) {
					focusedTF.setFocus(false);
					tabIndexMap.get(index).setFocus(true);
					handled = true;
				}
			}
		}
		if (!handled) {
			//Spout - Start of vanilla code, got wrapped with this if
			if(Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == 87) {
					this.mc.toggleFullscreen();
					return;
				}
				this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			}
			//Spout - End of vanilla code
		}
		//Spout End
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

	//Spout Start
	public void drawWidgets(int x, int y, float z) {
		if(getScreen() == null) {
			return;
		}
		//Draw ALL the widgets!!
		screen.render();
		//Draw the tooltip!
		String tooltip = "";
		//Widget tooltipWidget = null;
		for (RenderPriority priority : RenderPriority.values()) {	
			for (Widget widget : screen.getAttachedWidgets()){
				if (widget.getPriority() == priority){
					if(widget.isVisible() && isInBoundingRect(widget, x, y) && !widget.getTooltip().equals("")) {
						tooltip = widget.getTooltip();
						//tooltipWidget = widget;
						//No return here, when a widget that is over it comes next, tooltip will be overwritten.
					}
				}
			}
		}
		
		if(!tooltip.equals("")) {
			GL11.glPushMatrix();
			int tooltipWidth = this.fontRenderer.getStringWidth(tooltip);
			int offsetX = 0;
			if(x + tooltipWidth + 2 > screen.getWidth()){
				offsetX = -tooltipWidth - 11;
			}
			x += 6;
			y -= 6;
			this.drawGradientRect(x - 3 + offsetX, y - 3, x + tooltipWidth + 3 + offsetX, y + 8 + 3, -1073741824, -1073741824);
			this.fontRenderer.drawStringWithShadow(tooltip, x + offsetX, y, -1);
			GL11.glPopMatrix();
		}
	}
	
	protected boolean isInBoundingRect(Widget widget, int x, int y) {
		int left = (int) widget.getScreenX();
		int top = (int) widget.getScreenY();
		int height = (int) widget.getHeight();
		int width = (int) widget.getWidth();
		int right = left+width;
		int bottom = top+height;
		if(left <= x && x < right && top <= y && y < bottom){
			return true;
		}
		return false;
	}
	
	public void onTextFieldTyped(TextField textField, char key, int keyId) {
		boolean dirty = false;
		try {
			if(textField.isEnabled() && textField.isFocus()) {
				if(key == 22) {
					String clipboard = GuiScreen.getClipboardString();
					if(clipboard == null) {
						clipboard = "";
					}

					int max = 32 - textField.getText().length();
					if(max > clipboard.length()) {
						max = clipboard.length();
					}

					if(max > 0) {
						textField.setText(textField.getText() + clipboard.substring(0, max));
						dirty = true;
					}
				}
				if (keyId == Keyboard.KEY_RIGHT && textField.getCursorPosition() < textField.getText().length()) {
					textField.setCursorPosition(textField.getCursorPosition() + 1);
					dirty = true;
				}
				else if (keyId == Keyboard.KEY_LEFT && textField.getCursorPosition() > 0) {
					textField.setCursorPosition(textField.getCursorPosition() - 1);
					dirty = true;
				}
				else if (keyId == Keyboard.KEY_DELETE && textField.getCursorPosition() > 0 && textField.getCursorPosition() < textField.getText().length()) {
					textField.setText(textField.getText().substring(0, textField.getCursorPosition()) + textField.getText().substring(textField.getCursorPosition() + 1));
					dirty = true;
				}
				else if(keyId == Keyboard.KEY_BACK && textField.getText().length() > 0 && textField.getCursorPosition() > 0) {
					textField.setText(textField.getText().substring(0, textField.getCursorPosition() - 1) + textField.getText().substring(textField.getCursorPosition()));
					textField.setCursorPosition(textField.getCursorPosition() - 1);
					dirty = true;
				}
				if(ChatAllowedCharacters.allowedCharacters.indexOf(key) > -1 && (textField.getText().length() < textField.getMaximumCharacters() || textField.getMaximumCharacters() == 0)) {
					String newText = "";
					if (textField.getCursorPosition() > 0) {
						newText += textField.getText().substring(0, textField.getCursorPosition());
					}
					newText += key;
					if (textField.getCursorPosition() < textField.getText().length()) {
						newText += textField.getText().substring(textField.getCursorPosition());
					}
					textField.setText(newText);
					textField.setCursorPosition(textField.getCursorPosition() + 1);
					dirty = true;
				}
				if (dirty) {
					((EntityClientPlayerMP)Minecraft.theMinecraft.thePlayer).sendQueue.addToSendQueue(new CustomPacket(new PacketControlAction(screen, textField, textField.getText(), textField.getCursorPosition())));
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Screen getScreen() {
		if(screen == null) {
			ScreenType type = ScreenUtil.getType(this);
			if(type == ScreenType.GAME_SCREEN || type == ScreenType.CUSTOM_SCREEN){
				return screen;
			}
			screen = new GenericOverlayScreen();
			((OverlayScreen)screen).setScreenType(type);
		}
		return screen;
	}
	//Spout End
}
