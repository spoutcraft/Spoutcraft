package net.minecraft.src;

public class MapColor {
	public static final MapColor mapColorArray[] = new MapColor[16];
	public static final MapColor airColor = new MapColor(0, 0);
	public static final MapColor grassColor = new MapColor(1, 0x7fb238);
	public static final MapColor sandColor = new MapColor(2, 0xf7e9a3);
	public static final MapColor clothColor = new MapColor(3, 0xa7a7a7);
	public static final MapColor tntColor = new MapColor(4, 0xff0000);
	public static final MapColor iceColor = new MapColor(5, 0xa0a0ff);
	public static final MapColor ironColor = new MapColor(6, 0xa7a7a7);
	public static final MapColor foliageColor = new MapColor(7, 31744);
	public static final MapColor snowColor = new MapColor(8, 0xffffff);
	public static final MapColor clayColor = new MapColor(9, 0xa4a8b8);
	public static final MapColor dirtColor = new MapColor(10, 0xb76a2f);
	public static final MapColor stoneColor = new MapColor(11, 0x707070);
	public static final MapColor waterColor = new MapColor(12, 0x4040ff);
	public static final MapColor woodColor = new MapColor(13, 0x685332);
	public final int colorValue;
	public final int colorIndex;

	private MapColor(int i, int j) {
		colorIndex = i;
		colorValue = j;
		mapColorArray[i] = this;
	}
}
