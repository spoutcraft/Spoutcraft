package net.minecraft.src;

import java.awt.Graphics;
import java.awt.image.*;

public class ImageBufferDownload
	implements ImageBuffer {
	private int imageData[];
	private int imageWidth;
	private int imageHeight;

	public ImageBufferDownload() {
	}

	public BufferedImage parseUserSkin(BufferedImage bufferedimage) {
		if (bufferedimage == null) {
			return null;
		}
		imageWidth = 64;
		imageHeight = 32;
		BufferedImage bufferedimage1 = new BufferedImage(imageWidth, imageHeight, 2);
		Graphics g = bufferedimage1.getGraphics();
		g.drawImage(bufferedimage, 0, 0, null);
		g.dispose();
		imageData = ((DataBufferInt)bufferedimage1.getRaster().getDataBuffer()).getData();
		func_884_b(0, 0, 32, 16);
		func_885_a(32, 0, 64, 32);
		func_884_b(0, 16, 64, 32);
		boolean flag = false;
		for (int i = 32; i < 64; i++) {
			for (int k = 0; k < 16; k++) {
				int i1 = imageData[i + k * 64];
				if ((i1 >> 24 & 0xff) < 128) {
					flag = true;
				}
			}
		}

		if (!flag) {
			for (int j = 32; j < 64; j++) {
				for (int l = 0; l < 16; l++) {
					int j1 = imageData[j + l * 64];
					boolean flag1;
					if ((j1 >> 24 & 0xff) < 128) {
						flag1 = true;
					}
				}
			}
		}
		return bufferedimage1;
	}

	private void func_885_a(int i, int j, int k, int l) {
		if (func_886_c(i, j, k, l)) {
			return;
		}
		for (int i1 = i; i1 < k; i1++) {
			for (int j1 = j; j1 < l; j1++) {
				imageData[i1 + j1 * imageWidth] &= 0xffffff;
			}
		}
	}

	private void func_884_b(int i, int j, int k, int l) {
		for (int i1 = i; i1 < k; i1++) {
			for (int j1 = j; j1 < l; j1++) {
				imageData[i1 + j1 * imageWidth] |= 0xff000000;
			}
		}
	}

	private boolean func_886_c(int i, int j, int k, int l) {
		for (int i1 = i; i1 < k; i1++) {
			for (int j1 = j; j1 < l; j1++) {
				int k1 = imageData[i1 + j1 * imageWidth];
				if ((k1 >> 24 & 0xff) < 128) {
					return true;
				}
			}
		}

		return false;
	}
}
