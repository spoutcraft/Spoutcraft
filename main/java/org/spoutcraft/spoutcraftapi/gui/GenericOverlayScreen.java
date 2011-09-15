package org.spoutcraft.spoutcraftapi.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class GenericOverlayScreen extends GenericScreen implements OverlayScreen {

	public GenericOverlayScreen() {
		System.out.println("Created OverlayScreen");
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 4;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		screenType = ScreenType.getType(input.readInt());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.write(screenType.getCode());
	}

	ScreenType screenType;
	

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
