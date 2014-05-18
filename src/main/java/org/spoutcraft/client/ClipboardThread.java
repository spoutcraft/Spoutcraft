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
package org.spoutcraft.client;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import net.minecraft.src.EntityClientPlayerMP;

import org.spoutcraft.client.packet.builtin.PacketClipboardText;

public class ClipboardThread extends Thread {
	public ClipboardThread(EntityClientPlayerMP player) {
		this.player = player;
	}
	EntityClientPlayerMP player;
	String prevClipboardText = "";
	public void run() {
		while (!isInterrupted()) {
			try {
				sleep(1000);
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}
			try {
				Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
				if (contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					String text = null;
					try {
						text = (String) contents.getTransferData(DataFlavor.stringFlavor);
					} catch (UnsupportedFlavorException e) {
					} catch (IOException e) {
					}
					if (text != null) {
						if (!text.equals(prevClipboardText)) {
							prevClipboardText = text;
							SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketClipboardText(text));
						}
					}
				}
			} catch (Exception e2) {
			}
		}
	}
}
