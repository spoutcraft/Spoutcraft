package net.minecraft.src;

public class GenLayerTemperatureMix extends GenLayer {
	private GenLayer field_35505_b;
	private int field_35506_c;

	public GenLayerTemperatureMix(GenLayer genlayer, GenLayer genlayer1, int i) {
		super(0L);
		parent = genlayer1;
		field_35505_b = genlayer;
		field_35506_c = i;
	}

	public int[] getInts(int i, int j, int k, int l) {
		int ai[] = parent.getInts(i, j, k, l);
		int ai1[] = field_35505_b.getInts(i, j, k, l);
		int ai2[] = IntCache.getIntCache(k * l);
		for (int i1 = 0; i1 < k * l; i1++) {
			ai2[i1] = ai1[i1] + (BiomeGenBase.biomeList[ai[i1]].getIntTemperature() - ai1[i1]) / (field_35506_c * 2 + 1);
		}

		return ai2;
	}
}
