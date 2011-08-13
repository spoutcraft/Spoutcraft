package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.GuiButton;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.packet.PacketUtil;

public class GenericButton extends GenericControl implements Button {

	protected GenericLabel label = new GenericLabel();
	protected String disabledText = "";
	protected int hoverColor = 0xffffa0;
	private CustomGuiButton button = null;
	public GenericButton() {
		
	}
	
	public GenericButton(String text) {
		setText(text);
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + label.getNumBytes() + PacketUtil.getNumBytes(getDisabledText()) + 4;
	}
	
	public int getVersion() {
		return 0;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		label.readData(input);
		setDisabledText(PacketUtil.readString(input));
		setHoverColor(input.readInt());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		label.writeData(output);
		PacketUtil.writeString(output, getDisabledText());
		output.writeInt(getHoverColor());
	}

	@Override
	public String getText() {
		return label.getText();
	}

	@Override
	public Label setText(String text) {
		label.setText(text);
		return this;
	}
	
	@Override
	public double getScreenX() {
		return super.getScreenX();
	}
	
	@Override
	public double getScreenY() {
		return super.getScreenY();
	}
	
	@Override
	public Align getAlignX() {
		return label.getAlignX();
	}

	@Override
	public Align getAlignY() {
		return label.getAlignY();
	}
	
	@Override
	public Widget setAlignX(Align pos) {
		label.setAlignX(pos);
		return this;
	}

	@Override
	public Widget setAlignY(Align pos) {
		label.setAlignY(pos);
		return this;
	}

	@Override
	public int getHexColor() {
		return label.getHexColor();
	}

	@Override
	public Label setHexColor(int hex) {
		label.setHexColor(hex);
		return this;
	}

	@Override
	public String getDisabledText() {
		return disabledText;
	}

	@Override
	public Button setDisabledText(String text) {
		disabledText = text;
		return this;
	}
	
	@Override
	public int getHoverColor() {
		return hoverColor;
	}
	
	@Override
	public Button setHoverColor(int hexColor) {
		this.hoverColor = hexColor;
		return this;
	}
	
	@Override
	public WidgetType getType() {
		return WidgetType.Button;
	}
	
	protected void setup(int x, int y) {
		this.x = x;
		this.y = y;
	}
	private int x;
	private int y;
	
	@Override
	public void render() {
		if (button == null) {
			boolean success = false;
			if (SpoutClient.getHandle().currentScreen instanceof CustomScreen) {
				CustomScreen popup = (CustomScreen)SpoutClient.getHandle().currentScreen;
				for (GuiButton control : popup.getControlList()) {
					if (control instanceof CustomGuiButton) {
						if (control.equals(this)) {
							button = (CustomGuiButton)control;
							button.updateWidget(this);
							success = true;
							break;
						}
					}
				}
				if (!success) {
					button = new CustomGuiButton(getScreen(), this);
					popup.getControlList().add(button);
				}
			}
		}
		button.drawButton(SpoutClient.getHandle(), x, y);
	}

	@Override
	public Label setAuto(boolean auto) {
		label.setAuto(auto);
		return this;
	}

	@Override
	public boolean getAuto() {
		return label.getAuto();
	}

}
