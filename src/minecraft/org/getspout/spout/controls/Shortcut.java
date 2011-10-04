package org.getspout.spout.controls;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Shortcut implements Serializable {
	private static final long serialVersionUID = 4365592803468257957L;
	private int key = -1;
	private String title = "";
	private ArrayList<String> commands = new ArrayList<String>();
	
	public Shortcut() {
	}
	
	public void setKey(int key) {
		this.key = key;
	}
	
	public int getKey() {
		return key;
	}
	
	public void addCommand(String cmd) {
		commands.add(cmd);
	}
	
	public List<String> getCommands() {
		return commands;
	}
	
	public void clear() {
		commands.clear();
	}
	
	public void removeCommand(int i) {
		commands.remove(i);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setCommands(ArrayList<String> commands) {
		this.commands = commands;
	}
}
