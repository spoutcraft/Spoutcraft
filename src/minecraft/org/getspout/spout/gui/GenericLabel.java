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
	protected WidgetAnchor align = WidgetAnchor.TOP_LEFT;
	protected int hexColor = 0xFFFFFF;
	protected boolean auto = true;
	public GenericLabel(){
		
	}
	
	public int getVersion() {
		return super.getVersion() + 2;
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
		this.setAlign(WidgetAnchor.getAnchor(input.readByte()));
		this.setAuto(input.readBoolean());
		this.setHexColor(input.readInt());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		PacketUtil.writeString(output, getText());
		output.writeByte(align.getId());
		output.writeBoolean(getAuto());
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
	public boolean getAuto() {
		return auto;
	}
	
	@Override
	public Label setAuto(boolean auto) {
		this.auto = auto;
		return this;
	}

	@Override
	public WidgetAnchor getAlign() {
		return align;
	}
	
	@Override
	public Label setAlign(WidgetAnchor pos) {
		this.align = pos;
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
	
	@Override
	public double getActualWidth() {
		return auto ? getTextWidth() : super.getActualWidth();
	}
	
	public double getTextWidth() {
		double swidth = 0;
		String lines[] = getText().split("\\n");
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		for (int i = 0; i < lines.length; i++) {
			swidth = font.getStringWidth(lines[i]) > swidth ? font.getStringWidth(lines[i]) : swidth;
		}
		return swidth;
	}
	
	@Override
	public double getActualHeight() {
		return auto ? getTextHeight() : super.getActualHeight();
	}
	
	public double getTextHeight() {
		return getText().split("\\n").length * 10;
	}
	
	public void render() {
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		String lines[] = getText().split("\\n");
		
		double swidth = getTextWidth();
		double sheight = getTextHeight();
		
		GL11.glPushMatrix();
		
		double top = getScreenY();
		switch (align) {
			case CENTER_LEFT:
			case CENTER_CENTER:
			case CENTER_RIGHT:
				top += (int) ((auto ? screen.getHeight() : height) / 2 - (auto ? (sheight * (screen.getHeight() / 240f)) : height) / 2); break;
			case BOTTOM_LEFT:
			case BOTTOM_CENTER:
			case BOTTOM_RIGHT:
				top += (int) ((auto ? screen.getHeight() : height) - (auto ? (sheight * (screen.getHeight() / 240f)) : height)); break;
		}
		
		double aleft = getScreenX();
		switch (align) {
			case TOP_CENTER:
			case CENTER_CENTER:
			case BOTTOM_CENTER:
				aleft += (int) ((auto ? screen.getWidth() : width) / 2 - (auto ? (swidth * (screen.getWidth() / 427f)) : width) / 2); break;
			case TOP_RIGHT:
			case CENTER_RIGHT:
			case BOTTOM_RIGHT:
				aleft += (int) ((auto ? screen.getWidth() : width) - (auto ? (swidth * (screen.getWidth() / 427f)) : width)); break;
		}
		
		GL11.glTranslatef((float) aleft, (float) top, 0);
		if (!auto) {
			GL11.glScalef((float) (getWidth() / swidth), (float) (getHeight() / sheight), 1);
		} else if (getScale()) {
			GL11.glScalef((float) (screen.getWidth() / 427f), (float) (screen.getHeight() / 240f), 1);
		}
		for (int i = 0; i < lines.length; i++) {
			double left = 0;
			switch (align) {
				case TOP_CENTER:
				case CENTER_CENTER:
				case BOTTOM_CENTER:
					left = (swidth / 2) - (font.getStringWidth(lines[i]) / 2); break;
				case TOP_RIGHT:
				case CENTER_RIGHT:
				case BOTTOM_RIGHT:
					System.out.println("!");
					left = swidth - font.getStringWidth(lines[i]); break;
			}
			font.drawStringWithShadow(lines[i], (int) left, i * 10, getHexColor());
		}
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

}
