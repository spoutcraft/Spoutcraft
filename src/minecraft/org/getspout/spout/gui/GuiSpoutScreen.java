package org.getspout.spout.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiParticle;
import net.minecraft.src.GuiScreen;

import org.spoutcraft.spoutcraftapi.gui.GenericGradient;

public abstract class GuiSpoutScreen extends GuiScreen {

	protected boolean instancesCreated = false;
	
	/**
	 * Init the widgets in this method. 
	 * You should create new Instances of the widgets here, 
	 * and also set unique properties that will not change when you resize the screen.
	 * Also attach the widgets to their screen.
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
	
	@Override
	public void setWorldAndResolution(Minecraft var1, int var2, int var3) {
		this.guiParticles = new GuiParticle(var1);
		this.mc = var1;
		this.fontRenderer = var1.fontRenderer;
		this.width = var2;
		this.height = var3;
		
		if(!instancesCreated) {
			createInstances();
		}
		layoutWidgets();
		instancesCreated = true;
		
		bg = (GenericGradient) new GenericGradient().setHeight(this.height).setWidth(this.width);
	}
}
