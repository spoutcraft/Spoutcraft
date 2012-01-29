package net.minecraft.src;

import java.util.HashSet;
import java.util.Set;

public class IntHashMap {
	private transient IntHashMapEntry slots[];
	private transient int count;
	private int threshold;
	private final float growFactor = 0.75F;
	private volatile transient int versionStamp;
	private Set keySet;

	public IntHashMap() {
		keySet = new HashSet();
		threshold = 12;
		slots = new IntHashMapEntry[16];
	}

	private static int computeHash(int i) {
		i ^= i >>> 20 ^ i >>> 12;
		return i ^ i >>> 7 ^ i >>> 4;
	}

	private static int getSlotIndex(int i, int j) {
		return i & j - 1;
	}

	public Object lookup(int i) {
		int j = computeHash(i);
		for (IntHashMapEntry inthashmapentry = slots[getSlotIndex(j, slots.length)]; inthashmapentry != null; inthashmapentry = inthashmapentry.nextEntry) {
			if (inthashmapentry.hashEntry == i) {
				return inthashmapentry.valueEntry;
			}
		}

		return null;
	}

	public boolean containsKey(int i) {
		return lookupEntry(i) != null;
	}

	final IntHashMapEntry lookupEntry(int i) {
		int j = computeHash(i);
		for (IntHashMapEntry inthashmapentry = slots[getSlotIndex(j, slots.length)]; inthashmapentry != null; inthashmapentry = inthashmapentry.nextEntry) {
			if (inthashmapentry.hashEntry == i) {
				return inthashmapentry;
			}
		}

		return null;
	}

	public void addKey(int i, Object obj) {
		keySet.add(Integer.valueOf(i));
		int j = computeHash(i);
		int k = getSlotIndex(j, slots.length);
		for (IntHashMapEntry inthashmapentry = slots[k]; inthashmapentry != null; inthashmapentry = inthashmapentry.nextEntry) {
			if (inthashmapentry.hashEntry == i) {
				inthashmapentry.valueEntry = obj;
			}
		}

		versionStamp++;
		insert(j, i, obj, k);
	}

	private void grow(int i) {
		IntHashMapEntry ainthashmapentry[] = slots;
		int j = ainthashmapentry.length;
		if (j == 0x40000000) {
			threshold = 0x7fffffff;
			return;
		}
		else {
			IntHashMapEntry ainthashmapentry1[] = new IntHashMapEntry[i];
			copyTo(ainthashmapentry1);
			slots = ainthashmapentry1;
			threshold = (int)((float)i * growFactor);
			return;
		}
	}

	private void copyTo(IntHashMapEntry ainthashmapentry[]) {
		IntHashMapEntry ainthashmapentry1[] = slots;
		int i = ainthashmapentry.length;
		for (int j = 0; j < ainthashmapentry1.length; j++) {
			IntHashMapEntry inthashmapentry = ainthashmapentry1[j];
			if (inthashmapentry == null) {
				continue;
			}
			ainthashmapentry1[j] = null;
			do {
				IntHashMapEntry inthashmapentry1 = inthashmapentry.nextEntry;
				int k = getSlotIndex(inthashmapentry.slotHash, i);
				inthashmapentry.nextEntry = ainthashmapentry[k];
				ainthashmapentry[k] = inthashmapentry;
				inthashmapentry = inthashmapentry1;
			}
			while (inthashmapentry != null);
		}
	}

	public Object removeObject(int i) {
		keySet.remove(Integer.valueOf(i));
		IntHashMapEntry inthashmapentry = removeEntry(i);
		return inthashmapentry != null ? inthashmapentry.valueEntry : null;
	}

	final IntHashMapEntry removeEntry(int i) {
		int j = computeHash(i);
		int k = getSlotIndex(j, slots.length);
		IntHashMapEntry inthashmapentry = slots[k];
		IntHashMapEntry inthashmapentry1;
		IntHashMapEntry inthashmapentry2;
		for (inthashmapentry1 = inthashmapentry; inthashmapentry1 != null; inthashmapentry1 = inthashmapentry2) {
			inthashmapentry2 = inthashmapentry1.nextEntry;
			if (inthashmapentry1.hashEntry == i) {
				versionStamp++;
				count--;
				if (inthashmapentry == inthashmapentry1) {
					slots[k] = inthashmapentry2;
				}
				else {
					inthashmapentry.nextEntry = inthashmapentry2;
				}
				return inthashmapentry1;
			}
			inthashmapentry = inthashmapentry1;
		}

		return inthashmapentry1;
	}

	public void clearMap() {
		versionStamp++;
		IntHashMapEntry ainthashmapentry[] = slots;
		for (int i = 0; i < ainthashmapentry.length; i++) {
			ainthashmapentry[i] = null;
		}

		count = 0;
	}

	private void insert(int i, int j, Object obj, int k) {
		IntHashMapEntry inthashmapentry = slots[k];
		slots[k] = new IntHashMapEntry(i, j, obj, inthashmapentry);
		if (count++ >= threshold) {
			grow(2 * slots.length);
		}
	}

	public Set getKeySet() {
		return keySet;
	}

	static int getHash(int i) {
		return computeHash(i);
	}
}
