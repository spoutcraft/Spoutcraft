package net.minecraft.src;

public class PathPoint {
	public final int xCoord;
	public final int yCoord;
	public final int zCoord;
	private final int hash;
	int index;
	float totalPathDistance;
	float distanceToNext;
	float distanceToTarget;
	PathPoint previous;
	public boolean isFirst;

	public PathPoint(int i, int j, int k) {
		index = -1;
		isFirst = false;
		xCoord = i;
		yCoord = j;
		zCoord = k;
		hash = func_22329_a(i, j, k);
	}

	public static int func_22329_a(int i, int j, int k) {
		return j & 0xff | (i & 0x7fff) << 8 | (k & 0x7fff) << 24 | (i >= 0 ? 0 : 0x80000000) | (k >= 0 ? 0 : 0x8000);
	}

	public float distanceTo(PathPoint pathpoint) {
		float f = pathpoint.xCoord - xCoord;
		float f1 = pathpoint.yCoord - yCoord;
		float f2 = pathpoint.zCoord - zCoord;
		return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
	}

	public boolean equals(Object obj) {
		if (obj instanceof PathPoint) {
			PathPoint pathpoint = (PathPoint)obj;
			return hash == pathpoint.hash && xCoord == pathpoint.xCoord && yCoord == pathpoint.yCoord && zCoord == pathpoint.zCoord;
		}
		else {
			return false;
		}
	}

	public int hashCode() {
		return hash;
	}

	public boolean isAssigned() {
		return index >= 0;
	}

	public String toString() {
		return (new StringBuilder()).append(xCoord).append(", ").append(yCoord).append(", ").append(zCoord).toString();
	}
}
