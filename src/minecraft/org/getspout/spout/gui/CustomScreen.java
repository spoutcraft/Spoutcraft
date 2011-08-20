package org.getspout.spout.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.packet.*;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import java.util.ArrayList;

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
		if (button instanceof CustomGuiButton){
			((EntityClientPlayerMP)this.mc.thePlayer).sendQueue.addToSendQueue(new CustomPacket(new PacketControlAction(screen, ((CustomGuiButton)button).getWidget(), 1)));
		}
		else if (button instanceof CustomGuiSlider) {
			//This fires before the new position is set, so no good
		}	
	}
	
	@Override
	public void handleKeyboardInput() {
		boolean handled = false;
		if(Keyboard.getEventKeyState()) {		
						for (GuiButton control : getControlList()) {
								if (control instanceof CustomTextField) {
										if (((CustomTextField)control).isFocused()) {
												((CustomTextField)control).textboxKeyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
												handled = true;
												break;
										}
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
		for (Widget widget : screen.getAttachedWidgets()) {
			if (widget instanceof GenericButton) {
				((GenericButton)widget).setup(x, y);
			}
			else if (widget instanceof GenericTextField) {
				((GenericTextField)widget).setup(x, y);
			}
			else if (widget instanceof GenericSlider) {
				((GenericSlider)widget).setup(x, y);
			}
		}
		screen.render();
		//Draw the tooltip!
		String tooltip = "";
		for (RenderPriority priority : RenderPriority.values()) {	
			for (Widget widget : screen.getAttachedWidgets()){
				if (widget.getPriority() == priority){
					if(widget.isVisible() && isInBoundingRect(widget, x, y) && !widget.getTooltip().equals("")) {
						tooltip = widget.getTooltip();
						//No return here, when a widget that is over it comes next, tooltip will be overwritten.
					}
				}
			}
		}
		
		if(!tooltip.equals("")) {
			GL11.glPushMatrix();
			int tooltipWidth = this.fontRenderer.getStringWidth(tooltip);
			this.drawGradientRect(x - 3, y - 3, x + tooltipWidth + 3, y + 8 + 3, -1073741824, -1073741824);
			this.fontRenderer.drawStringWithShadow(tooltip, x, y, -1);
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
		if(left < x && x < right && top < y && y < bottom){
			return true;
		}
		return false;
	}
}