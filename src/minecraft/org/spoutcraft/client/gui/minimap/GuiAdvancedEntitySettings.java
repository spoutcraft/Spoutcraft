package org.spoutcraft.client.gui.minimap;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.GuiSpoutScreen;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollArea;

import net.minecraft.src.Entity;
import net.minecraft.src.GuiScreen;

public class GuiAdvancedEntitySettings extends GuiSpoutScreen {
	
	GuiMinimapMenu parent = null;
	GenericScrollArea scroll;
	GenericButton buttonDone;
	GenericLabel title;
	LinkedList<EntityVisibilityCheckbox> checks = new LinkedList<EntityVisibilityCheckbox>();

	public GuiAdvancedEntitySettings(GuiMinimapMenu guiMinimapMenu) {
		parent = guiMinimapMenu;
	}

	@Override
	protected void createInstances() {
		title = new GenericLabel("Select which mobs to show");
		buttonDone = new GenericButton("Done");
		scroll = new GenericScrollArea();
		
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		
		
		for(Class<? extends Entity> clazz:WatchedEntity.mobFaceTextures.keySet()) {
			EntityVisibilityCheckbox ch = new EntityVisibilityCheckbox(clazz);
			scroll.attachWidget(spoutcraft, ch);
			checks.add(ch);
		}
		Collections.sort(checks, new Comparator<EntityVisibilityCheckbox>() {
			@Override
			public int compare(EntityVisibilityCheckbox o1, EntityVisibilityCheckbox o2) {
				return o1.getText().compareTo(o2.getText());
			}
		});
		
		getScreen().attachWidgets(spoutcraft, buttonDone, title, scroll);
	}

	@Override
	protected void layoutWidgets() {
		title.setX(width / 2 - SpoutClient.getHandle().fontRenderer.getStringWidth(title.getText()) / 2);
		title.setY(10);
		
		scroll.setGeometry(0, 25, width, height - 25 - 30);
		
		int top = 5;
		int i = 0;
		int left = 5;
		int center = left + 100 + 5;
		int right = center + 100 + 5;
		for(EntityVisibilityCheckbox ch : checks) {
			ch.setGeometry(0, top, 100, 20);
			switch(i%3) {
			case 0:
				ch.setX(left);
				break;
			case 1:
				ch.setX(center);
				break;
			case 2:
				ch.setX(right);
				break;
			}
			i++;
			if(i%3==0) {
				top += 22;
			}
		}
		scroll.updateInnerSize(); 
		
		buttonDone.setGeometry(width - 205, height - 25, 200, 20);
	}

	@Override
	protected void buttonClicked(Button btn) {
		if(btn == buttonDone) {
			mc.displayGuiScreen(parent);
		}
	}
}
