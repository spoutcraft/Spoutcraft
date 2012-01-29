package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class TerrainTextureManager {
	private float field_1181_a[];
	private int field_1180_b[];
	private int field_1186_c[];
	private int field_1185_d[];
	private int field_1184_e[];
	private int field_1183_f[];
	private int field_1182_g[];

	public TerrainTextureManager() {
		field_1181_a = new float[768];
		field_1180_b = new int[17408];
		field_1186_c = new int[17408];
		field_1185_d = new int[17408];
		field_1184_e = new int[17408];
		field_1183_f = new int[34];
		field_1182_g = new int[768];
		try {
			BufferedImage bufferedimage = ImageIO.read((net.minecraft.src.TerrainTextureManager.class).getResource("/terrain.png"));
			int ai[] = new int[0x10000];
			bufferedimage.getRGB(0, 0, 256, 256, ai, 0, 256);
			for (int j = 0; j < 256; j++) {
				int k = 0;
				int l = 0;
				int i1 = 0;
				int j1 = (j % 16) * 16;
				int k1 = (j / 16) * 16;
				int l1 = 0;
				for (int i2 = 0; i2 < 16; i2++) {
					for (int j2 = 0; j2 < 16; j2++) {
						int k2 = ai[j2 + j1 + (i2 + k1) * 256];
						int l2 = k2 >> 24 & 0xff;
						if (l2 > 128) {
							k += k2 >> 16 & 0xff;
							l += k2 >> 8 & 0xff;
							i1 += k2 & 0xff;
							l1++;
						}
					}

					if (l1 == 0) {
						l1++;
					}
					field_1181_a[j * 3 + 0] = k / l1;
					field_1181_a[j * 3 + 1] = l / l1;
					field_1181_a[j * 3 + 2] = i1 / l1;
				}
			}
		}
		catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
		for (int i = 0; i < 256; i++) {
			if (Block.blocksList[i] != null) {
				field_1182_g[i * 3 + 0] = Block.blocksList[i].getBlockTextureFromSide(1);
				field_1182_g[i * 3 + 1] = Block.blocksList[i].getBlockTextureFromSide(2);
				field_1182_g[i * 3 + 2] = Block.blocksList[i].getBlockTextureFromSide(3);
			}
		}
	}

	public void func_799_a(IsoImageBuffer isoimagebuffer) {
		World world = isoimagebuffer.field_1347_b;
		if (world == null) {
			isoimagebuffer.field_1351_f = true;
			isoimagebuffer.field_1352_e = true;
			return;
		}
		int i = isoimagebuffer.field_1354_c * 16;
		int j = isoimagebuffer.field_1353_d * 16;
		int k = i + 16;
		int l = j + 16;
		Chunk chunk = world.getChunkFromChunkCoords(isoimagebuffer.field_1354_c, isoimagebuffer.field_1353_d);
		if (chunk.isEmpty()) {
			isoimagebuffer.field_1351_f = true;
			isoimagebuffer.field_1352_e = true;
			return;
		}
		isoimagebuffer.field_1351_f = false;
		Arrays.fill(field_1186_c, 0);
		Arrays.fill(field_1185_d, 0);
		Arrays.fill(field_1183_f, 544);
		for (int i1 = l - 1; i1 >= j; i1--) {
			for (int j1 = k - 1; j1 >= i; j1--) {
				int k1 = j1 - i;
				int l1 = i1 - j;
				int i2 = k1 + l1;
				boolean flag = true;
				for (int j2 = 0; j2 < world.worldHeight; j2++) {
					int k2 = ((l1 - k1 - j2) + 544) - 16;
					if (k2 >= field_1183_f[i2] && k2 >= field_1183_f[i2 + 1]) {
						continue;
					}
					Block block = Block.blocksList[world.getBlockId(j1, j2, i1)];
					if (block == null) {
						flag = false;
						continue;
					}
					if (block.blockMaterial == Material.water) {
						int l2 = world.getBlockId(j1, j2 + 1, i1);
						if (l2 != 0 && Block.blocksList[l2].blockMaterial == Material.water) {
							continue;
						}
						float f1 = ((float)j2 / ((float)world.worldHeight - 1.0F)) * 0.6F + 0.4F;
						float f2 = world.getLightBrightness(j1, j2 + 1, i1) * f1;
						if (k2 < 0 || k2 >= 544) {
							continue;
						}
						int i4 = i2 + k2 * 32;
						if (i2 >= 0 && i2 <= 32 && field_1185_d[i4] <= j2) {
							field_1185_d[i4] = j2;
							field_1184_e[i4] = (int)(f2 * 127F);
						}
						if (i2 >= -1 && i2 <= 31 && field_1185_d[i4 + 1] <= j2) {
							field_1185_d[i4 + 1] = j2;
							field_1184_e[i4 + 1] = (int)(f2 * 127F);
						}
						flag = false;
						continue;
					}
					if (flag) {
						if (k2 < field_1183_f[i2]) {
							field_1183_f[i2] = k2;
						}
						if (k2 < field_1183_f[i2 + 1]) {
							field_1183_f[i2 + 1] = k2;
						}
					}
					float f = ((float)j2 / ((float)world.worldHeight - 1.0F)) * 0.6F + 0.4F;
					if (k2 >= 0 && k2 < 544) {
						int i3 = i2 + k2 * 32;
						int k3 = field_1182_g[block.blockID * 3 + 0];
						float f3 = (world.getLightBrightness(j1, j2 + 1, i1) * 0.8F + 0.2F) * f;
						int j4 = k3;
						if (i2 >= 0) {
							float f5 = f3;
							if (field_1186_c[i3] <= j2) {
								field_1186_c[i3] = j2;
								field_1180_b[i3] = 0xff000000 | (int)(field_1181_a[j4 * 3 + 0] * f5) << 16 | (int)(field_1181_a[j4 * 3 + 1] * f5) << 8 | (int)(field_1181_a[j4 * 3 + 2] * f5);
							}
						}
						if (i2 < 31) {
							float f6 = f3 * 0.9F;
							if (field_1186_c[i3 + 1] <= j2) {
								field_1186_c[i3 + 1] = j2;
								field_1180_b[i3 + 1] = 0xff000000 | (int)(field_1181_a[j4 * 3 + 0] * f6) << 16 | (int)(field_1181_a[j4 * 3 + 1] * f6) << 8 | (int)(field_1181_a[j4 * 3 + 2] * f6);
							}
						}
					}
					if (k2 < -1 || k2 >= 543) {
						continue;
					}
					int j3 = i2 + (k2 + 1) * 32;
					int l3 = field_1182_g[block.blockID * 3 + 1];
					float f4 = world.getLightBrightness(j1 - 1, j2, i1) * 0.8F + 0.2F;
					int k4 = field_1182_g[block.blockID * 3 + 2];
					float f7 = world.getLightBrightness(j1, j2, i1 + 1) * 0.8F + 0.2F;
					if (i2 >= 0) {
						float f8 = f4 * f * 0.6F;
						if (field_1186_c[j3] <= j2 - 1) {
							field_1186_c[j3] = j2 - 1;
							field_1180_b[j3] = 0xff000000 | (int)(field_1181_a[l3 * 3 + 0] * f8) << 16 | (int)(field_1181_a[l3 * 3 + 1] * f8) << 8 | (int)(field_1181_a[l3 * 3 + 2] * f8);
						}
					}
					if (i2 >= 31) {
						continue;
					}
					float f9 = f7 * 0.9F * f * 0.4F;
					if (field_1186_c[j3 + 1] <= j2 - 1) {
						field_1186_c[j3 + 1] = j2 - 1;
						field_1180_b[j3 + 1] = 0xff000000 | (int)(field_1181_a[k4 * 3 + 0] * f9) << 16 | (int)(field_1181_a[k4 * 3 + 1] * f9) << 8 | (int)(field_1181_a[k4 * 3 + 2] * f9);
					}
				}
			}
		}

		func_800_a();
		if (isoimagebuffer.field_1348_a == null) {
			isoimagebuffer.field_1348_a = new BufferedImage(32, 544, 2);
		}
		isoimagebuffer.field_1348_a.setRGB(0, 0, 32, 544, field_1180_b, 0, 32);
		isoimagebuffer.field_1352_e = true;
	}

	private void func_800_a() {
		for (int i = 0; i < 32; i++) {
			for (int j = 0; j < 544; j++) {
				int k = i + j * 32;
				if (field_1186_c[k] == 0) {
					field_1180_b[k] = 0;
				}
				if (field_1185_d[k] <= field_1186_c[k]) {
					continue;
				}
				int l = field_1180_b[k] >> 24 & 0xff;
				field_1180_b[k] = ((field_1180_b[k] & 0xfefefe) >> 1) + field_1184_e[k];
				if (l < 128) {
					field_1180_b[k] = 0x80000000 + field_1184_e[k] * 2;
				}
				else {
					field_1180_b[k] |= 0xff000000;
				}
			}
		}
	}
}
