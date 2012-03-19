package org.spoutcraft.client.gui.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;

/**
 * @author lahwran
 * 
 */
public class MapRenderer {

	/** Direction you're facing */
	public float direction = 0.0f;

	/** Last direction you were facing */
	public float oldDir = 0.0f;

	private Map map;
	private TextureManager texman;

	/**
	 * @param minimap minimap instance to init with
	 */
	public MapRenderer(ZanMinimap minimap) {
		map = minimap.map;
		texman = minimap.texman;
	}

	/**
	 * Do rendering
	 * 
	 * @param scWidth screen width
	 * @param scHeight screen height
	 */
	public void onRenderTick(int scWidth, int scHeight) {
		if (this.oldDir != Minecraft.theMinecraft.thePlayer.rotationYaw) {
			this.direction += this.oldDir - Minecraft.theMinecraft.thePlayer.rotationYaw;
			this.oldDir = Minecraft.theMinecraft.thePlayer.rotationYaw;
		}

		if (this.direction >= 360.0f)
			this.direction %= 360.0f;

		if (this.direction < 0.0f) {
			while (this.direction < 0.0f)
				this.direction += 360.0f;
		}

		GL11.glPushMatrix();
		GL11.glTranslatef(scWidth, 0.0f, 0.0f);
		GL11.glTranslatef(MinimapConfig.getInstance().getAdjustX(), MinimapConfig.getInstance().getAdjustY(), 0);
		GL11.glScalef(MinimapConfig.getInstance().getSizeAdjust(), MinimapConfig.getInstance().getSizeAdjust(), 1F);
		if (!MinimapConfig.getInstance().isScale()) {
			GL11.glScalef(scWidth / 427, scHeight / 240F, 1);
		}
		renderMap();
		if (MinimapConfig.getInstance().isCavemap())
			renderMapFull(0, scHeight);

		GL11.glDepthMask(true);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		showCoords(0);
		GL11.glPopMatrix();
	}

