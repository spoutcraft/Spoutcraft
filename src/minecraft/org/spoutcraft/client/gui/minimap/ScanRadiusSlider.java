package org.spoutcraft.client.gui.minimap;

import org.spoutcraft.spoutcraftapi.ChatColor;
import org.spoutcraft.spoutcraftapi.event.screen.SliderDragEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericSlider;

public class ScanRadiusSlider extends GenericSlider {
	private static final int MIN_RADIUS = 2, MAX_RADIUS = 20;
	
	public ScanRadiusSlider() {
		updateText();
		updateSliderPosition();
		setTooltip("Sets how far the overview-map scans the map when you move.\nHigher values will mean increased lag when you move");
	}
	
	private void updateText() {
		int radius = MinimapConfig.getInstance().getScanRadius();
		ChatColor color = ChatColor.WHITE;
		if(radius > 4) {
			color = ChatColor.YELLOW;
		}
		if(radius > 7) {
			color = ChatColor.GOLD;
		}
		if(radius > 10) {
			color = ChatColor.RED;
		}
		if(radius > 15) {
			color = ChatColor.DARK_RED;
		}
		setText(color + "Scan Radius: "+ radius + " chunks");
	}
	
	private void updateSliderPosition() {
		setSliderPosition((float) (MinimapConfig.getInstance().getScanRadius() - MIN_RADIUS) / (float) (MAX_RADIUS - MIN_RADIUS));
	}

	@Override
	public void onSliderDrag(SliderDragEvent event) {
		int newradius = (int) (event.getNewPosition() * (MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS);
		MinimapConfig.getInstance().setScanRadius(newradius);
		MinimapConfig.getInstance().save();
		updateText();
		updateSliderPosition();
	}
	
	
}
