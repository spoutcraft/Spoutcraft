/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.api.block.design;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.MinecraftTessellator;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.material.Block;
import org.spoutcraft.api.packet.PacketUtil;
import org.spoutcraft.api.util.MutableIntegerVector;
import org.spoutcraft.client.SpoutcraftWorld;

public class GenericBlockDesign implements BlockDesign {
	protected boolean reset = false;

	protected float lowXBound;
	protected float lowYBound;
	protected float lowZBound;

	protected float highXBound;
	protected float highYBound;
	protected float highZBound;

	protected String textureURL;
	protected String textureAddon;

	protected Texture texture;

	protected float[][] xPos;
	protected float[][] yPos;
	protected float[][] zPos;

	protected float[][] textXPos;
	protected float[][] textYPos;

	protected int[] lightSourceXOffset;
	protected int[] lightSourceYOffset;
	protected int[] lightSourceZOffset;

	protected float maxBrightness = 1.0F;
	protected float minBrightness = 0F;

	protected float brightness = 0.5F;

	protected int renderPass = 0;

	public GenericBlockDesign() {
	}

	public GenericBlockDesign(float lowXBound, float lowYBound, float lowZBound, float highXBound, float highYBound, float highZBound, String textureURL, String textureAddon, float[][] xPos, float[][] yPos, float[][] zPos, float[][] textXPos, float[][] textYPos, int renderPass) {
		this.lowXBound = lowXBound;
		this.lowYBound = lowYBound;
		this.lowZBound = lowZBound;

		this.highXBound = highXBound;
		this.highYBound = highYBound;
		this.highZBound = highZBound;

		this.textureURL = textureURL;
		this.textureAddon = textureAddon;
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
		this.textXPos = textXPos;
		this.textYPos = textYPos;
		this.renderPass = renderPass;
	}

	public float[][] getX() {
		return xPos;
	}

	public float[][] getY() {
		return yPos;
	}

	public float[][] getZ() {
		return zPos;
	}

	public float[][] getTextureXPos() {
		return textXPos;
	}

	public float[][] getTextureYPos() {
		return textYPos;
	}

	public float getBrightness() {
		return brightness;
	}

	public float getMaxBrightness() {
		return maxBrightness;
	}

	public float getMinBrightness() {
		return minBrightness;
	}

	public float getLowXBound() {
		return lowXBound;
	}

	public float getLowYBound() {
		return lowYBound;
	}

	public float getLowZBound() {
		return lowZBound;
	}

	public float getHighXBound() {
		return highXBound;
	}

	public float getHighYBound() {
		return highYBound;
	}

	public float getHighZBound() {
		return highZBound;
	}

	public BlockDesign setMaxBrightness(float maxBrightness) {
		this.maxBrightness = maxBrightness;
		return this;
	}

	public BlockDesign setMinBrightness(float minBrightness) {
		this.minBrightness = minBrightness;
		return this;
	}

	public BlockDesign setBrightness(float brightness) {
		this.brightness = brightness * maxBrightness + (1 - brightness) * minBrightness;
		return this;
	}

	public BlockDesign setRenderPass(int renderPass) {
		this.renderPass = renderPass;
		return this;
	}

	public int getRenderPass() {
		return renderPass;
	}

	public int getVersion() {
		return 3;
	}

	public void read(SpoutInputStream input) throws IOException {
		textureURL = PacketUtil.readString(input);
		if (textureURL.equals(resetString)) {
			reset = true;
			return;
		}
		reset = false;
		textureAddon = PacketUtil.readString(input);
		xPos = PacketUtil.readDoubleArray(input);
		yPos = PacketUtil.readDoubleArray(input);
		zPos = PacketUtil.readDoubleArray(input);
		textXPos = PacketUtil.readDoubleArray(input);
		textYPos = PacketUtil.readDoubleArray(input);
		lowXBound = input.readFloat();
		lowYBound = input.readFloat();
		lowZBound = input.readFloat();
		highXBound = input.readFloat();
		highYBound = input.readFloat();
		highZBound = input.readFloat();
		maxBrightness = input.readFloat();
		minBrightness = input.readFloat();
		renderPass = input.readInt();
		lightSourceXOffset = PacketUtil.readIntArray(input);
		lightSourceYOffset = PacketUtil.readIntArray(input);
		lightSourceZOffset = PacketUtil.readIntArray(input);
	}

