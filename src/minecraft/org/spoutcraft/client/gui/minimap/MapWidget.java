package org.spoutcraft.client.gui.minimap;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.Tessellator;

import org.getspout.commons.util.map.TIntPairObjectHashMap;
import org.lwjgl.opengl.GL11;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.chunkcache.HeightMap;
import org.spoutcraft.client.gui.MCRenderDelegate;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollable;
import org.spoutcraft.spoutcraftapi.gui.MinecraftTessellator;
import org.spoutcraft.spoutcraftapi.gui.Orientation;
import org.spoutcraft.spoutcraftapi.gui.RenderUtil;
import org.spoutcraft.spoutcraftapi.gui.ScrollBarPolicy;
import org.spoutcraft.spoutcraftapi.gui.WidgetType;
import org.spoutcraft.spoutcraftapi.property.Property;

import com.pclewis.mcpatcher.mod.TextureUtils;

public class MapWidget extends GenericScrollable {
	static TIntPairObjectHashMap<Map> chunks = new TIntPairObjectHashMap<Map>(250);
	static HeightMap heightMap;
	static Map blankMap = new Map(1); //singleton instance used to indicate no pixels to draw in a chunk
	float scale = 1f;
	boolean dirty = true;
	GuiScreen parent = null;

	public MapWidget(GuiScreen parent) {
		this.parent = parent;
		heightMap = HeightMap.getHeightMap(MinimapUtils.getWorldName());

		addProperty("scale", new Property() {

			@Override
			public void set(Object value) {
				setScale((float)(double)(Double) value);
			}

			@Override
			public Object get() {
				return getScale();
			}
		});

		setScrollBarPolicy(Orientation.HORIZONTAL, ScrollBarPolicy.SHOW_NEVER);
		setScrollBarPolicy(Orientation.VERTICAL, ScrollBarPolicy.SHOW_NEVER);
	}

	@Override
	public int getInnerSize(Orientation axis) {
		if (axis == Orientation.HORIZONTAL) {
			return (int) ((double) (heightMap.getMaxX() - heightMap.getMinX() + 1) * scale * 16d);
		} else {
			return (int) ((double) (heightMap.getMaxZ() - heightMap.getMinZ() + 1) * scale * 16d);
		}
	}

	public void drawChunk(int x, int z, boolean force) {
		Map map = chunks.get(x, z);
		if (map == null) {
			map = new Map(16);
			map.originOffsetX = 0;
			map.originOffsetY = 0;
			map.renderSize = 16;
		} else if (!force) {
			return;
		}
		boolean pixelSet = false;
		try {
			for (int cx = 0; cx < 16; cx++) {
				for (int cz = 0; cz < 16; cz++) {
					short height = heightMap.getHeight(cx + x * 16, cz + z * 16);
					byte id = heightMap.getBlockId(cx + x * 16, cz + z * 16);
					if(id == -1 || height == -1) {
						height = 0;
						id = 0;
					} else {
						pixelSet = true;
					}
					map.setHeightPixel(cz, cx, height);
					map.setColorPixel(cz, cx, BlockColor.getBlockColor(id, 0).color | 0xff000000);
				}
			}
		}
		catch (Exception e) {
			pixelSet = false;
		}
		if(pixelSet) {
			chunks.put(x, z, map);
		}
		else {
			chunks.put(x, z, blankMap);
		}
	}

	public void showPlayer() {
		int x = (int) SpoutClient.getHandle().thePlayer.posX;
		int z = (int) SpoutClient.getHandle().thePlayer.posZ;
		scrollTo(x, z);
	}
	
	public void scrollTo(int x, int z) {
		int scrollX = -heightMap.getMinX() * 16, scrollZ = -heightMap.getMinZ() * 16;
		scrollX += x;
		scrollZ += z;
		scrollX *= scale;
		scrollZ *= scale;
		setScrollPosition(Orientation.HORIZONTAL, scrollX - (int) (getWidth() / 2));
		setScrollPosition(Orientation.VERTICAL, scrollZ - (int) (getHeight() / 2));
	}

