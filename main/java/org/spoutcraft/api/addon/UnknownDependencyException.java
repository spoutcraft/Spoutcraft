package org.spoutcraft.api.addon;

public class UnknownDependencyException extends Exception {

	private static final long serialVersionUID = 989022178855271278L;
	private final Throwable cause;
	private final String message;

	public UnknownDependencyException(Throwable throwable) {
		this(throwable, "Unknown dependency");
	}

	public UnknownDependencyException(String message) {
		this(null, message);
	}

	public UnknownDependencyException(Throwable throwable, String message) {
		this.cause = null;
		this.message = message;
	}

	public UnknownDependencyException() {
		this(null, "Unknown dependency");
	}

	public Throwable getCause() {
		return this.cause;
	}

	public String getMessage() {
		return this.message;
	}
}
