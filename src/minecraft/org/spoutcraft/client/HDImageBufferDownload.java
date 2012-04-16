package org.spoutcraft.client;

import java.awt.Graphics;
import java.awt.image.*;

import net.minecraft.src.ImageBuffer;

public class HDImageBufferDownload implements ImageBuffer {
	private int imageData[];
	private int imageWidth;
	private int imageHeight;

	public BufferedImage parseUserSkin(BufferedImage par1BufferedImage) {
		if (par1BufferedImage == null) {
			return null;
		}

		imageWidth = par1BufferedImage.getWidth(null);
		imageHeight = imageWidth / 2;
		BufferedImage bufferedimage1 = new BufferedImage(imageWidth, imageHeight, 2);
		Graphics g = bufferedimage1.getGraphics();
		g.drawImage(par1BufferedImage, 0, 0, null);
		g.dispose();
		imageData = ((DataBufferInt)bufferedimage1.getRaster().getDataBuffer()).getData();
		func_884_b(0, 0, imageWidth / 2, imageHeight / 2);
		func_885_a(imageWidth / 2, 0, imageWidth, imageHeight);
		func_884_b(0, imageHeight / 2, imageWidth, imageHeight);

		return par1BufferedImage;
	}

	private void func_885_a(int par1, int par2, int par3, int par4) {
		if (func_886_c(par1, par2, par3, par4)) {
			return;
		}

		for (int i = par1; i < par3; i++) {
			for (int j = par2; j < par4; j++) {
				imageData[i + j * imageWidth] &= 0xffffff;
			}
		}
	}

	private void func_884_b(int par1, int par2, int par3, int par4) {
		for (int i = par1; i < par3; i++) {
			for (int j = par2; j < par4; j++) {
				imageData[i + j * imageWidth] |= 0x00000000;
			}
		}
	}

	private boolean func_886_c(int par1, int par2, int par3, int par4) {
		for (int i = par1; i < par3; i++) {
			for (int j = par2; j < par4; j++) {
				int k = imageData[i + j * imageWidth];

				if ((k >> 24 & 0xff) < 128) {
					return true;
				}
			}
		}

		return false;
	}
}

