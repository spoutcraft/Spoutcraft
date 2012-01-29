package net.minecraft.src;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.ClippingHelper;
import net.minecraft.src.ClippingHelperImpl;
import net.minecraft.src.ICamera;

public class Frustrum implements ICamera {

	private ClippingHelper clippingHelper = ClippingHelperImpl.getInstance();
	private double xPosition;
	private double yPosition;
	private double zPosition;


	public void setPosition(double var1, double var3, double var5) {
		this.xPosition = var1;
		this.yPosition = var3;
		this.zPosition = var5;
	}

	public boolean isBoxInFrustum(double var1, double var3, double var5, double var7, double var9, double var11) {
		return this.clippingHelper.isBoxInFrustum(var1 - this.xPosition, var3 - this.yPosition, var5 - this.zPosition, var7 - this.xPosition, var9 - this.yPosition, var11 - this.zPosition);
	}

	public boolean isBoundingBoxInFrustum(AxisAlignedBB var1) {
		return this.isBoxInFrustum(var1.minX, var1.minY, var1.minZ, var1.maxX, var1.maxY, var1.maxZ);
	}
}
