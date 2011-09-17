package org.spoutcraft.spoutcraftapi.gui;

public class GenericOverlayScreen extends GenericScreen implements OverlayScreen {
	ScreenType screenType;
	
	public GenericOverlayScreen() {
	}

	public WidgetType getType() {
		return WidgetType.OverlayScreen;
	}

	public ScreenType getScreenType() {
		return screenType;
	}

	public OverlayScreen setScreenType(ScreenType screenType) {
		this.screenType = screenType;
		return this;
	}
}
