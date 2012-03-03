package net.minecraft.src;
//Spout HD Start
import com.pclewis.mcpatcher.mod.TextureUtils;
//Spout HD End
import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class TexturePackDefault extends TexturePackBase {
	private int texturePackName = -1;
	private BufferedImage texturePackThumbnail;

	public TexturePackDefault() {
		this.texturePackFileName = "Default";
		this.firstDescriptionLine = "The default look of Minecraft";

		try {
			//Spout HD Start
			this.texturePackThumbnail = TextureUtils.getResourceAsBufferedImage("/pack.png");
			//Spout HD End
		} catch (IOException var2) {
			var2.printStackTrace();
		}
	}

	public void unbindThumbnailTexture(Minecraft par1Minecraft) {
		if (this.texturePackThumbnail != null) {
			par1Minecraft.renderEngine.deleteTexture(this.texturePackName);
		}
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
}
