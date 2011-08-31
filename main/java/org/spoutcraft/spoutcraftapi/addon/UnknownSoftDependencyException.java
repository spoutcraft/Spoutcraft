package org.spoutcraft.spoutcraftapi.addon;

public class UnknownSoftDependencyException extends UnknownDependencyException {

	private static final long serialVersionUID = 5721389371901775899L;

	public UnknownSoftDependencyException(Throwable throwable) {
		this(throwable, "Unknown soft dependency");
	}

	public UnknownSoftDependencyException(final String message) {
		this(null, message);
	}

	public UnknownSoftDependencyException(final Throwable throwable, final String message) {
		super(throwable, message);
	}

	public UnknownSoftDependencyException() {
		this(null, "Unknown dependency");
	}
}