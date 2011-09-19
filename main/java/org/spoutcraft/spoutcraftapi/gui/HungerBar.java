package org.spoutcraft.spoutcraftapi.gui;

import java.util.UUID;

import org.spoutcraft.spoutcraftapi.Spoutcraft;

public class HungerBar extends GenericWidget {
	private int icons = 10;
	private boolean alwaysVisible = false;
	private int iconOffset = 8;
	private int updateCounter = 0;
	
	public HungerBar() {
		super();
		setX(427 / 2 + 82); // 295
		setY(198);
		setAnchor(WidgetAnchor.BOTTOM_CENTER);
	}

	public WidgetType getType() {
		return WidgetType.HungerBar;
	}
	
	@Override
	public double getScreenX() {
		double mid = getScreen() != null ? getScreen().getWidth() / 2 : 427 / 2D;
		double diff = super.getScreenX() - mid - 376;
		return getScreen() != null ? getScreen().getWidth() / 2D - diff : this.getX();
	}

	@Override
	public double getScreenY() {
		int diff = (int) (240 - this.getY());
		return getScreen() != null ? getScreen().getHeight() - diff : this.getY();
	}

	public UUID getId() {
		return new UUID(0, 0);
	}

	public void render() {
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}

	public boolean isAlwaysVisible() {
		return alwaysVisible;
	}

	public HungerBar setAlwaysVisible(boolean alwaysVisible) {
		this.alwaysVisible = alwaysVisible;
		return this;
	}

	public int getNumOfIcons() {
		return icons;
	}

	public HungerBar setNumOfIcons(int icons) {
		this.icons = icons;
		return this;
	}

	public int getIconOffset() {
		return iconOffset;
	}

	public HungerBar setIconOffset(int iconOffset) {
		this.iconOffset = iconOffset;
		return this;
	}
	
	@Override
	public void onTick() {
		super.onTick();
		updateCounter++;
	}
	
	public int getUpdateCounter() {
		return updateCounter;
	}
	
	@Override
	public int getVersion() {

		return super.getVersion();
	}

}
