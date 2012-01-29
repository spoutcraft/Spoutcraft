package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class AxisAlignedBB {
	private static List boundingBoxes = new ArrayList();
	private static int numBoundingBoxesInUse = 0;
	public double minX;
	public double minY;
	public double minZ;
	public double maxX;
	public double maxY;
	public double maxZ;

	public static AxisAlignedBB getBoundingBox(double d, double d1, double d2, double d3,
	        double d4, double d5) {
		return new AxisAlignedBB(d, d1, d2, d3, d4, d5);
	}

	public static void clearBoundingBoxes() {
		boundingBoxes.clear();
		numBoundingBoxesInUse = 0;
	}

	public static void clearBoundingBoxPool() {
		numBoundingBoxesInUse = 0;
	}

	public static AxisAlignedBB getBoundingBoxFromPool(double d, double d1, double d2, double d3,
	        double d4, double d5) {
		if (numBoundingBoxesInUse >= boundingBoxes.size()) {
			boundingBoxes.add(getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D));
		}
		return ((AxisAlignedBB)boundingBoxes.get(numBoundingBoxesInUse++)).setBounds(d, d1, d2, d3, d4, d5);
	}

	private AxisAlignedBB(double d, double d1, double d2, double d3, double d4, double d5) {
		minX = d;
		minY = d1;
		minZ = d2;
		maxX = d3;
		maxY = d4;
		maxZ = d5;
	}

	public AxisAlignedBB setBounds(double d, double d1, double d2, double d3, double d4, double d5) {
		minX = d;
		minY = d1;
		minZ = d2;
		maxX = d3;
		maxY = d4;
		maxZ = d5;
		return this;
	}

	public AxisAlignedBB addCoord(double d, double d1, double d2) {
		double d3 = minX;
		double d4 = minY;
		double d5 = minZ;
		double d6 = maxX;
		double d7 = maxY;
		double d8 = maxZ;
		if (d < 0.0D) {
			d3 += d;
		}
		if (d > 0.0D) {
			d6 += d;
		}
		if (d1 < 0.0D) {
			d4 += d1;
		}
		if (d1 > 0.0D) {
			d7 += d1;
		}
		if (d2 < 0.0D) {
			d5 += d2;
		}
		if (d2 > 0.0D) {
			d8 += d2;
		}
		return getBoundingBoxFromPool(d3, d4, d5, d6, d7, d8);
	}

	public AxisAlignedBB expand(double d, double d1, double d2) {
		double d3 = minX - d;
		double d4 = minY - d1;
		double d5 = minZ - d2;
		double d6 = maxX + d;
		double d7 = maxY + d1;
		double d8 = maxZ + d2;
		return getBoundingBoxFromPool(d3, d4, d5, d6, d7, d8);
	}

	public AxisAlignedBB getOffsetBoundingBox(double d, double d1, double d2) {
		return getBoundingBoxFromPool(minX + d, minY + d1, minZ + d2, maxX + d, maxY + d1, maxZ + d2);
	}

	public double calculateXOffset(AxisAlignedBB axisalignedbb, double d) {
		if (axisalignedbb.maxY <= minY || axisalignedbb.minY >= maxY) {
			return d;
		}
		if (axisalignedbb.maxZ <= minZ || axisalignedbb.minZ >= maxZ) {
			return d;
		}
		if (d > 0.0D && axisalignedbb.maxX <= minX) {
			double d1 = minX - axisalignedbb.maxX;
			if (d1 < d) {
				d = d1;
			}
		}
		if (d < 0.0D && axisalignedbb.minX >= maxX) {
			double d2 = maxX - axisalignedbb.minX;
			if (d2 > d) {
				d = d2;
			}
		}
		return d;
	}

	public double calculateYOffset(AxisAlignedBB axisalignedbb, double d) {
		if (axisalignedbb.maxX <= minX || axisalignedbb.minX >= maxX) {
			return d;
		}
		if (axisalignedbb.maxZ <= minZ || axisalignedbb.minZ >= maxZ) {
			return d;
		}
		if (d > 0.0D && axisalignedbb.maxY <= minY) {
			double d1 = minY - axisalignedbb.maxY;
			if (d1 < d) {
				d = d1;
			}
		}
		if (d < 0.0D && axisalignedbb.minY >= maxY) {
			double d2 = maxY - axisalignedbb.minY;
			if (d2 > d) {
				d = d2;
			}
		}
		return d;
	}

	public double calculateZOffset(AxisAlignedBB axisalignedbb, double d) {
		if (axisalignedbb.maxX <= minX || axisalignedbb.minX >= maxX) {
			return d;
		}
		if (axisalignedbb.maxY <= minY || axisalignedbb.minY >= maxY) {
			return d;
		}
		if (d > 0.0D && axisalignedbb.maxZ <= minZ) {
			double d1 = minZ - axisalignedbb.maxZ;
			if (d1 < d) {
				d = d1;
			}
		}
		if (d < 0.0D && axisalignedbb.minZ >= maxZ) {
			double d2 = maxZ - axisalignedbb.minZ;
			if (d2 > d) {
				d = d2;
			}
		}
		return d;
	}

	public boolean intersectsWith(AxisAlignedBB axisalignedbb) {
		if (axisalignedbb.maxX <= minX || axisalignedbb.minX >= maxX) {
			return false;
		}
		if (axisalignedbb.maxY <= minY || axisalignedbb.minY >= maxY) {
			return false;
		}
		return axisalignedbb.maxZ > minZ && axisalignedbb.minZ < maxZ;
	}

	public AxisAlignedBB offset(double d, double d1, double d2) {
		minX += d;
		minY += d1;
		minZ += d2;
		maxX += d;
		maxY += d1;
		maxZ += d2;
		return this;
	}

	public boolean isVecInside(Vec3D vec3d) {
		if (vec3d.xCoord <= minX || vec3d.xCoord >= maxX) {
			return false;
		}
		if (vec3d.yCoord <= minY || vec3d.yCoord >= maxY) {
			return false;
		}
		return vec3d.zCoord > minZ && vec3d.zCoord < maxZ;
	}

	public double getAverageEdgeLength() {
		double d = maxX - minX;
		double d1 = maxY - minY;
		double d2 = maxZ - minZ;
		return (d + d1 + d2) / 3D;
	}

	public AxisAlignedBB contract(double d, double d1, double d2) {
		double d3 = minX + d;
		double d4 = minY + d1;
		double d5 = minZ + d2;
		double d6 = maxX - d;
		double d7 = maxY - d1;
		double d8 = maxZ - d2;
		return getBoundingBoxFromPool(d3, d4, d5, d6, d7, d8);
	}

	public AxisAlignedBB copy() {
		return getBoundingBoxFromPool(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public MovingObjectPosition calculateIntercept(Vec3D vec3d, Vec3D vec3d1) {
		Vec3D vec3d2 = vec3d.getIntermediateWithXValue(vec3d1, minX);
		Vec3D vec3d3 = vec3d.getIntermediateWithXValue(vec3d1, maxX);
		Vec3D vec3d4 = vec3d.getIntermediateWithYValue(vec3d1, minY);
		Vec3D vec3d5 = vec3d.getIntermediateWithYValue(vec3d1, maxY);
		Vec3D vec3d6 = vec3d.getIntermediateWithZValue(vec3d1, minZ);
		Vec3D vec3d7 = vec3d.getIntermediateWithZValue(vec3d1, maxZ);
		if (!isVecInYZ(vec3d2)) {
			vec3d2 = null;
		}
		if (!isVecInYZ(vec3d3)) {
			vec3d3 = null;
		}
		if (!isVecInXZ(vec3d4)) {
			vec3d4 = null;
		}
		if (!isVecInXZ(vec3d5)) {
			vec3d5 = null;
		}
		if (!isVecInXY(vec3d6)) {
			vec3d6 = null;
		}
		if (!isVecInXY(vec3d7)) {
			vec3d7 = null;
		}
		Vec3D vec3d8 = null;
		if (vec3d2 != null && (vec3d8 == null || vec3d.squareDistanceTo(vec3d2) < vec3d.squareDistanceTo(vec3d8))) {
			vec3d8 = vec3d2;
		}
		if (vec3d3 != null && (vec3d8 == null || vec3d.squareDistanceTo(vec3d3) < vec3d.squareDistanceTo(vec3d8))) {
			vec3d8 = vec3d3;
		}
		if (vec3d4 != null && (vec3d8 == null || vec3d.squareDistanceTo(vec3d4) < vec3d.squareDistanceTo(vec3d8))) {
			vec3d8 = vec3d4;
		}
		if (vec3d5 != null && (vec3d8 == null || vec3d.squareDistanceTo(vec3d5) < vec3d.squareDistanceTo(vec3d8))) {
			vec3d8 = vec3d5;
		}
		if (vec3d6 != null && (vec3d8 == null || vec3d.squareDistanceTo(vec3d6) < vec3d.squareDistanceTo(vec3d8))) {
			vec3d8 = vec3d6;
		}
		if (vec3d7 != null && (vec3d8 == null || vec3d.squareDistanceTo(vec3d7) < vec3d.squareDistanceTo(vec3d8))) {
			vec3d8 = vec3d7;
		}
		if (vec3d8 == null) {
			return null;
		}
		byte byte0 = -1;
		if (vec3d8 == vec3d2) {
			byte0 = 4;
		}
		if (vec3d8 == vec3d3) {
			byte0 = 5;
		}
		if (vec3d8 == vec3d4) {
			byte0 = 0;
		}
		if (vec3d8 == vec3d5) {
			byte0 = 1;
		}
		if (vec3d8 == vec3d6) {
			byte0 = 2;
		}
		if (vec3d8 == vec3d7) {
			byte0 = 3;
		}
		return new MovingObjectPosition(0, 0, 0, byte0, vec3d8);
	}

	private boolean isVecInYZ(Vec3D vec3d) {
		if (vec3d == null) {
			return false;
		}
		else {
			return vec3d.yCoord >= minY && vec3d.yCoord <= maxY && vec3d.zCoord >= minZ && vec3d.zCoord <= maxZ;
		}
	}

	private boolean isVecInXZ(Vec3D vec3d) {
		if (vec3d == null) {
			return false;
		}
		else {
			return vec3d.xCoord >= minX && vec3d.xCoord <= maxX && vec3d.zCoord >= minZ && vec3d.zCoord <= maxZ;
		}
	}

	private boolean isVecInXY(Vec3D vec3d) {
		if (vec3d == null) {
			return false;
		}
		else {
			return vec3d.xCoord >= minX && vec3d.xCoord <= maxX && vec3d.yCoord >= minY && vec3d.yCoord <= maxY;
		}
	}

	public void setBB(AxisAlignedBB axisalignedbb) {
		minX = axisalignedbb.minX;
		minY = axisalignedbb.minY;
		minZ = axisalignedbb.minZ;
		maxX = axisalignedbb.maxX;
		maxY = axisalignedbb.maxY;
		maxZ = axisalignedbb.maxZ;
	}

	public String toString() {
		return (new StringBuilder()).append("box[").append(minX).append(", ").append(minY).append(", ").append(minZ).append(" -> ").append(maxX).append(", ").append(maxY).append(", ").append(maxZ).append("]").toString();
	}
}
