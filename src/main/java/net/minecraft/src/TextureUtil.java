package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TextureUtil {
	private static final IntBuffer field_111000_c = GLAllocation.createDirectIntBuffer(4194304);
	public static final DynamicTexture field_111001_a = new DynamicTexture(16, 16);
	public static final int[] field_110999_b = field_111001_a.func_110565_c();

	public static int func_110996_a() {
		return GL11.glGenTextures();
	}

	public static int func_110987_a(int par0, BufferedImage par1BufferedImage) {
		return func_110989_a(par0, par1BufferedImage, false, false);
	}

	public static void func_110988_a(int par0, int[] par1ArrayOfInteger, int par2, int par3) {
		bindTexture(par0);
		func_110998_a(par1ArrayOfInteger, par2, par3, 0, 0, false, false);
	}

	public static void func_110998_a(int[] par0ArrayOfInteger, int par1, int par2, int par3, int par4, boolean par5, boolean par6) {
		int var7 = 4194304 / par1;
		func_110992_b(par5);
		func_110997_a(par6);
		int var10;

		for (int var8 = 0; var8 < par1 * par2; var8 += par1 * var10) {
			int var9 = var8 / par1;
			var10 = Math.min(var7, par2 - var9);
			int var11 = par1 * var10;
			func_110994_a(par0ArrayOfInteger, var8, var11);
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, par3, par4 + var9, par1, var10, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, field_111000_c);
		}
	}

	public static int func_110989_a(int par0, BufferedImage par1BufferedImage, boolean par2, boolean par3) {
		func_110991_a(par0, par1BufferedImage.getWidth(), par1BufferedImage.getHeight());
		return func_110995_a(par0, par1BufferedImage, 0, 0, par2, par3);
	}

	public static void func_110991_a(int par0, int par1, int par2) {
		bindTexture(par0);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, par1, par2, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, (IntBuffer)null);
	}

	public static int func_110995_a(int par0, BufferedImage par1BufferedImage, int par2, int par3, boolean par4, boolean par5) {
		bindTexture(par0);
		func_110993_a(par1BufferedImage, par2, par3, par4, par5);
		return par0;
	}

	private static void func_110993_a(BufferedImage par0BufferedImage, int par1, int par2, boolean par3, boolean par4) {
		int var5 = par0BufferedImage.getWidth();
		int var6 = par0BufferedImage.getHeight();
		int var7 = 4194304 / var5;
		int[] var8 = new int[var7 * var5];
		func_110992_b(par3);
		func_110997_a(par4);

		for (int var9 = 0; var9 < var5 * var6; var9 += var5 * var7) {
			int var10 = var9 / var5;
			int var11 = Math.min(var7, var6 - var10);
			int var12 = var5 * var11;
			par0BufferedImage.getRGB(0, var10, var5, var11, var8, 0, var5);
			func_110990_a(var8, var12);
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, par1, par2 + var10, var5, var11, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, field_111000_c);
		}
	}

	private static void func_110997_a(boolean par0) {
		if (par0) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}
	}

	private static void func_110992_b(boolean par0) {
		if (par0) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}
	}

	private static void func_110990_a(int[] par0ArrayOfInteger, int par1) {
		func_110994_a(par0ArrayOfInteger, 0, par1);
	}

	private static void func_110994_a(int[] par0ArrayOfInteger, int par1, int par2) {
		int[] var3 = par0ArrayOfInteger;

		if (Minecraft.getMinecraft().gameSettings.anaglyph) {
			var3 = func_110985_a(par0ArrayOfInteger);
		}

		field_111000_c.clear();
		field_111000_c.put(var3, par1, par2);
		field_111000_c.position(0).limit(par2);
	}
	// Spout add Public
	public static void bindTexture(int par0) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, par0);
	}

	public static int[] func_110986_a(ResourceManager par0ResourceManager, ResourceLocation par1ResourceLocation) throws IOException {
		BufferedImage var2 = ImageIO.read(par0ResourceManager.func_110536_a(par1ResourceLocation).func_110527_b());
		int var3 = var2.getWidth();
		int var4 = var2.getHeight();
		int[] var5 = new int[var3 * var4];
		var2.getRGB(0, 0, var3, var4, var5, 0, var3);
		return var5;
	}

	public static int[] func_110985_a(int[] par0ArrayOfInteger) {
		int[] var1 = new int[par0ArrayOfInteger.length];

		for (int var2 = 0; var2 < par0ArrayOfInteger.length; ++var2) {
			int var3 = par0ArrayOfInteger[var2] >> 24 & 255;
			int var4 = par0ArrayOfInteger[var2] >> 16 & 255;
			int var5 = par0ArrayOfInteger[var2] >> 8 & 255;
			int var6 = par0ArrayOfInteger[var2] & 255;
			int var7 = (var4 * 30 + var5 * 59 + var6 * 11) / 100;
			int var8 = (var4 * 30 + var5 * 70) / 100;
			int var9 = (var4 * 30 + var6 * 70) / 100;
			var1[var2] = var3 << 24 | var7 << 16 | var8 << 8 | var9;
		}

		return var1;
	}

	static {
		int var0 = -16777216;
		int var1 = -524040;
		int[] var2 = new int[] { -524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040};
		int[] var3 = new int[] { -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216};
		int var4 = var2.length;

		for (int var5 = 0; var5 < 16; ++var5) {
			System.arraycopy(var5 < var4 ? var2 : var3, 0, field_110999_b, 16 * var5, var4);
			System.arraycopy(var5 < var4 ? var3 : var2, 0, field_110999_b, 16 * var5 + var4, var4);
		}

		field_111001_a.func_110564_a();
	}
}