	public void read(DataInputStream input) throws IOException {
		textureURL = PacketUtil.readString(input);
		if (textureURL.equals(resetString)) {
			reset = true;
			return;
		}
		reset = false;
		textureAddon = PacketUtil.readString(input);
		xPos = PacketUtil.readDoubleArray(input);
		yPos = PacketUtil.readDoubleArray(input);
		zPos = PacketUtil.readDoubleArray(input);
		textXPos = PacketUtil.readDoubleArray(input);
		textYPos = PacketUtil.readDoubleArray(input);
		lowXBound = input.readFloat();
		lowYBound = input.readFloat();
		lowZBound = input.readFloat();
		highXBound = input.readFloat();
		highYBound = input.readFloat();
		highZBound = input.readFloat();
		maxBrightness = input.readFloat();
		minBrightness = input.readFloat();
		renderPass = input.readInt();
		lightSourceXOffset = PacketUtil.readIntArray(input);
		lightSourceYOffset = PacketUtil.readIntArray(input);
		lightSourceZOffset = PacketUtil.readIntArray(input);
	}

	private final static String resetString = "[reset]";

	public BlockDesign setTexture(String addon, String textureURL) {
		this.textureAddon = addon;
		this.textureURL = textureURL;
		return this;
	}

	public BlockDesign setBoundingBox(float lowX, float lowY, float lowZ, float highX, float highY, float highZ) {
		this.lowXBound = lowX;
		this.lowYBound = lowY;
		this.lowZBound = lowZ;
		this.highXBound = highX;
		this.highYBound = highY;
		this.highZBound = highZ;
		return this;
	}

	public BlockDesign setQuadNumber(int quads) {
		xPos = new float[quads][];
		yPos = new float[quads][];
		zPos = new float[quads][];
		textXPos = new float[quads][];
		textYPos = new float[quads][];
		lightSourceXOffset = new int[quads];
		lightSourceYOffset = new int[quads];
		lightSourceZOffset = new int[quads];

		for (int i = 0; i < quads; i++) {
			xPos[i] = new float[4];
			yPos[i] = new float[4];
			zPos[i] = new float[4];
			textXPos[i] = new float[4];
			textYPos[i] = new float[4];
			lightSourceXOffset[i] = 0;
			lightSourceYOffset[i] = 0;
			lightSourceZOffset[i] = 0;
		}
		return this;
	}

	public BlockDesign setQuad(int quadNumber, float x1, float y1, float z1, int tx1, int ty1, float x2, float y2, float z2, int tx2, int ty2, float x3, float y3, float z3, int tx3, int ty3, float x4, float y4, float z4, int tx4, int ty4, int textureSizeX, int textureSizeY) {
		setVertex(quadNumber, 0, x1, y1, z1, tx1, ty1, textureSizeX, textureSizeY);
		setVertex(quadNumber, 1, x2, y2, z2, tx2, ty2, textureSizeX, textureSizeY);
		setVertex(quadNumber, 2, x3, y3, z3, tx3, ty3, textureSizeX, textureSizeY);
		setVertex(quadNumber, 3, x4, y4, z4, tx4, ty4, textureSizeX, textureSizeY);
		return this;
	}

	public BlockDesign setVertex(int quadNumber, int vertexNumber, float x, float y, float z, int tx, int ty, int textureSizeX, int textureSizeY) {
		xPos[quadNumber][vertexNumber] = x;
		yPos[quadNumber][vertexNumber] = y;
		zPos[quadNumber][vertexNumber] = z;
		textXPos[quadNumber][vertexNumber] = (float) tx / (float) textureSizeX;
		textYPos[quadNumber][vertexNumber] = (float) ty / (float) textureSizeY;
		return this;
	}

	public String getTexureURL() {
		return textureURL;
	}

	public String getTextureAddon() {
		return textureAddon;
	}

	public boolean isReset() {
		return reset;
	}

	public BlockDesign setLightSource(int quad, int x, int y, int z) {
		lightSourceXOffset[quad] = x;
		lightSourceYOffset[quad] = y;
		lightSourceZOffset[quad] = z;
		return this;
	}

	public MutableIntegerVector getLightSource(int quad, int x, int y, int z) {
		MutableIntegerVector blockVector = new MutableIntegerVector(x + lightSourceXOffset[quad], y + lightSourceYOffset[quad], z + lightSourceZOffset[quad]);
		return blockVector;
	}

	public BlockDesign setTexture(String addon, Texture texture) {
		this.texture = texture;
		return setTexture(addon, texture.getTexture());
	}

	public Texture getTexture() {
		return texture;
	}

	public BlockDesign setQuad(Quad quad) {
		return setVertex(quad.getVertex(0)).setVertex(quad.getVertex(1)).setVertex(quad.getVertex(2)).setVertex(quad.getVertex(3));
	}

