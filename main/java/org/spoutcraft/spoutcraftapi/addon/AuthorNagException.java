package org.spoutcraft.spoutcraftapi.addon;

public class AuthorNagException extends RuntimeException {
	private static final long serialVersionUID = 7865800524327635948L;
	private final String message;

	/**
	 * Constructs a new UnknownDependencyException based on the given Exception
	 * 
	 * @param message
	 *            Brief message explaining the cause of the exception
	 * @param throwable
	 *            Exception that triggered this Exception
	 */
	public AuthorNagException(final String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
