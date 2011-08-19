package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.packet.PacketUtil;

import net.minecraft.src.GuiButton;

public class GenericTextField extends GenericControl implements TextField{
	
	protected String text = "";
	protected int cursor = 0;
	protected int maxChars = 16;
	protected Color fieldColor = new Color(0, 0, 0);
	protected Color borderColor = new Color(0.625F, 0.625F, 0.625F);
	CustomTextField field = null;
	public GenericTextField() {

	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 40 + PacketUtil.getNumBytes(text);
	}
	
	public int getVersion() {
		return super.getVersion() + 1;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setCursorPosition(input.readInt());
		setFieldColor(PacketUtil.readColor(input));
		setBorderColor(PacketUtil.readColor(input));
		setMaximumCharacters(input.readInt());
		setText(PacketUtil.readString(input));
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(getCursorPosition());
		PacketUtil.writeColor(output, getFieldColor());
		PacketUtil.writeColor(output, getBorderColor());
		output.writeInt(getMaximumCharacters());
		PacketUtil.writeString(output, getText());
	}

	@Override
	public int getCursorPosition() {
		return cursor;
	}

	@Override
	public TextField setCursorPosition(int position) {
		this.cursor = position;
		return this;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public TextField setText(String text) {
		this.text = text;
		return this;
	}
	
	@Override
	public int getMaximumCharacters() {
		return maxChars;
	}
	
	@Override
	public TextField setMaximumCharacters(int max) {
		this.maxChars = max;
		return this;
	}

	@Override
	public Color getFieldColor() {
		return fieldColor;
	}

	@Override
	public TextField setFieldColor(Color color) {
		this.fieldColor = color;
		return this;
	}

	@Override
	public Color getBorderColor() {
		return borderColor;
	}

	@Override
	public TextField setBorderColor(Color color) {
		this.borderColor = color;
		return this;
	}
	
	@Override
	public WidgetType getType() {
		return WidgetType.TextField;
	}
	
	protected void setup(int x, int y) {
		this.x = x;
		this.y = y;
	}
	private int x;
	private int y;

	@Override
	public void render() {
		if (field == null) {
			boolean success = false;
			if (SpoutClient.getHandle().currentScreen instanceof CustomScreen) {
				CustomScreen popup = (CustomScreen)SpoutClient.getHandle().currentScreen;
				for (GuiButton control : popup.getControlList()) {
					System.out.println(control);
					if (control instanceof CustomTextField) {
						if (((CustomTextField)control).isEqual((Widget)this)) {
							field = (CustomTextField)control;
							field.updateWidget(this);
							success = true;
							break;
						}
					}
				}
				if (!success) {
					field = new CustomTextField(getScreen(), this);
					popup.getControlList().add(field);
				}
			}
		}
		field.drawButton(SpoutClient.getHandle(), x, y);
	}

}
