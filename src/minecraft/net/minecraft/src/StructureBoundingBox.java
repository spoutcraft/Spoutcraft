package net.minecraft.src;

public class StructureBoundingBox {
	public int minX;
	public int minY;
	public int minZ;
	public int maxX;
	public int maxY;
	public int maxZ;

	public StructureBoundingBox() {
	}

	public static StructureBoundingBox getNewBoundingBox() {
		return new StructureBoundingBox(0x7fffffff, 0x7fffffff, 0x7fffffff, 0x80000000, 0x80000000, 0x80000000);
	}

	public static StructureBoundingBox getComponentToAddBoundingBox(int i, int j, int k, int l, int i1, int j1, int k1, int l1,
	        int i2, int j2) {
		switch (j2) {
			default:
				return new StructureBoundingBox(i + l, j + i1, k + j1, ((i + k1) - 1) + l, ((j + l1) - 1) + i1, ((k + i2) - 1) + j1);

			case 2:
				return new StructureBoundingBox(i + l, j + i1, (k - i2) + 1 + j1, ((i + k1) - 1) + l, ((j + l1) - 1) + i1, k + j1);

			case 0:
				return new StructureBoundingBox(i + l, j + i1, k + j1, ((i + k1) - 1) + l, ((j + l1) - 1) + i1, ((k + i2) - 1) + j1);

			case 1:
				return new StructureBoundingBox((i - i2) + 1 + j1, j + i1, k + l, i + j1, ((j + l1) - 1) + i1, ((k + k1) - 1) + l);

			case 3:
				return new StructureBoundingBox(i + j1, j + i1, k + l, ((i + i2) - 1) + j1, ((j + l1) - 1) + i1, ((k + k1) - 1) + l);
		}
	}

	public StructureBoundingBox(StructureBoundingBox structureboundingbox) {
		minX = structureboundingbox.minX;
		minY = structureboundingbox.minY;
		minZ = structureboundingbox.minZ;
		maxX = structureboundingbox.maxX;
		maxY = structureboundingbox.maxY;
		maxZ = structureboundingbox.maxZ;
	}

	public StructureBoundingBox(int i, int j, int k, int l, int i1, int j1) {
		minX = i;
		minY = j;
		minZ = k;
		maxX = l;
		maxY = i1;
		maxZ = j1;
	}

	public StructureBoundingBox(int i, int j, int k, int l) {
		minX = i;
		minZ = j;
		maxX = k;
		maxZ = l;
		minY = 1;
		maxY = 512;
	}

	public boolean intersectsWith(StructureBoundingBox structureboundingbox) {
		return maxX >= structureboundingbox.minX && minX <= structureboundingbox.maxX && maxZ >= structureboundingbox.minZ && minZ <= structureboundingbox.maxZ && maxY >= structureboundingbox.minY && minY <= structureboundingbox.maxY;
	}

	public boolean isInsideStructureBB(int i, int j, int k, int l) {
		return maxX >= i && minX <= k && maxZ >= j && minZ <= l;
	}

	public void resizeBoundingBoxTo(StructureBoundingBox structureboundingbox) {
		minX = Math.min(minX, structureboundingbox.minX);
		minY = Math.min(minY, structureboundingbox.minY);
		minZ = Math.min(minZ, structureboundingbox.minZ);
		maxX = Math.max(maxX, structureboundingbox.maxX);
		maxY = Math.max(maxY, structureboundingbox.maxY);
		maxZ = Math.max(maxZ, structureboundingbox.maxZ);
	}

	public void offset(int i, int j, int k) {
		minX += i;
		minY += j;
		minZ += k;
		maxX += i;
		maxY += j;
		maxZ += k;
	}

	public boolean isVecInside(int i, int j, int k) {
		return i >= minX && i <= maxX && k >= minZ && k <= maxZ && j >= minY && j <= maxY;
	}

	public int getXSize() {
		return (maxX - minX) + 1;
	}

	public int getYSize() {
		return (maxY - minY) + 1;
	}

	public int getZSize() {
		return (maxZ - minZ) + 1;
	}

	public int func_40597_e() {
		return minX + ((maxX - minX) + 1) / 2;
	}

	public int func_40596_f() {
		return minY + ((maxY - minY) + 1) / 2;
	}

	public int func_40598_g() {
		return minZ + ((maxZ - minZ) + 1) / 2;
	}

	public String toString() {
		return (new StringBuilder()).append("(").append(minX).append(", ").append(minY).append(", ").append(minZ).append("; ").append(maxX).append(", ").append(maxY).append(", ").append(maxZ).append(")").toString();
	}
}
