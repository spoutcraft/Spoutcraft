package org.spoutcraft.spoutcraftapi.property;

import java.util.HashMap;

public class PropertyObject {
	private HashMap<String, PropertySetDelegate> properties = new HashMap<String, PropertySetDelegate>();
	
	protected void addProperty(String name, PropertySetDelegate delegate){
		properties.put(name, delegate);
	}
	
	public Object getProperty(String name){
		if(properties.containsKey(name)){
			return properties.get(name).get();
		}
		return null;
	}
	
	public void setProperty(String name, Object value){
		if(properties.containsKey(name)){
			properties.get(name).set(value);
		}
	}
	
	public PropertySetDelegate getPropertyDelegate(String name){
		return properties.get(name);
	}
}
