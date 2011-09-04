package org.spoutcraft.spoutcraftapi.gui;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spoutcraft.spoutcraftapi.property.Property;
import org.spoutcraft.spoutcraftapi.property.PropertyInterface;
import org.spoutcraft.spoutcraftapi.property.PropertyObject;

public class ListWidgetItem extends PropertyObject implements PropertyInterface {
	private String title = null, text = null, iconUrl = null;
	public ListWidgetItem(String title, String text, String iconUrl)
	{
		initProperties();
		setTitle(title);
		setText(text);
		setIconUrl(iconUrl);
	}
	
	private void initProperties(){
		addProperty("title", new Property() {
			
			@Override
			public void set(Object value) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Object get() {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	@Override
	public boolean equals(Object other) {
		if(! (other instanceof ListWidgetItem)){
			return false;
		}
		ListWidgetItem li = (ListWidgetItem)other;
		return (new EqualsBuilder()).append(this.text, li.text).append(this.title, li.title).append(this.iconUrl, li.iconUrl).isEquals();
	}

	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(text).append(title).append(iconUrl).toHashCode();
	}
	
}
