package org.spoutcraft.spoutcraftapi.gui;

public class GenericOverlayScreen extends GenericScreen implements OverlayScreen {
	ScreenType screenType;
	
	public GenericOverlayScreen() {
	}

	@Override
	public WidgetType getType() {
		return WidgetType.OverlayScreen;
	}

	@Override
	public ScreenType getScreenType() {
		return screenType;
	}

	@Override
	public OverlayScreen setScreenType(ScreenType screenType) {
		this.screenType = screenType;
		return this;
	}
}
