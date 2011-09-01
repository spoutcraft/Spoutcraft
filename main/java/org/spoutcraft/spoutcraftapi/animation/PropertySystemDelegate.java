package org.spoutcraft.spoutcraftapi.animation;

import org.spoutcraft.spoutcraftapi.property.PropertyInterface;
import org.spoutcraft.spoutcraftapi.property.PropertySetDelegate;

public class PropertySystemDelegate implements PropertyDelegate {
	private PropertySetDelegate delegate;
	public PropertySystemDelegate(PropertyInterface object, String property){
		delegate = object.getPropertyDelegate(property);
	}
	@Override
	public void set(Animatable value) {
		delegate.set(value);
	}
}
