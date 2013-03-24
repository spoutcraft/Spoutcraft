/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spoutcraft.client.gui.settings.controls;

import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.Configuration;

public class TimeButton extends GenericButton {
	public TimeButton() {
		setEnabled(SpoutClient.getInstance().isTimeCheat());
		setTooltip("Time\nDefault - normal day/night cycles\nDay Only - day only\nNight Only - night only");
	}

	@Override
	public String getText() {
		switch(Configuration.getTime()) {
			case 0: return "Time: Default";
			case 1: return "Time: Night";
			case 2: return "Time: Day";
		}
		return "Unknown State: " + Configuration.getTime();
	}

	@Override
	public String getTooltip() {
		if (!isEnabled()) {
			return "This option is not allowed by your server, it is considered cheating.";
		}
		return super.getTooltip();
	}

	@Override
	public void onButtonClick() {
		Configuration.setTime(Configuration.getTime() + 1);
		if (Configuration.getTime() > 2) {
			Configuration.setTime(0);
		}
		Configuration.write();
	}
}
