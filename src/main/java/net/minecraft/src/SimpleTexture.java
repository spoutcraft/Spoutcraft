package net.minecraft.src;

import com.prupe.mcpatcher.hd.MipmapHelper;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class SimpleTexture extends AbstractTexture {
	private final ResourceLocation textureLocation;

	public SimpleTexture(ResourceLocation par1ResourceLocation) {
		this.textureLocation = par1ResourceLocation;
	}

	public void loadTexture(ResourceManager par1ResourceManager) throws IOException {
		InputStream var2 = null;

		try {
			Resource var3 = par1ResourceManager.getResource(this.textureLocation);
			var2 = var3.getInputStream();
			BufferedImage var4 = ImageIO.read(var2);
			boolean var5 = false;
			boolean var6 = false;

			if (var3.hasMetadata()) {
				try {
					TextureMetadataSection var7 = (TextureMetadataSection)var3.getMetadata("texture");

					if (var7 != null) {
						var5 = var7.getTextureBlur();
						var6 = var7.getTextureClamp();
					}
				} catch (RuntimeException var11) {
					Minecraft.getMinecraft().getLogAgent().logWarningException("Failed reading metadata of: " + this.textureLocation, var11);
				}
			}

			MipmapHelper.setupTexture(this.getGlTextureId(), var4, var5, var6, this.textureLocation);
		} finally {
			if (var2 != null) {
				var2.close();
			}
		}
	}
}