	@Override
	public void renderContents() {
		
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDepthMask(false);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		int scrollX = (int) (getScrollPosition(Orientation.HORIZONTAL) / scale);
		int scrollY = (int) (getScrollPosition(Orientation.VERTICAL) / scale);
		
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslatef(-heightMap.getMinX() * 16, -heightMap.getMinZ() * 16, 0);

		int 	minChunkX = heightMap.getMinX() + scrollX / 16, 
				minChunkZ = heightMap.getMinZ() + scrollY / 16, 
				maxChunkX = 0, 
				maxChunkZ = 0;
		int horiz = (int) (getWidth() / 16 / scale) + 1;
		int vert = (int) (getHeight() / 16 / scale) + 1;
		maxChunkX = minChunkX + horiz;
		maxChunkZ = minChunkZ + vert;
		
		GL11.glPushMatrix();
		for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
			for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
				drawChunk(chunkX, chunkZ, dirty);
				Map map = chunks.get(chunkX, chunkZ);
				if(map != null && map != blankMap) {
					GL11.glPushMatrix();
					int x = chunkX * 16;
					int y = chunkZ * 16;
					int width = x + 16;
					int height = y + 16;
					map.loadColorImage();
					MinecraftTessellator tessellator = Spoutcraft.getTessellator();
					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV((double) width, (double) height, -90, 1, 1);
					tessellator.addVertexWithUV((double) width, (double) y, -90, 1, 0);
					tessellator.addVertexWithUV((double) x, (double) y, -90, 0, 0);
					tessellator.addVertexWithUV((double) x, (double) height, -90, 0, 1);
					tessellator.draw();
//					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//					RenderUtil.drawRectangle(x, y, width, height, 0x88ffffff);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_COLOR);
					map.loadHeightImage();
					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV((double) width, (double) height, -90, 1, 1);
					tessellator.addVertexWithUV((double) width, (double) y, -90, 1, 0);
					tessellator.addVertexWithUV((double) x, (double) y, -90, 0, 0);
					tessellator.addVertexWithUV((double) x, (double) height, -90, 0, 1);
					tessellator.draw();
					GL11.glPopMatrix();
				}
			}
		}
		int x = (int) SpoutClient.getHandle().thePlayer.posX;
		int z = (int) SpoutClient.getHandle().thePlayer.posZ;
		
		drawPOI("You", x, z, 0xffff0000);
		
		for(Waypoint waypoint : MinimapConfig.getInstance().getWaypoints(MinimapUtils.getWorldName())){			
			drawPOI(waypoint.name, waypoint.x, waypoint.z, 0xff00ff00);
		}
		
		
		
		GL11.glPopMatrix();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		
		
		
		GL11.glEnable(2929);
		GL11.glDisable(3042);
		dirty = false;
	}
	
	private void drawPOI(String name, int x, int z, int color) {
		int mouseX = (int) ((getScreen().getMouseX() - getX() + scrollX) / scale + heightMap.getMinX() * 16);
		int mouseY = (int) ((getScreen().getMouseY() - getY() + scrollY) / scale + heightMap.getMinZ() * 16);
		int radius = (int) (2f / scale);
		if(radius <= 0) {
			radius = 2;
		}
		int mouseRadius = radius * 2;
		if(parent.isInBoundingRect(x - mouseRadius, z - mouseRadius, mouseRadius * 2, mouseRadius * 2, mouseX, mouseY)) {
			color = 0xff0000ff;
			parent.drawTooltip(name, x, z);
		}
		RenderUtil.drawRectangle(x - radius, z - radius, x + radius, z + radius, color);
	}

	@Override
	public WidgetType getType() {
		return WidgetType.ScrollArea;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

}
