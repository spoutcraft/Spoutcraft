package org.spoutcraft.spoutcraftapi.addon;

public class InvalidAddonException extends Exception {

	private static final long serialVersionUID = 2533779376266992189L;
	private final Throwable cause;

	public InvalidAddonException(Throwable throwable) {
		this.cause = throwable;
	}

	public InvalidAddonException() {
		this.cause = null;
	}

	public Throwable getCause() {
		return this.cause;
	}
}
