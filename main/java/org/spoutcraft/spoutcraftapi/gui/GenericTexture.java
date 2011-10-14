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

public class GenericTexture extends GenericWidget implements Texture {
	protected String url = null;

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
		return super.getNumBytes() + PacketUtil.getNumBytes(getUrl());
	}

	public int getVersion() {
		return super.getVersion() + 0;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		this.setUrl(PacketUtil.readString(input));
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		PacketUtil.writeString(output, getUrl());
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
			Spoutcraft.getClient().getRenderDelegate().downloadTexture(getPlugin(), getUrl());
		}
		return this;
	}

	public Texture copy() {
		return ((Texture)super.copy()).setUrl(getUrl());
	}
}
