package net.minecraft.src;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TexturePackCustom extends TexturePackImplementation {
	public ZipFile field_77550_e; // Spout HD private -> public
	// Spout HD Start
	public ZipFile origZip;
	public File tmpFile;
	public long lastModified;
	// Spout HD End

	public TexturePackCustom(String par1Str, File par2File) {
		super(par1Str, par2File, par2File.getName());
	}

	public void func_77533_a(RenderEngine par1RenderEngine) {
		super.func_77533_a(par1RenderEngine);

		try {
			if (this.field_77550_e != null) {
				this.field_77550_e.close();
			}
		} catch (IOException var3) {
			;
		}

		this.field_77550_e = null;
	}

	/**
	 * Gives a texture resource as InputStream.
	 */
	public InputStream getResourceAsStream(String par1Str) {
		this.func_77549_g();

		try {
			ZipEntry var2 = this.field_77550_e.getEntry(par1Str.substring(1));

			if (var2 != null) {
				return this.field_77550_e.getInputStream(var2);
			}
		} catch (Exception var3) {
			;
		}

		return super.getResourceAsStream(par1Str);
	}

	private void func_77549_g() {
		if (this.field_77550_e == null) {
			try {
				this.field_77550_e = new ZipFile(this.field_77548_a);
			} catch (IOException var2) {
				;
			}
		}
	}
}
