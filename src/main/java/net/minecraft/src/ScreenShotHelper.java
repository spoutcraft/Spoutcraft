package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.File;
//Spout Start - Unused import
//import java.nio.IntBuffer;
//Spout End
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
// Spout Start
//import org.lwjgl.opengl.GL12; // Unused import
import java.nio.ByteBuffer;
// Spout End

public class ScreenShotHelper {
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
	// Spout Start - Moved to local variables
	/*
	private static IntBuffer field_74293_b;
	private static int[] field_74294_c;
	*/
	// Spout End

	/**
	 * Takes a screenshot and saves it to the screenshots directory. Returns the filename of the screenshot.
	 */
	public static String saveScreenshot(File par0File, int par1, int par2) {
		return saveScreenshot(par0File, (String)null, par1, par2);
	}

	// Spout Start - Method renamed from func_74292_a to saveScreenshot
	public static String saveScreenshot(File file, String imageFileName, int screenWidth, int screenHeight) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(screenWidth * screenHeight * 3);
		byte[] pixelData = new byte[screenWidth * screenHeight * 3];
		int[] imageData = new int[screenWidth * screenHeight];
		try {
			// Spout Start - Renamed from file1
			File screenshotsDir = new File(file, "screenshots");
			screenshotsDir.mkdir();
			// Just intialise it up there!
			/*if (buffer == null || buffer.capacity() < i * j) {
					 buffer = BufferUtils.createByteBuffer(i * j * 3);
				}
				if (imageData == null || imageData.length < i * j * 3) {
					 pixelData = new byte[i * j * 3];
					 imageData = new int[i * j];
				}*/
			// Use constants instead of magic numbers
			GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			buffer.clear();
			GL11.glReadPixels(0, 0, screenWidth, screenHeight, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
			// Spout End
			buffer.clear();
			String s1 = (new StringBuilder()).append("").append(dateFormat.format(new Date())).toString();
			File file2;
			if (imageFileName == null) {
				for (int k = 1; (file2 = new File(screenshotsDir, (new StringBuilder()).append(s1).append(k != 1 ? (new StringBuilder()).append("_").append(k).toString() : "").append(".png").toString())).exists(); k++) {
				}
			} else {
				file2 = new File(screenshotsDir, imageFileName);
			}
			buffer.get(pixelData);
			for (int l = 0; l < screenWidth; l++) {
				for (int i1 = 0; i1 < screenHeight; i1++) {
					int j1 = l + (screenHeight - i1 - 1) * screenWidth;
					int k1 = pixelData[j1 * 3 + 0] & 0xff;
					int l1 = pixelData[j1 * 3 + 1] & 0xff;
					int i2 = pixelData[j1 * 3 + 2] & 0xff;
					int j2 = 0xff000000 | k1 << 16 | l1 << 8 | i2;
					imageData[l + i1 * screenWidth] = j2;
				}

			}

			BufferedImage bufferedimage = new BufferedImage(screenWidth, screenHeight, 1);
			bufferedimage.setRGB(0, 0, screenWidth, screenHeight, imageData, 0, screenWidth);
			ImageIO.write(bufferedimage, "png", file2);
			return (new StringBuilder()).append("Saved screenshot as ").append(file2.getName()).toString();
		} catch (Exception exception) {
			exception.printStackTrace();
			return (new StringBuilder()).append("Failed to save: ").append(exception).toString();
		}
	}

	// Spout Start - New method
	public static BufferedImage getScreenshot(int screenWidth, int screenHeight) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(screenWidth * screenHeight * 3);
		byte[] pixelData = new byte[screenWidth * screenHeight * 3];
		int[] imageData = new int[screenWidth * screenHeight];
		GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		buffer.clear();
		GL11.glReadPixels(0, 0, screenWidth, screenHeight, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
		buffer.clear();
		buffer.get(pixelData);
		for (int l = 0; l < screenWidth; l++) {
			for (int i1 = 0; i1 < screenHeight; i1++) {
				int j1 = l + (screenHeight - i1 - 1) * screenWidth;
				int k1 = pixelData[j1 * 3 + 0] & 0xff;
				int l1 = pixelData[j1 * 3 + 1] & 0xff;
				int i2 = pixelData[j1 * 3 + 2] & 0xff;
				int j2 = 0xff000000 | k1 << 16 | l1 << 8 | i2;
				imageData[l + i1 * screenWidth] = j2;
			}
		}
		BufferedImage bufferedimage = new BufferedImage(screenWidth, screenHeight, 1);
		bufferedimage.setRGB(0, 0, screenWidth, screenHeight, imageData, 0, screenWidth);
		return bufferedimage;
	}
	// Spout End

	private static File func_74290_a(File par0File) {
		String var2 = dateFormat.format(new Date()).toString();
		int var3 = 1;

		while (true) {
			File var1 = new File(par0File, var2 + (var3 == 1 ? "" : "_" + var3) + ".png");

			if (!var1.exists()) {
				return var1;
			}

			++var3;
		}
	}

	private static void func_74289_a(int[] par0ArrayOfInteger, int par1, int par2) {
		int[] var3 = new int[par1];
		int var4 = par2 / 2;

		for (int var5 = 0; var5 < var4; ++var5) {
			System.arraycopy(par0ArrayOfInteger, var5 * par1, var3, 0, par1);
			System.arraycopy(par0ArrayOfInteger, (par2 - 1 - var5) * par1, par0ArrayOfInteger, var5 * par1, par1);
			System.arraycopy(var3, 0, par0ArrayOfInteger, (par2 - 1 - var5) * par1, par1);
		}
	}

	// Spout Start
	public static BufferedImage getScreenshot(int texture, int screenWidth, int screenHeight) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(screenWidth * screenHeight * 3);
		byte[] pixelData = new byte[screenWidth * screenHeight * 3];
		int[] imageData = new int[screenWidth * screenHeight];
		buffer.clear();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
		buffer.clear();
		buffer.get(pixelData);
		for (int l = 0; l < screenWidth; l++) {
			for (int i1 = 0; i1 < screenHeight; i1++) {
				int j1 = l + (screenHeight - i1 - 1) * screenWidth;
				int k1 = pixelData[j1 * 3 + 0] & 0xff;
				int l1 = pixelData[j1 * 3 + 1] & 0xff;
				int i2 = pixelData[j1 * 3 + 2] & 0xff;
				int j2 = 0xff000000 | k1 << 16 | l1 << 8 | i2;
				imageData[l + i1 * screenWidth] = j2;
			}
		}
		BufferedImage bufferedimage = new BufferedImage(screenWidth, screenHeight, 1);
		bufferedimage.setRGB(0, 0, screenWidth, screenHeight, imageData, 0, screenWidth);
		return bufferedimage;
	}
	// Spout End
}