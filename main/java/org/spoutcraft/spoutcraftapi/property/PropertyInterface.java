package org.spoutcraft.spoutcraftapi.property;


public interface PropertyInterface {
	public Object getProperty(String name);
	public void setProperty(String name, Object value);
	public PropertySetDelegate getPropertyDelegate(String name);
}
