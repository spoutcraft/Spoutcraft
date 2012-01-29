package net.minecraft.src;

public class MapCoord {
	public byte field_28217_a;
	public byte field_28216_b;
	public byte field_28220_c;
	public byte field_28219_d;
	final MapData field_28218_e;

	public MapCoord(MapData mapdata, byte byte0, byte byte1, byte byte2, byte byte3) {
		field_28218_e = mapdata;

		field_28217_a = byte0;
		field_28216_b = byte1;
		field_28220_c = byte2;
		field_28219_d = byte3;
	}
}
