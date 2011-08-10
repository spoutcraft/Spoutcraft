package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;

import net.minecraft.src.GuiButton;
public class GenericSlider extends GenericControl implements Slider {

	protected float slider = 0.5f;
	private CustomGuiSlider sliderControl = null;
	public GenericSlider() {
		
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 4;
	}
	
	public int getVersion() {
		return 0;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setSliderPosition(input.readFloat());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeFloat(getSliderPosition());
	}

	@Override
	public float getSliderPosition() {
		return slider;
	}

	@Override
	public Slider setSliderPosition(float value) {
		if (value > 1f) {
			value = 1f;
		}
		else if (value < 0f) {
			value = 0f;
		}
		slider = value;
		return this;
	}

	double offsetx = 0, offsety = 0;
	
	@Override
	public double getWidth() {
		double width = super.getWidth();
		if (width > 396) {
			offsetx = (width - 396) /2;
			width = 396;
		}
		return width;
	}
	
	@Override
	public double getHeight() {
		double height = super.getHeight();
		if (height > 20) {
			offsety = (height - 20) / 2;
			height = 20;
		}
		return height;
	}
	
	@Override
	public double getScreenX() {
		return super.getScreenX() + offsetx;
	}
	
	@Override
	public double getScreenY() {
		return super.getScreenY() + offsety;
	}
	
	@Override
	public WidgetType getType() {
		return WidgetType.Slider;
	}
	
	protected void setup(int x, int y) {
		this.x = x;
		this.y = y;
	}
	private int x;
	private int y;

	@Override
	public void render() {
		if (sliderControl == null) {
			boolean success = false;
			if (SpoutClient.getHandle().currentScreen instanceof CustomScreen) {
				CustomScreen popup = (CustomScreen)SpoutClient.getHandle().currentScreen;
				for (GuiButton control : popup.getControlList()) {
					if (control instanceof CustomGuiSlider) {
						if (control.equals(this)) {
							sliderControl = (CustomGuiSlider)control;
							sliderControl.updateWidget(this);
							success = true;
							break;
						}
					}
				}
				if (!success) {
					sliderControl = new CustomGuiSlider(getScreen(), this);
					popup.getControlList().add(sliderControl);
				}
			}
		}
		sliderControl.drawButton(SpoutClient.getHandle(), x, y);
	}

}
