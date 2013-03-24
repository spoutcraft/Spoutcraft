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
package org.spoutcraft.client.gui;

import org.spoutcraft.api.ChatColor;
import org.spoutcraft.api.gui.GenericButton;

public abstract class SafeButton extends GenericButton {
	private String warningText = ChatColor.RED + "Really?";
	protected boolean reallyShown = false;
	private long timeout = 3000;
	protected Thread currentThread = null;

	@Override
	public String getText() {
		return reallyShown?getWarningText():super.getText();
	}

	public String getOriginalText() {
		return super.getText();
	}

	public void setWarningText(String warningText) {
		this.warningText = warningText;
	}

	public String getWarningText() {
		return warningText;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public long getTimeout() {
		return timeout;
	}

	@Override
	public void onButtonClick() {
		if (currentThread != null) {
			currentThread.interrupt();
			currentThread = null;
		}

		if (reallyShown) {
			executeAction();
			reallyShown = false;
		} else {
			reallyShown = true;
			currentThread = new Thread() {
				public void run() {
					try {
						Thread.sleep(getTimeout());
						reallyShown = false;
						currentThread = null;
					} catch(InterruptedException e) {}
				}
			};
			currentThread.start();
		}
	}

	/**
	 * Execute the unsafe action. Will be called if clicked the second time within the timeout.
	 */
	protected abstract void executeAction();
}
