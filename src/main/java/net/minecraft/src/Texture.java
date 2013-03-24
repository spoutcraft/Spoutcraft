package net.minecraft.src;

import com.prupe.mcpatcher.mod.CTMUtils;
import com.prupe.mcpatcher.mod.MipmapHelper;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

public class Texture {
	private int field_94293_a;
	private int field_94291_b;
	private int field_94292_c;
	private final int field_94289_d;
	private final int field_94290_e;
	private final int field_94287_f;
	public int field_94288_g;
	private final int field_94300_h;
	public int field_94301_i;
	private final int field_94298_j;
	private final int field_94299_k;
	public boolean field_94296_l;
	private final String field_94297_m;
	private Rect2i field_94294_n;
	private boolean field_94295_o;
	private boolean field_94304_p;
	private boolean field_94303_q;
	public ByteBuffer field_94302_r;
	public int border;

	private Texture(String par1Str, int par2, int par3, int par4, int par5, int par6, int par7, int par8, int par9) {
		this.field_94297_m = CTMUtils.getOverrideTextureName(par1Str);
		this.field_94292_c = par2;
		this.field_94289_d = par3;
		this.field_94290_e = par4;
		this.field_94287_f = par5;
		this.field_94288_g = par7;
		this.field_94301_i = par8;
		this.field_94298_j = par9;
		this.field_94299_k = par6;
		this.field_94294_n = new Rect2i(0, 0, par3, par4);

		if (par4 == 1 && par5 == 1) {
			this.field_94300_h = 3552;
		} else if (par5 == 1) {
			this.field_94300_h = 3553;
		} else {
			this.field_94300_h = 32879;
		}

		this.field_94296_l = par8 != 9728 && par8 != 9729 || par9 != 9728 && par9 != 9729;

		if (par2 != 2) {
			this.field_94293_a = GL11.glGenTextures();
			GL11.glBindTexture(this.field_94300_h, this.field_94293_a);
			GL11.glTexParameteri(this.field_94300_h, GL11.GL_TEXTURE_MIN_FILTER, par8);
			GL11.glTexParameteri(this.field_94300_h, GL11.GL_TEXTURE_MAG_FILTER, par9);
			GL11.glTexParameteri(this.field_94300_h, GL11.GL_TEXTURE_WRAP_S, par6);
			GL11.glTexParameteri(this.field_94300_h, GL11.GL_TEXTURE_WRAP_T, par6);
		} else {
			this.field_94293_a = -1;
		}

		this.field_94291_b = TextureManager.func_94267_b().func_94265_c();
	}

	public Texture(String par1Str, int par2, int par3, int par4, int par5, int par6, int par7, int par8, BufferedImage par9BufferedImage) {
		this(par1Str, par2, par3, par4, 1, par5, par6, par7, par8, par9BufferedImage);
	}

	public Texture(String par1Str, int par2, int par3, int par4, int par5, int par6, int par7, int par8, int par9, BufferedImage par10BufferedImage) {
		this(par1Str, par2, par3, par4, par5, par6, par7, par8, par9);

		if (par10BufferedImage == null) {
			if (par3 != -1 && par4 != -1) {
				byte[] var11 = new byte[par3 * par4 * par5 * 4];

				for (int var12 = 0; var12 < var11.length; ++var12) {
					var11[var12] = 0;
				}

				this.field_94302_r = MipmapHelper.allocateByteBuffer(var11.length);
				this.field_94302_r.clear();
				this.field_94302_r.put(var11);
				this.field_94302_r.position(0).limit(var11.length);

				if (this.field_94304_p) {
					this.func_94285_g();
				} else {
					this.field_94303_q = false;
				}
			} else {
				this.field_94295_o = false;
			}
		} else {
			this.field_94295_o = true;
			this.func_94278_a(par10BufferedImage);

			if (par2 != 2) {
				this.func_94285_g();
				this.field_94304_p = false;
			}
		}
	}

	public final Rect2i func_94274_a() {
		return this.field_94294_n;
	}

