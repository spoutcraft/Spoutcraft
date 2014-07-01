package net.minecraft.src;

import com.google.common.collect.Lists;
import com.prupe.mcpatcher.hd.AAHelper;
import com.prupe.mcpatcher.hd.MipmapHelper;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;

public class TextureAtlasSprite implements Icon {
	private final String iconName;
	public List framesTextureData = Lists.newArrayList();
	private AnimationMetadataSection animationMetadata;
	protected boolean rotated;
	protected int originX;
	protected int originY;
	protected int width;
	protected int height;
	private float minU;
	private float maxU;
	private float minV;
	private float maxV;
	protected int frameCounter;
	protected int tickCounter;
	public List mipmaps = null;

	public TextureAtlasSprite(String par1Str) {
		this.iconName = par1Str;
	}

	public void initSprite(int par1, int par2, int par3, int par4, boolean par5) {
		this.originX = par3;
		this.originY = par4;
		this.rotated = par5;
		float var6 = (float)(0.009999999776482582D / (double)par1);
		float var7 = (float)(0.009999999776482582D / (double)par2);
		this.minU = (float)par3 / (float)((double)par1) + var6;
		this.maxU = (float)(par3 + this.width) / (float)((double)par1) - var6;
		this.minV = (float)par4 / (float)par2 + var7;
		this.maxV = (float)(par4 + this.height) / (float)par2 - var7;
	}

	public void copyFrom(TextureAtlasSprite par1TextureAtlasSprite) {
		this.originX = par1TextureAtlasSprite.originX;
		this.originY = par1TextureAtlasSprite.originY;
		this.width = par1TextureAtlasSprite.width;
		this.height = par1TextureAtlasSprite.height;
		this.rotated = par1TextureAtlasSprite.rotated;
		this.minU = par1TextureAtlasSprite.minU;
		this.maxU = par1TextureAtlasSprite.maxU;
		this.minV = par1TextureAtlasSprite.minV;
		this.maxV = par1TextureAtlasSprite.maxV;
	}

	/**
	 * Returns the X position of this icon on its texture sheet, in pixels.
	 */
	public int getOriginX() {
		return this.originX;
	}

	/**
	 * Returns the Y position of this icon on its texture sheet, in pixels.
	 */
	public int getOriginY() {
		return this.originY;
	}

	/**
	 * Returns the width of the icon, in pixels.
	 */
	public int getIconWidth() {
		return this.width;
	}

	/**
	 * Returns the height of the icon, in pixels.
	 */
	public int getIconHeight() {
		return this.height;
	}

	/**
	 * Returns the minimum U coordinate to use when rendering with this icon.
	 */
	public float getMinU() {
		return this.minU;
	}

	/**
	 * Returns the maximum U coordinate to use when rendering with this icon.
	 */
	public float getMaxU() {
		return this.maxU;
	}

	/**
	 * Gets a U coordinate on the icon. 0 returns uMin and 16 returns uMax. Other arguments return in-between values.
	 */
	public float getInterpolatedU(double par1) {
		float var3 = this.maxU - this.minU;
		return this.minU + var3 * (float)par1 / 16.0F;
	}

	/**
	 * Returns the minimum V coordinate to use when rendering with this icon.
	 */
	public float getMinV() {
		return this.minV;
	}

	/**
	 * Returns the maximum V coordinate to use when rendering with this icon.
	 */
	public float getMaxV() {
		return this.maxV;
	}

	/**
	 * Gets a V coordinate on the icon. 0 returns vMin and 16 returns vMax. Other arguments return in-between values.
	 */
	public float getInterpolatedV(double par1) {
		float var3 = this.maxV - this.minV;
		return this.minV + var3 * ((float)par1 / 16.0F);
	}

	public String getIconName() {
		return this.iconName;
	}

	public void updateAnimation() {
		++this.tickCounter;

		if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
			int var1 = this.animationMetadata.getFrameIndex(this.frameCounter);
			int var2 = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
			this.frameCounter = (this.frameCounter + 1) % var2;
			this.tickCounter = 0;
			int var3 = this.animationMetadata.getFrameIndex(this.frameCounter);

			if (var1 != var3 && var3 >= 0 && var3 < this.framesTextureData.size()) {
				MipmapHelper.copySubTexture(this, var3);
			}
		}
	}

	public int[] getFrameTextureData(int par1) {
		return (int[])this.framesTextureData.get(par1);
	}

	public int getFrameCount() {
		List var1 = this.framesTextureData;
		return var1 != null ? var1.size() : 1;
	}

	public void setIconWidth(int par1) {
		this.width = par1;
	}

	public void setIconHeight(int par1) {
		this.height = par1;
	}

	public void loadSprite(Resource par1Resource) throws IOException {
		this.resetSprite();
		InputStream var2 = par1Resource.getInputStream();
		AnimationMetadataSection var3 = (AnimationMetadataSection)par1Resource.getMetadata("animation");
		BufferedImage var4 = AAHelper.addBorder(this, par1Resource, ImageIO.read(var2));
		this.height = var4.getHeight();
		this.width = var4.getWidth();
		int[] var5 = new int[this.height * this.width];
		var4.getRGB(0, 0, this.width, this.height, var5, 0, this.width);

		if (var3 == null) {
			if (this.height != this.width) {
				throw new RuntimeException("broken aspect ratio and not an animation");
			}

			this.framesTextureData.add(var5);
		} else {
			int var6 = this.height / this.width;
			int var7 = this.width;
			int var8 = this.width;
			this.height = this.width;
			int var10;

			if (var3.getFrameCount() > 0) {
				Iterator var9 = var3.getFrameIndexSet().iterator();

				while (var9.hasNext()) {
					var10 = ((Integer)var9.next()).intValue();

					if (var10 >= var6) {
						throw new RuntimeException("invalid frameindex " + var10);
					}

					this.allocateFrameTextureData(var10);
					this.framesTextureData.set(var10, getFrameTextureData(var5, var7, var8, var10));
				}

				this.animationMetadata = var3;
			} else {
				ArrayList var11 = Lists.newArrayList();

				for (var10 = 0; var10 < var6; ++var10) {
					this.framesTextureData.add(getFrameTextureData(var5, var7, var8, var10));
					var11.add(new AnimationFrame(var10, -1));
				}

				this.animationMetadata = new AnimationMetadataSection(var11, this.width, this.height, var3.getFrameTime());
			}
		}
	}

	private void allocateFrameTextureData(int par1) {
		if (this.framesTextureData.size() <= par1) {
			for (int var2 = this.framesTextureData.size(); var2 <= par1; ++var2) {
				this.framesTextureData.add((Object)null);
			}
		}
	}

	private static int[] getFrameTextureData(int[] par0ArrayOfInteger, int par1, int par2, int par3) {
		int[] var4 = new int[par1 * par2];
		System.arraycopy(par0ArrayOfInteger, par3 * var4.length, var4, 0, var4.length);
		return var4;
	}

	public void clearFramesTextureData() {
		this.framesTextureData.clear();
	}

	public boolean hasAnimationMetadata() {
		return this.animationMetadata != null;
	}

	public void setFramesTextureData(List par1List) {
		this.framesTextureData = par1List;
	}

	private void resetSprite() {
		this.animationMetadata = null;
		this.setFramesTextureData(Lists.newArrayList());
		this.frameCounter = 0;
		this.tickCounter = 0;
	}

	public String toString() {
		return "TextureAtlasSprite{name=\'" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
	}
}