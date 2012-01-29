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

	public TexturePackCustom(File var1) {
		this.texturePackFileName = var1.getName();
		this.texturePackFile = var1;
	}

	private String truncateString(String var1) {
		if (var1 != null && var1.length() > 34) {
			var1 = var1.substring(0, 34);
		}

		return var1;
	}

	public void func_6485_a(Minecraft var1) throws IOException {
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
			}
			catch (Exception var20) {
				;
			}

			try {
				var3 = var2.getInputStream(var2.getEntry("pack.png"));
				this.texturePackThumbnail = ImageIO.read(var3);
				var3.close();
			}
			catch (Exception var19) {
				;
			}

			var2.close();
		}
		catch (Exception var21) {
			var21.printStackTrace();
		}
		finally {
			try {
				var3.close();
			}
			catch (Exception var18) {
				;
			}

			try {
				var2.close();
			}
			catch (Exception var17) {
				;
			}

		}

	}

	public void func_6484_b(Minecraft var1) {
		if (this.texturePackThumbnail != null) {
			var1.renderEngine.deleteTexture(this.texturePackName);
		}

		this.closeTexturePackFile();
	}

	public void bindThumbnailTexture(Minecraft var1) {
		if (this.texturePackThumbnail != null && this.texturePackName < 0) {
			this.texturePackName = var1.renderEngine.allocateAndSetupTexture(this.texturePackThumbnail);
		}

		if (this.texturePackThumbnail != null) {
			var1.renderEngine.bindTexture(this.texturePackName);
		}
		else {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.renderEngine.getTexture("/gui/unknown_pack.png"));
		}

	}

	public void func_6482_a() {
		try {
			this.texturePackZipFile = new ZipFile(this.texturePackFile);
		}
		catch (Exception var2) {
			;
		}
		
		//TextureUtils.openTexturePackFile(this);//Spout
	}

	public void closeTexturePackFile() {
		try {
			//TextureUtils.closeTexturePackFile(this);//Spout
			this.texturePackZipFile.close();
		}
		catch (Exception var2) {
			;
		}

		this.texturePackZipFile = null;
	}

	public InputStream getResourceAsStream(String var1) {
		try {
			ZipEntry var2 = this.texturePackZipFile.getEntry(var1.substring(1));
			if (var2 != null) {
				return this.texturePackZipFile.getInputStream(var2);
			}
		}
		catch (Exception var3) {
			;
		}

		return TexturePackBase.class.getResourceAsStream(var1);
	}
}