	private void renderMap() {
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(770, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		if (MinimapConfig.getInstance().isEnabled()) {
			if (MinimapConfig.getInstance().isSquare()) {

				//Scale
				GL11.glPushMatrix();
				switch (MinimapConfig.getInstance().getZoom()) {
					case 0: GL11.glScalef(8F, 8F, 1F); GL11.glTranslatef(56, 0, 0F); break;
					case 1: GL11.glScalef(4F, 4F, 1F); GL11.glTranslatef(48, 0, 0F); break;
					case 2: GL11.glScalef(2F, 2F, 1F); GL11.glTranslatef(32, 0, 0F); break;
				}
				map.loadColorImage();

				drawOnMap();
				GL11.glPopMatrix();

				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				try {
					GL11.glPushMatrix();
					GL11.glScalef(1.8f, 1.8f, 1.0f);
					GL11.glTranslatef(27, -1, 0F); //don't ask
					texman.loadMinimap();
					drawOnMap();
				} catch (Exception e) {
					System.err.println("error: minimap overlay not found!");
					e.printStackTrace();
				}
				finally {
					GL11.glPopMatrix();
				}

				try {
					GL11.glPushMatrix();
					texman.loadMMArrow();
					GL11.glTranslatef(- 32.0F, 32.0F, 0.0F);
					GL11.glRotatef(this.direction - 90F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(32.0F, -(32.0F), 0.0F);
					drawOnMap();
				} catch (Exception e) {
					System.err.println("Error: minimap arrow not found!");
					e.printStackTrace();
				} finally {
					GL11.glPopMatrix();
				}
				
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				renderWaypoints();
				
				//render directions with fudge factor to make them line up
				GL11.glPushMatrix();
				GL11.glTranslatef(-2, 2, 0.0F);
				drawDirections();
				GL11.glPopMatrix();
				
			} else {
				GL11.glPushMatrix();

				switch (MinimapConfig.getInstance().getZoom()) {
				case 0: GL11.glScalef(8F, 8F, 1F); GL11.glTranslatef(56, 0, 0F); break;
				case 1: GL11.glScalef(4F, 4F, 1F); GL11.glTranslatef(48, 0, 0F); break;
				case 2: GL11.glScalef(2F, 2F, 1F); GL11.glTranslatef(32, 0, 0F); break;
				}
				map.loadColorImage();

				GL11.glTranslatef(-32.0f, 32.0F, 0.0F);
				GL11.glRotatef(this.direction + 90.0F, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef( 32.0F, -(32.0F), 0.0F);

				drawOnMap();
				
				GL11.glPopMatrix();
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				renderWaypoints();
				drawRound();
				drawDirections();
			}
		}
	}
	
	private String getWorldName() {
		String worldname = Minecraft.theMinecraft.theWorld.getWorldInfo().getWorldName();
		if (worldname.equals("MpServer")) {
			return org.spoutcraft.client.gui.error.GuiConnectionLost.lastServerIp;
		}
		return worldname;
	}

	private void renderWaypoints() {
		for (Waypoint pt : MinimapConfig.getInstance().getWaypoints(getWorldName())) {
			if (pt.enabled) {
				double wayX = Minecraft.theMinecraft.thePlayer.posX - pt.x;
				double wayY = Minecraft.theMinecraft.thePlayer.posZ - pt.z;
				float locate = (float) Math.toDegrees(Math.atan2(wayX, wayY));
				double hypot = Math.sqrt((wayX * wayX) + (wayY * wayY)) / (Math.pow(2.0, MinimapConfig.getInstance().getZoom()) / 2.0);
				if (hypot >= 28.0D) {
					try {
						GL11.glPushMatrix();
						GL11.glColor3f(pt.red, pt.green, pt.blue);
						texman.loadMarker();
						GL11.glTranslatef(-32.0F, 32.0F, 0.0F);
						GL11.glRotatef(-locate + this.direction + 180.0F, 0.0F, 0.0F, 1.0F);
						GL11.glTranslatef(32.0F, -(32.0F), 0.0F);
						GL11.glTranslated(0.0D, -34.0D, 0.0D);
						drawOnMap();
					} catch (Exception e) {
						System.err.println("Error: marker overlay not found!");
						e.printStackTrace();
					} finally {
						GL11.glPopMatrix();
					}
				} else {
					try {
						GL11.glPushMatrix();
						GL11.glColor3f(pt.red, pt.green, pt.blue);
						texman.loadWaypoint();
						GL11.glTranslatef(- 32.0F, 32.0F, 0.0F);
						GL11.glRotatef(-locate + this.direction + 180.0F, 0.0F, 0.0F, 1.0F);
						GL11.glTranslated(0.0D, -hypot, 0.0D);
						GL11.glRotatef(-(-locate + this.direction + 180.0F),
								0.0F,
								0.0F,
								1.0F);
						GL11.glTranslated(0.0D, hypot, 0.0D);
						GL11.glTranslatef(32.0F, -(32.0F), 0.0F);
						GL11.glTranslated(0.0D, -hypot, 0.0D);
						drawOnMap();
					} catch (Exception e) {
						System.err.println("Error: waypoint overlay not found!");
						e.printStackTrace();
					} finally {
						GL11.glPopMatrix();
					}
				}
			}
		}
		GL11.glColor3f(1f, 1f, 1f);
	}

	private void renderMapFull(int scWidth, int scHeight) {
		map.loadColorImage();
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV((scWidth) / 2 - 128,
				(scHeight) / 2 + 128,
				1.0D,
				0.0D,
				1.0D);
		Tessellator.instance.addVertexWithUV((scWidth) / 2 + 128,
				(scHeight) / 2 + 128,
				1.0D,
				1.0D,
				1.0D);
		Tessellator.instance.addVertexWithUV((scWidth) / 2 + 128,
				(scHeight) / 2 - 128,
				1.0D,
				1.0D,
				0.0D);
		Tessellator.instance.addVertexWithUV((scWidth) / 2 - 128,
				(scHeight) / 2 - 128,
				1.0D,
				0.0D,
				0.0D);
		Tessellator.instance.draw();

		try {
			GL11.glPushMatrix();
			texman.loadMMArrow();
			GL11.glTranslatef((scWidth) / 2, (scHeight) / 2, 0.0F);
			GL11.glRotatef(-this.direction - 90.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-((scWidth) / 2), -((scHeight) / 2), 0.0F);
			Tessellator.instance.startDrawingQuads();
			Tessellator.instance.addVertexWithUV((scWidth) / 2 - 32,
					(scHeight) / 2 + 32,
					1.0D,
					0.0D,
					1.0D);
			Tessellator.instance.addVertexWithUV((scWidth) / 2 + 32,
					(scHeight) / 2 + 32,
					1.0D,
					1.0D,
					1.0D);
			Tessellator.instance.addVertexWithUV((scWidth) / 2 + 32,
					(scHeight) / 2 - 32,
					1.0D,
					1.0D,
					0.0D);
			Tessellator.instance.addVertexWithUV((scWidth) / 2 - 32,
					(scHeight) / 2 - 32,
					1.0D,
					0.0D,
					0.0D);
			Tessellator.instance.draw();
		} catch (Exception e) {
			System.err.println("Error: minimap arrow not found!");
			e.printStackTrace();
		} finally {
			GL11.glPopMatrix();
		}
	}

	private void showCoords(int scWidth) {
		if (MinimapConfig.getInstance().isCoords()) {
			GL11.glPushMatrix();
			GL11.glScalef(0.5f, 0.5f, 1.0f);
			String xy = format((int) Minecraft.theMinecraft.thePlayer.posX) + ", " + format((int) Minecraft.theMinecraft.thePlayer.posZ);
			int m = Minecraft.theMinecraft.fontRenderer.getStringWidth(xy) / 2;
			Minecraft.theMinecraft.fontRenderer.drawString(xy, scWidth * 2 - 32 * 2 - m, 146, 0xffffff);
			xy = Integer.toString((int) Minecraft.theMinecraft.thePlayer.posY);
			m = Minecraft.theMinecraft.fontRenderer.getStringWidth(xy) / 2;
			Minecraft.theMinecraft.fontRenderer.drawString(xy, scWidth * 2 - 32 * 2 - m, 156, 0xffffff);
			GL11.glPopMatrix();
		}// else
		//	obfhub.write("(" + format((int) Minecraft.theMinecraft.thePlayer.posX) + ", " + Minecraft.theMinecraft.thePlayer.posY
		//			+ ", " + format((int) Minecraft.theMinecraft.thePlayer.posZ) + ") " + (int) this.direction
		//			+ "'", 2, 10, 0xffffff);
	}
	
	private String format(int coord) {
		if (coord < 0)
			return "-" + Math.abs(coord + 1);
		else
			return "+" + coord;
	}

	private void drawRound() {
		try {
			GL11.glPushMatrix();
			texman.loadRoundmap();
			drawOnMap();
		} catch (Exception localException) {
			System.err.println("Error: minimap overlay not found!");
		}
		finally {
			GL11.glPopMatrix();
		}
	}

	private void drawOnMap() {
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV(-64.0D, 64.0D, 1.0D, 0.0D, 1.0D);
		Tessellator.instance.addVertexWithUV(0, 64.0D, 1.0D, 1.0D, 1.0D);
		Tessellator.instance.addVertexWithUV(0, 0, 1.0D, 1.0D, 0.0D);
		Tessellator.instance.addVertexWithUV(-64.0D, 0, 1.0D, 0.0D, 0.0D);
		Tessellator.instance.draw();
	}

	private void drawDirections() {
		if (!MinimapConfig.getInstance().isDirections()) {
			return;
		}
		float dir = this.direction;
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 1.0f);
		GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(dir)))), (64.0D * Math.cos(Math.toRadians(-(dir)))), 0.0D);
		Minecraft.theMinecraft.fontRenderer.drawString("N", - 66, 60, 0xffffff);
		GL11.glPopMatrix();
		
		dir += 90;
		
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 1.0f);
		GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-dir))), (64.0D * Math.cos(Math.toRadians(-dir))), 0.0D);
		Minecraft.theMinecraft.fontRenderer.drawString("E", - 66, 60, 0xffffff);
		GL11.glPopMatrix();
		
		dir += 90;
		
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 1.0f);
		GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(dir)))), (64.0D * Math.cos(Math.toRadians(-(dir)))), 0.0D);
		Minecraft.theMinecraft.fontRenderer.drawString("S", - 66, 60, 0xffffff);
		GL11.glPopMatrix();
		
		dir += 90;
		
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 1.0f);
		GL11.glTranslated((64.0D * Math.sin(Math.toRadians(-(dir)))), (64.0D * Math.cos(Math.toRadians(-(dir)))), 0.0D);
		Minecraft.theMinecraft.fontRenderer.drawString("W", - 66, 60, 0xffffff);
		GL11.glPopMatrix();
	}
}
