package org.getspout.spout.gui.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ServerListInfo {
	public List serverList = new ArrayList();;
	public int activeCountry = 0;
	public List<String> countries = new ArrayList();
	public HashMap<String,ArrayList<ServerSlot>> countryMappings = new LinkedHashMap<String,ArrayList<ServerSlot>>();
	public int page = 0;
	public int pages = 0;

	public String status ;
}