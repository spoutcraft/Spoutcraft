/*
 * This file is part of SpoutcraftAPI (http://wiki.getspout.org/).
 * 
 * SpoutcraftAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutcraftAPI is distributed in the hope that it will be useful,
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
import org.spoutcraft.spoutcraftapi.UnsafeClass;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

@UnsafeClass
public class GenericTexture extends GenericWidget implements Texture {
	protected String url = null;
	protected boolean drawAlpha = false;
	protected WidgetAnchor align = WidgetAnchor.SCALE;
	protected int top;
	protected int left;

	public GenericTexture() {

	}

	public GenericTexture(String url) {
		this.url = url;
	}

	public WidgetType getType() {
		return WidgetType.Texture;
	}

	@Override
	public int getNumBytes() {
		return super.getNumBytes() + PacketUtil.getNumBytes(getUrl()) + 6;
	}

	@Override
	public int getVersion() {
		return super.getVersion() + 2;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		this.setUrl(PacketUtil.readString(input));
		this.setDrawAlphaChannel(input.readBoolean());
		setAlign(WidgetAnchor.getAnchorFromId(input.readByte()));
		setTop(input.readShort());
		setLeft(input.readShort());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		PacketUtil.writeString(output, getUrl());
		output.writeBoolean(isDrawingAlphaChannel());
		output.writeByte(align.getId());
		output.writeShort(top);
		output.writeShort(left);
	}

	public void render() {
		Spoutcraft.getClient().getRenderDelegate().render(this);
	}

	public String getUrl() {
		return url;
	}

	public Texture setUrl(String url) {
		this.url = url;
		if (getUrl() != null) {
			Spoutcraft.getClient().getRenderDelegate().downloadTexture(getAddon().getDescription().getName(), getUrl());
		}
		return this;
	}

	@Override
	public Texture copy() {
		return ((Texture)super.copy()) //
				.setUrl(getUrl()) //
				.setTop(getTop()) //
				.setLeft(getLeft());
	}

	public boolean isDrawingAlphaChannel() {
		return drawAlpha;
	}

	public Texture setDrawAlphaChannel(boolean draw) {
		this.drawAlpha = draw;
		return this;
	}

	public Texture setAlign(WidgetAnchor align) {
		if (align != null && getAlign() != align) {
			this.align = align;
		}
		return this;
	}

	public WidgetAnchor getAlign() {
		return align;
	}

	public Texture setTop(int top) {
		if (getTop() != top) {
			this.top = top;
		}
		return this;
	}

	public int getTop() {
		return top;
	}

	public Texture setLeft(int left) {
		if (getLeft() != left) {
			this.left = left;
		}
		return this;
	}

	public int getLeft() {
		return left;
	}
}
