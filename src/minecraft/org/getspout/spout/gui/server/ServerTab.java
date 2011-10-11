package org.getspout.spout.gui.server;

public class ServerTab {
	
	public String title;
	public String url;
	public boolean pages;
	
	public ServerTab(String title, String url, boolean pages) {
		this.title = title;
		this.url = url;
		this.pages = pages;
	}
	
	public ServerTab(String title, String url) {
		this(title, url, false);
	}
	
}