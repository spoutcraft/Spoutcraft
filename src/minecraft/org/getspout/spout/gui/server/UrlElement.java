package org.getspout.spout.gui.server;

public interface UrlElement {
	/**
	 * Return if the element should be in the URL (as in, is it enabled?)
	 * @return
	 */
	boolean isActive();
	/**
	 * Should return something like 'key=value&key2=val'
	 * &-signs are inserted automagically.
	 * @return
	 */
	String getUrlPart();
}
