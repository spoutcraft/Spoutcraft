package org.getspout.spout.item;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.Block;
import net.minecraft.src.Tessellator;

import org.getspout.spout.packet.PacketUtil;

public class SpoutCustomBlockDesign {
	
	private boolean reset = false;

	private float lowXBound;
	private float lowYBound;
	private float lowZBound;
	private float highXBound;
	private float highYBound;
	private float highZBound;

	private String textureURL;
	private String texturePlugin;

	private float[][] xPos;
	private float[][] yPos;
	private float[][] zPos;

	private float[][] textXPos;
	private float[][] textYPos;
	
	public SpoutCustomBlockDesign() {
	}

	public SpoutCustomBlockDesign(float lowXBound, float lowYBound, float lowZBound, float highXBound, float highYBound, float highZBound, String textureURL, String texturePlugin,
			float[][] xPos, float[][] yPos, float[][] zPos, float[][] textXPos, float[][] textYPos) {
		this.lowXBound = lowXBound;
		this.lowYBound = lowYBound;
		this.lowZBound = lowZBound;
		this.highXBound = highXBound;
		this.highYBound = highYBound;
		this.highZBound = highZBound;
		this.textureURL = textureURL;
		this.texturePlugin = texturePlugin;
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
		this.textXPos = textXPos;
		this.textYPos = textYPos;
	}

	public void setBounds(Block block) {
		block.setBlockBounds(lowXBound, lowYBound, lowZBound, highXBound, highYBound, highZBound);
	}

	public void draw(Tessellator tessallator, int x, int y, int z) {

		tessallator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

		for (int i = 0; i < xPos.length; i++) {
			float[] xx = xPos[i];
			float[] yy = yPos[i];
			float[] zz = zPos[i];
			float[] tx = textXPos[i];
			float[] ty = textYPos[i];

			for (int j = 0; j < 4; j++) {
				tessallator.addVertexWithUV(x + xx[j], y + yy[j], z + zz[j], tx[j], ty[j]);
			}
		}

	}

	public int getNumBytes() {
		return PacketUtil.getNumBytes(textureURL) + PacketUtil.getNumBytes(texturePlugin) + getDoubleArrayLength(xPos) + getDoubleArrayLength(yPos) + getDoubleArrayLength(zPos)
		+ getDoubleArrayLength(textXPos) + getDoubleArrayLength(textYPos) + 6 * 4;
	}

	public void read(DataInputStream input) throws IOException {
		textureURL = PacketUtil.readString(input);
		if (textureURL.equals(resetString)) {
			reset = true;
			return;
		}
		reset = false;
		texturePlugin = PacketUtil.readString(input);
		xPos = readDoubleArray(input);
		yPos = readDoubleArray(input);
		zPos = readDoubleArray(input);
		textXPos = readDoubleArray(input);
		textYPos = readDoubleArray(input);
		lowXBound = input.readFloat();
		lowYBound = input.readFloat();
		lowZBound = input.readFloat();
		highXBound = input.readFloat();
		highYBound = input.readFloat();
		highZBound = input.readFloat();
	}

	private final static String resetString = "[reset]";
	
	public static void writeReset(DataOutputStream output) {
		PacketUtil.writeString(output, resetString);
	}
	
	public static int getResetNumBytes() {
		return PacketUtil.getNumBytes(resetString);
	}

	public void write(DataOutputStream output) throws IOException {
		if (reset) {
			PacketUtil.writeString(output, resetString);
			return;
		}
		PacketUtil.writeString(output, textureURL);
		PacketUtil.writeString(output, texturePlugin);
		writeDoubleArray(output, xPos);
		writeDoubleArray(output, yPos);
		writeDoubleArray(output, zPos);
		writeDoubleArray(output, textXPos);
		writeDoubleArray(output, textYPos);
		output.writeFloat(lowXBound);
		output.writeFloat(lowYBound);
		output.writeFloat(lowZBound);
		output.writeFloat(highXBound);
		output.writeFloat(highYBound);
		output.writeFloat(highZBound);
	}

