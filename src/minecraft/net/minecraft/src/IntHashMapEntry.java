package net.minecraft.src;

class IntHashMapEntry {
	final int hashEntry;
	Object valueEntry;
	IntHashMapEntry nextEntry;
	final int slotHash;

	IntHashMapEntry(int i, int j, Object obj, IntHashMapEntry inthashmapentry) {
		valueEntry = obj;
		nextEntry = inthashmapentry;
		hashEntry = j;
		slotHash = i;
	}

	public final int getHash() {
		return hashEntry;
	}

	public final Object getValue() {
		return valueEntry;
	}

	public final boolean equals(Object obj) {
		if (!(obj instanceof IntHashMapEntry)) {
			return false;
		}
		IntHashMapEntry inthashmapentry = (IntHashMapEntry)obj;
		Integer integer = Integer.valueOf(getHash());
		Integer integer1 = Integer.valueOf(inthashmapentry.getHash());
		if (integer == integer1 || integer != null && integer.equals(integer1)) {
			Object obj1 = getValue();
			Object obj2 = inthashmapentry.getValue();
			if (obj1 == obj2 || obj1 != null && obj1.equals(obj2)) {
				return true;
			}
		}
		return false;
	}

	public final int hashCode() {
		return IntHashMap.getHash(hashEntry);
	}

	public final String toString() {
		return (new StringBuilder()).append(getHash()).append("=").append(getValue()).toString();
	}
}
