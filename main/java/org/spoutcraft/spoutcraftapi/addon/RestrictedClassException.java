package org.spoutcraft.spoutcraftapi.addon;

public class RestrictedClassException extends ClassNotFoundException{
	
	private static final long serialVersionUID = -1772526528322888076L;
	private final String message;
	
	public RestrictedClassException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return super.toString();
	}
	
}
