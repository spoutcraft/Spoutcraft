package org.spoutcraft.client.gui.minimap;
public class Waypoint {
	public String name;
	public int x;
	public int z;
	public boolean enabled;
	public float red = 0.0F;
	public float green = 1.0F;
	public float blue = 0.0F;

	/**
	 * Initialize a waypoint with default color.
	 * @param name
	 * @param x
	 * @param z
	 * @param enabled
	 */
	public Waypoint(String name, int x, int z, boolean enabled) {
		this.name = name;
		this.x = x;
		this.z = z;
		this.enabled = enabled;
	}

	/**
	 * Initialize a waypoint with a color.
	 * @param name Waypoint name
	 * @param x Waypoint X location
	 * @param z Waypoint Z location
	 * @param enabled Whether the waypoint should be rendered
	 * @param red red channel of color
	 * @param green green channel of color
	 * @param blue blue channel of color
	 */
	public Waypoint(String name, int x, int z, boolean enabled, float red, float green, float blue) {
		this.name = name;
		this.x = x;
		this.z = z;
		this.enabled = enabled;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	/**
	 * Convert three-float color representation to 24bit int
	 * @return converted int
	 */
	public int getColor24() {
		return ((int) (red * 0xff) << 16)
			   + ((int) (green * 0xff) << 8)
			   + ((int) (blue * 0xff));
	}

	
}
