/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.Control;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericGradient;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollArea;
import org.spoutcraft.spoutcraftapi.gui.Gradient;
import org.spoutcraft.spoutcraftapi.gui.RenderPriority;
import org.spoutcraft.spoutcraftapi.gui.WidgetAnchor;

import net.minecraft.src.GuiScreen;

public class GuiMinimapMenu extends GuiScreen {
	private Button doneButton = null, positionButton = null;
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
		
		positionButton = new GenericButton("Move Minimap");
		positionButton.setGeometry(width / 2 - 75, top, 150, 20);
		screen.attachWidget(spoutcraft, positionButton);
		
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
		
		control = new DeathpointsCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(left).setY(top);
		screen.attachWidget(spoutcraft, control);
		
		control = new BackgroundCheckBox().setAlign(WidgetAnchor.TOP_CENTER);
		control.setWidth(150).setHeight(20).setX(right).setY(top);
		screen.attachWidget(spoutcraft, control);
	}
	
	@Override
	public void drawScreen(int x, int y, float z) {
		drawDefaultBackground();
		
		super.drawScreen(x, y, z);
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