	public BlockDesign setVertex(Vertex vertex) {
		return setVertex(vertex.getQuadNum(), vertex.getIndex(), vertex.getX(), vertex.getY(), vertex.getZ(), vertex.getTextureX(), vertex.getTextureY(), vertex.getTextureWidth(), vertex.getTextureHeight());
	}

	public boolean renderBlock(Block block, int x, int y, int z) {
		SpoutcraftWorld world = Spoutcraft.getWorld();
		if (block != null) {
			boolean enclosed = true;
			enclosed &= world.isOpaque(x, y + 1, z);
			enclosed &= world.isOpaque(x, y - 1, z);
			enclosed &= world.isOpaque(x, y, z + 1);
			enclosed &= world.isOpaque(x, y, z - 1);
			enclosed &= world.isOpaque(x + 1, y, z);
			enclosed &= world.isOpaque(x - 1, y, z);
			if (enclosed) {
				return false;
			}
		}

		if (getX() == null) {
			return false;
		}

		setBrightness(1F);

		MinecraftTessellator tessellator = Spoutcraft.getTessellator();

		int internalLightLevel = 0;
		if (block == null) {
			internalLightLevel = 0x00F000F0;
		} else {
			internalLightLevel = world.getMixedBrightnessAt(block, x, y, z);
		}

		for (int i = 0; i < getX().length; i++) {
			MutableIntegerVector sourceBlock = getLightSource(i, x, y, z);

			int sideBrightness;

			if (block != null && sourceBlock != null) {
				sideBrightness = world.getMixedBrightnessAt(block, sourceBlock.getBlockX(), sourceBlock.getBlockY(), sourceBlock.getBlockZ());
			} else {
				sideBrightness = internalLightLevel;
			}

			if (block == null) {
				if (i == 5) {
					tessellator.setNormal(0, 1, 0);
				} else if (i == 3) {
					tessellator.setNormal(0, 0, 1);
				} else if (i == 2) {
					tessellator.setNormal(1, 0, 0);
				}
			}

			tessellator.setBrightness(sideBrightness);

			tessellator.setColorOpaqueFloat(1.0F, 1.0F, 1.0F);

			float[] xx = getX()[i];
			float[] yy = getY()[i];
			float[] zz = getZ()[i];
			float[] tx = getTextureXPos()[i];
			float[] ty = getTextureYPos()[i];

			for (int j = 0; j < 4; j++) {
				tessellator.addVertexWithUV(x + xx[j], y + yy[j], z + zz[j], tx[j], ty[j]);
			}
		}
		return true;
	}

	public boolean renderItemstack(net.minecraft.src.ItemStack item, float x, float y, float depth, float rotation, float scale, Random rand) {
		int items = 1;
		if (item != null) {
			int amt = item.stackSize;
			if (amt > 1) {
				items = 2;
			}

			if (amt > 5) {
				items = 3;
			}

			if (amt > 20) {
				items = 4;
			}
		}

		boolean result = false;

		GL11.glPushMatrix();
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glTranslatef(x, y, depth);

		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(scale, scale, scale);

		Spoutcraft.getTessellator().startDrawingQuads();
		Spoutcraft.getTessellator().setNormal(0.0F, -1.0F, 0.0F);

		for (int i = 0; i < items; ++i) {
			GL11.glPushMatrix();
			if (i > 0) {
				float rotX = (rand.nextFloat() * 2.0F - 1.0F) * 0.2F / 0.25F;
				float rotY = (rand.nextFloat() * 2.0F - 1.0F) * 0.2F / 0.25F;
				float rotZ = (rand.nextFloat() * 2.0F - 1.0F) * 0.2F / 0.25F;
				GL11.glTranslatef(rotX, rotY, rotZ);
			}

			result &= renderBlock(null, 0, 0, 0);

			GL11.glPopMatrix();
		}

		Spoutcraft.getTessellator().draw();
		GL11.glPopMatrix();

		return result;
	}

	public boolean renderItemOnHUD(float x, float y, float depth) {
		GL11.glPushMatrix();
		Spoutcraft.getTessellator().startDrawingQuads();

		GL11.glTranslatef(x, y+1, depth);
		GL11.glScalef(10.0F, 10.0F, 10.0F);
		GL11.glTranslatef(1.0F, 0.5F, 1.0F);
		GL11.glScalef(1.0F, 1.0F, -1.0F);
		GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		boolean result = renderBlock(null, 0, 0, 0);
		Spoutcraft.getTessellator().draw();
		GL11.glPopMatrix();

		return result;
	}
}
