package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.lwjgl.opengl.GL11;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.packet.PacketUtil;

import net.minecraft.src.FontRenderer;

public class GenericLabel extends GenericWidget implements Label{
	protected String text = "";
	protected Align vAlign = Align.FIRST;
	protected Align hAlign = Align.FIRST;
	protected int hexColor = 0xFFFFFF;
	public GenericLabel(){
		
	}
	
	public int getVersion() {
		return 1;
	}
	
	public GenericLabel(String text) {
		this.text = text;
	}
	
	@Override
	public WidgetType getType() {
		return WidgetType.Label;
	}
	
	@Override
	public int getNumBytes() {
		return super.getNumBytes() + PacketUtil.getNumBytes(getText()) + 6;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		this.setText(PacketUtil.readString(input));
		this.setAlignX(Align.getAlign(input.readByte()));
		this.setAlignY(Align.getAlign(input.readByte()));
		this.setHexColor(input.readInt());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		PacketUtil.writeString(output, getText());
		output.writeByte(hAlign.getId());
		output.writeByte(vAlign.getId());
		output.writeInt(getHexColor());
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public Label setText(String text) {
		this.text = text;
		return this;
	}

	@Override
	public Align getAlignX() {
		return hAlign;
	}

	@Override
	public Align getAlignY() {
		return vAlign;
	}
	
	@Override
	public Label setAlignX(Align pos) {
		this.hAlign = pos;
		return this;
	}

	@Override
	public Label setAlignY(Align pos) {
		this.vAlign = pos;
		return this;
	}

	@Override
	public int getHexColor() {
		return hexColor;
	}

	@Override
	public Label setHexColor(int hex) {
		hexColor = hex;
		return this;
	}
	
	public void render() {
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		String lines[] = getText().split("\\n");
		double top = getScreenY();
		switch (vAlign) {
			case SECOND: top += (int) (getHeight() / 2 - lines.length * 5); break;
			case THIRD: top += (int) (getHeight() - lines.length * 10); break;
		}
		for (int i = 0; i < lines.length; i++) {
			int left = (int) getScreenX();
			switch (hAlign) {
				case SECOND: left += getWidth() / 2 - font.getStringWidth(lines[i]) / 2; break;
				case THIRD: left += getWidth() - font.getStringWidth(lines[i]); break;
			}
			font.drawStringWithShadow(lines[i], left, (int) (top + (i * 10)), getHexColor());
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
