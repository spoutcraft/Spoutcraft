package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.packet.PacketUtil;

public abstract class GenericControl extends GenericWidget implements Control{

	protected boolean enabled = true;
	protected Color color = new Color(0.878F, 0.878F, 0.878F);
	protected Color disabledColor = new Color(0.625F, 0.625F, 0.625F);
	public GenericControl() {
		
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 33;
	}
	
	public int getVersion() {
		return super.getVersion() + 1;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setEnabled(input.readBoolean());
		setColor(PacketUtil.readColor(input));
		setDisabledColor(PacketUtil.readColor(input));
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeBoolean(isEnabled());
		PacketUtil.writeColor(output, getColor());
		PacketUtil.writeColor(output, getDisabledColor());
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public Control setEnabled(boolean enable) {
		enabled = enable;
		return this;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public Control setColor(Color color) {
		this.color = color;
		return this;
	}

	@Override
	public Color getDisabledColor() {
		return disabledColor;
	}

	@Override
	public Control setDisabledColor(Color color) {
		this.disabledColor = color;
		return this;
	}

}
