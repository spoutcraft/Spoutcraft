package org.spoutcraft.spoutcraftapi.animation;

import org.spoutcraft.spoutcraftapi.property.PropertyInterface;

public class PropertyAnimation extends Animation {
	private String propertyName;
	private PropertyInterface object;
	public PropertyAnimation(PropertyInterface object, String property){
		propertyName = property;
		this.object = object;
		setValueDelegate(new PropertyDelegate(object, propertyName));
		Animatable value = (Animatable) object.getProperty(propertyName);
		setStartValue(value);
		setEndValue(value);
	}
	
	public void setProperty(String name){
		propertyName = name;
		setValueDelegate(new PropertyDelegate(object, propertyName));
	}
	
	public String getProperty(){
		return propertyName;
	}
	
	public PropertyInterface getAnimatedObject(){
		return object;
	}
	
	public void setAnimatedObject(PropertyInterface object){
		this.object = object;
		setValueDelegate(new PropertyDelegate(object, propertyName));
	}
}
