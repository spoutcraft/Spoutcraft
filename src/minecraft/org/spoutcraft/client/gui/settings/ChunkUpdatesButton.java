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
package org.spoutcraft.client.gui.settings;

import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;

public class ChunkUpdatesButton extends AutomatedButton{
	public ChunkUpdatesButton() {
		setTooltip("Chunk updates per frame\n1 - (default) slower world loading, higher FPS\n3 - faster world loading, lower FPS\n5 - fastest world loading, lowest FPS");
	}
	
	@Override
	public String getText() {
		return "Chunk Updates: " + ConfigReader.chunkUpdates;
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.chunkUpdates++;
		if (ConfigReader.chunkUpdates > 6) {
			ConfigReader.chunkUpdates = 1;
		}
		ConfigReader.write();
	}
}
