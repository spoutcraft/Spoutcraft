package net.minecraft.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class TexturePackCustom extends TexturePackImplementation {

	/** ZipFile object used to access the texture pack file's contents. */
	// MCPatcher Start - private to public
	public ZipFile texturePackZipFile;
	public ZipFile origZip;
	public File tmpFile;
	public long lastModified;
	// MCPatcher End

	public TexturePackCustom(String par1Str, File par2File, ITexturePack par3ITexturePack) {
		super(par1Str, par2File, par2File.getName(), par3ITexturePack);
	}

	/**
	 * Delete the OpenGL texture id of the pack's thumbnail image, and close the zip file in case of TexturePackCustom.
	 */
	public void deleteTexturePack(RenderEngine par1RenderEngine) {
		super.deleteTexturePack(par1RenderEngine);

		try {
			if (this.texturePackZipFile != null) {
				this.texturePackZipFile.close();
			}
		} catch (IOException var3) {
			;
		}

		this.texturePackZipFile = null;
	}

	protected InputStream func_98139_b(String par1Str) throws IOException {
		this.openTexturePackFile();
		ZipEntry var2 = this.texturePackZipFile.getEntry(par1Str.substring(1));

		if (var2 == null) {
			throw new FileNotFoundException(par1Str);
		} else {
			return this.texturePackZipFile.getInputStream(var2);
		}
	}

	public boolean func_98140_c(String par1Str) {
		try {
			this.openTexturePackFile();
			return this.texturePackZipFile.getEntry(par1Str.substring(1)) != null;
		} catch (Exception var3) {
			return false;
		}
	}

	/**
	 * Open the texture pack's file and initialize texturePackZipFile
	 */
	private void openTexturePackFile() throws IOException, ZipException {
		if (this.texturePackZipFile == null) {
			this.texturePackZipFile = new ZipFile(this.texturePackFile);
		}
	}

	public boolean isCompatible() {
		try {
			this.openTexturePackFile();
			Enumeration var1 = this.texturePackZipFile.entries();

			while (var1.hasMoreElements()) {
				ZipEntry var2 = (ZipEntry)var1.nextElement();

				if (var2.getName().startsWith("textures/")) {
					return true;
				}
			}
		} catch (Exception var3) {
			;
		}

		boolean var4 = this.func_98140_c("terrain.png") || this.func_98140_c("gui/items.png");
		return !var4;
	}
}
