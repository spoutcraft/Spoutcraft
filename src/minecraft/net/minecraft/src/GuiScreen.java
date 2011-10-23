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
import java.util.IdentityHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.getspout.spout.ScheduledTextFieldUpdate;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.config.ConfigReader;
import org.getspout.spout.gui.*;
import org.getspout.spout.packet.*;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.event.screen.SliderDragEvent;
import org.spoutcraft.spoutcraftapi.event.screen.TextFieldChangeEvent;
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
	private long updateTicks;
	private Scrollable holding = null;
	private Orientation holdingScrollBar = Orientation.VERTICAL;
	private long lastMouseMove = 0;
	public static int TOOLTIP_DELAY = 500;
	long renderEndNanoTime = 0L;
	protected static int limitedFramerate = 120;
	
	
	public Player getPlayer() {
		if (this.mc.thePlayer != null) {
			return (Player)this.mc.thePlayer.spoutEntity;
		}
		return null;
	}
	
	protected IdentityHashMap<TextField, ScheduledTextFieldUpdate> scheduledTextFieldUpdates = new IdentityHashMap<TextField, ScheduledTextFieldUpdate>();
	//Spout End
	
	public void drawScreenPre(int x, int y, float z) {
		drawScreen(x,y,z);
		drawWidgets(x, y, z);
		
		//Spout start
		//Limit main menu framerate to 120 FPS as long as we aren't in a game already
		if(this.mc.theWorld == null)
		{
			long sleeptime = (this.renderEndNanoTime + (long)(1000000000 / limitedFramerate) - System.nanoTime()) / 1000000L;
			if(sleeptime > 0L && sleeptime < 500L) {
				try {
					Thread.sleep(sleeptime);
				} catch (InterruptedException var12) {
					var12.printStackTrace();
				}
			}
			
			this.renderEndNanoTime = System.nanoTime();
		}
		//Spout end	
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
			//Spout start
			if (mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
			//Spout end
		}

	}
	
	//Spout Start
	public void update(Screen screen){
		if(this.screen != null) {
			for(Widget w : this.screen.getAttachedWidgets()) {
				screen.attachWidget(w);
			}
		}
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
			for (int i = 4; i>=0; i--) {
				for (Widget widget : screen.getAttachedWidgets(true)) {
					if(widget.getPriority().getId()!=i){
						continue;
					}
					if (widget instanceof Control) {
						Control control = (Control)widget;
						if (control.isEnabled() && control.isVisible() && isInBoundingRect(control, mouseX, mouseY)) {
							if(control.getScreen() instanceof Scrollable) {
								if(!isInBoundingRect(control.getScreen(), mouseX, mouseY)) {
									continue;
								}
							}
							control.setFocus(true);
							boolean handled = false;
							if (control instanceof Scrollable) {
								handled = handled || handleClickOnScrollable((Scrollable)control, mouseX, mouseY);
								if(!handled && control instanceof ListWidget) {
									handled = handled || handleClickOnListWidget((ListWidget)control, mouseX, mouseY);
								}
							} 
							if (!handled) {
								if (control instanceof Button) {
									handleButtonClick((Button)control);
									handled = true;
								}
								else if (control instanceof Slider) {
									//((Slider)control).setSliderPosition((float)(mouseX - (((Slider)control).getScreenX() + 4)) / (float)(((Slider)control).getWidth() - 8));
									((Slider)control).setDragging(true);
									handled = true;
								}
								else if (control instanceof TextField) {
									((TextField)control).setCursorPosition(((TextField)control).getText().length());
									handled = true;
								} 
							}
							if (handled) {
								this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private boolean handleClickOnListWidget(ListWidget lw, int mouseX, int mouseY) {
		int x = (int) (mouseX - lw.getActualX());
		int y = (int) (mouseY - lw.getActualY());
		int scroll = lw.getScrollPosition(Orientation.VERTICAL);
		y += scroll;
		y -= 5;
		int currentHeight = 0;
		int n = 0;
		for(ListWidgetItem item:lw.getItems()) {
			
			if(currentHeight <= y && y <= currentHeight + item.getHeight()) {
				lw.setSelection(n);
				return true;
			}
			n++;
			currentHeight += item.getHeight();
		}
		return false;
	}

	private boolean handleClickOnScrollable(Scrollable lw, int mouseX, int mouseY) {
		int x = (int) (mouseX - lw.getActualX());
		int y = (int) (mouseY - lw.getActualY());
		int scrollY = lw.getScrollPosition(Orientation.VERTICAL);
		int scrollX = lw.getScrollPosition(Orientation.HORIZONTAL);
		if(x > lw.getWidth()-16 && lw.needsScrollBar(Orientation.VERTICAL)) {
			double scrollFactor = 0;
			double p = (double)scrollY / (double)lw.getMaximumScrollPosition(Orientation.VERTICAL);
			scrollFactor = 3 + p * (lw.getViewportSize(Orientation.VERTICAL) - 16.0 - 6.0);
			if(scrollFactor <= y && y <= scrollFactor + 16) {
				holding = lw;
				holdingScrollBar = Orientation.VERTICAL;
			}
		}
		if(y > lw.getHeight() - 16 && lw.needsScrollBar(Orientation.HORIZONTAL)) {
			double scrollFactor = 0;
			double p = (double)scrollX / (double)lw.getMaximumScrollPosition(Orientation.HORIZONTAL);
			scrollFactor = 3 + p * (lw.getViewportSize(Orientation.HORIZONTAL) - 16.0 - 6.0);
			if(scrollFactor <= x && x <= scrollFactor + 16) {
				holding = lw;
				holdingScrollBar = Orientation.HORIZONTAL;
			}
		}
		if(holding != null)
		{
			return true;
		}
		return false;
	}

	private void handleButtonClick(Button control) {
		if(control instanceof CheckBox) {
			CheckBox check = (CheckBox)control;
			check.setChecked(!check.isChecked());
		}
		if(control instanceof RadioButton) {
			RadioButton radio = (RadioButton)control;
			radio.setSelected(true);
		}
		this.buttonClicked((Button)control);
		SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketControlAction(screen, control, 1));
		ButtonClickEvent event = ButtonClickEvent.getInstance(getPlayer(), screen, (Button) control);
		((Button) control).onButtonClick(event);
		SpoutClient.getInstance().getAddonManager().callEvent(event);
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
		lastMouseMove = System.currentTimeMillis();
		mouseMovedOrUp(mouseX, mouseY, eventButton);
		if(getScreen() == null) {
			return;
		}
		screen.setMouseX(mouseX);
		screen.setMouseY(mouseY);
		for (Widget widget : screen.getAttachedWidgets(true)) {
			if (widget instanceof Control) {
				Control control = (Control)widget;
				if (control.isEnabled() && control.isVisible()) {
					if (eventButton == 0) {
						if (control.isFocus() && !isInBoundingRect(control, mouseX, mouseY)) { //released control
							control.setFocus(false);
						}
						if (control instanceof Slider && ((Slider)control).isDragging()) {
							((Slider)control).setDragging(false);
							SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketControlAction(screen, control, ((Slider)control).getSliderPosition()));
							SliderDragEvent event = SliderDragEvent.getInstance(getPlayer(), screen, (Slider)control, ((Slider)control).getSliderPosition());
							((Slider)control).onSliderDrag(event);
							SpoutClient.getInstance().getAddonManager().callEvent(event);
						}
					}
				}
			}
		}
		if(holding != null && holdingScrollBar != null) {
			double p = 0;
			if(holdingScrollBar == Orientation.VERTICAL) {
				int y = (int) (mouseY - holding.getActualY());
				p = (double)y/holding.getViewportSize(Orientation.VERTICAL);
			} else {
				int x = (int) (mouseX - holding.getActualX());
				p = (double)x/holding.getViewportSize(Orientation.HORIZONTAL);
			}
			holding.setScrollPosition(holdingScrollBar, (int) ((double)holding.getMaximumScrollPosition(holdingScrollBar) * p));
			
			if(eventButton == 0) {
				holding = null;
			}
		}
	}
	
	protected boolean shouldShowTooltip() {
		return !ConfigReader.delayedTooltips || System.currentTimeMillis() - TOOLTIP_DELAY > lastMouseMove;
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
		if(!(this instanceof CustomScreen) && screen != null){
			for(Widget w:screen.getAttachedWidgets()){
				screen.removeWidget(w);
			}
		}
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
				SpoutClient.getInstance().getKeyBindingManager().pressKey(i, keyReleased, screen.getCode());
			}
		//Spout End
			this.handleKeyboardInput();
		}

	}

	//Spout Start rewritten
	public void handleMouseInput() {
		int x;
		int y;
		if(Mouse.getEventButtonState()) {
			x = Mouse.getEventX() * this.width / this.mc.displayWidth;
			y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			this.mouseClickedPre(x, y, Mouse.getEventButton());
		} else {
			x = Mouse.getEventX() * this.width / this.mc.displayWidth;
			y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			this.mouseMovedOrUpPre(x, y, Mouse.getEventButton());
		}
		int scroll = Mouse.getEventDWheel();
		if (scroll != 0) {
			Orientation axis = Orientation.VERTICAL;
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
				axis = Orientation.HORIZONTAL;
			}
			for(Widget w:getScreen().getAttachedWidgets(true)) {
				if(w != null && isInBoundingRect(w, x, y)) {
					if(w instanceof Scrollable) {
						//Stupid LWJGL not recognizing vertical scrolls :(
						Scrollable lw = (Scrollable)w;
						if(axis == Orientation.VERTICAL) {
							lw.scroll(0, -scroll / 30);
						} else {
							lw.scroll(-scroll / 30, 0);
						}
					}
				}
			}
		}
	}
	//Spout End rewritten

	public void handleKeyboardInput() {
		//Spout Start
		boolean handled = false;
		if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
			Keyboard.enableRepeatEvents(false);
		} else if(getScreen() != null) {
			boolean tab = Keyboard.getEventKey() == Keyboard.KEY_TAB;
			TextField focusedTF = null;
			ConcurrentSkipListMap<Integer, TextField> tabIndexMap = tab ? new ConcurrentSkipListMap<Integer, TextField>() : null;
			
			for (Widget widget : screen.getAttachedWidgets(true)) {
				if (widget instanceof Control) {
					Control control = (Control) widget;
					if(control.isFocus()){
						if(Keyboard.getEventKeyState()) {
							handled = control.onKeyPressed(org.spoutcraft.spoutcraftapi.gui.Keyboard.getKey(Keyboard.getEventKey()));
						} else {
							handled = control.onKeyReleased(org.spoutcraft.spoutcraftapi.gui.Keyboard.getKey(Keyboard.getEventKey()));
						}
					}
					if (handled) {
						break;
					}
				}
				if (!Keyboard.getEventKeyState()) {
					break;
				}
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
							TextFieldChangeEvent event = TextFieldChangeEvent.getInstance(getPlayer(), screen, tf, tf.getText());
							tf.onTextFieldChange(event);
							SpoutClient.getInstance().getAddonManager().callEvent(event);
							ScheduledTextFieldUpdate updateThread = null;
							if (scheduledTextFieldUpdates.containsKey(tf)) {
								updateThread =  scheduledTextFieldUpdates.get(tf);
								if (updateThread.isAlive())
									updateThread.delay();
								else
									updateThread.start();
							}
							else {
								updateThread = new ScheduledTextFieldUpdate(screen, tf);
								scheduledTextFieldUpdates.put(tf, updateThread);
								updateThread.start();
							}
						}
						handled = true;
						break;
					}
				}
				if (widget instanceof ListWidget) {
					ListWidget lw = (ListWidget)widget;
					if(lw.isEnabled() && lw.isFocus()) {
						if(Keyboard.getEventKey() == Keyboard.KEY_DOWN && Keyboard.getEventKeyState()) {
							handled = true;
							lw.shiftSelection(1);
							break;
						}
						if(Keyboard.getEventKey() == Keyboard.KEY_UP && Keyboard.getEventKeyState()) {
							handled = true;
							lw.shiftSelection(-1);
							break;
						}
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

	public void updateScreen() {
		//Spout Start
		updateTicks++;
		MCRenderDelegate.shouldRenderCursor = updateTicks / 6 % 2 == 0;
		//Spout End
	}

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
		if (shouldShowTooltip()) {
			drawTooltips(x, y);
		}
	}
		
	public void drawTooltips(int x, int y) {
		//Draw the tooltip!
		String tooltip = "";
		//Widget tooltipWidget = null;
		for (RenderPriority priority : RenderPriority.values()) {	
			for (Widget widget : screen.getAttachedWidgets(true)){ //We need ALL the tooltips now
				if (widget.getPriority() == priority){
					if(widget.isVisible() && isInBoundingRect(widget, x, y) && !widget.getTooltip().equals("")) {
						if(widget.getScreen() instanceof Scrollable) {
							if(!isInBoundingRect(widget.getScreen(), x, y)){
								continue;
							}
						}
						tooltip = widget.getTooltip();
						//tooltipWidget = widget;
						//No return here, when a widget that is over it comes next, tooltip will be overwritten.
					}
				}
			}
		}
		
		if(!tooltip.equals("")) {
			drawTooltip(tooltip, x, y);
		}
	}
	
	protected void drawTooltip(String tooltip, int x, int y) {
		GL11.glPushMatrix();
		String lines[] = tooltip.split("\n");
		int tooltipWidth = 0;
		int tooltipHeight = 8 * lines.length + 3;
		for(String line:lines) {
			tooltipWidth = Math.max(this.fontRenderer.getStringWidth(line), tooltipWidth);
		}
		int offsetX = 0;
		if(x + tooltipWidth > width){
			offsetX = -tooltipWidth - 11;
			if (offsetX + x < 0) {
				offsetX = -x;
			}
		}
		int offsetY = 0;
		if(y + tooltipHeight + 2 > height) {
			offsetY = -tooltipHeight;
			if(offsetY + y < 0) {
				offsetY = -y;
			}
		}
		
		x += 6;
		y -= 6;
		this.drawGradientRect(x - 3 + offsetX, y - 3 + offsetY, x + tooltipWidth + 3 + offsetX, y + tooltipHeight + offsetY, -1073741824, -1073741824);
		int i = 0;
		for(String line:lines) {
			this.fontRenderer.drawStringWithShadow(line, x + offsetX, y + 8 * i + offsetY, -1);
			i++;
		}
		GL11.glPopMatrix();
	}
	
	protected boolean isInBoundingRect(Widget widget, int x, int y) {
		int left = (int) widget.getActualX();
		int top = (int) widget.getActualY();
		int height = (int) widget.getHeight();
		int width = (int) widget.getWidth();
		int right = left+width;
		int bottom = top+height;
		
		if(left <= x && x < right && top <= y && y < bottom){
			return true;
		}
		return false;
	}

	protected boolean isInBoundingRect(int widgetX, int widgetY, int height, int width, int x, int y) {
		int left = widgetX;
		int top = widgetY;
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
					SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketControlAction(screen, textField, textField.getText(), textField.getCursorPosition()));
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
	
	protected void buttonClicked(Button btn){}

	
	//Spout End
}
