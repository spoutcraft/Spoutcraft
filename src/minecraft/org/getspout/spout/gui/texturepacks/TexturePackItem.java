package org.getspout.spout.gui.texturepacks;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.TexturePackBase;
import net.minecraft.src.TexturePackList;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.MCRenderDelegate;
import org.lwjgl.opengl.GL11;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.gui.ListWidget;
import org.spoutcraft.spoutcraftapi.gui.ListWidgetItem;
import org.spoutcraft.spoutcraftapi.gui.MinecraftTessellator;
import org.spoutcraft.spoutcraftapi.gui.RenderUtil;

import com.pclewis.mcpatcher.mod.TextureUtils;

public class TexturePackItem implements ListWidgetItem {
	
	private TexturePackBase pack;
	private ListWidget widget;
	private int tileSize;
	private TexturePackList packList = SpoutClient.getHandle().texturePackList;
	
	public TexturePackItem(TexturePackBase pack) {
		this.setPack(pack);
		tileSize = TextureUtils.getTileSize(pack);
	}

	public void setListWidget(ListWidget widget) {
		this.widget = widget;
	}

	public ListWidget getListWidget() {
		return widget;
	}

	public int getHeight() {
		return 29;
	}

	public void render(int x, int y, int width, int height) {
		MinecraftTessellator tessellator = Spoutcraft.getTessellator();
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		
		font.drawStringWithShadow(getName(), x+29, y+2, 0xffffffff);
		font.drawStringWithShadow(pack.firstDescriptionLine, x+29, y+11, 0xffaaaaaa);
		font.drawStringWithShadow(pack.secondDescriptionLine, x+29, y+20, 0xffaaaaaa);
//		String sTileSize = tileSize+"x";
//		int w = font.getStringWidth(sTileSize);
//		font.drawStringWithShadow(sTileSize, width - 5 - w, y + 2, 0xffaaaaaa);
		
		pack.bindThumbnailTexture(SpoutClient.getHandle());
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque(255,255,255);
        tessellator.addVertexWithUV(x + 2, y + 27, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(x + 27, y + 27, 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(x + 27, y + 2, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x + 2, y + 2, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
	}

	public void onClick(int x, int y, boolean doubleClick) {
		if(doubleClick) {
			select();
		}
	}

	public void setPack(TexturePackBase pack) {
		this.pack = pack;
	}

	public TexturePackBase getPack() {
		return pack;
	}
	
	public String getName() {
		String name = pack.texturePackFileName;
		name = name.replaceAll("^\\.zip", "");
		return name;
	}

	public void select() {
		packList.setTexturePack(getPack());
		SpoutClient.getHandle().renderEngine.refreshTextures();
	}
}
