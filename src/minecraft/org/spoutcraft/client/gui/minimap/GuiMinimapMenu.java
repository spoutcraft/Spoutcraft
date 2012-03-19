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
package org.spoutcraft.client.gui.minimap;

import org.lwjgl.input.Mouse;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.Control;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericGradient;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollArea;
import org.spoutcraft.spoutcraftapi.gui.Gradient;
import org.spoutcraft.spoutcraftapi.gui.Label;
import org.spoutcraft.spoutcraftapi.gui.RenderPriority;
import org.spoutcraft.spoutcraftapi.gui.WidgetAnchor;

import net.minecraft.src.GuiScreen;

public class GuiMinimapMenu extends GuiScreen {
	private Button doneButton = null, positionButton = null;
	private HoldableButton increaseScale, decreaseScale;
	private Label scale;
	GuiScreen parent;
	public GuiMinimapMenu(GuiScreen parent) {
		this.parent = parent;
	}
	
	public void initGui() {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		Control control;
		
		GenericScrollArea screen = new GenericScrollArea();
		screen.setHeight(height - 16 - 24 - 40).setWidth(width).setY(24).setX(0);
		getScreen().attachWidget(spoutcraft, screen);
		
		GenericLabel label = new GenericLabel("Minimap Settings");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(10);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget(spoutcraft, label);
		
		int left = (int)(width / 2  - 155);
		int right = (int)(width / 2 + 5);
		
		control = new ResetButton(this).setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(height - 30);
		getScreen().attachWidget(spoutcraft, control);

		doneButton = new GenericButton("Done");
		doneButton.setAlign(WidgetAnchor.CENTER_CENTER);
		doneButton.setX(right).setY(height - 30);
		doneButton.setHeight(20).setWidth(150);
		getScreen().attachWidget(spoutcraft, doneButton);
		
		int top = 5;
		final Color grey = new Color(0.80F, 0.80F, 0.80F, 0.65F);
		
		label = new GenericLabel("Minimap Position");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(top);
		label.setTextColor(grey);
		screen.attachWidget(spoutcraft, label);
		top += 11;

		Gradient linebreak = new GenericGradient();
		linebreak.setBottomColor(grey);
		linebreak.setTopColor(grey);
		linebreak.setX(width/2 - 318 / 2).setY(top).setHeight(3).setWidth(318);
		screen.attachWidget(spoutcraft, linebreak);
		top += 6;
		
		label = new GenericLabel("Minimap Position");
		label.setGeometry(left, top + 5, 150, 20);
		screen.attachWidget(spoutcraft, label);
		
		positionButton = new GenericButton("Move Minimap");
		positionButton.setGeometry(right, top, 150, 20);
		screen.attachWidget(spoutcraft, positionButton);
		
		top += 22;
		
		label = new GenericLabel("Size Adjust");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(top);
		screen.attachWidget(spoutcraft, label);

		scale = new GenericLabel("(" + (Math.round(MinimapConfig.getInstance().getSizeAdjust() * 100D)  / 100D) + "x)");
		size = Spoutcraft.getMinecraftFont().getTextWidth(scale.getText());
		scale.setX((int) (width / 2 - size / 2)).setY(top + 11);
		screen.attachWidget(spoutcraft, scale);
		
		increaseScale = (HoldableButton) new HoldableButton("Larger").setAlign(WidgetAnchor.TOP_CENTER);
		increaseScale.setWidth(50).setHeight(20).setX(left + 75).setY(top);
		screen.attachWidget(spoutcraft, increaseScale);
		
		decreaseScale = (HoldableButton) new HoldableButton("Smaller").setAlign(WidgetAnchor.TOP_CENTER);
		decreaseScale.setWidth(50).setHeight(20).setX(right + 25).setY(top);
		screen.attachWidget(spoutcraft, decreaseScale);
		
		top += 22;
		
		top += 5;
		
		label = new GenericLabel("Minimap Configuration");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(top);
		label.setTextColor(grey);
		screen.attachWidget(spoutcraft, label);
		top += 11;

		linebreak = new GenericGradient();
		linebreak.setBottomColor(grey);
		linebreak.setTopColor(grey);
		linebreak.setX(width/2 - 318 / 2).setY(top).setHeight(3).setWidth(318);
		screen.attachWidget(spoutcraft, linebreak);
		top += 6;
		
		control = new MinimapToggleCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new ColorToggleCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		
		control.setEnabled(false);
		control.setTooltip("Feature broken.");
		top += 22;
		
		control = new CoordsToggleCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);

		control = new SquareToggleCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		top += 22;
		
		control = new ScaleToggleCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		
		control = new DirectionsToggleCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
		
		top += 22;
		
		control = new MinimapModeButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		
		control.setEnabled(false);
		control.setTooltip("Feature broken.");

		control = new ZoomModeButton().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);

		top += 22;
		

		//TODO waypoints
	}
	
	@Override
	public void drawScreen(int x, int y, float z) {
		drawDefaultBackground();
		
		super.drawScreen(x, y, z);
	}
	
	@Override
	public void updateScreen() {
		increaseScale.onTick();
		decreaseScale.onTick();
		boolean redraw = true;
		if (increaseScale.isPressed()) {
			MinimapConfig.getInstance().setSizeAdjust(Math.min(5F, MinimapConfig.getInstance().getSizeAdjust() + 0.01F));
		}
		else if (decreaseScale.isPressed()) {
			MinimapConfig.getInstance().setSizeAdjust(Math.max(0.05F, MinimapConfig.getInstance().getSizeAdjust() - 0.01F));
		}
		else {
			redraw = false;
		}
		
		if (redraw) {
			int size;
			scale.setText("(" + (Math.round(MinimapConfig.getInstance().getSizeAdjust() * 100D)  / 100D) + "x)");
			size = Spoutcraft.getMinecraftFont().getTextWidth(scale.getText());
			scale.setX((int) (width / 2 - size / 2));
		}
		super.updateScreen();
	}
	
	@Override
	protected void buttonClicked(Button btn) {
		if (btn.equals(doneButton)) {
			MinimapConfig.getInstance().save();
			SpoutClient.getHandle().displayGuiScreen(parent);
		}
		if (btn.equals(positionButton)) {
			mc.displayGuiScreen(new GuiMoveMinimap(this));
		}
	}

}

class HoldableButton extends GenericButton {
	private boolean pressed = false;
	HoldableButton(String str) {
		super (str);
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		pressed = true;
	}
	
	public boolean isPressed() {
		return pressed && Mouse.isButtonDown(0);
	}
	
	@Override
	public void onTick() {
		if (!Mouse.isButtonDown(0)) {
			pressed = false;
		}
	}
}
