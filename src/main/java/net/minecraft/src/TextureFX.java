package net.minecraft.src;

import com.pclewis.mcpatcher.mod.TextureUtils;
import com.pclewis.mcpatcher.mod.TileSize;
import org.lwjgl.opengl.GL11;

public class TextureFX {
	public byte[] imageData;
	public int iconIndex;
	public boolean anaglyphEnabled;

	/** Texture ID */
	public int textureId;
	public int tileSize;
	public int tileImage;

	public TextureFX(int par1) {
		this.imageData = new byte[TileSize.int_numBytes];
		this.anaglyphEnabled = false;
		this.textureId = 0;
		this.tileSize = 1;
		this.tileImage = 0;
		this.iconIndex = par1;
	}

	public void onTick() {}

	public void bindImage(RenderEngine par1RenderEngine) {
		if (TextureUtils.bindImageBegin()) {
			if (this.tileImage == 0) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1RenderEngine.getTexture("/terrain.png"));
			} else if (this.tileImage == 1) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1RenderEngine.getTexture("/gui/items.png"));
			}

			TextureUtils.bindImageEnd();
		}
	}
}
