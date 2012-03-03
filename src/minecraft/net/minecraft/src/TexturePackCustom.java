package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.src.TexturePackBase;
import org.lwjgl.opengl.GL11;

import com.pclewis.mcpatcher.mod.TextureUtils;

public class TexturePackCustom extends TexturePackBase {

	public ZipFile texturePackZipFile; //Spout private -> public
	private int texturePackName = -1;
	private BufferedImage texturePackThumbnail;
	//Spout start
	public File texturePackFile;
	public ZipFile origZip;
	public File tmpFile;
	public long lastModified;
	//Spout end

	public TexturePackCustom(File par1File) {
		this.texturePackFileName = par1File.getName();
		this.texturePackFile = par1File;
	}

	private String truncateString(String par1Str) {
		if (par1Str != null && par1Str.length() > 34) {
			par1Str = par1Str.substring(0, 34);
		}

		return par1Str;
	}

	public void func_6485_a(Minecraft par1Minecraft) throws IOException {
		ZipFile var2 = null;
		InputStream var3 = null;

		try {
			var2 = new ZipFile(this.texturePackFile);

			try {
				var3 = var2.getInputStream(var2.getEntry("pack.txt"));
				BufferedReader var4 = new BufferedReader(new InputStreamReader(var3));
				this.firstDescriptionLine = this.truncateString(var4.readLine());
				this.secondDescriptionLine = this.truncateString(var4.readLine());
				var4.close();
				var3.close();
			} catch (Exception var20) {
				;
			}

			try {
				var3 = var2.getInputStream(var2.getEntry("pack.png"));
				this.texturePackThumbnail = ImageIO.read(var3);
				var3.close();
			} catch (Exception var19) {
				;
			}

			var2.close();
		} catch (Exception var21) {
			var21.printStackTrace();
		} finally {
			try {
				var3.close();
			} catch (Exception var18) {
				;
			}

			try {
				var2.close();
			} catch (Exception var17) {
				;
			}

		}

	}

	public void unbindThumbnailTexture(Minecraft par1Minecraft) {
		if (this.texturePackThumbnail != null) {
			par1Minecraft.renderEngine.deleteTexture(this.texturePackName);
		}

		this.closeTexturePackFile();
	}

	public void bindThumbnailTexture(Minecraft par1Minecraft) {
		if (this.texturePackThumbnail != null && this.texturePackName < 0) {
			this.texturePackName = par1Minecraft.renderEngine.allocateAndSetupTexture(this.texturePackThumbnail);
		}

		if (this.texturePackThumbnail != null) {
			par1Minecraft.renderEngine.bindTexture(this.texturePackName);
		} else {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1Minecraft.renderEngine.getTexture("/gui/unknown_pack.png"));
		}

	}

	public void func_6482_a() {
		try {
			this.texturePackZipFile = new ZipFile(this.texturePackFile);
		} catch (Exception var2) {
			;
		}

	}

	public void closeTexturePackFile() {
		try {
			this.texturePackZipFile.close();
		} catch (Exception var2) {
			;
		}

		this.texturePackZipFile = null;
	}

	public InputStream getResourceAsStream(String par1Str) {
		try {
			ZipEntry var2 = this.texturePackZipFile.getEntry(par1Str.substring(1));
			if (var2 != null) {
				return this.texturePackZipFile.getInputStream(var2);
			}
		} catch (Exception var3) {
			;
		}

		return TexturePackBase.class.getResourceAsStream(par1Str);
	}
}
