package org.spoutcraft.spoutcraftapi.addon;

public class IllegalAddonAccessException extends RuntimeException {

	private static final long serialVersionUID = -2402883487053693113L;
	
	public IllegalAddonAccessException() {}

    public IllegalAddonAccessException(String msg) {
        super(msg);
    }
	
}
