/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
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
package org.spoutcraft.api.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.UnsafeClass;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;

@UnsafeClass
public class GenericLabel extends GenericWidget implements BasicLabel {
	protected String text = "";
	protected WidgetAnchor align = WidgetAnchor.TOP_LEFT;
	protected Color color = new Color(1f, 1f, 1f);
	protected boolean auto = true;
	protected boolean wrapLines = false;
	protected float scale = 1.0F;
	protected String[] lines = new String[0];
	protected boolean shadow = true;

	public GenericLabel() {
	}

	public int getVersion() {
		return super.getVersion() + 6;
	}

	public GenericLabel(String text) {
		setText(text);
	}

	public WidgetType getType() {
		return WidgetType.Label;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		this.setText(input.readString());
		this.setAlign(WidgetAnchor.getAnchorFromId(input.read()));
		this.setAuto(input.readBoolean());
		this.setTextColor(input.readColor());
		this.setScale(input.readFloat());
		this.setShadow(input.readBoolean());
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.writeString(getText());
		output.write(align.getId());
		output.writeBoolean(isAuto());
		output.writeColor(getTextColor());
		output.writeFloat(scale);
		output.writeBoolean(shadow);
	}

	public String getText() {
		return text;
	}

	public Label setText(String text) {
		this.text = text;
		recalculateLines();
		return this;
	}

	public boolean isAuto() {
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
		return auto && !wrapLines ? getTextWidth() : super.getActualWidth();
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
		return auto && !wrapLines ? getTextHeight() : super.getActualHeight();
	}

	public double getTextHeight() {
		return getText().split("\\n").length * 10;
	}

	public boolean isWrapLines() {
		return wrapLines;
	}

	public GenericLabel setWrapLines(boolean wrapLines) {
		this.wrapLines = wrapLines;
		return this;
	}

	public void render() {
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}

	public String [] getLines() {
		return lines;
	}

	public void recalculateLines() {
		lines = text.split("\\n");

		if (isWrapLines()) {
			ArrayList<String> linesTmp = new ArrayList<String>(lines.length);
			for (String line:lines) {
				linesTmp.add(line);
			}
			for (int i = 0; i < linesTmp.size(); i++) {
				boolean wrapForSpace = true;
				String line = linesTmp.get(i);
				String lineTmp = new String(line);
				int brk = -1;
				while (Spoutcraft.getMinecraftFont().getTextWidth(lineTmp) > super.getWidth() && super.getWidth() >= 5 && !lineTmp.isEmpty()) {
					brk = lineTmp.lastIndexOf(" ");
					if (brk == -1) {
						brk = lineTmp.length() - 2;
						wrapForSpace = false;
					}
					lineTmp = lineTmp.substring(0, brk);
				}
				if (brk != -1) {
					linesTmp.set(i, lineTmp);
					String otherLine = line.substring(brk + (wrapForSpace?1:0), line.length());
					if (!StringUtils.isEmpty(otherLine.trim())) {
						linesTmp.add(i + 1, otherLine);
					}
				}
			}

			lines = linesTmp.toArray(new String[0]);

			if (isAuto()) {
				setHeight(lines.length * 11);
			}
		}
	}

	public Label setShadow(boolean shadow) {
		this.shadow = shadow;
		return this;
	}

	public boolean hasShadow() {
		return shadow;
	}
}
