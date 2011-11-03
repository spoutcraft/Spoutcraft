package org.getspout.spout.gui.server;

import java.util.Random;

import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.RadioButton;

public class RandomButton extends SortButton {
	Random rand = new Random();
	long seed = rand.nextLong();
	
	public RandomButton() {
		super("Random", "random");
		setAllowSorting(false);
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		seed = rand.nextLong();
		super.onButtonClick(event);
	}

	
	@Override
	public String getUrlPart() {
		return "random&seed="+seed;
	}
}
