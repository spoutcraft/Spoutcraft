package org.spoutcraft.client.gui;

import org.spoutcraft.spoutcraftapi.ChatColor;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;

public abstract class SafeButton extends GenericButton {
	private String warningText = ChatColor.RED + "Really?";
	private boolean reallyShown = false;
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
	public void onButtonClick(ButtonClickEvent event) {
		if(currentThread != null) {
			currentThread.interrupt();
			currentThread = null;
		}
		if(reallyShown) {
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
