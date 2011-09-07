package org.spoutcraft.spoutcraftapi.gui;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spoutcraft.spoutcraftapi.animation.Animatable;
import org.spoutcraft.spoutcraftapi.property.Property;
import org.spoutcraft.spoutcraftapi.property.PropertyInterface;
import org.spoutcraft.spoutcraftapi.property.PropertyObject;

public class Rectangle extends PropertyObject implements PropertyInterface, Animatable {
	int width, height, x, y;
	
	public Rectangle(int x, int y, int width, int height){
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		
		initProperties();
	}
	
	private void initProperties() {
		addProperty("x", new Property() {
			public void set(Object value) {
				setX((Integer)value);
			}
			public Object get() {
				return getX();
			}
		});
		addProperty("y", new Property() {
			public void set(Object value) {
				setY((Integer)value);
			}
			public Object get() {
				return getY();
			}
		});
		addProperty("width", new Property() {
			public void set(Object value) {
				setWidth((Integer)value);
			}
			public Object get() {
				return getWidth();
			}
		});
		addProperty("height", new Property() {
			public void set(Object value) {
				setHeight((Integer)value);
			}
			public Object get() {
				return getHeight();
			}
		});
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Animatable getValueAt(double p, Animatable startValue, Animatable endValue) {
		int w,h,x,y;
		Rectangle p1 = (Rectangle)startValue;
		Rectangle p2 = (Rectangle)endValue;
		h = p1.height;
		w = p1.width;
		x = p1.x;
		y = p1.y;
		h += (h-p2.height)*p;
		w += (w-p2.width)*p;
		x += (x-p2.x)*p;
		y += (y-p2.y)*p;
		return new Rectangle(x, y, w, h);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Rectangle){
			Rectangle other = (Rectangle)obj;
			return (new EqualsBuilder()).append(width, other.width).append(height, other.height).append(x, other.x).append(y, other.y).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(width).append(height).append(x).append(y).toHashCode();
	}
}
