package net.minecraft.src;

//Spout HD Start
import com.pclewis.mcpatcher.mod.TileSize;
//Spout HD End
import net.minecraft.src.RenderEngine;
import org.lwjgl.opengl.GL11;

public class TextureFX {
	//Spout HD Start
	public byte[] imageData = new byte[TileSize.int_numBytes];
	//Spout HD End
	public int iconIndex;
	public boolean anaglyphEnabled = false;
	public int textureId = 0;
	public int tileSize = 1;
	public int tileImage = 0;

	public TextureFX(int par1) {
		this.iconIndex = par1;
	}

	public void onTick() {}

	public void bindImage(RenderEngine par1RenderEngine) {
		if (this.tileImage == 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1RenderEngine.getTexture("/terrain.png"));
		} else if (this.tileImage == 1) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1RenderEngine.getTexture("/gui/items.png"));
		}
	}
}
