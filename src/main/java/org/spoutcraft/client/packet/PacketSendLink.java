/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org/>
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
package org.spoutcraft.client.packet;

import net.minecraft.src.Minecraft;
import net.minecraft.src.GuiConfirmOpenLink;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;

import java.io.IOException;
import java.net.URL;


public class PacketSendLink implements SpoutPacket {
    protected URL link;

    public PacketSendLink() {
        link = null;
    }

    @Override
    public void readData(SpoutInputStream input) throws IOException {
        link = new URL(input.readString());
    }

    @Override
    public void writeData(SpoutOutputStream output) throws IOException {
        throw new IOException("The client may not send a link to the server!");
    }

    @Override
    public void run(int playerId) {
    	if (link != null) {
			try {
				Minecraft.getMinecraft().displayGuiScreen(new GuiConfirmOpenLink(Minecraft.getMinecraft().currentScreen, link.toString(), 0, false));
			} catch (Exception e) { }
		}
    }

    @Override
    public void failure(int playerId) {

    }

    @Override
    public PacketType getPacketType() {
        return PacketType.PacketSendLink;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public String toString() {
        return "PacketSendLink{ version= " + getVersion() + ", link= " + (link == null ? "null" : link.toString()) + " }";
    }
}
