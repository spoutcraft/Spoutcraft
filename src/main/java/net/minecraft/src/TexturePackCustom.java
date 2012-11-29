package net.minecraft.src;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TexturePackCustom extends TexturePackImplementation {

	/** ZipFile object used to access the texture pack file's contents. */
	public ZipFile texturePackZipFile; // Spout HD private -> public
	// Spout HD Start
	public ZipFile origZip;
	public File tmpFile;
	public long lastModified;
	// Spout HD End

	public TexturePackCustom(String par1Str, File par2File) {
		super(par1Str, par2File, par2File.getName());
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

	/**
	 * Gives a texture resource as InputStream.
	 */
	public InputStream getResourceAsStream(String par1Str) {
		this.openTexturePackFile();

		try {
			ZipEntry var2 = this.texturePackZipFile.getEntry(par1Str.substring(1));

			if (var2 != null) {
				return this.texturePackZipFile.getInputStream(var2);
			}
		} catch (Exception var3) {
			;
		}

		return super.getResourceAsStream(par1Str);
	}

	/**
	 * Open the texture pack's file and initialize texturePackZipFile
	 */
	public void openTexturePackFile() {
		if (this.texturePackZipFile == null) {
			try {
				this.texturePackZipFile = new ZipFile(this.texturePackFile);
			} catch (IOException var2) {
				;
			}
		}
	}
}
