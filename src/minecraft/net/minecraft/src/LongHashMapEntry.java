package net.minecraft.src;

class LongHashMapEntry {
	final long key;
	Object value;
	LongHashMapEntry nextEntry;
	final int field_35831_d;

	LongHashMapEntry(int i, long l, Object obj, LongHashMapEntry longhashmapentry) {
		value = obj;
		nextEntry = longhashmapentry;
		key = l;
		field_35831_d = i;
	}

	public final long func_35830_a() {
		return key;
	}

	public final Object func_35829_b() {
		return value;
	}

	public final boolean equals(Object obj) {
		if (!(obj instanceof LongHashMapEntry)) {
			return false;
		}
		LongHashMapEntry longhashmapentry = (LongHashMapEntry)obj;
		Long long1 = Long.valueOf(func_35830_a());
		Long long2 = Long.valueOf(longhashmapentry.func_35830_a());
		if (long1 == long2 || long1 != null && long1.equals(long2)) {
			Object obj1 = func_35829_b();
			Object obj2 = longhashmapentry.func_35829_b();
			if (obj1 == obj2 || obj1 != null && obj1.equals(obj2)) {
				return true;
			}
		}
		return false;
	}

	public final int hashCode() {
		return LongHashMap.getHashCode(key);
	}

	public final String toString() {
		return (new StringBuilder()).append(func_35830_a()).append("=").append(func_35829_b()).toString();
	}
}
