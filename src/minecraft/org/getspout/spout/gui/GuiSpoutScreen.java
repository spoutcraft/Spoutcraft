package org.getspout.spout.gui;

import net.minecraft.src.GuiScreen;

public abstract class GuiSpoutScreen extends GuiScreen {
	protected boolean instancesCreated = false;
	
	/**
	 * Init the widgets in this method. 
	 * You should create new Instances of the widgets here, 
	 * and also set unique properties that will not change when you resize the screen.
	 */
	protected abstract void createInstances();
	
	/**
	 * Set the width, height and the position of the widgets here.
	 */
	protected abstract void layoutWidgets();
	
	@Override
	public void initGui() {
		if(!instancesCreated) {
			createInstances();
		}
		layoutWidgets();
		instancesCreated = true;
	}
	
	@Override
	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
	}
}
