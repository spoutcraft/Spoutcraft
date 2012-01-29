package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

class GuiTexturePackSlot extends GuiSlot {
	final GuiTexturePacks parentTexturePackGui;

	public GuiTexturePackSlot(GuiTexturePacks guitexturepacks) {
		super(GuiTexturePacks.func_22124_a(guitexturepacks), guitexturepacks.width, guitexturepacks.height, 32, (guitexturepacks.height - 55) + 4, 36);
		parentTexturePackGui = guitexturepacks;
	}

	protected int getSize() {
		List list = GuiTexturePacks.func_22126_b(parentTexturePackGui).texturePackList.availableTexturePacks();
		return list.size();
	}

	protected void elementClicked(int i, boolean flag) {
		List list = GuiTexturePacks.func_22119_c(parentTexturePackGui).texturePackList.availableTexturePacks();
		try {
			GuiTexturePacks.func_22122_d(parentTexturePackGui).texturePackList.setTexturePack((TexturePackBase)list.get(i));
			GuiTexturePacks.func_22117_e(parentTexturePackGui).renderEngine.refreshTextures();
		}
		catch (Exception exception) {
			GuiTexturePacks.func_35307_f(parentTexturePackGui).texturePackList.setTexturePack((TexturePackBase)list.get(0));
			GuiTexturePacks.func_35308_g(parentTexturePackGui).renderEngine.refreshTextures();
		}
	}

	protected boolean isSelected(int i) {
		List list = GuiTexturePacks.func_22118_f(parentTexturePackGui).texturePackList.availableTexturePacks();
		return GuiTexturePacks.func_22116_g(parentTexturePackGui).texturePackList.selectedTexturePack == list.get(i);
	}

	protected int getContentHeight() {
		return getSize() * 36;
	}

	protected void drawBackground() {
		parentTexturePackGui.drawDefaultBackground();
	}

	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
		TexturePackBase texturepackbase = (TexturePackBase)GuiTexturePacks.func_22121_h(parentTexturePackGui).texturePackList.availableTexturePacks().get(i);
		texturepackbase.bindThumbnailTexture(GuiTexturePacks.func_22123_i(parentTexturePackGui));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_I(0xffffff);
		tessellator.addVertexWithUV(j, k + l, 0.0D, 0.0D, 1.0D);
		tessellator.addVertexWithUV(j + 32, k + l, 0.0D, 1.0D, 1.0D);
		tessellator.addVertexWithUV(j + 32, k, 0.0D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(j, k, 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		parentTexturePackGui.drawString(GuiTexturePacks.func_22127_j(parentTexturePackGui), texturepackbase.texturePackFileName, j + 32 + 2, k + 1, 0xffffff);
		parentTexturePackGui.drawString(GuiTexturePacks.func_22120_k(parentTexturePackGui), texturepackbase.firstDescriptionLine, j + 32 + 2, k + 12, 0x808080);
		parentTexturePackGui.drawString(GuiTexturePacks.func_22125_l(parentTexturePackGui), texturepackbase.secondDescriptionLine, j + 32 + 2, k + 12 + 10, 0x808080);
	}
}
