package org.spoutcraft.client.gui.minimap;

import org.lwjgl.input.Keyboard;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericTextField;
import org.spoutcraft.spoutcraftapi.gui.RenderPriority;
import org.spoutcraft.spoutcraftapi.gui.TextField;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;

public class GuiAddWaypoint extends GuiScreen{
	Button done, cancel;
	TextField name;
	public void initGui() {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		
		GenericLabel label = new GenericLabel("Create Waypoint");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(10);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget(spoutcraft, label);
		
		int left = (int)(width / 2  - 155);
		int right = (int)(width / 2 + 5);
		
		label = new GenericLabel("Waypoint Name:");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX(left).setY(70);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget(spoutcraft, label);
		
		name = new GenericTextField();
		name.setHeight(20).setWidth(300).setX(left).setY(81);
		name.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget(spoutcraft, name);
		
		label = new GenericLabel("(" + (int)Minecraft.theMinecraft.thePlayer.posX + ", " + (int)Minecraft.theMinecraft.thePlayer.posY + ", " + (int)Minecraft.theMinecraft.thePlayer.posZ + ")");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(106);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget(spoutcraft, label);
		
		done = new GenericButton("Create");
		done.setWidth(150).setHeight(20).setX(right).setY(200);
		getScreen().attachWidget(spoutcraft, done);
		
		cancel = new GenericButton("Cancel");
		cancel.setWidth(150).setHeight(20).setX(left).setY(200);
		getScreen().attachWidget(spoutcraft, cancel);
	}
	
	@Override
	public void drawScreen(int x, int y, float z) {
		drawDefaultBackground();
		name.setFocus(true);
		done.setEnabled(name.getText().length() > 0);
		super.drawScreen(x, y, z);
	}
	
	@Override
	protected void keyTyped(char ch, int keycode) {
		if (keycode == Keyboard.KEY_RETURN) {
			buttonClicked(done);
		}
		else {
			super.keyTyped(ch, keycode);
		}
	}
	
	@Override
	protected void buttonClicked(Button btn) {
		if (btn.equals(done) && done.isEnabled()) {
			MinimapConfig.getInstance().addWaypoint(MinimapUtils.getWorldName(), name.getText(), (int)Minecraft.theMinecraft.thePlayer.posX, (int)Minecraft.theMinecraft.thePlayer.posZ, true);
			MinimapConfig.getInstance().save();
			SpoutClient.getHandle().displayGuiScreen(null);
		}
		if (btn.equals(cancel)) {
			mc.displayGuiScreen(null);
		}
	}
}


