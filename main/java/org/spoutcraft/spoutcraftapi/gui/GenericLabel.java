/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class GenericLabel extends GenericWidget implements Label {
	protected String text = "";
	protected WidgetAnchor align = WidgetAnchor.TOP_LEFT;
	protected Color color = new Color(1, 1, 1);
	protected boolean auto = true;
	protected float scale = 1.0F;

	public GenericLabel() {

	}

	public int getVersion() {
		return super.getVersion() + 5;
	}

	public GenericLabel(String text) {
		this.text = text;
	}

	public WidgetType getType() {
		return WidgetType.Label;
	}

	@Override
	public int getNumBytes() {
		return super.getNumBytes() + PacketUtil.getNumBytes(getText()) + 11;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		this.setText(PacketUtil.readString(input));
		this.setAlign(WidgetAnchor.getAnchorFromId(input.readByte()));
		this.setAuto(input.readBoolean());
		this.setTextColor(PacketUtil.readColor(input));
		this.setScale(input.readFloat());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		PacketUtil.writeString(output, getText());
		output.writeByte(align.getId());
		output.writeBoolean(getAuto());
		PacketUtil.writeColor(output, getTextColor());
		output.writeFloat(scale);
	}

	public String getText() {
		return text;
	}

	public Label setText(String text) {
		this.text = text;
		return this;
	}

	public boolean getAuto() {
		return auto;
	}

	public Label setAuto(boolean auto) {
		this.auto = auto;
		return this;
	}

	public WidgetAnchor getAlign() {
		return align;
	}

	public Label setAlign(WidgetAnchor pos) {
		this.align = pos;
		return this;
	}

	public Color getTextColor() {
		return color;
	}

	public Label setTextColor(Color color) {
		this.color = color;
		return this;
	}
	
	
	public Label setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	public float getScale() {
		return scale;
	}

	@Override
	public double getActualWidth() {
		return auto ? getTextWidth() : super.getActualWidth();
	}

	public double getTextWidth() {
		double swidth = 0;
		String lines[] = getText().split("\\n");
		MinecraftFont font = Spoutcraft.getClient().getRenderDelegate().getMinecraftFont();
		for (int i = 0; i < lines.length; i++) {
			swidth = font.getTextWidth(lines[i]) > swidth ? font.getTextWidth(lines[i]) : swidth;
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
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}

}
