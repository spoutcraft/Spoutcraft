/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spout.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spoutcraft.spoutcraftapi.gui.*;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.packet.*;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

public class CustomScreen extends GuiScreen {
	protected PopupScreen screen;
	public CustomScreen(PopupScreen screen) {
		update(screen);
		this.setWorldAndResolution(SpoutClient.getHandle(), (int) screen.getWidth(), (int) screen.getHeight());
	}
	
	public void update(PopupScreen screen) {
		this.screen = screen;
	}
	
	@Override
	public void actionPerformed(GuiButton button){
	//	if (button instanceof CustomGuiButton){
		//	((EntityClientPlayerMP)this.mc.thePlayer).sendQueue.addToSendQueue(new CustomPacket(new PacketControlAction(screen, ((CustomGuiButton)button).getWidget(), 1)));
	//	}
	//	else if (button instanceof CustomGuiSlider) {
			//This fires before the new position is set, so no good
	//	}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int click) {
		screen.setMouseX(mouseY);
		screen.setMouseY(mouseY);
		if (click == 0) {
			Keyboard.enableRepeatEvents(false);
			for (Widget widget : screen.getAttachedWidgets()) {
				if (widget instanceof Control) {
					Control control = (Control)widget;
					if (control.isEnabled()
						&& control.isVisible()
						&& isInBoundingRect(control, mouseX, mouseY))
					{
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
	
	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int click) {
		screen.setMouseX(mouseX);
		screen.setMouseY(mouseY);
		for (Widget widget : screen.getAttachedWidgets()) {
			if (widget instanceof Control) {
				Control control = (Control)widget;
				if (control.isEnabled() && control.isVisible()) {
					if (click == 0) {
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
	
	@Override
	public void handleKeyboardInput() {
		boolean handled = false;
		if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
			Keyboard.enableRepeatEvents(false);
		}
		else if (Keyboard.getEventKeyState()) {
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
			super.handleKeyboardInput();
		}
	}
	
	public ArrayList<GuiButton> getControlList() {
		return (ArrayList<GuiButton>)this.controlList;
	}
	
	@Override
	public void setWorldAndResolution(Minecraft var1, int var2, int var3) {
		this.guiParticles = new GuiParticle(var1);
		this.mc = var1;
		this.fontRenderer = var1.fontRenderer;
		this.width = var2;
		this.height = var3;
		bg = (GenericGradient) new GenericGradient().setHeight(this.height).setWidth(this.width);
		this.initGui();
	}
	
	public void drawScreen(int x, int y, float z) {
		if (!screen.isTransparent()) {
			this.drawDefaultBackground();
		}
		bg.setVisible(screen.isBgVisible());
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
	
	private boolean isInBoundingRect(Widget widget, int x, int y) {
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
}