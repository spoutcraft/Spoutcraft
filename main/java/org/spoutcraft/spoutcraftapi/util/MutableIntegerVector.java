package org.spoutcraft.spoutcraftapi.util;

public class MutableIntegerVector extends MutableVector {

	public MutableIntegerVector(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getIntX() {
		return (int) x;
	}

	public int getIntY() {
		return (int) y;
	}

	public int getIntZ() {
		return (int) z;
	}

	@Override
	public int hashCode() {
		int z = getIntZ();
		return getIntX() + (getIntY() << 24) + (z << 12) + (z >> 20);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MutableIntegerVector)) {
			return false;
		}
		MutableIntegerVector other = (MutableIntegerVector) o;

		return this.getIntX() == other.getIntX() && this.getIntY() == other.getIntY() && this.getIntZ() == other.getIntZ();

	}

}
