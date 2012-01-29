package net.minecraft.src;

public class MapInfo {
	public final EntityPlayer entityplayerObj;
	public int field_28119_b[];
	public int field_28124_c[];
	private int field_28122_e;
	private int field_28121_f;
	final MapData mapDataObj;

	public MapInfo(MapData mapdata, EntityPlayer entityplayer) {
		mapDataObj = mapdata;

		field_28119_b = new int[128];
		field_28124_c = new int[128];
		field_28122_e = 0;
		field_28121_f = 0;
		entityplayerObj = entityplayer;
		for (int i = 0; i < field_28119_b.length; i++) {
			field_28119_b[i] = 0;
			field_28124_c[i] = 127;
		}
	}
}
