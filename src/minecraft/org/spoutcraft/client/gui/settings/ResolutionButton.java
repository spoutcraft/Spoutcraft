/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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
package org.spoutcraft.client.gui.settings;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;

public class ResolutionButton extends GenericButton{
	public static Dimension defDimension;
	Resolution current = Resolution.fromId(ConfigReader.resolution);
	public ResolutionButton() {
		setEnabled(true);
		setTooltip("Switch the resolution of the game");
	}
	
	public static int initialize(int value) {
		Frame frame = Frame.getFrames()[0];
		defDimension = Toolkit.getDefaultToolkit().getScreenSize();
		
		Resolution res = Resolution.fromId(value);
		if (res.isValidResolution(defDimension) && res != Resolution.DEFAULT) {
			setSize(frame, res.width, res.height);
			return value;
		}
		return -1;
	}
	
	public static void setSize(Frame frame, int width, int height) {
		int insetWidth = frame.getInsets().left + frame.getInsets().right;
		int insetHeight = frame.getInsets().top + frame.getInsets().bottom;
		frame.setSize(width + insetWidth, height + insetHeight);
	}

	@Override
	public String getText() {
		return "Resolution: " + current.toString();
	}


	@Override
	public void onButtonClick(ButtonClickEvent event) {
		Frame frame = Frame.getFrames()[0];
		Dimension max = frame.getMaximumSize();
		
		boolean found = false;
		for (int i = current.id + 1; i < Resolution.values()[Resolution.values().length - 1].id; i++) {
			Resolution res = Resolution.fromId(i);
			if (res.isValidResolution(max)) {
				ConfigReader.resolution = res.id;
				
				setSize(frame, res.width, res.height);
				
				found = true;
				break;
			}
		}
		
		if (!found) {
			ConfigReader.resolution = Resolution.DEFAULT.id;
			setSize(frame, defDimension.width, defDimension.height);
		}

		frame.setLocationRelativeTo(null);
		
		ConfigReader.write();
		current = Resolution.fromId(ConfigReader.resolution);
	}
	
	public enum Resolution {
		DEFAULT(-1, -1, -1),
		SVGA(0, 800, 600),
		XGA(1, 1024, 768),
		WSVGA(2, 1024, 600),
		HD720(3, 1280, 720),
		SXGA(4, 1280, 1024),
		WXGA(5, 800, 720),
		WIDE(6, 1366, 768),
		WIDE2(7, 1440, 900),
		SXGAPLUS(8, 1400, 1050),
		WSXGA+(9, 1680, 1050),
		HD1080(10, 1920, 1080),
		WUXGA(11, 1920, 1200)
		;
		
		final int height, width, id;
		final String s;
		Resolution(final int id, final int width, final int height) {
			this.id = id;
			this.width = width;
			this.height = height;
			if (id == -1) {
				this.s = "Default";
			}
			else {
				this.s = width + "X" + height;
			}
		}
		
		public boolean isValidResolution(Dimension d) {
			return d.width >= width && d.height >= height;
		}
		
		public String toString() {
			return s;
		}
		
		public static Resolution fromId(int id) {
			for (Resolution r: values()) {
				if (r.id == id) {
					return r;
				}
			}
			return null;
		}
	}
}