	public void func_94272_a(Rect2i par1Rect2i, int par2) {
		if (this.field_94300_h != 32879) {
			Rect2i var3 = new Rect2i(0, 0, this.field_94289_d, this.field_94290_e);
			var3.func_94156_a(par1Rect2i);
			this.field_94302_r.position(0);

			for (int var4 = var3.func_94160_b(); var4 < var3.func_94160_b() + var3.func_94157_d(); ++var4) {
				int var5 = var4 * this.field_94289_d * 4;

				for (int var6 = var3.func_94158_a(); var6 < var3.func_94158_a() + var3.func_94159_c(); ++var6) {
					this.field_94302_r.put(var5 + var6 * 4 + 0, (byte)(par2 >> 24 & 255));
					this.field_94302_r.put(var5 + var6 * 4 + 1, (byte)(par2 >> 16 & 255));
					this.field_94302_r.put(var5 + var6 * 4 + 2, (byte)(par2 >> 8 & 255));
					this.field_94302_r.put(var5 + var6 * 4 + 3, (byte)(par2 >> 0 & 255));
				}
			}

			if (this.field_94304_p) {
				this.func_94285_g();
			} else {
				this.field_94303_q = false;
			}
		}
	}

	public void func_94279_c(String par1Str) {
		BufferedImage var2 = new BufferedImage(this.field_94289_d, this.field_94290_e, 2);
		ByteBuffer var3 = this.func_94273_h();
		byte[] var4 = new byte[this.field_94289_d * this.field_94290_e * 4];
		var3.position(0);
		var3.get(var4);

		for (int var5 = 0; var5 < this.field_94289_d; ++var5) {
			for (int var6 = 0; var6 < this.field_94290_e; ++var6) {
				int var7 = var6 * this.field_94289_d * 4 + var5 * 4;
				byte var8 = 0;
				int var10 = var8 | (var4[var7 + 2] & 255) << 0;
				var10 |= (var4[var7 + 1] & 255) << 8;
				var10 |= (var4[var7 + 0] & 255) << 16;
				var10 |= (var4[var7 + 3] & 255) << 24;
				var2.setRGB(var5, var6, var10);
			}
		}

		this.field_94302_r.position(this.field_94289_d * this.field_94290_e * 4);

		try {
			ImageIO.write(var2, "png", new File(Minecraft.getMinecraftDir(), par1Str));
		} catch (IOException var9) {
			var9.printStackTrace();
		}
	}

	public void func_94281_a(int par1, int par2, Texture par3Texture, boolean par4) {
		if (this.field_94303_q) {
			MipmapHelper.copySubTexture(this, par3Texture, par1, par2, par4);
		} else if (this.field_94300_h != 32879) {
			ByteBuffer var5 = par3Texture.func_94273_h();
			this.field_94302_r.position(0);
			var5.position(0);

			for (int var6 = 0; var6 < par3Texture.func_94276_e(); ++var6) {
				int var7 = par2 + var6;
				int var8 = var6 * par3Texture.func_94275_d() * 4;
				int var9 = var7 * this.field_94289_d * 4;

				if (par4) {
					var7 = par2 + (par3Texture.func_94276_e() - var6);
				}

				for (int var10 = 0; var10 < par3Texture.func_94275_d(); ++var10) {
					int var11 = var9 + (var10 + par1) * 4;
					int var12 = var8 + var10 * 4;

					if (par4) {
						var11 = par1 + var10 * this.field_94289_d * 4 + var7 * 4;
					}

					this.field_94302_r.put(var11 + 0, var5.get(var12 + 0));
					this.field_94302_r.put(var11 + 1, var5.get(var12 + 1));
					this.field_94302_r.put(var11 + 2, var5.get(var12 + 2));
					this.field_94302_r.put(var11 + 3, var5.get(var12 + 3));
				}
			}

			this.field_94302_r.position(this.field_94289_d * this.field_94290_e * 4);

			if (this.field_94304_p) {
				this.func_94285_g();
			} else {
				this.field_94303_q = false;
			}
		}
	}

