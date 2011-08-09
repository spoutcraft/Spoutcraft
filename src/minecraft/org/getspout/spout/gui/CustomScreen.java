package org.getspout.spout.gui;

import org.lwjgl.input.Keyboard;

public class CustomScreen extends GuiScreen {
	protected PopupScreen screen;
	public boolean waiting = false;
	public CustomScreen(PopupScreen screen) {
		update(screen);
		this.setWorldAndResolution(SpoutClient.getHandle(), (int) screen.getWidth(), (int) screen.getHeight());
	}
	
	public void update(PopupScreen screen) {
		this.screen = screen;
	}
	
	public void testScreenClose() {
		if (waiting) {
			return;
		}	
		if (this.mc.thePlayer instanceof EntityClientPlayerMP) {
			waiting = true;
			((EntityClientPlayerMP)this.mc.thePlayer).sendQueue.addToSendQueue(new CustomPacket(new PacketScreenAction(ScreenAction.ScreenClose)));
		}
	}
	
	public void closeScreen() {
		if (!waiting){
			testScreenClose();
			return;
		}
		this.mc.displayGuiScreen(null);
		this.mc.setIngameFocus();
	}
	
	public void failedCloseScreen() {
		waiting = false;
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
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
				handled = true;
				testScreenClose();
			}
			else {
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
		}
		if (!handled) {
			super.handleKeyboardInput();
		}
	}
		
	public ArrayList<GuiButton> getControlList() {
		return (ArrayList<GuiButton>)this.controlList;
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
		int left = (int) widget.getX();
		int top = (int) widget.getY();
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