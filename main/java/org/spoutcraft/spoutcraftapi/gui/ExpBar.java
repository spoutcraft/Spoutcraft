package org.spoutcraft.spoutcraftapi.gui;

import java.util.UUID;

import org.spoutcraft.spoutcraftapi.Spoutcraft;

public class ExpBar extends GenericWidget {

	public ExpBar() {
		super();
		setX(427 / 2 - 91); // 122
		setY(211);
		setAnchor(WidgetAnchor.BOTTOM_CENTER);
	}

	@Override
	public double getScreenX() {
		double mid = getScreen() != null ? getScreen().getWidth() / 2 : 427 / 2D;
		double diff = super.getScreenX() - mid - 31;
		return getScreen() != null ? getScreen().getWidth() / 2D - diff : this.getX();
	}

	@Override
	public double getScreenY() {
		int diff = (int) (240 - this.getY());
		return getScreen() != null ? getScreen().getHeight() - diff : this.getY();
	}

	@Override
	public UUID getId() {
		return new UUID(0, 6);
	}

	public WidgetType getType() {
		return WidgetType.ExpBar;
	}

	public void render() {
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}

	@Override
	public int getVersion() {
		return super.getVersion() + 1;
	}
}