	public void func_94278_a(BufferedImage par1BufferedImage) {
		if (this.field_94300_h != 32879) {
			int var2 = par1BufferedImage.getWidth();
			int var3 = par1BufferedImage.getHeight();

			if (var2 <= this.field_94289_d && var3 <= this.field_94290_e) {
				int[] var4 = new int[] {3, 0, 1, 2};
				int[] var5 = new int[] {3, 2, 1, 0};
				int[] var6 = this.field_94288_g == 32993 ? var5 : var4;
				int[] var7 = new int[this.field_94289_d * this.field_94290_e];
				int var8 = par1BufferedImage.getTransparency();
				par1BufferedImage.getRGB(0, 0, this.field_94289_d, this.field_94290_e, var7, 0, var2);
				byte[] var9 = new byte[this.field_94289_d * this.field_94290_e * 4];

				for (int var10 = 0; var10 < this.field_94290_e; ++var10) {
					for (int var11 = 0; var11 < this.field_94289_d; ++var11) {
						int var12 = var10 * this.field_94289_d + var11;
						int var13 = var12 * 4;
						var9[var13 + var6[0]] = (byte)(var7[var12] >> 24 & 255);
						var9[var13 + var6[1]] = (byte)(var7[var12] >> 16 & 255);
						var9[var13 + var6[2]] = (byte)(var7[var12] >> 8 & 255);
						var9[var13 + var6[3]] = (byte)(var7[var12] >> 0 & 255);
					}
				}

				this.field_94302_r = MipmapHelper.allocateByteBuffer(var9.length);
				this.field_94302_r.clear();
				this.field_94302_r.put(var9);
				this.field_94302_r.limit(var9.length);

				if (this.field_94304_p) {
					this.func_94285_g();
				} else {
					this.field_94303_q = false;
				}
			} else {
				Minecraft.getMinecraft().func_98033_al().func_98236_b("transferFromImage called with a BufferedImage with dimensions (" + var2 + ", " + var3 + ") larger than the Texture dimensions (" + this.field_94289_d + ", " + this.field_94290_e + "). Ignoring.");
			}
		}
	}

	public int func_94284_b() {
		return this.field_94291_b;
	}

	public int func_94282_c() {
		return this.field_94293_a;
	}

	public int func_94275_d() {
		return this.field_94289_d;
	}

	public int func_94276_e() {
		return this.field_94290_e;
	}

	public String func_94280_f() {
		return this.field_94297_m;
	}

	public void func_94277_a(int par1) {
		if (this.field_94287_f == 1) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		} else {
			GL11.glEnable(GL12.GL_TEXTURE_3D);
		}

		GL13.glActiveTexture(GL13.GL_TEXTURE0 + par1);
		GL11.glBindTexture(this.field_94300_h, this.field_94293_a);

		if (!this.field_94303_q) {
			this.func_94285_g();
		}
	}

	public void func_94285_g() {
		this.field_94302_r.flip();

		if (this.field_94290_e != 1 && this.field_94287_f != 1) {
			GL12.glTexImage3D(this.field_94300_h, 0, this.field_94288_g, this.field_94289_d, this.field_94290_e, this.field_94287_f, 0, this.field_94288_g, GL11.GL_UNSIGNED_BYTE, this.field_94302_r);
		} else if (this.field_94290_e != 1) {
			MipmapHelper.setupTexture(this.field_94300_h, 0, this.field_94288_g, this.field_94289_d, this.field_94290_e, 0, this.field_94288_g, 5121, this.field_94302_r, this);
		} else {
			GL11.glTexImage1D(this.field_94300_h, 0, this.field_94288_g, this.field_94289_d, 0, this.field_94288_g, GL11.GL_UNSIGNED_BYTE, this.field_94302_r);
		}

		this.field_94303_q = true;
	}

	public ByteBuffer func_94273_h() {
		return this.field_94302_r;
	}

	public void unloadGLTexture() {
		if (this.field_94293_a >= 0) {
			GL11.glDeleteTextures(this.field_94293_a);
			this.field_94293_a = -1;
		}
	}
}