	private float[] readQuadFloat(DataInputStream input) throws IOException {
		float[] newArray = new float[4];
		for (int i = 0; i < 4; i++) {
			newArray[i] = input.readFloat();
		}
		return newArray;
	}

	private int getDoubleArrayLength(float[][] doubleArray) {
		return doubleArray.length * 16;
	}

	private float[][] readDoubleArray(DataInputStream input) throws IOException {
		int length = input.readShort();
		if (length > 256) {
			throw new IllegalArgumentException("Double array exceeded max length (" + length + ")");
		}
		float[][] newDoubleArray = new float[length][];
		for (int i = 0; i < length; i++) {
			newDoubleArray[i] = readQuadFloat(input);
		}
		return newDoubleArray;
	}

	private void writeQuadFloat(DataOutputStream output, float[] floats) throws IOException {
		if (floats.length != 4) {
			throw new IllegalArgumentException("Array containing " + floats.length + " floats passed to writeQuadFloat");
		}
		for (int i = 0; i < 4; i++) {
			output.writeFloat(floats[i]);
		}
	}

	private void writeDoubleArray(DataOutputStream output, float[][] floats) throws IOException {
		if (floats.length > 256) {
			throw new IllegalArgumentException("Double array exceeded max length (" + floats.length + ")");
		}

		output.writeShort(floats.length);
		for (int i = 0; i < floats.length; i++) {
			writeQuadFloat(output, floats[i]);
		}
	}
	
	public void setTexture(String plugin, String textureURL) {
		this.texturePlugin = plugin;
		this.textureURL = textureURL;
	}
	
	public void setBoundingBox(float lowX, float lowY, float lowZ, float highX, float highY, float highZ) {
		this.lowXBound = lowX;
		this.lowYBound = lowY;
		this.lowZBound = lowZ;
		this.highXBound = highX;
		this.highYBound = highY;
		this.highZBound = highZ;
	}
	
	public void setQuadNumber(int quads) {
		xPos = new float[quads][];
		yPos = new float[quads][];
		zPos = new float[quads][];
		textXPos = new float[quads][];
		textYPos = new float[quads][];
		
		for (int i = 0; i < quads; i++) {
			xPos[i] = new float[4];
			yPos[i] = new float[4];
			zPos[i] = new float[4];
			textXPos[i] = new float[4];
			textYPos[i] = new float[4];
		}
	}
	
	public void setQuad(int quadNumber,
			float x1, float y1, float z1, int tx1, int ty1,
			float x2, float y2, float z2, int tx2, int ty2,
			float x3, float y3, float z3, int tx3, int ty3,
			float x4, float y4, float z4, int tx4, int ty4,
			int textureSizeX, int textureSizeY) {
		
		setVertex(quadNumber, 0, x1, y1, z1, tx1, ty1, textureSizeX, textureSizeY);
		setVertex(quadNumber, 1, x2, y2, z2, tx2, ty2, textureSizeX, textureSizeY);
		setVertex(quadNumber, 2, x3, y3, z3, tx3, ty3, textureSizeX, textureSizeY);
		setVertex(quadNumber, 3, x4, y4, z4, tx4, ty4, textureSizeX, textureSizeY);
		
	}
	
	public void setVertex(int quadNumber, int vertexNumber, float x, float y, float z, int tx, int ty, int textureSizeX, int textureSizeY) {
		xPos[quadNumber][vertexNumber] = x;
		yPos[quadNumber][vertexNumber] = y;
		zPos[quadNumber][vertexNumber] = z;
		textXPos[quadNumber][vertexNumber] = (float)tx / (float)textureSizeX;
		textYPos[quadNumber][vertexNumber] = (float)ty / (float)textureSizeY;
	}

	public String getTexureURL() {
		return textureURL;
	}

	public String getTexturePlugin() {
		return texturePlugin;
	}
	
	public boolean getReset() {
		return reset;
	}

}
