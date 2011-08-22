package org.getspout.spout.gui;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class ChatBar extends GenericWidget implements Widget{
	private int cursorX = 4, cursorY = 240;
	public ChatBar() {
		super();
		setX(425);
		setY(238);
		setWidth(425);
		setHeight(2);
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setX(425);
		setY(238);
		setWidth(425);
		setHeight(2);
	}
	
	public WidgetType getType() {
		return WidgetType.ChatBar;
	}
	
	@Override
	public UUID getId() {
		return new UUID(0, 2);
	}
	
	@Override
	public double getScreenX() {
		int diff = (int) (427 - this.getX());
		return getScreen() != null ? getScreen().getWidth() - diff : this.getX();
	}
	
	@Override
	public double getScreenY() {
		int diff = (int) (240 - this.getY());
		return getScreen() != null ? getScreen().getHeight() - diff : this.getY();
	}
	
	public int getCursorX() {
		return cursorX;
	}
	
	public ChatBar setCursorX(int x) {
		cursorX = x;
		return this;
	}
	
	public int getCursorY() {
		int diff = 240 - cursorY;
		return (int) (getScreen() != null ? getScreen().getHeight() - diff : cursorY);
	}
	
	public ChatBar setCursorY(int y){
		cursorY = y;
		return this;
	}
	
	public void render() {
		
	}

}
