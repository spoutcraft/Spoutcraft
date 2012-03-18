package org.spoutcraft.client.gui.minimap;

import java.awt.image.BufferedImage;

/**
 * @author lahwran
 *
 */
public class Map {

	/**
	 * Used to track world changes and force a rerender when the world
	 * changes.
	 */
	public String lastRenderedWorld = "";

	/**
	 * Used to track dimension changes and force a rerender when the dimension
	 * changes.
	 */
	public int lastRenderedDimension = 0;

	/**
	 * X coordinate of the player on last render
	 */
	private double playerX = Integer.MAX_VALUE;

	/**
	 * Z coordinate of the player on last render
	 */
	private double playerZ = Integer.MAX_VALUE;

	/**
	 * Zoom level currently rendering at at - used in case zoom changes in the
	 * middle of rendering a map frame
	 */
	public int zoom = 0;

	/**
	 * How many blocks to x+ to shift the map rendering from the origin of the
	 * center chunk
	 */
	public int originOffsetX = 0;

	/**
	 * How many blocks to z+ to shift the map rendering from the origin of the
	 * center chunk
	 */
	public int originOffsetY = 0;

	public int timer = 0;

	public int imageSize = 276;

	public int updatedist = 4;

	public final int renderSize = 256;

	public final int renderOff = 128; //used instead of dividing renderSize by two

	/**
	 * Map image to which the map is rendered to.
	 */
	private final ImageManager colorimg;

	private final ImageManager heightimg;

	private final ImageManager lightimg;

	public Map() {
		colorimg = new ImageManager(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
		heightimg = new ImageManager(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
		lightimg = new ImageManager(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * Take an array index and wrap it to the size of the array, properly
	 * dealing with negative values
	 * @param index index to wrap
	 * @param arraysize size of array to wrap to
	 * @return wrapped index
	 */
	private final int wrapIndex(int index, int arraysize) {
		if (index < 0)
			return arraysize + ((index+1) % arraysize) - 1;
		else
			return index % arraysize;
	}

	public final int toImageX(int worldz) {
		return wrapIndex((int) (( -(worldz - playerZ)) + originOffsetX), imageSize);
	}

	public final int toImageY(int worldx) {
		return wrapIndex( (int) ((worldx - playerX) + originOffsetY), imageSize);
	}

	public void setColorPixel(int worldx, int worldz, int color24) {
		colorimg.setRGB(toImageX(worldz), toImageY(worldx), color24);
	}

	public void setHeightPixel(int worldx, int worldz, int height) {
		heightimg.setRGB(toImageX(worldz), toImageY(worldx), height | height << 8 | height << 16);
	}

	public void setLightPixel(int worldx, int worldz, int light) {
		lightimg.setRGB(toImageX(worldz), toImageY(worldx), light | light << 8 | light << 16);
	}

	public void loadColorImage() {
		colorimg.loadGLImage();
	}

	public void loadHeightImage() {
		heightimg.loadGLImage();
	}

	public void loadLightImage() {
		lightimg.loadGLImage();
	}

	public void update(double playerx, double playerz) {
		this.playerX = playerx;
		this.playerZ = playerz;
		originOffsetX = wrapIndex((int) -this.playerZ, imageSize);
		originOffsetY = wrapIndex((int) this.playerX, imageSize);
	}

	public double getPlayerX() {
		return playerX;
	}

	public double getPlayerZ() {
		return playerZ;
	}

	public boolean isDirty(double newPlayerX, double newPlayerZ) {
		return Math.abs(playerX - newPlayerX) > updatedist || Math.abs(playerZ - newPlayerZ) > updatedist || timer > 300;
	}

	public float getRenderScale() {
		float displaydist = (float) (Math.pow(2, zoom) * 32);
		return (float)imageSize / displaydist;
	}

	/**
	 * @return
	 */
	public double getCurrOffsetX(double playerZ) {
		double wrapped = wrapIndex(((int) -playerZ), imageSize);
		double leftover = 0;//playerZ - ((double)(int)playerZ);
		return (wrapped+leftover)/2;
	}
	public double getCurrOffsetY(double playerX) {
		double wrapped = wrapIndex(((int) playerX), imageSize);
		double leftover = 0;//playerX - ((double)(int)playerX);
		return (wrapped+leftover)/2;
	}
}
