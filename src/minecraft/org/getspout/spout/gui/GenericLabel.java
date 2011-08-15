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
	protected boolean auto = true;
	public GenericLabel(){
		
	}
	
	public int getVersion() {
		return 2;
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
		return super.getNumBytes() + PacketUtil.getNumBytes(getText()) + 7;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		this.setText(PacketUtil.readString(input));
		this.setAlignX(Align.getAlign(input.readByte()));
		this.setAlignY(Align.getAlign(input.readByte()));
		this.setAuto(input.readBoolean());
		this.setHexColor(input.readInt());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		PacketUtil.writeString(output, getText());
		output.writeByte(hAlign.getId());
		output.writeByte(vAlign.getId());
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
		
		double swidth = 0;
		for (int i = 0; i < lines.length; i++) {
			swidth = font.getStringWidth(lines[i]) > swidth ? font.getStringWidth(lines[i]) : swidth;
		}
		double sheight = lines.length * 10;
		
		double height = getHeight();
		double width = getWidth();
		
		double top = getScreenY();
		switch (vAlign) {
			case SECOND: top += (int) ((auto ? screen.getHeight() : height) / 2 - (auto ? (sheight * (screen.getHeight() / 240f)) : height) / 2); break;
			case THIRD: top += (int) ((auto ? screen.getHeight() : height) - (auto ? (sheight * (screen.getHeight() / 240f)) : height)); break;
		}
		
		double aleft = getScreenX();
		switch (hAlign) {
			case SECOND: aleft += ((width) / 2) - ((auto ? (swidth * (screen.getWidth() / 427f)) : width) / 2); break;// - (font.getStringWidth(lines[i]) * getScreen().getWidth()) / 854f; break;
			case THIRD: aleft += (width) - (auto ? (swidth * (screen.getWidth() / 427f)) : width); break;// - (font.getStringWidth(lines[i]) * getScreen().getWidth()) / 427f; break;
		}
		
		GL11.glPushMatrix();
		GL11.glTranslatef((float) aleft, (float) top, 0);
		if (auto) {
			GL11.glScalef((float) (screen.getWidth() / 427f), (float) (screen.getHeight() / 240f), 1);
		} else {
			GL11.glScalef((float) (getWidth() / swidth), (float) (getHeight() / sheight), 1);
		}
		for (int i = 0; i < lines.length; i++) {
			double left = 0;
			switch (hAlign) {
				case SECOND: left = (swidth / 2) - (font.getStringWidth(lines[i]) / 2); break;// - (font.getStringWidth(lines[i]) * getScreen().getWidth()) / 854f; break;
				case THIRD: left = swidth - font.getStringWidth(lines[i]); break;// - (font.getStringWidth(lines[i]) * getScreen().getWidth()) / 427f; break;
			}
			font.drawStringWithShadow(lines[i], (int) left, i * 10, getHexColor());
		}
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
