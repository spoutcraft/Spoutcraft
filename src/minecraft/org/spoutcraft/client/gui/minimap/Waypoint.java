package org.spoutcraft.client.gui.minimap;
public class Waypoint {
	public String name;
	public int x;
	public int z;
	public boolean enabled;
	public float red = 0.0F;
	public float green = 1.0F;
	public float blue = 0.0F;
	private int[] colors = new int[] { 0xfe0000, 0xfe8000, 0xfefe00,
            0x80fe00, 0x00fe00, 0x00fe80, 0x00fefe, 0x0000fe, 0x8000fe,
            0xfe00fe, 0xfefefe, 0x7f0000, 0x7f4000, 0x7f7f00, 0x407f00,
            0x007f00, 0x007f40, 0x007f7f, 0x00007f, 0x40007f, 0x7f007f,
            0x7f7f7f
    };
	private int colorIndex = 0;

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
		
		//Initialize default color sceheme
		this.red = ((colors[colorIndex] << 16) & 255) / 255F;
		this.green = ((colors[colorIndex] << 8) & 255) / 255F;
		this.red = (colors[colorIndex] & 255) / 255F;
		colorIndex++;
		if (colorIndex > colors.length) {
			colorIndex = 0;
		}
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
