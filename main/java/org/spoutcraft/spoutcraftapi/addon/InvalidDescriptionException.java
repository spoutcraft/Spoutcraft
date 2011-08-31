package org.spoutcraft.spoutcraftapi.addon;

public class InvalidDescriptionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5428943040337929212L;
	private final Throwable cause;
	private final String message;

	public InvalidDescriptionException(Throwable throwable) {
		this(throwable, "Invalid addon.yml");
	}

	public InvalidDescriptionException(String message) {
		this(null, message);
	}

	public InvalidDescriptionException(Throwable throwable, String message) {
		this.cause = null;
		this.message = message;
	}

	public InvalidDescriptionException() {
		this(null, "Invalid addon.yml");
	}

	public Throwable getCause() {
		return this.cause;
	}

	public String getMessage() {
		return this.message;
	}

}
