package net.minecraft.src;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class TexturePackFolder extends TexturePackImplementation {
	public TexturePackFolder(String par1, File par2, ITexturePack par3ITexturePack) {
		super(par1, par2, par2.getName(), par3ITexturePack);
	}

	protected InputStream func_98139_b(String par1Str) throws IOException {
		File var2 = new File(this.texturePackFile, par1Str.substring(1));

		if (!var2.exists()) {
			throw new FileNotFoundException(par1Str);
		} else {
			return new BufferedInputStream(new FileInputStream(var2));
		}
	}

	public boolean func_98140_c(String par1Str) {
		File var2 = new File(this.texturePackFile, par1Str);
		return var2.exists() && var2.isFile();
	}

	public boolean isCompatible() {
		File var1 = new File(this.texturePackFile, "textures/");
		boolean var2 = var1.exists() && var1.isDirectory();
		boolean var3 = this.func_98140_c("terrain.png") || this.func_98140_c("gui/items.png");
		return var2 || !var3;
	}

	// MCPatcher Start
	@Override
	public boolean func_94179_g() {
		return true;
	}
	// MCPatcher End
}
