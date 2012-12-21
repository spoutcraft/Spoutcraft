package net.minecraft.src;

import org.lwjgl.opengl.GL11;
// MCPatcher Start
import com.pclewis.mcpatcher.mod.TextureUtils;
import com.pclewis.mcpatcher.mod.TileSize;
// MCPatcher End

public class TextureFX {
	// MCPatcher Start
	public byte[] imageData;
	// MCPatcher End
	public int iconIndex;
	// MCPatcher Start
	public boolean anaglyphEnabled;
	// MCPatcher End

	/** Texture ID */
	// MCPatcher Start
	public int textureId;
	public int tileSize;
	public int tileImage;
	// MCPatcher End

	public TextureFX(int par1) {
		// MCPatcher Start
		this.imageData = new byte[TileSize.int_numBytes];
		this.anaglyphEnabled = false;
		this.textureId = 0;
		this.tileSize = 1;
		this.tileImage = 0;
		// MCPatcher End
		this.iconIndex = par1;
	}

	public void onTick() {}

	public void bindImage(RenderEngine par1RenderEngine) {
		// MCPatcher Start
		if (TextureUtils.bindImageBegin()) {
			if (this.tileImage == 0) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1RenderEngine.getTexture("/terrain.png"));
			} else if (this.tileImage == 1) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1RenderEngine.getTexture("/gui/items.png"));
			}

			TextureUtils.bindImageEnd();
		// MCPatcher End
		}
	}
}
