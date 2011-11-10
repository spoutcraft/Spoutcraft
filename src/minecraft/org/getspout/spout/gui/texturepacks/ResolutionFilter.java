package org.getspout.spout.gui.texturepacks;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.database.UrlElement;
import org.lwjgl.input.Keyboard;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;

public class ResolutionFilter extends GenericButton implements UrlElement {
	
	private int current = -1;
	private int possibilities[] = {
			8, 16, 32, 64, 128, 256, 512, 1024	
	};
	
	public boolean isActive() {
		return current != -1;
	}

	public String getUrlPart() {
		return "resolution="+possibilities[current];
	}

	public void clear() {
		current = 1;
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		current++;
		if(current >= possibilities.length || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			current = -1;
		}
		SpoutClient.getInstance().getTexturePacksDatabaseModel().updateUrl();
	}
	
	@Override
	public String getText() {
		if(current == -1) {
			return "Resolution: All";
		} else {
			return "Resolution: "+possibilities[current]+"x"+possibilities[current];
		}
	}

}
