package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
// MCPatcher start
import com.prupe.mcpatcher.mod.MipmapHelper;
import com.prupe.mcpatcher.mod.TileLoader;
// MCPatcher end

public class Texture {
	private int glTextureId;
	private int textureId;
	private int textureType;

	/** Width of this texture in pixels. */
	private final int width;

	/** Height of this texture in pixels. */
	private final int height;
	private final int textureDepth;
	public int textureFormat;
	private final int textureTarget;
	public int textureMinFilter;
	private final int textureMagFilter;
	private final int textureWrap;
	public boolean mipmapActive;
	private final String textureName;
	private Rect2i textureRect;
	private boolean transferred;
	private boolean autoCreate;
	private boolean textureCreated;
	public ByteBuffer textureData;
	public int border;

	private Texture(String par1Str, int par2, int par3, int par4, int par5, int par6, int par7, int par8, int par9) {
		this.textureName = TileLoader.getOverrideTextureName(par1Str);
		this.textureType = par2;
		this.width = par3;
		this.height = par4;
		this.textureDepth = par5;
		this.textureFormat = par7;
		this.textureMinFilter = par8;
		this.textureMagFilter = par9;
		this.textureWrap = par6;
		this.textureRect = new Rect2i(0, 0, par3, par4);

		if (par4 == 1 && par5 == 1) {
			this.textureTarget = 3552;
		} else if (par5 == 1) {
			this.textureTarget = 3553;
		} else {
			this.textureTarget = 32879;
		}

		this.mipmapActive = par8 != 9728 && par8 != 9729 || par9 != 9728 && par9 != 9729;

		if (par2 != 2) {
			this.glTextureId = GL11.glGenTextures();
			GL11.glBindTexture(this.textureTarget, this.glTextureId);
			GL11.glTexParameteri(this.textureTarget, GL11.GL_TEXTURE_MIN_FILTER, par8);
			GL11.glTexParameteri(this.textureTarget, GL11.GL_TEXTURE_MAG_FILTER, par9);
			GL11.glTexParameteri(this.textureTarget, GL11.GL_TEXTURE_WRAP_S, par6);
			GL11.glTexParameteri(this.textureTarget, GL11.GL_TEXTURE_WRAP_T, par6);
		} else {
			this.glTextureId = -1;
		}

		this.textureId = TextureManager.instance().getNextTextureId();
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

				this.textureData = MipmapHelper.allocateByteBuffer(var11.length);
				this.textureData.clear();
				this.textureData.put(var11);
				this.textureData.position(0).limit(var11.length);

				if (this.autoCreate) {
					this.createTexture();
				} else {
					this.textureCreated = false;
				}
			} else {
				this.transferred = false;
			}
		} else {
			this.transferred = true;
			this.transferFromImage(par10BufferedImage);

			if (par2 != 2) {
				this.createTexture();
				this.autoCreate = false;
			}
		}
	}

	public final Rect2i getTextureRect() {
		return this.textureRect;
	}

	public void fillRect(Rect2i par1Rect2i, int par2) {
		if (this.textureTarget != 32879) {
			Rect2i var3 = new Rect2i(0, 0, this.width, this.height);
			var3.intersection(par1Rect2i);
			this.textureData.position(0);

			for (int var4 = var3.getRectY(); var4 < var3.getRectY() + var3.getRectHeight(); ++var4) {
				int var5 = var4 * this.width * 4;

				for (int var6 = var3.getRectX(); var6 < var3.getRectX() + var3.getRectWidth(); ++var6) {
					this.textureData.put(var5 + var6 * 4 + 0, (byte)(par2 >> 24 & 255));
					this.textureData.put(var5 + var6 * 4 + 1, (byte)(par2 >> 16 & 255));
					this.textureData.put(var5 + var6 * 4 + 2, (byte)(par2 >> 8 & 255));
					this.textureData.put(var5 + var6 * 4 + 3, (byte)(par2 >> 0 & 255));
				}
			}

			if (this.autoCreate) {
				this.createTexture();
			} else {
				this.textureCreated = false;
			}
		}
	}

	public void writeImage(String par1Str) {
		BufferedImage var2 = new BufferedImage(this.width, this.height, 2);
		ByteBuffer var3 = this.getTextureData();
		byte[] var4 = new byte[this.width * this.height * 4];
		var3.position(0);
		var3.get(var4);

		for (int var5 = 0; var5 < this.width; ++var5) {
			for (int var6 = 0; var6 < this.height; ++var6) {
				int var7 = var6 * this.width * 4 + var5 * 4;
				byte var8 = 0;
				int var10 = var8 | (var4[var7 + 2] & 255) << 0;
				var10 |= (var4[var7 + 1] & 255) << 8;
				var10 |= (var4[var7 + 0] & 255) << 16;
				var10 |= (var4[var7 + 3] & 255) << 24;
				var2.setRGB(var5, var6, var10);
			}
		}

		this.textureData.position(this.width * this.height * 4);

		try {
			ImageIO.write(var2, "png", new File(Minecraft.getMinecraftDir(), par1Str));
		} catch (IOException var9) {
			var9.printStackTrace();
		}
	}

	public void copyFrom(int par1, int par2, Texture par3Texture, boolean par4) {
		if (this.textureCreated) {
			MipmapHelper.copySubTexture(this, par3Texture, par1, par2, par4);
		} else if (this.textureTarget != 32879) {
			ByteBuffer var5 = par3Texture.getTextureData();
			this.textureData.position(0);
			var5.position(0);

			for (int var6 = 0; var6 < par3Texture.getHeight(); ++var6) {
				int var7 = par2 + var6;
				int var8 = var6 * par3Texture.getWidth() * 4;
				int var9 = var7 * this.width * 4;

				if (par4) {
					var7 = par2 + (par3Texture.getHeight() - var6);
				}

				for (int var10 = 0; var10 < par3Texture.getWidth(); ++var10) {
					int var11 = var9 + (var10 + par1) * 4;
					int var12 = var8 + var10 * 4;

					if (par4) {
						var11 = par1 + var10 * this.width * 4 + var7 * 4;
					}

					this.textureData.put(var11 + 0, var5.get(var12 + 0));
					this.textureData.put(var11 + 1, var5.get(var12 + 1));
					this.textureData.put(var11 + 2, var5.get(var12 + 2));
					this.textureData.put(var11 + 3, var5.get(var12 + 3));
				}
			}

			this.textureData.position(this.width * this.height * 4);

			if (this.autoCreate) {
				this.createTexture();
			} else {
				this.textureCreated = false;
			}
		}
	}

	public void transferFromImage(BufferedImage par1BufferedImage) {
		if (this.textureTarget != 32879) {
			int var2 = par1BufferedImage.getWidth();
			int var3 = par1BufferedImage.getHeight();

			if (var2 <= this.width && var3 <= this.height) {
				int[] var4 = new int[] {3, 0, 1, 2};
				int[] var5 = new int[] {3, 2, 1, 0};
				int[] var6 = this.textureFormat == 32993 ? var5 : var4;
				int[] var7 = new int[this.width * this.height];
				int var8 = par1BufferedImage.getTransparency();
				par1BufferedImage.getRGB(0, 0, this.width, this.height, var7, 0, var2);
				byte[] var9 = new byte[this.width * this.height * 4];

				for (int var10 = 0; var10 < this.height; ++var10) {
					for (int var11 = 0; var11 < this.width; ++var11) {
						int var12 = var10 * this.width + var11;
						int var13 = var12 * 4;
						var9[var13 + var6[0]] = (byte)(var7[var12] >> 24 & 255);
						var9[var13 + var6[1]] = (byte)(var7[var12] >> 16 & 255);
						var9[var13 + var6[2]] = (byte)(var7[var12] >> 8 & 255);
						var9[var13 + var6[3]] = (byte)(var7[var12] >> 0 & 255);
					}
				}

				this.textureData = MipmapHelper.allocateByteBuffer(var9.length);
				this.textureData.clear();
				this.textureData.put(var9);
				this.textureData.limit(var9.length);

				if (this.autoCreate) {
					this.createTexture();
				} else {
					this.textureCreated = false;
				}
			} else {
				Minecraft.getMinecraft().getLogAgent().logWarning("transferFromImage called with a BufferedImage with dimensions (" + var2 + ", " + var3 + ") larger than the Texture dimensions (" + this.width + ", " + this.height + "). Ignoring.");
			}
		}
	}

	public int getTextureId() {
		return this.textureId;
	}

	public int getGlTextureId() {
		return this.glTextureId;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public String getTextureName() {
		return this.textureName;
	}

	public void bindTexture(int par1) {
		if (this.textureDepth == 1) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		} else {
			GL11.glEnable(GL12.GL_TEXTURE_3D);
		}

		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit + par1);
		GL11.glBindTexture(this.textureTarget, this.glTextureId);

		if (!this.textureCreated) {
			this.createTexture();
		}
	}

	public void createTexture() {
		this.textureData.flip();

		if (this.height != 1 && this.textureDepth != 1) {
			GL12.glTexImage3D(this.textureTarget, 0, this.textureFormat, this.width, this.height, this.textureDepth, 0, this.textureFormat, GL11.GL_UNSIGNED_BYTE, this.textureData);
		} else if (this.height != 1) {
			MipmapHelper.setupTexture(this.textureTarget, 0, this.textureFormat, this.width, this.height, 0, this.textureFormat, 5121, this.textureData, this);
		} else {
			GL11.glTexImage1D(this.textureTarget, 0, this.textureFormat, this.width, 0, this.textureFormat, GL11.GL_UNSIGNED_BYTE, this.textureData);
		}

		this.textureCreated = true;
	}

	public ByteBuffer getTextureData() {
		return this.textureData;
	}

	public void unloadGLTexture() {
		if (this.glTextureId >= 0) {
			GL11.glDeleteTextures(this.glTextureId);
			this.glTextureId = -1;
		}
	}
}
