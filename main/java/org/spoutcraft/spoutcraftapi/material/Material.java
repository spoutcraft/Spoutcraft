package org.spoutcraft.spoutcraftapi.material;

public interface Material {
	
	public int getRawId();
	
	public int getRawData();
	
	public boolean hasSubtypes();
	
	public String getName();
	
	public void setName(String name);
}
