package org.spoutcraft.client.gui.chat;

import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.SliderDragEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericSlider;

public class ChatOpacitySlider extends GenericSlider {
	public ChatOpacitySlider() {
		setSliderPosition(ConfigReader.chatOpacity);
		updateText();
	}
	
	private void updateText() {
		setText("Background opacity: "+ (int) (getSliderPosition()*100) + "%");
	}
	
	@Override
	public void onSliderDrag(SliderDragEvent event) {
		ConfigReader.chatOpacity = event.getNewPosition();
		updateText();
	}
}
