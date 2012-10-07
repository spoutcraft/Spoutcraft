package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.spoutcraft.client.config.Configuration;

public class ScreenShotHelper {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
	// Spout Start -- moved to be local variables
	/*private static ByteBuffer buffer;
	private static byte[] pixelData;
	private static int[] imageData;*/
	// Spout End

	public static String saveScreenshot(File par0File, int par1, int par2) {
		if(Configuration.isResizeScreenshots()) return saveResizedScreenshot(par0File, Configuration.getResizedScreenshotWidth(), Configuration.getResizedScreenshotHeight());
		else return saveScreenshot(par0File, (String)null, par1, par2);
	}

	// Spout Start - method renamed from func_35879_a to saveScreenshot
	public static String saveScreenshot(File file, String imageFileName, int screenWidth, int screenHeight) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(screenWidth * screenHeight * 3);
		byte[] pixelData = new byte[screenWidth * screenHeight * 3];
		int[] imageData = new int[screenWidth * screenHeight];
		try {
			// Spout -- renamed from file1
			File screenshotsDir = new File(file, "screenshots");
			screenshotsDir.mkdir();
			// Spout Start -- just intialise it up there!
			/*if (buffer == null || buffer.capacity() < i * j) {
					 buffer = BufferUtils.createByteBuffer(i * j * 3);
				}
				if (imageData == null || imageData.length < i * j * 3) {
					 pixelData = new byte[i * j * 3];
					 imageData = new int[i * j];
				}*/
			// Spout End
			// Spout Start -- use constants instead of magic numbers
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

	// Spout Start -- new method
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
	
	public static String saveResizedScreenshot(File base, int width, int height) {
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexEnvf( GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE );
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0 , 3, width, height, 0 , GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		
		int buffer = EXTFramebufferObject.glGenFramebuffersEXT();
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, buffer);
		EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, texture, 0);
		
		int renderbuffer = EXTFramebufferObject.glGenRenderbuffersEXT();
		EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, renderbuffer);
		EXTFramebufferObject.glRenderbufferStorageEXT( EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL11.GL_DEPTH_COMPONENT, width, height);
		EXTFramebufferObject.glFramebufferRenderbufferEXT( EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, renderbuffer);
		
		int oldwidth = Minecraft.theMinecraft.displayWidth;
		int oldheight = Minecraft.theMinecraft.displayHeight;
		//resize
		Minecraft.theMinecraft.resize(width, height);
		//draw world
		Minecraft.theMinecraft.entityRenderer.renderWorld(0, 0);
		//draw gui
		ScaledResolution res = new ScaledResolution(Minecraft.theMinecraft.gameSettings, width, height);
		int renderwidth = Mouse.getX() * res.getScaledWidth() / width;
		int renderheight = res.getScaledHeight() - Mouse.getY() * res.getScaledHeight() / height - 1;
		if (!Minecraft.theMinecraft.gameSettings.hideGUI || Minecraft.theMinecraft.currentScreen != null) {
			Minecraft.theMinecraft.ingameGUI.renderGameOverlay(0, Minecraft.theMinecraft.currentScreen != null, renderwidth, renderheight);
		}
		if (Minecraft.theMinecraft.currentScreen != null) {
			GL11.glClear(256);
			// Spout Start
			Minecraft.theMinecraft.currentScreen.drawScreenPre(renderwidth, renderheight, 0);
			// Spout End
			if (Minecraft.theMinecraft.currentScreen != null && Minecraft.theMinecraft.currentScreen.guiParticles != null) {
				Minecraft.theMinecraft.currentScreen.guiParticles.draw(0);
			}
		}
		//return to old size
		Minecraft.theMinecraft.resize(oldwidth, oldheight);
		
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
		EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, 0);
		
		BufferedImage image = ScreenShotHelper.getScreenshot(texture, width, height);
		File screenshotsDir = new File(base, "screenshots");
		screenshotsDir.mkdir();
		File file;
		String s1 = (new StringBuilder()).append("").append(dateFormat.format(new Date())).toString();
		for (int k = 1; (file = new File(screenshotsDir, (new StringBuilder()).append(s1).append(k != 1 ? (new StringBuilder()).append("_").append(k).toString() : "").append(".png").toString())).exists(); k++) ;
		try {
			ImageIO.write(image, "png", file);
			return (new StringBuilder()).append("Saved screenshot as ").append(file.getName()).toString();
		} catch(Exception e) {
			e.printStackTrace();
			return (new StringBuilder()).append("Failed to save: ").append(e).toString();
		}
		//return saveScreenshot(par0File, (String)null, par1, par2);
	}

	
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
}
